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
import appMain.gui.editor.paint.event.DummyEditorEvent;
import appMain.gui.editor.paint.event.EditorEventStack;
import appMain.gui.export.ExportFrame;
import appUtils.ZabAppSettings;
import appUtils.ZabFileSaver;
import tab.InstrumentFactory;
import tab.Tab;
import util.FileUtils;
import util.testUtils.UtilsTest;

public class TestZabFileChooser{

	private static ZabGui gui;
	private ZabFrame frame;
	private ZabFileChooser chooser;

	private String name;
	private File file;
	private Tab tab;
	
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		UtilsTest.createUnitFolder();
	}
	
	@BeforeEach
	public void setup(){
		gui = new ZabGui();
		gui.setVisible(false);
		frame = new ExportFrame(gui);
		chooser = new ZabFileChooser(frame);
		
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
	public void getLastLocation(){
		assertEquals(new File(ZabFileChooser.DEFAULT_SAVES_LOC), chooser.getLastLocation(),
				"Checking last location initialized");
	}
	
	@Test
	public void filePrep(){
		File f = new File(UtilsTest.UNIT_PATH + "/testPrep");
		chooser.filePrep(f);
		assertTrue(f.exists(), "Checking path was made");
		
		chooser.filePrep(f);
		assertTrue(f.exists(), "Checking path still exists after it was made");
	}
	
	@Test
	public void saveCurrentLocation(){
		chooser.setSelectedFile(null);
		assertFalse(chooser.saveCurrentLocation(), "Checking saving null fails");
		
		File f = new File(UtilsTest.UNIT_PATH + "/folder/file.txt");
		chooser.setSelectedFile(f);
		assertTrue(chooser.saveCurrentLocation(), "Checking saving succeeds with file");
		assertEquals(new File(UtilsTest.UNIT_PATH + "/folder"), chooser.getLastLocation(),
				"Checking last location set to the directory, not file");
		
		f = new File("");
		chooser.setSelectedFile(f);
		assertFalse(chooser.saveCurrentLocation(), "Checking saving fails with a file with no parent");
	}
	
	@Test
	public void saveTab(){
		file = FileUtils.extendToZab(file);
		EditorEventStack stack = gui.getEditorFrame().getTabScreen().getUndoStack();
		
		chooser.setSelectedFile(file);
		assertTrue(chooser.saveTab(), "Checking save successful");
		assertTrue(stack.isSaved(), "Checking tab marked as saved");
		
		stack.addEvent(new DummyEditorEvent());
		gui.getEditorFrame().setOpenedTab(null);
		assertFalse(chooser.saveTab(), "Checking save fails on null tab");
		assertFalse(stack.isSaved(), "Checking tab not marked as saved");
		
		gui.getEditorFrame().setOpenedTab(tab);
		chooser.setSelectedFile(null);
		assertFalse(chooser.saveTab(), "Checking save fails on null file");
		
		stack.markNotSaved();
		assertFalse(chooser.saveTab(new File(UtilsTest.UNIT_PATH + "/fakeFolder/fakeFile")), "Checking save fails on file that doesn't exist");
		assertFalse(stack.isSaved(), "Checking tab marked as not saved");

		stack.markNotSaved();
		assertTrue(chooser.saveTab(file), "Checking save succeeds");
		assertTrue(stack.isSaved(), "Checking tab marked as saved");
	}

	@Test
	public void loadTab(){
		file = FileUtils.extendToZab(file);
		chooser.setSelectedFile(file);
		assertTrue(ZabFileSaver.save(file, InstrumentFactory.guitarStandard(), true),
				"Checking save successful");
		assertTrue(chooser.loadTab(), "Checking load successful");
		assertEquals(InstrumentFactory.guitarStandard(), gui.getEditorFrame().getOpenedTab(),
				"Checking tab loaded from a file and into openedTab");
		
		assertTrue(ZabFileSaver.save(file, InstrumentFactory.bassStandard(), true),
				"Checking save successful");
		assertTrue(chooser.loadTab(), "Checking load successful");
		assertEquals(InstrumentFactory.bassStandard(), gui.getEditorFrame().getOpenedTab(),
				"Checking different tab loaded from a file and into openedTab");
	
		gui.getEditorFrame().setOpenedTab(null);
		assertTrue(ZabFileSaver.save(file, InstrumentFactory.ukuleleStandard(), true),
				"Checking save successful");
		assertTrue(chooser.loadTab(), "Checking load successful");
		assertEquals(InstrumentFactory.ukuleleStandard(), gui.getEditorFrame().getOpenedTab(),
				"Checking tab loaded from a file and into null openedTab");
		
		chooser.setSelectedFile(null);
		assertFalse(chooser.loadTab(), "Checking load fails on null file");
	}
	
	@Test
	public void exportSelect(){
		chooser.setSelectedFile(file);
		chooser.exportSelect("txt");
		file = new File(name + ".txt");
		assertEquals(file, chooser.getSelectedFile(), "Checking correct file object created");
	}

	@After
	public void end(){}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
		UtilsTest.deleteUnitFolder();
	}

}
