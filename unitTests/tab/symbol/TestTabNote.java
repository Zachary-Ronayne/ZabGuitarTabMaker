package tab.symbol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.Pitch;
import music.Rhythm;
import util.Saveable;
import util.testUtils.UtilsTest;

public class TestTabNote{

	private TabNote note;
	private TabNote noteNoMod;
	private TabNote noteValues;
	
	private Pitch pitch;
	private TabModifier mod;
	
	@BeforeEach
	public void setup(){
		pitch = new Pitch(2);
		mod = new TabModifier("(", ")");
		
		note = new TabNote(pitch, mod);
		noteNoMod = new TabNote(pitch);
		noteValues = new TabNote(3);
	}
	
	@Test
	public void constructor(){
		assertEquals(pitch, note.getPitch(), "Checking pitch initialized in full constructor");
		assertEquals(mod, note.getModifier(), "Checking modifier initialized in full constructor");
		
		assertEquals(pitch, noteNoMod.getPitch(), "Checking pitch initialized in no modifier constructor");
		assertEquals(new TabModifier(), noteNoMod.getModifier(), "Checking empty modifier in no modifier constructor");
		
		assertEquals(3, noteValues.getPitch().getNote(), "Checking pitch initialized in values constructor");
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
		assertTrue("Checking modifier is equal", n.getModifier().equals(note.getModifier()));
		assertTrue("Checking rhythm is the same as the set rhythm", n.getRhythm() == r);
		
		assertFalse("Checking pitch is not the same object", n.getPitch() == note.getPitch());
		assertFalse("Checking modifier is not the same object", n.getModifier() == note.getModifier());
		

		r = new Rhythm(3, 7);
		n = note.convertToRhythm(r.getDuration(), r.getUnit());

		assertTrue("Checking pitch is equal", n.getPitch().equals(note.getPitch()));
		assertTrue("Checking modifier is equal", n.getModifier().equals(note.getModifier()));
		assertTrue("Checking rhythm is equal, expected duration " + r.getDuration() + " and unit " + r.getUnit() +
				   ", But got duration " + n.getRhythm().getDuration() + " and unit " + n.getRhythm().getUnit(),
					n.getRhythm().equals(r));

		assertFalse("Checking pitch is not the same object", n.getPitch() == note.getPitch());
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
	
	@Test
	public void getSaveObjects(){
		Saveable[] objs = note.getSaveObjects();
		assertEquals(pitch, objs[0], "Checking pitch obtained");
		assertEquals(mod, objs[1], "Checking modifier obtained");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("1 \n[\n]\n2 \no\np\n5");
		assertTrue("Checking note loaded correctly", note.load(scan));
		assertEquals(1, note.getPitch().getNote(), "Checking pitch loaded correctly");
		assertEquals("[", note.getModifier().getBefore(), "Checking before modifier loaded correctly");
		assertEquals("]", note.getModifier().getAfter(), "Checking after modifier loaded correctly");
		
		assertTrue("Checking note loaded correctly", note.load(scan));
		assertEquals(2, note.getPitch().getNote(), "Checking pitch loaded correctly");
		assertEquals("o", note.getModifier().getBefore(), "Checking before modifier loaded correctly");
		assertEquals("p", note.getModifier().getAfter(), "Checking after modifier loaded correctly");
		
		assertFalse("Checking load fails without enough data", note.load(scan));
	}

	@Test
	public void save(){
		assertEquals("2 \n(\n)\n", UtilsTest.testSave(note), "Checking note saved correctly");
		
		note.setPitch(3);
		note.setModifier(new TabModifier("w", "e"));
		assertEquals("3 \nw\ne\n", UtilsTest.testSave(note), "Checking note saved correctly");
	}
	
	@Test
	public void testToString(){
		assertEquals(""
				+ "[TabNote, "
				+ "[On C4 string, note: \"(2)\"], "
				+ "[TabModifier: \"(\" \")\"], "
				+ "[Pitch: D4]]", note.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
