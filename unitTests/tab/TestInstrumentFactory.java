package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.Music;

public class TestInstrumentFactory{

	@BeforeEach
	public void setup(){}
	
	@Test
	public void guitarStandard(){
		Tab guitar = InstrumentFactory.guitarStandard();
		assertEquals(6, guitar.getStrings().size(), "Checking guitar has 6 strings");
		assertEquals(Music.createNote(Music.E, 4), guitar.getRootNote(0), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.B, 3), guitar.getRootNote(1), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.G, 3), guitar.getRootNote(2), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.D, 3), guitar.getRootNote(3), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.A, 2), guitar.getRootNote(4), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.E, 2), guitar.getRootNote(5), "Checking guitar correct string tuning");
	}
	
	@Test
	public void guitarTuned(){
		Tab guitar = InstrumentFactory.guitarTuned(1);
		assertEquals(6, guitar.getStrings().size(), "Checking guitar has 6 strings");
		assertEquals(Music.createNote(Music.F, 4), guitar.getRootNote(0), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.C, 4), guitar.getRootNote(1), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.G_SHARP, 3), guitar.getRootNote(2), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.D_SHARP, 3), guitar.getRootNote(3), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.A_SHARP, 2), guitar.getRootNote(4), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.F, 2), guitar.getRootNote(5), "Checking guitar correct string tuning");
	}
	
	@Test
	public void guitarEbStandard(){
		Tab guitar = InstrumentFactory.guitarEbStandard();
		assertEquals(6, guitar.getStrings().size(), "Checking guitar has 6 strings");
		assertEquals(Music.createNote(Music.E_FLAT, 4), guitar.getRootNote(0), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.B_FLAT, 3), guitar.getRootNote(1), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.G_FLAT, 3), guitar.getRootNote(2), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.D_FLAT, 3), guitar.getRootNote(3), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.A_FLAT, 2), guitar.getRootNote(4), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.E_FLAT, 2), guitar.getRootNote(5), "Checking guitar correct string tuning");
	}
	
	@Test
	public void guitarDStandard(){
		Tab guitar = InstrumentFactory.guitarDStandard();
		assertEquals(6, guitar.getStrings().size(), "Checking guitar has 6 strings");
		assertEquals(Music.createNote(Music.D, 4), guitar.getRootNote(0), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.A, 3), guitar.getRootNote(1), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.F, 3), guitar.getRootNote(2), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.C, 3), guitar.getRootNote(3), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.G, 2), guitar.getRootNote(4), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.D, 2), guitar.getRootNote(5), "Checking guitar correct string tuning");
	}
	
	@Test
	public void guitarDropD(){
		Tab guitar = InstrumentFactory.guitarDropD();
		assertEquals(6, guitar.getStrings().size(), "Checking guitar has 6 strings");
		assertEquals(Music.createNote(Music.E, 4), guitar.getRootNote(0), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.B, 3), guitar.getRootNote(1), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.G, 3), guitar.getRootNote(2), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.D, 3), guitar.getRootNote(3), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.A, 2), guitar.getRootNote(4), "Checking guitar correct string tuning");
		assertEquals(Music.createNote(Music.D, 2), guitar.getRootNote(5), "Checking guitar correct string tuning");
	}
	
	@Test
	public void bassStandard(){
		Tab bass = InstrumentFactory.bassStandard();
		assertEquals(4, bass.getStrings().size(), "Checking bass has 4 strings");
		assertEquals(Music.createNote(Music.G, 2), bass.getRootNote(0), "Checking bass correct string tuning");
		assertEquals(Music.createNote(Music.D, 2), bass.getRootNote(1), "Checking bass correct string tuning");
		assertEquals(Music.createNote(Music.A, 1), bass.getRootNote(2), "Checking bass correct string tuning");
		assertEquals(Music.createNote(Music.E, 1), bass.getRootNote(3), "Checking bass correct string tuning");
	}
	
	@Test
	public void ukuleleStandard(){
		Tab uke = InstrumentFactory.ukuleleStandard();
		assertEquals(4, uke.getStrings().size(), "Checking ukulele has 4 strings");
		assertEquals(Music.createNote(Music.A, 4), uke.getRootNote(0), "Checking ukulele correct string tuning");
		assertEquals(Music.createNote(Music.E, 4), uke.getRootNote(1), "Checking ukulele correct string tuning");
		assertEquals(Music.createNote(Music.C, 4), uke.getRootNote(2), "Checking ukulele correct string tuning");
		assertEquals(Music.createNote(Music.G, 4), uke.getRootNote(3), "Checking ukulele correct string tuning");
	}
	
	@AfterEach
	public void end(){}
	
}
