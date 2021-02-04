package appUtils.settings;

import settings.SettingBoolean;
import settings.SettingChar;
import settings.SettingInt;
import settings.SettingString;
import settings.Settings;
import tab.TabTextExporter;

/**
 * A {@link Settings} object which contains all of the information related to exporting a tab to text
 * @author zrona
 */
public class TabTextSettings extends Settings{

	/** Default for {@link #preString} */
	public static final String PRE_STRING = "";
	/** Default for {@link #postNoteName} */
	public static final String POST_NOTE_NAME = "|";
	/** Default for {@link #noteNameFiller} */
	public static final char NOTE_NAME_FILLER = ' ';
	/** Default for {@link #noteNameAlignEnd} */
	public static final boolean NOTE_NAME_ALIGN_END = false;
	/** Default for {@link #noteNameOctave} */
	public static final boolean NOTE_NAME_OCTAVE = false;
	/** Default for {@link #noteNameFormat} */
	public static final int NOTE_NAME_FORMAT = TabTextExporter.NOTE_FORMAT_ALL_SHARP;
	/** Default for {@link #beforeSymbol} */
	public static final String BEFORE_SYMBOL = "-";
	/** Default for {@link #afterSymbol} */
	public static final String AFTER_SYMBOL = "";
	/** Default for {@link #textFiller} */
	public static final char FILLER = '-';
	/** Default for {@link #alignSymbolsEnd} */
	public static final boolean ALIGN_SYMBOLS_END = false;
	/** Default for {@link #end} */
	public static final String END = "|";
	/** Default for {@link #MEASURES_PER_LINE} */
	public static final int MEASURES_PER_LINE = 8;
	/** Default for {@link #useSpacing} */
	public static final boolean USE_SPACING = true;
	/** Default for {@link #measureSeparator} */
	public static final String MEASURE_SEPARATOR = "|";

	/** In text output of a tab, the text that goes before each string line in a line of tab */
	private SettingString preString;
	/** In text output of a tab, the text that goes after the note name of the string */
	private SettingString postNoteName;
	/** In text output of a tab, the character used as filler for note names of unequal length */
	private SettingChar noteNameFiller;
	/** In text output of a tab, true line up string line note names at the end of the name, false for the beginning */
	private SettingBoolean noteNameAlignEnd;
	/** In text output of a tab, true to display the octave of the note name label of each string line, false to not display it */
	private SettingBoolean noteNameOctave;
	/** In text output of a tab, the format to use for displaying notes with flats and or sharps */
	private SettingInt noteNameFormat;
	/** In text output of a tab, the text printed before every symbol */
	private SettingString beforeSymbol;
	/** In text output of a tab, the text printed after every symbol */
	private SettingString afterSymbol;
	/** In text output of a tab, the character used as filler between notes */
	private SettingChar textFiller;
	/** In text output of a tab, true align all symbols at the ends of their string representation, false to align at the beginning */
	private SettingBoolean alignSymbolsEnd;
	/** In text output of a tab, the text printed at the end of every string line */
	private SettingString end;
	/** In text output of a tab, the number of measures before going to a new line */
	private SettingInt measuresPerLine;
	/** In text output of a tab, true if symbols should be spaced based on their placement in measures, false to have no space */
	private SettingBoolean useSpacing;
	/** In text output of a tab, the string to use for the separator of a measure. Can use an empty string to turn off. Only applies when tabs use spacing */
	private SettingString measureSeparator;

	/**
	 * Create a new set of {@link TabTextSettings} with all default values loaded
	 */
	public TabTextSettings(){
		super();
		this.preString = this.addString(PRE_STRING);
		this.postNoteName = this.addString(POST_NOTE_NAME);
		this.noteNameFiller = this.addChar(NOTE_NAME_FILLER);
		this.noteNameAlignEnd = this.addBoolean(NOTE_NAME_ALIGN_END);
		this.noteNameOctave = this.addBoolean(NOTE_NAME_OCTAVE);
		this.noteNameFormat = this.addInt(NOTE_NAME_FORMAT);
		this.beforeSymbol = this.addString(BEFORE_SYMBOL);
		this.afterSymbol = this.addString(AFTER_SYMBOL);
		this.textFiller = this.addChar(FILLER);
		this.alignSymbolsEnd = this.addBoolean(ALIGN_SYMBOLS_END);
		this.end = this.addString(END);
		this.measuresPerLine = this.addInt(MEASURES_PER_LINE);
		this.useSpacing = this.addBoolean(USE_SPACING);
		this.measureSeparator = this.addString(MEASURE_SEPARATOR);
	}
	
	/** @return See {@link #preString} */
	public SettingString getPreString(){ return this.preString; }
	/** @return See {@link #postNoteName} */
	public SettingString getPostNoteName(){ return this.postNoteName; }
	/** @return See {@link #noteNameFiller} */
	public SettingChar getNoteNameFiller(){ return this.noteNameFiller; }
	/** @return See {@link #noteNameAlignEnd} */
	public SettingBoolean getNoteNameAlignEnd(){ return this.noteNameAlignEnd; }
	/** @return See {@link #noteNameOctave} */
	public SettingBoolean getNoteNameOctave(){ return this.noteNameOctave; }
	/** @return See {@link #noteNameFormat} */
	public SettingInt getNoteNameFormat(){ return this.noteNameFormat; }
	/** @return See {@link #beforeSymbol} */
	public SettingString getBeforeSymbol(){ return this.beforeSymbol; }
	/** @return See {@link #afterSymbol} */
	public SettingString getAfterSymbol(){ return this.afterSymbol; }
	/** @return See {@link #textFiller} */
	public SettingChar getFiller(){ return this.textFiller; }
	/** @return See {@link #alignSymbolsEnd} */
	public SettingBoolean getAlignSymbolsEnd(){ return this.alignSymbolsEnd; }
	/** @return See {@link #end} */
	public SettingString getEnd(){ return this.end; }
	/** @return See {@link #measuresPerLine} */
	public SettingInt getMeasuresPerLine(){ return this.measuresPerLine; }
	/** @return See {@link #useSpacing} */
	public SettingBoolean getUseSpacing(){ return this.useSpacing; }
	/** @return See {@link #measuresPerLine} */
	public SettingString getMeasureSeparator(){ return this.measureSeparator; }
	
	/** @return See {@link #preString} */
	public String preString(){ return this.getPreString().get(); }
	/** @return See {@link #postNoteName} */
	public String postNoteName(){ return this.getPostNoteName().get(); }
	/** @return See {@link #noteNameFiller} */
	public Character noteNameFiller(){ return this.getNoteNameFiller().get(); }
	/** @return See {@link #noteNameAlignEnd} */
	public Boolean noteNameAlignEnd(){ return this.getNoteNameAlignEnd().get(); }
	/** @return See {@link #noteNameOctave} */
	public Boolean noteNameOctave(){ return this.getNoteNameOctave().get(); }
	/** @return See {@link #noteNameFormat} */
	public Integer noteNameFormat(){ return this.getNoteNameFormat().get(); }
	/** @return See {@link #beforeSymbol} */
	public String beforeSymbol(){ return this.getBeforeSymbol().get(); }
	/** @return See {@link #afterSymbol} */
	public String afterSymbol(){ return this.getAfterSymbol().get(); }
	/** @return See {@link #textFiller} */
	public Character filler(){ return this.getFiller().get(); }
	/** @return See {@link #alignSymbolsEnd} */
	public Boolean alignSymbolsEnd(){ return this.getAlignSymbolsEnd().get(); }
	/** @return See {@link #end} */
	public String textEnd(){ return this.getEnd().get(); }
	/** @return See {@link #measuresPerLine} */
	public Integer measuresPerLine(){ return this.getMeasuresPerLine().get(); }
	/** @return See {@link #useSpacing} */
	public Boolean useSpacing(){ return this.getUseSpacing().get(); }
	/** @return See {@link #measuresPerLine} */
	public String measureSeparator(){ return this.getMeasureSeparator().get(); } 
}
