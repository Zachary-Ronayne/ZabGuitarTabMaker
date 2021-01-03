package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestArrayUtils{

	@BeforeEach
	public void setup(){}
	
	@Test
	public void insertSorted(){
		ArrayList<String> arr = new ArrayList<String>();

		assertTrue(ArrayUtils.insertSorted(arr, "d"), "Checking element is added to empty list");
		assertEquals(1, arr.size(), "Checking correct array size");
		assertEquals("d", arr.get(0), "Checking correct array position");

		assertTrue(ArrayUtils.insertSorted(arr, "c"), "Checking element is added at the beginning of a 1 element list");
		assertEquals("c", arr.get(0), "Checking correct array position");
		assertEquals("d", arr.get(1), "Checking correct array position");
		
		arr.remove("c");
		assertTrue(ArrayUtils.insertSorted(arr, "m"), "Checking element is added at the end of a 1 element list");
		assertEquals(2, arr.size(), "Checking correct array size");
		assertEquals("d", arr.get(0), "Checking correct array position");
		assertEquals("m", arr.get(1), "Checking correct array position");

		assertTrue(ArrayUtils.insertSorted(arr, "g"), "Checking element is added in the middle of a list");
		assertEquals(3, arr.size(), "Checking correct array size");
		assertEquals("d", arr.get(0), "Checking correct array position");
		assertEquals("g", arr.get(1), "Checking correct array position");
		assertEquals("m", arr.get(2), "Checking correct array position");

		assertTrue(ArrayUtils.insertSorted(arr, "f"), "Checking element is added in the middle of a list");
		assertEquals(4, arr.size(), "Checking correct array size");
		assertEquals("d", arr.get(0), "Checking correct array position");
		assertEquals("f", arr.get(1), "Checking correct array position");
		assertEquals("g", arr.get(2), "Checking correct array position");
		assertEquals("m", arr.get(3), "Checking correct array position");
		
		assertTrue(ArrayUtils.insertSorted(arr, "z"), "Checking element is added a the end of a list");
		assertEquals(5, arr.size(), "Checking correct array size");
		assertEquals("d", arr.get(0), "Checking correct array position");
		assertEquals("f", arr.get(1), "Checking correct array position");
		assertEquals("g", arr.get(2), "Checking correct array position");
		assertEquals("m", arr.get(3), "Checking correct array position");
		assertEquals("z", arr.get(4), "Checking correct array position");

		assertTrue(ArrayUtils.insertSorted(arr, "a"), "Checking element is added at the beginning of a list");
		assertEquals(6, arr.size(), "Checking correct array size");
		assertEquals("a", arr.get(0), "Checking correct array position");
		assertEquals("d", arr.get(1), "Checking correct array position");
		assertEquals("f", arr.get(2), "Checking correct array position");
		assertEquals("g", arr.get(3), "Checking correct array position");
		assertEquals("m", arr.get(4), "Checking correct array position");
		assertEquals("z", arr.get(5), "Checking correct array position");
		
		assertTrue(ArrayUtils.insertSorted(arr, "m"), "Checking duplicate element is added");
		assertEquals(7, arr.size(), "Checking correct array size");
		assertEquals("a", arr.get(0), "Checking correct array position");
		assertEquals("d", arr.get(1), "Checking correct array position");
		assertEquals("f", arr.get(2), "Checking correct array position");
		assertEquals("g", arr.get(3), "Checking correct array position");
		assertEquals("m", arr.get(4), "Checking correct array position");
		assertEquals("m", arr.get(5), "Checking correct array position");
		assertEquals("z", arr.get(6), "Checking correct array position");
		
		arr.clear();
		ArrayUtils.insertSorted(arr, "a", false);
		assertFalse(ArrayUtils.insertSorted(arr, "a", false), "Checking duplicate element is not added with disallowed duplicates");
		assertEquals(1, arr.size(), "Checking correct array size");
	}
	
	@AfterEach
	public void end(){}

}
