package appUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import settings.SettingString;

public class TestZabSettings{

	private ZabSettings settings;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		settings = new ZabSettings();
	}
	
	@Test
	public void getHammerOn(){
		SettingString s = settings.getHammerOn();
		assertEquals(ZabSettings.HAMMER_ON, s.get(), "Checking value set");
		assertEquals(ZabSettings.HAMMER_ON, s.getDefault(), "Checking default value set");
		assertEquals(ZabSettings.HAMMER_ON, settings.hammerOn(), "Checking value getter correct");
	}
	
	@Test
	public void getPullOff(){
		SettingString s = settings.getPullOff();
		assertEquals(ZabSettings.PULL_OFF, s.get(), "Checking value set");
		assertEquals(ZabSettings.PULL_OFF, s.getDefault(), "Checking default value set");
		assertEquals(ZabSettings.PULL_OFF, settings.pullOff(), "Checking value getter correct");
	}
	
	@Test
	public void getSlideUp(){
		SettingString s = settings.getSlideUp();
		assertEquals(ZabSettings.SLIDE_UP, s.get(), "Checking value set");
		assertEquals(ZabSettings.SLIDE_UP, s.getDefault(), "Checking default value set");
		assertEquals(ZabSettings.SLIDE_UP, settings.slideUp(), "Checking value getter correct");
	}
	
	@Test
	public void getSlideDown(){
		SettingString s = settings.getSlideDown();
		assertEquals(ZabSettings.SLIDE_DOWN, s.get(), "Checking value set");
		assertEquals(ZabSettings.SLIDE_DOWN, s.getDefault(), "Checking default value set");
		assertEquals(ZabSettings.SLIDE_DOWN, settings.slideDown(), "Checking value getter correct");
	}
	
	@Test
	public void getHarmonicBefore(){
		SettingString s = settings.getHarmonicBefore();
		assertEquals(ZabSettings.HARMONIC_BEFORE, s.get(), "Checking value set");
		assertEquals(ZabSettings.HARMONIC_BEFORE, s.getDefault(), "Checking default value set");
		assertEquals(ZabSettings.HARMONIC_BEFORE, settings.harmonicBefore(), "Checking value getter correct");
	}
	
	@Test
	public void getHarmonicAfter(){
		SettingString s = settings.getHarmonicAfter();
		assertEquals(ZabSettings.HARMONIC_AFTER, s.get(), "Checking value set");
		assertEquals(ZabSettings.HARMONIC_AFTER, s.getDefault(), "Checking default value set");
		assertEquals(ZabSettings.HARMONIC_AFTER, settings.harmonicAfter(), "Checking value getter correct");
	}
	
	@AfterEach
	public void end(){}

}
