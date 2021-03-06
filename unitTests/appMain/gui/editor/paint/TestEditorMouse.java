package appMain.gui.editor.paint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import appUtils.settings.ZabSettings;
import tab.TabPosition;

public class TestEditorMouse extends AbstractTestTabPainter{
	
	private EditorMouse mouse;

	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
		mouse = paint.getMouseInput();
	}
	
	@Test
	public void mouseX(){
		assertEquals(-1000, mouse.x(), "Checking x coordinate initialized");
	}
	
	@Test
	public void mouseY(){
		assertEquals(-1000, mouse.y(), "Checking y coordinate initialized");
	}
	
	@Test
	public void updateLastPos(){
		mouse.updateLastPos(new MouseEvent(paint, 0, 0, 0, 28, 39, 0, 0, 0, false, 0));
		assertEquals(28, mouse.x(), "Checking x coordinate updated");
		assertEquals(39, mouse.y(), "Checking y coordinate updated");
	}
	
	@Test
	public void mousePressed(){
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
		assertEquals(12, mouse.x(), "Checking x coordinate updated");
		assertEquals(34, mouse.y(), "Checking y coordinate updated");
		
		// Running call cases of pressing
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON2));
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON3));
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.NOBUTTON));
	}
	
	@Test
	public void leftMousePressed(){
		SelectionDragger drag = paint.getDragger();
		SelectionBox box = paint.getSelectionBox();
		
		// Clicking on a TabPosition, string 0 index 0
		paint.clearSelection();
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertEquals(tab.getStrings().get(0).get(0), list.selectedPosition(0), "Checking a note selected on left click");
		assertTrue(drag.isDragging(), "Checking clicking on a note initiates a drag");
		assertFalse(box.isSelecting(), "Checking clicking on a note does not initiate a box selection");
		
		// Clicking on a TabPosition and releasing the click and it deselects
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(list.isEmpty(), "Checking a note deselected");
		assertFalse(drag.isDragging(), "Checking deselected note doesn't initialize a drag");
		assertFalse(box.isSelecting(), "Checking clicking on a note does not initiate a box selection");
		
		// Clicking on a TabPosition and releasing the click to select it, then not deselect by releasing not on the bounds of the TabPosition
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertEquals(tab.getStrings().get(0).get(0), list.selectedPosition(0), "Checking a note selected");
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 1036, 373, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertEquals(tab.getStrings().get(0).get(0), list.selectedPosition(0), "Checking a note not deselected");
		
		// Clicking not on a TabPosition
		paint.clearSelection();
		drag.reset();
		box.clear();
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 596, 467, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertFalse(drag.isDragging(), "Checking clicking not on a note does not initiates a drag");
		assertTrue(box.isSelecting(), "Checking clicking not on a note initiates a box selection");
		
		// Holding shift causes a line selection
		paint.clearSelection();
		drag.reset();
		box.clear();
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, MouseEvent.SHIFT_DOWN_MASK, 236, 917, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, MouseEvent.SHIFT_DOWN_MASK, 236, 917, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, MouseEvent.SHIFT_DOWN_MASK, 687, 905, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, MouseEvent.SHIFT_DOWN_MASK, 236, 917, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(paint.isSelected(str5.get(0), 5), "Checking notes selected");
		assertTrue(paint.isSelected(str5.get(1), 5), "Checking notes selected");
		assertTrue(paint.isSelected(str5.get(2), 5), "Checking notes selected");
		assertFalse(paint.isSelected(str5.get(3), 5), "Checking note bit selected");
		
		// Clicking not on TabPosition initiates a box selection, drags then releases, then selects notes
		paint.clearSelection();
		drag.reset();
		box.clear();
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 381, 611, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(box.isSelecting(), "Checking box has begun a selection");
		assertFalse(drag.isDragging(), "Checking drag has not happened");
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 381, 611, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(box.isSelecting(), "Checking box is still selecting");
		assertFalse(drag.isDragging(), "Checking drag has not happened after another click");
		mouse.mouseDragged(new MouseEvent(paint, 0, 0, 0, 790, 822, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(box.isSelecting(), "Checking box is still selecting");
		assertTrue(box.hasCorners(), "Checking box has both corners");
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 790, 822, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertFalse(box.isSelecting(), "Checking box is now not selecting");
		assertFalse(box.hasCorners(), "Checking box has no corners");
		assertTrue(paint.isSelected(str3.get(0), 3), "Checking TabPosition from box are selected");
		assertTrue(paint.isSelected(str4.get(0), 4), "Checking TabPosition from box are selected");
		
		// Checking clicking with control doesn't remove other selections, but adds to the selection
		paint.clearSelection();
		drag.reset();
		box.clear();
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 690, 692, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 690, 692, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(paint.isSelected(str3.get(0), 3), "Checking TabPosition clicked on is selected");
		assertFalse(paint.isSelected(str2.get(0), 2), "Checking TabPosition not clicked on is not selected");
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 925, 573, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 925, 573, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(paint.isSelected(str2.get(0), 2), "Checking TabPosition clicked on is selected");
		assertFalse(paint.isSelected(str3.get(0), 3), "Checking TabPosition not clicked on is not selected");
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, MouseEvent.CTRL_DOWN_MASK, 690, 692, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, MouseEvent.CTRL_DOWN_MASK, 690, 692, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(paint.isSelected(str3.get(0), 3), "Checking TabPosition clicked on is selected");
		assertTrue(paint.isSelected(str2.get(0), 2), "Checking TabPosition not clicked on is not deselected with ctrl held down");
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, MouseEvent.CTRL_DOWN_MASK | MouseEvent.SHIFT_DOWN_MASK, 972, 445, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, MouseEvent.CTRL_DOWN_MASK | MouseEvent.SHIFT_DOWN_MASK, 972, 445, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(paint.isSelected(str1.get(0), 1), "Checking TabPosition clicked on is selected");
		assertTrue(paint.isSelected(str2.get(0), 2), "Checking TabPosition not clicked on is not deselected with ctrl and shift held down");
		assertTrue(paint.isSelected(str3.get(0), 3), "Checking TabPosition not clicked on is not deselected with ctrl and shift held down");
	}
	
	@Test
	public void middleMousePressed(){
		cam.releaseAnchor();
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 100, 101, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertTrue(cam.isAchored(), "Checking camera anchored on middle click");
	}
	
	@Test
	public void rightMousePressed(){
		paint.clearSelection();
		assertTrue(paint.getUndoStack().isEmpty(), "Checking no events in undo stack");
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 460, 340, 0, 0, 0, false, MouseEvent.BUTTON3));
		assertEquals(2, tab.getStrings().get(0).size(), "Checking a new note added on right click");
		
		assertFalse(paint.getUndoStack().isEmpty(), "Checking event is added to undo stack");
	}
	
	@Test
	public void mouseReleased(){
		mouse.mouseReleased(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
		assertEquals(12, mouse.x(), "Checking x coordinate updated");
		assertEquals(34, mouse.y(), "Checking y coordinate updated");
		
		// Running call cases of releasing
		mouse.mouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.mouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON2));
		mouse.mouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON3));
		mouse.mouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.NOBUTTON));
	}
	
	@Test
	public void leftMouseReleased(){
		SelectionDragger drag = paint.getDragger();
		SelectionBox box = paint.getSelectionBox();
		
		// Basic checking of things reset
		box.setSelecting(true);
		box.updateSelectionBox(478, 512);
		paint.selectNote(688.0, 696.0);
		drag.begin(688.0, 696.0);
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 478, 512, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertFalse(box.isSelecting(), "Checking box is not selecting after mouse release");
		assertFalse(box.hasCorners(), "Checking box has no corners after mouse release");
		assertFalse(drag.isDragging(), "Checking no drag happening after mouse release");

		// Checking dragging makes a placement
		paint.clearSelection();
		paint.select(0, 2);
		drag.begin(918.0, 567.0);
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 691, 573, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertEquals(2, str2.get(0).getPos(), "Checking TabPosition moved after a drag from mouse release");
		
		// Checking a note to deselect becomes deselected
		paint.clearSelection();
		paint.select(0, 0);
		assertTrue(paint.isSelected(str0.get(0), 0), "Checking TabPosition selected");
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(list.isEmpty(), "Checking a note deselected");
		
		// Checking having a TabPosition to deselect doesn't deselect when no TabPosition is on the mouse 
		paint.clearSelection();
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(paint.isSelected(str0.get(0), 0), "Checking TabPosition selected");
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 532, 503, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(paint.isSelected(str0.get(0), 0), "Checking TabPosition still selected after releasing not on a note");

		// Checking having a TabPosition to deselect doesn't deselect when the TabPosition on the mouse is not the one to deselect
		paint.clearSelection();
		paint.select(0, 0);
		assertTrue(paint.isSelected(str0.get(0), 0), "Checking TabPosition selected");
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 969, 474, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(paint.isSelected(str0.get(0), 0), "Checking TabPosition still selected after releasing on a different note");
		
		// Checking having a note to deselect and the mouse isn't in the bounds doesn't deselect
		paint.clearSelection();
		paint.select(0, 0);
		assertTrue(paint.isSelected(str0.get(0), 0), "Checking TabPosition selected");
		mouse.leftMousePressed(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.leftMouseReleased(new MouseEvent(paint, 0, 0, 0, 1035, 374, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(paint.isSelected(str0.get(0), 0), "Checking TabPosition still selected after releasing not on the note hit box");
		
	}
	
	@Test
	public void middleMouseReleased(){
		cam.setAnchor(0, 0);
		mouse.mouseReleased(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertFalse(cam.isAchored(), "Checking middle click releases camera anchor");
	}
	
	@Test
	public void rightMouseReleased(){
		// Running empty case
		mouse.rightMouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON3));
	}
	
	@Test
	public void mouseClicked(){
		mouse.mouseClicked(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
		assertEquals(12, mouse.x(), "Checking x coordinate updated");
		assertEquals(34, mouse.y(), "Checking y coordinate updated");
		
		// Running call cases of clicking
		mouse.mouseClicked(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.mouseClicked(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON2));
		mouse.mouseClicked(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON3));
		mouse.mouseClicked(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.NOBUTTON));
	}
	
	@Test
	public void leftMouseClicked(){
		// Running empty case
		mouse.rightMouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON1));
	}
	
	@Test
	public void middleMouseClicked(){
		// Running empty case
		mouse.rightMouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON2));
	}
	
	@Test
	public void rightMouseClicked(){
		// Running empty case
		mouse.rightMouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON3));
	}
	
	@Test
	public void mouseDragged(){
		mouse.mouseDragged(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
		assertEquals(12, mouse.x(), "Checking x coordinate updated");
		assertEquals(34, mouse.y(), "Checking y coordinate updated");
		
		double x = cam.getX();
		cam.setAnchor(0, 0);
		mouse.mouseDragged(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertNotEquals(x, cam.getX(), "Checking camera was panned");

		x = cam.getX();
		cam.releaseAnchor();
		mouse.mouseDragged(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertEquals(x, cam.getX(), "Checking camera was not panned");
	}
	
	@Test
	public void mouseMoved(){
		mouse.mouseMoved(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
		assertEquals(12, mouse.x(), "Checking x coordinate updated");
		assertEquals(34, mouse.y(), "Checking y coordinate updated");
		
		super.setup(false);
		mouse = paint.getMouseInput();
		mouse.mouseMoved(new MouseEvent(paint, 0, 0, 0, -1000, -1000, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertEquals(null, paint.getHoveredPosition(), "Checking hover TabPosition null on a position not on a note");
		assertEquals(null, paint.getLastSymbolFont(), "Checking symbol font not set");

		paint.paint(g);
		TabPosition p = str0.get(0);
		mouse.mouseMoved(new MouseEvent(paint, 0, 0, 0, 
				(int)paint.tabPosToX(p.getPos()) + 1, 
				(int)paint.tabPosToY(p.getPos(), 0) + 1, 
				0, 0, 0, false, MouseEvent.BUTTON1));
		assertEquals(p, paint.getHoveredPosition(), "Checking hover TabPosition set");
		assertNotEquals(null, paint.getLastSymbolFont(), "Checking symbol font set");
	}
	
	@Test
	public void mouseWheelMoved(){
		int shift = MouseWheelEvent.SHIFT_DOWN_MASK;
		int alt = MouseWheelEvent.ALT_DOWN_MASK;
		int ctrl = MouseWheelEvent.CTRL_DOWN_MASK;
		
		ZabSettings settings = ZabAppSettings.get();
		
		// Basic mouse wheel movement
		mouse.mouseWheelMoved(mouseWheelMove(0, 12, 34, 1));
		assertEquals(12, mouse.x(), "Checking x coordinate updated");
		assertEquals(34, mouse.y(), "Checking y coordinate updated");
		
		// Scrolling up to down, and left to right
		paint.resetCamera();
		cam.setY(100);
		settings.control().getScrollFactor().set(10.0);
		mouse.mouseWheelMoved(mouseWheelMove(0, 950, 300, 1));
		assertEquals(110, cam.getY(), "Checking scrolling up with y scroll inverted");
		
		mouse.mouseWheelMoved(mouseWheelMove(0, 950, 300, -2));
		assertEquals(90, cam.getY(), "Checking scrolling down with y scroll inverted");
		
		settings.control().getScrollYInverted().set(true);
		mouse.mouseWheelMoved(mouseWheelMove(0, 950, 300, 1));
		assertEquals(80, cam.getY(), "Checking scrolling up with y scroll not inverted");
		
		mouse.mouseWheelMoved(mouseWheelMove(0, 950, 300, -2));
		assertEquals(100, cam.getY(), "Checking scrolling down with y scroll not inverted");
		
		cam.setX(200);
		settings.control().getScrollFactor().set(10.0);
		mouse.mouseWheelMoved(mouseWheelMove(shift, 950, 300, 1));
		assertEquals(210, cam.getX(), "Checking scrolling up with x scroll inverted");
		
		mouse.mouseWheelMoved(mouseWheelMove(shift, 950, 300, -2));
		assertEquals(190, cam.getX(), "Checking scrolling down with x scroll inverted");
		
		settings.control().getScrollXInverted().set(true);
		mouse.mouseWheelMoved(mouseWheelMove(shift, 950, 300, 1));
		assertEquals(180, cam.getX(), "Checking scrolling up with x scroll not inverted");
		
		mouse.mouseWheelMoved(mouseWheelMove(shift, 950, 300, -2));
		assertEquals(200, cam.getX(), "Checking scrolling down with x scroll not inverted");
		
		// Zooming
		settings.control().getZoomFactor().set(0.5);
		settings.control().getZoomInverted().set(true);
		
		paint.resetCamera();
		cam.setZoomBase(2);
		mouse.mouseWheelMoved(mouseWheelMove(ctrl, 950, 300, -1));
		assertEquals(0.5, cam.getXZoomFactor(), "Checking zooming on both axes");
		assertEquals(0.5, cam.getYZoomFactor(), "Checking zooming on both axes");
		assertEquals(478.2485578727798, cam.getX(), "Checking zooming on both axes, camera position updated");
		assertEquals(1717.8679656440358, cam.getY(), "Checking zooming on both axes, camera position updated");
		
		paint.resetCamera();
		cam.setZoomBase(2);
		mouse.mouseWheelMoved(mouseWheelMove(ctrl | alt, 950, 300, -1));
		assertEquals(0.5, cam.getXZoomFactor(), "Checking zooming on only x axis");
		assertEquals(0, cam.getYZoomFactor(), "Checking zooming on only x axis");
		assertEquals(478.2485578727798, cam.getX(), "Checking zooming on both axes, camera x position updated");
		assertEquals(1630.0, cam.getY(), "Checking zooming on both axes, camera y position not updated");
		
		paint.resetCamera();
		cam.setZoomBase(2);
		mouse.mouseWheelMoved(mouseWheelMove(ctrl | shift, 950, 300, -1));
		assertEquals(0, cam.getXZoomFactor(), "Checking zooming on only y axis");
		assertEquals(0.5, cam.getYZoomFactor(), "Checking zooming on only y axis");
		assertEquals(200, cam.getX(), "Checking zooming on both axes, camera x position not updated");
		assertEquals(1717.8679656440358, cam.getY(), "Checking zooming on both axes, camera y position updated");
		
		paint.resetCamera();
		cam.setZoomBase(2);
		mouse.mouseWheelMoved(mouseWheelMove(ctrl | alt | shift, 950, 300, -1));
		assertEquals(0.5, cam.getXZoomFactor(), "Checking zooming on both axes");
		assertEquals(0.5, cam.getYZoomFactor(), "Checking zooming on both axes");
		assertEquals(478.2485578727798, cam.getX(), "Checking zooming on both axes, camera position updated");
		assertEquals(1717.8679656440358, cam.getY(), "Checking zooming on both axes, camera position updated");
		
		settings.control().getZoomInverted().set(false);
		paint.resetCamera();
		cam.setZoomBase(2);
		mouse.mouseWheelMoved(mouseWheelMove(ctrl | alt | shift, 950, 300, -1));
		assertEquals(-0.5, cam.getXZoomFactor(), "Checking zooming on both axes with inverted zooming");
		assertEquals(-0.5, cam.getYZoomFactor(), "Checking zooming on both axes with inverted zooming");
		assertEquals(-193.50288425444035, cam.getX(), "Checking zooming on both axes, camera position updated with inverted zooming");
		assertEquals(1505.7359312880715, cam.getY(), "Checking zooming on both axes, camera position updated with inverted zooming");
		
		// Putting settings back to their defaults
		AbstractTestTabPainter.init();
	}
	
	/**
	 * A convenience method for testing mouse wheel events. Creates a mouse wheel event with most of the values filled in.
	 * @param modifiers The modifier mask for the event
	 * @param x The x position the event took place
	 * @param y The y position the event took place
	 * @param srollAmount The amount the wheel was scrolled
	 * @return The event
	 */
	private MouseWheelEvent mouseWheelMove(int modifiers, int x, int y, int srollAmount){
		return new MouseWheelEvent(paint, 0, 0, modifiers,
				x, y, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, srollAmount, srollAmount);
	}
	
	@Test
	public void mouseEntered(){
		mouse.mouseEntered(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
		assertEquals(12, mouse.x(), "Checking x coordinate updated");
		assertEquals(34, mouse.y(), "Checking y coordinate updated");
	}
	
	@Test
	public void mouseExited(){
		mouse.mouseExited(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
		assertEquals(12, mouse.x(), "Checking x coordinate updated");
		assertEquals(34, mouse.y(), "Checking y coordinate updated");
	}
	
	@AfterEach
	public void end(){}
	
}
