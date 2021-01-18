package settings;

import java.io.PrintWriter;
import java.util.Scanner;

import util.Saveable;

/**
 * A {@link Setting} specifically designed for handling {@link Number} objects
 * @param T The type of number for this setting
 * @author zrona
 */
public abstract class SettingNumber<T extends Number> extends NotNullSetting<T>{
	
	/** 
	 * Maximum numerical value which this {@link SettingNumber} can take on, must be greater than {@link #minValue}, otherwise undefined behavior occurs. 
	 * Can be null to represent no maximum
	 */
	private T maxValue;
	/**
	 * Minimum numerical value which this {@link SettingNumber} can take on, must be less than {@link #maxValue}, otherwise undefined behavior occurs. 
	 * Can be null to represent no maximum
	 */
	private T minValue;
	
	/**
	 * Create a new {@link SettingNumber} with the given values
	 * @param value The numerical value of this {@link SettingNumber}
	 * @param minValue See {@link #minValue}
	 * @param maxValue See {@link #maxValue}
	 */
	public SettingNumber(T value, T minValue, T maxValue){
		super(value);
		this.maxValue = maxValue;
		this.setMin(minValue);
	}
	
	/**
	 * Create a new {@link SettingNumber} with the given value
	 * @param value The numerical value of this {@link SettingNumber}
	 */
	public SettingNumber(T value){
		this(value, null, null);
	}
	
	/**
	 * Take the given number and return a number which is either equal to itself or the minimum or maximum of this {@link SettingNumber} 
	 * ensuring that which ever value is returned is one which is inside the minimum and maximum range
	 * @param value The value to process
	 * @return Either the same value, or the minimum or maximum of this {@link SettingNumber} 
	 */
	public T keepNumberInRange(T value){
		if(value == null) return null;
		Double v = value.doubleValue();
		T maxObj = this.getMax();
		T minObj = this.getMin();
		if(minObj != null && v < minObj.doubleValue()) value = minObj;
		if(maxObj != null && v > maxObj.doubleValue()) value = maxObj;
		return value;
	}
	
	/**
	 * Ensure that the current value and default values are in the current minimum and maximum range
	 */
	private void keepValuesInRange(){
		this.set(this.get());
		this.setDefault(this.getDefault());
	}
	
	/**
	 * Set the value of this {@link SettingNumber}, ensuring it does not exceed the minimum or maximum
	 */
	@Override
	public void set(T value){
		super.set(this.keepNumberInRange(value));
	}

	/**
	 * Set the default value of this {@link SettingNumber}, ensuring it does not exceed the minimum or maximum
	 */
	@Override
	public void setDefault(T value){
		super.setDefault(this.keepNumberInRange(value));
	}
	
	/** @return See {@link #minValue} */
	public T getMin(){
		return this.minValue;
	}
	
	/** 
	 * Set the minimum value and ensure the current value and default values are in the new range
	 * @param minValue See {@link #minValue} 
	 */
	public void setMin(T minValue){
		T maxObj = this.getMax();
		if(maxObj != null){
			double min = minValue.doubleValue();
			double max = maxObj.doubleValue();
			// If the new minimum value is greater than the current maximum, then both the minimum and maximum will have the same value
			// This ensures minimum can never be greater than maximum
			if(min > max) this.maxValue = minValue;
		}
		this.minValue = minValue;
		this.keepValuesInRange();
	}
	
	/** @return See {@link #maxValue} */
	public T getMax(){
		return this.maxValue;
	}
	
	/** 
	 * Set the maximum value and ensure the current value and default values are in the new range
	 * @param maxValue See {@link #maxValue}
	 */
	public void setMax(T maxValue){
		T minObj = this.getMin();
		if(minObj != null){
			double min = minObj.doubleValue();
			double max = maxValue.doubleValue();
			// If the new maximum value is less than the current minimum, then both the minimum and maximum will have the same value
			// This ensures maximum can never be less than minimum
			if(max < min) this.minValue = maxValue;
		}
		this.maxValue = maxValue;
		this.keepValuesInRange();
	}
	
	/**
	 * Convert the given {@link String} into the valid type of this SettingNumber
	 * @param n The {@link Number} as a string
	 * @return The valid value for this {@link Setting}, should return null if n cannot be converted into the type
	 */
	public abstract T parseType(String n);
	
	/**
	 * Add the given {@link Number} to the value of this {@link SettingNumber}
	 * @param toAdd The number to add, can be a negative number to subtract
	 */
	public abstract void add(T toAdd);
	
	@Override
	public boolean loadValues(Scanner reader) throws Exception{
		// Load the 4 values, if an issue happened, return false
		Object[] load = Saveable.loadObjects(reader, 4, 6);
		if(load == null) return false;

		// Set the minimum and maximum values directly, these values are permitted to be null
		this.minValue = this.parseType(load[2].toString());
		this.maxValue = this.parseType(load[3].toString());
		// Set the value and default value, if either is null, return false
		T v = this.parseType(load[0].toString());
		if(v == null) return false;
		this.set(v);
		
		T d = this.parseType(load[1].toString());
		if(d == null) return false;
		this.setDefault(d);
		
		return Saveable.nextLine(reader);
	}
	
	@Override
	public boolean saveValues(PrintWriter writer) throws Exception{
		T v = this.get();
		T d = this.getDefault();
		T min = this.getMin();
		T max = this.getMax();
		
		// Save all 4 values
		if(!Saveable.saveToStrings(writer, new String[]{
				String.valueOf(v), String.valueOf(d), String.valueOf(min), String.valueOf(max), 
		})) return false;
		
		// Go to the next line
		return Saveable.newLine(writer);
	}
	
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[Setting, Type: ");
		b.append(this.get().getClass().getSimpleName());
		b.append(", value: ");
		b.append(this.get());
		b.append(", default: ");
		b.append(this.getDefault());
		b.append(", min: ");
		b.append(this.getMin());
		b.append(", max: ");
		b.append(this.getMax());
		b.append("]");
		return b.toString();}
	
}
