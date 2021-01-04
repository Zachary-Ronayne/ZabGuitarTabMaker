package tab;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.Music;
import music.NotePosition;
import music.Pitch;
import music.Rhythm;
import music.TimeSignature;
import tab.TabString.SymbolHolder;
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
	
	private SymbolHolder hold;
	
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
		
		hold = new SymbolHolder(notes[0]);
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
	public void symbol(){
		string.add(notes[0]);
		assertEquals(notes[0], string.symbol(0), "Checking symbol found after adding it");
	}
	
	@Test
	public void replace(){
		string.add(notes[0]);
		assertNotEquals(notes[0].getPos(), notes[1].getPos(), "Checking symbols have the different positions before replacement");
		string.replace(0, notes[1]);
		assertEquals(notes[1], string.symbol(0), "Checking symbol found after adding it");
		assertEquals(notes[0].getPos(), notes[1].getPos(), "Checking symbols have the same position after a replacement");
	}
	
	@Test
	public void add(){
		// Adding notes in arbitrary order, including duplicates which should be ignored
		string.add(notes[4]);
		string.add(notes[6]);
		string.add(notes[2]);
		string.add(notes[0]);
		string.add(notes[0]);
		string.add(notes[5]);
		string.add(notes[5]);
		string.add(notes[1]);
		string.add(notes[3]);
		string.add(notes[3]);
		
		assertEquals(7, string.size(), "Checking correct number of notes added, no duplicates");
		assertEquals(notes[0], string.symbol(0), "Checking notes are added in sorted order");
		assertEquals(notes[1], string.symbol(1), "Checking notes are added in sorted order");
		assertEquals(notes[2], string.symbol(2), "Checking notes are added in sorted order");
		assertEquals(notes[3], string.symbol(3), "Checking notes are added in sorted order");
		assertEquals(notes[4], string.symbol(4), "Checking notes are added in sorted order");
		assertEquals(notes[5], string.symbol(5), "Checking notes are added in sorted order");
		assertEquals(notes[6], string.symbol(6), "Checking notes are added in sorted order");

		// Adding notes as holders directly
		string.clear();
		string.add(new SymbolHolder(notes[4]));
		string.add(new SymbolHolder(notes[6]));
		string.add(new SymbolHolder(notes[2]));
		string.add(new SymbolHolder(notes[2]));
		assertEquals(3, string.size(), "Checking correct number of notes added, no duplicates");
		assertEquals(notes[2], string.symbol(0), "Checking notes are added in sorted order");
		assertEquals(notes[4], string.symbol(1), "Checking notes are added in sorted order");
		assertEquals(notes[6], string.symbol(2), "Checking notes are added in sorted order");
	}
	
	@Test
	public void getAll(){
		string.add(new SymbolHolder(notes[1]));
		string.add(new SymbolHolder(notes[2]));
		string.add(new SymbolHolder(notes[4]));
		
		ArrayList<TabSymbol> all = string.getAll();
		assertEquals(3, all.size(), "Checking correct number of notes obtained");
		assertEquals(notes[1], all.get(0), "Checking notes in correct order");
		assertEquals(notes[2], all.get(1), "Checking notes in correct order");
		assertEquals(notes[4], all.get(2), "Checking notes in correct order");
	}
	
	@Test
	public void remove(){
		string.add(new SymbolHolder(notes[1]));
		string.add(new SymbolHolder(notes[2]));
		string.add(new SymbolHolder(notes[4]));
		
		assertFalse(string.remove(notes[0]), "Checking note not in string not removed");
		assertTrue(string.remove(notes[2]), "Checking note in string removed");
		assertEquals(2, string.size(), "Checking correct number of notes obtained");
		assertEquals(notes[1], string.symbol(0), "Checking correct notes remain");
		assertEquals(notes[4], string.symbol(1), "Checking correct notes remain");
	}
	
	@Test
	public void placeQuantizedNote(){
		TabNote n;
		TabNote placed;
		
		placed = (TabNote)string.placeQuantizedNote(sig, 0, 1.01).getSymbol();
		n = (TabNote)string.symbol(0);
		assertEquals(n, placed, "Checking placed note is the one returned");
		assertEquals(4, n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(1, n.getPos(), "Checking note has quantized position");
		
		placed = (TabNote)string.placeQuantizedNote(sig, 6, 1.49).getSymbol();
		n = (TabNote)string.symbol(1);
		assertEquals(n, placed, "Checking placed note is the one returned");
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
		
		assertEquals(1, string.symbol(0).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(2, string.symbol(1).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(3, string.symbol(2).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(4, string.symbol(3).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(5, string.symbol(4).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(6, string.symbol(5).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
		assertEquals(7, string.symbol(6).getPosition().getValue(), UtilsTest.DELTA, "Checking note is quantized");
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
		assertEquals(new TabNote(2, 1), string.symbol(0), "Checking correct note loaded");
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
		TabSymbol t = string.symbol(0);
		assertTrue("Checking note is correct type", t instanceof TabNote);
		TabNote n = (TabNote)t;
		assertEquals(1, n.getPitch().getNote(), "Checking correct pitch");
		assertEquals(2.0, n.getPos(), "Checking correct position");
		assertEquals("", n.getModifier().getBefore(), "Checking correct before modifier");
		assertEquals("", n.getModifier().getAfter(), "Checking correct after modifier");
		
		assertEquals(new TabDeadNote(new NotePosition(3)),
				string.symbol(1), "Checking correct note loaded");
		assertEquals(new TabNoteRhythm(new Pitch(2), new Rhythm(3, 4), new NotePosition(5.4), new TabModifier("q", "w")),
				string.symbol(2), "Checking correct note loaded");
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
	
	@Test
	public void getSymbolSymbolHolder(){
		assertEquals(notes[0], hold.getSymbol(), "Checking symbol initialized");
	}
	
	@Test
	public void setSymbolSymbolHolder(){
		hold.setSymbol(notes[1]);
		assertEquals(notes[1], hold.getSymbol(), "Checking symbol set");
	}
	
	@Test
	public void compareToSymbolHolder(){
		SymbolHolder low = new SymbolHolder(notes[0]);
		SymbolHolder high = new SymbolHolder(notes[1]);
		SymbolHolder high2 = new SymbolHolder(notes[1]);
		assertTrue(low.compareTo(high) < 0, "Checking comparing lower to higher");
		assertTrue(high.compareTo(low) > 0, "Checking comparing higher to lower");
		assertTrue(high.compareTo(high2) == 0, "Checking comparing equal");
	}

	@Test
	public void equalsSymbolHolder(){
		SymbolHolder copy = new SymbolHolder(notes[0]);
		assertFalse(copy == hold, "Checking objects are not the same one");
		assertTrue(copy.equals(hold), "Checking objects are equal");
	}
	
	@AfterEach
	public void end(){}
	
}
