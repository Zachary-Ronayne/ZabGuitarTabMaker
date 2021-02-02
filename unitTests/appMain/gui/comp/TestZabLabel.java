package appMain.gui.comp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Font;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestZabLabel{
	
	private ZabLabel label;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		label = new ZabLabel();
	}
	
	@Test
	public void constructor(){
		assertEquals(new Font("Arial", Font.PLAIN, 20), label.getFont(), "Checking font initialized");
		assertEquals("", label.getText(), "Checking text is empty");
		
		label = new ZabLabel("words");
		assertEquals("words", label.getText(), "Checking text is initialized");
	}
	
	@Test
	public void setFontSize(){
		label.setFontSize(10);
		assertEquals(10, label.getFont().getSize(), "Checking font size set");
	}
	
	@AfterEach
	public void end(){}
	
}
