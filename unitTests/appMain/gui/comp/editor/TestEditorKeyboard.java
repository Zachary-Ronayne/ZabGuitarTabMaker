package appMain.gui.comp.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestEditorKeyboard extends AbstractTestTabPainter{

	private EditorKeyboard keys;

	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
		keys = paint.getKeyInput();
		
		paint.select(0, 0);
		cam.setXZoomFactor(1);
	}
	
	@Test
	public void mouseX(){
		paint.getMouseInput().mouseClicked(new MouseEvent(paint, 0, 0, 0, 23, 45, 0, 0, 0, false, 0));
		assertEquals(23, keys.mouseX(), "Checking mouse x set");
	}
	
	@Test
	public void mouseY(){
		paint.getMouseInput().mouseClicked(new MouseEvent(paint, 0, 0, 0, 23, 45, 0, 0, 0, false, 0));
		assertEquals(45, keys.mouseY(), "Checking mouse y set");
	}

	@Test
	public void keyPressed(){
		// Running all of the key presses
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_R, 'r'));
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_D, 'd'));
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_DELETE, ' '));
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_A, 'a'));
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_ESCAPE, ' '));
		
		// Running case of invalid key press
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.KEY_LOCATION_UNKNOWN, ' '));
	}
	
	@Test
	public void keyTyped(){
		// Running empty case
		keys.keyTyped(new KeyEvent(paint, 0, 0, 0, KeyEvent.KEY_LOCATION_UNKNOWN, ' '));
	}
	
	@Test
	public void keyReleased(){
		// Running empty case
		keys.keyReleased(new KeyEvent(paint, 0, 0, 0, KeyEvent.KEY_LOCATION_UNKNOWN, ' '));
	}
	
	@Test
	public void keyReset(){
		tab.placeQuantizedNote(0, 0, 0);
		tab.placeQuantizedNote(1, 0, 1);
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_R, 'r'));
		assertFalse(tab.isEmpty(), "Checking tab notes were not removed on r press no ctrl");
		
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_R, 'r'));
		assertTrue(tab.isEmpty(), "Checking tab notes were removed on r press no ctrl");
	}
	
	@Test
	public void keySelectionRemoval(){
		paint.selectAllNotes();
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_D, 'd'));
		assertFalse(tab.isEmpty(), "Checking tab notes were not removed on d press no ctrl");

		paint.selectAllNotes();
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_D, 'd'));
		assertFalse(tab.isEmpty(), "Checking tab notes were not removed on d press with ctrl");
		assertTrue(paint.getSelected().isEmpty(), "Checking selection removed on d press with ctrl");

		paint.selectAllNotes();
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_D, 'd'));
		assertTrue(tab.isEmpty(), "Checking tab notes were removed on d press with ctrl and shift");
		assertTrue(paint.getSelected().isEmpty(), "Checking selection removed on d press with ctrl and shift");
	}
	
	@Test
	public void keySelectionDelete(){
		TestTabPainter.initNotes(tab);
		assertFalse(tab.isEmpty(), "Checking tab notes were placed");
		paint.selectAllNotes();
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_DELETE, ' '));
		assertTrue(tab.isEmpty(), "Checking tab notes were removed on delete press");
	}
	
	@Test
	public void keySelectAll(){
		paint.clearSelection();
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_A, 'a'));
		assertTrue(paint.getSelected().isEmpty(), "Checking no selection made on a press no ctrl");
		
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_A, 'a'));
		assertEquals(9, paint.getSelected().size(), "Checking all notes selected on a press with ctrl");
	}
	
	@Test
	public void keyCancelActions(){
		Point2D.Double p = new Point2D.Double(20, 10);
		paint.getDragger().setDragPoint(p);
		paint.getSelectionBox().setFirstCorner(p);
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_ESCAPE, ' '));
		assertEquals(null, paint.getDragger().getDragPoint(), "Checking drag point reset on escape press");
		assertEquals(null, paint.getSelectionBox().getFirstCorner(), "Checking selection box corner reset on escape press");
	}
	
	@Test
	public void keyTypeTabPitch(){
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, 0, '1'));
		assertEquals(1, paint.getSelectedNewTabNum(), "Checking tab num updated with number");
		
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, 0, '-'));
		assertEquals(-1, paint.getSelectedNewTabNum(), "Checking tab num updated with minus sign");
	}
	
	@AfterEach
	public void end(){}
	
}
