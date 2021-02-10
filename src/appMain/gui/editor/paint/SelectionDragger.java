package appMain.gui.editor.paint;

import java.awt.geom.Point2D;

import appMain.gui.editor.paint.event.RemovePlaceNotesEvent;
import appMain.gui.util.Camera;
import appUtils.ZabAppSettings;
import appUtils.settings.TabControlSettings;
import tab.Tab;

/**
 * A class used by {@link TabPainter} to keep track of and control moving a selection of notes around
 * @author zrona
 */
public class SelectionDragger extends SelectionPlacer{
	
	/** The {@link Selection} which was clicked to initialize a drag, null if nothing is selected */
	private Selection baseSelection;
	
	/** The point where this {@link #draggedTab} should be drawn, null if no point is selected */
	private Point2D.Double dragPoint;
	/** 
	 * The camera coordinates of the mouse click when the drag is initiated. 
	 * This is used, when rendering the selection to line up the selection to where it was with the mouse when the selection was initiated
	 */
	private Point2D.Double dragAnchorPoint;
	
	/**
	 * Create a new {@link SelectionDragger} which is used by the given {@link TabPainter}
	 * @param painter See {@link #painter}
	 */
	public SelectionDragger(TabPainter painter){
		super(painter);
		this.reset();
	}
	
	/** @return See {@link #baseSelection} */
	public Selection getBaseSelection(){
		return this.baseSelection;
	}
	/** @param baseSelection See {@link #baseSelection} */
	public void setBaseSelection(Selection baseSelection){
		this.baseSelection = baseSelection;
	}
	
	/** @return See {@link #dragPoint} */
	public Point2D.Double getDragPoint(){
		return this.dragPoint;
	}
	/** @param dragPoint See {@link #dragPoint} */
	public void setDragPoint(Point2D.Double dragPoint){
		this.dragPoint = dragPoint;
	}
	/** @return See {@link #dragAnchorPoint} */
	public Point2D.Double getAnchorPoint(){
		return this.dragAnchorPoint;
	}
	/** @param anchorPoint See {@link #dragAnchorPoint} */
	public void setAnchorPoint(Point2D.Double anchorPoint){
		this.dragAnchorPoint = anchorPoint;
	}
	
	/**
	 * Cancel the current drag selection
	 */
	public void reset(){
		super.reset();
		this.setBaseSelection(null);
		this.setDragPoint(null);
		this.dragAnchorPoint = null;
	}
	
	/** @return true if a selection drag has been initialized, false otherwise */
	public boolean isDragging(){
		return this.getBaseSelection() != null;
	}
	
	/**
	 * Begin dragging selected notes based on the given position. 
	 * Will not initialize a drag if the given position does not land on a symbol.
	 * @param mX The x coordinate to base the drag, usually a mouse position
	 * @param mY The y coordinate to base the drag, usually a mouse position
	 * @return true if the call began the drag, false otherwise
	 */
	public boolean begin(double mX, double mY){
		TabPainter paint = this.getPainter();
		
		// If a drag is in progress, do not begin a new one
		if(this.isDragging()) return false;
		
		Selection s = paint.findPosition(mX, mY);
		// If a selected note was not found, do nothing
		if(s == null || !paint.getSelected().isSelected(s)) return false;

		// Set the base selection
		this.setBaseSelection(s);
		
		// Set up the anchor point for dragging
		Camera cam = paint.getCamera();
		this.dragAnchorPoint = new Point2D.Double(cam.toCamX(mX), cam.toCamY(mY));
		
		// Otherwise begin the drag by performing a selection with the selected notes of the painter
		return this.select(paint.getSelected());
	}
	
	/**
	 * Update the state of the dragging position to the given coordinates.
	 * @param mX The x coordinate of the drag, usually a mouse position
	 * @param mY The y coordinate of the drag, usually a mouse position
	 * @return true if the update happened, false otherwise
	 */
	public boolean update(double mX, double mY){
		// If dragging has not begun, don't do anything
		if(!this.isDragging()) return false;
		
		// Set the dragging point to the given coordinates
		Camera pCam = this.getPainter().getCamera();
		this.setDragPoint(new Point2D.Double(pCam.toCamX(mX), pCam.toCamY(mY)));
		
		return true;
	}
	
	/**
	 * End the selection drag, placing the current selection based on the given position. 
	 * Depending on settings, may not move, or may delete parts, of the selection if the given position cannot place the selection
	 * @param mX The x painter coordinate to place the selection, usually a mouse position
	 * @param mY The y painter coordinate to place the selection, usually a mouse position
	 * @param keepPitch Only applies when the note uses pitch. Use true to make the pitch stay the same, 
	 * i.e. the fret number will change if the strings have a different tuning, 
	 * 	use false to make the fret number stay the same, modifying the pitch if necessary
	 * @param recordUndo true to record this action in {@link TabPainter#undoStack}, false otherwise 
	 * @return true if the selection was moved, false otherwise
	 */
	public boolean place(double mX, double mY, boolean keepPitch, boolean recordUndo){
		// If the painter tab is null, fail the drag
		TabPainter paint = this.getPainter();
		Tab t = paint.getTab();
		Selection base = this.getBaseSelection();
		if(t == null) return false;
		int stringNum = paint.pixelYToStringNum(mY);
		
		// If the position to place is not on a valid line of tab, or a the base position isn't selected, fail the drag
		if(paint.xToTabPos(mX, mY) < 0 || stringNum < 0 || base == null) return false;
		
		// Find the position where the note will be placed
		double quantizedPos = paint.quanitzedTabPos(mX, mY);
		
		// Remove the selections of this dragger and store them
		SelectionList removed = paint.removeSelections(this.getSelectedList());
		
		// Get the settings
		TabControlSettings settings = ZabAppSettings.get().control();
		boolean overwrite = settings.moveOverwrite();
		boolean deleteInvalid = settings.moveDeleteInvalid();
		boolean cancelInvalid = settings.cancelInvalid();

		// Call the main place method
		RemovePlaceNotesEvent event = super.place(
				base.getPosition(), quantizedPos,
				base.getStringIndex(), stringNum,
				keepPitch,
				!deleteInvalid, overwrite, cancelInvalid);
		
		// If the placement failed, then put back all of the removed notes, and return failure
		if(event == null){
			paint.placeAndSelect(removed);
			return false;
		}
		// Otherwise, add the notes that this method removed, to the event's removed list
		else{
			// If the any of the added notes are the same as the ones which where initially removed, take them out of the event
			SelectionList eventAdded = event.getPlaced();
			for(int i = 0; i < eventAdded.size(); i++){
				if(removed.contains(eventAdded.get(i))){
					eventAdded.remove(i);
					i--;
				}
			}
			
			// If any of the initially removed notes are in the tab, take them out of the removed list
			for(int i = 0; i < removed.size(); i++){
				Selection s = removed.get(i);
				if(s.getString().contains(s.getPos())){
					removed.remove(i);
					i--;
				}
			}
			
			// Add applicable notes which were removed
			event.getRemoved().addAll(removed);
		}

		// Determine if at least some notes were placed
		boolean madeAction = !event.getPlaced().isEmpty();
		
		// If recording undo, and an action occurred, add the event
		if(madeAction && recordUndo) paint.getUndoStack().addEvent(event);

		// Return if the method did anything
		return madeAction;
	}
	
	/**
	 * End the selection drag, placing the current selection based on the given position. 
	 * Depending on settings, may not move, or may delete parts, of the selection if the given position cannot place the selection
	 * @param mX The x painter coordinate to place the selection, usually a mouse position
	 * @param mY The y painter coordinate to place the selection, usually a mouse position
	 * @param keepPitch Only applies when the note uses pitch. Use true to make the pitch stay the same, 
	 * i.e. the fret number will change if the strings have a different tuning, 
	 * 	use false to make the fret number stay the same, modifying the pitch if necessary
	 * @return true if the selection was moved, false otherwise
	 */
	public boolean place(double mX, double mY, boolean keepPitch){
		return this.place(mX, mY, keepPitch, false);
	}
	
	/**
	 * Draw the currently being dragged TabPositions to the {@link TabPainter} using this {@link SelectionDragger}.<br>
	 * This method does not handle setting color, font, etc those must be set by the method calling this method
	 * @return true if the draw took place, false otherwise
	 */
	public boolean draw(){
		if(!this.isDragging() || this.getSelectedTab() == null) return false;
		
		TabPainter paint = this.getPainter();
		Point2D.Double p = this.getDragPoint();
		Point2D.Double anchor = this.getAnchorPoint();
		if(p == null || anchor == null) return false;
		return paint.drawSymbols(this.getSelectedTab(), p.getX() - anchor.getX(), p.getY() - anchor.getY());
	}
}
