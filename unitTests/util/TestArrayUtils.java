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
	
	@Test
	public void addWithoutDuplicate(){
		ArrayList<String> arr = new ArrayList<String>();
		assertTrue(ArrayUtils.addWithoutDuplicate(arr, "a"), "Checking element added");
		assertTrue(ArrayUtils.addWithoutDuplicate(arr, "b"), "Checking element added");
		assertTrue(ArrayUtils.addWithoutDuplicate(arr, "c"), "Checking element added");
		assertFalse(ArrayUtils.addWithoutDuplicate(arr, "b"), "Checking duplicate element not added");
		
		assertEquals(3, arr.size(), "Checking array has correct size");
		assertTrue(arr.contains("a"), "Checking array has element");
		assertTrue(arr.contains("b"), "Checking array has element");
		assertTrue(arr.contains("c"), "Checking array has element");
	}
	
	@Test
	public void addManyWithoutDuplicate(){
		ArrayList<String> arr = new ArrayList<String>();
		ArrayList<String> dupes = new ArrayList<String>();
		dupes.add("a");
		dupes.add("b");
		dupes.add("c");
		dupes.add("b");
		assertFalse(ArrayUtils.addManyWithoutDuplicate(arr, dupes), "Checking at least one element wasn't added");

		dupes = new ArrayList<String>();
		dupes.add("d");
		dupes.add("e");
		assertTrue(ArrayUtils.addManyWithoutDuplicate(arr, dupes), "Checking all elements added");
		
		assertEquals(5, arr.size(), "Checking array has correct size");
		assertTrue(arr.contains("a"), "Checking array has element");
		assertTrue(arr.contains("b"), "Checking array has element");
		assertTrue(arr.contains("c"), "Checking array has element");
		assertTrue(arr.contains("d"), "Checking array has element");
		assertTrue(arr.contains("e"), "Checking array has element");
	}
	
	@AfterEach
	public void end(){}

}
