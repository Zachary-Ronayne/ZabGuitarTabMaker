package settings;

/**
 * A {@link SettingNumber} specifically designed for double floating point values
 * @author zrona
 */
public class SettingDouble extends SettingNumber<Double>{

	/**
	 * Create a new {@link SettingInt} with the given value
	 * @param value The value and default value of the setting
	 * @param minValue See {@link SettingNumber#minValue}
	 * @param maxValue See {@link SettingNumber#maxValue}
	 */
	public SettingDouble(Double value, Double minValue, Double maxValue){
		super(value, minValue, maxValue);
	}
	
	/**
	 * Create a new {@link SettingDouble} with the given value
	 * @param value The value and default value of the setting
	 */
	public SettingDouble(Double value){
		super(value);
	}

	/***/
	@Override
	public Double parseType(String n){
		try{
			return Double.parseDouble(n);
		}catch(NumberFormatException e){return null;}
	}

}
