package appUtils.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestTabTextSettings{

	private static TabTextSettings settings;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		settings = new TabTextSettings();
	}
	
	@BeforeEach
	public void setup(){}

	@Test
	public void preString(){
		TestZabSettings.assertSettingInitialized(settings.getPreString(), TabTextSettings.PRE_STRING);
		assertEquals(TabTextSettings.PRE_STRING, settings.preString(), "Checking value getter correct");
	}
	@Test
	public void postNoteName(){
		TestZabSettings.assertSettingInitialized(settings.getPostNoteName(), TabTextSettings.POST_NOTE_NAME);
		assertEquals(TabTextSettings.POST_NOTE_NAME, settings.postNoteName(), "Checking value getter correct");
	}
	@Test
	public void noteNameFiller(){
		TestZabSettings.assertSettingInitialized(settings.getNoteNameFiller(), TabTextSettings.NOTE_NAME_FILLER);
		assertEquals(TabTextSettings.NOTE_NAME_FILLER, settings.noteNameFiller(), "Checking value getter correct");
	}
	@Test
	public void noteNameAlignEnd(){
		TestZabSettings.assertSettingInitialized(settings.getNoteNameAlignEnd(), TabTextSettings.NOTE_NAME_ALIGN_END);
		assertEquals(TabTextSettings.NOTE_NAME_ALIGN_END, settings.noteNameAlignEnd(), "Checking value getter correct");
	}
	@Test
	public void noteNameOctave(){
		TestZabSettings.assertSettingInitialized(settings.getNoteNameOctave(), TabTextSettings.NOTE_NAME_OCTAVE);
		assertEquals(TabTextSettings.NOTE_NAME_OCTAVE, settings.noteNameOctave(), "Checking value getter correct");
	}
	@Test
	public void noteNameFormat(){
		TestZabSettings.assertSettingInitialized(settings.getNoteNameFormat(), TabTextSettings.NOTE_NAME_FORMAT);
		assertEquals(TabTextSettings.NOTE_NAME_FORMAT, settings.noteNameFormat(), "Checking value getter correct");
	}
	@Test
	public void beforeSymbol(){
		TestZabSettings.assertSettingInitialized(settings.getBeforeSymbol(), TabTextSettings.BEFORE_SYMBOL);
		assertEquals(TabTextSettings.BEFORE_SYMBOL, settings.beforeSymbol(), "Checking value getter correct");
	}
	@Test
	public void afterSymbol(){
		TestZabSettings.assertSettingInitialized(settings.getAfterSymbol(), TabTextSettings.AFTER_SYMBOL);
		assertEquals(TabTextSettings.AFTER_SYMBOL, settings.afterSymbol(), "Checking value getter correct");
	}
	@Test
	public void textFiller(){
		TestZabSettings.assertSettingInitialized(settings.getFiller(), TabTextSettings.FILLER);
		assertEquals(TabTextSettings.FILLER, settings.filler(), "Checking value getter correct");
	}
	@Test
	public void alignSymbolsEnd(){
		TestZabSettings.assertSettingInitialized(settings.getAlignSymbolsEnd(), TabTextSettings.ALIGN_SYMBOLS_END);
		assertEquals(TabTextSettings.ALIGN_SYMBOLS_END, settings.alignSymbolsEnd(), "Checking value getter correct");
	}
	@Test
	public void testEnd(){
		TestZabSettings.assertSettingInitialized(settings.getEnd(), TabTextSettings.END);
		assertEquals(TabTextSettings.END, settings.textEnd(), "Checking value getter correct");
	}
	@Test
	public void measuresPerLine(){
		TestZabSettings.assertSettingInitialized(settings.getMeasuresPerLine(), TabTextSettings.MEASURES_PER_LINE);
		assertEquals(TabTextSettings.MEASURES_PER_LINE, settings.measuresPerLine(), "Checking value getter correct");
	}
	
	@AfterEach
	public void end(){}
}
