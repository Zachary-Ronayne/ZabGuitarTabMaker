package settings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		
		assertTrue("Checking each setting exists", s.get(0).get().equals("set a"));
		assertTrue("Checking each setting exists", s.get(1).get().equals("set b"));
	}
	
	@Test
	public void loadDefaults(){
		ArrayList<Setting<?>> s = settings.getAll();
		((SettingString)s.get(0)).set("set y");
		((SettingString)s.get(1)).set("set z");
		
		assertFalse("Checking settings are not equal", settings.equals(newSettings));
		
		settings.loadDefaults();
		assertTrue("Checking settings are equal after loading defaults", settings.equals(newSettings));
	}
	
	@Test
	public void load(){
		// Changing settings to all different values
		ArrayList<Setting<?>> s = settings.getAll();
		((SettingString)s.get(0)).set("set y");
		((SettingString)s.get(1)).set("set z");
		
		String saved = UtilsTest.testSave(settings);
		Scanner scan = new Scanner(saved);
		assertFalse("Checking new settings is different from the main one", newSettings.equals(settings));
		
		boolean success = newSettings.load(scan);
		assertTrue("Checking loading succeeds", success);
		assertTrue("Checking new settings are the same as the main one after loading", newSettings.equals(settings));
	}
	
	@Test
	public void save(){
		String saved = UtilsTest.testSave(settings);
		assertEquals("set a\nset a\nset b\nset b\n", saved, "Checking save was successful");
	}
	
	@Test
	public void equals(){
		assertFalse("Checking null is not equal to settings", settings.equals(null));
		
		assertTrue("Checking identical settings are equal", settings.equals(newSettings));
		assertFalse("Checking identical settings are not the same object", settings == newSettings);

		ArrayList<Setting<?>> s = newSettings.getAll();
		((SettingString)s.get(0)).set("a");
		assertFalse("Checking settings with different values are not equal", settings.equals(newSettings));
		
		((SettingString)s.get(0)).loadDefault();
		assertTrue("Checking identical settings are equal after using a default", settings.equals(newSettings));
		
		newSettings.getAll().remove(0);
		assertFalse("Checking settings with differing sizes are not equal", settings.equals(newSettings));
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
