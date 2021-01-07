package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractButton;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;

public class TestZabMenuBar{

	private ZabMenuBar bar;
	
	private static ZabGui gui;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
	}
	
	@BeforeEach
	public void setup(){
		bar = gui.getZabMenuBar();
	}
	
	@Test
	public void constructor(){
		assertEquals(new Color(40, 40, 40), bar.getBackground(), "Checking background set");
	}
	
	@Test
	public void getGui(){
		assertEquals(gui, bar.getGui(), "Checking gui initialized");
	}

	@Test
	public void getFileMenu(){
		ZabMenu menu = bar.getFileMenu();
		assertEquals("File", menu.getText(), "Checking correct name of menu");
		assertEquals(3, menu.getMenuComponentCount(), "Checking correct number of items");
		assertEquals(bar.getSaveItem(), menu.getMenuComponent(0), "Checking correct item order");
		assertEquals(bar.getLoadItem(), menu.getMenuComponent(1), "Checking correct item order");
		assertEquals(bar.getExportItem(), menu.getMenuComponent(2), "Checking correct item order");
	}
	
	@Test
	public void getFileChooser(){
		assertNotEquals(null, bar.getFileChooser(), "Checking file chooser initialized");
	}
	
	@Test
	public void getSaveItem(){
		assertEquals("Save", bar.getSaveItem().getText(), "Checking save item has correct name");
	}
	
	@Test
	public void getSaver(){
		assertNotEquals(null, bar.getSaver(), "Checking saver listener initialized");
		assertTrue(Arrays.asList(bar.getSaveItem().getActionListeners()).contains(bar.getSaver()),
				"Checking the save button has the correct listener");
	}
	
	@Test
	public void getLoadItem(){
		assertEquals("Load", bar.getLoadItem().getText(), "Checking load item has correct name");
	}
	
	@Test
	public void getLoader(){
		assertNotEquals(null, bar.getLoader(), "Checking loader listener initialized");
		assertTrue(Arrays.asList(bar.getLoadItem().getActionListeners()).contains(bar.getLoader()),
				"Checking the load button has the correct listener");
	}
	
	@Test
	public void getExportItem(){
		ZabMenu menu = bar.getFileMenu();
		assertEquals(menu.getMenuComponent(2), bar.getExportItem(), "Checking export item is the correct index");
		assertEquals("Export", bar.getExportItem().getText(), "Checking export item has the correct text");
	}
	
	@Test
	public void getExporter(){
		assertNotEquals(null, bar.getExporter(), "Checking exporter initialized");
	}

	@Test
	public void getExportDialog(){
		assertNotEquals(null, bar.getExportDialog(), "Checking export dialog initialized");
	}

	@Test
	public void getEditMenu(){
		ZabMenu menu = bar.getEditMenu();
		assertEquals("Edit", menu.getText(), "Checking correct name of menu");
		assertEquals(2, menu.getMenuComponentCount(), "Checking correct number of items");
		assertEquals("Undo", ((AbstractButton)menu.getMenuComponent(0)).getText(), "Checking correct name of item");
		assertEquals("Redo", ((AbstractButton)menu.getMenuComponent(1)).getText(), "Checking correct name of item");
	}
	
	@Test
	public void actionPerformedSaveListener(){
		bar.getSaver().actionPerformed(new ActionEvent(bar, 0, null));
	}

	@Test
	public void actionPerformedLoadListener(){
		bar.getLoader().actionPerformed(new ActionEvent(bar, 0, null));
	}
	
	@Test
	public void actionPerformedExportListener(){
		bar.getExporter().actionPerformed(new ActionEvent(bar, 0, null));
	}
		
	@AfterEach
	public void end(){}

}
