package tab.symbol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.NotePosition;
import music.Rhythm;

public class TestTabDeadNote{
	
	private TabDeadNote note;
	private NotePosition pos;
	
	@BeforeEach
	public void setup(){
		pos = new NotePosition(4);
		note = new TabDeadNote(pos);
	}
	
	@Test
	public void constructor(){
		assertEquals(pos, note.getPosition(), "Checking position initialized");
		assertEquals(new TabModifier(), note.getModifier(), "Checking modifier initialized");
	}

	@Test
	public void copy(){
		TabDeadNote copy = note.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(note));
		assertTrue("Checking copy is not the same as the source object", copy != note);
	}
	
	@Test
	public void convertToRhythm(){
		assertEquals(note, note.convertToRhythm(new Rhythm(1, 1)), "Checking converted version is the same");
	}

	@Test
	public void removeRhythm(){
		assertEquals(note, note.removeRhythm(), "Checking removed version is the same");
	}
	
	@Test
	public void usesRhythm(){
		assertFalse("Checking note doesn't use rhythm", note.usesRhythm());
	}
	
	@Test
	public void getSymbol(){
		assertEquals("X", note.getSymbol(null), "Checking symbol is obtained");
	}
	
	@AfterEach
	public void end(){}
	
}
