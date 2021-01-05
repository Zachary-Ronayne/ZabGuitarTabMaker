package tab.symbol;

import music.Pitch;
import music.Rhythm;
import tab.Tab;
import tab.TabString;
import util.Copyable;
import util.ObjectUtils;
import util.Saveable;

/**
 * A symbol used in a {@link Tab} to represent anything on a tab, i.e. fret numbers and dead notes
 * @author zrona
 */
public abstract class TabSymbol implements Copyable<TabSymbol>, Saveable{

	/**
	 * The {@link TabModifier} used by this {@link TabSymbol} to specify how it is played on an instrument. Cannot be null.
	 */
	private TabModifier modifier;
	
	/**
	 * Create a new {@link TabSymbol} using the given position and modifier
	 * @param pos The {@link #position}
	 * @param modifier The {@link #modifier}
	 */
	public TabSymbol(TabModifier modifier){
		if(modifier == null) throw new IllegalArgumentException("Modifier cannot be null");
		this.setModifier(modifier);
	}

	/**
	 * Get the {@link #modifier} of this {@link TabSymbol}
	 * @return See {@link #modifier}
	 */
	public TabModifier getModifier(){
		return this.modifier;
	}

	/**
	 * Set the {@link #modifier} of this {@link TabSymbol}
	 * @param See {@link #modifier}
	 */
	public void setModifier(TabModifier modifier){
		if(modifier == null) return;
		this.modifier = modifier;
	}

	/**
	 * Get the text representing this symbol with no modifiers
	 * @param string The {@link TabString} to base the symbol off of
	 * @return The text
	 */
	public abstract String getSymbol(TabString string);

	
	/**
	 * Get the text representing this symbol with all modifiers
	 * @param string The {@link TabString} to base the symbol off of
	 * @return The text
	 */
	public String getModifiedSymbol(TabString string){
		String symbol = this.getSymbol(string);
		if(this.getModifier() == null) return symbol;
		return this.getModifier().modifySymbol(symbol);
	}

	/**
	 * Create a new version of this {@link TabSymbol} as using the given {@link Rhythm}.<br>
	 * This method should just return this object if this object cannot use rhythmic information
	 * @param r The rhythm, uses the object itself, not a copy. 
	 * @return The new {@link TabSymbol} using the rhythmic information.
	 */
	public abstract TabSymbol convertToRhythm(Rhythm r);
	
	/**
	 * Create a new version of this {@link TabSymbol} using not rhythmic information.<br>
	 * This method should just return this object if this object already uses no rhythmic information
	 * @return The new {@link TabSymbol} not using the rhythmic information.
	 */
	public abstract TabSymbol removeRhythm();
	
	/**
	 * Determine if this {@link TabSymbol} uses rhythmic information
	 * @return true if it uses rhythmic information, false otherwise
	 */
	public abstract boolean usesRhythm();

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		TabSymbol s = (TabSymbol)obj;
		TabModifier m1 = this.getModifier();
		TabModifier m2 = s.getModifier();
		return	super.equals(obj) ||
				(m1 == null && m2 == null || m1.equals(m2));
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[On C4 string, note: \"");
		b.append(this.getModifiedSymbol(new TabString(new Pitch(0))));
		b.append("\"], ");
		b.append(this.getModifier());
		return b.toString();
	}
	
}
