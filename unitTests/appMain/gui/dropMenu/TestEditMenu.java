package appMain.gui.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.ActionEvent;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.editor.paint.TabPainter;
import appUtils.ZabAppSettings;
import tab.TabFactory;
import tab.TabString;
import util.testUtils.Assert;

public class TestEditMenu{
	
	private static ZabGui gui;
	
	private EditMenu menu;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		gui.getEditorFrame().getTabScreen().reset();
		menu = gui.getZabMenuBar().getEditMenu();
	}
	
	@Test
	public void constructor(){
		assertEquals("Edit", menu.getText(), "Checking menu has correct text");
	}
	
	@Test
	public void getUndoItem(){
		ZabMenuItem item = menu.getUndoItem();
		assertNotEquals(null, item, "Checking item initialized");
		assertEquals("Undo", item.getText(), "Checking item has correct text");
		// Checking undo item in menu
		Assert.contains(menu.getMenuComponents(), item);
	}
	
	@Test
	public void getUndoListener(){
		assertNotEquals(null, menu.getUndoListener(), "Checking listener initialized");
		
		// Checking listener in item
		Assert.contains(menu.getUndoItem().getActionListeners(), menu.getUndoListener());
	}
	
	@Test
	public void getRedoItem(){
		ZabMenuItem item = menu.getRedoItem();
		assertNotEquals(null, item, "Checking item initialized");
		assertEquals("Redo", item.getText(), "Checking item has correct text");
		// Checking redo item in menu
		Assert.contains(menu.getMenuComponents(), item);
	}	
	
	@Test
	public void getRedoListener(){
		assertNotEquals(null, menu.getRedoListener(), "Checking listener initialized");
		
		// Checking listener in item
		Assert.contains(menu.getRedoItem().getActionListeners(), menu.getRedoListener());
	}
	
	@Test
	public void actionPerformedUndoListener(){
		TabPainter paint = menu.getPainter();
		TabString str = paint.getTab().getStrings().get(0);
		assertTrue(str.isEmpty(), "Checking no notes exist");
		assertTrue(paint.placeNote(TabFactory.modifiedFret(str, 0, 0), str, true), "Checking note placed");
		assertFalse(str.isEmpty(), "Checking note in place");
		
		menu.getUndoListener().actionPerformed(new ActionEvent(this, 0, ""));
		assertTrue(str.isEmpty(), "Checking notes removed");
	}
	
	@Test
	public void actionPerformedRedoListener(){
		TabPainter paint = menu.getPainter();
		TabString str = paint.getTab().getStrings().get(0);
		assertTrue(str.isEmpty(), "Checking no notes exist");
		assertTrue(paint.placeNote(TabFactory.modifiedFret(str, 0, 0), str, true), "Checking note placed");
		assertFalse(str.isEmpty(), "Checking note in place");
		
		paint.undo();
		assertTrue(str.isEmpty(), "Checking notes removed");

		menu.getRedoListener().actionPerformed(new ActionEvent(this, 0, ""));
		assertFalse(str.isEmpty(), "Checking note placed back after redo");
	}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
	}
	
}
