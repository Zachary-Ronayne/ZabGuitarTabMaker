package appMain.gui.editor.paint.event;

import appMain.gui.editor.paint.SelectionList;
import appMain.gui.editor.paint.TabPainter;

/**
 * A {@link RemovePlaceNotesEvent} which also resets the current selected tab number when the undo or redo occurs. 
 * This is designed for use with {@link TabPainter#appendSelectedTabNum(char, boolean)}
 * @author zrona
 */
public class SelectedTabNumberEvent extends RemovePlaceNotesEvent{
	
	/**
	 * Create a new {@link SelectedTabNumberEvent} with the given selections for placed and removed.
	 * @param placed The notes placed during this event, i.e. the selections containing the new values
	 * @param removed The notes removed during this event, i.e. the selections containing the old values
	 */
	public SelectedTabNumberEvent(SelectionList placed, SelectionList removed){
		super(placed, removed);
	}
	
	/**
	 * Replace all notes effected back to their old positions from before this event,
	 * and clear the selection, resetting the selectedNewTabNum
	 */
	@Override
	public boolean undo(TabPainter p){
		p.clearSelection();
		return super.undo(p);
	}

	/**
	 * Replace all notes effected back to their new positions from this event,
	 * and clear the selection, resetting the selectedNewTabNum
	 */
	@Override
	public boolean redo(TabPainter p){
		p.clearSelection();
		return super.redo(p);
	}

}
