package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.event.ActionEvent;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appMain.gui.ZabTheme.DarkTheme;
import appMain.gui.ZabTheme.LightTheme;
import appMain.gui.comp.dropMenu.ThemeMenu.DarkThemeListener;
import appMain.gui.comp.dropMenu.ThemeMenu.LightThemeListener;
import appUtils.ZabAppSettings;
import util.testUtils.Assert;

public class TestThemeMenu{
	
	private static ZabGui gui;

	private ThemeMenu menu;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		menu = gui.getZabMenuBar().getGraphicsMenu().getThemeSubMenu();
	}
	
	@Test
	public void constructor(){
		assertEquals("Theme", menu.getText(), "Checking correct text set");
	}
	
	@Test
	public void getDarkThemeItem(){
		ZabMenuItem item = menu.getDarkThemeItem();
		assertNotEquals(null, item, "Checking dark item initialized");
		assertEquals("Dark Theme", item.getText(), "Checking dark item has correct text");
		// Checking item in menu
		Assert.contains(menu.getMenuComponents(), item);
	}
	
	@Test
	public void getDarkSetter(){
		DarkThemeListener lis = menu.getDarkSetter();
		ZabMenuItem item = menu.getDarkThemeItem();
		assertNotEquals(null, lis, "Checking dark setter initialized");
		// Checking listener in item
		Assert.contains(item.getActionListeners(), lis);
	}
	
	@Test
	public void getLightThemeItem(){
		ZabMenuItem item = menu.getLightThemeItem();
		assertNotEquals(null, item, "Checking dark item initialized");
		assertEquals("Light Theme", item.getText(), "Checking light item has correct text");
		// Checking item in menu
		Assert.contains(menu.getMenuComponents(), item);
	}
	
	@Test
	public void getLightSetter(){
		LightThemeListener lis = menu.getLightSetter();
		ZabMenuItem item = menu.getLightThemeItem();
		assertNotEquals(null, lis, "Checking light setter initialized");
		// Checking listener in item
		Assert.contains(item.getActionListeners(), lis);
	}
	
	@Test
	public void actionPerformedDarkThemeListener(){
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		menu.getDarkSetter().actionPerformed(new ActionEvent(gui, 0, null));
		menu.getGui().setVisible(false);
		Assert.isInstance(DarkTheme.class, ZabAppSettings.theme(), "Checking dark theme set");
	}
	
	@Test
	public void actionPerformedLightThemeListener(){
		ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), null, false);
		menu.getLightSetter().actionPerformed(new ActionEvent(gui, 0, null));
		menu.getGui().setVisible(false);
		Assert.isInstance(LightTheme.class, ZabAppSettings.theme(), "Checking light theme set");
	}

	@AfterAll
	public static void endAll(){
		gui.dispose();
	}
	
}
