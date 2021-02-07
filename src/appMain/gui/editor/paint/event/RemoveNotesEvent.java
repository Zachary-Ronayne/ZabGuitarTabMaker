package appMain.gui.editor.paint.event;

import appMain.gui.editor.paint.Selection;
import appMain.gui.editor.paint.SelectionList;
import appMain.gui.editor.paint.TabPainter;

/**
 * An {@link EditorEvent} which does the opposite of {@link PlaceNotesEvent}, 
 * i.e. use undo to put the {@link SelectionList} back into the {@link TabPainter}, and use redo to remove the notes
 * @author zrona
 */
public class RemoveNotesEvent extends PlaceNotesEvent{
	
	/**
	 * Create a new {@link RemoveNotesEvent} with the given list of selections
	 * @param placed The list of selections, the object itself to be stored
	 */
	public RemoveNotesEvent(SelectionList list){
		super(list);
	}
	
	/**
	 * Create a new {@link RemoveNotesEvent} with only the given selection
	 * @param s The selections
	 */
	public RemoveNotesEvent(Selection s){
		super(s);
	}
	
	/** Place all of the notes this event removed */
	@Override
	public boolean undo(TabPainter p){
		return super.redo(p);
	}
	
	/** Remove all of the notes this event originally removed */
	@Override
	public boolean redo(TabPainter p){
		return super.undo(p);
	}

}
