package appMain.gui.editor.paint.event;

import appMain.gui.editor.paint.Selection;
import appMain.gui.editor.paint.SelectionList;
import appMain.gui.editor.paint.TabPainter;

/**
 * An {@link EditorEvent} which simultaneously removes and places notes
 * @author zrona
 */
public class RemovePlaceNotesEvent implements EditorEvent{
	
	/** The event keeping track of the notes that were placed during this event */
	private PlaceNotesEvent placed;
	/** The event keeping track of the notes that were removed during this event */
	private RemoveNotesEvent removed;
	
	/**
	 * Create a new {@link RemovePlaceNotesEvent} with the given selections
	 * @param placed The list of selections which were placed during the event
	 * @param removed The list of selections which were removed during the event
	 */
	public RemovePlaceNotesEvent(SelectionList placed, SelectionList removed){
		this.placed = new PlaceNotesEvent(placed);
		this.removed = new RemoveNotesEvent(removed);
	}
	
	/**
	 * Create a new {@link RemovePlaceNotesEvent} with the given selections
	 * @param placed The {@link Selection} which was placed during the event
	 * @param removed The {@link Selection} which was removed during the event
	 */
	public RemovePlaceNotesEvent(Selection placed, Selection removed){
		this.placed = new PlaceNotesEvent(placed);
		this.removed = new RemoveNotesEvent(removed);
	}
	
	/**
	 * Get the {@link SelectionList} holding all of the {@link Selection} objects which were placed by this event. 
	 * Selections can be added to the returned list to update the event.
	 * @return The list
	 */
	public SelectionList getPlaced(){
		return this.placed.getPlaced();
	}
	
	/**
	 * Get the {@link SelectionList} holding all of the {@link Selection} objects which were removed by this event. 
	 * Selections can be added to the returned list to update the event.
	 * @return The list
	 */
	public SelectionList getRemoved(){
		return this.removed.getPlaced();
	}
	
	/**
	 * First remove all of the notes that were placed as a part of this event,
	 * and then place all of the notes removed as a part of this event
	 */
	@Override
	public boolean undo(TabPainter p){
		// USing & and not && because both calls need to happen regardless of if the first fails
		return this.placed.undo(p) & this.removed.undo(p);
	}

	/**
	 * First remove all of the notes that were removed as a part of this event,
	 * and then place all of the notes placed as a part of this event
	 */
	@Override
	public boolean redo(TabPainter p){
		// USing & and not && because both calls need to happen regardless of if the first fails
		return this.removed.redo(p) & this.placed.redo(p);
	}

}
