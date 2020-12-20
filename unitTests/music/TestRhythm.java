package music;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.testUtils.UtilsTest;

public class TestRhythm{
	
	private Rhythm whole;
	private Rhythm half;
	private Rhythm third;
	private Rhythm threeHalf;
	private Rhythm fives;
	private Rhythm simplify;
	
	@BeforeEach
	public void setup(){
		whole = new Rhythm(1, 1);
		half = new Rhythm(1, 2);
		third = new Rhythm(1, 3);
		threeHalf = new Rhythm(3, 2);
		fives = new Rhythm(2, 5);
		simplify = new Rhythm(10, 4);
	}
	
	@Test
	public void copy(){
		Rhythm copy = half.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(half));
		assertTrue("Checking copy is not the same as the source object", copy != half);
	}
	
	@Test
	public void getDuration(){
		assertEquals(1, whole.getDuration(), "Checking correct initialized duration");
		assertEquals(1, half.getDuration(), "Checking correct initialized duration");
		assertEquals(1, third.getDuration(), "Checking correct initialized duration");
		assertEquals(3, threeHalf.getDuration(), "Checking correct initialized duration");
		assertEquals(2, fives.getDuration(), "Checking correct initialized duration");
		
		assertEquals(5, simplify.getDuration(), "Checking simplified initialized duration");
	}
	
	@Test
	public void setDuration(){
		fives.setDuration(4);
		assertEquals(4, fives.getDuration(), "Checking correct set duration");
	}
	
	@Test
	public void getUnit(){
		assertEquals(1, whole.getUnit(), "Checking correct initialized unit");
		assertEquals(2, half.getUnit(), "Checking correct initialized unit");
		assertEquals(3, third.getUnit(), "Checking correct initialized unit");
		assertEquals(2, threeHalf.getUnit(), "Checking correct initialized unit");
		assertEquals(5, fives.getUnit(), "Checking correct initialized unit");
		
		assertEquals(2, simplify.getUnit(), "Checking simplified initialized unit");
	}
	
	@Test
	public void setUnit(){
		fives.setUnit(3);
		assertEquals(3, fives.getUnit(), "Checking correct set unit");
	}
	
	@Test
	public void getLength(){
		assertEquals(1, whole.getLength(), UtilsTest.DELTA, "Checking correct length");
		assertEquals(0.5, half.getLength(), UtilsTest.DELTA, "Checking correct length");
		assertEquals(0.3333333333333333, third.getLength(), UtilsTest.DELTA, "Checking correct length");
		assertEquals(1.5, threeHalf.getLength(), UtilsTest.DELTA, "Checking correct length");
		assertEquals(0.4, fives.getLength(), UtilsTest.DELTA, "Checking correct length");
	}
	
	@Test
	public void simplify(){
		whole.setDuration(2);
		whole.setUnit(4);
		whole.simplify();
		assertEquals(1, whole.getDuration(), "Checking duration is simplified");
		assertEquals(2, whole.getUnit(), "Checking unit is simplified");
		
		whole.setDuration(12);
		whole.setUnit(4);
		whole.simplify();
		assertEquals(3, whole.getDuration(), "Checking duration is simplified");
		assertEquals(1, whole.getUnit(), "Checking unit is simplified");
		
		whole.setDuration(7);
		whole.setUnit(5);
		whole.simplify();
		assertEquals(7, whole.getDuration(), "Checking duration is unchanged");
		assertEquals(5, whole.getUnit(), "Checking unit is unchanged");
		
		whole.setDuration(1);
		whole.setUnit(0);
		whole.simplify();
		assertEquals(1, whole.getDuration(), "Checking duration is unchanged with unit at zero");
		assertEquals(0, whole.getUnit(), "Checking unit is unchanged");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("1 2 \n3 4 \n5 7\n");
		assertTrue("Checking load successful", (whole.load(scan)));
		assertEquals(1, whole.getDuration(), "Checking duration loaded");
		assertEquals(2, whole.getUnit(), "Checking unit loaded");
		
		assertTrue("Checking load successful", (whole.load(scan)));
		assertEquals(3, whole.getDuration(), "Checking duration loaded");
		assertEquals(4, whole.getUnit(), "Checking unit loaded");
		
		assertTrue("Checking load successful", (whole.load(scan)));
		assertEquals(5, whole.getDuration(), "Checking duration loaded");
		assertEquals(7, whole.getUnit(), "Checking unit loaded");
		
		assertFalse("Checking load fails with nothing more to load", (whole.load(scan)));
		
		scan.close();
	}
	
	@Test
	public void save(){
		assertEquals("1 2 \n", UtilsTest.testSave(half), "Checking saved values are correct");
		assertEquals("3 2 \n", UtilsTest.testSave(threeHalf), "Checking saved values are correct");
		assertEquals("2 5 \n", UtilsTest.testSave(fives), "Checking saved values are correct");
	}
	
	@Test
	public void equals(){
		Rhythm r = new Rhythm(1, 2);
		assertFalse("Checking objects are not the same object", r == half);
		assertTrue("Checking objects are equal", r.equals(half));
		
		r.setDuration(10);
		assertFalse("Checking objects are not equal", r.equals(half));
		
		r.setDuration(1);
		assertTrue("Checking objects are equal", r.equals(half));
		r.setUnit(20);
		assertFalse("Checking objects are not equal", r.equals(half));
	}
	
	@AfterEach
	public void end(){}
	
}
