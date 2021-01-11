package tab;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import music.Music;
import music.NotePosition;
import music.Pitch;
import music.Rhythm;
import music.TimeSignature;
import tab.symbol.TabDeadNote;
import tab.symbol.TabModifier;
import tab.symbol.TabNote;
import tab.symbol.TabNoteRhythm;
import tab.symbol.TabPitch;
import tab.symbol.TabSymbol;
import util.testUtils.UtilsTest;

public class TestTabString{
	
	private TabString string;
	private Pitch pitch;
	private Pitch newPitch;
	private Pitch negPitch;
	
	private TabPitch[] pitches;
	private TabPosition[] notes;
	
	private TabString noteAndOctave;
	
	private TimeSignature sig;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		newPitch = new Pitch(0);
		pitch = new Pitch(4);
		string = new TabString(pitch);
		
		pitches = new TabPitch[]{
				new TabNote(4), 
				new TabNote(7), 
				new TabNote(9), 
				new TabNote(4), 
				new TabNote(7), 
				new TabNote(10),
				new TabNote(9)
		};
		
		notes = new TabPosition[]{
				new TabPosition(pitches[0], 0),
				new TabPosition(pitches[1], 1),
				new TabPosition(pitches[2], 2),
				new TabPosition(pitches[3], 3),
				new TabPosition(pitches[4], 4),
				new TabPosition(pitches[5], 5),
				new TabPosition(pitches[6], 6)
		};
		negPitch = new Pitch(-3);
		
		noteAndOctave = new TabString(Music.B, 3);
		
		sig = new TimeSignature(4, 4);
	}

	@Test
	public void copy(){
		string.add(notes[0]);
		string.add(notes[1]);
		string.add(notes[2]);
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
		assertEquals(notes[0].getSymbol(), string.symbol(0), "Checking symbol found after adding it");
	}
	
	@Test
	public void replace(){
		string.add(notes[0]);
		string.add(notes[1]);
		string.replace(0, pitches[1]);
		assertEquals(pitches[1], string.symbol(0), "Checking symbol found after replacing it");

		string.replace(1, pitches[2]);
		assertEquals(pitches[2], string.symbol(1), "Checking symbol found after replacing it");
	}
	
	@Test
	public void add(){
		string.add(null);
		assertTrue(string.isEmpty(), "Checking string size unchanged after adding null");
		
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
		assertEquals(notes[0], string.get(0), "Checking notes are added in sorted order");
		assertEquals(notes[1], string.get(1), "Checking notes are added in sorted order");
		assertEquals(notes[2], string.get(2), "Checking notes are added in sorted order");
		assertEquals(notes[3], string.get(3), "Checking notes are added in sorted order");
		assertEquals(notes[4], string.get(4), "Checking notes are added in sorted order");
		assertEquals(notes[5], string.get(5), "Checking notes are added in sorted order");
		assertEquals(notes[6], string.get(6), "Checking notes are added in sorted order");

		// Adding notes as pitches
		string.clear();
		string.add(pitches[4], 1);
		string.add(pitches[6], new NotePosition(2));
		string.add(pitches[2], new NotePosition(0));
		string.add(pitches[2], 0);
		assertEquals(3, string.size(), "Checking correct number of notes added, no duplicates");
		assertEquals(pitches[2], string.symbol(0), "Checking notes are added in sorted order");
		assertEquals(pitches[4], string.symbol(1), "Checking notes are added in sorted order");
		assertEquals(pitches[6], string.symbol(2), "Checking notes are added in sorted order");
	}
	
	@Test
	public void getAll(){
		string.add(notes[1]);
		string.add(notes[2]);
		string.add(notes[4]);
		
		ArrayList<TabSymbol> all = string.getAll();
		assertEquals(3, all.size(), "Checking correct number of notes obtained");
		assertEquals(pitches[1], all.get(0), "Checking notes in correct order");
		assertEquals(pitches[2], all.get(1), "Checking notes in correct order");
		assertEquals(pitches[4], all.get(2), "Checking notes in correct order");
	}
	
	@Test
	public void remove(){
		string.add(notes[1]);
		string.add(notes[2]);
		string.add(notes[4]);
		
		assertFalse(string.remove(notes[0]), "Checking note not in string not removed");
		assertTrue(string.remove(notes[2]), "Checking note in string removed");
		assertEquals(2, string.size(), "Checking correct number of notes obtained");
		assertEquals(notes[1], string.get(0), "Checking correct notes remain");
		assertEquals(notes[4], string.get(1), "Checking correct notes remain");
		
		assertTrue(string.remove(pitches[4], 4), "Checking note is removed");
		assertEquals(1, string.size(), "Checking correct number of notes obtained");
		assertEquals(notes[1], string.get(0), "Checking correct note remains, removing via symbol");

		assertFalse(string.remove(pitches[4], 1.5), "Checking note not in list isn't removed");
	}
	
	@Test
	public void placeQuantizedNote(){
		TabNote n;
		TabNote placed;
		TabPosition np;
		
		placed = (TabNote)string.placeQuantizedNote(sig, 0, 1.01).getSymbol();
		np = string.get(0);
		n = (TabNote)np.getSymbol();
		assertEquals(n, placed, "Checking placed note is the one returned");
		assertEquals(4, n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(1, np.getPos(), "Checking note has quantized position");
		
		placed = (TabNote)string.placeQuantizedNote(sig, 6, 1.49).getSymbol();
		np = string.get(1);
		n = (TabNote)np.getSymbol();
		assertEquals(n, placed, "Checking placed note is the one returned");
		assertEquals(10, n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(1.5, np.getPos(), "Checking note has quantized position");
		
		assertEquals(null, string.placeQuantizedNote(sig, 6, 1.49),
					"Checking placing note at the same position returns null");
	}
	
	@Test
	public void getTabNumber(){
		assertEquals(0, string.getTabNumber(pitches[0]), "Checking tab number is correctly found");
		assertEquals(3, string.getTabNumber(pitches[1]), "Checking tab number is correctly found");
		assertEquals(5, string.getTabNumber(pitches[2]), "Checking tab number is correctly found");
		assertEquals(0, string.getTabNumber(pitches[3]), "Checking tab number is correctly found");
		assertEquals(3, string.getTabNumber(pitches[4]), "Checking tab number is correctly found");
		assertEquals(6, string.getTabNumber(pitches[5]), "Checking tab number is correctly found");
		assertEquals(5, string.getTabNumber(pitches[6]), "Checking tab number is correctly found");
		
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
		for(TabPosition p : notes){
			p.getPosition().addValue(1.1);
			string.add(p);
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
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void equals(){
		TabString s = new TabString(pitch);
		s.add(notes[0]);
		s.add(notes[1]);
		string.add(notes[0]);
		string.add(notes[1]);
		assertFalse(s == string, "Checking objects are not the same object");
		assertTrue(s.equals(string), "Checking objects are equal");
		
		assertFalse(s.equals(pitch), "Checking objects are not equal when not a string");
		
		s.setRootPitch(newPitch);
		assertFalse(s.equals(string), "Checking objects are not equal");
		
		s.setRootPitch(pitch);
		assertTrue(s.equals(string), "Checking objects are equal");
		s.remove(notes[0]);
		assertFalse(s.equals(string), "Checking objects are not equal");
		s.add(notes[0]);
		assertTrue(s.equals(string), "Checking objects are equal");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("2 \n0\n2 \n1\nTabNote\n2 \n\n\n1.0 \n");
		assertTrue("Checking load successful", string.load(scan));
		assertEquals(2, string.getRootNote(), "Checking root note set");
		assertEquals(0, string.size(), "Checking no notes exist");
		assertTrue("Checking load successful", string.load(scan));
		assertEquals(2, string.getRootNote(), "Checking root note set");
		assertEquals(1, string.size(), "Checking one note exists");
		assertEquals(new TabNote(2), string.symbol(0), "Checking correct note loaded");
		assertEquals(1.0, string.get(0).getPos(), "Checking correct position loaded");
		assertFalse("Checking load failed with not enough data", string.load(scan));
		
		scan.close();
		scan = new Scanner(""
				+ "1 \n"
				+ "a \n");
		assertFalse(string.load(scan), "Checking load fails with invalid note count");
		
		scan.close();
		scan = new Scanner("0 \n0");
		assertFalse(string.load(scan), "Checking load fails with no new line to load");
		
		scan = new Scanner("0 \n1 \n a");
		assertFalse(string.load(scan), "Checking load fails with invalid symbols");
		
		scan.close();
		scan = new Scanner("4 \n"
				+ "3\n"
				+ "TabNote\n"
				+ "1 \n"
				+ "\n"
				+ "\n"
				+ "2.0\n"
				+ "TabDeadNote\n"
				+ "\n"
				+ "\n"
				+ "3.1\n"
				+ "TabNoteRhythm\n"
				+ "2 \n"
				+ "3 4 \n"
				+ "q\n"
				+ "w\n"
				+ "4.2\n");

		assertTrue("Checking load successful", string.load(scan));
		assertEquals(4, string.getRootNote(), "Checking root note set");
		assertEquals(3, string.size(), "Checking three notes exist");
		TabPosition p = string.get(0);
		TabSymbol t = p.getSymbol();
		assertTrue("Checking note is correct type", t instanceof TabNote);
		TabNote n = (TabNote)t;
		assertEquals(1, n.getPitch().getNote(), "Checking correct pitch");
		assertEquals(2.0, p.getPos(), "Checking correct position");
		assertEquals("", n.getModifier().getBefore(), "Checking correct before modifier");
		assertEquals("", n.getModifier().getAfter(), "Checking correct after modifier");
		
		assertEquals(new TabDeadNote(),
				string.symbol(1), "Checking correct note loaded");
		assertEquals(3.1,
				string.get(1).getPos(), "Checking correct position loaded");
		assertEquals(new TabNoteRhythm(new Pitch(2), new Rhythm(3, 4), new TabModifier("q", "w")),
				string.symbol(2), "Checking correct note loaded");
		assertEquals(4.2,
				string.get(2).getPos(), "Checking correct position loaded");
	}
	
	@Test
	public void save(){
		assertEquals("4 \n0\n", UtilsTest.testSave(string), "Checking save successful, no notes");
		
		string.setRootPitch(new Pitch(2));
		string.add(new TabNote(2), 1);
		assertEquals("2 \n1\nTabNote\n2 \n\n\n1.0 \n", UtilsTest.testSave(string), "Checking save successful, one note");
		
		string.clear();
		string.setRootPitch(new Pitch(4));
		string.add(new TabNote(1), 2);
		string.add(new TabDeadNote(), 3);
		string.add(new TabNoteRhythm(new Pitch(2), new Rhythm(3, 4), new TabModifier("q", "w")), 5.4);
		assertEquals(
				"4 \n"
				+ "3\n"
				+ "TabNote\n"
				+ "1 \n"
				+ "\n"
				+ "\n"
				+ "2.0 \n"
				+ "TabDeadNote\n"
				+ "\n"
				+ "\n"
				+ "3.0 \n"
				+ "TabNoteRhythm\n"
				+ "2 \n"
				+ "3 4 \n"
				+ "q\n"
				+ "w\n"
				+ "5.4 \n",
				UtilsTest.testSave(string), "Checking save successful, many notes");
	}
	
	@Test
	public void testToString(){
		for(TabPosition p : notes) string.add(p);
		
		assertEquals(""
				+ "[TabString, "
				+ "rootPitch: [Pitch: E4], "
				+ "[Notes: "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"4\"], [TabModifier: \"\" \"\"], [Pitch: E4]], [NotePosition, position: 0.0]], "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"7\"], [TabModifier: \"\" \"\"], [Pitch: G4]], [NotePosition, position: 1.0]], "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"9\"], [TabModifier: \"\" \"\"], [Pitch: A4]], [NotePosition, position: 2.0]], "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"4\"], [TabModifier: \"\" \"\"], [Pitch: E4]], [NotePosition, position: 3.0]], "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"7\"], [TabModifier: \"\" \"\"], [Pitch: G4]], [NotePosition, position: 4.0]], "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"10\"], [TabModifier: \"\" \"\"], [Pitch: A#4]], [NotePosition, position: 5.0]], "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"9\"], [TabModifier: \"\" \"\"], [Pitch: A4]], [NotePosition, position: 6.0]]"
				+ "]"
				+ "]", string.toString(), "Checking correct string");
		
		string.clear();
		assertEquals(""
				+ "[TabString, "
					+ "rootPitch: [Pitch: E4], "
					+ "[Notes: "
					+ "]"
				+ "]", string.toString(), "Checking correct empty string");
	}
	
	
	@AfterEach
	public void end(){}
	
}
