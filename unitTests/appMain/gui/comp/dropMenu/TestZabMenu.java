package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;

public class TestZabMenu{
	
	private static ZabGui gui;
	
	private ZabMenu menu;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		menu = new ZabMenu("name", gui);
	}
	
	@Test
	public void constructor(){
		assertEquals(new Color(30, 30, 30), menu.getBackground(), "Checking background set");
		assertEquals("name", menu.getText(), "Checking text initialized");
	}
	
	@AfterEach
	public void end(){}

}
