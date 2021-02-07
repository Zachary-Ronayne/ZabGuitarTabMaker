package appMain.gui.editor.paint;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;

import appMain.gui.util.Camera;
import appUtils.ZabAppSettings;
import appUtils.settings.TabControlSettings;
import tab.TabPosition;

/**
 * A class used by {@link TabPainter} to control mouse input. Also tracks the most recent mouse positions
 * @author zrona
 */
public class EditorMouse extends TabPaintController implements MouseListener, MouseMotionListener, MouseWheelListener{
	
	/** The last x painter coordinate the mouse was located during any event */
	private double lastX;
	
	/** The last y painter coordinate the mouse was located during any event */
	private double lastY;
	
	/**
	 * The {@link Selection} which needs to be deselected if the mouse is released over that {@link Selection}.  
	 * Will be null when no {@link Selection} needs to be deselected
	 */
	private Selection toDeselect;
	
	/**
	 * Create a default state {@link EditorMouse}
	 * @param paint See {@link TabPaintController#paint}
	 */
	public EditorMouse(TabPainter paint){
		super(paint);
		// Initializing coordinates to be negative so that they are not in the positive bounds of the painter
		this.lastX = -1000;
		this.lastY = -1000;
		this.toDeselect = null;
	}
	
	/** @return See {@link #lastX} */
	public double x(){
		return this.lastX;
	}
	/** @return See {@link #lastY} */
	public double y(){
		return this.lastY;
	}
	
	/**
	 * Update the stored position of the last x and y mouse positions
	 * @param e The event to use to update the position
	 */
	public void updateLastPos(MouseEvent e){
		double x = e.getX();
		double y = e.getY();
		this.lastX = x;
		this.lastY = y;
	}
	
	/**
	 * Regardless of what button is pressed, give focus to the window when the mouse clicks, 
	 * and repaint the {@link TabPainter} of this {@link EditorMouse}
	 */
	@Override
	public void mousePressed(MouseEvent e){
		TabPainter paint = this.getPainter();
		this.updateLastPos(e);
		
		switch(e.getButton()){
			case MouseEvent.BUTTON1: this.leftMousePressed(e); break;
			case MouseEvent.BUTTON2: this.middleMousePressed(e); break;
			case MouseEvent.BUTTON3: this.rightMousePressed(e); break;
		}

		paint.requestFocusInWindow();
		
		paint.repaint();
	}
	
	/**
	 * Called when the left mouse button is pressed. 
	 * Attempt to select a note, if possible, if shift is held down select a line. 
	 * If ctrl is held down, add to the selection, otherwise, only select the one note. 
	 * If the selection fails, then try to deselect a note. 
	 * Also enable clicking and dragging for a selection box. 
	 * @param e The mouse event, it is assumed this event is for the correct mouse button
	 */
	public void leftMousePressed(MouseEvent e){
		TabPainter paint = this.getPainter();
		double x = e.getX();
		double y = e.getY();
		
		SelectionBox box = paint.getSelectionBox();
		SelectionDragger drag = paint.getDragger();
		
		// Find the Selection which was clicked on
		Selection clickedPosition = paint.findPosition(x, y);
		// Determine if the Selection had a TabPosition 
		boolean mouseOnPosition = clickedPosition != null;
		// If the TabPosition exists and is selected before the click, then it should be deselected after the click
		boolean shouldDeselect = mouseOnPosition && paint.getSelected().isSelected(clickedPosition);

		/*
		 * If the selection is currently not dragging, 
		 * the selection box is not currently making a selection, 
		 * and there is at least one selected note, 
		 * attempt to begin a drag. 
		 * This case is needed to account for when multiple notes are selected, and 
		 * the user wants to click and drag all of those notes
		 */
		if(!drag.isDragging() && !box.isSelecting() && !paint.getSelected().isEmpty()){
			drag.begin(x, y);
		}
		
		// If a drag was not otherwise started, do a normal click
		if(!drag.isDragging()){
			// If the mouse is on a TabPosition, then select or deselect it when applicable
			if(mouseOnPosition){
				boolean selectLine = e.isShiftDown();
				// If shift is held down, select a line of TabPositions
				if(selectLine) paint.selectLine(x, y, e.isControlDown());
				// Otherwise, try to select a single TabPosition
				else paint.selectNote(x, y, e.isControlDown());
				
				// Begin a drag, it exists by being in this if chain, thus it can be moved
				drag.begin(x, y);
			}
			// Otherwise, begin a box selection
			else{
				box.setSelecting(true);
				box.updateSelectionBox(x, y);
			}
		}
		
		// If the note was selected before this click, then set it up to be deselected
		if(shouldDeselect) this.toDeselect = paint.findPosition(x, y);
	}
	
	/**
	 * When the middle mouse button is pressed, it sets an anchor point.
	 * @param e The mouse event, it is assumed this event is for the correct mouse button
	 */
	public void middleMousePressed(MouseEvent e){
		TabPainter paint = this.getPainter();
		Camera cam = paint.getCamera();
		double x = e.getX();
		double y = e.getY();
		cam.setAnchor(x, y);
	}

	/**
	 * When the right mouse button is pressed, place a note on the tab, if the mouse is close enough.
	 * @param e The mouse event, it is assumed this event is for the correct mouse button
	 */
	public void rightMousePressed(MouseEvent e){
		TabPainter paint = this.getPainter();
		double x = e.getX();
		double y = e.getY();
		paint.placeNote(x, y, 0, true);
	}
	
	/**
	 * Regardless of what button is pressed, give focus to the window when the mouse clicks, 
	 * and repaint the {@link TabPainter} of this {@link EditorMouse}
	 */
	@Override
	public void mouseReleased(MouseEvent e){
		TabPainter paint = this.getPainter();
		this.updateLastPos(e);
		
		switch(e.getButton()){
			case MouseEvent.BUTTON1: this.leftMouseReleased(e); break;
			case MouseEvent.BUTTON2: this.middleMouseReleased(e); break;
			case MouseEvent.BUTTON3: this.rightMouseReleased(e); break;
		}

		paint.repaint();
	}
	
	/**
	 * When the left mouse button is released, remove the selection box and add the selected {@link TabPosition} objects, 
	 * if control is held down, add to the selection.<br>
	 * If a not is set to be deselected, and the mouse is still on that position, deselect it
	 * @param e The mouse event, it is assumed this event is for the correct mouse button
	 */
	public void leftMouseReleased(MouseEvent e){
		TabPainter paint = this.getPainter();
		Camera cam = paint.getCamera();
		SelectionBox box = paint.getSelectionBox();
		box.selectInPainter(e.isControlDown());
		box.clear();
		SelectionDragger drag = paint.getDragger();
		double x = e.getX();
		double y = e.getY();

		if(drag.isDragging()){
			drag.update(x, y);
			drag.place(x, y, e.isShiftDown(), true);
			drag.reset();
		}
		
		/*
		 *  Deselect the TabPosition to deselect if:
		 *  There is a selection to deselect, 
		 *  it is the position on the mouse, 
		 *  And the bounds TabPosition to deselect contain the mouse
		 */
		if(this.toDeselect != null){
			// Checking the selections are equal
			if(this.toDeselect.equals(paint.findPosition(x, y))){
				// Finding the compatible bounds for comparing the bounds of the TabPosition to the mouse
				Rectangle2D bounds = paint.symbolBounds(this.toDeselect.getPos(), this.toDeselect.getStringIndex());
				// Checking if the mouse is in the bounds
				if(bounds.contains(cam.toCamX(x), cam.toCamY(y))){
					paint.getSelected().deselect(this.toDeselect);
				}
			}
			// Regardless of if the TabPosition was deselected, the selection to deselect should be cleared
			this.toDeselect = null;
		}
	}
	
	/**
	 * When the middle mouse button is released, it releases the anchor point
	 * @param e The mouse event, it is assumed this event is for the correct mouse button
	 */
	public void middleMouseReleased(MouseEvent e){
		TabPainter paint = this.getPainter();
		Camera cam = paint.getCamera();
		cam.releaseAnchor();
	}

	/**
	 * Right mouse button release does nothing
	 * @param e The mouse event, it is assumed this event is for the correct mouse button
	 */
	public void rightMouseReleased(MouseEvent e){}
	
	@Override
	/**
	 * When the middle mouse button is clicked while holding shift, reset the camera
	 */
	public void mouseClicked(MouseEvent e){
		TabPainter paint = this.getPainter();
		this.updateLastPos(e);
		
		switch(e.getButton()){
			case MouseEvent.BUTTON1: this.leftMouseClicked(e); break;
			case MouseEvent.BUTTON2: this.middleMouseClicked(e); break;
			case MouseEvent.BUTTON3: this.rightMouseClicked(e); break;
		}

		paint.repaint();
	}
	
	/**
	 * Left mouse button click does nothing
	 * @param e The mouse event, it is assumed this event is for the correct mouse button
	 */
	public void leftMouseClicked(MouseEvent e){}
	
	/**
	 * Middle mouse button click does nothing
	 * @param e The mouse event, it is assumed this event is for the correct mouse button
	 */
	public void middleMouseClicked(MouseEvent e){}
	
	/**
	 * Right mouse button click does nothing
	 * @param e The mouse event, it is assumed this event is for the correct mouse button
	 */
	public void rightMouseClicked(MouseEvent e){}
	
	/**
	 * When left mouse button is dragged, update the selection box. 
	 * When middle mouse button is dragged, attempt to pan the camera. 
	 * This method does not handle detecting which button was dragged
	 */
	@Override
	public void mouseDragged(MouseEvent e){
		TabPainter paint = this.getPainter();
		Camera cam = paint.getCamera();
		this.updateLastPos(e);
		double x = e.getX();
		double y = e.getY();
		
		paint.getSelectionBox().updateSelectionBox(x, y);
		cam.pan(x, y);
		paint.getDragger().update(x, y);
		paint.updateHoveredPosition(x, y);
		
		paint.repaint();
	}
	
	/**
	 * Set the note that the user is hovering over, or set it to null if a not isn't found
	 */
	@Override
	public void mouseMoved(MouseEvent e){
		TabPainter paint = this.getPainter();
		this.updateLastPos(e);
		double x = e.getX();
		double y = e.getY();
		
		if(paint.updateHoveredPosition(x, y)) paint.repaint();
	}
	
	/**
	 * When the mouse wheel is moved, zoom in or out based on the mouse position and wheel movement
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e){
		TabPainter paint = this.getPainter();
		TabControlSettings settings = ZabAppSettings.get().control();
		Camera cam = paint.getCamera();
		this.updateLastPos(e);
		double x = e.getX();
		double y = e.getY();
		double wheelMove = e.getWheelRotation();
		boolean shift = e.isShiftDown();
		boolean alt = e.isAltDown();
		boolean ctrl = e.isControlDown();
		boolean zoom = ctrl;
		
		// If zooming in, i.e. holding control, then zoom in depending on which buttons are held
		if(zoom){
			double factor = settings.zoomFactor();
			if(settings.zoomInverted()) factor *= -1;
			double amount = wheelMove * factor;

			// If alt is held and shift is not, then zoom only on the x axis
			if(!shift && alt) cam.zoomInX(x, amount);
			// If shift is held and alt is not, then zoom only on the y axis
			else if(shift && !alt) cam.zoomInY(y, amount);
			// Otherwise, zoom on both axis
			else cam.zoomIn(x, y, amount);
		}
		// If not zooming, scroll the camera up or down
		else{
			double amount = wheelMove * settings.scrollFactor();
			boolean inverseX = settings.scrollXInverted();
			boolean inverseY = settings.scrollYInverted();
			if(shift){
				if(inverseX) amount *= -1;
				cam.addX(cam.inverseZoomX(amount));
			}
			else{
				if(inverseY) amount *= -1;
				cam.addY(cam.inverseZoomY(amount));
			}
		}
		
		paint.repaint();
	}

	/** Only updates the mouse position */
	@Override
	public void mouseEntered(MouseEvent e){
		this.updateLastPos(e);
	}

	/** Only updates the mouse position */
	@Override
	public void mouseExited(MouseEvent e){
		this.updateLastPos(e);
	}

}
