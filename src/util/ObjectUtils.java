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
	
	/** Cannot make instances of {@link ObjectUtils} */
	private ObjectUtils(){};
	
}
