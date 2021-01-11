package appMain.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabTheme.ButtonHoverSensor;
import appUtils.ZabAppSettings;

public class TestZabTheme{

	private JButton button;
	private ButtonHoverSensor sensor;
	
	private ZabTheme dark;
	private ZabTheme light;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		button = new JButton();
		sensor = new ButtonHoverSensor(button);
		
		dark = new ZabTheme.DarkTheme();
		light = new ZabTheme.LightTheme();
	}
	
	@Test
	public void setToTheme(){
		JPanel panel = new JPanel();
		JMenu menu = new JMenu();
		JMenuBar bar = new JMenuBar();
		@SuppressWarnings("serial")
		Component component = new Component(){};
		ZabTheme.setToTheme(panel);
		ZabTheme.setToTheme(menu);
		ZabTheme.setToTheme(bar);
		
		// Running on non JComponent
		ZabTheme.setToTheme(component);
		
		// Running on null value
		ZabTheme.setToTheme(null);
		
		assertEquals(new Color(30, 30, 30), panel.getBackground(), "Checking background correct");
		assertTrue(panel.getBorder() instanceof EmptyBorder, "Checking border correct");
		
		assertEquals(new Color(50, 50, 50), bar.getBackground(), "Checking background correct");
		
		assertTrue(menu.getPopupMenu().getBorder() instanceof LineBorder, "Checking border correct");

		ZabTheme.setToTheme(button);
		assertEquals(new Color(30, 30, 30), button.getBackground(), "Checking background correct");
		assertTrue(button.isOpaque(), "Checking button is set to opaque");
		boolean found = false;
		MouseListener[] m = button.getMouseListeners();
		for(int i = 0; i < m.length; i++){
			if(m[i] instanceof ButtonHoverSensor) found = true;
		}
		assertTrue(found, "Checking a ButtonHoverSensor was added");
		ZabTheme.setToTheme(button);
		
		found = false;
		m = button.getMouseListeners();
		for(int i = 0; i < m.length; i++){
			if(m[i] instanceof ButtonHoverSensor) found = true;
		}
		assertTrue(found, "Checking a ButtonHoverSensor is still there");
		ZabTheme.setToTheme(button);
	}
	
	@Test
	public void setUIMangerTheme(){
		ZabTheme t = dark;
		ZabTheme.setUIMangerTheme(t);
		assertEquals(t.menuBarMouseHover(), UIManager.get("MenuItem.selectionBackground"), "Checking UIManager key set");
	}
	
	@Test
	public void printDefaultUI(){
		ZabTheme.printDefaultUI();
	}
	
	@Test
	public void getDefaultTheme(){
		assertTrue(ZabTheme.getDefaultTheme() instanceof ZabTheme.DarkTheme, "Checking theme has correct type");
	}
	
	
	@Test
	public void backgroundDarkTheme(){
		assertEquals(new Color(30, 30, 30), dark.background());
	}
	
	@Test
	public void foregroundDarkTheme(){
		assertEquals(new Color(180, 180, 180), dark.foreground());
	}
	
	@Test
	public void buttonClickDarkTheme(){
		assertEquals(new Color(70, 70, 70), dark.buttonClick());
	}
	
	@Test
	public void buttonHoverDarkTheme(){
		assertEquals(new Color(80, 80, 80), dark.buttonHover());
	}
	
	@Test
	public void menuMouseClickDarkTheme(){
		assertEquals(new Color(70, 70, 70), dark.menuMouseClick());
	}
	
	@Test
	public void menuMouseClickTextDarkTheme(){
		assertEquals(new Color(150, 150, 150), dark.menuMouseClickText());
	}
	
	@Test
	public void menuBarBackgroundDarkTheme(){
		assertEquals(new Color(50, 50, 50), dark.menuBarBackground());
	}
	
	@Test
	public void menuBarMouseHoverDarkTheme(){
		assertEquals(new Color(80, 80, 80), dark.menuBarMouseHover());
	}
	
	@Test
	public void menuBarMouseHoverTextDarkTheme(){
		assertEquals(new Color(150, 150, 150), dark.menuBarMouseHoverText());
	}
	
	@Test
	public void borderColorDarkTheme(){
		assertEquals(new Color(100, 100, 100), dark.borderColor());
	}
	
	@Test
	public void tabStringDarkTheme(){
		assertEquals(new Color(60, 60, 60), dark.tabString());
	}
	
	@Test
	public void tabSymbolTextDarkTheme(){
		assertEquals(new Color(200, 200, 200), dark.tabSymbolText());
	}
	
	@Test
	public void tabSymbolHighlightDarkTheme(){
		assertEquals(new Color(170, 170, 255, 100), dark.tabSymbolHighlight());
	}
	
	
	@Test
	public void backgroundLightTheme(){
		assertEquals(new Color(240, 240, 240), light.background());
	}
	
	@Test
	public void foregroundLightTheme(){
		assertEquals(new Color(0, 0, 0), light.foreground());
	}
	
	@Test
	public void buttonClickLightTheme(){
		assertEquals(new Color(200, 200, 255), light.buttonClick());
	}
	
	@Test
	public void buttonHoverLightTheme(){
		assertEquals(new Color(220, 220, 255), light.buttonHover());
	}
	
	@Test
	public void menuMouseClickLightTheme(){
		assertEquals(new Color(200, 200, 255), light.menuMouseClick());
	}
	
	@Test
	public void menuMouseClickTextLightTheme(){
		assertEquals(new Color(0, 0, 20), light.menuMouseClickText());
	}
	
	@Test
	public void menuBarBackgroundLightTheme(){
		assertEquals(new Color(200, 200, 200), light.menuBarBackground());
	}
	
	@Test
	public void menuBarMouseHoverLightTheme(){
		assertEquals(new Color(220, 220, 255), light.menuBarMouseHover());
	}
	
	@Test
	public void menuBarMouseHoverTextLightTheme(){
		assertEquals(new Color(0, 0, 20), light.menuBarMouseHoverText());
	}
	
	@Test
	public void borderColorLightTheme(){
		assertEquals(new Color(0, 0, 0), light.borderColor());
	}
	
	@Test
	public void tabStringLightTheme(){
		assertEquals(new Color(150, 150, 150), light.tabString());
	}
	
	@Test
	public void tabSymbolTextLightTheme(){
		assertEquals(new Color(20, 20, 20), light.tabSymbolText());
	}
	
	@Test
	public void tabSymbolHighlightLightTheme(){
		assertEquals(new Color(70, 70, 255, 100), light.tabSymbolHighlight());
	}
	
	@Test
	public void mouseEnteredButtonHoverSensor(){
		button.setBackground(Color.RED);
		sensor.mouseEntered(new MouseEvent(button, 0, 0, 0, 0, 0, 0, 0, 0, false, 0));
		assertEquals(dark.buttonHover(), button.getBackground());
	}
	
	@Test
	public void mouseExitedButtonHoverSensor(){
		button.setBackground(Color.RED);
		sensor.mouseExited(new MouseEvent(button, 0, 0, 0, 0, 0, 0, 0, 0, false, 0));
		assertEquals(dark.background(), button.getBackground());
	}

	@AfterEach
	public void end(){}

}
