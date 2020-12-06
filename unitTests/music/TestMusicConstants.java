package music;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TestMusicConstants{
	
	@BeforeEach
	public void setup(){}

	@Test
	public void intToNoteName(){
		assertEquals(Music.intToNoteName(0, true), "C", "Checking unmodified note is found");
		assertEquals(Music.intToNoteName(-2, true), "Bb", "Checking flat is found");
		assertEquals(Music.intToNoteName(-2, false), "A#", "Checking sharp is found");
		
		assertEquals(Music.intToNoteName(0, false), "C", "Checking note is found");
		assertEquals(Music.intToNoteName(1, false), "C#", "Checking note is found");
		assertEquals(Music.intToNoteName(2, false), "D", "Checking note is found");
		assertEquals(Music.intToNoteName(3, false), "D#", "Checking note is found");
		assertEquals(Music.intToNoteName(4, false), "E", "Checking note is found");
		assertEquals(Music.intToNoteName(5, false), "F", "Checking note is found");
		assertEquals(Music.intToNoteName(6, false), "F#", "Checking note is found");
		assertEquals(Music.intToNoteName(7, false), "G", "Checking note is found");
		assertEquals(Music.intToNoteName(8, false), "G#", "Checking note is found");
		assertEquals(Music.intToNoteName(9, false), "A", "Checking note is found");
		assertEquals(Music.intToNoteName(10, false), "A#", "Checking note is found");
		assertEquals(Music.intToNoteName(11, false), "B", "Checking note is found");
		assertEquals(Music.intToNoteName(12, false), "C", "Checking note is found");
		
		assertEquals(Music.intToNoteName(-1, false), "B", "Checking note is found");
		assertEquals(Music.intToNoteName(-2, false), "A#", "Checking note is found");
		assertEquals(Music.intToNoteName(-3, false), "A", "Checking note is found");
		assertEquals(Music.intToNoteName(-4, false), "G#", "Checking note is found");
		assertEquals(Music.intToNoteName(-5, false), "G", "Checking note is found");
		assertEquals(Music.intToNoteName(-6, false), "F#", "Checking note is found");
		assertEquals(Music.intToNoteName(-7, false), "F", "Checking note is found");
		assertEquals(Music.intToNoteName(-8, false), "E", "Checking note is found");
		assertEquals(Music.intToNoteName(-9, false), "D#", "Checking note is found");
		assertEquals(Music.intToNoteName(-10, false), "D", "Checking note is found");
		assertEquals(Music.intToNoteName(-11, false), "C#", "Checking note is found");
		assertEquals(Music.intToNoteName(-12, false), "C", "Checking note is found");
	}

	@Test
	public void intToNoteNameFlat(){
		assertEquals(Music.intToNoteNameFlat(-2), "Bb", "Checking flat is found");
		assertEquals(Music.intToNoteNameFlat(-4), "Ab", "Checking flat is found");
		assertEquals(Music.intToNoteNameFlat(1), "Db", "Checking flat is found");
		assertEquals(Music.intToNoteNameFlat(13), "Db", "Checking flat is found in higher ocvate");
		assertEquals(Music.intToNoteNameFlat(-14), "Bb", "Checking flat is found in lower ocvate");
	}
	
	@Test
	public void intToNoteNameSharp(){
		assertEquals(Music.intToNoteNameSharp(-2), "A#", "Checking sharp is found");
		assertEquals(Music.intToNoteNameSharp(-4), "G#", "Checking sharp is found");
		assertEquals(Music.intToNoteNameSharp(1), "C#", "Checking sharp is found");
		assertEquals(Music.intToNoteNameSharp(13), "C#", "Checking sharp is found in higher ocvate");
		assertEquals(Music.intToNoteNameSharp(-14), "A#", "Checking sharp is found in lower ocvate");
	}
	
	@Test
	public void intToNote(){
		assertEquals(Music.intToNote(0, false), "C4", "Checking flat is found");
		assertEquals(Music.intToNote(12, false), "C5", "Checking flat is found");
		assertEquals(Music.intToNote(-12, false), "C3", "Checking flat is found");
		
		assertEquals(Music.intToNote(1, false), "C#4", "Checking flat is found");
		assertEquals(Music.intToNote(11, false), "B4", "Checking flat is found");
		assertEquals(Music.intToNote(23, false), "B5", "Checking flat is found");
		
		assertEquals(Music.intToNote(-1, false), "B3", "Checking flat is found");
		assertEquals(Music.intToNote(-11, false), "C#3", "Checking flat is found");
		assertEquals(Music.intToNote(-12, false), "C3", "Checking flat is found");
		assertEquals(Music.intToNote(-13, false), "B2", "Checking flat is found");
	}
	
	@Test
	public void intToNoteFlat(){
		assertEquals(Music.intToNoteFlat(-2), "Bb3", "Checking flat is found");
		assertEquals(Music.intToNoteFlat(-4), "Ab3", "Checking flat is found");
		assertEquals(Music.intToNoteFlat(1), "Db4", "Checking flat is found");
		assertEquals(Music.intToNoteFlat(13), "Db5", "Checking flat is found in higher ocvate");
		assertEquals(Music.intToNoteFlat(-14), "Bb2", "Checking flat is found in lower ocvate");
	}
	
	@Test
	public void intToNoteSharp(){
		assertEquals(Music.intToNoteSharp(-2), "A#3", "Checking sharp is found");
		assertEquals(Music.intToNoteSharp(-4), "G#3", "Checking sharp is found");
		assertEquals(Music.intToNoteSharp(1), "C#4", "Checking sharp is found");
		assertEquals(Music.intToNoteSharp(13), "C#5", "Checking sharp is found in higher ocvate");
		assertEquals(Music.intToNoteSharp(-14), "A#2", "Checking sharp is found in lower ocvate");
	}
	
	@Test
	public void toOctave(){
		assertEquals(Music.toOctave(0), 4, "Checking correct ocvate is found");
		assertEquals(Music.toOctave(4), 4, "Checking correct ocvate is found");
		assertEquals(Music.toOctave(12), 5, "Checking correct ocvate is found");
		assertEquals(Music.toOctave(23), 5, "Checking correct ocvate is found");
		assertEquals(Music.toOctave(24), 6, "Checking correct ocvate is found");
		
		assertEquals(Music.toOctave(-1), 3, "Checking correct ocvate is found");
		assertEquals(Music.toOctave(-4), 3, "Checking correct ocvate is found");
		assertEquals(Music.toOctave(-12), 3, "Checking correct ocvate is found");
		assertEquals(Music.toOctave(-23), 2, "Checking correct ocvate is found");
		assertEquals(Music.toOctave(-24), 2, "Checking correct ocvate is found");
		assertEquals(Music.toOctave(-25), 1, "Checking correct ocvate is found");
	}
	
	@AfterEach
	public void end(){}
	
}
