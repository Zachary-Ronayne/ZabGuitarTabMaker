package util.testUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import util.ObjectUtils;

/**
 * A utility class for custom unit test assertions
 * @author zrona
 */
public class Assert{
	
	/**
	 * Assert that the given low number is less than the given high number
	 * @param low The low number
	 * @param high The high number
	 */
	public static void lessThan(Number low, Number high){
		lessThan(low.doubleValue(), high.doubleValue(), "");
	}
	/**
	 * Assert that the given low number is less than the given high number
	 * @param low The low number
	 * @param high The high number
	 * @param message The string to display in the case of error
	 */
	public static void lessThan(Number low, Number high, String message){
		assertTrue(low.doubleValue() < high.doubleValue(), message + " Number " + low + " should be less than " + high + " but it was not.");
	}
	
	/**
	 * Assert that the given high number is greater than the low high number
	 * @param high The high number
	 * @param low The low number
	 */
	public static void greaterThan(Number high, Number low){
		greaterThan(high.doubleValue(), low.doubleValue(), "");
	}
	
	/**
	 * Assert that the given high number is greater than the low high number
	 * @param high The high number
	 * @param low The low number
	 * @param message The string to display in the case of error
	 */
	public static void greaterThan(Number high, Number low, String message){
		assertTrue(low.doubleValue() < high.doubleValue(), message + " Number " + high + " should be greater than " + low + " but it was not.");
	}
	
	/**
	 * Assert that the given low number is less than or equal the given high number
	 * @param low The low number
	 * @param high The high number
	 */
	public static void lessThanOrEqual(Number low, Number high){
		lessThanOrEqual(low.doubleValue(), high.doubleValue(), "");
	}
	
	/**
	 * Assert that the given low number is less than or equal the given high number
	 * @param low The low number
	 * @param high The high number
	 * @param message The string to display in the case of error
	 */
	public static void lessThanOrEqual(Number low, Number high, String message){
		assertTrue(low.doubleValue() <= high.doubleValue(), message + " Number " + low + " should be greater than or equal to " + high + " but it was not.");
	}
	
	/**
	 * Assert that the given high number is greater than or equal the low high number
	 * @param high The high number
	 * @param low The low number
	 */
	public static void greaterThanOrEqual(Number high, Number low){
		greaterThanOrEqual(low.doubleValue(), high.doubleValue(), "");
	}
	
	/**
	 * Assert that the given high number is greater than or equal the low high number
	 * @param high The high number
	 * @param low The low number
	 * @param message The string to display in the case of error
	 */
	public static void greaterThanOrEqual(Number high, Number low, String message){
		assertTrue(low.doubleValue() <= high.doubleValue(), message + " Number " + high + " should be greater than or equal to " + low + " but it was not.");
	}
	
	/**
	 * Assert that the given object is a type of the given object
	 * @param c The type
	 * @param obj The object to check
	 */
	public static void isInstance(Class<?> c, Object obj){
		assertTrue(ObjectUtils.isType(obj, c), "");
	}
	
	/**
	 * Assert that the given object is a type of the given object
	 * @param c The type
	 * @param obj The object to check
	 * @param message The string to display in the case of error
	 */
	public static void isInstance(Class<?> c, Object obj, String message){
		assertTrue(ObjectUtils.isType(obj, c), message + " Checking object <" + obj + "> is not an instance of class <" + c + "> but it was.");
	}

	/**
	 * Assert that at least one object in the given iterable is an instance of the given class
	 * @param c The class
	 * @param i The iterable
	 */
	public static void containsInstance(Class<?> c, Iterable<?> i){
		boolean found = false;
		for(Object obj : i){
			if(ObjectUtils.isType(obj, c)) found = true;
		}
		assertTrue(found, "");
	}
	
	/**
	 * Assert that at least one object in the given iterable is an instance of the given class
	 * @param c The class
	 * @param i The iterable
	 * @param message The string to display in the case of error
	 */
	public static <T> void containsInstance(Class<?> c, Iterable<?> i, String message){
		boolean found = false;
		for(Object obj : i){
			if(ObjectUtils.isType(obj, c)) found = true;
		}
		assertTrue(found, message + " Checking iterable <" + i + "> contains at least one instance of <" + c + "> but it did not.");
	}
	
	/**
	 * Assert that at least one object in the given array is an instance of the given class
	 * @param c The class
	 * @param i The object array
	 */
	public static void containsInstance(Class<?> c, Object[] i){
		containsInstance(c, i, "");
	}
	
	/**
	 * Assert that at least one object in the given iterable is an instance of the given class
	 * @param c The class
	 * @param i The object array
	 * @param message The string to display in the case of error
	 */
	public static <T> void containsInstance(Class<?> c, Object[] i, String message){
		containsInstance(c, Arrays.asList(i), message);
	}
	
	/**
	 * Assert that the given collection contains all of the given elements
	 * @param c The collection to check
	 * @param contents The objects to assert that are in the collection
	 */
	public static void contains(Collection<?> c, Object... contents){
		for(Object obj : contents){
			assertTrue(c.contains(obj), "Collection <" + c + "> should contain object <" + obj + "> but it did not.");
		}
	}
	
	/**
	 * Assert that the given array contains all of the given elements
	 * @param c The collection to check
	 * @param contents The objects to assert that are in the collection
	 */
	public static void contains(Object[] c, Object... contents){
		contains(Arrays.asList(c), contents);
	}
	
	/**
	 * Assert that the given collection contains all of the given elements, and that the given collection and contents are of the same size
	 * @param c The collection to check
	 * @param contents The objects to assert that are in the collection
	 */
	public static void containsSize(Collection<?> c, Object... contents){
		sameLength(c, contents);
		contains(c, contents);
	}
	
	/**
	 * Assert that the given array contains all of the given elements, and that the given collection and contents are of the same size
	 * @param c The collection to check
	 * @param contents The objects to assert that are in the collection
	 */
	public static void containsSize(Object[] c, Object... contents){
		containsSize(Arrays.asList(c), contents);
	}
	
	/**
	 * Assert that the given collection does not contain any of the given elements
	 * @param c The collection to check
	 * @param contents The objects to assert that are not in the collection
	 */
	public static void notContains(Collection<?> c, Object... contents){
		for(Object obj : contents){
			assertFalse(c.contains(obj), "Collection <" + c + "> should not contain object <" + obj + "> but it did.");
		}
	}
	
	/**
	 * Assert that all of the given objects are in the given list in the given order, and that the size of the list is the same as the number of given objects
	 * @param list The list to check
	 * @param objs The objects to assert are in the list
	 */
	public static void listSame(List<?> list, Object... objs){
		sameLength(list, objs);
		
		for(int i = 0; i < list.size(); i++){
			assertEquals(objs[i], list.get(i), 
					"List <" + list + "> at index <" + i + "> was <" + list.get(i) + "> but it should be the element of the same index as objs <" + objs + ">"
							+ " which is <" + objs[i] + ">");
		}
	}
	
	/**
	 * Assert that the size of the list is the same as the number of given objects
	 * @param list The list to check
	 * @param objs The objects to assert are in the list
	 */
	public static void sameLength(Collection<?> list, Object... objs){
		int ls = list.size();
		int os = objs.length;
		assertEquals(os, ls, "Expect collection <" + list + ">, length: " + ls + " to be the same length as object list <" + objs + ">, length: " + os);
	}
	
	/**
	 * Assert that all of the given objects are in the given array in the given order, and that the size of the list is the same as the number of given objects
	 * @param arr The array to check
	 * @param objs The objects to assert are in the array
	 */
	public static void listSame(Object[] arr, Object... objs){
		listSame(Arrays.asList(arr), objs);
	}
}
