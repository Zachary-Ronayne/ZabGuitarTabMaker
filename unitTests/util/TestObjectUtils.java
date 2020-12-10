package util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestObjectUtils{

	@BeforeEach
	public void setup(){}
	
	@Test
	public void isType(){
		class A{}
		class B{}
		class C extends A{}
		
		A a1 = new A();
		A a2 = null;
		B b = new B();
		A c = new C();
		
		assertTrue("Checking objects with the same class type", ObjectUtils.isType(a1, A.class));
		assertTrue("Checking objects with the same class type", ObjectUtils.isType(a1, a1.getClass()));
		assertTrue("Checking objects with the same class type", ObjectUtils.isType(b, B.class));
		assertFalse("Checking objects with different class types", ObjectUtils.isType(a1, B.class));
		assertFalse("Checking objects with different class types", ObjectUtils.isType(b, A.class));
		assertFalse("Checking null objects with the same class type", ObjectUtils.isType(a2, A.class));
		assertFalse("Checking null objects with different class types", ObjectUtils.isType(a2, B.class));
		
		assertTrue("Checking objects extending each other are the same type", ObjectUtils.isType(c, A.class));
		assertTrue("Checking objects extending each other are the same type", ObjectUtils.isType(c, C.class));
		assertFalse("Checking objects extending each other are not a different type", ObjectUtils.isType(c, B.class));
	}
	
	@AfterEach
	public void end(){}
	
}