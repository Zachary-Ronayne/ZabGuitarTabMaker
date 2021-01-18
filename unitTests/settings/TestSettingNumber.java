package settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import util.testUtils.UtilsTest;

public class TestSettingNumber{

	private NumberTest setting;
	private NumberTest settingFull;
	
	/** A class for testing the abstract SettingNumber */
	class NumberTest extends SettingNumber<Integer>{
		public NumberTest(Integer value, Integer min, Integer max){super(value, min, max);}
		public NumberTest(Integer value){super(value);}
		@Override
		public Integer parseType(String n){
			try{
				return Integer.parseInt(n);
			}catch(NumberFormatException e){return null;}
		}
		@Override
		public void add(Integer toAdd){
			this.set(this.get() + toAdd);
		}
	}
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		setting = new NumberTest(3);
		settingFull = new NumberTest(2, -1, 5);
	}
	
	@Test
	public void constructor(){
		assertEquals(3, setting.get(), "Checking value initialized");
		assertEquals(3, setting.getDefault(), "Checking default value initialized");
		
		assertEquals(2, settingFull.get(), "Checking value initialized");
		assertEquals(2, settingFull.getDefault(), "Checking default value initialized");
	}
	
	@Test
	public void keepNumberInRange(){
		assertEquals(1, settingFull.keepNumberInRange(1), "Checking value in the middle of the range gives the same");
		assertEquals(-1, settingFull.keepNumberInRange(-2), "Checking value too low gives min");
		assertEquals(5, settingFull.keepNumberInRange(8), "Checking value too high gives max");
		assertEquals(null, settingFull.keepNumberInRange(null), "Checking null value gives null");
		
		assertEquals(1, setting.keepNumberInRange(1), "Checking value unchanged with no bounds");
		assertEquals(-2, setting.keepNumberInRange(-2), "Checking value unchanged with no bounds");
		assertEquals(8, setting.keepNumberInRange(8), "Checking value unchanged with no bounds");
		assertEquals(null, setting.keepNumberInRange(null), "Checking null value gives null");
	}
	
	@Test
	public void set(){
		settingFull.set(-2);
		assertEquals(-1, settingFull.get(), "Checking value set to min");
		
		settingFull.set(9);
		assertEquals(5, settingFull.get(), "Checking value set to max");
		
		settingFull.set(null);
		assertEquals(5, settingFull.get(), "Checking value unchanged after set to null");
	}
	
	@Test
	public void setDefaultValue(){
		settingFull.setDefault(-2);
		assertEquals(-1, settingFull.getDefault(), "Checking default value set to min");
		
		settingFull.setDefault(9);
		assertEquals(5, settingFull.getDefault(), "Checking default value set to max");
		
		settingFull.setDefault(null);
		assertEquals(5, settingFull.getDefault(), "Checking default value unchanged after set to null");
	}
	
	@Test
	public void getMin(){
		assertEquals(null, setting.getMin(), "Checking min set");
		assertEquals(-1, settingFull.getMin(), "Checking min set");
	}
	
	@Test
	public void setMin(){
		// Checking sets the min field
		setting.setMin(4);
		assertInitialized(4, 4, 4, null, setting);
		
		// Checking sets the max field when min is below max
		setting.setMax(4);
		setting.setMin(7);
		assertInitialized(7, 7, 7, 7, setting);
		
		// Checking only min value is updated
		setting.setMin(2);
		assertInitialized(7, 7, 2, 7, setting);
	}
	
	@Test
	public void getMax(){
		assertEquals(null, setting.getMax(), "Checking max set");
		assertEquals(5, settingFull.getMax(), "Checking max set");
	}
	
	@Test
	public void setMax(){
		// Checking sets the max field
		setting.setMax(1);
		assertInitialized(1, 1, null, 1, setting);

		// Checking sets the min field when max is below min
		setting.setMin(1);
		setting.setMax(-2);
		assertInitialized(-2, -2, -2, -2, setting);
		
		// Checking only max value is updated
		setting.setMax(2);
		assertInitialized(-2, -2, -2, 2, setting);
	}
	
	@Test
	public void loadValues(){
		Scanner scan = new Scanner(""
				+ "1 2 0 3 \n"
				+ "-1 3 -1 5 \n"
				+ "1 2 null 3 \n"
				+ "2 4 0 null \n"
				+ "1 2 null null \n"
				+ "a a a a\n"); 
		
		assertTrue(setting.load(scan), "Checking load successful");
		assertInitialized(1, 2, 0, 3, setting);
		
		assertTrue(setting.load(scan), "Checking load successful");
		assertInitialized(-1, 3, -1, 5, setting);
		
		assertTrue(setting.load(scan), "Checking load successful");
		assertInitialized(1, 2, null, 3, setting);
		
		assertTrue(setting.load(scan), "Checking load successful");
		assertInitialized(2, 4, 0, null, setting);
		
		assertTrue(setting.load(scan), "Checking load successful");
		assertInitialized(1, 2, null, null, setting);
		
		assertFalse(setting.load(scan), "Checking load fails with invalid value");
		
		scan.close();
		scan = new Scanner("1 a a a \n");
		assertFalse(setting.load(scan), "Checking load fails with invalid default value");
		
		scan.close();
		scan = new Scanner("1 \n");
		assertFalse(setting.load(scan), "Checking load fails with not enough data");
		
		scan.close();
		scan = new Scanner(""
				+ "1 2 0 10 \n"
				+ "a 1");
		assertTrue(setting.load(scan), "Checking normal load succeeds");
		assertEquals("a", scan.next(), "Checking next in scan is the correct value");
		assertEquals("1", scan.next(), "Checking next in scan is the correct value");
		
		scan.close();
		scan = new Scanner(""
				+ "1 2 0 10 \n"
				+ "a 1");
		assertTrue(setting.load(scan), "Checking normal load succeeds");
		assertEquals("a 1", scan.nextLine(), "Checking next line in scan is the correct value");
		
		scan.close();
	}
	
	/**
	 * Utility method for checking if a NumberSetting has values set
	 * @param value The expected value
	 * @param defaultValue The expected default value
	 * @param min The expected min valid
	 * @param max The expected max value
	 * @param set The setting to check
	 */
	public static void assertInitialized(Number value, Number defaultValue, Number min, Number max, SettingNumber<?> set){
		assertEquals(value, set.get(), "Checking value set");
		assertEquals(defaultValue, set.getDefault(), "Checking default value set");
		assertEquals(min, set.getMin(), "Checking min set");
		assertEquals(max, set.getMax(), "Checking max set");
	}
	
	@Test
	public void saveValues() throws Exception{
		assertFalse(setting.saveValues(null), "Checking save fails with invalid writer");
		
		assertEquals("3 3 null null \n", UtilsTest.testSave(setting), "Checking setting saved correctly");
		assertEquals("2 2 -1 5 \n", UtilsTest.testSave(settingFull), "Checking setting saved correctly");
		
		setting.setMax(10);
		setting.setMin(-9);
		setting.set(5);
		setting.setDefault(-1);
		assertEquals("5 -1 -9 10 \n", UtilsTest.testSave(setting), "Checking setting saved correctly");
		
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		BufferedOutputStream bs = new BufferedOutputStream(bytes);
		PrintWriter write = new PrintWriter(bs);
		setting.save(write);
		setting.set(4);
		setting.save(write);
		write.close();
		String result = UtilsTest.removeSlashR(bytes.toString());
		assertEquals(""
				+ "5 -1 -9 10 \n"
				+ "4 -1 -9 10 \n", result, "Checking two saved numbers are correct");
	}

	@Test
	public void testToString(){
		assertEquals("[Setting, Type: Integer, value: 2, default: 2, min: -1, max: 5]", settingFull.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
