package settings;

/**
 * A {@link SettingNumber} specifically designed for integers
 * @author zrona
 */
public class SettingInt extends SettingNumber<Integer>{

	/**
	 * Create a new {@link SettingInt} with the given value
	 * @param value The value and default value of the setting
	 * @param minValue See {@link SettingNumber#minValue}
	 * @param maxValue See {@link SettingNumber#maxValue}
	 */
	public SettingInt(Integer value, Integer minValue, Integer maxValue){
		super(value, minValue, maxValue);
	}
	
	/**
	 * Create a new {@link SettingInt} with the given value
	 * @param value The value and default value of the setting
	 */
	public SettingInt(Integer value){
		super(value);
	}

	/***/
	@Override
	public Integer parseType(String n){
		try{
			return Integer.parseInt(n);
		}catch(NumberFormatException e){return null;}
	}
	
	@Override
	public void add(Integer toAdd){
		this.set(this.get() + toAdd);
	}

}
