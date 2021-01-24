package appMain.gui.comp.editor;

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
		mouse.mouseEntered(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
		assertEquals(12, mouse.x(), "Checking x coordinate updated");
		assertEquals(34, mouse.y(), "Checking y coordinate updated");
		
		// Running call cases of pressing
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON2));
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON3));
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.NOBUTTON));
	}
	
	@Test
	public void leftMousePressed(){ // TODO
		paint.clearSelection();
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertEquals(tab.getStrings().get(0).get(0), list.selectedPosition(0), "Checking a note selected on left click");
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
		mouse.mousePressed(new MouseEvent(paint, 0, 0, 0, 460, 340, 0, 0, 0, false, MouseEvent.BUTTON3));
		assertEquals(2, tab.getStrings().get(0).size(), "Checking a new note added on right click");
	}
	
	@Test
	public void mouseReleased(){
		mouse.mouseEntered(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
		assertEquals(12, mouse.x(), "Checking x coordinate updated");
		assertEquals(34, mouse.y(), "Checking y coordinate updated");
		
		// Running call cases of releasing
		mouse.mouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON1));
		mouse.mouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON2));
		mouse.mouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON3));
		mouse.mouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.NOBUTTON));
	}
	
	@Test
	public void leftMouseReleased(){ // TODO
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
		mouse.mouseEntered(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
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
		cam.setXZoomFactor(1);
		mouse.mouseClicked(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertEquals(1, cam.getXZoomFactor(), "Checking zoom factor unchanged on middle click without shift");
		
		mouse.mouseClicked(new MouseEvent(paint, 0, 0, MouseEvent.SHIFT_DOWN_MASK, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertEquals(0, cam.getXZoomFactor(), "Checking zoom factor reset on middle click with shift");
	}
	
	@Test
	public void rightMouseClicked(){
		// Running empty case
		mouse.rightMouseReleased(new MouseEvent(paint, 0, 0, 0, 10, 11, 0, 0, 0, false, MouseEvent.BUTTON3));
	}
	
	@Test
	public void mouseDragged(){
		mouse.mouseEntered(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
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
		mouse.mouseEntered(new MouseEvent(paint, 0, 0, 0, 12, 34, 0, 0, 0, false, 0));
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
		mouse.mouseEntered(new MouseWheelEvent(paint, 0, 0, 0, 12, 34, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(12, mouse.x(), "Checking x coordinate updated");
		assertEquals(34, mouse.y(), "Checking y coordinate updated");
	
		
		paint.resetCamera();
		mouse.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, 0, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-2.0, cam.getXZoomFactor(), "Checking zooming in with no modifiers");
		
		paint.resetCamera();
		mouse.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, 0, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, -1));
		assertEquals(2.0, cam.getXZoomFactor(), "Checking zooming out with no modifiers");

		paint.resetCamera();
		mouse.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.SHIFT_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-4.0, cam.getXZoomFactor(), "Checking zooming in with shift");
		
		paint.resetCamera();
		mouse.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.SHIFT_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, -1, -1));
		assertEquals(4.0, cam.getXZoomFactor(), "Checking zooming out with shift");
		
		ZabAppSettings.get().getZoomInverted().set(false);
		paint.resetCamera();
		mouse.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.SHIFT_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, -2, -2));
		assertEquals(-8.0, cam.getXZoomFactor(), "Checking zooming out with shift with opposite setting");
		ZabAppSettings.get().getZoomInverted().set(true);
		
		paint.resetCamera();
		mouse.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.ALT_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-4.0, cam.getXZoomFactor(), "Checking zooming in with alt");
		
		paint.resetCamera();
		mouse.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.CTRL_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-4.0, cam.getXZoomFactor(), "Checking zooming in with ctrl");

		paint.resetCamera();
		mouse.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0,
				MouseWheelEvent.CTRL_DOWN_MASK | MouseWheelEvent.SHIFT_DOWN_MASK | MouseWheelEvent.ALT_DOWN_MASK,
				950, 300, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-16.0, cam.getXZoomFactor(), "Checking zooming in with all modifiers");
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
