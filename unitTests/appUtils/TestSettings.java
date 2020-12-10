package appUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import settings.Setting;
import settings.SettingString;
import util.testUtils.UtilsTest;

public class TestSettings{

	private Settings settings;
	
	@BeforeEach
	public void setup(){
		settings = new Settings();
	}
	
	@Test
	public void getHammerOn(){
		SettingString s = settings.getHammerOn();
		assertEquals(Settings.HAMMER_ON, s.getValue(), "Checking value set");
		assertEquals(Settings.HAMMER_ON, s.getDefaultValue(), "Checking default value set");
	}
	
	@Test
	public void getPullOff(){
		SettingString s = settings.getPullOff();
		assertEquals(Settings.PULL_OFF, s.getValue(), "Checking value set");
		assertEquals(Settings.PULL_OFF, s.getDefaultValue(), "Checking default value set");
	}
	
	@Test
	public void getSlideUp(){
		SettingString s = settings.getSlideUp();
		assertEquals(Settings.SLIDE_UP, s.getValue(), "Checking value set");
		assertEquals(Settings.SLIDE_UP, s.getDefaultValue(), "Checking default value set");
	}
	
	@Test
	public void getSlideDown(){
		SettingString s = settings.getSlideDown();
		assertEquals(Settings.SLIDE_DOWN, s.getValue(), "Checking value set");
		assertEquals(Settings.SLIDE_DOWN, s.getDefaultValue(), "Checking default value set");
	}
	
	@Test
	public void getHarmonicBefore(){
		SettingString s = settings.getHarmonicBefore();
		assertEquals(Settings.HARMONIC_BEFORE, s.getValue(), "Checking value set");
		assertEquals(Settings.HARMONIC_BEFORE, s.getDefaultValue(), "Checking default value set");
	}
	
	@Test
	public void getHarmonicAfter(){
		SettingString s = settings.getHarmonicAfter();
		assertEquals(Settings.HARMONIC_AFTER, s.getValue(), "Checking value set");
		assertEquals(Settings.HARMONIC_AFTER, s.getDefaultValue(), "Checking default value set");
	}
	
	@Test
	public void getAll(){
		ArrayList<Setting<?>> s = settings.getAll();
		assertEquals(6, s.size(), "Checking correct number of settings exist");
		
		assertTrue("Checking each setting exists", s.contains(settings.getHammerOn()));
		assertTrue("Checking each setting exists", s.contains(settings.getPullOff()));
		assertTrue("Checking each setting exists", s.contains(settings.getSlideUp()));
		assertTrue("Checking each setting exists", s.contains(settings.getSlideDown()));
		assertTrue("Checking each setting exists", s.contains(settings.getHarmonicBefore()));
		assertTrue("Checking each setting exists", s.contains(settings.getHarmonicAfter()));
	}
	
	@Test
	public void loadDefaults(){ // TODO
		Settings newSettings = new Settings();
		settings.getHammerOn().setValue("0");
		settings.getPullOff().setValue("1");
		settings.getSlideUp().setValue("2");
		settings.getSlideDown().setValue("3");
		settings.getHarmonicBefore().setValue("4");
		settings.getHarmonicAfter().setValue("5");
		assertFalse("Checking settings are not equal", settings.equals(newSettings));
		
		settings.loadDefaults();
		assertTrue("Checking settings are equal after loading defaults", settings.equals(newSettings));
	}
	
	@Test
	public void load(){
		// Changing settings to all different values
		settings.getHammerOn().setValue("0");
		settings.getPullOff().setValue("1");
		settings.getSlideUp().setValue("2");
		settings.getSlideDown().setValue("3");
		settings.getHarmonicBefore().setValue("4");
		settings.getHarmonicAfter().setValue("5");
		
		String saved = UtilsTest.testSave(settings);
		Scanner scan = new Scanner(saved);
		Settings newSettings = new Settings();
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
		
		Settings newSettings = new Settings();
		assertTrue("Checking identical settings are equal", settings.equals(newSettings));
		assertFalse("Checking identical settings are not the same object", settings == newSettings);
		
		newSettings.getHammerOn().setValue("a");
		assertFalse("Checking settings with different values are not equal", settings.equals(newSettings));
		
		newSettings.getHammerOn().loadDefault();
		assertTrue("Checking identical settings are equal after using a default", settings.equals(newSettings));
		
		newSettings.getAll().remove(0);
		assertFalse("Checking settings with differing sizes are not equal", settings.equals(newSettings));
	}
	
	@AfterEach
	public void end(){}

}
