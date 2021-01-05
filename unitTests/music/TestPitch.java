package music;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.testUtils.UtilsTest;

public class TestPitch{
	
	private Pitch fSharp3;
	private Pitch c4;
	private Pitch c5;
	
	@BeforeEach
	public void setup(){
		fSharp3 = new Pitch(-6);
		c4 = new Pitch(0);
		c5 = new Pitch(12);
	}

	@Test
	public void copy(){
		Pitch copy = c4.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(c4));
		assertTrue("Checking copy is not the same as the source object", copy != c4);
	}
	
	@Test
	public void getNote(){
		assertEquals(-6, fSharp3.getNote(), "Checking correct note is initialized");
		assertEquals(0, c4.getNote(), "Checking correct note is initialized");
		assertEquals(12, c5.getNote(), "Checking correct note is initialized");
	}
	
	@Test
	public void setNote(){
		c5.setNote(5);
		assertEquals(5, c5.getNote(), "Checking correct note is set");
	}
	
	@Test
	public void addNote(){
		c5.setNote(0);
		c5.addNote(2);
		assertEquals(2, c5.getNote(), "Checking correct note is set after adding");
	}
	
	@Test
	public void tune(){
		c5.setNote(0);
		c5.tune(4);
		assertEquals(4, c5.getNote(), "Checking correct note is set after tuning");
		
		c5.tune(-3);
		assertEquals(1, c5.getNote(), "Checking correct note is set after tuning");
		
		c5.tune(-10);
		assertEquals(-9, c5.getNote(), "Checking correct note is set after tuning");
	}
	
	@Test
	public void getPitchName(){
		assertEquals(Music.F_SHARP + "3", fSharp3.getPitchName(), "Checking correct note name is found");
		assertEquals(Music.G_FLAT + "3", fSharp3.getPitchName(true), "Checking correct note name is found as flat");
		assertEquals(Music.F_SHARP + "3", fSharp3.getPitchName(false), "Checking correct note name is found as sharp");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("0 \n3 \n5 \n");
		assertTrue("Checking loading succeeded", c5.load(scan));
		assertEquals(0, c5.getNote(), "Checking correct note loaded");
		
		assertTrue("Checking loading succeeded", c5.load(scan));
		assertEquals(3, c5.getNote(), "Checking correct note loaded");
		
		assertTrue("Checking loading succeeded", c5.load(scan));
		assertEquals(5, c5.getNote(), "Checking correct note loaded");
		
		assertFalse("Checking loading fails with nothing more to load", c5.load(scan));
	}
	
	@Test
	public void save(){
		assertEquals("0 \n", UtilsTest.testSave(c4), "Checking saved value is correct");
		assertEquals("12 \n", UtilsTest.testSave(c5), "Checking saved value is correct");
		assertEquals("-6 \n", UtilsTest.testSave(fSharp3), "Checking saved value is correct");
	}
	
	@Test
	public void equals(){
		Pitch c = new Pitch(0);
		assertFalse("Checking objects are not the same object", c == c4);
		assertTrue("Checking objects are equal", c.equals(c4));
		
		c.setNote(-1);
		assertFalse("Checking objects are not equal", c.equals(c4));
	}
	
	@Test
	public void testToString(){
		assertEquals("[Pitch: C4]", c4.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
