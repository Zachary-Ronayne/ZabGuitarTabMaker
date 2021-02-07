package appMain.gui.editor.paint;

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

import appMain.gui.dropMenu.FileMenu;
import appMain.gui.editor.paint.event.EditorEventStack;
import tab.ModifierFactory;
import tab.Tab;
import tab.symbol.TabModifier;
import tab.symbol.TabNote;
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
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_Z, ' '));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_S, ' '));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_L, ' '));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_E, ' '));
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_N, ' '));
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_MINUS, '-'));
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_EQUALS, '='));
		
		paint.clearSelection();
		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_A, 'a'));
		assertTrue(paint.isSelected(str0.get(0), 0), "Checking note selected after pressing ctrl a");
		
		paint.clearSelection();
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_A, 'a'));
		assertFalse(paint.isSelected(str0.get(0), 0), "Checking note not selected after pressing a without ctrl");
		
		cam.setXZoomFactor(0);
		keys.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_MINUS, '-'));
		assertEquals(0, cam.getXZoomFactor(), "Checking not zoomed in with control not held down");

		keys.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_MINUS, '-'));
		assertEquals(2, cam.getXZoomFactor(), "Checking zoomed in with control held down");
		
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
	public void keyZoom(){
		assertFalse(keys.keyZoom(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_0, '0')),
				"Checking non zoom key does not zoom");
		
		cam.setXZoomFactor(0);
		cam.setYZoomFactor(0);
		assertTrue(keys.keyZoom(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_MINUS, '-')),
				"Checking zoom in, no modifiers");
		assertEquals(2, cam.getXZoomFactor(), "Checking x zoomed in");
		assertEquals(2, cam.getYZoomFactor(), "Checking y zoomed in");
		
		assertTrue(keys.keyZoom(new KeyEvent(paint, 0, 0, KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_EQUALS, '=')),
				"Checking zoom out, only x axis");
		assertEquals(0, cam.getXZoomFactor(), "Checking x zoomed out");
		assertEquals(2, cam.getYZoomFactor(), "Checking y not zoomed out");
		
		assertTrue(keys.keyZoom(new KeyEvent(paint, 0, 0, KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_MINUS, '-')),
				"Checking zoom in, only y axis");
		assertEquals(0, cam.getXZoomFactor(), "Checking x not zoomed in");
		assertEquals(4, cam.getYZoomFactor(), "Checking y zoomed in");
	}
	
	@Test
	public void keyReset(){
		tab.placeQuantizedNote(0, 0, 0);
		tab.placeQuantizedNote(1, 0, 1);
		paint.resetCamera();
		double centeredX = cam.getX();
		double centeredY = cam.getY();
		cam.setX(-99);
		cam.setY(-79);
		keys.keyReset(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_R, 'r'));
		assertEquals(-99, cam.getX(), "Checking cam x not changed on reset without control");
		assertEquals(-79, cam.getY(), "Checking cam y not changed on reset without control");
		
		keys.keyReset(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_R, 'r'));
		assertEquals(centeredX, cam.getX(), "Checking cam x centered on reset with control");
		assertEquals(centeredY, cam.getY(), "Checking cam y centered on reset with control");
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
		EditorEventStack stack = paint.getUndoStack();
		TestTabPainter.initNotes(tab);
		Tab oldTab = tab.copy();
		assertFalse(tab.isEmpty(), "Checking tab notes were placed");
		paint.selectAllNotes();
		keys.keySelectionDelete(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_DELETE, ' '));
		assertTrue(tab.isEmpty(), "Checking tab notes were removed on delete press");
		assertFalse(stack.isEmpty(), "Checking an event was added");
		
		assertTrue(paint.undo(), "Checking undo succeeds");
		assertEquals(oldTab, tab, "Checking notes were placed after undo");
		assertEquals(0, stack.undoSize(), "Checking no events remain after undo");
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
	public void keyUndo(){
		paint.placeNote(str5.get(0), str0, true);
		assertFalse(paint.getUndoStack().isEmpty(), "Checking undo stack has an event");
		
		keys.keyUndo(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_Z, 'z'));
		assertFalse(paint.getUndoStack().isEmpty(), "Checking undo without control didn't remove the event");
		
		keys.keyUndo(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_Z, 'z'));
		assertEquals(0, paint.getUndoStack().undoSize(), "Checking undo removed the event");
		
		keys.keyUndo(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_Z, 'z'));
		assertFalse(paint.getUndoStack().isEmpty(), "Checking undo without shift didn't remove the event");
		
		keys.keyUndo(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_Z, 'z'));
		assertEquals(1, paint.getUndoStack().undoSize(), "Checking redo added the event");
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
	public void keyNewFile(){
		FileMenu menu = gui.getZabMenuBar().getFileMenu();
		menu.setLoadedFile(new File(UtilsTest.UNIT_PATH));
		keys.keyNewFile(new KeyEvent(gui, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_N, 'n'));
		assertEquals(null, menu.getLoadedFile(), "Checking loaded file reset");
	}
	
	@Test
	public void keyTypeTabPitch(){
		paint.select(0, 0);
		
		keys.keyTypeTabPitch(new KeyEvent(paint, 0, 0, 0, 0, '1'));
		assertEquals(1, str0.getTabNumber(((TabNote)str0.get(0).getSymbol()).getPitch()), "Checking tab num updated with number");
		
		keys.keyTypeTabPitch(new KeyEvent(paint, 0, 0, 0, 0, '-'));
		assertEquals(-1, str0.getTabNumber(((TabNote)str0.get(0).getSymbol()).getPitch()), "Checking tab num updated with minus sign");
		
		paint.undo();
		assertEquals(1, str0.getTabNumber(((TabNote)str0.get(0).getSymbol()).getPitch()), "Checking tab num updated after undo");
		
		paint.redo();
		assertEquals(-1, str0.getTabNumber(((TabNote)str0.get(0).getSymbol()).getPitch()), "Checking tab num updated after redo");
	}
	
	@Test
	public void keyTypeSetModifier(){
		paint.clearSelection();
		
		assertFalse(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_P, '1')),
				"Checking nothing placed with no selection");
		
		paint.select(0, 0);
		assertFalse(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_1, '1')),
				"Checking nothing placed with unrecognized key");
		assertEquals(new TabModifier(), keyTypeSetHelper(), "Checking no modifier placed");
		
		assertFalse(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_1, '1')),
				"Checking nothing placed with control held down key");
		assertEquals(new TabModifier(), keyTypeSetHelper(), "Checking no modifier placed");
		
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_P, 'p')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "p"), keyTypeSetHelper(), "Checking pull off placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_H, 'h')),
				"Checking modifier placed");
		assertEquals(new TabModifier("h", ""), keyTypeSetHelper(), "Checking hammer on placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_SLASH, '/')),
				"Checking modifier placed");
		assertEquals(new TabModifier("/", ""), keyTypeSetHelper(), "Checking slide down placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_BACK_SLASH, '\\')),
				"Checking modifier placed");
		assertEquals(new TabModifier("\\", ""), keyTypeSetHelper(), "Checking slide up placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_B, 'b')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "b"), keyTypeSetHelper(), "Checking bend placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_R, 'r')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "ph"), keyTypeSetHelper(), "Checking pinch harmonic placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_BACK_QUOTE, '`')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "~"), keyTypeSetHelper(), "Checking vibrato placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_T, 't')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "t"), keyTypeSetHelper(), "Checking tap placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_PERIOD, '.')),
				"Checking modifier placed");
		assertEquals(new TabModifier("<", ">"), keyTypeSetHelper(), "Checking harmonic placed");
		paint.placeModifier(new TabModifier(), 0);
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_COMMA, ',')),
				"Checking modifier placed");
		assertEquals(new TabModifier("<", ">"), keyTypeSetHelper(), "Checking harmonic placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_9, '(')),
				"Checking modifier placed");
		assertEquals(new TabModifier("(", ")"), keyTypeSetHelper(), "Checking ghost note placed");
		paint.placeModifier(new TabModifier(), 0);
		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_0, ')')),
				"Checking modifier placed");
		assertEquals(new TabModifier("(", ")"), keyTypeSetHelper(), "Checking ghost note placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_P, 'p')),
				"Checking modifier placed");
		assertEquals(new TabModifier("", "p"), keyTypeSetHelper(), "Checking pull off placed");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_H, 'h')),
				"Checking modifier placed");
		assertEquals(new TabModifier("h", "p"), keyTypeSetHelper(), "Checking both modifiers exist");

		assertTrue(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_SPACE, ' ')),
				"Checking modifier removed");
		assertEquals(new TabModifier("", ""), keyTypeSetHelper(), "Checking removing modifier");

		paint.placeModifier(ModifierFactory.hammerOn(), 0, true);
		assertFalse(keys.keyTypeSetModifier(
				new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_SPACE, ' ')),
				"Checking modifier not changed");
		assertEquals(new TabModifier("h", ""), keyTypeSetHelper(), "Checking modifier unchanged with space and no shift");
		
		paint.undo();
		assertEquals(new TabModifier("", ""), keyTypeSetHelper(), "Checking modifier change undone");
		
		paint.undo();
		assertEquals(new TabModifier("h", "p"), keyTypeSetHelper(), "Checking modifier change undone");
		
		paint.redo();
		assertEquals(new TabModifier("", ""), keyTypeSetHelper(), "Checking modifier change redone");
		
		paint.redo();
		assertEquals(new TabModifier("h", ""), keyTypeSetHelper(), "Checking modifier change redone");
	}
	
	/** @return A modifier at a specific place on a tab for testing convenience */
	private TabModifier keyTypeSetHelper(){
		return paint.stringSelection(0, 0).getPos().getSymbol().getModifier();
	}
	
	@AfterEach
	public void end(){}
	
	@AfterAll
	public static void endAll(){
		UtilsTest.deleteUnitFolder();
	}
	
}
