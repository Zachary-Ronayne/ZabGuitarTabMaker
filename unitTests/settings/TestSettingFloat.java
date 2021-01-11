package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestSettingFloat{

	private SettingFloat setting;
	private SettingFloat settingFull;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		setting = new SettingFloat(2.1F);
		settingFull = new SettingFloat(3.2F, 1.1F, 6.5F);
	}

	@Test
	public void constructor(){
		assertEquals(2.1F, setting.get(), "Checking value initialized");
		assertEquals(2.1F, setting.getDefault(), "Checking default value initialized");
		assertEquals(null, setting.getMin(), "Checking min initialized");
		assertEquals(null, setting.getMax(), "Checking max initialized");
		
		assertEquals(3.2F, settingFull.get(), "Checking value initialized");
		assertEquals(3.2F, settingFull.getDefault(), "Checking default value initialized");
		assertEquals(1.1F, settingFull.getMin(), "Checking min initialized");
		assertEquals(6.5F, settingFull.getMax(), "Checking max initialized");
	}
	
	@Test
	public void parseType(){
		assertEquals(3.3F, setting.parseType("3.3"), "Checking parsing number works");
		assertEquals(null, setting.parseType("h"), "Checking parsing invalid returns null");
		assertEquals(null, setting.parseType("null"), "Checking parsing invalid returns null");
	}
	
	@AfterEach
	public void end(){}
	
}
