package appMain.gui.editor.paint.event;

import appMain.gui.editor.paint.TabPainter;

/***
 * An which keeps track of an event which took place in a {@link TabPainter}, 
 * and should be able to undo and redo the action
 * @author zrona
 */
public interface EditorEvent{
	/**
	 * Undo the action of this EditorEvent on the given {@link TabPainter}. 
	 * i.e. if this action was to place an object, then this method should remove that object.<br>
	 * It is assumed that when this method is called, the TabPainter is in an appropriate 
	 * state to perform the undo.
	 * @param p The painter
	 */
	public void undo(TabPainter p);
	
	/**
	 * Redo the action of this EditorEvent on the given {@link TabPainter}. 
	 * i.e. if this action was to place an object, then this method should place that object.<br>
	 * It is assumed that when this method is called, the TabPainter is in an appropriate 
	 * state to perform the redo.
	 * @param p The painter
	 */
	public void redo(TabPainter p);
	
}
