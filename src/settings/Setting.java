package settings;

import java.io.PrintWriter;
import java.util.Scanner;

import appUtils.ZabConstants;
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
		this.set(value);
		this.setDefault(defaultValue);
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
	public T get(){
		return value;
	}
	
	/**
	 * Set the value of this {@link Setting}
	 * @param value See {@link #value}
	 */
	public void set(T value){
		this.value = value;
	}
	
	/**
	 * Get the default value of this {@link Setting}
	 * @return See {@link #defaultValue}
	 */
	public T getDefault(){
		return defaultValue;
	}
	
	/**
	 * Set the default value of this {@link Setting}<br>
	 * Generally should not be used outside of constructor
	 * @param defaultValue See {@link #defaultValue}
	 */
	public void setDefault(T defaultValue){
		this.defaultValue = defaultValue;
	}

	/**
	 * Set the value of this {@link Setting} to its default value
	 */
	public void loadDefault(){
		this.set(this.getDefault());
	}
	
	/**
	 * Load in the setting, handling error checking
	 */
	@Override
	public final boolean load(Scanner reader){
		if(reader == null) return false;
		try{
			return this.loadValues(reader);
		}catch(Exception e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Save in the setting, handling error checking
	 */
	@Override
	public final boolean save(PrintWriter writer){
		if(writer == null) return false;
		try{
			return this.saveValues(writer);
		}catch(Exception e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Load the value and default value of this Setting
	 * @param reader The {@link Scanner} object to use loading, do not close this object. 
	 * 	Can assume this object is not null, all calls to this method should never send a null parameter
	 * @return True if the load was successful, false otherwise
	 * @throws Exception Any exception which can happen in loading
	 */
	public abstract boolean loadValues(Scanner reader) throws Exception;
	
	/**
	 * Save the value and default value of this Setting
	 * @param writer The {@link PrintWriter} object to use saving, do not close this object.
	 * 	Can assume this object is not null, all calls to this method should never send a null parameter
	 * @return True if the save was successful, false otherwise
	 * @throws Exception Any exception which can happen in saving
	 */
	public abstract boolean saveValues(PrintWriter writer) throws Exception;
	
	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		
		Setting<?> s = (Setting<?>)obj;
		return 	super.equals(obj) ||
				this.get().equals(s.get()) &&
				this.getDefault().equals(s.getDefault());
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[Setting, Type: ");
		b.append(this.get().getClass().getSimpleName());
		b.append(", value: ");
		b.append(this.get());
		b.append(", default: ");
		b.append(this.getDefault());
		b.append("]");
		return b.toString();
	}
	
}
