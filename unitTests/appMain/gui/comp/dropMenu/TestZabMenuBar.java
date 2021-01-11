package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.Color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;

public class TestZabMenuBar{

	private ZabMenuBar bar;
	
	private static ZabGui gui;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
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
	}
	
	@Test
	public void getEditMenu(){
		ZabMenu menu = bar.getEditMenu();
		assertNotEquals(null, menu, "Checking menu initialized");
	}
	
	@Test
	public void getGraphicsMenu(){
		ZabMenu menu = bar.getGraphicsMenu();
		assertNotEquals(null, menu, "Checking menu initialized");
	}
		
	@AfterEach
	public void end(){}

}
