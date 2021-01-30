package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestSize2D{

	private Size2D size;
	private Size2D small;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		size = new Size2D(23, 39);
		small = new Size2D(0.2, 0.04);
	}
	
	@Test
	public void getWidth(){
		assertEquals(23, size.getWidth(), "Checking width initialized");
		assertEquals(0.2, small.getWidth(), "Checking width initialized");
	}
	
	@Test
	public void getHeight(){
		assertEquals(39, size.getHeight(), "Checking height initialized");
		assertEquals(0.04, small.getHeight(), "Checking height initialized");
	}
	
	@Test
	public void equals(){
		Size2D same = new Size2D(23, 39);
		assertTrue(size.equals(same), "Checking objects are equal");
		assertTrue(size.equals(size), "Checking object equal to itself");
		assertFalse(size == same, "Checking objects are not the same object");
		assertFalse(size.equals(null), "Checking object is not equal to null");

		assertFalse(size.equals(new Size2D(1, 39)), "Checking objects are not equal with different width");
		assertFalse(size.equals(new Size2D(23, 3)), "Checking objects are not equal with different height");
	}

	@Test
	public void testToString(){
		assertEquals("[Size2D: width:23.0, height:39.0]", size.toString(), "Checking correct string");
		assertEquals("[Size2D: width:0.2, height:0.04]", small.toString(), "Checking correct string");
	}
	
	
	@AfterEach
	public void end(){}
	
}
