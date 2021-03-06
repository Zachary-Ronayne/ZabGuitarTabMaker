package tab.symbol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import music.Rhythm;
import util.testUtils.UtilsTest;

public class TestTabDeadNote{
	
	private TabDeadNote note;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		note = new TabDeadNote();
	}
	
	@Test
	public void constructor(){
		assertEquals(new TabModifier(), note.getModifier(), "Checking modifier initialized");
	}

	@Test
	public void copy(){
		TabDeadNote copy = note.copy();
		assertTrue(copy.equals(note), "Checking copy is equal to the source object");
		assertTrue(copy != note, "Checking copy is not the same as the source object");
	}
	
	@Test
	public void convertToRhythm(){
		assertEquals(note, note.convertToRhythm(new Rhythm(1, 1)), "Checking converted version is the same");
	}

	@Test
	public void removeRhythm(){
		assertEquals(note, note.removeRhythm(), "Checking removed version is the same");
	}
	
	@Test
	public void usesRhythm(){
		assertFalse(note.usesRhythm(), "Checking note doesn't use rhythm");
	}
	
	@Test
	public void updateOnNewString(){
		assertEquals(note, note.movingToNewString(null, null), "Checking note is equal to itself on a new string");
	}
	
	@Test
	public void getSymbol(){
		assertEquals("X", note.getSymbol(null), "Checking symbol is obtained");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("\n");
		assertTrue(note.load(scan), "Checking load successful");
		assertEquals(new TabModifier(), note.getModifier(), "Checking modifier unchanged after load");

		assertFalse(note.load(scan), "Checking load fails without enough data");
	}
	
	@Test
	public void save(){
		assertEquals("\n", UtilsTest.testSave(note), "Checking note saved correctly");
		
		note = new TabDeadNote();
		assertEquals("\n", UtilsTest.testSave(note), "Checking note saved correctly");
	}
	
	@Test
	public void testToString(){
		assertEquals("[TabDeadNote, X]", note.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
