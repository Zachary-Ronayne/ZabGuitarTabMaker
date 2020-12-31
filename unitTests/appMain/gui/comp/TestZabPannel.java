package appMain.gui.comp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestZabPannel{
	
	private ZabPanel panel;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		panel = new ZabPanel();
	}
	
	@Test
	public void constructor(){
		assertEquals(new Color(40, 40, 40), panel.getBackground(), "Checking background set");
	}
	
	@AfterEach
	public void end(){}

}
