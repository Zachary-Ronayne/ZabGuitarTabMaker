package appMain.gui.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.Color;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;
import util.testUtils.Assert;

public class TestZabMenuBar{

	private ZabMenuBar bar;
	
	private static ZabGui gui;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		bar = gui.getZabMenuBar();
	}
	
	@Test
	public void constructor(){
		assertEquals(new Color(50, 50, 50), bar.getBackground(), "Checking background set");
	}
	
	@Test
	public void getGui(){
		assertEquals(gui, bar.getGui(), "Checking gui initialized");
	}

	@Test
	public void getFileMenu(){
		ZabMenu menu = bar.getFileMenu();
		assertNotEquals(null, menu, "Checking menu initialized");
		// Checking menu bar contains the menu
		Assert.contains(bar.getComponents(), menu);
	}
	
	@Test
	public void getEditMenu(){
		ZabMenu menu = bar.getEditMenu();
		assertNotEquals(null, menu, "Checking menu initialized");
		// Checking menu bar contains the menu
		Assert.contains(bar.getComponents(), menu);
	}
	
	@Test
	public void getGraphicsMenu(){
		ZabMenu menu = bar.getGraphicsMenu();
		assertNotEquals(null, menu, "Checking menu initialized");
		// Checking menu bar contains the menu
		Assert.contains(bar.getComponents(), menu);
	}
	
	@Test
	public void getHelpMenu(){
		ZabMenu menu = bar.getHelpMenu();
		assertNotEquals(null, menu, "Checking menu initialized");
		// Checking menu bar contains the menu
		Assert.contains(bar.getComponents(), menu);
	}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
	}

}
