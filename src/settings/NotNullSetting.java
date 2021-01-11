package settings;

/**
 * A Setting where the values of the setting cannot be null
 * @param T The type of the object
 * @author zrona
 */
public abstract class NotNullSetting<T> extends Setting<T>{
	
	/**
	 * Create a new {@link Setting} where the value and value cannot be null.
	 * @param value The value
	 * @param defaultValue The default value
	 * @throws IllegalArgumentException if either argument is null
	 */
	public NotNullSetting(T value, T defaultValue){
		super(value, defaultValue);
		if(value == null || defaultValue == null){
			throw new IllegalArgumentException("Neither value nor default value can be null");
		}
	}
	
	/**
	 * Create a new {@link Setting} where the value and value cannot be null.
	 * @param value The value and default value
	 * @throws IllegalArgumentException if the argument is null
	 */
	public NotNullSetting(T value){
		this(value, value);
	}
	
	/**
	 * If the given new value for this setting is null, then it is not set 
	 */
	@Override
	public void set(T value){
		if(value == null) return;
		super.set(value);
	}
	
	/**
	 * If the given new default value for this setting is null, then it is not set 
	 */
	@Override
	public void setDefault(T defaultValue){
		if(defaultValue == null) return;
		super.setDefault(defaultValue);
	}

}
