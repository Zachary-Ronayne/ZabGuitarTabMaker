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

	private Scanner read;
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
		read = new Scanner("true false 3 -2 2.1 0\nword\nspace word\n7");
		
		assertEquals(true, Saveable.loadObject(read, 0), "Checking boolean value loaded");
		assertEquals(false, Saveable.loadObject(read, 0), "Checking boolean value loaded");
		
		assertEquals(3, Saveable.loadObject(read, 1), "Checking int value loaded");
		assertEquals(-2, Saveable.loadObject(read, 1), "Checking int value loaded");
		
		assertEquals(2.1, Saveable.loadObject(read, 2), "Checking double value loaded");
		assertEquals(0.0, Saveable.loadObject(read, 2), "Checking double value loaded");
		
		read.nextLine();
		assertEquals("word", Saveable.loadObject(read, 3), "Checking string value loaded");
		assertEquals("space word", Saveable.loadObject(read, 3), "Checking string value with space loaded");
		
		assertEquals(null, Saveable.loadObject(null, 0), "Checking null returned with null reader");
		
		assertEquals(null, Saveable.loadObject(read, -1), "Checking null returned with invalid type");
		
		assertEquals(7, Saveable.loadObject(read, 1), "Checking value loaded after a failed load");
		
		assertEquals(null, Saveable.loadObject(read, 1), "Checking null returned with nothing left to load");
	}
	
	@Test
	public void loadObjects(){
		read = new Scanner("test\na big\nfinal line");
		Object[] load = Saveable.loadObjects(read, 3, 3);
		assertNotEquals(null, load, "Checking returned value is not null");
		assertEquals("test", load[0], "Checking value loaded from list");
		assertEquals("a big", load[1], "Checking value loaded from list");
		assertEquals("final line", load[2], "Checking value loaded from list");
		
		read.close();
		read = new Scanner("test");
		load = Saveable.loadObjects(read, 2, 3);
		assertEquals(null, load, "Checking null returned with size too large for values to load");
	}
	
	@Test
	public void loadBool(){
		read = new Scanner("false true 11");
		assertEquals(false, Saveable.loadBool(read), "Checking boolean value loaded");
		assertEquals(true, Saveable.loadBool(read), "Checking boolean value loaded");
		assertEquals(null, Saveable.loadBool(read), "Checking null returned on invalid boolean value");
		assertEquals(null, Saveable.loadBool(read), "Checking null returned with no space left");
	}
	
	@Test
	public void loadBools(){
		read = new Scanner("false true");
		Object[] load = Saveable.loadBools(read, 2);
		assertNotEquals(null, load, "Checking returned value not null");
		assertEquals(false, load[0], "Checking boolean value loaded");
		assertEquals(true, load[1], "Checking boolean value loaded");
		
		read.close();
		read = new Scanner("false");
		load = Saveable.loadBools(read, 2);
		assertEquals(null, load, "Checking null returned with size too big");
		
		read.close();
		read = new Scanner("false s");
		load = Saveable.loadBools(read, 2);
		assertEquals(null, load, "Checking null returned with invalid boolean value");
	}
	
	@Test
	public void loadInt(){
		read = new Scanner("1 -3 1.1 a");
		assertEquals(1, Saveable.loadInt(read), "Checking integer value loaded");
		assertEquals(-3, Saveable.loadInt(read), "Checking integer value loaded");
		assertEquals(null, Saveable.loadInt(read), "Checking null returned on invalid integer value");
		assertEquals(null, Saveable.loadInt(read), "Checking null returned non number value");
		assertEquals(null, Saveable.loadInt(read), "Checking null returned with no space left");
	}
	
	@Test
	public void loadInts(){
		read = new Scanner("1 2");
		Object[] load = Saveable.loadInts(read, 2);
		assertNotEquals(null, load, "Checking returned value not null");
		assertEquals(1, load[0], "Checking integer value loaded");
		assertEquals(2, load[1], "Checking integer value loaded");
		
		read.close();
		read = new Scanner("3");
		load = Saveable.loadInts(read, 2);
		assertEquals(null, load, "Checking null returned with size too big");
		
		read.close();
		read = new Scanner("1 s");
		load = Saveable.loadInts(read, 2);
		assertEquals(null, load, "Checking null returned with invalid integer value");
	}
	
	@Test
	public void loadDouble(){
		read = new Scanner("1.0 -3.2 a");
		assertEquals(1.0, Saveable.loadDouble(read), "Checking double value loaded");
		assertEquals(-3.2, Saveable.loadDouble(read), "Checking double value loaded");
		assertEquals(null, Saveable.loadDouble(read), "Checking null returned on invalid double value");
		assertEquals(null, Saveable.loadDouble(read), "Checking null returned with no space left");
	}
	
	@Test
	public void loadDoubles(){
		read = new Scanner("1.2 2.4");
		Object[] load = Saveable.loadDoubles(read, 2);
		assertNotEquals(null, load, "Checking returned value not null");
		assertEquals(1.2, load[0], "Checking double value loaded");
		assertEquals(2.4, load[1], "Checking double value loaded");
		
		read.close();
		read = new Scanner("3.3");
		load = Saveable.loadDoubles(read, 2);
		assertEquals(null, load, "Checking null returned with size too big");
		
		read.close();
		read = new Scanner("1.3 s");
		load = Saveable.loadDoubles(read, 2);
		assertEquals(null, load, "Checking null returned with invalid double value");
	}
	
	@Test
	public void loadString(){
		read = new Scanner("a\nword 1\nword big words\n\ntest\n");
		read.nextLine();
		assertEquals("word 1", Saveable.loadString(read), "Checking string loaded from next line");
		assertEquals("word big words", Saveable.loadString(read), "Checking string loaded");
		assertEquals("", Saveable.loadString(read), "Checking loading empty string");
		assertEquals("test", Saveable.loadString(read), "Checking loading string after empty string");
		assertEquals(null, Saveable.loadString(read), "Checking null returned on no more space to load");
	}
	
	@Test
	public void loadStrings(){
		read = new Scanner("a1 word\nbig boi words\n\n");
		Object[] load = Saveable.loadStrings(read, 3);
		assertNotEquals(null, load, "Checking returned value not null");
		assertEquals("a1 word", load[0], "Checking string loaded");
		assertEquals("big boi words", load[1], "Checking string loaded");
		assertEquals("", load[2], "Checking empty string loaded");
		
		read.close();
		read = new Scanner("yes w");
		load = Saveable.loadStrings(read, 2);
		assertEquals(null, load, "Checking null returned with size too big");
	}
	
	@Test
	public void load(){
		read = new Scanner("1 2 -1");
		assertEquals(false, Saveable.load(null, saveObj), "Checking false returned on null scanner");
		assertEquals(false, Saveable.load(read, null), "Checking false returned on null object to save");
		
		assertEquals(true, Saveable.load(read, saveObj), "Checking true returned on successful load");
		assertEquals(false, Saveable.load(read, saveObj), "Checking false returned on bad load");
		assertEquals(false, Saveable.load(read, saveObj), "Checking false returned on exception");
		
		read.close();
		read = new Scanner("1 false\na b c");
		assertTrue(saveLine.load(read), "Checking multiple line object loads");
		assertEquals(1, saveLine.a, "Checking values loaded correctly");
		assertEquals(false, saveLine.b, "Checking values loaded correctly");
		assertEquals("a b c", saveLine.c, "Checking values loaded correctly");
	}
	
	@Test
	public void loadMultiple(){
		read = new Scanner("1 10");
		assertEquals(false, Saveable.loadMultiple(null, saveObjs), "Checking false returned on null scanner");
		assertEquals(false, Saveable.loadMultiple(read, null), "Checking false returned on null list to save");
		assertEquals(false, Saveable.loadMultiple(read, new Saveable[]{new SingleLine(), null}), "Checking false returned on list with a null value");
		
		read.close();
		read = new Scanner("1 10");
		assertEquals(true, Saveable.loadMultiple(read, saveObjs), "Checking true returned with loading multiple objects"); 
		assertEquals(1, saveObjs[0].value, "Checking values loaded in order"); 
		assertEquals(10, saveObjs[1].value, "Checking values loaded in order");
		
		read.close();
		read = new Scanner("2 10");
		assertEquals(false, Saveable.loadMultiple(read, saveObjs), "Checking false returned with invalid loaded value"); 
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
		read = new Scanner("a");
		assertTrue(Saveable.nextLine(read), "Checking next line was advanced");
		assertFalse(Saveable.nextLine(read), "Checking false returned on error, nothing left to advance");
	}
	
	@Test
	public void newLine(){
		assertTrue(Saveable.newLine(write), "Checking next line was written");
		assertFalse(Saveable.newLine(null), "Checking false returned on error, writer null");
	}
	
	@AfterEach
	public void end(){
		if(read != null) read.close();
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
	public void saveHelper(Object obj, String expected, boolean newLine, int method){
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
	public void saveHelper(Object obj, String expected, int method){
		saveHelper(obj, expected, false, method);
	}
	
}
