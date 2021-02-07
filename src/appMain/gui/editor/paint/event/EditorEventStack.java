package appMain.gui.editor.paint.event;

import java.util.LinkedList;

import appMain.gui.editor.paint.TabPaintController;
import appMain.gui.editor.paint.TabPainter;
import appUtils.ZabAppSettings;

/**
 * An object which keeps track of the changes made to a {@link TabPainter} for undo and redo actions
 * @author zrona
 */
public class EditorEventStack extends TabPaintController{
	
	/**
	 * The stack which contains all of the events stored in the undo
	 */
	private LinkedList<EditorEvent> undoStack;
	
	/** 
	 * The stack which keeps track of the elements to be redone.
	 * When a new event is added to the stack, all elements in this list are removed.
	 * When a redo occurs, this stack is popped, and that event is redone, then added to the undo stack.
	 */
	private LinkedList<EditorEvent> redoStack;
	
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
		
		this.undoStack = new LinkedList<>();
		this.redoStack = new LinkedList<>();
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

	/** @return true if the stack has no elements, false otherwise */
	public boolean isEmpty(){
		return this.undoStack.isEmpty() && this.redoStack.isEmpty();
	}
	
	/**
	 * Find the number of events in the stack
	 * @return The total number of events
	 */
	public int size(){
		return this.undoSize() + this.redoSize();
	}
	
	/**
	 * Find the number of events ready to be undone
	 * @return The number of events
	 */
	public int undoSize(){
		return this.undoStack.size();
	}
	
	/**
	 * Find the number of events ready to be redone
	 * @return The number of events
	 */
	public int redoSize(){
		return this.redoStack.size();
	}
	
	/**
	 * Remove all of the events stored in the stack
	 */
	public void clearStack(){
		this.undoStack.clear();
		this.redoStack.clear();
	}
	
	/**
	 * Determine if this stack has the given {@link EditorEvent}. 
	 * This method performs a linear search on a {@link LinkedList} implementation of a stack, 
	 * be careful with using this with execution time.
	 * @param e The event to look for
	 * @return true if it is contained, false otherwise
	 */
	public boolean contains(EditorEvent e){
		return this.undoStack.contains(e) || this.redoStack.contains(e);
	}
	
	/**
	 * Add the given {@link EditorEvent} to the stack. This method does nothing to {@link #getPainter()}, 
	 * it only adds the event to the stack, and marks the stack as not saved
	 * @param e The editor event to add
	 * @return true if the event was added to the stack and no events were removed, false otherwise
	 */
	public boolean addEvent(EditorEvent e){
		int size = this.size();
		int maxSize = ZabAppSettings.get().paint().maxUndo();
		/*
		 *  Determine if this stack is maxed out or not
		 *  It is maxed out if the maximum size is zero, 
		 *  	or if the maximum size is greater than zero, 
		 *  	and the size is greater than or equal to the max size
		 */
		boolean maxed = maxSize == 0 || maxSize > 0 && size >= maxSize;
		// If there is a max size, remove events until it is at least one below the max size
		if(maxSize >= 0){
			while(this.undoSize() >= maxSize && this.undoSize() > 0) this.undoStack.removeLast();
		}
		// Add the normal event if the stack is allowed to hold events
		if(maxSize != 0) this.undoStack.push(e);
		
		// Clear out the redo stack, as adding a new event removes the ability to redo
		this.redoStack.clear();
		
		// Mark the stack as not saved and return whether or not no events were removed
		this.markNotSaved();
		return !maxed;
	}
	
	/**
	 * Undo the last action which was on the stack, or do nothing if the stack is empty
	 * @return true if action was undone, false otherwise
	 */
	public boolean undo(){
		// If nothing is recorded in the undo stack, do nothing
		if(this.undoStack.isEmpty()) return false;
		
		// Get the most recent undo event and take it off the stack
		EditorEvent e = this.undoStack.pop();
		
		// Push it on the redo stack
		this.redoStack.push(e);
		
		// Perform the undo and return its success
		return e.undo(this.getPainter());
	}
	
	/**
	 * Redo the last undone action which was on the stack, or do nothing no actions are available to be redone
	 * @return true if action was redone, false otherwise
	 */
	public boolean redo(){
		// If nothing is recorded in the redo stack, do nothing
		if(this.redoStack.isEmpty()) return false;
		
		// Get the most recent redo event and take it off the stack
		EditorEvent e = this.redoStack.pop();
		
		// Push it on the undo stack
		this.undoStack.push(e);
		
		// Perform the redo and return its success
		return e.redo(this.getPainter());
	}
	
}
