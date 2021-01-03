package appMain.gui.comp.dropMenu;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractButton;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;
import util.FileUtils;

public class TestZabMenuBar{

	private ZabMenuBar bar;
	
	private ZabGui gui;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		gui = new ZabGui();
		bar = new ZabMenuBar(gui);
	}
	
	@Test
	public void constructor(){
		assertEquals(new Color(40, 40, 40), bar.getBackground(), "Checking background set");
	}
	
	@Test
	public void getFileMenu(){
		ZabMenu menu = bar.getFileMenu();
		assertEquals("File", menu.getText(), "Checking correct name of menu");
		assertEquals(3, menu.getMenuComponentCount(), "Checking correct number of items");
		assertEquals("Save", ((AbstractButton)menu.getMenuComponent(0)).getText(), "Checking correct name of item");
		assertEquals("Load", ((AbstractButton)menu.getMenuComponent(1)).getText(), "Checking correct name of item");
		assertEquals("Export", ((AbstractButton)menu.getMenuComponent(2)).getText(), "Checking correct name of item");
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
	public void getGui(){
		assertEquals(gui, bar.getGui(), "Checking gui initialized");
	}
	
	@Test
	public void actionPerformedExportListener(){
		File f = new File(FileUtils.makeFileName("./saves", "export", "txt"));
		bar.getExporter().actionPerformed(new ActionEvent(bar, 0, ""));
		assertTrue("Checking file was made", f.exists());
		f.delete();
	}
	
	@AfterEach
	public void end(){}

}
