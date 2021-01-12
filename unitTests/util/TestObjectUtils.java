package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestObjectUtils{

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
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
		
		assertTrue(ObjectUtils.isType(a1, A.class), "Checking objects with the same class type");
		assertTrue(ObjectUtils.isType(a1, a1.getClass()), "Checking objects with the same class type");
		assertTrue(ObjectUtils.isType(b, B.class), "Checking objects with the same class type");
		assertFalse(ObjectUtils.isType(a1, B.class), "Checking objects with different class types");
		assertFalse(ObjectUtils.isType(b, A.class), "Checking objects with different class types");
		assertFalse(ObjectUtils.isType(a2, A.class), "Checking null objects with the same class type");
		assertFalse(ObjectUtils.isType(a2, B.class), "Checking null objects with different class types");
		
		assertTrue(ObjectUtils.isType(c, A.class), "Checking objects extending each other are the same type");
		assertTrue(ObjectUtils.isType(c, C.class), "Checking objects extending each other are the same type");
		assertFalse(ObjectUtils.isType(c, B.class), "Checking objects extending each other are not a different type");
	}
	
	@Test
	public void copy(){
		class A implements Copyable<A>{
			public int x;
			public A(int x){
				this.x = x;
			}
			@Override
			public A copy(){
				return new A(this.x);
			}
		}
		A a;
		
		a = null;
		assertEquals(null, ObjectUtils.copy(a), "Checking null returned on null parameter");
		
		a = new A(1);
		A copy = a.copy();
		assertEquals(1, copy.x, "Checking copy has the same value");
		assertFalse(a == copy, "Checking copy is not the same object");
	}
	
	@AfterEach
	public void end(){}
	
}
