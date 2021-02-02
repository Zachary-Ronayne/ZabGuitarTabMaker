package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabFileChooser;
import appMain.gui.comp.dropMenu.FileMenu.NewFileListener;
import appMain.gui.comp.dropMenu.FileMenu.SaveListener;
import appUtils.ZabAppSettings;
import tab.InstrumentFactory;
import tab.Tab;
import util.testUtils.Assert;
import util.testUtils.UtilsTest;

public class TestFileMenu{

	private static ZabGui gui;
	
	private FileMenu menu;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		
		UtilsTest.createUnitFolder();
	}
	
	@BeforeEach
	public void setup(){
		gui = new ZabGui();
		gui.setVisible(false);
		menu = gui.getZabMenuBar().getFileMenu();
	}
	
	@Test
	public void constructor(){
		assertEquals("File", menu.getText(), "Checking menu has correct text");
	}
	
	@Test
	public void getFileChooser(){
		assertNotEquals(null, menu.getFileChooser(), "Checking file chooser initialized");
	}
	
	@Test
	public void getLoadedFile(){
		assertEquals(null, menu.getLoadedFile(), "Checking loaded file null by default");
	}
	
	@Test
	public void setLoadedFile(){
		String name = "setLoadedFileTest";
		File f = new File(UtilsTest.UNIT_PATH + "/" + name + ".zab");
		menu.setLoadedFile(f);
		assertTrue(gui.getTitle().contains(name), "Checking the title has the name of the file");
		
		menu.setLoadedFile(null);
		assertFalse(gui.getTitle().contains(name), "Checking the title no longer has the name of the file with a null file");
	}
	
	@Test
	public void getNewFileItem(){
		// Checking save item in the list
		Assert.contains(menu.getMenuComponents(), menu.getNewFileItem());
		assertEquals("New", menu.getNewFileItem().getText(), "Checking new file item has correct name");
	}
	
	@Test
	public void getNewFileMaker(){
		assertNotEquals(null, menu.getNewFileMaker(), "Checking saver listener initialized");
		// Checking the new file button has the correct listener
		Assert.contains(menu.getNewFileItem().getActionListeners(), menu.getNewFileMaker());
	}
	
	@Test
	public void getSaveItem(){
		// Checking save item in the list
		Assert.contains(menu.getMenuComponents(), menu.getSaveItem());
		assertEquals("Save", menu.getSaveItem().getText(), "Checking save item has correct name");
	}
	
	@Test
	public void getSaveAsItem(){
		// Checking save as item in the list
		Assert.contains(menu.getMenuComponents(), menu.getSaveAsItem());
		assertEquals("Save As", menu.getSaveAsItem().getText(), "Checking save as item has correct name");
	}
	
	@Test
	public void getSaver(){
		assertNotEquals(null, menu.getSaver(), "Checking saver listener initialized");
		// Checking the save button has the correct listener
		Assert.contains(menu.getSaveItem().getActionListeners(), menu.getSaver());
	}
	
	@Test
	public void getSaveAsAction(){
		assertNotEquals(null, menu.getSaveAsAction(), "Checking saver as listener initialized");
		// Checking the save button has the correct listener
		Assert.contains(menu.getSaveAsItem().getActionListeners(), menu.getSaveAsAction());
	}
	
	@Test
	public void getLoadItem(){
		assertEquals("Load", menu.getLoadItem().getText(), "Checking load item has correct name");
	}
	
	@Test
	public void getLoader(){
		assertNotEquals(null, menu.getLoader(), "Checking loader listener initialized");
		// Checking the load button has the correct listener
		Assert.contains(menu.getLoadItem().getActionListeners(), menu.getLoader());
	}
	
	@Test
	public void getExportItem(){
		// Checking export item in the list
		Assert.contains(menu.getMenuComponents(), menu.getExportItem());
		assertEquals("Export", menu.getExportItem().getText(), "Checking export item has the correct text");
	}
	
	@Test
	public void getExporter(){
		assertNotEquals(null, menu.getExporter(), "Checking exporter initialized");
	}

	@Test
	public void getExportDialog(){
		assertNotEquals(null, menu.getExportDialog(), "Checking export dialog initialized");
	}
	
	@Test
	public void newFile(){
		menu.setLoadedFile(new File(UtilsTest.UNIT_PATH));
		gui.getEditorFrame().setOpenedTab(InstrumentFactory.guitarEbStandard());
		menu.newFile();
		assertEquals(null, menu.getLoadedFile(), "Checking loaded file set to null");
		assertEquals(InstrumentFactory.guitarStandard(), gui.getEditorFrame().getOpenedTab(), "Checking tab reset");
	}
	
	@Test
	public void save(){
		ZabFileChooser choose = menu.getFileChooser();
		
		assertEquals(null, menu.getLoadedFile(), "Checking loaded file is initially null");
		
		assertFalse(menu.save(), "Checking save fails");
		assertEquals(null, menu.getLoadedFile(), "Checking loaded file is null after failed save");
		assertEquals("Save failed", gui.getEditorFrame().getEditorBar().getFileStatusLab().getText(), "Checking save fail text set");
		
		File expectFile = new File(UtilsTest.UNIT_PATH + "/testSave.zab");
		choose.setSelectedFile(expectFile);
		assertTrue(menu.save(), "Checking save successful");
		assertEquals(expectFile, menu.getLoadedFile(),
				"Checking loaded file is now the selected file, i.e. save as succeeded");
		assertEquals("Save successful", gui.getEditorFrame().getEditorBar().getFileStatusLab().getText(), "Checking save success text set");
		
		choose.setSelectedFile(null);
		assertTrue(menu.save(), "Checking save successful");
		assertEquals(expectFile, menu.getLoadedFile(), "Checking file now saves without needing to use the file chooser");
	}
	
	@Test
	public void saveAs(){
		ZabFileChooser choose = menu.getFileChooser();
		
		assertEquals(null, menu.getLoadedFile(), "Checking loaded file is initially null");
		
		File expectFile = new File(UtilsTest.UNIT_PATH + "/testSaveAs.zab");
		choose.setSelectedFile(expectFile);
		assertTrue(menu.saveAs(), "Checking save successful");
		assertEquals(expectFile, menu.getLoadedFile(), "Checking loaded file is now the selected file");
		assertEquals("Save successful", gui.getEditorFrame().getEditorBar().getFileStatusLab().getText(), "Checking save success text set");
		
		choose.setSelectedFile(null);
		menu.setLoadedFile(null);
		assertFalse(menu.saveAs(), "Checking save fails");
		assertEquals(null, menu.getLoadedFile(), "Checking loaded file is null with failed save");
		assertEquals("Save failed", gui.getEditorFrame().getEditorBar().getFileStatusLab().getText(), "Checking save fail text set");
	}
	
	@Test
	public void load(){
		Tab uke = InstrumentFactory.ukuleleStandard();
		assertNotEquals(uke, gui.getEditorFrame().getOpenedTab(), "Checking opened tab is not the tab defined in the test");
		File f = new File(UtilsTest.UNIT_PATH + "/fileLoadTest.zab");
		assertTrue(ZabAppSettings.save(f, uke, true), "Checking file saved");
		menu.getFileChooser().setSelectedFile(f);
		assertTrue(menu.load(), "Checking load successful");
		assertEquals(uke, gui.getEditorFrame().getOpenedTab(), "Checking opened tab is now the one saved, i.e. it has loaded");
		assertEquals("Load successful", gui.getEditorFrame().getEditorBar().getFileStatusLab().getText(), "Checking load success text set");
		
		menu.getFileChooser().setSelectedFile(new File(""));
		assertFalse(menu.load(), "Checking load fails");
		assertEquals("Load failed", gui.getEditorFrame().getEditorBar().getFileStatusLab().getText(), "Checking load fail text set");
	}
	
	@Test
	public void openExportDialog(){
		menu.openExportDialog();
	}
	
	@Test
	public void actionPerformedNewFileListener(){
		NewFileListener newFile = menu.getNewFileMaker();
		
		gui.getEditorFrame().getOpenedTab().placeQuantizedNote(0, 0, 0);
		assertFalse(gui.getEditorFrame().getOpenedTab().isEmpty(), "Checking the tab has notes before the button press");

		newFile.actionPerformed(new ActionEvent(this, 0, null));
		assertTrue(gui.getEditorFrame().getOpenedTab().isEmpty(), "Checking the tab is empty after the button press");
	}
	
	@Test
	public void actionPerformedSaveListener(){
		ZabFileChooser choose = menu.getFileChooser();
		SaveListener save = menu.getSaver();
		
		assertEquals(null, menu.getLoadedFile(), "Checking loaded file is initially null");
		
		save.actionPerformed(new ActionEvent(this, 0, null));
		assertEquals(null, menu.getLoadedFile(), "Checking loaded file is null after failed save");
		
		File expectFile = new File(UtilsTest.UNIT_PATH + "/testSave.zab");
		choose.setSelectedFile(expectFile);
		save.actionPerformed(new ActionEvent(this, 0, null));
		assertEquals(expectFile, menu.getLoadedFile(),
				"Checking loaded file is now the selected file, i.e. save as succeeded");
		
		choose.setSelectedFile(null);
		save.actionPerformed(new ActionEvent(menu, 0, null));
		assertEquals(expectFile, menu.getLoadedFile(), "Checking file now saves without needing to use the file chooser");
	}
	
	@Test
	public void actionPerformedSaveAsListener(){
		ZabFileChooser choose = menu.getFileChooser();
		
		assertEquals(null, menu.getLoadedFile(), "Checking loaded file is initially null");
		menu.getSaveAsAction().actionPerformed(new ActionEvent(menu, 0, null));

		File expectFile = new File(UtilsTest.UNIT_PATH + "/testSaveAs.zab");
		choose.setSelectedFile(expectFile);
		menu.getSaveAsAction().actionPerformed(new ActionEvent(menu, 0, null));
		assertEquals(expectFile, menu.getLoadedFile(), "Checking loaded file is now the selected file");
		
		choose.setSelectedFile(null);
		menu.setLoadedFile(null);
		menu.getSaveAsAction().actionPerformed(new ActionEvent(menu, 0, null));
		assertEquals(null, menu.getLoadedFile(), "Checking loaded file is null with failed save");
	}

	@Test
	public void actionPerformedLoadListener(){
		menu.getLoader().actionPerformed(new ActionEvent(menu, 0, null));
	}
	
	@Test
	public void actionPerformedExportListener(){
		menu.getExporter().actionPerformed(new ActionEvent(menu, 0, null));
		menu.getExportDialog().setVisible(false);
		menu.getExportDialog().dispose();
	}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
		UtilsTest.deleteUnitFolder();
	}
	
}
