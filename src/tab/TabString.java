package tab;

import java.util.ArrayList;

import music.Pitch;
import tab.symbol.TabSymbol;

/**
 * A class representing one string of a tablature 
 * @author zrona
 */
public class TabString{
	
	/**
	 * An ordered list of all of the symbols contained within this {@link TabString}
	 */
	private ArrayList<TabSymbol> symbols;
	
	/**
	 * The {@link Pitch} which this {@link TabString} is tuned to, i.e. the note of this string when played open.
	 */
	private Pitch rootPitch;
}
