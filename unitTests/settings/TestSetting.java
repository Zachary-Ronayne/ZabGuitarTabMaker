package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestSetting{

	private TestSettingObject settingNoDefault;
	private TestSettingObject setting;
	
	class TestSettingObject extends Setting<String>{
		public TestSettingObject(String value){
			super(value);
		}
		public TestSettingObject(String value, String defaultValue){
			super(value, defaultValue);
		}
		@Override
		public boolean loadValues(Scanner reader) throws Exception{
			if(this.get().equals("error")) throw new Exception();
			return !this.get().equals("break");
		}
		@Override
		public boolean saveValues(PrintWriter writer) throws Exception{
			if(this.get().equals("error")) throw new Exception();
			return !this.get().equals("break");
		}
	}
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		settingNoDefault = new TestSettingObject("a");
		setting = new TestSettingObject("b", "c");
	}
	
	@Test
	public void getValue(){
		assertEquals("a", settingNoDefault.get(), "Checking value initialized");
		assertEquals("b", setting.get(), "Checking value initialized");
	}
	
	@Test
	public void setValue(){
		settingNoDefault.set("d");
		assertEquals("d", settingNoDefault.get(), "Checking value set");
	}
	
	@Test
	public void getDefaultValue(){
		assertEquals("a", settingNoDefault.getDefault(), "Checking default value initialized");
		assertEquals("c", setting.getDefault(), "Checking default value initialized");
	}
	
	@Test
	public void setDefaultValue(){
		settingNoDefault.setDefault("e");
		assertEquals("e", settingNoDefault.getDefault(), "Checking default value set");
	}
	
	@Test
	public void loadDefault(){
		setting.loadDefault();
		assertEquals("c", setting.getDefault(), "Checking value updated to default value");
	}
	
	@Test
	public void load(){
		Scanner scan;
		assertFalse(setting.load(null), "Checking load detects errors on null");
		
		scan = new Scanner("");
		assertTrue(setting.load(scan), "Checking load detects good loading");
		
		scan.close();
		scan = new Scanner("");
		setting.set("break");
		assertFalse(setting.load(scan), "Checking load detects bad loading");

		scan.close();
		scan = new Scanner("");
		setting.set("error");
		assertFalse(setting.load(scan), "Checking load detects error in loading");
		
		scan.close();
	}

	@Test
	public void save(){
		PrintWriter write;
		assertFalse(setting.save(null), "Checking save detects errors on null");
		
		write = new PrintWriter(new ByteArrayOutputStream());
		assertTrue(setting.save(write), "Checking save detects good saving");
		write.close();

		write = new PrintWriter(new ByteArrayOutputStream());
		setting.set("break");
		assertFalse(setting.save(write), "Checking save detects bad saving");
		write.close();

		write = new PrintWriter(new ByteArrayOutputStream());
		setting.set("error");
		assertFalse(setting.save(write), "Checking save detects error in saving");
		write.close();
	}

	@Test
	public void equals(){
		assertTrue(setting.equals(setting), "Checking object is equal to itself");
		assertFalse(setting.equals(null), "Checking object is not equal to null");
		
		TestSettingObject newSetting = new TestSettingObject("a");
		assertTrue(settingNoDefault.equals(newSetting), "Checking two setting are equal");
		assertFalse(settingNoDefault == newSetting, "Checking two setting are not the same object");
		
		newSetting.set("b");
		assertFalse(settingNoDefault.equals(newSetting), "Checking two setting are not equal with different values");
		
		newSetting.set("a");
		assertTrue(settingNoDefault.equals(newSetting), "Checking two setting are still equal");
		
		newSetting.setDefault("b");
		assertFalse(settingNoDefault.equals(newSetting), "Checking two setting are not equal with different default values");
	}
	
	@Test
	public void testToString(){
		assertEquals("[Setting, Type: String, value: b, default: c]", setting.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
