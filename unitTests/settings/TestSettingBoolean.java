package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import util.testUtils.UtilsTest;

public class TestSettingBoolean{

	private SettingBoolean settingF;
	private SettingBoolean settingT;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		settingF = new SettingBoolean(false);
		settingT = new SettingBoolean(true);
	}
	
	@Test
	public void constructor(){
		assertFalse(settingF.get(), "Checking value initialized");
		assertFalse(settingF.getDefault(), "Checking default value initialized");
		assertTrue(settingT.get(), "Checking value initialized");
		assertTrue(settingT.getDefault(), "Checking default value initialized");
	}
	
	@Test
	public void loadValues(){
		Scanner scan = new Scanner(""
				+ "false true \n"
				+ "true false \n"
				+ "false a \n");
		
		assertTrue(settingF.load(scan), "Checking setting loaded");
		assertFalse(settingF.get(), "Checking value loaded");
		assertTrue(settingF.getDefault(), "Checking default value loaded");
		
		assertTrue(settingF.load(scan), "Checking setting loaded");
		assertTrue(settingF.get(), "Checking value loaded");
		assertFalse(settingF.getDefault(), "Checking default value loaded");
		
		assertFalse(settingF.load(scan), "Checking load fails without enough data");
	}
	
	@Test
	public void saveValues(){
		assertEquals("false false \n", UtilsTest.testSave(settingF), "Checking save value correct");
		
		settingF.setDefault(true);
		assertEquals("false true \n", UtilsTest.testSave(settingF), "Checking save value correct");
	}
	
	@AfterEach
	public void end(){}
	
}
