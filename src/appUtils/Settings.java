package appUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import settings.Setting;
import settings.SettingString;
import util.ObjectUtils;
import util.Saveable;

/**
 * An object containing all settings used by the entire application.<br>
 * This includes, but is not limited to, GUI settings, notation settings, and so on.<br>
 * To add a new setting:
 * <ul>
 * 	<li>1. Make a static final for the default value</li>
 * 	<li>2. Define a new instance variable for it, create a getter, and no setter</li>
 * 	<li>3. In the empty constructor, initialize it with default values using a variation {@link #add(Object, Object)}</li>
 * </ul>
 * To add a new setting type, create a method like addString, but using the new type
 * @author zrona
 */
public class Settings implements Saveable{
	
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
	
	/** A list of all of the settings used by this {@link Settings} object */
	private ArrayList<Setting<?>> all;
	
	/**
	 * Create a new version of settings with all of the default values loaded
	 */
	public Settings(){
		this.all = new ArrayList<Setting<?>>();
		
		this.hammerOn = this.addString(HAMMER_ON);
		this.pullOff = this.addString(PULL_OFF);
		this.slideUp = this.addString(SLIDE_UP);
		this.slideDown = this.addString(SLIDE_DOWN);
		this.harmonicBefore = this.addString(HARMONIC_BEFORE);
		this.harmonicAfter = this.addString(HARMONIC_AFTER);
	}
	
	/**
	 * A helper method used by the constructor to generate initial setting objects.
	 * Adds the given object to add and returns it
	 * @param <T> The type of the {@link Setting} object
	 * @param value The setting value to use
	 * @return The generated settings object
	 */
	private SettingString add(SettingString value){
		this.all.add(value);
		return value;
	}
	
	/**
	 * A helper method used by the constructor to generate initial setting objects.
	 * @param value The string to use
	 * @return The generated settings object
	 */
	private SettingString addString(String value){
		SettingString s = new SettingString(value);
		return this.add(s);
	}
	
	/** @return See {@link #getHammerOn} */
	public SettingString getHammerOn(){
		return hammerOn;
	}
	/** @return See {@link #getPullOff} */
	public SettingString getPullOff(){
		return pullOff;
	}
	/** @return See {@link #getSlideUp} */
	public SettingString getSlideUp(){
		return slideUp;
	}
	/** @return See {@link #getSlideDown} */
	public SettingString getSlideDown(){
		return slideDown;
	}
	/** @return See {@link #getHarmonicBefore} */
	public SettingString getHarmonicBefore(){
		return harmonicBefore;
	}
	/** @return See {@link #getHarmonicAfter} */
	public SettingString getHarmonicAfter(){
		return harmonicAfter;
	}

	/**
	 * Get a list of every setting in no particular order
	 * @return The list
	 */
	public ArrayList<Setting<?>> getAll(){
		return all;
	}
	
	/**
	 * Load the default value of all settings
	 */
	public void loadDefaults(){
		for(Setting<?> s : this.getAll()){
			s.loadDefault();
		}
	}
	
	/***/
	@Override
	public boolean load(Scanner reader){
		boolean success = true;
		for(Setting<?> s : this.getAll()){
			success &= s.load(reader);
			if(!success) break;
		}
		return success;
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		boolean success = true;
		for(Setting<?> s : this.getAll()){
			success &= s.save(writer);
			if(!success) break;
		}
		return success;
	}
	
	/***/
	@Override
	public boolean equals(Object obj){
		// Check that the objects are of the same type
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		
		// If the objects are the same object, return true
		if(super.equals(obj)) return true;
		
		Settings s = (Settings)obj;
		ArrayList<Setting<?>> thisSettings = this.getAll();
		ArrayList<Setting<?>> objSettings = s.getAll();
		
		// Check that both objects have the same sized lists of Settings
		if(thisSettings.size() != objSettings.size()) return false;
		
		// Check that every setting is equal, if any are not equal, return false
		for(int i = 0; i < thisSettings.size(); i++){
			if(!thisSettings.get(i).equals(objSettings.get(i))) return false;
		}
		
		// Otherwise, the objects are equal
		return true;
	}
	
}
