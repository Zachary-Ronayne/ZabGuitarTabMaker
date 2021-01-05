package settings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		public boolean loadValue(Scanner reader) throws Exception{
			if(this.getValue().equals("error")) throw new Exception();
			return !this.getValue().equals("break");
		}
		@Override
		public boolean saveValue(PrintWriter writer) throws Exception{
			if(this.getValue().equals("error")) throw new Exception();
			return !this.getValue().equals("break");
		}
	}
	
	@BeforeEach
	public void setup(){
		settingNoDefault = new TestSettingObject("a");
		setting = new TestSettingObject("b", "c");
	}
	
	@Test
	public void getValue(){
		assertEquals("a", settingNoDefault.getValue(), "Checking value initialized");
		assertEquals("b", setting.getValue(), "Checking value initialized");
	}
	
	@Test
	public void setValue(){
		settingNoDefault.setValue("d");
		assertEquals("d", settingNoDefault.getValue(), "Checking value set");
	}
	
	@Test
	public void getDefaultValue(){
		assertEquals("a", settingNoDefault.getDefaultValue(), "Checking default value initialized");
		assertEquals("c", setting.getDefaultValue(), "Checking default value initialized");
	}
	
	@Test
	public void setDefaultValue(){
		settingNoDefault.setDefaultValue("e");
		assertEquals("e", settingNoDefault.getDefaultValue(), "Checking default value set");
	}
	
	@Test
	public void loadDefault(){
		setting.loadDefault();
		assertEquals("c", setting.getDefaultValue(), "Checking value updated to default value");
	}
	
	@Test
	public void load(){
		assertFalse("Checking load detects errors on null", setting.load(null));
		
		assertTrue("Checking load detects good loading", setting.load(new Scanner("")));
		
		setting.setValue("break");
		assertFalse("Checking load detects bad loading", setting.load(new Scanner("")));
		
		setting.setValue("error");
		assertFalse("Checking load detects error in loading", setting.load(new Scanner("")));
	}

	@Test
	public void save(){
		assertFalse("Checking save detects errors on null", setting.save(null));
		
		assertTrue("Checking save detects good saving", setting.save(new PrintWriter(new ByteArrayOutputStream())));
		
		setting.setValue("break");
		assertFalse("Checking save detects bad saving", setting.save(new PrintWriter(new ByteArrayOutputStream())));
		
		setting.setValue("error");
		assertFalse("Checking save detects error in saving", setting.save(new PrintWriter(new ByteArrayOutputStream())));
	}

	@Test
	public void equals(){
		TestSettingObject newSetting = new TestSettingObject("a");
		assertTrue("Checking two setting are equal", settingNoDefault.equals(newSetting));
		assertFalse("Checking two setting are not the same object", settingNoDefault == newSetting);
		
		newSetting.setValue("b");
		assertFalse("Checking two setting are not equal with different values", settingNoDefault.equals(newSetting));
		
		newSetting.setValue("a");
		assertTrue("Checking two setting are still equal", settingNoDefault.equals(newSetting));
		
		newSetting.setDefaultValue("b");
		assertFalse("Checking two setting are not equal with different default values", settingNoDefault.equals(newSetting));
	}
	
	@Test
	public void testToString(){
		assertEquals("[Setting, Type: String, value: b, default: c]", setting.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
