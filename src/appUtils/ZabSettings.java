package appUtils;

import appMain.gui.util.Camera;
import music.Rhythm;
import settings.SettingBoolean;
import settings.SettingChar;
import settings.SettingDouble;
import settings.SettingInt;
import settings.SettingRhythm;
import settings.SettingString;
import settings.Settings;
import tab.Tab;
import tab.TabPosition;
import tab.TabString;
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
	/** Default for {@link #tabTextEnd} */
	public static final int TAB_TEXT_MEASURES_PER_LINE = 8;

	/** Default for {@link #tabPaintBaseX} */
	public static final double TAB_PAINT_BASE_X = 100;
	/** Default for {@link #tabPaintBaseY} */
	public static final double TAB_PAINT_BASE_Y = -100;
	/** Default for {@link #tabPaintMeasureWidth} */
	public static final double TAB_PAINT_MEASURE_WIDTH = 200;
	/** Default for {@link #tabPaintLineMeasures} */
	public static final int TAB_PAINT_LINE_MEASURES = 8;
	/** Default for {@link #tabPaintStringSpace} */
	public static final double TAB_PAINT_STRING_SPACE = 30;
	/** Default for {@link #tabPaintSelectionBuffer} */
	public static final double TAB_PAINT_SELECTION_BUFFER = 15;
	/** Default for {@link #tabPaintAboveSpace} */
	public static final double TAB_PAINT_ABOVE_SPACE = 40;
	/** Default for {@link #tabPaintBelowSpace} */
	public static final double TAB_PAINT_BELOW_SPACE = 40;
	/** Default for {@link #tabPaintSymbolScaleMode} */
	public static final int TAB_PAINT_SYMBOL_SCALE_MODE = Camera.STRING_SCALE_Y_AXIS;
	/** Default for {@link #tabPaintSymbolXAlign} */
	public static final int TAB_PAINT_SYMBOL_X_ALIGN = Camera.STRING_ALIGN_CENTER;
	/** Default for {@link #tabPaintSymbolYAlign} */
	public static final int TAB_PAINT_SYMBOL_Y_ALIGN = Camera.STRING_ALIGN_CENTER;
	/** Default for {@link #tabPaintStringLabelScaleMode} */
	public static final int TAB_PAINT_STRING_LABEL_SCALE_MODE = Camera.STRING_SCALE_Y_AXIS;
	/** Default for {@link #tabPaintStringLabelXAlign} */
	public static final int TAB_PAINT_STRING_LABEL_X_ALIGN = Camera.STRING_ALIGN_MAX;
	/** Default for {@link #tabPaintStringLabelYAlign} */
	public static final int TAB_PAINT_STRING_LABEL_Y_ALIGN = Camera.STRING_ALIGN_CENTER;
	/** Default for {@link #tabPaintSymbolBorderSize} */
	public static final double TAB_PAINT_SYMBOL_BORDER_SIZE = 2;
	/** Default for {@link #tabPaintStringLabelSpace} */
	public static final double TAB_PAINT_STRING_LABEL_SPACE = 14;
	
	/** Default for {@link #tabControlMoveDeleteInvalid} */
	public static final boolean TAB_CONTROL_MOVE_DELETE_INVALID = false;
	/** Default for {@link #tabControlMoveCancelInvalid} */
	public static final boolean TAB_CONTROL_MOVE_CANCEL_INVALID = false;
	/** Default for {@link #tabControlZoomFactor} */
	public static final double TAB_CONTROL_ZOOM_FACTOR = 0.1;
	/** Default for {@link #tabControlZoomInverted} */
	public static final boolean TAB_CONTROL_ZOOM_INVERTED = true;
	/** Default for {@link #tabControlScrollFactor} */
	public static final double TAB_CONTROL_SCROLL_FACTOR = 40;
	/** Default for {@link #tabControlScrollXInverted} */
	public static final boolean TAB_CONTROL_SCROLL_X_INVERTED = false;
	/** Default for {@link #tabControlScrollYInverted} */
	public static final boolean TAB_CONTROL_SCROLL_Y_INVERTED = false;
	
	/** Default for {@link #quantizeDivisor} */
	public static final double QUANTIZE_DIVISOR = 8.0;
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
	/** In text output of a tab, the number of measures before going to a new line */
	private SettingInt tabTextMeasuresPerLine;
	
	/** The base x coordinate at which the painted {@link Tab} of a {@link TabPainter} is rendered */
	private SettingDouble tabPaintBaseX;
	/** The base y coordinate at which the painted {@link Tab} of a {@link TabPainter} is rendered */
	private SettingDouble tabPaintBaseY;
	/** The base size of a rendered measure of a {@link TabPainter}, in pixels */
	private SettingDouble tabPaintMeasureWidth;
	/** The number of measures a {@link TabPainter} draws per line */
	private SettingInt tabPaintLineMeasures;
	/** The amount of space between painted strings in a {@link TabPainter}, in pixels */
	private SettingDouble tabPaintStringSpace;
	/** The amount of space above and below a tab where a coordinate can still be detected in a {@link TabPainter}, in pixels */
	private SettingDouble tabPaintSelectionBuffer;
	/** The amount of space above each line of tab in a {@link TabPainter}, in pixels */
	private SettingDouble tabPaintAboveSpace;
	/** The amount of space below each line of tab in a {@link TabPainter}, in pixels */
	private SettingDouble tabPaintBelowSpace;
	/** The {@link Camera} scale mode to use when scaling the text of {@link TabPosition} symbols */
	private SettingInt tabPaintSymbolScaleMode;
	/** The {@link Camera} alignment mode to use for the x axis of the text of {@link TabPosition} symbols */
	private SettingInt tabPaintSymbolXAlign;
	/** The {@link Camera} alignment mode to use for the y axis of the text of {@link TabPosition} symbols */
	private SettingInt tabPaintSymbolYAlign;
	/** The {@link Camera} scale mode to use when scaling the text of the pitch labels of drawn {@link TabString} objects */
	private SettingInt tabPaintStringLabelScaleMode;
	/** The {@link Camera} alignment mode to use for the x axis of the text of text of the pitch labels of drawn {@link TabString} objects */
	private SettingInt tabPaintStringLabelXAlign;
	/** The {@link Camera} alignment mode to use for the y axis of the text of text of the pitch labels of drawn {@link TabString} objects */
	private SettingInt tabPaintStringLabelYAlign;
	/** The extra amount of space added to the bounds of a rendered {@link TabPosition} when it has a highlight, in pixels. This pixel amount is the same regardless of zoom level */
	private SettingDouble tabPaintSymbolBorderSize;
	/** The extra amount of space added between the pitch labels of rendered {@link TabString} objects, in pixels. This pixel amount is the same regardless of zoom level */
	private SettingDouble tabPaintStringLabelSpace;
	
	/**
	 * true if, when moving a {@link TabPosition} selection, 
	 * they should be deleted if they are in an invalid position, 
	 * false if they should remain unmoved 
	 */
	private SettingBoolean tabControlMoveDeleteInvalid;
	/**
	 * true if, when moving a {@link TabPosition} selection, 
	 * the entire move should be canceled if any {@link TabPosition} objects couldn't be moved. 
	 * False to determine behavior for individual notes based on {@link #tabControlMoveDeleteInvalid}
	 */
	private SettingBoolean tabControlMoveCancelInvalid;
	
	/** The value which determines how fast the camera zooms */
	private SettingDouble tabControlZoomFactor;
	/** true if zooming should be inverted, i.e. moving the mouse wheel towards the user should zoom out, false otherwise */
	private SettingBoolean tabControlZoomInverted;
	/** The value that determines how quickly scrolling happens. Larger values mean scrolling faster, lower values mean scrolling slower */
	private SettingDouble tabControlScrollFactor;
	/** true to reverse the direction of scrolling on the x axis, false otherwise */
	private SettingBoolean tabControlScrollXInverted;
	/** true to reverse the direction of scrolling on the y axis, false otherwise */
	private SettingBoolean tabControlScrollYInverted;
	
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
		this.tabTextMeasuresPerLine = this.addInt(TAB_TEXT_MEASURES_PER_LINE);
		
		this.tabPaintBaseX = this.addDouble(TAB_PAINT_BASE_X);
		this.tabPaintBaseY = this.addDouble(TAB_PAINT_BASE_Y);
		this.tabPaintMeasureWidth = this.addDouble(TAB_PAINT_MEASURE_WIDTH);
		this.tabPaintLineMeasures = this.addInt(TAB_PAINT_LINE_MEASURES);
		this.tabPaintStringSpace = this.addDouble(TAB_PAINT_STRING_SPACE);
		this.tabPaintSelectionBuffer = this.addDouble(TAB_PAINT_SELECTION_BUFFER);
		this.tabPaintAboveSpace = this.addDouble(TAB_PAINT_ABOVE_SPACE);
		this.tabPaintBelowSpace = this.addDouble(TAB_PAINT_BELOW_SPACE);
		this.tabPaintSymbolScaleMode = this.addInt(TAB_PAINT_SYMBOL_SCALE_MODE);
		this.tabPaintSymbolXAlign = this.addInt(TAB_PAINT_SYMBOL_X_ALIGN);
		this.tabPaintSymbolYAlign = this.addInt(TAB_PAINT_SYMBOL_Y_ALIGN);
		this.tabPaintStringLabelScaleMode = this.addInt(TAB_PAINT_STRING_LABEL_SCALE_MODE);
		this.tabPaintStringLabelXAlign = this.addInt(TAB_PAINT_STRING_LABEL_X_ALIGN);
		this.tabPaintStringLabelYAlign = this.addInt(TAB_PAINT_STRING_LABEL_Y_ALIGN);
		this.tabPaintSymbolBorderSize = this.addDouble(TAB_PAINT_SYMBOL_BORDER_SIZE);
		this.tabPaintStringLabelSpace = this.addDouble(TAB_PAINT_STRING_LABEL_SPACE);
		
		this.tabControlMoveDeleteInvalid = this.addBoolean(TAB_CONTROL_MOVE_DELETE_INVALID);
		this.tabControlMoveCancelInvalid = this.addBoolean(TAB_CONTROL_MOVE_CANCEL_INVALID);
		this.tabControlZoomFactor = this.addDouble(TAB_CONTROL_ZOOM_FACTOR);
		this.tabControlZoomInverted = this.addBoolean(TAB_CONTROL_ZOOM_INVERTED);
		this.tabControlScrollFactor = this.addDouble(TAB_CONTROL_SCROLL_FACTOR);
		this.tabControlScrollXInverted = this.addBoolean(TAB_CONTROL_SCROLL_X_INVERTED);
		this.tabControlScrollYInverted = this.addBoolean(TAB_CONTROL_SCROLL_Y_INVERTED);
		
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
	/** @return See {@link #tabTextMeasuresPerLine} */
	public SettingInt getTabTextMeasuresPerLine(){ return this.tabTextMeasuresPerLine; }

	/** @return See {@link #tabPaintBaseX} */
	public SettingDouble getTabPaintBaseX(){ return this.tabPaintBaseX; }
	/** @return See {@link #tabPaintBaseY} */
	public SettingDouble getTabPaintBaseY(){ return this.tabPaintBaseY; }
	/** @return See {@link #tabPaintMeasureWidth} */
	public SettingDouble getTabPaintMeasureWidth(){ return this.tabPaintMeasureWidth; }
	/** @return See {@link #tabPaintLineMeasures} */
	public SettingInt getTabPaintLineMeasures(){ return this.tabPaintLineMeasures; }
	/** @return See {@link #tabPaintStringSpace} */
	public SettingDouble getTabPaintStringSpace(){ return this.tabPaintStringSpace; }
	/** @return See {@link #tabPaintSelectionBuffer} */
	public SettingDouble getTabPaintSelectionBuffer(){ return this.tabPaintSelectionBuffer; }
	/** @return See {@link #tabPaintAboveSpace} */
	public SettingDouble getTabPaintAboveSpace(){ return this.tabPaintAboveSpace; }
	/** @return See {@link #tabPaintBelowSpace} */
	public SettingDouble getTabPaintBelowSpace(){ return this.tabPaintBelowSpace; }
	/** @return See {@link #tabPaintSymbolScaleMode} */
	public SettingInt getTabPaintSymbolScaleMode(){ return this.tabPaintSymbolScaleMode; }
	/** @return See {@link #tabPaintSymbolXAlign} */
	public SettingInt getTabPaintSymbolXAlign(){ return this.tabPaintSymbolXAlign; }
	/** @return See {@link #tabPaintSymbolYAlign} */
	public SettingInt getTabPaintSymbolYAlign(){ return this.tabPaintSymbolYAlign; }
	/** @return See {@link #tabPaintStringLabelScaleMode} */
	public SettingInt getTabPaintStringLabelScaleMode(){ return this.tabPaintStringLabelScaleMode; }
	/** @return See {@link #tabPaintStringLabelXAlign} */
	public SettingInt getTabPaintStringLabelXAlign(){ return this.tabPaintStringLabelXAlign; }
	/** @return See {@link #tabPaintStringLabelYAlign} */
	public SettingInt getTabPaintStringLabelYAlign(){ return this.tabPaintStringLabelYAlign; }
	/** @return See {@link #tabPaintSymbolBorderSize} */
	public SettingDouble getTabPaintSymbolBorderSize(){ return this.tabPaintSymbolBorderSize; }
	/** @return See {@link #tabPaintStringLabelSpace} */
	public SettingDouble getTabPaintStringLabelSpace(){ return this.tabPaintStringLabelSpace; }
	
	/** @return See {@link #tabControlMoveDeleteInvalid} */
	public SettingBoolean getTabControlMoveDeleteInvalid(){ return this.tabControlMoveDeleteInvalid; }
	/** @return See {@link #tabControlMoveCancelInvalid} */
	public SettingBoolean getTabControlMoveCancelInvalid(){ return this.tabControlMoveCancelInvalid; }
	/** @return See {@link #tabControlZoomFactor} */
	public SettingDouble getTabControlZoomFactor(){ return this.tabControlZoomFactor; }
	/** @return See {@link #tabControlZoomInverted} */
	public SettingBoolean getTabControlZoomInverted(){ return this.tabControlZoomInverted; }
	/** @return See {@link #tabControlScrollFactor} */
	public SettingDouble getTabControlScrollFactor(){ return this.tabControlScrollFactor; }
	/** @return See {@link #tabControlScrollXInverted} */
	public SettingBoolean getTabControlScrollXInverted(){ return this.tabControlScrollXInverted; }
	/** @return See {@link #tabControlScrollYInverted} */
	public SettingBoolean getTabControlScrollYInverted(){ return this.tabControlScrollYInverted; }
	
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
	/** @return See {@link #tabTextMeasuresPerLine} */
	public Integer tabTextMeasuresPerLine(){ return this.getTabTextMeasuresPerLine().get(); }
	
	/** @return See {@link #tabPaintBaseX} */
	public Double tabPaintBaseX(){ return this.getTabPaintBaseX().get(); }
	/** @return See {@link #tabPaintBaseY} */
	public Double tabPaintBaseY(){ return this.getTabPaintBaseY().get(); }
	/** @return See {@link #tabPaintMeasureWidth} */
	public Double tabPaintMeasureWidth(){ return this.getTabPaintMeasureWidth().get(); }
	/** @return See {@link #tabPaintLineMeasures} */
	public Integer tabPaintLineMeasures(){ return this.getTabPaintLineMeasures().get(); }
	/** @return See {@link #tabPaintStringSpace} */
	public Double tabPaintStringSpace(){ return this.getTabPaintStringSpace().get(); }
	/** @return See {@link #tabPaintSelectionBuffer} */
	public Double tabPaintSelectionBuffer(){ return this.getTabPaintSelectionBuffer().get(); }
	/** @return See {@link #tabPaintAboveSpace} */
	public Double tabPaintAboveSpace(){ return this.getTabPaintAboveSpace().get(); }
	/** @return See {@link #tabPaintBelowSpace} */
	public Double tabPaintBelowSpace(){ return this.getTabPaintBelowSpace().get(); }
	/** @return See {@link #tabPaintSymbolScaleMode} */
	public Integer tabPaintSymbolScaleMode(){ return this.getTabPaintSymbolScaleMode().get(); }
	/** @return See {@link #tabPaintSymbolXAlign} */
	public Integer tabPaintSymbolXAlign(){ return this.getTabPaintSymbolXAlign().get(); }
	/** @return See {@link #tabPaintSymbolYAlign} */
	public Integer tabPaintSymbolYAlign(){ return this.getTabPaintSymbolYAlign().get(); }
	/** @return See {@link #tabPaintStringLabelScaleMode} */
	public Integer tabPaintStringLabelScaleMode(){ return this.getTabPaintStringLabelScaleMode().get(); }
	/** @return See {@link #tabPaintStringLabelXAlign} */
	public Integer tabPaintStringLabelXAlign(){ return this.getTabPaintStringLabelXAlign().get(); }
	/** @return See {@link #tabPaintStringLabelYAlign} */
	public Integer tabPaintStringLabelYAlign(){ return this.getTabPaintStringLabelYAlign().get(); }
	/** @return See {@link #tabPaintSymbolBorderSize} */
	public Double tabPaintSymbolBorderSize(){ return this.getTabPaintSymbolBorderSize().get(); }
	/** @return See {@link #tabPaintStringLabelSpace} */
	public Double tabPaintStringLabelSpace(){ return this.getTabPaintStringLabelSpace().get(); }
	
	/** @return See {@link #tabControlMoveDeleteInvalid} */
	public Boolean tabControlMoveDeleteInvalid(){ return this.getTabControlMoveDeleteInvalid().get(); }
	/** @return See {@link #tabControlMoveCancelInvalid} */
	public Boolean tabControlMoveCancelInvalid(){ return this.getTabControlMoveCancelInvalid().get(); }
	/** @return See {@link #tabControlZoomFactor} */
	public Double tabControlZoomFactor(){ return this.getTabControlZoomFactor().get(); }
	/** @return See {@link #tabControlZoomInverted} */
	public Boolean tabControlZoomInverted(){ return this.getTabControlZoomInverted().get(); }
	/** @return See {@link #tabControlScrollFactor} */
	public Double tabControlScrollFactor(){ return this.getTabControlScrollFactor().get(); }
	/** @return See {@link #tabControlScrollXInverted} */
	public Boolean tabControlScrollXInverted(){ return this.getTabControlScrollXInverted().get(); }
	/** @return See {@link #tabControlScrollYInverted} */
	public Boolean tabControlScrollYInverted(){ return this.getTabControlScrollYInverted().get(); }
	
	/** @return See {@link #quantizeDivisor} */
	public Double quantizeDivisor(){ return this.getQuantizeDivisor().get(); }
	/** @return See {@link #rhythmConversionEndValue} */
	public Rhythm rhythmConversionEndValue(){ return this.getRhythmConversionEndValue().get(); }
}
