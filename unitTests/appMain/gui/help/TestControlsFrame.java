package appMain.gui.help;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;
import util.testUtils.Assert;

public class TestControlsFrame{

	private static ZabGui gui;
	private ControlsFrame frame;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		frame = new ControlsFrame(gui);
	}
	
	@Test
	public void getTitle(){
		assertEquals("Controls", frame.getTitle().getText(), "Checking title text correct");
	}
	
	@Test
	public void getMainPanel(){
		assertNotEquals(null, frame.getMainPanel(), "Checking main panel initialized");
		// Checking main panel is in the frame
		Assert.contains(frame.getComponents(), frame.getMainPanel());
	}
	
	@Test
	public void getMouseInfo(){
		assertNotEquals(null, frame.getMouseInfo(), "Checking mouse info initialized");
		// Checking list is in the main panel
		Assert.contains(frame.getMainPanel().getComponents(), frame.getMouseInfo());
	}
	
	@Test
	public void getKeyInfo(){
		assertNotEquals(null, frame.getKeyInfo(), "Checking key info initialized");
		// Checking list is in the main panel
		Assert.contains(frame.getMainPanel().getComponents(), frame.getKeyInfo());
	}
	
	@Test
	public void getModifiersInfo(){
		assertNotEquals(null, frame.getModifiersInfo(), "Checking modifiers info initialized");
		// Checking list is in the main panel
		Assert.contains(frame.getMainPanel().getComponents(), frame.getModifiersInfo());
	}
	
	@Test
	public void getZoomInfo(){
		assertNotEquals(null, frame.getZoomInfo(), "Checking zoom info initialized");
		// Checking list is in the frame
		Assert.contains(frame.getComponents(), frame.getZoomInfo());
	}
	
	@Test
	public void parentResized(){
		frame.parentResized(0, 0);
	}
	
	@AfterEach
	public void end(){
		gui.dispose();
	}
	
}
