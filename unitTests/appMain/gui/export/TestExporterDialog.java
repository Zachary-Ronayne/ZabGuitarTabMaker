package appMain.gui.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.editor.frame.FileStatusLabel;
import appUtils.ZabAppSettings;
import util.testUtils.UtilsTest;

public class TestExporterDialog{

	private static ZabGui gui;
	
	private ExportDialog dialog;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
		UtilsTest.createUnitFolder();
	}
	
	@BeforeEach
	public void setup(){
		dialog = new ExportDialog(gui);
		dialog.setVisible(false);
	}
	
	@Test
	public void open(){
		dialog.open();
	}
	
	@Test
	public void export(){
		FileStatusLabel lab = gui.getEditorFrame().getEditorBar().getFileStatusLab();
		dialog.getFrame().setExportFile(new File(UtilsTest.UNIT_PATH + "/ZabExporterDialogExportTest"));
		assertTrue(dialog.export(), "Checking export succeeds");
		assertEquals("Export successful", lab.getText(), "Checking export success text set");
		
		dialog.getFrame().setExportFile(null);
		gui.getEditorFrame().setOpenedTab(null);
		assertFalse(dialog.export(), "Checking export fails");
		assertEquals("Export failed", lab.getText(), "Checking export fail text set");
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
