package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import appUtils.ZabAppSettings;

public class TestNotNullSetting{
	
	private NotNullTest setting;
	private NotNullTest settingBoth;
	
	/** Class for testing abstract not null setting */
	private class NotNullTest extends NotNullSetting<Integer>{
		public NotNullTest(Integer value){super(value);}
		public NotNullTest(Integer value, Integer defaultValue){super(value, defaultValue);}
		@Override
		public boolean loadValues(Scanner reader) throws Exception{return false;}
		@Override
		public boolean saveValues(PrintWriter writer) throws Exception{return false;}
	}
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		setting = new NotNullTest(2);
		settingBoth = new NotNullTest(3, 4);
	}
	
	@Test
	public void constructor(){
		assertThrows(IllegalArgumentException.class, 
				new Executable(){
			@Override
			public void execute() throws Throwable{
				new NotNullTest(null);
			}
		}, "Checking null value with single argument constructor throws exception");
		
		assertThrows(IllegalArgumentException.class, 
				new Executable(){
			@Override
			public void execute() throws Throwable{
				new NotNullTest(null, 1);
			}
		}, "Checking null value with double argument constructor throws exception");
		
		assertThrows(IllegalArgumentException.class, 
				new Executable(){
			@Override
			public void execute() throws Throwable{
				new NotNullTest(1, null);
			}
		}, "Checking null value with double argument constructor throws exception");
		
		assertEquals(2, setting.get(), "Checking value initialized");
		assertEquals(2, setting.getDefault(), "Checking default value initialized");
		
		assertEquals(3, settingBoth.get(), "Checking value initialized");
		assertEquals(4, settingBoth.getDefault(), "Checking default value initialized");
	}
	
	@Test
	public void set(){
		setting.set(6);
		assertEquals(6, setting.get(), "Checking value set");
		
		setting.set(null);
		assertEquals(6, setting.get(), "Checking value unchanged with null value");
	}
	
	@Test
	public void setDefault(){
		setting.setDefault(7);
		assertEquals(7, setting.getDefault(), "Checking default value set");
		
		setting.setDefault(null);
		assertEquals(7, setting.getDefault(), "Checking default value unchanged with null value");
	}
	
	@AfterEach
	public void end(){}
	
}
