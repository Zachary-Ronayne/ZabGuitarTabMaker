package tab.symbol;

import tab.Tab;

/**
 * A symbol used in a {@link Tab} to represent anything on a tab, i.e. fret numbers and dead notes
 * @author zrona
 */
public abstract class TabSymbol{
	
	/**
	 * Get the text representing this symbol
	 * @return
	 */
	public abstract String getSymbol();
	
}
