package settings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
		setting = new SettingString("q z");
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
	public void setValue(){
		setting.setValue("test");
		setting.setValue("line\n");
		assertEquals("test", setting.getValue(), "Checking value was not changed with a new line character");
	}
	
	@Test
	public void setDefaultValue(){
		setting.setDefaultValue("test");
		setting.setDefaultValue("line\n");
		assertEquals("test", setting.getDefaultValue(), "Checking default value was not changed with a new line character");
	}
	
	@Test
	public void loadValue(){
		Scanner read = new Scanner("word z\ndefault z\n123\nsdf\nk");
		assertTrue("Checking load successful", setting.load(read));
		assertEquals("word z", setting.getValue(), "Checking value loaded from scanner");
		assertEquals("default z", setting.getDefaultValue(), "Checking default value loaded from scanner");

		assertTrue("Checking load successful", setting.load(read));
		assertEquals("123", setting.getValue(), "Checking value loaded from scanner");
		assertEquals("sdf", setting.getDefaultValue(), "Checking default value loaded from scanner");

		assertFalse("Checking load fails with not enough data", setting.load(read));
	}
	
	@Test
	public void saveValue(){
		setting.setDefaultValue("s w");
		assertEquals("q z\ns w\n", UtilsTest.testSave(setting), "Checking setting saved saved by writer");
	}
	
	@AfterEach
	public void end(){}
	
}
