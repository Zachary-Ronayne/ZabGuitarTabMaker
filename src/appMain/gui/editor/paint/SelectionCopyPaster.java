package appMain.gui.editor.paint;

import appMain.gui.editor.paint.event.RemovePlaceNotesEvent;
import appUtils.ZabAppSettings;
import appUtils.settings.TabControlSettings;
import tab.Tab;
import tab.TabPosition;

/**
 * A class which handles tracking a some {@link TabPosition} objects from a {@link TabPainter}, and allowing them to later be placed
 * @author zrona
 */
public class SelectionCopyPaster extends SelectionPlacer{

	/** The {@link Selection} where the copy took place. Can be null if no copy is selected */
	private Selection baseSelection;
	
	/**
	 * Create a new {@link SelectionCopyPaster} with a default state
	 * @param painter
	 */
	public SelectionCopyPaster(TabPainter painter){
		super(painter);
		this.baseSelection = null;
	}
	
	/** @return See {@link #baseSelection} */
	public Selection getBaseSelection(){
		return this.baseSelection;
	}
	
	/**
	 * Remove the current selection in the clip board so that nothing is remembered.
	 */
	@Override
	public void reset(){
		this.baseSelection = null;
		super.reset();
	}
	
	/**
	 * Put the current selection of {@link #getPainter()} into the copy paste clip board. 
	 * The clip board is only placed in this object, this does not do anything to the OS clip board. 
	 * @return true if at least one note was copied, false otherwise
	 */
	public boolean runCopy(){
		TabPainter paint = this.getPainter();
		boolean success = this.select(paint.getSelected());
		
		// If a selection is made, that means at least one note was selected
		if(success) this.baseSelection = paint.getSelected().get(0);
		return success;
	}
	
	/**
	 * Paste the currently copied notes onto {@link #getPainter()}, without recording undo. 
	 * The position of the placement is based on the mouse position
	 * @param mX The x painter coordinate to paste the selection, usually a mouse position
	 * @param mY The y painter coordinate to paste the selection, usually a mouse position
	 * @param keepPitch Only applies when the note uses pitch. Use true to make the pitch stay the same, 
	 * i.e. the fret number will change if the strings have a different tuning, 
	 * 	use false to make the fret number stay the same, modifying the pitch if necessary
	 * @param recordUndo true to record this action in {@link TabPainter#undoStack}, false otherwise 
	 * @return true if at least one note was pasted, false otherwise
	 */
	public boolean paste(double mX, double mY, boolean keepPitch, boolean recordUndo){
		// If the painter tab is null, fail the drag
		TabPainter paint = this.getPainter();
		Tab t = paint.getTab();
		Selection base = this.getBaseSelection();
		if(t == null) return false;
		int stringNum = paint.pixelYToStringNum(mY);
		
		// If the position to place is not on a valid line of tab, or a the base position isn't selected, fail the drag
		if(paint.xToTabPos(mX, mY) < 0 || stringNum < 0 || base == null) return false;
		
		// Find the position where the note will be placed
		double quantizedPos = paint.quantizedTabPos(mX, mY);
		
		TabControlSettings settings = ZabAppSettings.get().control();
		boolean overwrite = settings.pasteOverwrite();
		boolean cancelInvalid = settings.pasteCancelInvalid();
		
		RemovePlaceNotesEvent event = super.place(
				base.getPosition(), quantizedPos,
				base.getStringIndex(), stringNum,
				keepPitch,
				false, overwrite, cancelInvalid);
		
		// If recording undo, and a note was placed, add it to the undo stack
		boolean madeAction = event != null;
		if(recordUndo && madeAction) paint.getUndoStack().addEvent(event);
		return madeAction;
	}
	
	/**
	 * Paste the currently copied notes onto {@link #getPainter()}, without recording undo. 
	 * The position of the placement is based on the mouse position
	 * @param mX The x painter coordinate to paste the selection, usually a mouse position
	 * @param mY The y painter coordinate to paste the selection, usually a mouse position
	 * @param keepPitch Only applies when the note uses pitch. Use true to make the pitch stay the same, 
	 * i.e. the fret number will change if the strings have a different tuning, 
	 * 	use false to make the fret number stay the same, modifying the pitch if necessary
	 * @return true if at least one note was pasted, false otherwise
	 */
	public boolean paste(double mX, double mY, boolean keepPitch){
		return this.paste(mX, mY, keepPitch, false);
	}

}
