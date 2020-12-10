package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import util.testUtils.UtilsTest;

public class TestSettingString{
	
	private SettingString setting;
	
	@BeforeEach
	public void setup(){
		setting = new SettingString("q");
	}
	
	@Test
	public void constructor(){
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new SettingString("\n");
			}
		}, "Checking that a new line character causes an error");
	}
	
	@Test
	public void loadValue(){
		Scanner read = new Scanner("word");
		setting.load(read);
		assertEquals("word", setting.getValue(), "Checking value loaded from scanner");
	}
	
	@Test
	public void saveValue() throws FileNotFoundException{
		String result = UtilsTest.testSave(setting);
		assertEquals("q\n", result, "Checking value saved by writer");
	}
	
	@AfterEach
	public void end(){}
	
}
