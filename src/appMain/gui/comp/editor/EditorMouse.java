package appMain.gui.comp.editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import appMain.gui.util.Camera;
import appUtils.ZabAppSettings;
import appUtils.ZabSettings;
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
	 * Create a default state {@link EditorMouse}
	 * @param paint See {@link TabPaintController#paint}
	 */
	public EditorMouse(TabPainter paint){
		super(paint);
		// Initializing coordinates to be negative so that they are not in the positive bounds of the painter
		this.lastX = -1000;
		this.lastY = -1000;
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
		Selection clickedPosition = paint.findPosition(x, y);
		boolean mouseOnPosition = clickedPosition != null;
		
		/*
		 * If the selection box is currently not dragging, 
		 * selection box is not currently making a selection, 
		 * there is at least one selected note, 
		 * and the user is not holding control
		 * attempt to begin a drag
		 */
		if(!drag.isDragging() && 
				!box.isSelecting() && 
				!paint.getSelected().isEmpty() &&
				!e.isControlDown()){
			
			drag.begin(x, y);
		}
		
		// If a drag not is in place, then a selection box can begin, or selecting individual notes 
		if(!drag.isDragging()){
			// If the mouse is on a TabPosition, then select or deselect it when applicable
			if(mouseOnPosition){
				boolean selected;
				// If shift is held down, select a line of TabPositions
				if(e.isShiftDown()) selected = paint.selectLine(x, y, e.isControlDown());
				// Otherwise, select one note
				else selected = paint.selectNote(x, y, e.isControlDown());
				
				// If selecting a TabPosition succeeds, allow a drag to begin
				if(selected) drag.begin(x, y);
				// Otherwise, the TabPosition wasn't selected, so deselect it if applicable
				else paint.deselect(x, y);
			}
			// Otherwise, begin a box selection
			else box.setSelecting(true);
		}
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
		paint.placeNote(x, y, 0);
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
	 * if control is held down, add to the selection.
	 * @param e The mouse event, it is assumed this event is for the correct mouse button
	 */
	public void leftMouseReleased(MouseEvent e){
		TabPainter paint = this.getPainter();
		SelectionBox box = paint.getSelectionBox();
		box.selectInPainter(e.isControlDown());
		box.clear();
		SelectionDragger drag = paint.getDragger();
		double x = e.getX();
		double y = e.getY();
		if(drag.isDragging()){
			drag.place(x, y, e.isShiftDown());
			drag.reset();
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
	 * Left mouse button click does nothing
	 * @param e The mouse event, it is assumed this event is for the correct mouse button
	 */
	public void middleMouseClicked(MouseEvent e){
		TabPainter paint = this.getPainter();
		if(e.isShiftDown()) paint.resetCamera();
	}
	
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
		ZabSettings settings = ZabAppSettings.get();
		Camera cam = paint.getCamera();
		this.updateLastPos(e);
		double x = e.getX();
		double y = e.getY();
		
		double factor = settings.zoomFactor();
		if(settings.zoomInverted()) factor *= -1;
		
		double zoomMult = settings.zoomModifierFactor();
		if(e.isShiftDown()) factor *= zoomMult;
		if(e.isAltDown()) factor *= zoomMult;
		if(e.isControlDown()) factor *= zoomMult;
		cam.zoomIn(x, y, e.getWheelRotation() * factor);
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
