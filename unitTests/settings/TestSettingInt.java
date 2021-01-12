package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestSettingInt{

	private SettingInt setting;
	private SettingInt settingFull;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		setting = new SettingInt(2);
		settingFull = new SettingInt(3, 1, 6);
	}

	@Test
	public void constructor(){
		// Checking default values initialized
		TestSettingNumber.assertInitialized(2, 2, null, null, setting);

		// Checking full constructor values initialized
		TestSettingNumber.assertInitialized(3, 3, 1, 6, settingFull);
	}
	
	@Test
	public void parseType(){
		assertEquals(3, setting.parseType("3"), "Checking parsing number works");
		assertEquals(null, setting.parseType("3.1"), "Checking parsing invalid returns null");
		assertEquals(null, setting.parseType("h"), "Checking parsing invalid returns null");
		assertEquals(null, setting.parseType("null"), "Checking parsing invalid returns null");
	}
	
	@AfterEach
	public void end(){}
	
}
