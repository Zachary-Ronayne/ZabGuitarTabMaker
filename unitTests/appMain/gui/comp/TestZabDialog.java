package appMain.gui.comp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;
import util.testUtils.Assert;
import util.testUtils.UtilsTest;

public class TestZabDialog{

	private static ZabGui gui;
	
	private ZabDialog dialog;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		
		gui = new ZabGui();
		gui.setVisible(false);
		UtilsTest.createUnitFolder();
	}
	
	@BeforeEach
	public void setup(){
		dialog = new ZabDialog(gui, new TestZabFrame.TestFrame(gui));
		dialog.setVisible(false);
	}
	
	@Test
	public void getGui(){
		assertEquals(gui, dialog.getGui(), "Checking gui initialized");
	}
	
	@Test
	public void getFrame(){
		assertNotEquals(null, dialog.getFrame(), "Checking frame initialized");
		// Checking frame in the dialog
		Assert.contains(dialog.getContentPane().getComponents(), dialog.getFrame());
	}
	
	@AfterEach
	public void end(){
		dialog.setVisible(false);
		dialog.dispose();
	}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
		UtilsTest.deleteUnitFolder();
	}
}
