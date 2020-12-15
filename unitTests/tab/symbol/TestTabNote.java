package tab.symbol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.NotePosition;
import music.Pitch;
import music.Rhythm;

public class TestTabNote{

	private TabNote note;
	private TabNote noteNoMod;
	private TabNote noteValues;
	
	private Pitch pitch;
	private NotePosition pos;
	private TabModifier mod;
	
	@BeforeEach
	public void setup(){
		pitch = new Pitch(2);
		pos = new NotePosition(1);
		mod = new TabModifier("(", ")");
		
		note = new TabNote(pitch, pos, mod);
		noteNoMod = new TabNote(pitch, pos);
		noteValues = new TabNote(3, 2);
	}
	
	@Test
	public void constructor(){
		assertEquals(pitch, note.getPitch(), "Checking pitch initialized in full constructor");
		assertEquals(pos, note.getPosition(), "Checking position initialized in full constructor");
		assertEquals(mod, note.getModifier(), "Checking modifier initialized in full constructor");
		
		assertEquals(pitch, noteNoMod.getPitch(), "Checking pitch initialized in no modifier constructor");
		assertEquals(pos, noteNoMod.getPosition(), "Checking position initialized in no modifier constructor");
		assertEquals(new TabModifier(), noteNoMod.getModifier(), "Checking empty modifier in no modifier constructor");
		
		assertEquals(3, noteValues.getPitch().getNote(), "Checking pitch initialized in values constructor");
		assertEquals(2, noteValues.getPosition().getValue(), "Checking position initialized in values constructor");
		assertEquals(new TabModifier(), noteValues.getModifier(), "Checking empty modifier in values constructor");
	}
	
	@Test
	public void copy(){
		TabNote copy = note.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(note));
		assertTrue("Checking copy is not the same as the source object", copy != note);
	}
	
	@Test
	public void convertToRhythm(){
		Rhythm r = new Rhythm(3, 8);
		TabNoteRhythm n = note.convertToRhythm(r);
		
		assertTrue("Checking pitch is equal", n.getPitch().equals(note.getPitch()));
		assertTrue("Checking pos is equal", n.getPosition().equals(note.getPosition()));
		assertTrue("Checking modifier is equal", n.getModifier().equals(note.getModifier()));
		assertTrue("Checking rhythm is the same as the set rhythm", n.getRhythm() == r);
		
		assertFalse("Checking pitch is not the same object", n.getPitch() == note.getPitch());
		assertFalse("Checking pos is not the same object", n.getPosition() == note.getPosition());
		assertFalse("Checking modifier is not the same object", n.getModifier() == note.getModifier());
		

		r = new Rhythm(3, 7);
		n = note.convertToRhythm(r.getDuration(), r.getUnit());

		assertTrue("Checking pitch is equal", n.getPitch().equals(note.getPitch()));
		assertTrue("Checking pos is equal", n.getPosition().equals(note.getPosition()));
		assertTrue("Checking modifier is equal", n.getModifier().equals(note.getModifier()));
		assertTrue("Checking rhythm is equal, expected duration " + r.getDuration() + " and unit " + r.getUnit() +
				   ", But got duration " + n.getRhythm().getDuration() + " and unit " + n.getRhythm().getUnit(),
					n.getRhythm().equals(r));

		assertFalse("Checking pitch is not the same object", n.getPitch() == note.getPitch());
		assertFalse("Checking pos is not the same object", n.getPosition() == note.getPosition());
		assertFalse("Checking modifier is not the same object", n.getModifier() == note.getModifier());
		assertFalse("Checking rhythm is not the same as the set rhythm", n.getRhythm() == r);
	}
	
	@Test
	public void removeRhythm(){
		assertEquals(note, note.removeRhythm(), "Checking removed version is the same");
	}
	
	@Test
	public void usesRhythm(){
		assertFalse("Checking note doesn't use rhythm", note.usesRhythm());
	}
	
	@AfterEach
	public void end(){}
	
}
