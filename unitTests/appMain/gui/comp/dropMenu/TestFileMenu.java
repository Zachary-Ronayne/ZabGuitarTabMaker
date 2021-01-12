package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.event.ActionEvent;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;
import util.testUtils.Assert;

public class TestFileMenu{

	private static ZabGui gui;
	
	private FileMenu menu;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
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
	public void getSaveItem(){
		assertEquals("Save", menu.getSaveItem().getText(), "Checking save item has correct name");
	}
	
	@Test
	public void getSaver(){
		assertNotEquals(null, menu.getSaver(), "Checking saver listener initialized");
		// Checking the save button has the correct listener
		Assert.contains(menu.getSaveItem().getActionListeners(), menu.getSaver());
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
		assertEquals(menu.getMenuComponent(2), menu.getExportItem(), "Checking export item is the correct index");
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
	public void actionPerformedSaveListener(){
		menu.getSaver().actionPerformed(new ActionEvent(menu, 0, null));
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
	}
	
}
