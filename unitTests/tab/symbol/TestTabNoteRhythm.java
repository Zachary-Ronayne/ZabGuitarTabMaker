package tab.symbol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import appUtils.ZabAppSettings;
import music.Pitch;
import music.Rhythm;
import music.TestTimeSignature;
import music.TimeSignature;
import util.Saveable;
import util.testUtils.UtilsTest;

public class TestTabNoteRhythm{
	
	private TabNoteRhythm note;
	private Pitch pitch;
	private Rhythm rhythm;
	private TabModifier mod;
	
	private Rhythm newRhythm;

	private TabNoteRhythm noteNoMod;
	private TabNoteRhythm noteValues;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		newRhythm = new Rhythm(1, 2);
		
		pitch = new Pitch(3);
		rhythm = new Rhythm(1, 2);
		mod = new TabModifier("[", "]");
		note = new TabNoteRhythm(pitch, rhythm, mod);
		
		noteNoMod = new TabNoteRhythm(pitch, rhythm);
		noteValues = new TabNoteRhythm(2, rhythm);
	}
	
	@Test
	public void constructor(){
		assertEquals(pitch, note.getPitch(), "Checking pitch initialized for full constructor");
		assertEquals(mod, note.getModifier(), "Checking modifier initialized for full constructor");
		
		assertEquals(pitch, noteNoMod.getPitch(), "Checking pitch initialized for no modifier constructor");
		assertEquals(new TabModifier(), noteNoMod.getModifier(), "Checking modifier initialized for no modifier constructor");
		
		assertEquals(2, noteValues.getPitch().getNote(), "Checking pitch initialized for values constructor");
		assertEquals(new TabModifier(), noteValues.getModifier(), "Checking modifier initialized for values constructor");
		
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new TabNoteRhythm(pitch, null);
			}
		}, "Checking exception is thrown on null rhythm");
	}

	@Test
	public void copy(){
		TabNoteRhythm copy = note.copy();
		assertTrue(copy.equals(note), "Checking copy is equal to the source object");
		assertTrue(copy != note, "Checking copy is not the same as the source object");
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
	public void removeRhythm(){
		TabNote n = note.removeRhythm();
		assertTrue(n.getPitch().equals(note.getPitch()), "Checking pitch is equal");
		assertTrue(n.getModifier().equals(note.getModifier()), "Checking modifier is equal");
		
		assertFalse(n.getPitch() == note.getPitch(), "Checking pitch is not the same object");
		assertFalse(n.getModifier() == note.getModifier(), "Checking modifier is not the same object");
	}
	
	@Test
	public void convertToRhythm(){
		assertEquals(note, note.convertToRhythm(rhythm), "Checking new note is the same as the original");
	}
	
	@Test
	public void usesRhythm(){
		assertTrue(note.usesRhythm(), "Checking note uses rhythm");
	}
	
	@Test
	public void quantize(){
		TimeSignature sig = new TimeSignature(4, 4);
		
		note.setRhythm(new Rhythm(33, 64));
		note.quantize(sig, 4);
		TestTimeSignature.guessRhythmHelper(1, 2, note.getRhythm().getLength(), sig, false);

		note.setRhythm(new Rhythm(18, 32));
		note.quantize(sig, 4);
		TestTimeSignature.guessRhythmHelper(9, 16, note.getRhythm().getLength(), sig, false);

		note.setRhythm(new Rhythm(5, 64));
		note.quantize(sig, 4);
		TestTimeSignature.guessRhythmHelper(1, 16, note.getRhythm().getLength(), sig, false);
	}
	
	@Test
	public void getSaveObjects(){
		Saveable[] objs = note.getSaveObjects();
		assertEquals(pitch, objs[0], "Checking pitch obtained");
		assertEquals(rhythm, objs[1], "Checking rhythm obtained");
		assertEquals(mod, objs[2], "Checking modifier obtained");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("4 \n3 2 \na\ns\nk");
		assertTrue(note.load(scan), "Checking load successful");
		assertEquals(4, note.getPitch().getNote(), "Checking pitch correctly loaded");
		assertEquals(new Rhythm(3, 2), note.getRhythm(), "Checking rhythm correctly loaded");
		assertEquals(new TabModifier("a", "s"), note.getModifier(), "Checking modifier correctly loaded");
		
		assertFalse(note.load(scan), "Checking load fails without enough data");
	}
	
	@Test
	public void save(){
		assertEquals("3 \n1 2 \n[\n]\n", UtilsTest.testSave(note), "Checking note saved correctly");
		
		note.setPitch(4);
		note.setRhythm(new Rhythm(3, 2));
		note.setModifier(new TabModifier("a", "s"));
		assertEquals("4 \n3 2 \na\ns\n", UtilsTest.testSave(note), "Checking note saved correctly");
	}
	
	@Test
	public void equals(){
		assertTrue(note.equals(note), "Checking note equals itself");
		assertFalse(note.equals(null), "Checking note nodes not equal null");
		
		TabNoteRhythm n = new TabNoteRhythm(pitch, rhythm, mod);
		assertFalse(n == note, "Checking objects are not the same object");
		assertTrue(n.equals(note), "Checking objects are equal");
		
		n.setRhythm(new Rhythm(20, 10));
		assertFalse(n.equals(note), "Checking objects are not equal");
	}
	
	@Test
	public void testToString(){
		assertEquals(""
				+ "[TabNoteRhyhtm, "
					+ "[On C4 string, note: \"[3]\"], "
					+ "[TabModifier: \"[\" \"]\"], [Pitch: D#4], "
					+ "[Rhythm: 1 2 notes"
				+ "]", note.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
