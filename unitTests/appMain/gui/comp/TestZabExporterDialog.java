package appMain.gui.comp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
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
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		dialog = new ZabExporterDialog(gui);
		dialog.setVisible(false);
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
		assertTrue(dialog.isAlwaysOnTop(), "Checking dialog always on top");
	}
	
	@Test
	public void export(){
		dialog.export();
		dialog.getFrame().setExportFile(null);
		gui.getEditorFrame().setOpenedTab(null);
	}
	
	@AfterEach
	public void end(){
		dialog.setVisible(false);
		dialog.dispose();
	}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
	}
	
}
