package music;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
	
	@BeforeEach
	public void setup(){
		whole = new Rhythm(1, 1);
		half = new Rhythm(1, 2);
		third = new Rhythm(1, 3);
		threeHalf = new Rhythm(3, 2);
		fives = new Rhythm(2, 5);
	}
	
	@Test
	public void getDuration(){
		assertEquals(1, whole.getDuration(), "Checking correct initilaized duration");
		assertEquals(1, half.getDuration(), "Checking correct initilaized duration");
		assertEquals(1, third.getDuration(), "Checking correct initilaized duration");
		assertEquals(3, threeHalf.getDuration(), "Checking correct initilaized duration");
		assertEquals(2, fives.getDuration(), "Checking correct initilaized duration");
	}
	
	@Test
	public void setDuration(){
		fives.setDuration(4);
		assertEquals(4, fives.getDuration(), "Checking correct set duration");
	}
	
	@Test
	public void getUnit(){
		assertEquals(1, whole.getUnit(), "Checking correct initilaized unit");
		assertEquals(2, half.getUnit(), "Checking correct initilaized unit");
		assertEquals(3, third.getUnit(), "Checking correct initilaized unit");
		assertEquals(2, threeHalf.getUnit(), "Checking correct initilaized unit");
		assertEquals(5, fives.getUnit(), "Checking correct initilaized unit");
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
	}
	
	@AfterEach
	public void end(){}
	
}
