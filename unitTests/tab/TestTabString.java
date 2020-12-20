package tab;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.Music;
import music.NotePosition;
import music.Pitch;
import music.Rhythm;
import music.TimeSignature;
import tab.symbol.TabDeadNote;
import tab.symbol.TabModifier;
import tab.symbol.TabNote;
import tab.symbol.TabNoteRhythm;
import tab.symbol.TabSymbol;
import util.testUtils.UtilsTest;

public class TestTabString{
	
	private TabString string;
	private Pitch pitch;
	private Pitch newPitch;
	private Pitch negPitch;
	
	private TabNote[] notes;
	
	private TabString noteAndOctave;
	
	private TimeSignature sig;
	
	@BeforeEach
	public void setup(){
		newPitch = new Pitch(0);
		pitch = new Pitch(4);
		string = new TabString(pitch);
		
		notes = new TabNote[]{
				new TabNote(4, 0),
				new TabNote(7, 1),
				new TabNote(9, 2),
				new TabNote(4, 3),
				new TabNote(7, 4),
				new TabNote(10, 5),
				new TabNote(9, 6),
		};
		negPitch = new Pitch(-3);
		
		noteAndOctave = new TabString(Music.B, 3);
		
		sig = new TimeSignature(4, 4);
	}

	@Test
	public void copy(){
		TabString copy = string.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(string));
		assertTrue("Checking copy is not the same as the source object", copy != string);
	}
	
	@Test
	public void getRootPitch(){
		assertEquals(pitch, string.getRootPitch(), "Checking root pitch initialized");
		
		
		assertTrue("Checking note and octave constructor generates correct root note",
					noteAndOctave.getRootPitch().equals(new Pitch(-1)));
	}
	
	@Test
	public void setRootPitch(){
		string.setRootPitch(newPitch);
		assertEquals(newPitch, string.getRootPitch(), "Checking root pitch initialized");
	}
	
	@Test
	public void getRootNote(){
		assertEquals(4, string.getRootNote(), "Checking correct root note is found");
		assertEquals(-1, noteAndOctave.getRootNote(), "Checking correct root note is found");
	}
	
	@Test
	public void add(){
		// Adding notes in arbitrary order
		string.add(notes[4]);
		string.add(notes[6]);
		string.add(notes[2]);
		string.add(notes[0]);
		string.add(notes[5]);
		string.add(notes[1]);
		string.add(notes[3]);
		
		assertEquals(notes[0], string.get(0), "Checking notes are added in sorted order");
		assertEquals(notes[1], string.get(1), "Checking notes are added in sorted order");
		assertEquals(notes[2], string.get(2), "Checking notes are added in sorted order");
		assertEquals(notes[3], string.get(3), "Checking notes are added in sorted order");
		assertEquals(notes[4], string.get(4), "Checking notes are added in sorted order");
		assertEquals(notes[5], string.get(5), "Checking notes are added in sorted order");
		assertEquals(notes[6], string.get(6), "Checking notes are added in sorted order");
	}
	
	@Test
	public void placeQuantizedNote(){
		TabNote n;
		
		string.placeQuantizedNote(sig, 0, 1.01);
		n = (TabNote)string.get(0);
		assertEquals(4, n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(1, n.getPos(), "Checking note has quantized position");
		
		string.placeQuantizedNote(sig, 6, 1.49);
		n = (TabNote)string.get(1);
		assertEquals(10, n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(1.5, n.getPos(), "Checking note has quantized position");
	}
	
	@Test
	public void getTabNumber(){
		assertEquals(0, string.getTabNumber(notes[0]), "Checking tab number is correctly found");
		assertEquals(3, string.getTabNumber(notes[1]), "Checking tab number is correctly found");
		assertEquals(5, string.getTabNumber(notes[2]), "Checking tab number is correctly found");
		assertEquals(0, string.getTabNumber(notes[3]), "Checking tab number is correctly found");
		assertEquals(3, string.getTabNumber(notes[4]), "Checking tab number is correctly found");
		assertEquals(6, string.getTabNumber(notes[5]), "Checking tab number is correctly found");
		assertEquals(5, string.getTabNumber(notes[6]), "Checking tab number is correctly found");
		
		assertEquals(-7, string.getTabNumber(negPitch), "Checking negative tab number is correctly found");
	}

	@Test
	public void createPitch(){
		assertEquals(Music.createNote(Music.E, 4), string.createPitch(0).getNote(), "Checking open string generates correct pitch");
		assertEquals(Music.createNote(Music.F, 4), string.createPitch(1).getNote(), "Checking fret position generates correct pitch");
		assertEquals(Music.createNote(Music.D, 4), string.createPitch(-2).getNote(), "Checking negative position generates correct pitch");
		assertEquals(Music.createNote(Music.E, 5), string.createPitch(12).getNote(), "Checking high octave generates correct pitch");
		assertEquals(Music.createNote(Music.E, 3), string.createPitch(-12).getNote(), "Checking low octave generates correct pitch");
	}
	
	@Test
	public void quantize(){
		for(TabNote n : notes){
			n.getPosition().addValue(1.1);
			string.add(n);
		}
		
		string.quantize(new TimeSignature(4, 4), 1);
		
		assertEquals(1, string.get(0).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(2, string.get(1).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(3, string.get(2).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(4, string.get(3).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(5, string.get(4).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(6, string.get(5).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(7, string.get(6).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
	}
	
	@Test
	public void equals(){
		TabString s = new TabString(pitch);
		s.add(notes[0]);
		s.add(notes[1]);
		string.add(notes[0]);
		string.add(notes[1]);
		assertFalse("Checking objects are not the same object", s == string);
		assertTrue("Checking objects are equal", s.equals(string));
		
		s.setRootPitch(newPitch);
		assertFalse("Checking objects are not equal", s.equals(string));
		
		s.setRootPitch(pitch);
		assertTrue("Checking objects are equal", s.equals(string));
		s.remove(notes[0]);
		assertFalse("Checking objects are not equal", s.equals(string));
		s.add(notes[0]);
		assertTrue("Checking objects are equal", s.equals(string));
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("2 \n0\n2 \n1\nTabNote\n2 \n1.0 \n\n\n1\n");
		assertTrue("Checking load successful", string.load(scan));
		assertEquals(2, string.getRootNote(), "Checking root note set");
		assertEquals(0, string.size(), "Checking no notes exist");
		assertTrue("Checking load successful", string.load(scan));
		assertEquals(2, string.getRootNote(), "Checking root note set");
		assertEquals(1, string.size(), "Checking one note exists");
		assertEquals(new TabNote(2, 1), string.get(0), "Checking correct note loaded");
		assertFalse("Checking load failed with not enough data", string.load(scan));
		
		scan.close();
		scan = new Scanner("4 \n"
				+ "3\n"
				+ "TabNote\n"
				+ "1 \n"
				+ "2.0 \n"
				+ "\n"
				+ "\n"
				+ "TabDeadNote\n"
				+ "3.0 \n"
				+ "\n"
				+ "\n"
				+ "TabNoteRhythm\n"
				+ "2 \n"
				+ "5.4 \n"
				+ "3 4 \n"
				+ "q\n"
				+ "w\n");

		assertTrue("Checking load successful", string.load(scan));
		assertEquals(4, string.getRootNote(), "Checking root note set");
		assertEquals(3, string.size(), "Checking three notes exist");
		TabSymbol t = string.get(0);
		assertTrue("Checking note is correct type", t instanceof TabNote);
		TabNote n = (TabNote)t;
		assertEquals(1, n.getPitch().getNote(), "Checking correct pitch");
		assertEquals(2.0, n.getPos(), "Checking correct position");
		assertEquals("", n.getModifier().getBefore(), "Checking correct before modifier");
		assertEquals("", n.getModifier().getAfter(), "Checking correct after modifier");
		
		assertEquals(new TabDeadNote(new NotePosition(3)),
				string.get(1), "Checking correct note loaded");
		assertEquals(new TabNoteRhythm(new Pitch(2), new Rhythm(3, 4), new NotePosition(5.4), new TabModifier("q", "w")),
				string.get(2), "Checking correct note loaded");
	}
	
	@Test
	public void save(){
		assertEquals("4 \n0\n", UtilsTest.testSave(string), "Checking save successful, no notes");
		
		string.setRootPitch(new Pitch(2));
		string.add(new TabNote(2, 1));
		assertEquals("2 \n1\nTabNote\n2 \n1.0 \n\n\n", UtilsTest.testSave(string), "Checking save successful, one note");
		
		string.clear();
		string.setRootPitch(new Pitch(4));
		string.add(new TabNote(1, 2));
		string.add(new TabDeadNote(new NotePosition(3)));
		string.add(new TabNoteRhythm(new Pitch(2), new Rhythm(3, 4), new NotePosition(5.4), new TabModifier("q", "w")));
		assertEquals(
				"4 \n"
				+ "3\n"
				+ "TabNote\n"
				+ "1 \n"
				+ "2.0 \n"
				+ "\n"
				+ "\n"
				+ "TabDeadNote\n"
				+ "3.0 \n"
				+ "\n"
				+ "\n"
				+ "TabNoteRhythm\n"
				+ "2 \n"
				+ "5.4 \n"
				+ "3 4 \n"
				+ "q\n"
				+ "w\n",
				UtilsTest.testSave(string), "Checking save successful, many notes");
	}
	
	@AfterEach
	public void end(){}
	
}
