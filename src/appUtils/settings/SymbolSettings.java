package appUtils.settings;

import settings.SettingString;
import settings.Settings;

/**
 * A {@link Settings} object which contains all of the information related to symbols representing different kinds of tab notes and modifiers.
 * @author zrona
 */
public class SymbolSettings extends Settings{
	
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
	/** Default for {@link #bend} */
	public static final String BEND = "b";
	/** Default for {@link #ghostBefore} */
	public static final String GHOST_BEFORE = "(";
	/** Default for {@link #ghostAfter} */
	public static final String GHOST_AFTER = ")";
	/** Default for {@link #pinchHarmoic} */
	public static final String PINCH_HARMONIC = "ph";
	/** Default for {@link #vibrato} */
	public static final String VIBRATO = "~";
	/** Default for {@link #tap} */
	public static final String TAP = "t";
	
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
	/** The symbol used to represent a bend note, i.e. bending a note up in pitch */
	private SettingString bend;
	/** The symbol used to represent the symbol before a ghost note, i.e. a note that continues to ring out */
	private SettingString ghostBefore;
	/** The symbol used to represent the symbol after a ghost note, i.e. a note that continues to ring out */
	private SettingString ghostAfter;
	/** The symbol used to represent the symbol after a pinch harmonic note */
	private SettingString pinchHarmonic;
	/** The symbol used to represent a note that should have vibrato applied */
	private SettingString vibrato;
	/** The symbol used to represent a note that should be tapped */
	private SettingString tap;
	
	/**
	 * Create a new set of {@link SymbolSettings} with all default values loaded
	 */
	public SymbolSettings(){
		super();
		this.hammerOn = this.addString(HAMMER_ON);
		this.pullOff = this.addString(PULL_OFF);
		this.slideUp = this.addString(SLIDE_UP);
		this.slideDown = this.addString(SLIDE_DOWN);
		this.harmonicBefore = this.addString(HARMONIC_BEFORE);
		this.harmonicAfter = this.addString(HARMONIC_AFTER);
		this.deadNote = this.addString(DEAD_NOTE);
		this.bend = this.addString(BEND);
		this.ghostBefore = this.addString(GHOST_BEFORE);
		this.ghostAfter = this.addString(GHOST_AFTER);
		this.pinchHarmonic = this.addString(PINCH_HARMONIC);
		this.vibrato = this.addString(VIBRATO);
		this.tap = this.addString(TAP);
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
	/** @return See {@link #bend} */
	public SettingString getBend(){ return this.bend; }
	/** @return See {@link #ghostBefore} */
	public SettingString getGhostBefore(){ return this.ghostBefore; }
	/** @return See {@link #ghostAfter} */
	public SettingString getGhostAfter(){ return this.ghostAfter; }
	/** @return See {@link #pinchHarmonic} */
	public SettingString getPinchHarmonic(){ return this.pinchHarmonic; }
	/** @return See {@link #vibrato} */
	public SettingString getVibrato(){ return this.vibrato; }
	/** @return See {@link #tap} */
	public SettingString getTap(){ return this.tap; }
	
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
	/** @return See {@link #bend} */
	public String bend(){ return this.getBend().get(); }
	/** @return See {@link #ghostBefore} */
	public String ghostBefore(){ return this.getGhostBefore().get(); }
	/** @return See {@link #ghostAfter} */
	public String ghostAfter(){ return this.getGhostAfter().get(); }
	/** @return See {@link #pinchHarmoic} */
	public String pinchHarmonic(){ return this.getPinchHarmonic().get(); }
	/** @return See {@link #vibrato} */
	public String vibrato(){ return this.getVibrato().get(); }
	/** @return See {@link #tap} */
	public String tap(){ return this.getTap().get(); }
	
}
