package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;
import util.testUtils.Assert;

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
		// Checking theme menu in the graphics menu
		Assert.contains(menu.getMenuComponents(), menu.getThemeSubMenu());
	}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
	}
	
}
