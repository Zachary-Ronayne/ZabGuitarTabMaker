package tab.symbol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTabModifier{

	private TabModifier mod;
	private TabModifier empty;
	
	@BeforeEach
	public void setup(){
		mod = new TabModifier("(", ")");
		empty = new TabModifier();
	}

	@Test
	public void copy(){
		TabModifier copy = mod.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(mod));
		assertTrue("Checking copy is not the same as the source object", copy != mod);
	}
	
	@Test
	public void getBefore(){
		assertEquals("(", mod.getBefore(), "Checking before initialized");
		
		assertEquals("", empty.getBefore(), "Checking empty constructor has an empty before string");
	}
	
	@Test
	public void setBefore(){
		mod.setBefore("<");
		assertEquals("<", mod.getBefore(), "Checking before set");
	}
	
	@Test
	public void getAfter(){
		assertEquals(")", mod.getAfter(), "Checking after initialized");
		
		assertEquals("", empty.getAfter(), "Checking empty constructor has an empty after string");
	}
	
	@Test
	public void setAfter(){
		mod.setAfter(">");
		assertEquals(">", mod.getAfter(), "Checking after set");
	}
	
	@Test
	public void modifySymbol(){
		assertEquals("()", mod.modifySymbol(""), "Checking modifying an empty string");
		assertEquals("(C3)", mod.modifySymbol("C3"), "Checking modifying a note");
		assertEquals("(3)", mod.modifySymbol("3"), "Checking modifying a number");
	}
	
	@Test
	public void equals(){
		TabModifier m = new TabModifier("(", ")");
		assertFalse("Checking objects are not the same object", m == mod);
		assertTrue("Checking objects are equal", m.equals(mod));
		
		m.setBefore("a");
		assertFalse("Checking objects are not equal", m.equals(mod));

		m.setBefore("(");
		assertTrue("Checking objects are equal", m.equals(mod));
		m.setAfter("b");
		assertFalse("Checking objects are not equal", m.equals(mod));
	}
	
	@AfterEach
	public void end(){}
	
}
