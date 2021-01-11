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
import music.Rhythm;
import util.testUtils.UtilsTest;

public class TestSettingRhythm{

	private SettingRhythm setting;
	
	private Rhythm rhythm;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		rhythm = new Rhythm(1, 8);
		setting = new SettingRhythm(rhythm);
	}
	
	@Test
	public void get(){
		assertEquals(rhythm, setting.get(), "Checking value initialized");
		assertFalse(rhythm == setting.get(), "Checking value is not the same object as the initialized value");
	}
	
	@Test
	public void set(){
		Rhythm r = new Rhythm(3, 4);
		setting.set(r);
		assertEquals(r, setting.get(), "Checking value set");
		assertFalse(r == setting.get(), "Checking value is not the same object as the set value");
	}
	
	@Test
	public void getDefault(){
		assertEquals(rhythm, setting.getDefault(), "Checking default value initialized");
		assertFalse(rhythm == setting.getDefault(), "Checking default value is not the same object as the initialized value");
	}
	
	@Test
	public void setDefault(){
		Rhythm r = new Rhythm(3, 4);
		setting.setDefault(r);
		assertEquals(r, setting.getDefault(), "Checking default value set");
		assertFalse(r == setting.getDefault(), "Checking default value is not the same object as the set value");
	}
	
	@Test
	public void loadValues(){
		Scanner scan = new Scanner(""
				+ "7 8 \n"
				+ "3 5 \n"
				+ "2 3 \n"
				+ "6 7 \n"
				+ "3 a \n");
		
		assertTrue(setting.load(scan), "Checking load successful");
		assertEquals(new Rhythm(7, 8), setting.get(), "Checking loaded correct value");
		assertEquals(new Rhythm(3, 5), setting.getDefault(), "Checking loaded correct default value");
		
		assertTrue(setting.load(scan), "Checking load successful");
		assertEquals(new Rhythm(2, 3), setting.get(), "Checking loaded correct value");
		assertEquals(new Rhythm(6, 7), setting.getDefault(), "Checking loaded correct default value");
		
		assertFalse(setting.load(scan), "Checking load fails with invalid data");
		
		scan.close();
	}
	
	@Test
	public void saveValues(){
		assertEquals(""
				+ "1 8 \n"
				+ "1 8 \n", UtilsTest.testSave(setting), "Checking saved correct values");
		
		setting.set(new Rhythm(3, 5));
		setting.setDefault(new Rhythm(7, 9));
		assertEquals(""
				+ "3 5 \n"
				+ "7 9 \n", UtilsTest.testSave(setting), "Checking saved correct values");
	}
	
	@AfterEach
	public void end(){}
	
}
