package settings;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * A setting containing a String, the string should not include new line characters
 * @author zrona
 */
public class SettingString extends Setting<String>{

	public SettingString(String value){
		super(value);
		if(value.contains("\n")) throw new IllegalArgumentException("StringSettings cannot contain new line characters");
	}

	/***/
	@Override
	public void loadValue(Scanner reader) throws Exception{
		this.setValue(reader.nextLine());
	}
	
	/***/
	@Override
	public void saveValue(PrintWriter writer) throws Exception{
		writer.println(this.getValue());
	}

}
