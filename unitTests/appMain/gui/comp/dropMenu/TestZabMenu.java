package appMain.gui.comp.dropMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;

public class TestZabMenu{
	
	private static ZabGui gui;
	
	private ZabMenu menu;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		menu = new ZabMenu("name", gui);
	}
	
	@Test
	public void constructor(){
		assertEquals(new Color(30, 30, 30), menu.getBackground(), "Checking background set");
		assertEquals("name", menu.getText(), "Checking text initialized");
	}
	
	@Test
	public void getGui(){
		assertEquals(gui, menu.getGui(), "Checking gui initialized");
	}
	
	@Test
	public void getComponents(){
		ZabMenuItem[] items = new ZabMenuItem[4];
		ZabMenu subMenu = new ZabMenu("", gui);
		for(int i = 0; i < 4; i++) items[i] = new ZabMenuItem("" + i);
		subMenu.add(items[0]);
		subMenu.add(items[1]);
		menu.add(subMenu);
		menu.add(items[2]);
		menu.add(items[3]);
		
		Component[] comps = menu.getComponents();
		List<Component> list = Arrays.asList(comps);
		assertEquals(5, list.size(), "Checking correct number of components obtained");
		assertTrue(list.contains(items[0]), "Checking list contains all items");
		assertTrue(list.contains(items[1]), "Checking list contains all items");
		assertTrue(list.contains(items[2]), "Checking list contains all items");
		assertTrue(list.contains(items[3]), "Checking list contains all items");
		assertTrue(list.contains(subMenu), "Checking list contains sub menu");
	}

	@AfterAll
	public static void endAll(){
		gui.dispose();
	}

}
