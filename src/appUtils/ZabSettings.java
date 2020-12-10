package appUtils;

import settings.SettingString;
import settings.Settings;

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
	
	/** The default symbol for a hammer on type note */
	public static final String HAMMER_ON = "h";
	/** The default symbol for a pull off type note */
	public static final String PULL_OFF = "p";
	/** The default symbol for sliding up in pitch to a note */
	public static final String SLIDE_UP = "/";
	/** The default symbol for sliding down in pitch to a note */
	public static final String SLIDE_DOWN = "\\";
	/** The default symbol for the before part of a natural harmonic note */
	public static final String HARMONIC_BEFORE = "<";
	/** The default symbol for the after part of a natural harmonic note */
	public static final String HARMONIC_AFTER = ">";
	
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
	}
	
	/** @return See {@link #hammerOn} */
	public SettingString getHammerOn(){
		return hammerOn;
	}
	/** @return See {@link #pullOff} */
	public SettingString getPullOff(){
		return pullOff;
	}
	/** @return See {@link #slideUp} */
	public SettingString getSlideUp(){
		return slideUp;
	}
	/** @return See {@link #slideDown} */
	public SettingString getSlideDown(){
		return slideDown;
	}
	/** @return See {@link #harmonicBefore} */
	public SettingString getHarmonicBefore(){
		return harmonicBefore;
	}
	/** @return See {@link #harmonicAfter} */
	public SettingString getHarmonicAfter(){
		return harmonicAfter;
	}
	
	/** @return See {@link #hammerOn} */
	public String hammerOn(){
		return getHammerOn().getValue();
	}
	/** @return See {@link #pullOff} */
	public String pullOff(){
		return getPullOff().getValue();
	}
	/** @return See {@link #slideUp} */
	public String slideUp(){
		return getSlideUp().getValue();
	}
	/** @return See {@link #slideDown} */
	public String slideDown(){
		return getSlideDown().getValue();
	}
	/** @return See {@link #harmonicBefore} */
	public String harmonicBefore(){
		return getHarmonicBefore().getValue();
	}
	/** @return See {@link #harmonicAfter} */
	public String harmonicAfter(){
		return getHarmonicAfter().getValue();
	}
}
