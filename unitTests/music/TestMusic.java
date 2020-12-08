package music;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TestMusic{
	
	@BeforeEach
	public void setup(){}

	@Test
	public void intToNoteName(){
		assertEquals(Music.C, Music.intToNoteName(0, true), "Checking unmodified note is found");
		assertEquals(Music.B_FLAT, Music.intToNoteName(-2, true), "Checking flat is found");
		assertEquals(Music.A_SHARP, Music.intToNoteName(-2, false), "Checking sharp is found");
		
		assertEquals(Music.C, Music.intToNoteName(0, false), "Checking note is found");
		assertEquals(Music.C_SHARP, Music.intToNoteName(1, false), "Checking note is found");
		assertEquals(Music.D, Music.intToNoteName(2, false), "Checking note is found");
		assertEquals(Music.D_SHARP, Music.intToNoteName(3, false), "Checking note is found");
		assertEquals(Music.E, Music.intToNoteName(4, false), "Checking note is found");
		assertEquals(Music.F, Music.intToNoteName(5, false), "Checking note is found");
		assertEquals(Music.F_SHARP, Music.intToNoteName(6, false), "Checking note is found");
		assertEquals(Music.G, Music.intToNoteName(7, false), "Checking note is found");
		assertEquals(Music.G_SHARP, Music.intToNoteName(8, false), "Checking note is found");
		assertEquals(Music.A, Music.intToNoteName(9, false), "Checking note is found");
		assertEquals(Music.A_SHARP, Music.intToNoteName(10, false), "Checking note is found");
		assertEquals(Music.B, Music.intToNoteName(11, false), "Checking note is found");
		assertEquals(Music.C, Music.intToNoteName(12, false), "Checking note is found");
		
		assertEquals(Music.B, Music.intToNoteName(-1, false), "Checking note is found");
		assertEquals(Music.A_SHARP, Music.intToNoteName(-2, false), "Checking note is found");
		assertEquals(Music.A, Music.intToNoteName(-3, false), "Checking note is found");
		assertEquals(Music.G_SHARP, Music.intToNoteName(-4, false), "Checking note is found");
		assertEquals(Music.G, Music.intToNoteName(-5, false), "Checking note is found");
		assertEquals(Music.F_SHARP, Music.intToNoteName(-6, false), "Checking note is found");
		assertEquals(Music.F, Music.intToNoteName(-7, false), "Checking note is found");
		assertEquals(Music.E, Music.intToNoteName(-8, false), "Checking note is found");
		assertEquals(Music.D_SHARP, Music.intToNoteName(-9, false), "Checking note is found");
		assertEquals(Music.D, Music.intToNoteName(-10, false), "Checking note is found");
		assertEquals(Music.C_SHARP, Music.intToNoteName(-11, false), "Checking note is found");
		assertEquals(Music.C, Music.intToNoteName(-12, false), "Checking note is found");
	}

	@Test
	public void intToNoteNameFlat(){
		assertEquals(Music.B_FLAT, Music.intToNoteNameFlat(-2), "Checking flat is found");
		assertEquals(Music.A_FLAT, Music.intToNoteNameFlat(-4), "Checking flat is found");
		assertEquals(Music.D_FLAT, Music.intToNoteNameFlat(1), "Checking flat is found");
		assertEquals(Music.D_FLAT, Music.intToNoteNameFlat(13), "Checking flat is found in higher octave");
		assertEquals(Music.B_FLAT, Music.intToNoteNameFlat(-14), "Checking flat is found in lower octave");
	}
	
	@Test
	public void intToNoteNameSharp(){
		assertEquals(Music.A_SHARP, Music.intToNoteNameSharp(-2), "Checking sharp is found");
		assertEquals(Music.G_SHARP, Music.intToNoteNameSharp(-4), "Checking sharp is found");
		assertEquals(Music.C_SHARP, Music.intToNoteNameSharp(1), "Checking sharp is found");
		assertEquals(Music.C_SHARP, Music.intToNoteNameSharp(13), "Checking sharp is found in higher octave");
		assertEquals(Music.A_SHARP, Music.intToNoteNameSharp(-14), "Checking sharp is found in lower octave");
	}
	
	@Test
	public void intToNote(){
		assertEquals(Music.C + "4", Music.intToNote(0, false), "Checking flat is found");
		assertEquals(Music.C + "5", Music.intToNote(12, false), "Checking flat is found");
		assertEquals(Music.C + "3", Music.intToNote(-12, false), "Checking flat is found");
		
		assertEquals(Music.C_SHARP + "4", Music.intToNote(1, false), "Checking flat is found");
		assertEquals(Music.B + "4", Music.intToNote(11, false), "Checking flat is found");
		assertEquals(Music.B + "5", Music.intToNote(23, false), "Checking flat is found");
		
		assertEquals(Music.B + "3", Music.intToNote(-1, false), "Checking flat is found");
		assertEquals(Music.C_SHARP + "3", Music.intToNote(-11, false), "Checking flat is found");
		assertEquals(Music.C + "3", Music.intToNote(-12, false), "Checking flat is found");
		assertEquals(Music.B + "2", Music.intToNote(-13, false), "Checking flat is found");
	}
	
	@Test
	public void intToNoteFlat(){
		assertEquals(Music.B_FLAT + "3", Music.intToNoteFlat(-2), "Checking flat is found");
		assertEquals(Music.A_FLAT + "3", Music.intToNoteFlat(-4), "Checking flat is found");
		assertEquals(Music.D_FLAT + "4", Music.intToNoteFlat(1), "Checking flat is found");
		assertEquals(Music.D_FLAT + "5", Music.intToNoteFlat(13), "Checking flat is found in higher octave");
		assertEquals(Music.B_FLAT + "2", Music.intToNoteFlat(-14), "Checking flat is found in lower octave");
	}
	
	@Test
	public void intToNoteSharp(){
		assertEquals(Music.A_SHARP + "3", Music.intToNoteSharp(-2), "Checking sharp is found");
		assertEquals(Music.G_SHARP + "3", Music.intToNoteSharp(-4), "Checking sharp is found");
		assertEquals(Music.C_SHARP + "4", Music.intToNoteSharp(1), "Checking sharp is found");
		assertEquals(Music.C_SHARP + "5", Music.intToNoteSharp(13), "Checking sharp is found in higher octave");
		assertEquals(Music.A_SHARP + "2", Music.intToNoteSharp(-14), "Checking sharp is found in lower octave");
	}
	
	@Test
	public void toOctave(){
		assertEquals(4, Music.toOctave(0), "Checking correct octave is found");
		assertEquals(4, Music.toOctave(4), "Checking correct octave is found");
		assertEquals(5, Music.toOctave(12), "Checking correct octave is found");
		assertEquals(5, Music.toOctave(23), "Checking correct octave is found");
		assertEquals(6, Music.toOctave(24), "Checking correct octave is found");
		
		assertEquals(3, Music.toOctave(-1), "Checking correct octave is found");
		assertEquals(3, Music.toOctave(-4), "Checking correct octave is found");
		assertEquals(3, Music.toOctave(-12), "Checking correct octave is found");
		assertEquals(2, Music.toOctave(-23), "Checking correct octave is found");
		assertEquals(2, Music.toOctave(-24), "Checking correct octave is found");
		assertEquals(1, Music.toOctave(-25), "Checking correct octave is found");
	}
	
	@Test
	public void createNote(){
		assertEquals(0, Music.createNote(Music.C), "Checking correct note is found with createNote");
		assertEquals(2, Music.createNote(Music.D), "Checking correct note is found with createNote");
		assertEquals(11, Music.createNote(Music.B), "Checking correct note is found with createNote");
		assertThrows(IllegalArgumentException.class, new org.junit.jupiter.api.function.Executable(){
			@Override
			public void execute() throws Throwable{
				Music.createNote("fake note");
			}
		}, "Checking exception is thrown with invalid string");
		assertThrows(IllegalArgumentException.class, new org.junit.jupiter.api.function.Executable(){
			@Override
			public void execute() throws Throwable{
				Music.createNote(null);
			}
		}, "Checking exception is thrown with null string");
		
		
		assertEquals(0, Music.createNote(Music.C, 4), "Checking correct note is found using middle C octave");
		assertEquals(0, Music.createNote(Music.B_SHARP, 4), "Checking correct note is found using middle C octave");
		
		assertEquals(1, Music.createNote(Music.C_SHARP, 4), "Checking correct note is found using middle C octave");
		assertEquals(1, Music.createNote(Music.D_FLAT, 4), "Checking correct note is found using middle C octave");
		
		assertEquals(2, Music.createNote(Music.D, 4), "Checking correct note is found using middle C octave");
		
		assertEquals(3, Music.createNote(Music.D_SHARP, 4), "Checking correct note is found using middle C octave");
		assertEquals(3, Music.createNote(Music.E_FLAT, 4), "Checking correct note is found using middle C octave");
		
		assertEquals(4, Music.createNote(Music.E, 4), "Checking correct note is found using middle C octave");
		assertEquals(4, Music.createNote(Music.F_FLAT, 4), "Checking correct note is found using middle C octave");
		
		assertEquals(5, Music.createNote(Music.F, 4), "Checking correct note is found using middle C octave");
		assertEquals(5, Music.createNote(Music.E_SHARP, 4), "Checking correct note is found using middle C octave");
		
		assertEquals(6, Music.createNote(Music.F_SHARP, 4), "Checking correct note is found using middle C octave");
		assertEquals(6, Music.createNote(Music.G_FLAT, 4), "Checking correct note is found using middle C octave");
		
		assertEquals(7, Music.createNote(Music.G, 4), "Checking correct note is found using middle C octave");
		
		assertEquals(8, Music.createNote(Music.G_SHARP, 4), "Checking correct note is found using middle C octave");
		assertEquals(8, Music.createNote(Music.A_FLAT, 4), "Checking correct note is found using middle C octave");
		
		assertEquals(9, Music.createNote(Music.A, 4), "Checking correct note is found using middle C octave");
		
		assertEquals(10, Music.createNote(Music.A_SHARP, 4), "Checking correct note is found using middle C octave");
		assertEquals(10, Music.createNote(Music.B_FLAT, 4), "Checking correct note is found using middle C octave");
		
		assertEquals(11, Music.createNote(Music.B, 4), "Checking correct note is found using middle C octave");
		assertEquals(11, Music.createNote(Music.C_FLAT, 4), "Checking correct note is found using middle C octave");
		
		
		assertEquals(-12, Music.createNote(Music.C, 3), "Checking correct note is found with low octave");
		assertEquals(-10, Music.createNote(Music.D, 3), "Checking correct note is found with low octave");
		
		assertEquals(12, Music.createNote(Music.C, 5), "Checking correct note is found with high octave");
		assertEquals(18, Music.createNote(Music.F_SHARP, 5), "Checking correct note is found with high octave");
	}
	
	@AfterEach
	public void end(){}
	
}
