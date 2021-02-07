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
		this.modifier = modifier;
	}

	/**
	 * Get the {@link #modifier} of this {@link TabSymbol}
	 * @return See {@link #modifier}
	 */
	public TabModifier getModifier(){
		return this.modifier;
	}

	/**
	 * Create a new {@link TabSymbol} which is a copy of this one, but with the given modifier
	 * @return The new {@link TabSymbol}, or null if modifier is null
	 */
	public TabSymbol copyNewModifier(TabModifier modifier){
		if(modifier == null) return null;
		TabSymbol symbol = this.copy();
		symbol.modifier = modifier;
		return symbol;
	}
	
	/**
	 * Create a new {@link TabSymbol} which is a copy of this one, but with the given modifier added on. 
	 * For definition of adding, see {@link TabModifier#added(TabModifier)}
	 * @return The new {@link TabSymbol}, or null if mod is null
	 */
	public TabSymbol copyAddModifier(TabModifier mod){
		if(mod == null) return null;
		TabSymbol symbol = this.copy();
		symbol.modifier = this.getModifier().added(mod);
		return symbol;
	}
	
	/**
	 * Create a new {@link TabSymbol} with should be otherwise the same as this object, but also holding the given pitch information. 
	 * Override this method to change what kind of {@link TabPitch} is returned, defaults to a {@link TabNote}
	 * @param p The pitch to use for the note
	 * @return The new {@link TabPitch}, should never be null
	 */
	public TabPitch createPitchNote(Pitch p){
		return new TabNote(p, this.getModifier().copy());
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
		return this.getModifier().modifySymbol(symbol);
	}

	/**
	 * Create a new version of this {@link TabSymbol} as using the given {@link Rhythm}.<br>
	 * This method should just return a copy of this object if this object cannot use rhythmic information
	 * @param r The rhythm, uses the object itself, not a copy. 
	 * @return The new {@link TabSymbol} using the rhythmic information.
	 */
	public abstract TabSymbol convertToRhythm(Rhythm r);
	
	/**
	 * Create a new version of this {@link TabSymbol} using not rhythmic information.<br>
	 * This method should just return a copy of this object if this object already uses no rhythmic information
	 * @return The new {@link TabSymbol} not using the rhythmic information.
	 */
	public abstract TabSymbol removeRhythm();
	
	/**
	 * Determine if this {@link TabSymbol} uses rhythmic information
	 * @return true if it uses rhythmic information, false otherwise
	 */
	public abstract boolean usesRhythm();
	
	/**
	 * Create a version of this state of this {@link TabSymbol} so that it remains the same symbol when changing {@link TabString} objects
	 * @param oldStr The {@link TabString} it used to be on
	 * @param newStr The {@link TabString} it will be moved to
	 * @return The new {@link TabSymbol}, or null if the pitch couldn't be generated
	 */
	public abstract TabSymbol movingToNewString(TabString oldStr, TabString newStr);

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, TabSymbol.class)) return false;
		TabSymbol s = (TabSymbol)obj;
		TabModifier m1 = this.getModifier();
		TabModifier m2 = s.getModifier();
		return	super.equals(obj) ||
				m1.equals(m2);
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
