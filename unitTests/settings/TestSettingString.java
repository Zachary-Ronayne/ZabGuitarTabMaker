package settings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import appUtils.ZabAppSettings;
import util.testUtils.UtilsTest;

public class TestSettingString{
	
	private SettingString setting;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
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
		
		assertEquals("q z", setting.get(), "Checking value set");
		assertEquals("q z", setting.getDefault(), "Checking default value set");
	}
	
	@Test
	public void set(){
		setting.set("test");
		setting.set("line\n");
		assertEquals("test", setting.get(), "Checking value was not changed with a new line character");
		
		setting.set(null);
		assertEquals("", setting.get(), "Checking value empty after setting to null");
	}
	
	@Test
	public void setDefault(){
		setting.setDefault("test");
		setting.setDefault("line\n");
		assertEquals("test", setting.getDefault(), "Checking default value was not changed with a new line character");
	}
	
	@Test
	public void loadValues(){
		Scanner scan = new Scanner("word z\ndefault z\n123\nsdf\nk");
		assertTrue("Checking load successful", setting.load(scan));
		assertEquals("word z", setting.get(), "Checking value loaded from scanner");
		assertEquals("default z", setting.getDefault(), "Checking default value loaded from scanner");

		assertTrue("Checking load successful", setting.load(scan));
		assertEquals("123", setting.get(), "Checking value loaded from scanner");
		assertEquals("sdf", setting.getDefault(), "Checking default value loaded from scanner");

		assertFalse("Checking load fails with not enough data", setting.load(scan));
		
		scan.close();
	}
	
	@Test
	public void saveValues(){
		setting.setDefault("s w");
		assertEquals("q z\ns w\n", UtilsTest.testSave(setting), "Checking setting saved saved by writer");
	}

	@Test
	public void cleanValue(){
		assertEquals("a", SettingString.cleanValue("a"), "Checking cleaned returns the same value");
		assertEquals("", SettingString.cleanValue(null), "Checking cleaned null returns empty string");
		assertEquals(null, SettingString.cleanValue("word\n test"), "Checking cleaned null returns null with new line");
	}
	
	@AfterEach
	public void end(){}
	
}
