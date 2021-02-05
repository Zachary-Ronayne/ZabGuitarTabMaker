package appMain.gui.dropMenu;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.event.ActionEvent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;
import util.testUtils.Assert;

public class TestHelpMenu{

	private static ZabGui gui;
	private HelpMenu menu;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		menu = new HelpMenu(gui);
	}
	
	@Test
	public void getControlsItem(){
		assertNotEquals(null, menu.getControlsItem(), "Checking controls item initialized");
		// Checking item in the menu
		Assert.contains(menu.getMenuComponents(), menu.getControlsItem());
	}
	
	@Test
	public void getControlsListener(){
		assertNotEquals(null, menu.getControlsListener(), "Checking controls listener initialized");
		// Checking action listener in the item
		Assert.contains(menu.getControlsItem().getActionListeners(), menu.getControlsListener());
	}
	
	@Test
	public void getControlsDialog(){
		assertNotEquals(null, menu.getControlsDialog(), "Checking controls dialog initialized");
	}
	
	@Test
	public void actionPerformedControlsListener(){
		menu.getControlsListener().actionPerformed(new ActionEvent(this, 0, ""));
	}

	@AfterEach
	public void end(){
		gui.dispose();
	}
	
}
