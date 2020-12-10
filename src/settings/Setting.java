package settings;

import java.io.PrintWriter;
import java.util.Scanner;

import appUtils.ZabSettings;
import util.ObjectUtils;
import util.Saveable;

/**
 * A class representing a setting used by {@link ZabSettings}
 * @param T the type of object used by this setting
 * @author zrona
 */
public abstract class Setting<T> implements Saveable{
	
	/** The value held by this Setting */
	private T value;
	
	/**
	 * The default value of this Setting, generally should not be changed after creation.<br>
	 * should change {@link #value} to modify this setting
	 */
	private T defaultValue;

	/**
	 * Generate a {@link Setting} using the given value and default 
	 * @param value The value for this setting
	 * @param defaultValue The default value for this setting
	 */
	public Setting(T value, T defaultValue){
		this.setValue(value);
		this.setDefaultValue(defaultValue);
	}
	
	/**
	 * Generate a {@link Setting} using the given value for the default and current value
	 * @param value The value for this setting
	 */
	public Setting(T value){
		this(value, value);
	}
	
	/**
	 * Get the value of this {@link Setting}
	 * @return See {@link #value}
	 */
	public T getValue(){
		return value;
	}
	
	/**
	 * Set the value of this {@link Setting}
	 * @param value See {@link #value}
	 */
	public void setValue(T value){
		this.value = value;
	}
	
	/**
	 * Get the default value of this {@link Setting}
	 * @return See {@link #defaultValue}
	 */
	public T getDefaultValue(){
		return defaultValue;
	}
	
	/**
	 * Set the default value of this {@link Setting}<br>
	 * Generally should not be used outside of constructor
	 * @param defaultValue See {@link #defaultValue}
	 */
	public void setDefaultValue(T defaultValue){
		this.defaultValue = defaultValue;
	}

	/**
	 * Set the value of this {@link Setting} to its default value
	 */
	public void loadDefault(){
		this.setValue(this.getDefaultValue());
	}
	
	/**
	 * Load in the setting, handling error checking
	 */
	@Override
	public boolean load(Scanner reader){
		if(reader == null) return false;
		try{
			this.loadValue(reader);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Save in the setting, handling error checking
	 */
	@Override
	public boolean save(PrintWriter writer){
		if(writer == null) return false;
		try{
			this.saveValue(writer);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Load the value of this Setting, error checking is already handled
	 * @param reader The {@link Scanner} object to use loading, do not close this object.
	 * @throws Exception Any exception which can happen in loading
	 */
	public abstract void loadValue(Scanner reader) throws Exception;
	
	/**
	 * Save the value of this Setting, error checking is already handled
	 * @param writer The {@link PrintWriter} object to use saving, do not close this object.
	 * @throws Exception Any exception which can happen in saving
	 */
	public abstract void saveValue(PrintWriter writer) throws Exception;
	
	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		
		Setting<?> s = (Setting<?>)obj;
		return 	super.equals(obj) ||
				this.getValue().equals(s.getValue()) &&
				this.getDefaultValue().equals(s.getDefaultValue());
	}
	
}
