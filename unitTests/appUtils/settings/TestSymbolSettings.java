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
	@Test
	public void bend(){
		TestZabSettings.assertSettingInitialized(settings.getBend(), SymbolSettings.BEND);
		assertEquals(SymbolSettings.BEND, settings.bend(), "Checking value getter correct");
	}
	@Test
	public void ghostBefore(){
		TestZabSettings.assertSettingInitialized(settings.getGhostBefore(), SymbolSettings.GHOST_BEFORE);
		assertEquals(SymbolSettings.GHOST_BEFORE, settings.ghostBefore(), "Checking value getter correct");
	}
	@Test
	public void ghostAfter(){
		TestZabSettings.assertSettingInitialized(settings.getGhostAfter(), SymbolSettings.GHOST_AFTER);
		assertEquals(SymbolSettings.GHOST_AFTER, settings.ghostAfter(), "Checking value getter correct");
	}
	@Test
	public void pinchHarmoic(){
		TestZabSettings.assertSettingInitialized(settings.getPinchHarmonic(), SymbolSettings.PINCH_HARMONIC);
		assertEquals(SymbolSettings.PINCH_HARMONIC, settings.pinchHarmonic(), "Checking value getter correct");
	}
	@Test
	public void vibrato(){
		TestZabSettings.assertSettingInitialized(settings.getVibrato(), SymbolSettings.VIBRATO);
		assertEquals(SymbolSettings.VIBRATO, settings.vibrato(), "Checking value getter correct");
	}
	@Test
	public void tap(){
		TestZabSettings.assertSettingInitialized(settings.getTap(), SymbolSettings.TAP);
		assertEquals(SymbolSettings.TAP, settings.tap(), "Checking value getter correct");
	}
	
	@AfterEach
	public void end(){}
}
