package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import util.testUtils.UtilsTest;

public class TestSettingDouble{

	private SettingDouble setting;
	private SettingDouble settingFull;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		setting = new SettingDouble(2.1);
		settingFull = new SettingDouble(3.2, 1.1, 6.5);
	}

	@Test
	public void constructor(){
		// Checking default values initialized
		TestSettingNumber.assertInitialized(2.1, 2.1, null, null, setting);

		// Checking full constructor values initialized
		TestSettingNumber.assertInitialized(3.2, 3.2, 1.1, 6.5, settingFull);
	}
	
	@Test
	public void parseType(){
		assertEquals(3.3, setting.parseType("3.3"), "Checking parsing number works");
		assertEquals(null, setting.parseType("h"), "Checking parsing invalid returns null");
		assertEquals(null, setting.parseType("null"), "Checking parsing invalid returns null");
	}
	
	@Test
	public void add(){
		setting.add(0.1);
		assertEquals(2.2, setting.get(), UtilsTest.DELTA, "Checking value added");
		
		setting.add(-0.3);
		assertEquals(1.9, setting.get(), UtilsTest.DELTA, "Checking value subtracted");
	}
	
	@AfterEach
	public void end(){}
	
}
