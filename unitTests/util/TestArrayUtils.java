package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import util.testUtils.Assert;

public class TestArrayUtils{

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){}
	
	@Test
	public void insertSorted(){
		ArrayList<String> arr = new ArrayList<String>();

		assertTrue(ArrayUtils.insertSorted(arr, "d"), "Checking element is added to empty list");
		Assert.listSame(arr, "d");

		assertTrue(ArrayUtils.insertSorted(arr, "c"), "Checking element is added at the beginning of a 1 element list");
		Assert.listSame(arr, "c", "d");
		
		arr.remove("c");
		assertTrue(ArrayUtils.insertSorted(arr, "m"), "Checking element is added at the end of a 1 element list");
		Assert.listSame(arr, "d", "m");

		assertTrue(ArrayUtils.insertSorted(arr, "g"), "Checking element is added in the middle of a list");
		Assert.listSame(arr, "d", "g", "m");

		assertTrue(ArrayUtils.insertSorted(arr, "f"), "Checking element is added in the middle of a list");
		Assert.listSame(arr, "d", "f", "g", "m");
		
		assertTrue(ArrayUtils.insertSorted(arr, "z"), "Checking element is added a the end of a list");
		Assert.listSame(arr, "d", "f", "g", "m", "z");

		assertTrue(ArrayUtils.insertSorted(arr, "a"), "Checking element is added at the beginning of a list");
		Assert.listSame(arr, "a", "d", "f", "g", "m", "z");
		
		assertTrue(ArrayUtils.insertSorted(arr, "m"), "Checking duplicate element is added");
		Assert.listSame(arr, "a", "d", "f", "g", "m", "m", "z");
		
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

		Assert.containsSize(arr, "a", "b", "c");
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
		
		Assert.containsSize(arr, "a", "b", "c", "d", "e");
	}
	
	@AfterEach
	public void end(){}

}
