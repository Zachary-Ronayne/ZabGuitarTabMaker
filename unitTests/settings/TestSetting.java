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

	private TestT settingNoDefault;
	private TestT setting;
	
	class TestT extends Setting<String>{
		public TestT(String value){
			super(value);
		}
		public TestT(String value, String defaultValue){
			super(value, defaultValue);
		}
		@Override
		public void loadValue(Scanner reader) throws Exception{
			if(this.getValue().equals("break")) throw new Exception();
		}
		@Override
		public void saveValue(PrintWriter writer) throws Exception{
			if(this.getValue().equals("break")) throw new Exception();
		}
	}
	
	@BeforeEach
	public void setup(){
		settingNoDefault = new TestT("a");
		setting = new TestT("b", "c");
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
	}

	@Test
	public void save(){
		assertFalse("Checking save detects errors on null", setting.save(null));
		
		assertTrue("Checking save detects good saving", setting.save(new PrintWriter(new ByteArrayOutputStream())));
		
		setting.setValue("break");
		assertFalse("Checking save detects bad saving", setting.save(new PrintWriter(new ByteArrayOutputStream())));
	}

	@Test
	public void equals(){
		TestT newSetting = new TestT("a");
		assertTrue("Checking two setting are equal", settingNoDefault.equals(newSetting));
		assertFalse("Checking two setting are not the same object", settingNoDefault == newSetting);
		
		newSetting.setValue("b");
		assertFalse("Checking two setting are not equal with different values", settingNoDefault.equals(newSetting));
		
		newSetting.setValue("a");
		assertTrue("Checking two setting are still equal", settingNoDefault.equals(newSetting));
		
		newSetting.setDefaultValue("b");
		assertFalse("Checking two setting are not equal with different default values", settingNoDefault.equals(newSetting));
	}
	
	@AfterEach
	public void end(){}
	
}
