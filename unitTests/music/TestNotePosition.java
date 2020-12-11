package music;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.testUtils.UtilsTest;

public class TestNotePosition{
	
	private NotePosition pos;
	
	@BeforeEach
	public void setup(){
		pos = new NotePosition(3);
	}

	@Test
	public void copy(){
		NotePosition copy = pos.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(pos));
		assertTrue("Checking copy is not the same as the source object", copy != pos);
	}
	
	@Test
	public void getValue(){
		assertEquals(3, pos.getValue(), "Checking value is initialized");
	}
	
	@Test
	public void setValue(){
		pos.setValue(2);
		assertEquals(2, pos.getValue(), "Checking value is set");
	}
	
	@Test
	public void addValue(){
		pos.setValue(2);
		pos.addValue(3);
		assertEquals(5, pos.getValue(), "Checking value is added");
	}
	
	@Test
	public void quantize(){
		pos.setValue(1.1);
		pos.quantize(new TimeSignature(4, 4), 4);
		assertEquals(1, pos.getValue(), UtilsTest.DELTA, "Checking note is quantized");
	}
	
	@Test
	public void equals(){
		NotePosition p = new NotePosition(3);
		assertFalse("Checking objects are not the same object", p == pos);
		assertTrue("Checking objects are equal", p.equals(pos));
		
		p.setValue(4);
		assertFalse("Checking objects are not equal", p.equals(pos));
	}
	
	@AfterEach
	public void end(){}
	
}
