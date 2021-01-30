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
	public void tabTextMeasuresPerLine(){
		assertSettingInitialized(settings.getTabTextMeasuresPerLine(), ZabSettings.TAB_TEXT_MEASURES_PER_LINE);
		assertEquals(ZabSettings.TAB_TEXT_MEASURES_PER_LINE, settings.tabTextMeasuresPerLine(), "Checking value getter correct");
	}
	
	@Test
	public void tabPaintBaseX(){
		assertSettingInitialized(settings.getTabPaintBaseX(), ZabSettings.TAB_PAINT_BASE_X);
		assertEquals(ZabSettings.TAB_PAINT_BASE_X, settings.tabPaintBaseX(), "Checking value getter correct");
	}
	@Test
	public void tabPaintBaseY(){
		assertSettingInitialized(settings.getTabPaintBaseY(), ZabSettings.TAB_PAINT_BASE_Y);
		assertEquals(ZabSettings.TAB_PAINT_BASE_Y, settings.tabPaintBaseY(), "Checking value getter correct");
	}
	@Test
	public void tabPaintMeasureWidth(){
		assertSettingInitialized(settings.getTabPaintMeasureWidth(), ZabSettings.TAB_PAINT_MEASURE_WIDTH);
		assertEquals(ZabSettings.TAB_PAINT_MEASURE_WIDTH, settings.tabPaintMeasureWidth(), "Checking value getter correct");
	}
	@Test
	public void tabPaintLineMeasures(){
		assertSettingInitialized(settings.getTabPaintLineMeasures(), ZabSettings.TAB_PAINT_LINE_MEASURES);
		assertEquals(ZabSettings.TAB_PAINT_LINE_MEASURES, settings.tabPaintLineMeasures(), "Checking value getter correct");
	}
	@Test
	public void tabPaintStringSpace(){
		assertSettingInitialized(settings.getTabPaintStringSpace(), ZabSettings.TAB_PAINT_STRING_SPACE);
		assertEquals(ZabSettings.TAB_PAINT_STRING_SPACE, settings.tabPaintStringSpace(), "Checking value getter correct");
	}
	@Test
	public void tabPaintSelectionBuffer(){
		assertSettingInitialized(settings.getTabPaintSelectionBuffer(), ZabSettings.TAB_PAINT_SELECTION_BUFFER);
		assertEquals(ZabSettings.TAB_PAINT_SELECTION_BUFFER, settings.tabPaintSelectionBuffer(), "Checking value getter correct");
	}
	@Test
	public void tabPaintAboveSpace(){
		assertSettingInitialized(settings.getTabPaintAboveSpace(), ZabSettings.TAB_PAINT_ABOVE_SPACE);
		assertEquals(ZabSettings.TAB_PAINT_ABOVE_SPACE, settings.tabPaintAboveSpace(), "Checking value getter correct");
	}
	@Test
	public void tabPaintBelowSpace(){
		assertSettingInitialized(settings.getTabPaintBelowSpace(), ZabSettings.TAB_PAINT_BELOW_SPACE);
		assertEquals(ZabSettings.TAB_PAINT_BELOW_SPACE, settings.tabPaintBelowSpace(), "Checking value getter correct");
	}
	@Test
	public void tabPaintSymbolScaleMode(){
		assertSettingInitialized(settings.getTabPaintSymbolScaleMode(), ZabSettings.TAB_PAINT_SYMBOL_SCALE_MODE);
		assertEquals(ZabSettings.TAB_PAINT_SYMBOL_SCALE_MODE, settings.tabPaintSymbolScaleMode(), "Checking value getter correct");
	}
	@Test
	public void tabPaintSymbolXAlign(){
		assertSettingInitialized(settings.getTabPaintSymbolXAlign(), ZabSettings.TAB_PAINT_SYMBOL_X_ALIGN);
		assertEquals(ZabSettings.TAB_PAINT_SYMBOL_X_ALIGN, settings.tabPaintSymbolXAlign(), "Checking value getter correct");
	}
	@Test
	public void tabPaintSymbolYAlign(){
		assertSettingInitialized(settings.getTabPaintSymbolYAlign(), ZabSettings.TAB_PAINT_SYMBOL_Y_ALIGN);
		assertEquals(ZabSettings.TAB_PAINT_SYMBOL_Y_ALIGN, settings.tabPaintSymbolYAlign(), "Checking value getter correct");
	}
	@Test
	public void tabPaintStringLabelScaleMode(){
		assertSettingInitialized(settings.getTabPaintStringLabelScaleMode(), ZabSettings.TAB_PAINT_STRING_LABEL_SCALE_MODE);
		assertEquals(ZabSettings.TAB_PAINT_STRING_LABEL_SCALE_MODE, settings.tabPaintStringLabelScaleMode(), "Checking value getter correct");
	}
	@Test
	public void tabPaintStringLabelXAlign(){
		assertSettingInitialized(settings.getTabPaintStringLabelXAlign(), ZabSettings.TAB_PAINT_STRING_LABEL_X_ALIGN);
		assertEquals(ZabSettings.TAB_PAINT_STRING_LABEL_X_ALIGN, settings.tabPaintStringLabelXAlign(), "Checking value getter correct");
	}
	@Test
	public void tabPaintStringLabelYAlign(){
		assertSettingInitialized(settings.getTabPaintStringLabelYAlign(), ZabSettings.TAB_PAINT_STRING_LABEL_Y_ALIGN);
		assertEquals(ZabSettings.TAB_PAINT_STRING_LABEL_Y_ALIGN, settings.tabPaintStringLabelYAlign(), "Checking value getter correct");
	}
	@Test
	public void tabPaintSymbolBorderSize(){
		assertSettingInitialized(settings.getTabPaintSymbolBorderSize(), ZabSettings.TAB_PAINT_SYMBOL_BORDER_SIZE);
		assertEquals(ZabSettings.TAB_PAINT_SYMBOL_BORDER_SIZE, settings.tabPaintSymbolBorderSize(), "Checking value getter correct");
	}
	@Test
	public void tabPaintStringLabelSpace(){
		assertSettingInitialized(settings.getTabPaintStringLabelSpace(), ZabSettings.TAB_PAINT_STRING_LABEL_SPACE);
		assertEquals(ZabSettings.TAB_PAINT_STRING_LABEL_SPACE, settings.tabPaintStringLabelSpace(), "Checking value getter correct");
	}
	
	@Test
	public void tabControlMoveDeleteInvalid(){
		assertSettingInitialized(settings.getTabControlMoveDeleteInvalid(), ZabSettings.TAB_CONTROL_MOVE_DELETE_INVALID);
		assertEquals(ZabSettings.TAB_CONTROL_MOVE_DELETE_INVALID, settings.tabControlMoveDeleteInvalid(), "Checking value getter correct");
	}
	@Test
	public void tabControlMoveCancelInvalid(){
		assertSettingInitialized(settings.getTabControlMoveCancelInvalid(), ZabSettings.TAB_CONTROL_MOVE_CANCEL_INVALID);
		assertEquals(ZabSettings.TAB_CONTROL_MOVE_CANCEL_INVALID, settings.tabControlMoveCancelInvalid(), "Checking value getter correct");
	}
	@Test
	public void tabControlZoomFactor(){
		assertSettingInitialized(settings.getTabControlZoomFactor(), ZabSettings.TAB_CONTROL_ZOOM_FACTOR);
		assertEquals(ZabSettings.TAB_CONTROL_ZOOM_FACTOR, settings.tabControlZoomFactor(), "Checking value getter correct");
	}
	@Test
	public void tabControlZoomInverted(){
		assertSettingInitialized(settings.getTabControlZoomInverted(), ZabSettings.TAB_CONTROL_ZOOM_INVERTED);
		assertEquals(ZabSettings.TAB_CONTROL_ZOOM_INVERTED, settings.tabControlZoomInverted(), "Checking value getter correct");
	}
	@Test
	public void tabControlScrollFactor(){
		assertSettingInitialized(settings.getTabControlScrollFactor(), ZabSettings.TAB_CONTROL_SCROLL_FACTOR);
		assertEquals(ZabSettings.TAB_CONTROL_SCROLL_FACTOR, settings.tabControlScrollFactor(), "Checking value getter correct");
	}
	@Test
	public void tabControlScrollXInverted(){
		assertSettingInitialized(settings.getTabControlScrollXInverted(), ZabSettings.TAB_CONTROL_SCROLL_X_INVERTED);
		assertEquals(ZabSettings.TAB_CONTROL_SCROLL_X_INVERTED, settings.tabControlScrollXInverted(), "Checking value getter correct");
	}
	@Test
	public void tabControlScrollYInverted(){
		assertSettingInitialized(settings.getTabControlScrollYInverted(), ZabSettings.TAB_CONTROL_SCROLL_Y_INVERTED);
		assertEquals(ZabSettings.TAB_CONTROL_SCROLL_Y_INVERTED, settings.tabControlScrollYInverted(), "Checking value getter correct");
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
