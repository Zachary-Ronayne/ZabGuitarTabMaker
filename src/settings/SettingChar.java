package settings;

import java.io.PrintWriter;
import java.util.Scanner;

import util.Saveable;

/**
 * A {@link Setting} representing a character
 * @author zrona
 */
public class SettingChar extends NotNullSetting<Character>{
	
	/**
	 * Create a new {@link SettingChar} with the given character
	 * @param value The value and default value
	 */
	public SettingChar(Character value){
		super(value);
		if(!isValid(value)) throw new IllegalArgumentException("Character settings cannot be the new line or tab characters");
	}
	
	/**
	 * Set the value, so long that it is a valid character, defined by {@link #isValid(Character)}
	 */
	@Override
	public void set(Character value){
		if(!isValid(value)) return;
		super.set(value);
	}
	
	@Override
	public void setDefault(Character defaultValue){
		if(!isValid(defaultValue)) return;
		super.setDefault(defaultValue);
	}
	
	/***/
	@Override
	public boolean loadValues(Scanner reader) throws Exception{
		// Load the two lines, and if the load fails, return false
		String[] load = Saveable.loadStrings(reader, 2);
		if(load == null) return false;
		
		// Set the values and return true
		this.set(load[0].charAt(0));
		this.setDefault(load[1].charAt(0));
		return true;
	}

	/***/
	@Override
	public boolean saveValues(PrintWriter writer) throws Exception{
		return Saveable.saveToStrings(writer, new Character[]{this.get(), this.getDefault()}, true);
	}
	
	/**
	 * Determine if the given character is one which can be used for a {@link SettingChar}
	 * @param c The character to test
	 * @return true if it is valid, false otherwise
	 */
	public static boolean isValid(Character c){
		return c != null && c != '\n' && c != '\t';
	}
	
}
