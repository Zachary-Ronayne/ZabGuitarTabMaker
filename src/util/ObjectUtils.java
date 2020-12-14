package util;

/**
 * A class for handling interactions between objects
 * @author zrona
 */
public final class ObjectUtils{
	
	/**
	 * Check if the given object is non null and of the given type
	 * @param <T> The type
	 * @param obj The object to test
	 * @param type The actual type
	 * @return true if obj is not null and an instance of type, false otherwise
	 */
	public static <T> boolean isType(Object obj, Class<T> type){
		return obj != null && type.isInstance(obj);
	}
	
	/**
	 * Create a deep copy of the given {@link Copyable} object, checking for null values
	 * @param <T> The type of the {@link Copyable} object
	 * @param c The object to copy
	 * @return The copied object, or null if c is null
	 */
	public static <T> T copy(Copyable<T> c){
		if(c == null) return null;
		return c.copy();
	}
	
	/** Cannot make instances of {@link ObjectUtils} */
	private ObjectUtils(){};
	
}
