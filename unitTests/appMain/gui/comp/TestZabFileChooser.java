package appMain.gui.comp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.frames.ExporterFrame;
import appMain.gui.frames.ZabFrame;
import appUtils.ZabAppSettings;
import tab.InstrumentFactory;
import tab.Tab;
import util.FileUtils;
import util.testUtils.UtilsTest;

public class TestZabFileChooser{

	private static ZabGui gui;
	private static ZabFrame frame;
	private static ZabFileChooser chooser;

	private String name;
	private File file;
	private Tab tab;
	
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
		frame = new ExporterFrame(gui);
		chooser = new ZabFileChooser(frame);
		UtilsTest.createUnitFolder();
	}
	
	@BeforeEach
	public void setup(){
		name = FileUtils.makeFileName(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME);
		file = new File(name);
		tab = new Tab();
		gui.getEditorFrame().setOpenedTab(tab);
	}
	
	@Test
	public void getGui(){
		assertEquals(gui, chooser.getGui(), "Checking gui initialized");
	}
	
	@Test
	public void getZabFileFilter(){
		assertNotEquals(null, chooser.getZabFileFilter(), "Checking file filter initialized");
	}
	
	@Test
	public void filePrep(){
		String path = UtilsTest.UNIT_PATH + "/testPrep";
		chooser.filePrep(path);
		assertTrue(new File(path).exists(), "Checking path was made");
		
		chooser.filePrep(path);
		assertTrue(new File(path).exists(), "Checking path still exists after it was made");
	}
	
	@Test
	public void exportSelect(){
		chooser.setSelectedFile(file);
		chooser.exportSelect("txt");
		file = new File(name + ".txt");
		assertEquals(file, chooser.getSelectedFile(), "Checking correct file object created");
	}
	
	@Test
	public void loadTab(){
		file = FileUtils.extendToZab(file);
		chooser.setSelectedFile(file);
		assertTrue(ZabAppSettings.save(file, InstrumentFactory.guitarStandard(), true),
				"Checking save successful");
		assertTrue(chooser.loadTab(), "Checking load successful");
		assertEquals(InstrumentFactory.guitarStandard(), gui.getEditorFrame().getOpenedTab(),
				"Checking tab loaded from a file and into openedTab");
		
		assertTrue(ZabAppSettings.save(file, InstrumentFactory.bassStandard(), true),
				"Checking save successful");
		assertTrue(chooser.loadTab(), "Checking load successful");
		assertEquals(InstrumentFactory.bassStandard(), gui.getEditorFrame().getOpenedTab(),
				"Checking different tab loaded from a file and into openedTab");

		gui.getEditorFrame().setOpenedTab(null);
		assertTrue(ZabAppSettings.save(file, InstrumentFactory.ukuleleStandard(), true),
				"Checking save successful");
		assertTrue(chooser.loadTab(), "Checking load successful");
		assertEquals(InstrumentFactory.ukuleleStandard(), gui.getEditorFrame().getOpenedTab(),
				"Checking tab loaded from a file and into null openedTab");
		
		chooser.setSelectedFile(null);
		assertFalse(chooser.loadTab(), "Checking load fails on null file");
	}
	
	@Test
	public void saveTab(){
		file = FileUtils.extendToZab(file);
		
		chooser.setSelectedFile(file);
		assertTrue(chooser.saveTab(), "Checking save successful");
		
		gui.getEditorFrame().setOpenedTab(null);
		assertFalse(chooser.saveTab(), "Checking save fails on null tab");

		gui.getEditorFrame().setOpenedTab(tab);
		chooser.setSelectedFile(null);
		assertFalse(chooser.saveTab(), "Checking save fails on null file");
	}
	
	@After
	public void end(){
		UtilsTest.deleteUnitFolder();
	}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
	}

}
