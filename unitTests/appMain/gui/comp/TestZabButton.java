package appMain.gui.comp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestZabButton{

	private ZabButton button;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		button = new ZabButton();
	}
	
	@Test
	public void constructor(){
		assertEquals(new Color(30, 30, 30), button.getBackground(), "Checking background set");
	}
	
	@Test
	public void setFontSize(){
		button.setFontSize(23);
		assertEquals(23, button.getFont().getSize(), "Checking font size set");
		
		button.setFontSize(2);
		assertEquals(2, button.getFont().getSize(), "Checking font size set");

		button.setFontSize(-1);
		assertEquals(2, button.getFont().getSize(), "Checking font size unchanged on negative");
	}
	
	@AfterEach
	public void end(){}
	
}
