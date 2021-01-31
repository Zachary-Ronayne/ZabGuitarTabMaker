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
	
}
