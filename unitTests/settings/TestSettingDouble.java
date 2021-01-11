package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

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
		assertEquals(2.1, setting.get(), "Checking value initialized");
		assertEquals(2.1, setting.getDefault(), "Checking default value initialized");
		assertEquals(null, setting.getMin(), "Checking min initialized");
		assertEquals(null, setting.getMax(), "Checking max initialized");
		
		assertEquals(3.2, settingFull.get(), "Checking value initialized");
		assertEquals(3.2, settingFull.getDefault(), "Checking default value initialized");
		assertEquals(1.1, settingFull.getMin(), "Checking min initialized");
		assertEquals(6.5, settingFull.getMax(), "Checking max initialized");
	}
	
	@Test
	public void parseType(){
		assertEquals(3.3, setting.parseType("3.3"), "Checking parsing number works");
		assertEquals(null, setting.parseType("h"), "Checking parsing invalid returns null");
		assertEquals(null, setting.parseType("null"), "Checking parsing invalid returns null");
	}
	
	@AfterEach
	public void end(){}
	
}
