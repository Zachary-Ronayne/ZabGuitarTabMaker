package settings;

import java.io.PrintWriter;
import java.util.Scanner;

import music.Rhythm;
import util.Saveable;

/**
 * A {@link Setting} used to hold a {@link Rhythm}
 * @author zrona
 */
public class SettingRhythm extends NotNullSetting<Rhythm>{
	
	/**
	 * Create a new {@link Rhythm} setting with the given value
	 * @param value The default and current value
	 */
	public SettingRhythm(Rhythm value){
		super(value);
	}
	
	/**
	 * Returns a copy of the value, not the original object 
	 */
	@Override
	public Rhythm get(){
		return super.get().copy();
	}
	
	/**
	 * Set's this setting's value to a copy of the parameter, it does not use the actual object
	 */
	@Override
	public void set(Rhythm value){
		super.set(value.copy());
	}

	/**
	 * Returns a copy of the default value, not the original object 
	 */
	@Override
	public Rhythm getDefault(){
		return super.getDefault().copy();
	}

	/**
	 * Set's this setting's default value to a copy of the parameter, it does not use the actual object
	 */
	@Override
	public void setDefault(Rhythm defaultValue){
		super.setDefault(defaultValue.copy());
	}

	@Override
	public boolean loadValues(Scanner reader) throws Exception{
		// Create objects to load
		Rhythm v = new Rhythm(1, 1);
		Rhythm d = new Rhythm(1, 1);
		
		// Load the objects
		if(!Saveable.loadMultiple(reader, new Rhythm[]{v, d})) return false;
		
		// Set the values
		this.set(v);
		this.setDefault(d);
		return true;
	}

	@Override
	public boolean saveValues(PrintWriter writer) throws Exception{
		return Saveable.saveMultiple(writer, new Rhythm[]{this.get(), this.getDefault()});
	}
	
}
