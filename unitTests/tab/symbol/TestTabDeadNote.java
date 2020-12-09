package tab.symbol;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTabDeadNote{
	
	private TabDeadNote note;
	private TabPosition pos;
	
	@BeforeEach
	public void setup(){
		pos = new TabPosition(4);
		note = new TabDeadNote(pos);
	}
	
	@Test
	public void constructor(){
		assertEquals(pos, note.getPos(), "Checking position initialized");
		assertEquals(null, note.getModifier(), "Checking modifier initialized");
	}

	@Test
	public void copy(){
		TabDeadNote copy = note.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(note));
		assertTrue("Checking copy is not the same as the source object", copy != note);
	}
	
	@Test
	public void getSymbol(){
		assertEquals("X", note.getSymbol(null), "Checking symbol is obtained");
	}
	
	@AfterEach
	public void end(){}
	
}
