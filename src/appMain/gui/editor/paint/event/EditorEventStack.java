package appMain.gui.editor.paint.event;

import appMain.gui.editor.paint.TabPaintController;
import appMain.gui.editor.paint.TabPainter;

/**
 * An object which keeps track of the changes made to a {@link TabPainter} for undo and redo actions
 * @author zrona
 */
public class EditorEventStack extends TabPaintController{
	
	/**
	 * A flag that keeps track of if the editor was saved since a new event occurred. 
	 * Essentially, every event applied to this stack, or when an element is modified in the stack, 
	 * this flag is set to false. This flag should be set to true if the actions have been saved.
	 */
	private boolean saved;
	
	/**
	 * Create an empty stack which is used by the given painter
	 * @param painter The painter using the stack
	 */
	public EditorEventStack(TabPainter painter){
		super(painter);
		
		this.saved = false;
		
		// TODO implement undo/redo stack
	}
	
	/**
	 * Set {@link #saved} to true, i.e. mark that the events have been saved.
	 */
	public void markSaved(){
		this.saved = true;
	}
	
	/**
	 * Set {@link #saved} to false, i.e. mark that the events have not been saved.
	 */
	public void markNotSaved(){
		this.saved = false;
	}
	
	/** @return See {@link #saved} */
	public boolean isSaved(){
		return this.saved;
	}
	
	/**
	 * Add the given {@link EditorEvent} to the stack. This method does nothing to {@link #getPainter()}, 
	 * it only adds the event to the stack, and marks the stack as not saved
	 * @param e
	 */
	public void addEvent(EditorEvent e){
		this.saved = false;
	}
	
}
