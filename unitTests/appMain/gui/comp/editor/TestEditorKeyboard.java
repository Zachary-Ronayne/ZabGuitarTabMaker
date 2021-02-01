package appMain.gui.comp.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tab.ModifierFactory;
import tab.symbol.TabModifier;
import tab.symbol.TabSymbol;
import util.testUtils.UtilsTest;

public class TestEditorKeyboard extends AbstractTestTabPainter{

	private EditorKeyboard keys;

	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
		UtilsTest.createUnitFolder();
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
		// Running all of the key press cases
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_R, 'r'));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_R, 'r'));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_D, 'd'));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_DELETE, ' '));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_A, 'a'));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_ESCAPE, ' '));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_S, ' '));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_L, ' '));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_E, ' '));
		
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
		keys.keyReset(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_R, 'r'));
		assertFalse(tab.isEmpty(), "Checking tab notes were not removed on r press no ctrl");
		
		keys.keyReset(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_R, 'r'));
		assertTrue(tab.isEmpty(), "Checking tab notes were removed on r press no ctrl");
	}
	
	@Test
	public void keySelectionRemoval(){
		paint.selectAllNotes();
		keys.keySelectionRemoval(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_D, 'd'));
		assertFalse(tab.isEmpty(), "Checking tab notes were not removed on d press no ctrl");

		paint.selectAllNotes();
		keys.keySelectionRemoval(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_D, 'd'));
		assertFalse(tab.isEmpty(), "Checking tab notes were not removed on d press with ctrl");
		assertTrue(paint.getSelected().isEmpty(), "Checking selection removed on d press with ctrl");

		paint.selectAllNotes();
		keys.keySelectionRemoval(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_D, 'd'));
		assertTrue(tab.isEmpty(), "Checking tab notes were removed on d press with ctrl and shift");
		assertTrue(paint.getSelected().isEmpty(), "Checking selection removed on d press with ctrl and shift");
	}
	
	@Test
	public void keySelectionDelete(){
		TestTabPainter.initNotes(tab);
		assertFalse(tab.isEmpty(), "Checking tab notes were placed");
		paint.selectAllNotes();
		keys.keySelectionDelete(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_DELETE, ' '));
		assertTrue(tab.isEmpty(), "Checking tab notes were removed on delete press");
	}
	
	@Test
	public void keySelectAll(){
		paint.clearSelection();
		keys.keySelectAll(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_A, 'a'));
		assertTrue(paint.getSelected().isEmpty(), "Checking no selection made on a press no ctrl");
		
		keys.keySelectAll(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_A, 'a'));
		assertEquals(9, paint.getSelected().size(), "Checking all notes selected on a press with ctrl");
	}
	
	@Test
	public void keyCancelActions(){
		Point2D.Double p = new Point2D.Double(20, 10);
		paint.getDragger().setDragPoint(p);
		paint.getSelectionBox().setFirstCorner(p);
		keys.keyCancelActions(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_ESCAPE, ' '));
		assertEquals(null, paint.getDragger().getDragPoint(), "Checking drag point reset on escape press");
		assertEquals(null, paint.getSelectionBox().getFirstCorner(), "Checking selection box corner reset on escape press");
	}
	
	@Test
	public void findFileMenu(){
		assertEquals(gui.getZabMenuBar().getFileMenu(), keys.findFileMenu(), "Checking file menu found");
	}
	
	@Test
	public void keySave(){
		File f = new File(UtilsTest.UNIT_PATH + "/keyboardShortCutSaveTest.zab");
		keys.findFileMenu().setLoadedFile(f);
		keys.findFileMenu().getFileChooser().setSelectedFile(f);
		assertFalse(keys.keySave(new KeyEvent(gui, 0, 0, 0, KeyEvent.VK_S, 's')), "Checking save fails with control not held down");
		
		assertTrue(keys.keySave(new KeyEvent(gui, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_S, 's')),
				"Checking save succeeds with control held down");
		
		assertTrue(keys.keySave(new KeyEvent(gui, 0, 0, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_S, 's')),
				"Checking save as succeeds with control and shift held down");
	}
	
	@Test
	public void keyLoad(){
		File f = new File(UtilsTest.UNIT_PATH + "/keyboardShortCutSaveTest.zab");
		keys.findFileMenu().setLoadedFile(f);
		keys.findFileMenu().getFileChooser().setSelectedFile(f);
		keys.findFileMenu().save();
		assertFalse(keys.keyLoad(new KeyEvent(gui, 0, 0, 0, KeyEvent.VK_L, ';')), "Checking load fails with control not held down");
		
		assertTrue(keys.keyLoad(new KeyEvent(gui, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_L, 'l')),
				"Checking load succeeds with control held down");
	}
	
	@Test
	public void keyExport(){
		assertFalse(keys.keyExport(new KeyEvent(gui, 0, 0, 0, KeyEvent.VK_E, 'e')),
				"Checking opening dialog fails with no modifiers held down");
		
		assertFalse(keys.keyExport(new KeyEvent(gui, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_E, 'e')),
				"Checking opening dialog fails with only control held down");
		
		assertFalse(keys.keyExport(new KeyEvent(gui, 0, 0, KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_E, 'e')),
				"Checking opening dialog fails with only shift held down");
		
		assertTrue(keys.keyExport(new KeyEvent(gui, 0, 0, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK , KeyEvent.VK_E, 'e')),
				"Checking opening dialog succeeds with shift and ctrl held down");
	}
	
	@Test
	public void keyTypeTabPitch(){
		keys.keyTypeTabPitch(new KeyEvent(paint, 0, 0, 0, 0, '1'));
		assertEquals(1, paint.getSelectedNewTabNum(), "Checking tab num updated with number");
		
		keys.keyTypeTabPitch(new KeyEvent(paint, 0, 0, 0, 0, '-'));
		assertEquals(-1, paint.getSelectedNewTabNum(), "Checking tab num updated with minus sign");
	}
	
	@Test
	public void keyTypeSetModifier(){
		paint.clearSelection();
		TabSymbol sym = str0.get(0).getSymbol();
		
		assertFalse(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_P, '1')),
				"Checking nothing placed with no selection");
		
		paint.select(0, 0);
		assertFalse(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_1, '1')),
				"Checking nothing placed with unrecognized key");
		
		assertFalse(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_1, '1')),
				"Checking nothing placed with control held down key");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_P, 'p')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "p"), sym.getModifier(), "Checking pull off placed");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_H, 'h')),
				"Checking modifier placed");
		assertEquals(new TabModifier("h", ""), sym.getModifier(), "Checking hammer on placed");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_SLASH, '/')),
				"Checking modifier placed");
		assertEquals(new TabModifier("/", ""), sym.getModifier(), "Checking slide down placed");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_BACK_SLASH, '\\')),
				"Checking modifier placed");
		assertEquals(new TabModifier("\\", ""), sym.getModifier(), "Checking slide up placed");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_B, 'b')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "b"), sym.getModifier(), "Checking bend placed");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_R, 'r')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "ph"), sym.getModifier(), "Checking pinch harmonic placed");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_BACK_QUOTE, '`')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "~"), sym.getModifier(), "Checking vibrato placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_T, 't')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "t"), sym.getModifier(), "Checking tap placed");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_PERIOD, '.')),
				"Checking modifier placed");
		assertEquals(new TabModifier("<", ">"), sym.getModifier(), "Checking harmonic placed");
		sym.setModifier(new TabModifier());
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_COMMA, ',')),
				"Checking modifier placed");
		assertEquals(new TabModifier("<", ">"), sym.getModifier(), "Checking harmonic placed");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_9, '(')),
				"Checking modifier placed");
		assertEquals(new TabModifier("(", ")"), sym.getModifier(), "Checking ghost note placed");
		sym.setModifier(new TabModifier());
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_0, ')')),
				"Checking modifier placed");
		assertEquals(new TabModifier("(", ")"), sym.getModifier(), "Checking ghost note placed");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_P, 'p')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "p"), sym.getModifier(), "Checking pull off placed");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_H, 'h')),
				"Checking modifier placed");
		assertEquals(new TabModifier("h", "p"), sym.getModifier(), "Checking both modifiers exist");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SPACE, ' ')),
				"Checking modifier removed");
		assertEquals(new TabModifier("", ""), sym.getModifier(), "Checking removing modifier");
		
		sym.setModifier(ModifierFactory.hammerOn());
		assertFalse(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_SPACE, ' ')),
				"Checking modifier not changed");
		assertEquals(new TabModifier("h", ""), sym.getModifier(), "Checking modifier unchanged with space and no shift");
	}
	
	@AfterEach
	public void end(){}
	
	@AfterAll
	public static void endAll(){
		UtilsTest.deleteUnitFolder();
	}
	
}
