package appUtils;

import music.Rhythm;
import settings.SettingBoolean;
import settings.SettingChar;
import settings.SettingDouble;
import settings.SettingInt;
import settings.SettingRhythm;
import settings.SettingString;
import settings.Settings;
import tab.TabTextExporter;

/**
 * An object containing all settings used by the entire Zab application.<br>
 * <ul>
 * 	<li>1. Make a static final for the default value</li>
 * 	<li>2. Define a new instance variable for it, create a getter for both the value and the object, and no setter</li>
 * 	<li>3. In the empty constructor, initialize it with default values using a variation {@link #add(Object, Object)}</li>
 * 	<li>4. Add test cases for new methods</li>
 * </ul>
 * @author zrona
 */
public class ZabSettings extends Settings{
	
	/** Default for {@link #hammerOn} */
	public static final String HAMMER_ON = "h";
	/** Default for {@link #pullOff} */
	public static final String PULL_OFF = "p";
	/** Default for {@link #slideUp} */
	public static final String SLIDE_UP = "/";
	/** Default for {@link #slideDown} */
	public static final String SLIDE_DOWN = "\\";
	/** Default for {@link #harmonicBefore} */
	public static final String HARMONIC_BEFORE = "<";
	/** Default for {@link #harmonicAfter} */
	public static final String HARMONIC_AFTER = ">";
	/** Default for {@link #deadNote} */
	public static final String DEAD_NOTE = "X";

	/** Default for {@link #tabTextPreString} */
	public static final String TAB_TEXT_PRE_STRING = "";
	/** Default for {@link #tabTextPostNoteName} */
	public static final String TAB_TEXT_POST_NOTE_NAME = "|";
	/** Default for {@link #tabTextNoteNameFiller} */
	public static final char TAB_TEXT_NOTE_NAME_FILLER = ' ';
	/** Default for {@link #tabTextNoteNameAlignEnd} */
	public static final boolean TAB_TEXT_NOTE_NAME_ALIGN_END = false;
	/** Default for {@link #tabTextNoteNameOctave} */
	public static final boolean TAB_TEXT_NOTE_NAME_OCTAVE = false;
	/** Default for {@link #tabTextNoteNameFormat} */
	public static final int TAB_TEXT_NOTE_NAME_FORMAT = TabTextExporter.NOTE_FORMAT_ALL_SHARP;
	/** Default for {@link #tabTextBeforeSymbol} */
	public static final String TAB_TEXT_BEFORE_SYMBOL = "-";
	/** Default for {@link #tabTextAfterSymbol} */
	public static final String TAB_TEXT_AFTER_SYMBOL = "-";
	/** Default for {@link #tabTextFiller} */
	public static final char TAB_TEXT_FILLER = '-';
	/** Default for {@link #tabTextAlignSymbolsEnd} */
	public static final boolean TAB_TEXT_ALIGN_SYMBOLS_END = false;
	/** Default for {@link #tabTextEnd} */
	public static final String TAB_TEXT_END = "|";
	
	/** Default for {@link #zoomFactor} */
	public static final double ZOOM_FACTOR = 0.1;
	/** Default for {@link #zoomInverted} */
	public static final boolean ZOOM_INVERTED = true;
	/** Default for {@link #zoomModifierFactor} */
	public static final double ZOOM_MODIFIER_FACTOR = 2.0;
	
	
	/** Default for {@link #quantizeDivisor} */
	public static final Double QUANTIZE_DIVISOR = 8.0;
	/** @return Default for {@link #rhythmConversionEndValue} */
	public static Rhythm RHYTHM_CONVERSION_END_VALUE(){ return new Rhythm(1, 4); }
	
	
	/** The symbol used to represent a hammer on note */
	private SettingString hammerOn;
	/** The symbol used to represent a pull off note */
	private SettingString pullOff;
	/** The symbol used to represent sliding up in pitch to a note */
	private SettingString slideUp;
	/** The symbol used to represent sliding down in pitch to a note */
	private SettingString slideDown;
	/** The symbol used to represent the before part of a natural harmonic note */
	private SettingString harmonicBefore;
	/** The symbol used to represent the after part of a natural harmonic note */
	private SettingString harmonicAfter;
	/** The symbol used to represent a dead note, i.e. putting the fretting hand on a fret, but not holding the string to the fretboard */
	private SettingString deadNote;
	
	/** In text output of a tab, the text that goes before each string line in a line of tab */
	private SettingString tabTextPreString;
	/** In text output of a tab, the text that goes after the note name of the string */
	private SettingString tabTextPostNoteName;
	/** In text output of a tab, the character used as filler for note names of unequal length */
	private SettingChar tabTextNoteNameFiller;
	/** In text output of a tab, true line up string line note names at the end of the name, false for the beginning */
	private SettingBoolean tabTextNoteNameAlignEnd;
	/** In text output of a tab, true to display the octave of the note name label of each string line, false to not display it */
	private SettingBoolean tabTextNoteNameOctave;
	/** In text output of a tab, the format to use for displaying notes with flats and or sharps */
	private SettingInt tabTextNoteNameFormat;
	/** In text output of a tab, the text printed before every symbol */
	private SettingString tabTextBeforeSymbol;
	/** In text output of a tab, the text printed after every symbol */
	private SettingString tabTextAfterSymbol;
	/** In text output of a tab, the character used as filler between notes */
	private SettingChar tabTextFiller;
	/** In text output of a tab, true align all symbols at the ends of their string representation, false to align at the beginning */
	private SettingBoolean tabTextAlignSymbolsEnd;
	/** In text output of a tab, the text printed at the end of every string line */
	private SettingString tabTextEnd;
	
	/** The value which determines how fast the camera zooms */
	private SettingDouble zoomFactor;
	/** true if zooming should be inverted, i.e. moving the mouse wheel towards the user should zoom out, false otherwise */
	private SettingBoolean zoomInverted;
	/** The value for the extra amount the camera zooms when holding shift/alt/ctrl */
	private SettingDouble zoomModifierFactor;
	
	/**
	 * The divisor used for quantizing notes for creation, placement, and selection, based on note duration. 
	 * If this value is 8, it uses eighth notes, if it is 4, it uses quarter notes, if it is 1.23 it uses notes 1/1.23 the length of a whole note
	 */
	private SettingDouble quantizeDivisor;
	/**
	 * The value for the {@link Rhythm} used for the last note when a rhythmicless tab is converted to use rhythm, and the last note cannot be guessed
	 * because it has no note after it
	 */
	private SettingRhythm rhythmConversionEndValue;
	
	
	/**
	 * Create a new version of settings with all of the default values loaded
	 */
	public ZabSettings(){
		super();
		this.hammerOn = this.addString(HAMMER_ON);
		this.pullOff = this.addString(PULL_OFF);
		this.slideUp = this.addString(SLIDE_UP);
		this.slideDown = this.addString(SLIDE_DOWN);
		this.harmonicBefore = this.addString(HARMONIC_BEFORE);
		this.harmonicAfter = this.addString(HARMONIC_AFTER);
		this.deadNote = this.addString(DEAD_NOTE);
		
		this.tabTextPreString = this.addString(TAB_TEXT_PRE_STRING);
		this.tabTextPostNoteName = this.addString(TAB_TEXT_POST_NOTE_NAME);
		this.tabTextNoteNameFiller = this.addChar(TAB_TEXT_NOTE_NAME_FILLER);
		this.tabTextNoteNameAlignEnd = this.addBoolean(TAB_TEXT_NOTE_NAME_ALIGN_END);
		this.tabTextNoteNameOctave = this.addBoolean(TAB_TEXT_NOTE_NAME_OCTAVE);
		this.tabTextNoteNameFormat = this.addInt(TAB_TEXT_NOTE_NAME_FORMAT);
		this.tabTextBeforeSymbol = this.addString(TAB_TEXT_BEFORE_SYMBOL);
		this.tabTextAfterSymbol = this.addString(TAB_TEXT_AFTER_SYMBOL);
		this.tabTextFiller = this.addChar(TAB_TEXT_FILLER);
		this.tabTextAlignSymbolsEnd = this.addBoolean(TAB_TEXT_ALIGN_SYMBOLS_END);
		this.tabTextEnd = this.addString(TAB_TEXT_END);
		
		this.zoomFactor = this.addDouble(ZOOM_FACTOR);
		this.zoomInverted = this.addBoolean(ZOOM_INVERTED);
		this.zoomModifierFactor = this.addDouble(ZOOM_MODIFIER_FACTOR);
		
		this.quantizeDivisor = this.addDouble(QUANTIZE_DIVISOR);
		this.rhythmConversionEndValue = this.add(new SettingRhythm(RHYTHM_CONVERSION_END_VALUE()));
	}
	
	/** @return See {@link #hammerOn} */
	public SettingString getHammerOn(){ return this.hammerOn; }
	/** @return See {@link #pullOff} */
	public SettingString getPullOff(){ return this.pullOff; }
	/** @return See {@link #slideUp} */
	public SettingString getSlideUp(){ return this.slideUp; }
	/** @return See {@link #slideDown} */
	public SettingString getSlideDown(){ return this.slideDown; }
	/** @return See {@link #harmonicBefore} */
	public SettingString getHarmonicBefore(){ return this.harmonicBefore; }
	/** @return See {@link #harmonicAfter} */
	public SettingString getHarmonicAfter(){ return this.harmonicAfter; }
	/** @return See {@link #deadNote} */
	public SettingString getDeadNote(){ return this.deadNote; }
	
	/** @return See {@link #tabTextPreString} */
	public SettingString getTabTextPreString(){ return this.tabTextPreString; }
	/** @return See {@link #tabTextPostNoteName} */
	public SettingString getTabTextPostNoteName(){ return this.tabTextPostNoteName; }
	/** @return See {@link #tabTextNoteNameFiller} */
	public SettingChar getTabTextNoteNameFiller(){ return this.tabTextNoteNameFiller; }
	/** @return See {@link #tabTextNoteNameAlignEnd} */
	public SettingBoolean getTabTextNoteNameAlignEnd(){ return this.tabTextNoteNameAlignEnd; }
	/** @return See {@link #tabTextNoteNameOctave} */
	public SettingBoolean getTabTextNoteNameOctave(){ return this.tabTextNoteNameOctave; }
	/** @return See {@link #tabTextNoteNameFormat} */
	public SettingInt getTabTextNoteNameFormat(){ return this.tabTextNoteNameFormat; }
	/** @return See {@link #tabTextBeforeSymbol} */
	public SettingString getTabTextBeforeSymbol(){ return this.tabTextBeforeSymbol; }
	/** @return See {@link #tabTextAfterSymbol} */
	public SettingString getTabTextAfterSymbol(){ return this.tabTextAfterSymbol; }
	/** @return See {@link #tabTextFiller} */
	public SettingChar getTabTextFiller(){ return this.tabTextFiller; }
	/** @return See {@link #tabTextAlignSymbolsEnd} */
	public SettingBoolean getTabTextAlignSymbolsEnd(){ return this.tabTextAlignSymbolsEnd; }
	/** @return See {@link #tabTextEnd} */
	public SettingString getTabTextEnd(){ return this.tabTextEnd; }
	
	/** @return See {@link #zoomFactor} */
	public SettingDouble getZoomFactor(){ return this.zoomFactor; }
	/** @return See {@link #zoomInverted} */
	public SettingBoolean getZoomInverted(){ return this.zoomInverted; }
	/** @return See {@link #zoomModifierFactor} */
	public SettingDouble getZoomModifierFactor(){ return this.zoomModifierFactor; }
	
	/** @return See {@link #quantizeDivisor} */
	public SettingDouble getQuantizeDivisor(){ return this.quantizeDivisor; }
	/** @return See {@link #rhythmConversionEndValue} */
	public SettingRhythm getRhythmConversionEndValue(){ return this.rhythmConversionEndValue; }
	
	
	/** @return See {@link #hammerOn} */
	public String hammerOn(){ return this.getHammerOn().get(); }
	/** @return See {@link #pullOff} */
	public String pullOff(){ return this.getPullOff().get(); }
	/** @return See {@link #slideUp} */
	public String slideUp(){ return this.getSlideUp().get(); }
	/** @return See {@link #slideDown} */
	public String slideDown(){ return this.getSlideDown().get(); }
	/** @return See {@link #harmonicBefore} */
	public String harmonicBefore(){ return this.getHarmonicBefore().get(); }
	/** @return See {@link #harmonicAfter} */
	public String harmonicAfter(){ return this.getHarmonicAfter().get(); }
	/** @return See {@link #deadNote} */
	public String deadNote(){ return this.getDeadNote().get(); }
	
	/** @return See {@link #tabTextPreString} */
	public String tabTextPreString(){ return this.getTabTextPreString().get(); }
	/** @return See {@link #tabTextPostNoteName} */
	public String tabTextPostNoteName(){ return this.getTabTextPostNoteName().get(); }
	/** @return See {@link #tabTextNoteNameFiller} */
	public Character tabTextNoteNameFiller(){ return this.getTabTextNoteNameFiller().get(); }
	/** @return See {@link #tabTextNoteNameAlignEnd} */
	public Boolean tabTextNoteNameAlignEnd(){ return this.getTabTextNoteNameAlignEnd().get(); }
	/** @return See {@link #tabTextNoteNameOctave} */
	public Boolean tabTextNoteNameOctave(){ return this.getTabTextNoteNameOctave().get(); }
	/** @return See {@link #tabTextNoteNameFormat} */
	public Integer tabTextNoteNameFormat(){ return this.getTabTextNoteNameFormat().get(); }
	/** @return See {@link #tabTextBeforeSymbol} */
	public String tabTextBeforeSymbol(){ return this.getTabTextBeforeSymbol().get(); }
	/** @return See {@link #tabTextAfterSymbol} */
	public String tabTextAfterSymbol(){ return this.getTabTextAfterSymbol().get(); }
	/** @return See {@link #tabTextFiller} */
	public Character tabTextFiller(){ return this.getTabTextFiller().get(); }
	/** @return See {@link #tabTextAlignSymbolsEnd} */
	public Boolean tabTextAlignSymbolsEnd(){ return this.getTabTextAlignSymbolsEnd().get(); }
	/** @return See {@link #tabTextEnd} */
	public String tabTextEnd(){ return this.getTabTextEnd().get(); }
	
	/** @return See {@link #zoomFactor} */
	public Double zoomFactor(){ return this.getZoomFactor().get(); }
	/** @return See {@link #zoomInverted} */
	public Boolean zoomInverted(){ return this.getZoomInverted().get(); }
	/** @return See {@link #zoomModifierFactor} */
	public Double zoomModifierFactor(){ return this.getZoomModifierFactor().get(); }
	
	/** @return See {@link #quantizeDivisor} */
	public Double quantizeDivisor(){ return this.getQuantizeDivisor().get(); }
	/** @return See {@link #rhythmConversionEndValue} */
	public Rhythm rhythmConversionEndValue(){ return this.getRhythmConversionEndValue().get(); }
}
