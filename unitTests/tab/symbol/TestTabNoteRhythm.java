package tab.symbol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.Pitch;
import music.Rhythm;

public class TestTabNoteRhythm{
	
	private TabNoteRhythm note;
	private Pitch pitch;
	private Rhythm rhythm;
	private TabPosition pos;
	private TabModifier mod;
	
	private Rhythm newRhythm;

	private TabNoteRhythm noteNoMod;
	private TabNoteRhythm noteValues;
	
	@BeforeEach
	public void setup(){
		newRhythm = new Rhythm(1, 2);
		
		pitch = new Pitch(3);
		rhythm = new Rhythm(1, 2);
		pos = new TabPosition(2);
		mod = new TabModifier("[", "]");
		note = new TabNoteRhythm(pitch, rhythm, pos, mod);
		
		noteNoMod = new TabNoteRhythm(pitch, rhythm, pos);
		noteValues = new TabNoteRhythm(2, rhythm, 3);
	}
	
	@Test
	public void constructor(){
		assertEquals(pitch, note.getPitch(), "Checking pitch initialized for full constructor");
		assertEquals(pos, note.getPos(), "Checking pos initialized for full constructor");
		assertEquals(mod, note.getModifier(), "Checking modifier initialized for full constructor");
		
		assertEquals(pitch, noteNoMod.getPitch(), "Checking pitch initialized for no modifier constructor");
		assertEquals(pos, noteNoMod.getPos(), "Checking pos initialized for no modifier constructor");
		assertEquals(null, noteNoMod.getModifier(), "Checking modifier null for no modifier constructor");
		
		assertEquals(2, noteValues.getPitch().getNote(), "Checking pitch initialized for values constructor");
		assertEquals(3, noteValues.getPos().getValue(), "Checking pos initialized for values constructor");
		assertEquals(null, noteValues.getModifier(), "Checking modifier null for values constructor");
	}

	@Test
	public void copy(){
		TabNoteRhythm copy = note.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(note));
		assertTrue("Checking copy is not the same as the source object", copy != note);
	}
	
	@Test
	public void getRhythm(){
		assertEquals(rhythm, note.getRhythm(), "Checking rhythm initialized");
	}
	
	@Test
	public void setRhythm(){
		note.setRhythm(newRhythm);
		assertEquals(newRhythm, note.getRhythm(), "Checking rhythm set");
	}
	@Test
	public void equals(){
		TabNoteRhythm n = new TabNoteRhythm(pitch, rhythm, pos, mod);
		assertFalse("Checking objects are not the same object", n == note);
		assertTrue("Checking objects are equal", n.equals(note));
		
		n.setRhythm(new Rhythm(20, 10));
		assertFalse("Checking objects are not equal", n.equals(note));
	}
	
	@AfterEach
	public void end(){}
	
}
