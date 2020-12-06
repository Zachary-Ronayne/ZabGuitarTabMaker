package tab.symbol;

import tab.Tab;
import tab.TabString;

/**
 * A symbol used in a {@link Tab} to represent anything on a tab, i.e. fret numbers and dead notes
 * @author zrona
 */
public abstract class TabSymbol{
	
	/** The {@link TabModifier} used by this {@link TabSymbol} */
	private TabModifier modifier;
	
	/**
	 * Get the text representing this symbol
	 * @param string The {@link TabString} to base the symbol off of
	 * @return The text
	 */
	public abstract String getSymbol(TabString string);
	
	/**
	 * Get the full symbol used to draw this {@link TabSymbol}
	 * @param string The {@link TabString} to base the symbol off of
	 * @return The text of the symbol
	 */
	public String getModifiedSymbol(TabString string){ // TODO
		return "";
	}
	
}
