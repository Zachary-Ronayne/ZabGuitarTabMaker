package appUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import settings.Setting;
import util.testUtils.Assert;

public class TestZabSettings{

	private static ZabSettings settings;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		settings = new ZabSettings();
	}
	
	@BeforeEach
	public void setup(){}
	
	/**
	 * Utility method for asserting that a setting is set up correctly
	 * @param s The setting to check
	 * @param value The expected value of the setting for the current value and default
	 */
	public static void assertSettingInitialized(Setting<?> s, Object value){
		assertEquals(value, s.get(), "Checking value set");
		assertEquals(value, s.getDefault(), "Checking default value set");
		Assert.contains(settings.getAll(), s);
	}
	
	@Test
	public void hammerOn(){
		assertSettingInitialized(settings.getHammerOn(), ZabSettings.HAMMER_ON);
		assertEquals(ZabSettings.HAMMER_ON, settings.hammerOn(), "Checking value getter correct");
	}
	
	@Test
	public void pullOff(){
		assertSettingInitialized(settings.getPullOff(), ZabSettings.PULL_OFF);
		assertEquals(ZabSettings.PULL_OFF, settings.pullOff(), "Checking value getter correct");
	}
	
	@Test
	public void slideUp(){
		assertSettingInitialized(settings.getSlideUp(), ZabSettings.SLIDE_UP);
		assertEquals(ZabSettings.SLIDE_UP, settings.slideUp(), "Checking value getter correct");
	}
	
	@Test
	public void slideDown(){
		assertSettingInitialized(settings.getSlideDown(), ZabSettings.SLIDE_DOWN);
		assertEquals(ZabSettings.SLIDE_DOWN, settings.slideDown(), "Checking value getter correct");
	}
	
	@Test
	public void harmonicBefore(){
		assertSettingInitialized(settings.getHarmonicBefore(), ZabSettings.HARMONIC_BEFORE);
		assertEquals(ZabSettings.HARMONIC_BEFORE, settings.harmonicBefore(), "Checking value getter correct");
	}
	
	@Test
	public void harmonicAfter(){
		assertSettingInitialized(settings.getHarmonicAfter(), ZabSettings.HARMONIC_AFTER);
		assertEquals(ZabSettings.HARMONIC_AFTER, settings.harmonicAfter(), "Checking value getter correct");
	}
	
	@Test
	public void deadNote(){
		assertSettingInitialized(settings.getDeadNote(), ZabSettings.DEAD_NOTE);
		assertEquals(ZabSettings.DEAD_NOTE, settings.deadNote(), "Checking value getter correct");
	}
	
	@Test
	public void tabTextPreString(){
		assertSettingInitialized(settings.getTabTextPreString(), ZabSettings.TAB_TEXT_PRE_STRING);
		assertEquals(ZabSettings.TAB_TEXT_PRE_STRING, settings.tabTextPreString(), "Checking value getter correct");
	}
	
	@Test
	public void tabTextPostNoteName(){
		assertSettingInitialized(settings.getTabTextPostNoteName(), ZabSettings.TAB_TEXT_POST_NOTE_NAME);
		assertEquals(ZabSettings.TAB_TEXT_POST_NOTE_NAME, settings.tabTextPostNoteName(), "Checking value getter correct");
	}
	
	@Test
	public void tabTextNoteNameFiller(){
		assertSettingInitialized(settings.getTabTextNoteNameFiller(), ZabSettings.TAB_TEXT_NOTE_NAME_FILLER);
		assertEquals(ZabSettings.TAB_TEXT_NOTE_NAME_FILLER, settings.tabTextNoteNameFiller(), "Checking value getter correct");
	}
	
	@Test
	public void tabTextNoteNameAlignEnd(){
		assertSettingInitialized(settings.getTabTextNoteNameAlignEnd(), ZabSettings.TAB_TEXT_NOTE_NAME_ALIGN_END);
		assertEquals(ZabSettings.TAB_TEXT_NOTE_NAME_ALIGN_END, settings.tabTextNoteNameAlignEnd(), "Checking value getter correct");
	}
	
	@Test
	public void tabTextNoteNameOctave(){
		assertSettingInitialized(settings.getTabTextNoteNameOctave(), ZabSettings.TAB_TEXT_NOTE_NAME_OCTAVE);
		assertEquals(ZabSettings.TAB_TEXT_NOTE_NAME_OCTAVE, settings.tabTextNoteNameOctave(), "Checking value getter correct");
	}
	
	@Test
	public void tabTextNoteNameFormat(){
		assertSettingInitialized(settings.getTabTextNoteNameFormat(), ZabSettings.TAB_TEXT_NOTE_NAME_FORMAT);
		assertEquals(ZabSettings.TAB_TEXT_NOTE_NAME_FORMAT, settings.tabTextNoteNameFormat(), "Checking value getter correct");
	}
	
	@Test
	public void tabTextBeforeSymbol(){
		assertSettingInitialized(settings.getTabTextBeforeSymbol(), ZabSettings.TAB_TEXT_BEFORE_SYMBOL);
		assertEquals(ZabSettings.TAB_TEXT_BEFORE_SYMBOL, settings.tabTextBeforeSymbol(), "Checking value getter correct");
	}
	
	@Test
	public void tabTextAfterSymbol(){
		assertSettingInitialized(settings.getTabTextAfterSymbol(), ZabSettings.TAB_TEXT_AFTER_SYMBOL);
		assertEquals(ZabSettings.TAB_TEXT_AFTER_SYMBOL, settings.tabTextAfterSymbol(), "Checking value getter correct");
	}
	
	@Test
	public void tabTextFiller(){
		assertSettingInitialized(settings.getTabTextFiller(), ZabSettings.TAB_TEXT_FILLER);
		assertEquals(ZabSettings.TAB_TEXT_FILLER, settings.tabTextFiller(), "Checking value getter correct");
	}
	
	@Test
	public void tabTextAlignSymbolsEnd(){
		assertSettingInitialized(settings.getTabTextAlignSymbolsEnd(), ZabSettings.TAB_TEXT_ALIGN_SYMBOLS_END);
		assertEquals(ZabSettings.TAB_TEXT_ALIGN_SYMBOLS_END, settings.tabTextAlignSymbolsEnd(), "Checking value getter correct");
	}
	
	@Test
	public void tabTextEnd(){
		assertSettingInitialized(settings.getTabTextEnd(), ZabSettings.TAB_TEXT_END);
		assertEquals(ZabSettings.TAB_TEXT_END, settings.tabTextEnd(), "Checking value getter correct");
	}
	
	@Test
	public void zoomFactor(){
		assertSettingInitialized(settings.getZoomFactor(), ZabSettings.ZOOM_FACTOR);
		assertEquals(ZabSettings.ZOOM_FACTOR, settings.zoomFactor(), "Checking value getter correct");
	}
	
	@Test
	public void zoomInverted(){
		assertSettingInitialized(settings.getZoomInverted(), ZabSettings.ZOOM_INVERTED);
		assertEquals(ZabSettings.ZOOM_INVERTED, settings.zoomInverted(), "Checking value getter correct");
	}
	
	@Test
	public void zoomModifierFactor(){
		assertSettingInitialized(settings.getZoomModifierFactor(), ZabSettings.ZOOM_MODIFIER_FACTOR);
		assertEquals(ZabSettings.ZOOM_MODIFIER_FACTOR, settings.zoomModifierFactor(), "Checking value getter correct");
	}
	
	@Test
	public void quantizeDivisor(){
		assertSettingInitialized(settings.getQuantizeDivisor(), ZabSettings.QUANTIZE_DIVISOR);
		assertEquals(ZabSettings.QUANTIZE_DIVISOR, settings.quantizeDivisor(), "Checking value getter correct");
	}
	
	@Test
	public void rhythmConversionEndValue(){
		assertSettingInitialized(settings.getRhythmConversionEndValue(), ZabSettings.RHYTHM_CONVERSION_END_VALUE());
		assertEquals(ZabSettings.RHYTHM_CONVERSION_END_VALUE(), settings.rhythmConversionEndValue(), "Checking value getter correct");
	}
	
	@AfterEach
	public void end(){}

}
