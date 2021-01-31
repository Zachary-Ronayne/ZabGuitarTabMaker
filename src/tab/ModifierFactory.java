package tab;

import appUtils.ZabAppSettings;
import appUtils.settings.SymbolSettings;
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
		return new TabModifier(ZabAppSettings.get().symbol().hammerOn(), "");
	}
	
	/**
	 * Get the modifier for a pull off
	 * @return The modifier
	 */
	public static TabModifier pullOff(){
		return new TabModifier("", ZabAppSettings.get().symbol().pullOff());
	}
	
	/**
	 * Get the modifier for a hammer on followed by a pull off
	 * @return The modifier
	 */
	public static TabModifier hammerOnPullOff(){
		TabModifier m = hammerOn();
		return new TabModifier(m.getBefore(), ZabAppSettings.get().symbol().pullOff());
	}
	
	/**
	 * Get the modifier for sliding up to a note
	 * @return The modifier
	 */
	public static TabModifier slideUp(){
		return new TabModifier(ZabAppSettings.get().symbol().slideUp(), "");
	}
	
	/**
	 * Get the modifier for sliding down to a note
	 * @return The modifier
	 */
	public static TabModifier slideDown(){
		return new TabModifier(ZabAppSettings.get().symbol().slideDown(), "");
	}
	
	/**
	 * Get the modifier for a natural harmonic
	 * @return The modifier
	 */
	public static TabModifier harmonic(){
		SymbolSettings settings = ZabAppSettings.get().symbol();
		return new TabModifier(settings.harmonicBefore(), settings.harmonicAfter());
	}
	
	/** Cannot instantiate {@link ModifierFactory} */
	private ModifierFactory(){}
	
}
