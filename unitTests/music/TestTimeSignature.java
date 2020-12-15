package music;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.testUtils.UtilsTest;

public class TestTimeSignature{

	private TimeSignature four4;
	private TimeSignature five8;
	private TimeSignature five4;
	private TimeSignature three2;
	private TimeSignature six4;
	private TimeSignature seven11;
	
	@BeforeEach
	public void setup(){
		four4 = new TimeSignature(4, 4);
		five8 = new TimeSignature(5, 8);
		five4 = new TimeSignature(5, 4);
		three2 = new TimeSignature(3, 2);
		six4 = new TimeSignature(6, 4);
		seven11 = new TimeSignature(7, 11);
	}
	
	@Test
	public void getUpper(){
		assertEquals(4, four4.getUpper(), "Checking upper initialized");
		assertEquals(5, five8.getUpper(), "Checking upper initialized");
		assertEquals(5, five4.getUpper(), "Checking upper initialized");
		assertEquals(3, three2.getUpper(), "Checking upper initialized");
	}
	
	@Test
	public void setUpper(){
		four4.setUpper(2);
		assertEquals(2, four4.getUpper(), "Checking upper set");
	}
	
	@Test
	public void getLower(){
		assertEquals(4, four4.getLower(), "Checking lower initialized");
		assertEquals(8, five8.getLower(), "Checking lower initialized");
		assertEquals(4, five4.getLower(), "Checking lower initialized");
		assertEquals(2, three2.getLower(), "Checking lower initialized");
	}
	
	@Test
	public void setLower(){
		four4.setLower(2);
		assertEquals(2, four4.getLower(), "Checking lower set");
	}
	
	@Test
	public void getRatio(){
		assertEquals(1, four4.getRatio(), UtilsTest.DELTA, "Checking correct ratio");
		assertEquals(0.625, five8.getRatio(), UtilsTest.DELTA, "Checking correct ratio");
		assertEquals(1.25, five4.getRatio(), UtilsTest.DELTA, "Checking correct ratio");
		assertEquals(1.5, three2.getRatio(), UtilsTest.DELTA, "Checking correct ratio");
		assertEquals(1.5, six4.getRatio(), UtilsTest.DELTA, "Checking correct ratio");
		assertEquals(0.63636363636363636, seven11.getRatio(), UtilsTest.DELTA, "Checking correct ratio");
	}
	
	@Test
	public void symbol(){
		assertEquals("4/4", four4.symbol(), "Checking correct symbol");
		assertEquals("5/8", five8.symbol(), "Checking correct symbol");
		assertEquals("5/4", five4.symbol(), "Checking correct symbol");
		assertEquals("3/2", three2.symbol(), "Checking correct symbol");
		assertEquals("6/4", six4.symbol(), "Checking correct symbol");
		assertEquals("7/11", seven11.symbol(), "Checking correct symbol");
	}
	
	@Test
	public void quantize(){
		assertEquals(0, four4.quantize(0.1, 4), UtilsTest.DELTA, "Checking quantized note to 4/4");
		assertEquals(0.25, four4.quantize(0.2, 4), UtilsTest.DELTA, "Checking quantized note to 4/4");
		assertEquals(1, four4.quantize(1.1, 4), UtilsTest.DELTA, "Checking quantized note to 4/4");
		assertEquals(1, four4.quantize(0.9, 4), UtilsTest.DELTA, "Checking quantized note to 4/4");
		assertEquals(3, four4.quantize(2.9, 4), UtilsTest.DELTA, "Checking quantized note to 4/4");
		assertEquals(2.25, four4.quantize(2.21, 4), UtilsTest.DELTA, "Checking quantized note to 4/4");
		
		assertEquals(2, four4.quantize(2.24, 2), UtilsTest.DELTA, "Checking quantized half note to 4/4");
		assertEquals(2.5, four4.quantize(2.26, 2), UtilsTest.DELTA, "Checking quantized half note to 4/4");
		
		assertEquals(2.25, four4.quantize(2.26, 8), UtilsTest.DELTA, "Checking quantized eighth note to 4/4");
		assertEquals(2.125, four4.quantize(2.126, 8), UtilsTest.DELTA, "Checking quantized eighth note to 4/4");
		assertEquals(2.125, four4.quantize(2.1874, 8), UtilsTest.DELTA, "Checking quantized eighth note to 4/4");
		assertEquals(2.25, four4.quantize(2.1876, 8), UtilsTest.DELTA, "Checking quantized eighth note to 4/4");
		
		assertEquals(2.33333333333333, four4.quantize(2.34, 6), UtilsTest.DELTA, "Checking quantized dotted quarter note to 4/4");
		assertEquals(2.16666666666667, four4.quantize(2.15, 6), UtilsTest.DELTA, "Checking quantized dotted quarter note to 4/4");
		
		assertEquals(2.4, four4.quantize(2.34, 5), UtilsTest.DELTA, "Checking quantized fifth note to 4/4");
		assertEquals(2.2, four4.quantize(2.15, 5), UtilsTest.DELTA, "Checking quantized fifth note to 4/4");
		
		
		assertEquals(0, five8.quantize(0.05, 8), UtilsTest.DELTA, "Checking quantized eighth note to 5/8");
		assertEquals(0.6, five8.quantize(0.62, 8), UtilsTest.DELTA, "Checking quantized eighth note to 5/8");
		assertEquals(1, five8.quantize(0.9, 8), UtilsTest.DELTA, "Checking quantized eighth note to 5/8");
		assertEquals(0.8, five8.quantize(0.83, 8), UtilsTest.DELTA, "Checking quantized eighth note to 5/8");
		
		assertEquals(0.8, five8.quantize(0.83, 4), UtilsTest.DELTA, "Checking quantized quarter note to 5/8");
		assertEquals(0.8, five8.quantize(0.99, 4), UtilsTest.DELTA, "Checking quantized quarter note to 5/8");
		assertEquals(1.2, five8.quantize(1.01, 4), UtilsTest.DELTA, "Checking quantized quarter note to 5/8");
		
		
		assertEquals(1, five4.quantize(1.01, 4), UtilsTest.DELTA, "Checking quantized quarter note to 5/4");
		assertEquals(0.6, five4.quantize(0.61, 4), UtilsTest.DELTA, "Checking quantized quarter note to 5/4");
		
		
		assertEquals(0.33333333333333, three2.quantize(0.3, 4), UtilsTest.DELTA, "Checking quantized quarter note to 3/2");
		assertEquals(0.33333333333333, six4.quantize(0.3, 4), UtilsTest.DELTA, "Checking quantized quarter note to 6/4");
		assertEquals(0.5, three2.quantize(0.51, 4), UtilsTest.DELTA, "Checking quantized quarter note to 3/2");
		assertEquals(0.5, six4.quantize(0.51, 4), UtilsTest.DELTA, "Checking quantized quarter note to 6/4");
		
		assertEquals(0.285714286, seven11.quantize(0.28, 11), UtilsTest.DELTA, "Checking quantized 11th note to 7/11");
		assertEquals(0.571428571, seven11.quantize(0.57, 5.5), UtilsTest.DELTA, "Checking quantized 5.5th note to 7/11");
	}
	
	@Test
	public void guessRhythm(){
		guessRhythmHelper(1, 1, 1, four4);
		guessRhythmHelper(2, 1, 2, four4);
		guessRhythmHelper(2, 1, 2.01, four4);
		guessRhythmHelper(3, 1, 2.99, four4);
		guessRhythmHelper(3, 2, 1.5, four4);
		guessRhythmHelper(3, 2, 1.51, four4);
		guessRhythmHelper(5, 4, 1.26, four4);
		guessRhythmHelper(9, 8, 1.126, four4);
		guessRhythmHelper(17, 16, 1.0626, four4);
		
		guessRhythmHelper(5, 8, 1, five8);
		guessRhythmHelper(5, 8, 1.01, five8);
		guessRhythmHelper(1, 8, 0.21, five8);
	}
	
	/**
	 * Helper method for testing {@link #guessRhythm()}
	 * @param expectDuration The expected duration value for the guessed rhythm
	 * @param expectUnit The expected unit value for the guessed rhythm
	 * @param duration The duration to guess from
	 * @param sig The {@link TimeSignature} to use
	 */
	private void guessRhythmHelper(int expectDuration, int expectUnit, double duration, TimeSignature sig){
		Rhythm guess = sig.guessRhythm(duration);
		assertTrue("Checking duration guessing " + expectUnit + " unit notes in " + sig.symbol() + ", "
				+ "expected duration: " + expectDuration + " unit: " + expectUnit + ", "
				+ " but got duration: " + guess.getDuration() + " unit: " + guess.getUnit(),
				expectDuration == guess.getDuration() && expectUnit == guess.getUnit());
	}
	
	@Test
	public void equals(){
		TimeSignature new4 = new TimeSignature(4, 4);
		assertTrue("Checking identical time signatures are equal", four4.equals(new4));
		assertFalse("Checking identical time signatures are not the same object", four4 == new4);
		
		assertFalse("Checking time signatures with different upper are not equal", four4.equals(five4));
		assertFalse("Checking time signatures with different lower are not equal", five8.equals(five4));
	}
	
	@AfterEach
	public void end(){}
	
}
