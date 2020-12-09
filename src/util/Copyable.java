package util;

/**
 * An interface which defines that an object can generate an exact copy of itself with identical fields, but as a separate object
 * @param <T> The type of object which is returned by the copy
 * @author zrona
 * @
 */
public interface Copyable<T>{
	
	/**
	 * Make an identical copy of this object, with all field values as the same, but as a separate object.<br>
	 * Should also be a deep copy, i.e. all objects within this object should be copied, 
	 * and all objects within those objects should be copied, and so on
	 * @return The copy
	 */
	public T copy();
	
}
