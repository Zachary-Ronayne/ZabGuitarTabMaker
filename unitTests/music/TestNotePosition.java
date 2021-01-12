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

public class TestNotePosition{
	
	private NotePosition pos;
	private TimeSignature four4;
	private TimeSignature five8;
	private TimeSignature three2;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		pos = new NotePosition(3);
		
		four4 = new TimeSignature(4, 4);
		five8 = new TimeSignature(5, 8);
		three2 = new TimeSignature(3, 2);
	}

	@Test
	public void copy(){
		NotePosition copy = pos.copy();
		assertTrue(copy.equals(pos), "Checking copy is equal to the source object");
		assertTrue(copy != pos, "Checking copy is not the same as the source object");
	}
	
	@Test
	public void getValue(){
		assertEquals(3, pos.getValue(), "Checking value is initialized");
	}
	
	@Test
	public void setValue(){
		pos.setValue(2);
		assertEquals(2, pos.getValue(), "Checking value is set");
	}
	
	@Test
	public void addValue(){
		pos.setValue(2);
		pos.addValue(3);
		assertEquals(5, pos.getValue(), "Checking value is added");
	}
	
	@Test
	public void retime(){
		pos.setValue(1.5);
		pos.retime(five8, four4);
		assertEquals(2.4, pos.getValue(), "Checking retimed position");
	}
	
	@Test
	public void retimeMeasure(){
		retimeMeasureHelper(1, 1, four4, five8, true);
		retimeMeasureHelper(0, 0, four4, five8, true);
		retimeMeasureHelper(1.4, 1.25, four4, five8, true);
		retimeMeasureHelper(2.2, 1.75, four4, five8, false);
		
		retimeMeasureHelper(1, 1, five8, four4, true);
		retimeMeasureHelper(1.5, 1.8, five8, four4, true);
		
		retimeMeasureHelper(1, 1, four4, three2, true);
		retimeMeasureHelper(1.5, 1.75, four4, three2, true);
		
		retimeMeasureHelper(1.08333333333333, 1.2, five8, three2, true);
		retimeMeasureHelper(3, 1.83333333333333, three2, five8, false);
	}
	
	/**
	 * Helper for testing retimeMeasure
	 * @param expectPos The expected position for the note
	 * @param currentPos The current position of a note 
	 * @param oldTime The original time signature of the position
	 * @param newTime The new time signature of the position
	 * @param inside true if the note should be inside the same measure, false otherwise
	 */
	private void retimeMeasureHelper(double expectPos, double currentPos, TimeSignature oldTime, TimeSignature newTime, boolean inside){
		NotePosition p = new NotePosition(currentPos);
		boolean result = p.retimeMeasure(newTime, oldTime);
		assertEquals(expectPos, p.getValue(), UtilsTest.DELTA, "Checking retiming in the same measure from " 
				+ oldTime.symbol() + " to " + newTime.symbol());
		if(inside) assertTrue(result, "Checking retiming is in the original measure");
		else assertFalse(result, "Checking retiming is outside the original measure");
	}
	
	@Test
	public void quantize(){
		pos.setValue(1.1);
		pos.quantize(new TimeSignature(4, 4), 4);
		assertEquals(1, pos.getValue(), UtilsTest.DELTA, "Checking note is quantized");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("1.2 \n2 \n");
		assertTrue(pos.load(scan), "Checking load successful");
		assertEquals(1.2, pos.getValue(), "Checking correct value loaded");
		
		assertTrue(pos.load(scan), "Checking load successful");
		assertEquals(2.0, pos.getValue(), "Checking correct value loaded");
		
		assertFalse(pos.load(scan), "Checking load fails with nothing left to load");
		scan.close();
	}
	
	@Test
	public void save(){
		pos.setValue(2.3);
		assertEquals("2.3 \n", UtilsTest.testSave(pos), "Checking correct value saved");
		
		pos.setValue(2);
		assertEquals("2.0 \n", UtilsTest.testSave(pos), "Checking correct value saved");
		
		pos.setValue(-1.5);
		assertEquals("-1.5 \n", UtilsTest.testSave(pos), "Checking correct value saved");
	}
	
	@Test
	public void compareTo(){
		NotePosition comp = new NotePosition(0);
		NotePosition same = new NotePosition(3);
		assertTrue(comp.compareTo(pos) < 0, "Checking comparing low to high");
		assertTrue(pos.compareTo(comp) > 0, "Checking comparing high to low");
		assertTrue(pos.compareTo(same) == 0, "Checking comparing equal");
	}
	
	@Test
	public void equals(){
		NotePosition p = new NotePosition(3);
		assertFalse(p == pos, "Checking objects are not the same object");
		assertTrue(p.equals(pos), "Checking objects are equal");
		
		p.setValue(4);
		assertFalse(p.equals(pos), "Checking objects are not equal");
	}
	
	@Test
	public void testToString(){
		assertEquals("[NotePosition, position: 3.0]", pos.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
