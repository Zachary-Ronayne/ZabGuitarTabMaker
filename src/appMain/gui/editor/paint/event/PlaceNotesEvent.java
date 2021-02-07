package appMain.gui.editor.paint.event;

import java.util.Collection;

import appMain.gui.editor.paint.Selection;
import appMain.gui.editor.paint.SelectionList;
import appMain.gui.editor.paint.TabPainter;
import tab.TabPosition;

/**
 * An {@link EditorEvent} which acts as placing one or more {@link TabPosition} objects into a tab
 * @author zrona
 */
public class PlaceNotesEvent implements EditorEvent{
	
	/** Every {@link TabPosition}, located in a selection, which was placed on a tab */
	private SelectionList placed;
	
	/**
	 * Create a new {@link PlaceNotesEvent} with the given list of selections
	 * @param placed The selections, the list itself will not be stored, but each element in the list
	 */
	public PlaceNotesEvent(Collection<Selection> placed){
		this.placed = new SelectionList();
		this.placed.addAll(placed);
	}
	
	/**
	 * Create a new {@link PlaceNotesEvent} with only the given selection
	 * @param s The selections
	 */
	public PlaceNotesEvent(Selection s){
		this(new SelectionList());
		this.placed.add(new Selection(s.getPos().copy(), s.getString(), s.getStringIndex()));
	}
	
	/** @return See {@link #placed} */
	public SelectionList getPlaced(){
		return this.placed;
	}
	
	/** Remove all of the notes that this event placed */
	@Override
	public boolean undo(TabPainter p){
		return this.placed.isEmpty() || !p.removeSelections(this.placed).isEmpty();
	}
	
	/** Place all of the notes that this event originally placed */
	@Override
	public boolean redo(TabPainter p){
		return this.placed.isEmpty() || p.placeNotes(this.placed);
	}

}
