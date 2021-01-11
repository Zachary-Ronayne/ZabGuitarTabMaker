package settings;

import java.io.PrintWriter;
import java.util.Scanner;

import util.Saveable;

/**
 * A class specifically for handling boolean settings, i.e. settings which either are or are not
 * @author zrona
 */
public class SettingBoolean extends NotNullSetting<Boolean>{

	/**
	 * Create a new {@link SettingBoolean} with the given value
	 * @param value The boolean value
	 */
	public SettingBoolean(Boolean value){
		super(value);
	}
	/***/
	@Override
	public boolean loadValues(Scanner reader) throws Exception{
		this.set(reader.nextBoolean());
		this.setDefault(reader.nextBoolean());
		return Saveable.nextLine(reader);
	}

	/***/
	@Override
	public boolean saveValues(PrintWriter writer) throws Exception{
		if(!Saveable.saveToStrings(writer, new Boolean[]{this.get(), this.getDefault()})) return false;
		return Saveable.newLine(writer);
	}

}
