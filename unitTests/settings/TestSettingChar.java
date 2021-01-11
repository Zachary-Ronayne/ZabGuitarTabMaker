package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import appUtils.ZabAppSettings;
import util.testUtils.UtilsTest;

public class TestSettingChar{

	private SettingChar setting;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		setting = new SettingChar('j');
		setting.setDefault('d');
	}
	
	@Test
	public void constructor(){
		assertThrows(IllegalArgumentException.class, 
				new Executable(){
			@Override
			public void execute() throws Throwable{
				new SettingChar('\n');
			}
		}, "Checking exception thrown on new line character (\\n)");
		
		assertThrows(IllegalArgumentException.class, 
			new Executable(){
				@Override
				public void execute() throws Throwable{
					new SettingChar('\t');
				}
		}, "Checking exception thrown on tab character (\\t)");
	}
	
	@Test
	public void set(){
		setting.set('9');
		assertEquals('9', setting.get(), "Checking value set");
		
		setting.set('b');
		assertEquals('b', setting.get(), "Checking value changed");
		
		setting.set('\n');
		assertEquals('b', setting.get(), "Checking unchanged after trying to set to a new line");
		
		setting.set('\t');
		assertEquals('b', setting.get(), "Checking unchanged after trying to set to a tab");
	}
	
	@Test
	public void setDefault(){
		setting.setDefault('9');
		assertEquals('9', setting.getDefault(), "Checking value set");
		
		setting.setDefault('b');
		assertEquals('b', setting.getDefault(), "Checking value changed");
		
		setting.setDefault('\n');
		assertEquals('b', setting.getDefault(), "Checking unchanged after trying to set to a new line");
		
		setting.setDefault('\t');
		assertEquals('b', setting.getDefault(), "Checking unchanged after trying to set to a tab");
	}
	
	@Test
	public void loadValues(){
		Scanner scan = new Scanner(""
				+ "a\n"
				+ "s\n"
				+ "c\n"
				+ " \n"
				+ "a\n");
		
		assertTrue(setting.load(scan), "Checking load successful");
		assertEquals('a', setting.get(), "Checking value loaded");
		assertEquals('s', setting.getDefault(), "Checking default value loaded");
		
		assertTrue(setting.load(scan), "Checking load successful");
		assertEquals('c', setting.get(), "Checking value loaded");
		assertEquals(' ', setting.getDefault(), "Checking default value loaded");
		
		assertFalse(setting.load(scan), "Checking load fails without enough data");
		
		scan.close();
	}
	
	@Test
	public void saveValues(){
		assertEquals("j\nd\n", UtilsTest.testSave(setting), "Checking save successful");
		
		setting.set('r');
		setting.setDefault(' ');
		assertEquals("r\n \n", UtilsTest.testSave(setting), "Checking save successful");
	}
	
	@Test
	public void isValid(){
		assertFalse(SettingChar.isValid(null), "Checking null is invalid");
		assertFalse(SettingChar.isValid('\n'), "Checking new line is invalid");
		assertFalse(SettingChar.isValid('\t'), "Checking tab is invalid");
		assertTrue(SettingChar.isValid(' '), "Checking space is valid");
		assertTrue(SettingChar.isValid('u'), "Checking letter is valid");
	}
	
	@AfterEach
	public void end(){}
	
}
