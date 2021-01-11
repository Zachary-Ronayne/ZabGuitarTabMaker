package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appMain.gui.comp.dropMenu.ThemeMenu.DarkThemeListener;
import appMain.gui.comp.dropMenu.ThemeMenu.LightThemeListener;
import appUtils.ZabAppSettings;

public class TestThemeMenu{
	
	private static ZabGui gui;

	private ThemeMenu menu;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
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
		assertTrue(Arrays.asList(menu.getMenuComponents()).contains(item), "Checking item in menu");
	}
	
	@Test
	public void getDarkSetter(){
		DarkThemeListener lis = menu.getDarkSetter();
		ZabMenuItem item = menu.getDarkThemeItem();
		assertNotEquals(null, lis, "Checking dark setter initialized");
		assertTrue(Arrays.asList(item.getActionListeners()).contains(lis), "Checking listener in item");
	}
	
	@Test
	public void getLightThemeItem(){
		ZabMenuItem item = menu.getLightThemeItem();
		assertNotEquals(null, item, "Checking dark item initialized");
		assertEquals("Light Theme", item.getText(), "Checking light item has correct text");
		assertTrue(Arrays.asList(menu.getMenuComponents()).contains(item), "Checking item in menu");
	}
	
	@Test
	public void getLightSetter(){
		LightThemeListener lis = menu.getLightSetter();
		ZabMenuItem item = menu.getLightThemeItem();
		assertNotEquals(null, lis, "Checking light setter initialized");
		assertTrue(Arrays.asList(item.getActionListeners()).contains(lis), "Checking listener in item");
	}
	
	@Test
	public void actionPerformedDarkThemeListener(){
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		menu.getDarkSetter().actionPerformed(new ActionEvent(gui, 0, null));
		menu.getGui().setVisible(false);
		assertTrue(ZabAppSettings.theme() instanceof ZabTheme.DarkTheme, "Checking dark theme set");
	}
	
	@Test
	public void actionPerformedLightThemeListener(){
		ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), null, false);
		menu.getLightSetter().actionPerformed(new ActionEvent(gui, 0, null));
		menu.getGui().setVisible(false);
		assertTrue(ZabAppSettings.theme() instanceof ZabTheme.LightTheme, "Checking light theme set");
	}
	
	@AfterEach
	public void end(){}
	
}
