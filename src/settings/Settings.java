package settings;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import util.ObjectUtils;
import util.Saveable;

/**
 * An object used for containing settings used by an application.<br>
 * This includes, but is not limited to, GUI settings, notation settings, and so on.<br>
 * To add a new setting type, create a method like addString, but using the new type
 * @author zrona
 */
public abstract class Settings implements Saveable{
	
	/** A list of all of the settings used by this {@link Settings} object */
	private ArrayList<Setting<?>> all;
	
	/**
	 * Create a new version of settings with all of the default values loaded
	 */
	public Settings(){
		this.all = new ArrayList<Setting<?>>();
	}
	
	/**
	 * A helper method used by the constructor to generate initial setting objects.
	 * Adds the given object to add and returns it
	 * @param <T> The type of the {@link Setting} object
	 * @param value The setting value to use
	 * @return The generated settings object
	 */
	protected <T extends Setting<?>> T add(T value){
		this.all.add(value);
		return value;
	}
	
	/**
	 * A helper method used by the constructor to generate initial setting objects.
	 * @param value The string to use
	 * @return The generated settings object
	 */
	protected SettingBoolean addBoolean(Boolean value){
		SettingBoolean b = new SettingBoolean(value);
		return this.add(b);
	}
	
	/**
	 * A helper method used by the constructor to generate initial setting objects.
	 * @param value The string to use
	 * @return The generated settings object
	 */
	protected SettingInt addInt(Integer value){
		SettingInt i = new SettingInt(value);
		return this.add(i);
	}
	
	/**
	 * A helper method used by the constructor to generate initial setting objects.
	 * @param value The string to use
	 * @return The generated settings object
	 */
	protected SettingDouble addDouble(Double value){
		SettingDouble d = new SettingDouble(value);
		return this.add(d);
	}
	
	/**
	 * A helper method used by the constructor to generate initial setting objects.
	 * @param value The string to use
	 * @return The generated settings object
	 */
	protected SettingChar addChar(Character value){
		SettingChar d = new SettingChar(value);
		return this.add(d);
	}
	
	/**
	 * A helper method used by the constructor to generate initial setting objects.
	 * @param value The string to use
	 * @return The generated settings object
	 */
	protected SettingString addString(String value){
		SettingString s = new SettingString(value);
		return this.add(s);
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
		for(Setting<?> s : this.getAll()){
			if(!s.load(reader)) return false;
		}
		return true;
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		for(Setting<?> s : this.getAll()){
			if(!s.save(writer)) return false;
		}
		return true;
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
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[Settings, values: [");
		Setting<?> last = this.getAll().get(this.getAll().size() - 1);
		for(Setting<?> s : this.getAll()){
			b.append(s);
			if(last != s) b.append(", ");
		}
		b.append("]]");
		return b.toString();
	}
	
}
