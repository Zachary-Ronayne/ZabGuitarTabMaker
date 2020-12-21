package settings;

import java.io.PrintWriter;
import java.util.Scanner;

import util.Saveable;

/**
 * A setting containing a String, the string cannot include new line characters
 * @author zrona
 */
public class SettingString extends Setting<String>{

	public SettingString(String value){
		super(value);
		if(value.contains("\n")) throw new IllegalArgumentException("StringSettings cannot contain new line characters");
	}
	
	/**
	 * Set the String value. If the given value contains a new line, nothing happens.
	 */
	@Override
	public void setValue(String value){
		if(value.contains("\n")) return;
		super.setValue(value);
	}

	/**
	 * Set the default String value. If the given value contains a new line, nothing happens.
	 */
	@Override
	public void setDefaultValue(String defaultValue){
		if(defaultValue.contains("\n")) return;
		super.setDefaultValue(defaultValue);
	}
	
	/***/
	@Override
	public boolean loadValue(Scanner reader){
		String[] load = Saveable.loadStrings(reader, 2);
		if(load == null) return false;
		this.setValue(load[0]);
		this.setDefaultValue(load[1]);
		return true;
	}
	
	/***/
	@Override
	public boolean saveValue(PrintWriter writer){
		return Saveable.saveToStrings(writer, new String[]{this.getValue(), this.getDefaultValue()}, true);
	}

}
