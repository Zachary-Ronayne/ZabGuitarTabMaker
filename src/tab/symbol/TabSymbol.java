package tab.symbol;

import tab.Tab;
import tab.TabString;

/**
 * A symbol used in a {@link Tab} to represent anything on a tab, i.e. fret numbers and dead notes
 * @author zrona
 */
public abstract class TabSymbol{

	/** The position of this {@link TabSymbol} on a {@link TabString} */
	private TabPosition pos;
	
	/**
	 * The {@link TabModifier} used by this {@link TabSymbol} to specify how it is played<br>
	 * Can be null to represent no modifier
	 */
	private TabModifier modifier;
	
	/**
	 * Create a new {@link TabSymbol} using the given position and modifier
	 * @param pos The {@link #pos}
	 * @param modifier The {@link #modifier}
	 */
	public TabSymbol(TabPosition pos, TabModifier modifier){
		this.setPos(pos);
		this.setModifier(modifier);
	}
	
	/**
	 * Get the {@link #pos} of this {@link TabSymbol}
	 * @return The position
	 */
	public TabPosition getPos(){
		return pos;
	}

	/**
	 * Set the {@link #pos} of this {@link TabSymbol}
	 * @param pos The position
	 */
	public void setPos(TabPosition pos){
		this.pos = pos;
	}

	/**
	 * Get the {@link #modifier} of this {@link TabSymbol}
	 * @return The modifier
	 */
	public TabModifier getModifier(){
		return modifier;
	}

	/**
	 * Set the {@link #modifier} of this {@link TabSymbol}
	 * @param The modifier
	 */
	public void setModifier(TabModifier modifier){
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
		return this.getModifier().modifySymbol(this.getSymbol(string));
	}
	
}
