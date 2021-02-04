package appMain.gui.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestZabMenuItem{
	
	private ZabMenuItem item;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		item = new ZabMenuItem("text");
	}

	@Test
	public void constructor(){
		assertEquals(new Color(30, 30, 30), item.getBackground(), "Checking background set");
		assertEquals("text", item.getText(), "Checking name set");
	}
	
	@AfterEach
	public void end(){}

}
