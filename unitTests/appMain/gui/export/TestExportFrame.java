package appMain.gui.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabButton;
import appUtils.ZabAppSettings;
import lang.Language;
import util.testUtils.UtilsTest;

public class TestExportFrame{

	private static ZabGui gui;
	
	private ExportFrame frame;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		frame = new ExportFrame(gui);
	}
	
	@Test
	public void getFileSelectButton(){
		ZabButton b = frame.getFileSelectButton();
		assertEquals(Language.get().noPathSelected(), b.getText(), "Checking text set on export button");
	}
	
	@Test
	public void getFileSelector(){
		assertNotEquals(null, frame.getFileSelector(), "Checking file selector initialized");
	}

	@Test
	public void getFileChooser(){
		assertNotEquals(null, frame.getFileChooser(), "Checking file chooser initialized");
	}
	
	@Test
	public void getExportDialog(){
		assertNotEquals(null, frame.getExportDialog(), "Checking export dialog initialized");
	}
	
	@Test
	public void getExportButton(){
		ZabButton b = frame.getExportButton();
		assertEquals(Language.get().export(), b.getText(), "Checking button has correct text");
	}

	@Test
	public void getExporter(){
		assertNotEquals(null, frame.getExporter(), "Checking file exporter initialized");
	}
	
	@Test
	public void getExportFile(){
		assertEquals(null, frame.getExportFile(), "Checking export file initially null");
	}
	
	@Test
	public void setExportFile(){
		ZabButton b = frame.getFileSelectButton();
		File file = UtilsTest.getUnitFolder();
		
		frame.setExportFile(file);
		assertEquals(file, frame.getExportFile(), "Checking export file set");
		assertEquals(UtilsTest.UNIT_PATH, b.getText(), "Checking text set on export button");

		frame.setExportFile(null);
		assertEquals(null, frame.getExportFile(), "Checking export file set to null");
		assertEquals(Language.get().noPathSelected(), b.getText(), "Checking text set on export button");
	}
	
	@Test
	public void parentResized(){
		frame.parentResized(1000, 1000);
	}
	
	@Test
	public void actionPerformedFileSelectListener(){
		frame.getFileSelector().actionPerformed(new ActionEvent(frame, 0, null));
	}
	
	@Test
	public void actionPerformedExportListener(){
		frame.getExporter().actionPerformed(new ActionEvent(frame, 0, null));
	}
	
	@AfterAll
	public static void end(){
		UtilsTest.deleteUnitFolder();
	}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
	}
	
}
