package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestMathUtils{

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}

	@BeforeEach
	public void setup(){}
	
	@Test
	public void gcd(){
		assertEquals(2, MathUtils.gcd(4, 2), "Checking GCD is correctly calculated");
		assertEquals(1, MathUtils.gcd(5, 2), "Checking GCD is correctly calculated");
		assertEquals(7, MathUtils.gcd(21, 7), "Checking GCD is correctly calculated");
		assertEquals(0, MathUtils.gcd(0, 0), "Checking GCD is correctly calculated");
		assertEquals(2, MathUtils.gcd(18, 8), "Checking GCD is correctly calculated");
		assertEquals(-5, MathUtils.gcd(25, -5), "Checking GCD is correctly calculated");
		assertEquals(5, MathUtils.gcd(-25, 5), "Checking GCD is correctly calculated");
		assertEquals(-5, MathUtils.gcd(-25, -5), "Checking GCD is correctly calculated");
	}
	
	@AfterEach
	public void end(){}
}
