package tab;

import appUtils.ZabAppSettings;
import appUtils.ZabSettings;
import tab.symbol.TabModifier;

/**
 * A class containing utility methods for generating different modifiers
 * @author zrona
 */
public class ModifierFactory{
	
	/**
	 * Get the modifier for a hammer on
	 * @return The modifier
	 */
	public static TabModifier hammerOn(){
		return new TabModifier(ZabAppSettings.get().hammerOn(), "");
	}
	
	/**
	 * Get the modifier for a pull off
	 * @return The modifier
	 */
	public static TabModifier pullOff(){
		return new TabModifier("", ZabAppSettings.get().pullOff());
	}
	
	/**
	 * Get the modifier for a hammer on followed by a pull off
	 * @return The modifier
	 */
	public static TabModifier hammerOnPullOff(){
		TabModifier m = hammerOn();
		m.setAfter(ZabAppSettings.get().pullOff());
		return m;
	}
	
	/**
	 * Get the modifier for sliding up to a note
	 * @return The modifier
	 */
	public static TabModifier slideUp(){
		return new TabModifier(ZabAppSettings.get().slideUp(), "");
	}
	
	/**
	 * Get the modifier for sliding down to a note
	 * @return The modifier
	 */
	public static TabModifier slideDown(){
		return new TabModifier(ZabAppSettings.get().slideDown(), "");
	}
	
	/**
	 * Get the modifier for a natural harmonic
	 * @return The modifier
	 */
	public static TabModifier harmonic(){
		ZabSettings settings = ZabAppSettings.get();
		return new TabModifier(settings.harmonicBefore(), settings.harmonicAfter());
	}
	
	/** Cannot instantiate {@link ModifierFactory} */
	private ModifierFactory(){}
	
}
