package settings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.testUtils.UtilsTest;

public class TestSettings{

	private TestT settings;
	private TestT newSettings;
	
	private class TestT extends Settings{}
	
	@BeforeEach
	public void setup(){
		settings = new TestT();
		settings.addString("set a");
		settings.addString("set b");
		
		newSettings = new TestT();
		newSettings.addString("set a");
		newSettings.addString("set b");
	}
	
	@Test
	public void getAll(){
		ArrayList<Setting<?>> s = settings.getAll();
		assertEquals(2, s.size(), "Checking correct number of settings exist");
		
		assertTrue("Checking each setting exists", s.get(0).getValue().equals("set a"));
		assertTrue("Checking each setting exists", s.get(1).getValue().equals("set b"));
	}
	
	@Test
	public void loadDefaults(){
		ArrayList<Setting<?>> s = settings.getAll();
		((SettingString)s.get(0)).setValue("set y");
		((SettingString)s.get(1)).setValue("set z");
		
		assertFalse("Checking settings are not equal", settings.equals(newSettings));
		
		settings.loadDefaults();
		assertTrue("Checking settings are equal after loading defaults", settings.equals(newSettings));
	}
	
	@Test
	public void load(){
		// Changing settings to all different values
		ArrayList<Setting<?>> s = settings.getAll();
		((SettingString)s.get(0)).setValue("set y");
		((SettingString)s.get(1)).setValue("set z");
		
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
		assertNotEquals(null, saved, "Checking save was successful");
	}
	
	@Test
	public void equals(){
		assertFalse("Checking null is not equal to settings", settings.equals(null));
		
		assertTrue("Checking identical settings are equal", settings.equals(newSettings));
		assertFalse("Checking identical settings are not the same object", settings == newSettings);

		ArrayList<Setting<?>> s = newSettings.getAll();
		((SettingString)s.get(0)).setValue("a");
		assertFalse("Checking settings with different values are not equal", settings.equals(newSettings));
		
		((SettingString)s.get(0)).loadDefault();
		assertTrue("Checking identical settings are equal after using a default", settings.equals(newSettings));
		
		newSettings.getAll().remove(0);
		assertFalse("Checking settings with differing sizes are not equal", settings.equals(newSettings));
	}
	
	@AfterEach
	public void end(){}

}
