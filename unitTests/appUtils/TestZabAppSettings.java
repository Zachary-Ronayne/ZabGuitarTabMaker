package appUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appMain.gui.ZabTheme.DarkTheme;
import appMain.gui.ZabTheme.LightTheme;
import util.testUtils.Assert;
import util.testUtils.UtilsTest;

public class TestZabAppSettings{

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		UtilsTest.createUnitFolder();
	}
	
	@Test
	public void reset(){
		ZabAppSettings.reset();
		assertEquals(null, ZabAppSettings.get(), "Checking settings set to null");
		assertEquals(null, ZabAppSettings.theme(), "Checking theme set to null");
		ZabAppSettings.init();
	}
	
	@Test
	public void get(){
		assertNotEquals(null, ZabAppSettings.get(), "Checking settings are initialized");
	}
	
	@Test
	public void theme(){
		assertNotEquals(null, ZabAppSettings.theme(), "Checking theme initialized");
	}
	
	@Test
	public void setTheme(){
		ZabGui gui = new ZabGui();
		gui.setVisible(false);
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), gui, false);
		ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), null, true);
		
		gui.dispose();
	}
	
	@Test
	public void getThemeFile(){
		assertEquals(new File(ZabAppSettings.THEME_SAVE_LOCATION), ZabAppSettings.getThemeFile(), "Checking file correct");
	}
	
	@Test
	public void loadDefaultTheme(){
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		ZabAppSettings.loadDefaultTheme();
		Assert.isInstance(DarkTheme.class, ZabAppSettings.theme(), "Checking default theme set");
	}
	
	@Test
	public void loadTheme() throws FileNotFoundException{
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		ZabAppSettings.getThemeFile().delete();
		assertFalse(ZabAppSettings.loadTheme(), "Checking theme failed to load from file");
		Assert.isInstance(DarkTheme.class, ZabAppSettings.theme(), "Checking default theme set");
		
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, true);
		assertTrue(ZabAppSettings.loadTheme(), "Checking theme loaded");
		Assert.isInstance(LightTheme.class, ZabAppSettings.theme(), "Checking light theme set");

		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, true);
		File f = ZabAppSettings.getThemeFile();
		PrintWriter write = new PrintWriter(f);
		write.print("a");
		write.close();
		assertFalse(ZabAppSettings.loadTheme(), "Checking theme failed to load from file with invalid theme name");
		Assert.isInstance(DarkTheme.class, ZabAppSettings.theme(), "Checking default theme set");

		ZabAppSettings.setTheme(new LightTheme(), null, false);
		ZabAppSettings.getThemeFile().delete();
		assertFalse(ZabAppSettings.loadTheme(), "Checking theme failed to load from file with missing file");
		Assert.isInstance(DarkTheme.class, ZabAppSettings.theme(), "Checking default theme set");
	}
	
	@Test
	public void saveTheme(){
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		assertTrue(ZabAppSettings.saveTheme(), "Checking save successful");
		ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), null, false);
		assertTrue(ZabAppSettings.loadTheme(), "Checking load after save successful");
		Assert.isInstance(LightTheme.class, ZabAppSettings.theme(), "Checking light theme loaded");
		
		ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), null, false);
		assertTrue(ZabAppSettings.saveTheme(), "Checking save successful");
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		assertTrue(ZabAppSettings.loadTheme(), "Checking load after save successful");
		Assert.isInstance(DarkTheme.class, ZabAppSettings.theme(), "Checking dark theme loaded");
		
		ZabAppSettings.reset();
		assertFalse(ZabAppSettings.saveTheme(), "Checking reset theme fails to save");
		ZabAppSettings.init();
	}
	
	@AfterEach
	public void end(){
		UtilsTest.deleteUnitFolder();
	}
	
}
