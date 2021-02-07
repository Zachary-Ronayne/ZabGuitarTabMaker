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

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
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
		assertTrue(copy.equals(note), "Checking copy is equal to the source object");
		assertTrue(copy != note, "Checking copy is not the same as the source object");
	}
	
	@Test
	public void convertToRhythm(){
		Rhythm r = new Rhythm(3, 8);
		TabNoteRhythm n = note.convertToRhythm(r);
		
		assertTrue(n.getPitch().equals(note.getPitch()), "Checking pitch is equal");
		assertTrue(n.getModifier().equals(note.getModifier()), "Checking modifier is equal");
		assertTrue(n.getRhythm() == r, "Checking rhythm is the same as the set rhythm");
		
		assertFalse(n.getPitch() == note.getPitch(), "Checking pitch is not the same object");
		assertFalse(n.getModifier() == note.getModifier(), "Checking modifier is not the same object");
		

		r = new Rhythm(3, 7);
		n = note.convertToRhythm(r.getDuration(), r.getUnit());

		assertTrue(n.getPitch().equals(note.getPitch()), "Checking pitch is equal");
		assertTrue(n.getModifier().equals(note.getModifier()), "Checking modifier is equal");
		assertTrue(n.getRhythm().equals(r), "Checking rhythm is equal");

		assertFalse(n.getPitch() == note.getPitch(), "Checking pitch is not the same object");
		assertFalse(n.getModifier() == note.getModifier(), "Checking modifier is not the same object");
		assertFalse(n.getRhythm() == r, "Checking rhythm is not the same as the set rhythm");
	}
	
	@Test
	public void removeRhythm(){
		assertEquals(note, note.removeRhythm(), "Checking removed version is the same");
	}
	
	@Test
	public void usesRhythm(){
		assertFalse(note.usesRhythm(), "Checking note doesn't use rhythm");
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
		assertTrue(note.load(scan), "Checking note loaded correctly");
		assertEquals(1, note.getPitch().getNote(), "Checking pitch loaded correctly");
		assertEquals("[", note.getModifier().getBefore(), "Checking before modifier loaded correctly");
		assertEquals("]", note.getModifier().getAfter(), "Checking after modifier loaded correctly");
		
		assertTrue(note.load(scan), "Checking note loaded correctly");
		assertEquals(2, note.getPitch().getNote(), "Checking pitch loaded correctly");
		assertEquals("o", note.getModifier().getBefore(), "Checking before modifier loaded correctly");
		assertEquals("p", note.getModifier().getAfter(), "Checking after modifier loaded correctly");
		
		assertFalse(note.load(scan), "Checking load fails without enough data");
	}

	@Test
	public void save(){
		assertEquals("2 \n(\n)\n", UtilsTest.testSave(note), "Checking note saved correctly");
		
		note = new TabNote(new Pitch(3), new TabModifier("w", "e"));
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
