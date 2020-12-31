package appMain.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestZabTheme{

	private JPanel panel;
	private JButton button;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		panel = new JPanel();
		button = new JButton();
	}
	
	@Test
	public void setToTheme(){
		ZabTheme.setToTheme(panel);
		ZabTheme.setToTheme(button);
		
		assertEquals(new Color(40, 40, 40), panel.getBackground(), "Checking background correct");
		assertEquals(new Color(180, 180, 180), panel.getForeground(), "Checking foreground correct");
		
		assertEquals(new Color(40, 40, 40), button.getBackground(), "Checking background correct");
		assertEquals(new Color(180, 180, 180), button.getForeground(), "Checking foreground correct");
	}
	
	@AfterEach
	public void end(){}

}
