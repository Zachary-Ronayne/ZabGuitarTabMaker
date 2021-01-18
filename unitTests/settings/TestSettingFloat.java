package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import util.testUtils.UtilsTest;

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
		// Checking default values initialized
		TestSettingNumber.assertInitialized(2.1F, 2.1F, null, null, setting);

		// Checking full constructor values initialized
		TestSettingNumber.assertInitialized(3.2F, 3.2F, 1.1F, 6.5F, settingFull);
	}
	
	@Test
	public void parseType(){
		assertEquals(3.3F, setting.parseType("3.3"), "Checking parsing number works");
		assertEquals(null, setting.parseType("h"), "Checking parsing invalid returns null");
		assertEquals(null, setting.parseType("null"), "Checking parsing invalid returns null");
	}
	
	@Test
	public void add(){
		setting.add(0.1F);
		assertEquals(2.2F, setting.get(), UtilsTest.DELTA_SMALL, "Checking value added");
		
		setting.add(-0.3F);
		assertEquals(1.9F, setting.get(), UtilsTest.DELTA_SMALL, "Checking value subtracted");
	}
	
	@AfterEach
	public void end(){}
	
}
