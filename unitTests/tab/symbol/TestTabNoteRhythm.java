package tab.symbol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import music.NotePosition;
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
		assertEquals(pos, note.getPosition(), "Checking pos initialized for full constructor");
		assertEquals(mod, note.getModifier(), "Checking modifier initialized for full constructor");
		
		assertEquals(pitch, noteNoMod.getPitch(), "Checking pitch initialized for no modifier constructor");
		assertEquals(pos, noteNoMod.getPosition(), "Checking pos initialized for no modifier constructor");
		assertEquals(new TabModifier(), noteNoMod.getModifier(), "Checking modifier initialized for no modifier constructor");
		
		assertEquals(2, noteValues.getPitch().getNote(), "Checking pitch initialized for values constructor");
		assertEquals(3, noteValues.getPosition().getValue(), "Checking pos initialized for values constructor");
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
	public void removeRhythm(){
		TabNote n = note.removeRhythm();
		assertTrue("Checking pitch is equal", n.getPitch().equals(note.getPitch()));
		assertTrue("Checking pos is equal", n.getPosition().equals(note.getPosition()));
		assertTrue("Checking modifier is equal", n.getModifier().equals(note.getModifier()));
		
		assertFalse("Checking pitch is not the same object", n.getPitch() == note.getPitch());
		assertFalse("Checking pos is not the same object", n.getPosition() == note.getPosition());
		assertFalse("Checking modifier is not the same object", n.getModifier() == note.getModifier());
	}
	
	@Test
	public void convertToRhythm(){
		assertEquals(note, note.convertToRhythm(rhythm), "Checking new note is the same as the original");
	}
	
	@Test
	public void usesRhythm(){
		assertTrue("Checking note uses rhythm", note.usesRhythm());
	}
	
	@Test
	public void quantize(){
		TimeSignature sig = new TimeSignature(4, 4);
		
		note.getRhythm().setDuration(33);
		note.getRhythm().setUnit(64);
		note.quantize(sig, 4);
		TestTimeSignature.guessRhythmHelper(1, 2, note.getRhythm().getLength(), sig, false);
		
		note.getRhythm().setDuration(18);
		note.getRhythm().setUnit(32);
		note.quantize(sig, 4);
		TestTimeSignature.guessRhythmHelper(9, 16, note.getRhythm().getLength(), sig, false);
		
		note.getRhythm().setDuration(5);
		note.getRhythm().setUnit(64);
		note.quantize(sig, 4);
		TestTimeSignature.guessRhythmHelper(1, 16, note.getRhythm().getLength(), sig, false);
	}
	
	@Test
	public void getSaveObjects(){
		Saveable[] objs = note.getSaveObjects();
		assertEquals(pitch, objs[0], "Checking pitch obtained");
		assertEquals(pos, objs[1], "Checking position obtained");
		assertEquals(rhythm, objs[2], "Checking rhythm obtained");
		assertEquals(mod, objs[3], "Checking modifier obtained");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("4 \n2.3 \n3 2 \na\ns\nk");
		assertTrue("Checking load successful", note.load(scan));
		assertEquals(4, note.getPitch().getNote(), "Checking pitch correctly loaded");
		assertEquals(2.3, note.getPos(), "Checking position correctly loaded");
		assertEquals(new Rhythm(3, 2), note.getRhythm(), "Checking rhythm correctly loaded");
		assertEquals(new TabModifier("a", "s"), note.getModifier(), "Checking modifier correctly loaded");
		
		assertFalse("Checking load fails without enough data", note.load(scan));
	}
	
	@Test
	public void save(){
		assertEquals("3 \n2.0 \n1 2 \n[\n]\n", UtilsTest.testSave(note), "Checking note saved correctly");
		
		note.setPitch(4);
		note.setPos(2.3);
		note.setRhythm(new Rhythm(3, 2));
		note.setModifier(new TabModifier("a", "s"));
		assertEquals("4 \n2.3 \n3 2 \na\ns\n", UtilsTest.testSave(note), "Checking note saved correctly");
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
