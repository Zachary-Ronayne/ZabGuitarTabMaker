package appUtils.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestSymbolSettings{

	private static SymbolSettings settings;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		settings = new SymbolSettings();
	}
	
	@BeforeEach
	public void setup(){}

	@Test
	public void hammerOn(){
		TestZabSettings.assertSettingInitialized(settings.getHammerOn(), SymbolSettings.HAMMER_ON);
		assertEquals(SymbolSettings.HAMMER_ON, settings.hammerOn(), "Checking value getter correct");
	}
	@Test
	public void pullOff(){
		TestZabSettings.assertSettingInitialized(settings.getPullOff(), SymbolSettings.PULL_OFF);
		assertEquals(SymbolSettings.PULL_OFF, settings.pullOff(), "Checking value getter correct");
	}
	@Test
	public void slideUp(){
		TestZabSettings.assertSettingInitialized(settings.getSlideUp(), SymbolSettings.SLIDE_UP);
		assertEquals(SymbolSettings.SLIDE_UP, settings.slideUp(), "Checking value getter correct");
	}
	@Test
	public void slideDown(){
		TestZabSettings.assertSettingInitialized(settings.getSlideDown(), SymbolSettings.SLIDE_DOWN);
		assertEquals(SymbolSettings.SLIDE_DOWN, settings.slideDown(), "Checking value getter correct");
	}
	@Test
	public void harmonicBefore(){
		TestZabSettings.assertSettingInitialized(settings.getHarmonicBefore(), SymbolSettings.HARMONIC_BEFORE);
		assertEquals(SymbolSettings.HARMONIC_BEFORE, settings.harmonicBefore(), "Checking value getter correct");
	}
	@Test
	public void harmonicAfter(){
		TestZabSettings.assertSettingInitialized(settings.getHarmonicAfter(), SymbolSettings.HARMONIC_AFTER);
		assertEquals(SymbolSettings.HARMONIC_AFTER, settings.harmonicAfter(), "Checking value getter correct");
	}
	@Test
	public void deadNote(){
		TestZabSettings.assertSettingInitialized(settings.getDeadNote(), SymbolSettings.DEAD_NOTE);
		assertEquals(SymbolSettings.DEAD_NOTE, settings.deadNote(), "Checking value getter correct");
	}
	
	@AfterEach
	public void end(){}
}
