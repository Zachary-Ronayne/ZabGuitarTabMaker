package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import javax.swing.AbstractButton;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestZabMenuBar{

	private ZabMenuBar bar;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		bar = new ZabMenuBar(null);
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
	
	@AfterEach
	public void end(){}

}
