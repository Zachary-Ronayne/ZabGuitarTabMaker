package settings;

import java.io.PrintWriter;
import java.util.Scanner;

import util.Saveable;

/**
 * A setting containing a String, the string cannot include new line characters
 * @author zrona
 */
public class SettingString extends NotNullSetting<String>{

	/**
	 * Create a new {@link SettingString} with the given String
	 * @param value The value and default value
	 */
	public SettingString(String value){
		super(value);
		if(value.contains("\n")) throw new IllegalArgumentException("StringSettings cannot contain new line characters");
	}
	
	/**
	 * Set the String value. If the given value contains a new line, nothing happens.
	 */
	@Override
	public void set(String value){
		super.set(cleanValue(value));
	}

	/**
	 * Set the default String value. If the given value contains a new line, nothing happens.
	 */
	@Override
	public void setDefault(String defaultValue){
		super.setDefault(cleanValue(defaultValue));
	}
	
	/***/
	@Override
	public boolean loadValues(Scanner reader){
		String[] load = Saveable.loadStrings(reader, 2);
		if(load == null) return false;
		this.set(load[0]);
		this.setDefault(load[1]);
		return true;
	}
	
	/***/
	@Override
	public boolean saveValues(PrintWriter writer){
		return Saveable.saveToStrings(writer, new String[]{this.get(), this.getDefault()}, true);
	}
	
	/**
	 * Utility method for checking validity of strings for a SettingString
	 * @param value The value to check
	 * @return Either value if it is valid, and empty string if value is null, or null if value contained an invalid character i.e. a \n
	 */
	public static String cleanValue(String value){
		boolean isNull = value == null;
		if(isNull) return "";
		if(value.contains("\n")) return null;
		return value;
	}

}
