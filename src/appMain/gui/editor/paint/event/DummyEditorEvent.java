package appMain.gui.editor.paint.event;

import appMain.gui.editor.paint.TabPainter;

/**
 * An implementation of {@link EditorEvent} which does nothing
 * @author zrona
 */
public class DummyEditorEvent implements EditorEvent{

	/**
	 * Create a new {@link DummyEditorEvent}, this event does not need to do anything, 
	 * and is only in place as an easy way to cause an event with no action
	 */
	public DummyEditorEvent(){}
	
	/** Does nothing */
	@Override
	public boolean undo(TabPainter p){
		return true;
	}

	/** Does nothing */
	@Override
	public boolean redo(TabPainter p){
		return true;
	}

}
