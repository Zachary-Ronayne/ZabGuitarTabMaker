package music;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import appUtils.ZabAppSettings;
import util.testUtils.UtilsTest;

public class TestTimeSignature{

	private TimeSignature four4;
	private TimeSignature five8;
	private TimeSignature five4;
	private TimeSignature three2;
	private TimeSignature six4;
	private TimeSignature seven11;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		four4 = new TimeSignature();
		five8 = new TimeSignature(5, 8);
		five4 = new TimeSignature(5, 4);
		three2 = new TimeSignature(3, 2);
		six4 = new TimeSignature(6, 4);
		seven11 = new TimeSignature(7, 11);
	}
	
	@Test
	public void copy(){
		TimeSignature copy = four4.copy();
		assertTrue(copy.equals(four4), "Checking copy is equal to the source object");
		assertTrue(copy != four4, "Checking copy is not the same as the source object");
	}
	
	@Test
	public void constructor(){
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new TimeSignature(0, 4);
			}
		}, "Checking error thrown on invalid upper value");
		
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new TimeSignature(4, 0);
			}
		}, "Checking error thrown on invalid lower value");
	}
	
	@Test
	public void getUpper(){
		assertEquals(4, four4.getUpper(), "Checking upper initialized");
		assertEquals(5, five8.getUpper(), "Checking upper initialized");
		assertEquals(5, five4.getUpper(), "Checking upper initialized");
		assertEquals(3, three2.getUpper(), "Checking upper initialized");
	}
	
	@Test
	public void getLower(){
		assertEquals(4, four4.getLower(), "Checking lower initialized");
		assertEquals(8, five8.getLower(), "Checking lower initialized");
		assertEquals(4, five4.getLower(), "Checking lower initialized");
		assertEquals(2, three2.getLower(), "Checking lower initialized");
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
	public void retime(){
		assertEquals(1, three2.retime(six4, 1), UtilsTest.DELTA, "Checking equal retiming with same ratio time signatures");
		assertEquals(1.5, three2.retime(six4, 1.5), UtilsTest.DELTA, "Checking equal retiming with same ratio time signatures");
		assertEquals(3.232, three2.retime(six4, 3.232), UtilsTest.DELTA, "Checking equal retiming with same ratio time signatures");

		assertEquals(1.6, five8.retime(four4, 1), UtilsTest.DELTA, "Checking equal retiming 4/4 to 5/8");
		assertEquals(1, five8.retime(four4, 0.625), UtilsTest.DELTA, "Checking equal retiming 4/4 to 5/8");
		
		assertEquals(1.25, four4.retime(five4, 1), UtilsTest.DELTA, "Checking equal retiming 5/4 to 4/4");
		assertEquals(1.5, four4.retime(five4, 1.2), UtilsTest.DELTA, "Checking equal retiming 5/4 to 4/4");
		
		assertEquals(0.83333333333333, three2.retime(five4, 1), UtilsTest.DELTA, "Checking equal retiming 5/4 to 3/2");
		assertEquals(1.16666666666667, three2.retime(five4, 1.4), UtilsTest.DELTA, "Checking equal retiming 5/4 to 3/2");
	}
	
	@Test
	public void toMeasures(){
		assertEquals(2, four4.toMeasures(2), UtilsTest.DELTA, "Checking whole notes to measures is the same for 4/4");
		
		assertEquals(0, five8.toMeasures(0), UtilsTest.DELTA, "Checking whole notes to measures is correct for 5/8");
		assertEquals(3.2, five8.toMeasures(2), UtilsTest.DELTA, "Checking whole notes to measures is correct for 5/8");
		
		assertEquals(1.33333333333333, six4.toMeasures(2), UtilsTest.DELTA, "Checking whole notes to measures is correct for 6/4");
		assertEquals(1, six4.toMeasures(1.5), UtilsTest.DELTA, "Checking whole notes to measures is correct for 6/4");
	}
	
	@Test
	public void toWholeNotes(){
		assertEquals(2, four4.toWholeNotes(2), UtilsTest.DELTA, "Checking measures to whole notes is the same for 4/4");
		
		assertEquals(0, five8.toWholeNotes(0), UtilsTest.DELTA, "Checking measures to whole notes is correct for 5/8");
		assertEquals(2, five8.toWholeNotes(3.2), UtilsTest.DELTA, "Checking measures to whole notes is correct for 5/8");
		
		assertEquals(2, six4.toWholeNotes(1.33333333333333), UtilsTest.DELTA, "Checking measures to whole notes is correct for 6/4");
		assertEquals(1.5, six4.toWholeNotes(1), UtilsTest.DELTA, "Checking measures to whole notes is correct for 6/4");
	}
	
	@Test
	public void guessRhythm(){
		guessRhythmHelper(1, 1, 1, four4, true);
		guessRhythmHelper(2, 1, 2, four4, true);
		guessRhythmHelper(2, 1, 2.01, four4, true);
		guessRhythmHelper(3, 1, 2.99, four4, true);
		guessRhythmHelper(3, 2, 1.5, four4, true);
		guessRhythmHelper(3, 2, 1.51, four4, true);
		guessRhythmHelper(5, 4, 1.26, four4, true);
		guessRhythmHelper(9, 8, 1.126, four4, true);
		guessRhythmHelper(17, 16, 1.0626, four4, true);
		guessRhythmHelper(3, 16, 0.1874, four4, true);
		guessRhythmHelper(1, 16, 0.0624, four4, true);
		guessRhythmHelper(1, 16, 0.09374, four4, true);
		guessRhythmHelper(1, 8, 0.09375, four4, true);
		guessRhythmHelper(1, 16, 0.06, four4, true);
		guessRhythmHelper(0, 1, 0.000001, four4, true);
		
		guessRhythmHelper(5, 8, 1, five8, true);
		guessRhythmHelper(5, 8, 1.01, five8, true);
		guessRhythmHelper(1, 8, 0.21, five8, true);
	}
	
	@Test
	public void guessRhythmWholeNotes(){
		guessRhythmHelper(2, 1, 2.01, four4, false);
		guessRhythmHelper(3, 1, 2.99, four4, false);
		guessRhythmHelper(3, 2, 1.5, four4, false);
		guessRhythmHelper(3, 2, 1.51, four4, false);
		guessRhythmHelper(5, 4, 1.26, four4, false);
		
		guessRhythmHelper(5, 8, 0.626, five8, false);
		guessRhythmHelper(1, 8, 0.126, five8, false);
	}
	
	@Test
	public void guessRhythmMeasures(){
		guessRhythmHelper(2, 1, 2.01, four4, true);
		guessRhythmHelper(3, 1, 2.99, four4, true);
		guessRhythmHelper(3, 2, 1.5, four4, true);
		guessRhythmHelper(3, 2, 1.51, four4, true);
		guessRhythmHelper(5, 4, 1.26, four4, true);
		
		guessRhythmHelper(5, 8, 1.01, five8, true);
		guessRhythmHelper(1, 8, 0.21, five8, true);
	}
	
	/**
	 * Helper method for testing {@link #guessRhythm()}
	 * @param expectDuration The expected duration value for the guessed rhythm
	 * @param expectUnit The expected unit value for the guessed rhythm
	 * @param duration The duration to guess from in number of measures
	 * @param sig The {@link TimeSignature} to use
	 */
	public static void guessRhythmHelper(int expectDuration, int expectUnit, double duration, TimeSignature sig, boolean measures){
		Rhythm guess;
		if(measures) guess = sig.guessRhythmMeasures(duration);
		else guess = sig.guessRhythmWholeNotes(duration);
		assertEquals(new Rhythm(expectDuration, expectUnit), guess, "Checking guessed rhythm is equal to the expected rhythm");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("3 4 \n2 5 \n9 8 \n1 2");
		assertTrue(four4.load(scan), "Checking load is successful");
		assertEquals(3, four4.getUpper(), "Checking upper value loaded");
		assertEquals(4, four4.getLower(), "Checking lower value loaded");
		
		assertTrue(four4.load(scan), "Checking load is successful");
		assertEquals(2, four4.getUpper(), "Checking upper value loaded");
		assertEquals(5, four4.getLower(), "Checking lower value loaded");
		
		assertTrue(four4.load(scan), "Checking load is successful");
		assertEquals(9, four4.getUpper(), "Checking upper value loaded");
		assertEquals(8, four4.getLower(), "Checking lower value loaded");
		
		assertFalse(four4.load(scan), "Checking load fails with not enough to load");
		
		assertFalse(four4.load(scan), "Checking load fails with nothing left to load");
		scan.close();
	}
	
	@Test
	public void save(){
		assertFalse(four4.save(null), "Checking save fails with invalid writer");
		
		assertEquals("4 4 \n", UtilsTest.testSave(four4), "Checking correct values are saved");
		assertEquals("5 8 \n", UtilsTest.testSave(five8), "Checking correct values are saved");
	}
	
	@Test
	public void equals(){
		assertTrue(four4.equals(four4), "Checking time signature equals itself");
		assertFalse(four4.equals(null), "Checking time signature not equal to null");
		
		TimeSignature new4 = new TimeSignature(4, 4);
		assertTrue(four4.equals(new4), "Checking identical time signatures are equal");
		assertFalse(four4 == new4, "Checking identical time signatures are not the same object");
		
		assertFalse(four4.equals(five4), "Checking time signatures with different upper are not equal");
		assertFalse(five8.equals(five4), "Checking time signatures with different lower are not equal");
	}
	
	@Test
	public void testToString(){
		assertEquals("[TimeSignature: 5/8]", five8.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
