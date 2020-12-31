package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestZabMenu{
	
	private ZabMenu menu;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		menu = new ZabMenu("name");
	}
	
	@Test
	public void constructor(){
		assertEquals(new Color(40, 40, 40), menu.getBackground(), "Checking background set");
		assertEquals("name", menu.getText(), "Checking text initialized");
	}
	
	@AfterEach
	public void end(){}

}
