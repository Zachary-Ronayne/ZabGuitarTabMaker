package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;

public class TestGraphicsMenu{
	
	private static ZabGui gui;

	private GraphicsMenu menu;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		menu = gui.getZabMenuBar().getGraphicsMenu();
	}
	
	@Test
	public void constructor(){
		assertEquals("Graphics", menu.getText(), "Checking menu has correct text");
	}
	
	@Test
	public void getThemeSubMenu(){
		assertNotEquals(null, menu.getThemeSubMenu(), "Checking theme menu initialized");
		assertTrue(Arrays.asList(menu.getMenuComponents()).contains(menu.getThemeSubMenu()), "Checking theme menu in the graphics menu");
	}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
	}
	
}
