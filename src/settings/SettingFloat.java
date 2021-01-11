package settings;

/**
 * A {@link SettingNumber} specifically designed for floating point values
 * @author zrona
 */
public class SettingFloat extends SettingNumber<Float>{

	/**
	 * Create a new {@link SettingInt} with the given value
	 * @param value The value and default value of the setting
	 * @param minValue See {@link SettingNumber#minValue}
	 * @param maxValue See {@link SettingNumber#maxValue}
	 */
	public SettingFloat(Float value, Float minValue, Float maxValue){
		super(value, minValue, maxValue);
	}
	
	/**
	 * Create a new {@link SettingFloat} with the given value
	 * @param value The value and default value of the setting
	 */
	public SettingFloat(Float value){
		super(value);
	}

	/***/
	@Override
	public Float parseType(String n){
		try{
			return Float.parseFloat(n);
		}catch(NumberFormatException e){return null;}
	}

}
