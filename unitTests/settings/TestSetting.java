package settings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		assertFalse("Checking load detects errors on null", setting.load(null));
		
		scan = new Scanner("");
		assertTrue("Checking load detects good loading", setting.load(scan));
		
		scan.close();
		scan = new Scanner("");
		setting.set("break");
		assertFalse("Checking load detects bad loading", setting.load(scan));

		scan.close();
		scan = new Scanner("");
		setting.set("error");
		assertFalse("Checking load detects error in loading", setting.load(scan));
		
		scan.close();
	}

	@Test
	public void save(){
		PrintWriter write;
		assertFalse("Checking save detects errors on null", setting.save(null));
		
		write = new PrintWriter(new ByteArrayOutputStream());
		assertTrue("Checking save detects good saving", setting.save(write));
		write.close();

		write = new PrintWriter(new ByteArrayOutputStream());
		setting.set("break");
		assertFalse("Checking save detects bad saving", setting.save(write));
		write.close();

		write = new PrintWriter(new ByteArrayOutputStream());
		setting.set("error");
		assertFalse("Checking save detects error in saving", setting.save(write));
		write.close();
	}

	@Test
	public void equals(){
		TestSettingObject newSetting = new TestSettingObject("a");
		assertTrue("Checking two setting are equal", settingNoDefault.equals(newSetting));
		assertFalse("Checking two setting are not the same object", settingNoDefault == newSetting);
		
		newSetting.set("b");
		assertFalse("Checking two setting are not equal with different values", settingNoDefault.equals(newSetting));
		
		newSetting.set("a");
		assertTrue("Checking two setting are still equal", settingNoDefault.equals(newSetting));
		
		newSetting.setDefault("b");
		assertFalse("Checking two setting are not equal with different default values", settingNoDefault.equals(newSetting));
	}
	
	@Test
	public void testToString(){
		assertEquals("[Setting, Type: String, value: b, default: c]", setting.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
