package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import util.testUtils.UtilsTest;

public class TestSettings{

	private TestSettingsObject settings;
	private TestSettingsObject newSettings;
	
	private class TestSettingsObject extends Settings{}

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		settings = new TestSettingsObject();
		settings.addString("set a");
		settings.addString("set b");
		
		newSettings = new TestSettingsObject();
		newSettings.addString("set a");
		newSettings.addString("set b");
	}
	
	@Test
	public void getAll(){
		ArrayList<Setting<?>> s = settings.getAll();
		assertEquals(2, s.size(), "Checking correct number of settings exist");
		
		assertTrue(s.get(0).get().equals("set a"), "Checking each setting exists");
		assertTrue(s.get(1).get().equals("set b"), "Checking each setting exists");
	}
	
	@Test
	public void loadDefaults(){
		ArrayList<Setting<?>> s = settings.getAll();
		((SettingString)s.get(0)).set("set y");
		((SettingString)s.get(1)).set("set z");
		
		assertFalse(settings.equals(newSettings), "Checking settings are not equal");
		
		settings.loadDefaults();
		assertTrue(settings.equals(newSettings), "Checking settings are equal after loading defaults");
	}
	
	@Test
	public void load(){
		// Changing settings to all different values
		ArrayList<Setting<?>> s = settings.getAll();
		((SettingString)s.get(0)).set("set y");
		((SettingString)s.get(1)).set("set z");
		
		String saved = UtilsTest.testSave(settings);
		Scanner scan = new Scanner(saved);
		assertFalse(newSettings.equals(settings), "Checking new settings is different from the main one");
		
		boolean success = newSettings.load(scan);
		assertTrue(success, "Checking loading succeeds");
		assertTrue(newSettings.equals(settings), "Checking new settings are the same as the main one after loading");
		
		scan.close();
	}
	
	@Test
	public void save(){
		String saved = UtilsTest.testSave(settings);
		assertEquals("set a\nset a\nset b\nset b\n", saved, "Checking save was successful");
	}
	
	@Test
	public void equals(){
		assertFalse(settings.equals(null), "Checking null is not equal to settings");
		
		assertTrue(settings.equals(newSettings), "Checking identical settings are equal");
		assertFalse(settings == newSettings, "Checking identical settings are not the same object");

		ArrayList<Setting<?>> s = newSettings.getAll();
		((SettingString)s.get(0)).set("a");
		assertFalse(settings.equals(newSettings), "Checking settings with different values are not equal");
		
		((SettingString)s.get(0)).loadDefault();
		assertTrue(settings.equals(newSettings), "Checking identical settings are equal after using a default");
		
		newSettings.getAll().remove(0);
		assertFalse(settings.equals(newSettings), "Checking settings with differing sizes are not equal");
	}

	@Test
	public void testToString(){
		assertEquals(""
				+ "[Settings, "
					+ "values: ["
						+ "[Setting, Type: String, value: set a, default: set a], "
						+ "[Setting, Type: String, value: set b, default: set b]"
					+ "]"
				+ "]", 
				settings.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}

}
