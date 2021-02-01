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
	
	/**
	 * Get the modifier for bending a string up to a note
	 * @return The modifier
	 */
	public static TabModifier bend(){
		SymbolSettings settings = ZabAppSettings.get().symbol();
		return new TabModifier("", settings.bend());
	}
	
	/**
	 * Get the modifier for a ghost note that rings out
	 * @return The modifier
	 */
	public static TabModifier ghostNote(){
		SymbolSettings settings = ZabAppSettings.get().symbol();
		return new TabModifier(settings.ghostBefore(), settings.ghostAfter());
	}
	
	/**
	 * Get the modifier for a pinch harmonic note
	 * @return The modifier
	 */
	public static TabModifier pinchHarmonic(){
		SymbolSettings settings = ZabAppSettings.get().symbol();
		return new TabModifier("", settings.pinchHarmonic());
	}
	
	/**
	 * Get the modifier for putting vibrato on a note
	 * @return The modifier
	 */
	public static TabModifier vibrato(){
		SymbolSettings settings = ZabAppSettings.get().symbol();
		return new TabModifier("", settings.vibrato());
	}
	
	/**
	 * Get the modifier for a tap note
	 * @return The modifier
	 */
	public static TabModifier tap(){
		SymbolSettings settings = ZabAppSettings.get().symbol();
		return new TabModifier("", settings.tap());
	}
	
	/** Cannot instantiate {@link ModifierFactory} */
	private ModifierFactory(){}
	
}
