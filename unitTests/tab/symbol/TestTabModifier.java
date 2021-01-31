package tab.symbol;

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

public class TestTabModifier{

	private TabModifier mod;
	private TabModifier empty;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		mod = new TabModifier("(", ")");
		empty = new TabModifier();
	}

	@Test
	public void copy(){
		TabModifier copy = mod.copy();
		assertTrue(copy.equals(mod), "Checking copy is equal to the source object");
		assertTrue(copy != mod, "Checking copy is not the same as the source object");
	}
	
	@Test
	public void getBefore(){
		assertEquals("(", mod.getBefore(), "Checking before initialized");
		
		assertEquals("", empty.getBefore(), "Checking empty constructor has an empty before string");
	}
	
	@Test
	public void getAfter(){
		assertEquals(")", mod.getAfter(), "Checking after initialized");
		
		assertEquals("", empty.getAfter(), "Checking empty constructor has an empty after string");
	}
	
	@Test
	public void added(){
		assertEquals(new TabModifier("a", "b"), empty.added(new TabModifier("a", "b")), "Checking adding both modifiers");

		mod = new TabModifier("", "d");
		assertEquals(new TabModifier("a", "d"), mod.added(new TabModifier("a", "b")), "Checking adding only before modifier");
		
		mod = new TabModifier("c", "");
		assertEquals(new TabModifier("c", "b"), mod.added(new TabModifier("a", "b")), "Checking adding only after modifier");
		
		mod = new TabModifier("c", "d");
		assertEquals(new TabModifier("c", "d"), mod.added(new TabModifier("a", "b")), "Checking adding no modifiers");
	}
	
	@Test
	public void modifySymbol(){
		assertEquals("()", mod.modifySymbol(""), "Checking modifying an empty string");
		assertEquals("(C3)", mod.modifySymbol("C3"), "Checking modifying a note");
		assertEquals("(3)", mod.modifySymbol("3"), "Checking modifying a number");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("z\nq\n] [\n} {\n\n\n");
		assertTrue(mod.load(scan), "Checking load successful");
		assertEquals("z", mod.getBefore(), "Checking before value loaded");
		assertEquals("q", mod.getAfter(), "Checking after value loaded");
		
		assertTrue(mod.load(scan), "Checking load successful");
		assertEquals("] [", mod.getBefore(), "Checking before value loaded");
		assertEquals("} {", mod.getAfter(), "Checking after value loaded");
		
		assertTrue(mod.load(scan), "Checking load successful with empty string");
		assertEquals("", mod.getBefore(), "Checking before value loaded");
		assertEquals("", mod.getAfter(), "Checking after value loaded");
		
		assertFalse(mod.load(scan), "Checking load fails with nothing left to load");
	}
	
	@Test
	public void save(){
		assertEquals("(\n)\n", UtilsTest.testSave(mod), "Checking correct value saved");
		
		mod = new TabModifier("d", "f");
		assertEquals("d\nf\n", UtilsTest.testSave(mod), "Checking correct value saved");

		mod = new TabModifier();
		assertEquals("\n\n", UtilsTest.testSave(mod), "Checking correct value saved for the case of an empty string");
	}
	
	@Test
	public void equals(){
		assertTrue(mod.equals(mod), "Checking modifier equals itself");
		assertFalse(mod.equals(null), "Checking modifier does not equal null"); 
		
		TabModifier m = new TabModifier("(", ")");
		assertFalse(m == mod, "Checking objects are not the same object");
		assertTrue(m.equals(mod), "Checking objects are equal");
		
		m = new TabModifier("a", ")");
		assertFalse(m.equals(mod), "Checking objects are not equal");

		m = new TabModifier("(", ")");
		assertTrue(m.equals(mod), "Checking objects are equal");
		m = new TabModifier("(", "b");
		assertFalse(m.equals(mod), "Checking objects are not equal");
	}
	
	@Test
	public void testToString(){
		assertEquals("[TabModifier: \"(\" \")\"]", mod.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
