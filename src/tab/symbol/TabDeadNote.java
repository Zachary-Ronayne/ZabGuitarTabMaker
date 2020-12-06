package tab.symbol;

import tab.Tab;
import tab.TabString;

/**
 * A symbol for a {@link Tab} representing a dead note or muted note
 * @author zrona
 */
public class TabDeadNote extends TabSymbol{

	/**
	 * Get the text representing this dead note
	 * @return Always returns an X
	 */
	@Override
	public String getSymbol(TabString string){ // TODO
		return "";
	}

}
