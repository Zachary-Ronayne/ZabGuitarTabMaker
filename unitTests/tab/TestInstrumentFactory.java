package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import music.Music;

public class TestInstrumentFactory{

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){}
	
	@Test
	public void guitarStandard(){
		Tab guitar = InstrumentFactory.guitarStandard();
		assertEquals(6, guitar.getStrings().size(), "Checking guitar has 6 strings");
		assertRootNote(Music.E, 4, guitar, 0, "guitar");
		assertRootNote(Music.B, 3, guitar, 1, "guitar");
		assertRootNote(Music.G, 3, guitar, 2, "guitar");
		assertRootNote(Music.D, 3, guitar, 3, "guitar");
		assertRootNote(Music.A, 2, guitar, 4, "guitar");
		assertRootNote(Music.E, 2, guitar, 5, "guitar");
	}
	
	@Test
	public void guitarTuned(){
		Tab guitar = InstrumentFactory.guitarTuned(1);
		assertEquals(6, guitar.getStrings().size(), "Checking guitar has 6 strings");
		assertRootNote(Music.F, 4, guitar, 0, "guitar");
		assertRootNote(Music.C, 4, guitar, 1, "guitar");
		assertRootNote(Music.G_SHARP, 3, guitar, 2, "guitar");
		assertRootNote(Music.D_SHARP, 3, guitar, 3, "guitar");
		assertRootNote(Music.A_SHARP, 2, guitar, 4, "guitar");
		assertRootNote(Music.F, 2, guitar, 5, "guitar");
	}
	
	@Test
	public void guitarEbStandard(){
		Tab guitar = InstrumentFactory.guitarEbStandard();
		assertEquals(6, guitar.getStrings().size(), "Checking guitar has 6 strings");
		assertRootNote(Music.E_FLAT, 4, guitar, 0, "guitar");
		assertRootNote(Music.B_FLAT, 3, guitar, 1, "guitar");
		assertRootNote(Music.G_FLAT, 3, guitar, 2, "guitar");
		assertRootNote(Music.D_FLAT, 3, guitar, 3, "guitar");
		assertRootNote(Music.A_FLAT, 2, guitar, 4, "guitar");
		assertRootNote(Music.E_FLAT, 2, guitar, 5, "guitar");
	}
	
	@Test
	public void guitarDStandard(){
		Tab guitar = InstrumentFactory.guitarDStandard();
		assertEquals(6, guitar.getStrings().size(), "Checking guitar has 6 strings");
		assertRootNote(Music.D, 4, guitar, 0, "guitar");
		assertRootNote(Music.A, 3, guitar, 1, "guitar");
		assertRootNote(Music.F, 3, guitar, 2, "guitar");
		assertRootNote(Music.C, 3, guitar, 3, "guitar");
		assertRootNote(Music.G, 2, guitar, 4, "guitar");
		assertRootNote(Music.D, 2, guitar, 5, "guitar");
	}
	
	@Test
	public void guitarDropD(){
		Tab guitar = InstrumentFactory.guitarDropD();
		assertEquals(6, guitar.getStrings().size(), "Checking guitar has 6 strings");
		assertRootNote(Music.E, 4, guitar, 0, "guitar");
		assertRootNote(Music.B, 3, guitar, 1, "guitar");
		assertRootNote(Music.G, 3, guitar, 2, "guitar");
		assertRootNote(Music.D, 3, guitar, 3, "guitar");
		assertRootNote(Music.A, 2, guitar, 4, "guitar");
		assertRootNote(Music.D, 2, guitar, 5, "guitar");
	}
	
	@Test
	public void bassStandard(){
		Tab bass = InstrumentFactory.bassStandard();
		assertEquals(4, bass.getStrings().size(), "Checking bass has 4 strings");
		assertRootNote(Music.G, 2, bass, 0, "bass");
		assertRootNote(Music.D, 2, bass, 1, "bass");
		assertRootNote(Music.A, 1, bass, 2, "bass");
		assertRootNote(Music.E, 1, bass, 3, "bass");
	}
	
	@Test
	public void ukuleleStandard(){
		Tab uke = InstrumentFactory.ukuleleStandard();
		assertEquals(4, uke.getStrings().size(), "Checking ukulele has 4 strings");
		assertRootNote(Music.A, 4, uke, 0, "ukulele");
		assertRootNote(Music.E, 4, uke, 1, "ukulele");
		assertRootNote(Music.C, 4, uke, 2, "ukulele");
		assertRootNote(Music.G, 4, uke, 3, "ukulele");
	}
	
	@AfterEach
	public void end(){}
	
	/**
	 * Utility method for testing that the root note of a {@link TabString} on a {@link Tab} is correct
	 * @param note The note name defined in {@link Music}
	 * @param octave The octave number of the note
	 * @param instrument The tab to check
	 * @param stringIndex The index of the string to check
	 * @param name The name of the instrument being checked, only here for output purposes
	 */
	public static void assertRootNote(String note, int octave, Tab instrument, int stringIndex, String name){
		assertEquals(Music.createNote(note, octave), instrument.getRootNote(stringIndex), "Checking instrument<" + name + "> correct string tuning");
	}
	
}
