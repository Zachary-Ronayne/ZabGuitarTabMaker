package tab;

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
import music.Pitch;
import music.Rhythm;
import music.TimeSignature;
import tab.symbol.TabModifier;
import tab.symbol.TabNote;
import tab.symbol.TabNoteRhythm;
import tab.symbol.TabSymbol;
import util.testUtils.UtilsTest;

public class TestTab{
			
	private Tab tab;
	private Tab tabDefault;
	private Tab tabSpecific;
	private Tab tabFull;
	private ArrayList<TabString> strings;
	private TabString highString;
	private TabString lowString;
	
	private Tab tabForRhythms;
	private TabString highRhythms;
	private TabString lowRhythms;

	private static TimeSignature four4;
	private static TimeSignature five4;
	private static TimeSignature three4;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		four4 = new TimeSignature(4, 4);
		five4 = new TimeSignature(5, 4);
		three4 = new TimeSignature(3, 4);
	}
	
	@BeforeEach
	public void setup(){
		highString = new TabString(new Pitch(Music.createNote(Music.C, 3)));
		lowString = new TabString(new Pitch(Music.createNote(Music.C, 2)));
		strings = new ArrayList<TabString>();
		strings.add(highString);
		strings.add(lowString);
		tab = new Tab(strings);
		
		tabDefault = new Tab();
		tabSpecific = new Tab(strings, new TimeSignature(3, 2));
		tabFull = new Tab(strings, new TimeSignature(4, 4), true);
		
		tabForRhythms = new Tab();
		highRhythms = new TabString(Music.createPitch(Music.E, 3));
		lowRhythms = new TabString(Music.createPitch(Music.E, 2));
		tabForRhythms.getStrings().add(highRhythms);
		tabForRhythms.getStrings().add(lowRhythms);
		highRhythms.add(TabFactory.modifiedFret(highRhythms, 0, 0));
		highRhythms.add(TabFactory.modifiedFret(highRhythms, 0, 1));
		highRhythms.add(TabFactory.modifiedFret(highRhythms, 0, 1.5));
		lowRhythms.add(TabFactory.modifiedFret(lowRhythms, 0, 2));
		lowRhythms.add(TabFactory.modifiedFret(lowRhythms, 0, 2.25));
		lowRhythms.add(TabFactory.modifiedFret(lowRhythms, 0, 2.375));
	}
	
	@Test
	public void copy(){
		Tab copy = tab.copy();
		assertTrue(copy.equals(tab), "Checking copy is equal to the source object");
		assertTrue(copy != tab, "Checking copy is not the same as the source object");
	}
	
	@Test
	public void copyWithoutSymbols(){
		Tab copy = tabForRhythms.copy();
		Tab cleared = copy.copyWithoutSymbols();
		
		assertEquals(copy.getStrings().get(0).getRootPitch(), highRhythms.getRootPitch(), "Checking first string copied correctly");
		assertEquals(copy.getStrings().get(1).getRootPitch(), lowRhythms.getRootPitch(), "Checking first string copied correctly");
		assertEquals(copy.getTimeSignature(), cleared.getTimeSignature(), "Checking time signature copied correctly");
		assertEquals(copy.usesRhythm(), cleared.usesRhythm(), "Checking use of rhythm copied correctly");
		assertTrue(cleared.isEmpty(), "Checking all of the notes have been removed");
		assertFalse(copy.isEmpty(), "Checking source tab hasn't been cleared");
	}
	
	@Test
	public void getStrings(){
		assertEquals(strings, tab.getStrings(), "Checking strings initialized");
	}
	
	@Test
	public void setStrings(){
		ArrayList<TabString> newStrings = new ArrayList<TabString>();
		tab.setStrings(newStrings);
		assertEquals(newStrings, tab.getStrings(), "Checking strings set");
	}
	
	@Test
	public void getTimeSignature(){
		assertEquals(new TimeSignature(4, 4), tab.getTimeSignature(), "Checking time signature initialized");
		assertEquals(new TimeSignature(4, 4), tabDefault.getTimeSignature(), "Checking time signature initialized");
		assertEquals(new TimeSignature(3, 2), tabSpecific.getTimeSignature(), "Checking time signature initialized");
	}
	
	@Test
	public void setTimeSignature(){
		TimeSignature t = new TimeSignature(5, 4);
		tab.setTimeSignature(t);
		assertEquals(t, tab.getTimeSignature(), "Checking time signature set");
	}
	
	@Test
	public void usesRhythm(){
		assertFalse(tab.usesRhythm(), "Checking rhythm initialized");
		assertTrue(tabFull.usesRhythm(), "Checking initialized");
	}
	
	@Test
	public void setUsesRhythm(){
		lowString.add(TabFactory.modifiedFret(lowString, 0, 0));
		tab.setUsesRhythm(true);
		assertTrue(tab.usesRhythm(), "Checking rhythm set");
		assertTrue(lowString.symbol(0).usesRhythm(), "Checking note uses rhythm");
		
		tab.setUsesRhythm(false);
		assertFalse(tab.usesRhythm(), "Checking rhythm set");
		assertFalse(lowString.symbol(0).usesRhythm(), "Checking note doesn't use rhythm");
	}
	
	@Test
	public void retime(){
		Tab copy;
		TabString copyH;
		TabString copyL;
		TimeSignature newTime;
		
		highString.add(TabFactory.modifiedFret(highString, 0, 0));
		highString.add(TabFactory.modifiedFret(highString, 0, 0.75));
		highString.add(TabFactory.modifiedFret(highString, 0, 1));
		highString.add(TabFactory.modifiedFret(highString, 0, 1.25));
		lowString.add(TabFactory.modifiedFret(lowString, 0, 2));
		lowString.add(TabFactory.modifiedFret(lowString, 0, 2.9));

		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(5, 4);
		copy.retime(newTime, false, false);
		assertEquals(newTime, copy.getTimeSignature(), "Checking retime set the time signature");
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertRetimed(copyH, new double[]{0, 0.6, 0.8, 1}, false, false, four4, five4);
		assertRetimed(copyL, new double[]{1.6, 2.32}, false, false, four4, five4);
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(5, 4);
		copy.retime(newTime, false, true);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertRetimed(copyH, new double[]{0, 0.6, 1, 1.2}, false, true, four4, five4);
		assertRetimed(copyL, new double[]{2, 2.72}, false, true, four4, five4);
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(3, 4);
		copy.retime(newTime, false, false);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertRetimed(copyH, new double[]{0, 1, 1.33333333333333, 1.66666666666667}, false, false, four4, three4);
		assertRetimed(copyL, new double[]{2.66666666666667, 3.86666666666667}, false, false, four4, three4);
		
		copy = copy.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(5, 4);
		copy.retime(newTime, false, false);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra true, converting 3/4 to 5/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale false, deleteExtra true, converting 3/4 to 5/4");
		assertRetimed(copyH, new double[]{0, 0.6, 0.8, 1}, false, true, three4, five4);
		assertRetimed(copyL, new double[]{1.6, 2.32}, false, true, three4, five4);
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(3, 4);
		copy.retime(newTime, false, true);
		assertEquals(2, copyH.size(), "Checking 2 symbols were deleted with rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(1, copyL.size(), "Checking one symbol was deleted with rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertRetimed(copyH, new double[]{0, 1.33333333333333}, false, true, four4, three4);
		assertRetimed(copyL, new double[]{2}, false, true, four4, three4);
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(5, 4);
		copy.retime(newTime, true, true);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale true, converting 4/4 to 5/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale true, converting 4/4 to 5/4");
		assertRetimed(copyH, new double[]{0, 0.75, 1, 1.25}, true, true, four4, five4);
		assertRetimed(copyL, new double[]{2, 2.9}, true, true, four4, five4);
	}
	
	/**
	 * Utility method for testing retime. Assert that a TabString's notes are correctly retimed
	 * @param str The TabString to test
	 * @param pos The expected positions in the order of the indexes of the string
	 * @param rescale True if rescale was true during the call, false otherwise, this is only for display purposes
	 * @param deleteExtra True if the deleteExtra was true during the call, false otherwise, this is only for display purposes
	 * @param oldTime The old time signature, this is only for display purposes
	 * @param newTime The time signature converted to, this is only for display purposes
	 */
	public static void assertRetimed(TabString str, double[] pos, boolean rescale, boolean deleteExtra, TimeSignature oldTime, TimeSignature newTime){
		for(int i = 0; i < pos.length; i++){
			assertRetimed(str, i, pos[i], rescale, deleteExtra, oldTime, newTime);
		}
	}
	
	/**
	 * Utility method for testing retime. Assert that a TabString's note is correctly retimed
	 * @param str The TabString to test
	 * @param noteIndex The index of the note
	 * @param pos The expected position
	 * @param rescale True if rescale was true during the call, false otherwise, this is only for display purposes
	 * @param deleteExtra True if the deleteExtra was true during the call, false otherwise, this is only for display purposes
	 * @param oldTime The old time signature, this is only for display purposes
	 * @param newTime The time signature converted to, this is only for display purposes
	 */
	public static void assertRetimed(TabString str, int noteIndex, double pos, boolean rescale, boolean deleteExtra, TimeSignature oldTime, TimeSignature newTime){
		assertEquals(pos, str.get(noteIndex).getPos(), UtilsTest.DELTA,
				"Checking correct retimed note rescale: " + rescale + ", deleteExtra: " + deleteExtra
				+ ", converting " + oldTime + " to " + newTime);
	}
	
	@Test
	public void quantize(){
		lowString.add(new TabPosition(new TabNote(0), 1.1));
		highString.add(new TabPosition(new TabNote(0), 2.1));
		tab.quantize(1);
		assertEquals(1, lowString.get(0).getPosition().getValue(), UtilsTest.DELTA, "Checking string is quantized");
		assertEquals(2, highString.get(0).getPosition().getValue(), UtilsTest.DELTA, "Checking string is quantized");
	}
	
	@Test
	public void placeQuantizedNote(){
		TabPosition returnNote = tab.placeQuantizedNote(1, 3, 2.1);
		TabPosition p = lowString.get(0);
		assertEquals(p, returnNote, "Checking returned note was the same as the one placed");
		assertEquals(-21, ((TabNote)p.getSymbol()).getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(2.125, p.getPos(), "Checking note has quantized position");
		
		assertEquals(null, tab.placeQuantizedNote(1, 3, 2.1), "Checking placing note at the same position returns null");
	}
	
	@Test
	public void clearNotes(){
		assertFalse(highRhythms.isEmpty(), "Checking strings contain notes");
		assertFalse(lowRhythms.isEmpty(), "Checking strings contain notes");
		tabForRhythms.clearNotes();
		assertTrue(highRhythms.isEmpty(), "Checking string notes removed");
		assertTrue(lowRhythms.isEmpty(), "Checking string notes removed");
	}
	
	@Test
	public void isEmpty(){
		assertFalse(tabForRhythms.isEmpty(), "Checking tab is not empty");
		tabForRhythms.clearNotes();
		assertTrue(tabForRhythms.isEmpty(), "Checking tab is empty after removing notes");
		
		highRhythms.clear();
		tabForRhythms.placeQuantizedNote(1, 0, 0);
		assertFalse(tabForRhythms.isEmpty(), "Checking tab is not empty with notes only on last string");
		
		tabForRhythms.placeQuantizedNote(0, 0, 0);
		lowRhythms.clear();
		assertFalse(tabForRhythms.isEmpty(), "Checking tab is not empty with notes only on first string");
		
		tabForRhythms.getStrings().clear();
		assertTrue(tabForRhythms.isEmpty(), "Checking tab is empty with no strings");
	}
	
	@Test
	public void getRootNote(){
		assertEquals(-12, tab.getRootNote(0), "Checking root note found");
		assertEquals(-24, tab.getRootNote(1), "Checking root note found");
	}
	
	@Test
	public void removeRhythms(){
		tabFull.getStrings().get(0).add(TabFactory.modifiedFretRhythm(highString, 0, new Rhythm(1, 1), 0, new TabModifier()));
		tabFull.getStrings().get(1).add(TabFactory.modifiedFretRhythm(lowString, 0, new Rhythm(1, 1), 0, new TabModifier()));
		assertTrue(tabFull.usesRhythm(), "Checking using rhythm is set to true initially");
		assertTrue(highString.symbol(0).usesRhythm(), "Checking rhythm is being used");
		assertTrue(lowString.symbol(0).usesRhythm(), "Checking rhythm is being used");
		
		tabFull.removeRhythms();
		assertFalse(tabFull.usesRhythm(), "Checking using rhythm is set to false");
		assertFalse(highString.symbol(0).usesRhythm(), "Checking rhythm is not being used");
		assertFalse(lowString.symbol(0).usesRhythm(), "Checking rhythm is not being used");
	}
	
	@Test
	public void addRhythm(){
		assertFalse(tabForRhythms.usesRhythm(), "Checking using rhythm is set to false initially");
		assertFalse(highRhythms.symbol(0).usesRhythm(), "Checking note doesn't use rhythm"); 
		assertFalse(highRhythms.symbol(1).usesRhythm(), "Checking note doesn't use rhythm"); 
		assertFalse(highRhythms.symbol(2).usesRhythm(), "Checking note doesn't use rhythm"); 
		assertFalse(lowRhythms.symbol(0).usesRhythm(), "Checking note doesn't use rhythm"); 
		assertFalse(lowRhythms.symbol(1).usesRhythm(), "Checking note doesn't use rhythm"); 
		assertFalse(lowRhythms.symbol(2).usesRhythm(), "Checking note doesn't use rhythm"); 
		
		Rhythm one8 = new Rhythm(1, 8);
		tabForRhythms.addRhythm(one8);
		assertTrue(tabForRhythms.usesRhythm(), "Checking using rhythm is set to true");
		assertRhythmEqual(1, 8, highRhythms.symbol(0));
		
		tabForRhythms.removeRhythms();
		tabForRhythms.addRhythm(null);
		assertTrue(tabForRhythms.usesRhythm(), "Checking using rhythm is set to true");
		assertRhythmEqual(1, 1, highRhythms.symbol(0));
		assertRhythmEqual(1, 2, highRhythms.symbol(1));
		
		highRhythms.clear();
		lowRhythms.clear();
		tabForRhythms.addRhythm(null);
		assertEquals(0, highRhythms.size(), "Checking nothing happens when adding rhythms to empty strings");
		assertEquals(0, lowRhythms.size(), "Checking nothing happens when adding rhythms to empty strings");
	}
	
	@Test
	public void guessRhythms(){
		tabForRhythms.removeRhythms();
		tabForRhythms.guessRhythms();
		assertTrue(tabForRhythms.usesRhythm(), "Checking using rhythm is set to true");
		assertRhythmEqual(1, 1, highRhythms.symbol(0));
		assertRhythmEqual(1, 2, highRhythms.symbol(1));
		assertRhythmEqual(1, 4, highRhythms.symbol(2));
		assertRhythmEqual(1, 4, lowRhythms.symbol(0));
		assertRhythmEqual(1, 8, lowRhythms.symbol(1));
		assertRhythmEqual(1, 4, lowRhythms.symbol(2));
	}

	@Test
	public void setRhythmAll(){
		tabForRhythms.removeRhythms();
		tabForRhythms.setRhythmAll(new Rhythm(1, 8));
		assertTrue(tabForRhythms.usesRhythm(), "Checking using rhythm is set to true");
		assertRhythmEqual(1, 8, highRhythms.symbol(0));
		assertRhythmEqual(1, 8, highRhythms.symbol(1));
		assertRhythmEqual(1, 8, highRhythms.symbol(2));
		assertRhythmEqual(1, 8, lowRhythms.symbol(0));
		assertRhythmEqual(1, 8, lowRhythms.symbol(1));
		assertRhythmEqual(1, 8, lowRhythms.symbol(2));
	}
	
	/***
	 * Helper method for checking rhythmic equality and showing test information
	 * @param duration
	 * @param unit
	 * @param note
	 */
	private void assertRhythmEqual(int duration, int unit, TabSymbol note){
		assertTrue(note instanceof TabNoteRhythm, "Note should be a TabNoteRhythm, was a " + note.getClass().getSimpleName());
		Rhythm r = ((TabNoteRhythm)note).getRhythm();
		assertEquals(new Rhythm(duration, unit), r, "Checking given rhythm has the same rhythm as the note");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner(""
				+ "false 4 4 \n"
				+ "0\n"
				
				+ "false 4 4 \n"
				+ "2\n"
				+ "-12 \n"
				+ "0\n"
				+ "-24 \n"
				+ "0\n"
				
				+ "false 4 4 \n"
				+ "1\n"
				+ "-20 \n"
				+ "3\n"
				+ "TabNote\n"
				+ "-20 \n"
				+ "\n"
				+ "\n"
				+ "2.0 \n"
				+ "TabNote\n"
				+ "-20 \n"
				+ "\n"
				+ "\n"
				+ "2.25 \n"
				+ "TabNote\n"
				+ "-20 \n"
				+ "\n"
				+ "\n"
				+ "2.375 \n"
				+ "g\n");
		
		Tab t = new Tab();
		tab.getStrings().clear();
		assertTrue(t.load(scan), "Checking loading successful");
		assertEquals(tab, t, "Checking loaded tab equal, no strings");
				
		tab.getStrings().clear();
		tab.getStrings().add(highString);
		tab.getStrings().add(lowString);
		assertTrue(t.load(scan), "Checking loading successful");
		assertEquals(tab, t, "Checking loaded tab equal, empty strings");
		
		tab.getStrings().clear();
		tab.getStrings().add(lowRhythms);
		assertTrue(t.load(scan), "Checking loading successful");
		assertEquals(tab, t, "Checking loaded tab equal, strings with notes");
		assertFalse(t.load(scan), "Checking load fails with invalid data");
		
		scan.close();
		scan = new Scanner(""
				+ "false a 4 \n"
				+ "0\n");
		assertFalse(t.load(scan), "Checking load fails on invalid time signature");
		
		scan.close();
		scan = new Scanner(""
				+ "false 4 4 \n"
				+ "a\n");
		assertFalse(t.load(scan), "Checking load fails on invalid number of strings");
		
		scan.close();
		scan = new Scanner(""
				+ "false 4 4 \n"
				+ "1");
		assertFalse(t.load(scan), "Checking load fails on not having a new line");
		
		scan.close();
		scan = new Scanner(""
				+ "false 4 4 \n"
				+ "2\n"
				+ "1\n"
				+ "0\n"
				+ "2\n"
				+ "0\n");
		assertTrue(t.load(scan), "Checking load succeeds loading multiple strings");
		
		scan.close();
		scan = new Scanner(""
				+ "false 4 4 \n"
				+ "2\n"
				+ "1\n"
				+ "0\n"
				+ "2\n"
				+ "a\n");
		assertFalse(t.load(scan), "Checking load fails on invalid strings");
		
		scan.close();
	}
	
	@Test
	public void save(){
		tab.getStrings().clear();
		assertEquals(""
				+ "false 4 4 \n"
				+ "0\n",
				UtilsTest.testSave(tab), "Checking save with no strings");
		
		tab.getStrings().add(highString);
		tab.getStrings().add(lowString);
		assertEquals(""
				+ "false 4 4 \n"
				+ "2\n"
				+ "-12 \n"
				+ "0\n"
				+ "-24 \n"
				+ "0\n",
				UtilsTest.testSave(tab), "Checking save with multiple TabStrings with no notes");

		tab.getStrings().clear();
		tab.getStrings().add(lowRhythms);
		assertEquals(""
				+ "false 4 4 \n"
				+ "1\n"
				+ "-20 \n"
				+ "3\n"
				+ "TabNote\n"
				+ "-20 \n"
				+ "\n"
				+ "\n"
				+ "2.0 \n"
				+ "TabNote\n"
				+ "-20 \n"
				+ "\n"
				+ "\n"
				+ "2.25 \n"
				+ "TabNote\n"
				+ "-20 \n"
				+ "\n"
				+ "\n"
				+ "2.375 \n",
				UtilsTest.testSave(tab), "Checking save with multiple TabStrings with notes");
		
		assertFalse(tab.save(null), "Checking save fails with invalid writer");
		
		tab.setTimeSignature(null);
		assertEquals(null, UtilsTest.testSave(tab), "Checking save fails with invalid time signature");
		
		tab.setTimeSignature(four4);
		tab.getStrings().add(null);
		assertEquals(null, UtilsTest.testSave(tab), "Checking save fails with invalid tab strings");
	}

	@Test
	public void equals(){
		assertFalse(tab.equals(null), "Checking tab is not equal to null");
		assertTrue(tab.equals(tab), "Checking tab is equal to itself");
		
		ArrayList<TabString> s = new ArrayList<TabString>();
		s.add(highString);
		s.add(lowString);
		Tab t = new Tab(s);
		assertFalse(t == tab, "Checking objects are not the same object");
		assertTrue(t.equals(tab), "Checking objects are equal");
		
		s.remove(lowString);
		assertFalse(t.equals(tab), "Checking objects are not equal after changing strings");
		s.add(lowString);
		assertTrue(t.equals(tab), "Checking objects equal");
		
		t.setTimeSignature(new TimeSignature(1, 8));
		assertFalse(t.equals(tab), "Checking objects not equal with different time signatures");
		t.setTimeSignature(new TimeSignature(4, 4));
		assertTrue(t.equals(tab), "Checking objects equal");
		
		t.setUsesRhythm(true);
		assertFalse(t.equals(tab), "Checking objects not equal with different rhythm usage");
	}

	@Test
	public void testToString(){
		assertEquals(""
				+ "[Tab, "
				+ "[TimeSignature: 4/4], "
				+ "usesRhythm: false, "
				+ "[Strings: "
				+ "[TabString, "
				+ "rootPitch: [Pitch: E3], "
				+ "[Notes: "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"-8\"], [TabModifier: \"\" \"\"], [Pitch: E3]], [NotePosition, position: 0.0]], "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"-8\"], [TabModifier: \"\" \"\"], [Pitch: E3]], [NotePosition, position: 1.0]], "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"-8\"], [TabModifier: \"\" \"\"], [Pitch: E3]], [NotePosition, position: 1.5]]"
				+ "]"
				+ "], "
				+ "[TabString, "
				+ "rootPitch: [Pitch: E2], "
				+ "[Notes: "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"-20\"], [TabModifier: \"\" \"\"], [Pitch: E2]], [NotePosition, position: 2.0]], "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"-20\"], [TabModifier: \"\" \"\"], [Pitch: E2]], [NotePosition, position: 2.25]], "
				+ "[TabPosition, [TabNote, [On C4 string, note: \"-20\"], [TabModifier: \"\" \"\"], [Pitch: E2]], [NotePosition, position: 2.375]]"
				+ "]"
				+ "]"
				+ "]"
				+ "]",
				tabForRhythms.toString(), "Checking correct string");
		
		tabForRhythms.clearNotes();
		assertEquals(""
				+ "[Tab, "
				+ "[TimeSignature: 4/4], "
				+ "usesRhythm: false, "
				+ "[Strings: "
				+ "[TabString, "
				+ "rootPitch: [Pitch: E3], "
				+ "[Notes: "
				+ "]"
				+ "], "
				+ "[TabString, "
				+ "rootPitch: [Pitch: E2], "
				+ "[Notes: "
				+ "]"
				+ "]"
				+ "]"
				+ "]",
				tabForRhythms.toString(), "Checking correct string with no notes");
		
		tabForRhythms.getStrings().clear();;
		assertEquals(""
			+ "[Tab, "
				+ "[TimeSignature: 4/4], "
				+ "usesRhythm: false, "
				+ "[Strings: "
				+ "]"
			+ "]",
			tabForRhythms.toString(), "Checking correct string with no strings");
	}
	
	@AfterEach
	public void end(){}
	
}
