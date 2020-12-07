package tab.symbol;

import music.Pitch;
import tab.TabString;

/**
 * An object representing a symbol in a tab which contains pitch information
 * @author zrona
 */
public abstract class TabPitch extends TabSymbol{

	/** The {@link Pitch} of this {@link TabPitch}, i.e. the musical note */
	private Pitch pitch;
	
	public TabPitch(TabPosition pos, TabModifier modifier){ // TODO add pitch
		super(pos, modifier);
	}
	
	/**
	 * Get the symbol representing the pitch of this {@link TabPitch}
	 */
	public String getSymbol(TabString string){ // TODO
		return "";
	}
	
}
