package appMain.gui.comp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;

public class TestZabExporterDialog{

	private static ZabGui gui;
	
	private ZabExporterDialog dialog;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
	}
	
	@BeforeEach
	public void setup(){
		dialog = new ZabExporterDialog(gui);
	}
	
	@Test
	public void getGui(){
		assertEquals(gui, dialog.getGui(), "Checking gui initialized");
	}
	
	@Test
	public void getFrame(){
		assertNotEquals(null, dialog.getFrame(), "Checking frame initialized");
	}
	
	@Test
	public void open(){
		dialog.open();
		assertTrue(dialog.isVisible(), "Checking dialog opened");
		assertTrue(dialog.isAlwaysOnTop(), "Checking dialog always on top");
		dialog.setVisible(false);
	}
	
	@Test
	public void export(){
		dialog.export();
		dialog.getFrame().setExportFile(null);
		gui.getEditorFrame().setOpenedTab(null);
	}
	
	@AfterEach
	public void end(){}
	
}
