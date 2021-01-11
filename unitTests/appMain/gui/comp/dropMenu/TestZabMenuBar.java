package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

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
		assertEquals(new Color(50, 50, 50), bar.getBackground(), "Checking background set");
	}
	
	@Test
	public void getGui(){
		assertEquals(gui, bar.getGui(), "Checking gui initialized");
	}

	@Test
	public void getFileMenu(){
		FileMenu menu = bar.getFileMenu();
		assertEquals("File", menu.getText(), "Checking correct name of menu");
		assertEquals(3, menu.getMenuComponentCount(), "Checking correct number of items");
		assertEquals(menu.getSaveItem(), menu.getMenuComponent(0), "Checking correct item order");
		assertEquals(menu.getLoadItem(), menu.getMenuComponent(1), "Checking correct item order");
		assertEquals(menu.getExportItem(), menu.getMenuComponent(2), "Checking correct item order");
	}
	
	@Test
	public void getEditMenu(){
		EditMenu menu = bar.getEditMenu();
		assertEquals("Edit", menu.getText(), "Checking correct name of menu");
		assertEquals(2, menu.getMenuComponentCount(), "Checking correct number of items");
		assertEquals("Undo", ((AbstractButton)menu.getMenuComponent(0)).getText(), "Checking correct name of item");
		assertEquals("Redo", ((AbstractButton)menu.getMenuComponent(1)).getText(), "Checking correct name of item");
	}
		
	@AfterEach
	public void end(){}

}
