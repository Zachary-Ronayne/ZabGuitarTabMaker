package appMain.gui.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;
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
	public void getRedoItem(){
		ZabMenuItem item = menu.getRedoItem();
		assertNotEquals(null, item, "Checking item initialized");
		assertEquals("Redo", item.getText(), "Checking item has correct text");
		// Checking redo item in menu
		Assert.contains(menu.getMenuComponents(), item);
	}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
	}
	
}
