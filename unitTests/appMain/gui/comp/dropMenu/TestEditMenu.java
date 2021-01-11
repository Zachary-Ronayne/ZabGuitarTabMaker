package appMain.gui.comp.dropMenu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;

public class TestEditMenu{
	
	private static ZabGui gui;
	
	private EditMenu menu;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
	}
	
	@BeforeEach
	public void setup(){
		menu = gui.getZabMenuBar().getEditMenu();
	}
	
	@Test
	public void test(){}
	
	@AfterEach
	public void end(){}
	
}
