package tab.symbol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import music.NotePosition;
import music.Pitch;
import music.Rhythm;

public class TestTabNoteRhythm{
	
	private TabNoteRhythm note;
	private Pitch pitch;
	private Rhythm rhythm;
	private NotePosition pos;
	private TabModifier mod;
	
	private Rhythm newRhythm;

	private TabNoteRhythm noteNoMod;
	private TabNoteRhythm noteValues;
	
	@BeforeEach
	public void setup(){
		newRhythm = new Rhythm(1, 2);
		
		pitch = new Pitch(3);
		rhythm = new Rhythm(1, 2);
		pos = new NotePosition(2);
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
		assertEquals(new TabModifier(), noteNoMod.getModifier(), "Checking modifier initialized for no modifier constructor");
		
		assertEquals(2, noteValues.getPitch().getNote(), "Checking pitch initialized for values constructor");
		assertEquals(3, noteValues.getPos().getValue(), "Checking pos initialized for values constructor");
		assertEquals(new TabModifier(), noteValues.getModifier(), "Checking modifier initialized for values constructor");
		
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new TabNoteRhythm(pitch, null, pos);
			}
		}, "Checking exception is thrown on null rhythm");
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
		
		note.setRhythm(null);
		assertEquals(newRhythm, note.getRhythm(), "Checking rhythm not changed with null parameter");
	}
	
	@Test
	public void convertToNoRhythm(){
		TabNote n = note.convertToNoRhythm();
		assertTrue("Checking pitch is equal", n.getPitch().equals(note.getPitch()));
		assertTrue("Checking pos is equal", n.getPos().equals(note.getPos()));
		assertTrue("Checking modifier is equal", n.getModifier().equals(note.getModifier()));
		
		assertFalse("Checking pitch is not the same object", n.getPitch() == note.getPitch());
		assertFalse("Checking pos is not the same object", n.getPos() == note.getPos());
		assertFalse("Checking modifier is not the same object", n.getModifier() == note.getModifier());
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
