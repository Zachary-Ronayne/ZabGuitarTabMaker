package music;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import util.testUtils.UtilsTest;

public class TestRhythm{
	
	private Rhythm whole;
	private Rhythm half;
	private Rhythm third;
	private Rhythm threeHalf;
	private Rhythm fives;
	private Rhythm simplify;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
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
	public void constructor(){
		Rhythm r = new Rhythm(-1, -1);
		assertEquals(0, r.getDuration(), "Checking duration set to 0 on negative value");
		assertEquals(1, r.getUnit(), "Checking unit set to 1 on negative value");

		r = new Rhythm(-1, 0);
		assertEquals(1, r.getUnit(), "Checking unit set to 1 on zero value");
	}
	
	@Test
	public void copy(){
		Rhythm copy = half.copy();
		assertTrue(copy.equals(half), "Checking copy is equal to the source object");
		assertTrue(copy != half, "Checking copy is not the same as the source object");
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
	public void getUnit(){
		assertEquals(1, whole.getUnit(), "Checking correct initialized unit");
		assertEquals(2, half.getUnit(), "Checking correct initialized unit");
		assertEquals(3, third.getUnit(), "Checking correct initialized unit");
		assertEquals(2, threeHalf.getUnit(), "Checking correct initialized unit");
		assertEquals(5, fives.getUnit(), "Checking correct initialized unit");
		
		assertEquals(2, simplify.getUnit(), "Checking simplified initialized unit");
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
		Rhythm r = new Rhythm(2, 4);
		assertEquals(1, r.getDuration(), "Checking duration is simplified");
		assertEquals(2, r.getUnit(), "Checking unit is simplified");

		r = new Rhythm(12, 4);
		assertEquals(3, r.getDuration(), "Checking duration is simplified");
		assertEquals(1, r.getUnit(), "Checking unit is simplified");

		r = new Rhythm(7, 5);
		assertEquals(7, r.getDuration(), "Checking duration is unchanged");
		assertEquals(5, r.getUnit(), "Checking unit is unchanged");

		r = new Rhythm(1, 0);
		assertEquals(1, r.getDuration(), "Checking duration is unchanged with unit set to zero");
		assertEquals(1, r.getUnit(), "Checking unit is unchanged");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("1 2 \n3 4 \n5 7\n1 2");
		assertTrue(whole.load(scan), "Checking load successful");
		assertEquals(1, whole.getDuration(), "Checking duration loaded");
		assertEquals(2, whole.getUnit(), "Checking unit loaded");
		
		assertTrue(whole.load(scan), "Checking load successful");
		assertEquals(3, whole.getDuration(), "Checking duration loaded");
		assertEquals(4, whole.getUnit(), "Checking unit loaded");
		
		assertTrue(whole.load(scan), "Checking load successful");
		assertEquals(5, whole.getDuration(), "Checking duration loaded");
		assertEquals(7, whole.getUnit(), "Checking unit loaded");
		
		assertFalse(whole.load(scan), "Checking load fails with not enough to load");
		assertFalse(whole.load(scan), "Checking load fails with nothing more to load");
		
		scan.close();
	}
	
	@Test
	public void save(){
		assertFalse(half.save(null), "Checking save fails with invalid writer");
		
		assertEquals("1 2 \n", UtilsTest.testSave(half), "Checking saved values are correct");
		assertEquals("3 2 \n", UtilsTest.testSave(threeHalf), "Checking saved values are correct");
		assertEquals("2 5 \n", UtilsTest.testSave(fives), "Checking saved values are correct");
	}
	
	@Test
	public void equals(){
		assertTrue(half.equals(half), "Checking rhythm equals itself");
		assertFalse(half.equals(null), "Checking rhythm does not equal null");
		
		Rhythm r = new Rhythm(1, 2);
		assertFalse(r == half, "Checking objects are not the same object");
		assertTrue(r.equals(half), "Checking objects are equal");
		
		r = new Rhythm(10, 2);
		assertFalse(r.equals(half), "Checking objects are not equal");

		r = new Rhythm(1, 2);
		assertTrue(r.equals(half), "Checking objects are equal");
		
		r = new Rhythm(1, 20);
		assertFalse(r.equals(half), "Checking objects are not equal");
	}
	
	@Test
	public void testToString(){
		assertEquals("[Rhythm: 3 2 notes]", threeHalf.toString(), "Checking correct string");
	}
	
	
	@AfterEach
	public void end(){}
	
}
