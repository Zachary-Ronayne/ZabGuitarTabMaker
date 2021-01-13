package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

public class TestSaveable{

	private Scanner scan;
	private SingleLine saveObj;
	private SingleLine[] saveObjs;
	private DoubleLine saveLine;
	
	private ByteArrayOutputStream bytes;
	private BufferedOutputStream bs;
	private PrintWriter write;
	
	private class SingleLine implements Saveable{
		public int value = 0;
		@Override
		public boolean load(Scanner reader){
			value = reader.nextInt();
			
			// Case for throwing exception
			if(value < 0){
				@SuppressWarnings("unused")
				int x = 1 / 0;
			}
			
			return value == 1 || value == 10;
		}
		@Override
		public boolean save(PrintWriter writer){
			// Case for failing saving
			if(value == 5) return false;
			
			// Case for throwing exception
			if(value < 0){
				@SuppressWarnings("unused")
				int x = 1 / 0;
			}
			
			writer.print(value + " ");
			return true;
		}
	}

	private class DoubleLine implements Saveable{
		Integer a = 0;
		Boolean b = true;
		String c = "";
		@Override
		public boolean load(Scanner reader){
			a = Saveable.loadInt(reader);
			b = Saveable.loadBool(reader);
			reader.nextLine();
			c = Saveable.loadString(reader);
			if(a == null || b == null || c == null) return false;
			return true;
		}
		@Override
		public boolean save(PrintWriter writer){
			writer.println(a + " " + b);
			writer.println(c);
			return true;
		}
	}
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		saveObj = new SingleLine();
		saveObjs = new SingleLine[]{new SingleLine(), new SingleLine()};
		saveLine = new DoubleLine();
		
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
	}
	
	@Test
	public void loadObject(){
		scan = new Scanner(""
				+ "true false 3 -2 3 387246587654 3.5345 -7.2345 2.1436543764765476 0\n"
				+ "word\n"
				+ "space word\n"
				+ "big b-/102\n"
				+ "h\n"
				+ "7");
		
		assertEquals(true, Saveable.loadObject(scan, 0), "Checking boolean value loaded");
		assertEquals(false, Saveable.loadObject(scan, 0), "Checking boolean value loaded");
		
		assertEquals(3, Saveable.loadObject(scan, 1), "Checking int value loaded");
		assertEquals(-2, Saveable.loadObject(scan, 1), "Checking int value loaded");
		
		assertEquals(3L, Saveable.loadObject(scan, 2), "Checking long value loaded");
		assertEquals(387246587654L, Saveable.loadObject(scan, 2), "Checking long value loaded");
		
		assertEquals(3.5345F, Saveable.loadObject(scan, 3), "Checking float value loaded");
		assertEquals(-7.2345F, Saveable.loadObject(scan, 3), "Checking float value loaded");
		
		assertEquals(2.1436543764765476, Saveable.loadObject(scan, 4), "Checking double value loaded");
		assertEquals(0.0, Saveable.loadObject(scan, 4), "Checking double value loaded");
		
		scan.nextLine();
		assertEquals("word", Saveable.loadObject(scan, 5), "Checking string value loaded");
		assertEquals("space word", Saveable.loadObject(scan, 5), "Checking string value with space loaded");
		
		assertEquals("big", Saveable.loadObject(scan, 6), "Checking next element value loaded");
		assertEquals("b-/102", Saveable.loadObject(scan, 6), "Checking next element loaded after a space");
		assertEquals("h", Saveable.loadObject(scan, 6), "Checking next element loaded after a new line");
		scan.nextLine();
		
		assertEquals(null, Saveable.loadObject(null, 0), "Checking null returned with null reader");
		
		assertEquals(null, Saveable.loadObject(scan, -1), "Checking null returned with invalid type");
		
		assertEquals(7, Saveable.loadObject(scan, 1), "Checking value loaded after a successful load");
		
		assertEquals(null, Saveable.loadObject(scan, 1), "Checking null returned with nothing left to load");
	}
	
	@Test
	public void loadObjects(){
		scan = new Scanner("test\na big\nfinal line");
		Object[] load = Saveable.loadObjects(scan, 3, 5);
		assertNotEquals(null, load, "Checking returned value is not null");
		assertEquals("test", load[0], "Checking value loaded from list");
		assertEquals("a big", load[1], "Checking value loaded from list");
		assertEquals("final line", load[2], "Checking value loaded from list");
		
		scan.close();
		scan = new Scanner("test");
		load = Saveable.loadObjects(scan, 2, 3);
		assertEquals(null, load, "Checking null returned with size too large for values to load");
	}
	
	@Test
	public void loadBool(){
		scan = new Scanner("false true 11");
		assertEquals(false, Saveable.loadBool(scan), "Checking boolean value loaded");
		assertEquals(true, Saveable.loadBool(scan), "Checking boolean value loaded");
		assertEquals(null, Saveable.loadBool(scan), "Checking null returned on invalid boolean value");
		assertEquals(null, Saveable.loadBool(scan), "Checking null returned with no space left");
	}
	
	@Test
	public void loadBools(){
		scan = new Scanner("false true");
		Object[] load = Saveable.loadBools(scan, 2);
		assertNotEquals(null, load, "Checking returned value not null");
		assertEquals(false, load[0], "Checking boolean value loaded");
		assertEquals(true, load[1], "Checking boolean value loaded");
		
		scan.close();
		scan = new Scanner("false");
		load = Saveable.loadBools(scan, 2);
		assertEquals(null, load, "Checking null returned with size too big");
		
		scan.close();
		scan = new Scanner("false s");
		load = Saveable.loadBools(scan, 2);
		assertEquals(null, load, "Checking null returned with invalid boolean value");
	}
	
	@Test
	public void loadInt(){
		scan = new Scanner("1 -3 1.1 a");
		assertEquals(1, Saveable.loadInt(scan), "Checking integer value loaded");
		assertEquals(-3, Saveable.loadInt(scan), "Checking integer value loaded");
		assertEquals(null, Saveable.loadInt(scan), "Checking null returned on invalid integer value");
		assertEquals(null, Saveable.loadInt(scan), "Checking null returned non number value");
		assertEquals(null, Saveable.loadInt(scan), "Checking null returned with no space left");
	}
	
	@Test
	public void loadInts(){
		scan = new Scanner("1 2");
		Object[] load = Saveable.loadInts(scan, 2);
		assertNotEquals(null, load, "Checking returned value not null");
		assertEquals(1, load[0], "Checking integer value loaded");
		assertEquals(2, load[1], "Checking integer value loaded");
		
		scan.close();
		scan = new Scanner("3");
		load = Saveable.loadInts(scan, 2);
		assertEquals(null, load, "Checking null returned with size too big");
		
		scan.close();
		scan = new Scanner("1 s");
		load = Saveable.loadInts(scan, 2);
		assertEquals(null, load, "Checking null returned with invalid integer value");
	}
	
	@Test
	public void loadLong(){
		scan = new Scanner("1 -3 1.1 a");
		assertEquals(1L, Saveable.loadLong(scan), "Checking long value loaded");
		assertEquals(-3L, Saveable.loadLong(scan), "Checking long value loaded");
		assertEquals(null, Saveable.loadLong(scan), "Checking null returned on invalid long value");
		assertEquals(null, Saveable.loadLong(scan), "Checking null returned non number value");
		assertEquals(null, Saveable.loadLong(scan), "Checking null returned with no space left");
	}
	
	@Test
	public void loadLongs(){
		scan = new Scanner("1 2");
		Object[] load = Saveable.loadLongs(scan, 2);
		assertNotEquals(null, load, "Checking returned value not null");
		assertEquals(1L, load[0], "Checking integer value loaded");
		assertEquals(2L, load[1], "Checking integer value loaded");
		
		scan.close();
		scan = new Scanner("3");
		load = Saveable.loadLongs(scan, 2);
		assertEquals(null, load, "Checking null returned with size too big");
		
		scan.close();
		scan = new Scanner("1 s");
		load = Saveable.loadLongs(scan, 2);
		assertEquals(null, load, "Checking null returned with invalid integer value");
	}
	
	@Test
	public void loadFloat(){
		scan = new Scanner("1.1 -3.43 a");
		assertEquals(1.1F, Saveable.loadFloat(scan), "Checking float value loaded");
		assertEquals(-3.43F, Saveable.loadFloat(scan), "Checking float value loaded");
		assertEquals(null, Saveable.loadFloat(scan), "Checking null returned non number value");
		assertEquals(null, Saveable.loadFloat(scan), "Checking null returned with no space left");
	}
	
	@Test
	public void loadFloats(){
		scan = new Scanner("1.6 2.2");
		Object[] load = Saveable.loadFloats(scan, 2);
		assertNotEquals(null, load, "Checking returned value not null");
		assertEquals(1.6F, load[0], "Checking float value loaded");
		assertEquals(2.2F, load[1], "Checking float value loaded");
		
		scan.close();
		scan = new Scanner("-2.5");
		load = Saveable.loadFloats(scan, 2);
		assertEquals(null, load, "Checking null returned with size too big");
		
		scan.close();
		scan = new Scanner("1.7 s");
		load = Saveable.loadFloats(scan, 2);
		assertEquals(null, load, "Checking null returned with invalid float value");
	}
	
	@Test
	public void loadDouble(){
		scan = new Scanner("1.0 -3.2 a");
		assertEquals(1.0, Saveable.loadDouble(scan), "Checking double value loaded");
		assertEquals(-3.2, Saveable.loadDouble(scan), "Checking double value loaded");
		assertEquals(null, Saveable.loadDouble(scan), "Checking null returned on invalid double value");
		assertEquals(null, Saveable.loadDouble(scan), "Checking null returned with no space left");
	}
	
	@Test
	public void loadDoubles(){
		scan = new Scanner("1.2 2.4");
		Object[] load = Saveable.loadDoubles(scan, 2);
		assertNotEquals(null, load, "Checking returned value not null");
		assertEquals(1.2, load[0], "Checking double value loaded");
		assertEquals(2.4, load[1], "Checking double value loaded");
		
		scan.close();
		scan = new Scanner("3.3");
		load = Saveable.loadDoubles(scan, 2);
		assertEquals(null, load, "Checking null returned with size too big");
		
		scan.close();
		scan = new Scanner("1.3 s");
		load = Saveable.loadDoubles(scan, 2);
		assertEquals(null, load, "Checking null returned with invalid double value");
	}
	
	@Test
	public void loadString(){
		scan = new Scanner("a\nword 1\nword big words\n\ntest\n");
		scan.nextLine();
		assertEquals("word 1", Saveable.loadString(scan), "Checking string loaded from next line");
		assertEquals("word big words", Saveable.loadString(scan), "Checking string loaded");
		assertEquals("", Saveable.loadString(scan), "Checking loading empty string");
		assertEquals("test", Saveable.loadString(scan), "Checking loading string after empty string");
		assertEquals(null, Saveable.loadString(scan), "Checking null returned on no more space to load");
	}
	
	@Test
	public void loadStrings(){
		scan = new Scanner("a1 word\nbig boi words\n\n");
		Object[] load = Saveable.loadStrings(scan, 3);
		assertNotEquals(null, load, "Checking returned value not null");
		assertEquals("a1 word", load[0], "Checking string loaded");
		assertEquals("big boi words", load[1], "Checking string loaded");
		assertEquals("", load[2], "Checking empty string loaded");
		
		scan.close();
		scan = new Scanner("yes w");
		load = Saveable.loadStrings(scan, 2);
		assertEquals(null, load, "Checking null returned with size too big");
	}
	
	@Test
	public void load(){
		scan = new Scanner("1 2 -1");
		assertEquals(false, Saveable.load(null, saveObj), "Checking false returned on null scanner");
		assertEquals(false, Saveable.load(scan, null), "Checking false returned on null object to save");
		
		assertEquals(true, Saveable.load(scan, saveObj), "Checking true returned on successful load");
		assertEquals(false, Saveable.load(scan, saveObj), "Checking false returned on bad load");
		assertEquals(false, Saveable.load(scan, saveObj), "Checking false returned on exception");
		
		scan.close();
		scan = new Scanner("1 false\na b c");
		assertTrue(saveLine.load(scan), "Checking multiple line object loads");
		assertEquals(1, saveLine.a, "Checking values loaded correctly");
		assertEquals(false, saveLine.b, "Checking values loaded correctly");
		assertEquals("a b c", saveLine.c, "Checking values loaded correctly");
	}
	
	@Test
	public void loadMultiple(){
		scan = new Scanner("1 10");
		assertEquals(false, Saveable.loadMultiple(null, saveObjs), "Checking false returned on null scanner");
		assertEquals(false, Saveable.loadMultiple(scan, null), "Checking false returned on null list to save");
		assertEquals(false, Saveable.loadMultiple(scan, new Saveable[]{new SingleLine(), null}), "Checking false returned on list with a null value");
		
		scan.close();
		scan = new Scanner("1 10");
		assertEquals(true, Saveable.loadMultiple(scan, saveObjs), "Checking true returned with loading multiple objects"); 
		assertEquals(1, saveObjs[0].value, "Checking values loaded in order"); 
		assertEquals(10, saveObjs[1].value, "Checking values loaded in order");
		
		scan.close();
		scan = new Scanner("2 10");
		assertEquals(false, Saveable.loadMultiple(scan, saveObjs), "Checking false returned with invalid loaded value"); 
	}
	
	@Test
	public void saveToString(){
		assertFalse(Saveable.saveToString(null, 4, false), "Checking save fails with null writer");
		assertFalse(Saveable.saveToString(write, null, false), "Checking save fails with null object to save");
		assertFalse(Saveable.saveToString(write, "\n", false), "Checking save fails with a new line character string");
		
		saveHelper(4, "4 ", false, 0);
		saveHelper(4, "4\n", true, 0);
		saveHelper("test yes", "test yes ", false, 0);
		saveHelper(true, "true ", false, 0);
		saveHelper(false, "false\n", true, 0);
		saveHelper("", " ", false, 0);
		saveHelper("", "\n", true, 0);
		
		saveHelper(true, "true ", 1);
		saveHelper(5, "5 ", 1);
	}
	
	@Test
	public void saveToStrings(){
		assertFalse(Saveable.saveToStrings(null, new Object[]{}, false), "Checking save fails with null writer");
		assertFalse(Saveable.saveToStrings(write, new Object[]{null}, false), "Checking save fails with null object in list to save");
		assertFalse(Saveable.saveToStrings(write, null, false), "Checking save fails with null object to save");
		assertFalse(Saveable.saveToStrings(write, new Object[]{"\n"}, false), "Checking save fails with new line string in list to save");

		saveHelper(new Object[]{}, "", false, 2);
		saveHelper(new Object[]{2, 3}, "2 3 ", false, 2);
		saveHelper(new Object[]{"test", 3, false}, "test\n3\nfalse\n", true, 2);
		saveHelper(new Object[]{"", ""}, "\n\n", true, 2);
		
		saveHelper(new Object[]{"test", 3, false}, "test 3 false ", 3);
		saveHelper(new Object[]{1, 4, 5}, "1 4 5 ", 3);
	}
	
	@Test
	public void save(){
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		BufferedOutputStream bs = new BufferedOutputStream(bytes);
		PrintWriter write = new PrintWriter(bs);
		assertFalse(Saveable.save(null, saveObj), "Checking save fails with null writer");
		assertFalse(Saveable.save(write, null), "Checking save fails with null object to save");
		write.close();
		
		saveObj.value = 2;
		saveHelper(saveObj, "2 ", 4);
		
		saveObj.value = 3;
		saveHelper(saveObj, "3 ", 4);
		
		saveObj.value = 5;
		assertFalse(Saveable.save(write, saveObj), "Checking save returns false when the object fails to save");
		
		saveObj.value = -1;
		assertFalse(Saveable.save(write, saveObj), "Checking save returns false when an exception occurs");
	}
	
	@Test
	public void saveMultiple(){
		assertFalse(Saveable.saveMultiple(null, saveObjs), "Checking save fails with null writer");
		assertFalse(Saveable.saveMultiple(write, null), "Checking save fails with null object to save");
		assertFalse(Saveable.saveMultiple(write, new Saveable[]{null}), "Checking save fails with null object in list to save");

		saveObjs[0].value = 1;
		saveObjs[1].value = 2;
		saveHelper(saveObjs, "1 2 ", 5);
		
		saveObjs[0].value = 4;
		saveObjs[1].value = 7;
		saveHelper(saveObjs, "4 7 ", 5);
	}
	
	@Test
	public void nextLine(){
		scan = new Scanner("a");
		assertTrue(Saveable.nextLine(scan), "Checking next line was advanced");
		assertFalse(Saveable.nextLine(scan), "Checking false returned on error, nothing left to advance");
	}
	
	@Test
	public void newLine(){
		assertTrue(Saveable.newLine(write), "Checking next line was written");
		assertFalse(Saveable.newLine(null), "Checking false returned on error, writer null");
	}
	
	@AfterEach
	public void end(){
		if(scan != null) scan.close();
		if(write != null) write.close();
	}
	
	/**
	 * Helper method for testing save methods
	 * @param obj The object to save.<br>
	 * 	For method == 0 or method == 1, can be any object.<br>
	 * 	For method == 2 or method == 3, must be an array of any object.<br>
	 * 	For method == 4 or method == 5, must be a {@link Saveable} object
	 * @param expected The expected string to be saved
	 * @param newLine See {@link Saveable#saveToString(PrintWriter, Object, boolean)}
	 * 	only used for method == 0 and method == 2
	 * @param method The method to use
	 * 	<ul>
	 * 		<li>0: saveToString 3 params</li>
	 * 		<li>1: saveToString 2 params</li>
	 * 		<li>2: saveToStrings 3 params</li>
	 * 		<li>3: saveToStrings 2 params</li>
	 * 		<li>4: save</li>
	 * 		<li>5: saveMultiple</li>
	 * 	</ul>
	 */
	public static void saveHelper(Object obj, String expected, boolean newLine, int method){
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		BufferedOutputStream bs = new BufferedOutputStream(bytes);
		PrintWriter write = new PrintWriter(bs);
		
		boolean success = false;
		
		switch(method){
			case 0: success = Saveable.saveToString(write, obj, newLine); break;
			case 1: success = Saveable.saveToString(write, obj); break;
			case 2: success = Saveable.saveToStrings(write, (Object[])obj, newLine); break;
			case 3: success = Saveable.saveToStrings(write, (Object[])obj); break;
			case 4: success = Saveable.save(write, (Saveable)obj); break;
			case 5: success = Saveable.saveMultiple(write, (Saveable[])obj); break;
		}
		
		write.close();
		String saved = UtilsTest.removeSlashR(bytes.toString());
		assertTrue(success, "Checking save is successful");
		assertEquals(expected, saved, "Checking correct value saved");
	}
	
	/**
	 * Helper method for testing save methods
	 * @param obj The object to save.<br>
	 * 	For method == 1, can be any object.<br>
	 * 	For method == 3, must be an array of any object.<br>
	 * 	For method == 4 or method == 5, must be a {@link Saveable} object
	 * @param expected The expected string to be saved
	 * @param method The method to use
	 * 	<ul>
	 * 		<li>1: saveToString 2 params</li>
	 * 		<li>3: saveToStrings 2 params</li>
	 * 		<li>4: save</li>
	 * 		<li>5: saveMultiple</li>
	 * 	</ul>
	 */
	public static void saveHelper(Object obj, String expected, int method){
		saveHelper(obj, expected, false, method);
	}
	
}
