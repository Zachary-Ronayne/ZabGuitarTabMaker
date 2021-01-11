package settings;

/**
 * A {@link SettingNumber} specifically designed for long integer type values
 * @author zrona
 */
public class SettingLong extends SettingNumber<Long>{
	
	/**
	 * Create a new {@link SettingLong} with the given value
	 * @param value The value and default value of the setting
	 * @param minValue See {@link SettingNumber#minValue}
	 * @param maxValue See {@link SettingNumber#maxValue}
	 */
	public SettingLong(Long value, Long minValue, Long maxValue){
		super(value, minValue, maxValue);
	}
	
	/**
	 * Create a new {@link SettingLong} with the given value
	 * @param value The value and default value of the setting
	 */
	public SettingLong(Long value){
		super(value);
	}
	
	/***/
	@Override
	public Long parseType(String n){
		try{
			return Long.parseLong(n);
		}catch(NumberFormatException e){return null;}
	}

}
