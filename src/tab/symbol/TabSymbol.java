package tab.symbol;

import music.NotePosition;
import music.TimeSignature;
import tab.Tab;
import tab.TabString;
import util.Copyable;
import util.ObjectUtils;

/**
 * A symbol used in a {@link Tab} to represent anything on a tab, i.e. fret numbers and dead notes
 * @author zrona
 */
public abstract class TabSymbol implements Comparable<TabSymbol>, Copyable<TabSymbol>{

	/** The position {@link NotePosition} object of this {@link TabSymbol} on a {@link TabString}*/
	private NotePosition pos;
	
	/**
	 * The {@link TabModifier} used by this {@link TabSymbol} to specify how it is played on an instrument<br>
	 * Can be null to represent no modifier
	 */
	private TabModifier modifier;
	
	/**
	 * Create a new {@link TabSymbol} using the given position and modifier
	 * @param pos The {@link #pos}
	 * @param modifier The {@link #modifier}
	 */
	public TabSymbol(NotePosition pos, TabModifier modifier){
		this.setPos(pos);
		this.setModifier(modifier);
	}
	
	/**
	 * Get the {@link #pos} of this {@link TabSymbol}
	 * @return The position
	 */
	public NotePosition getPos(){
		return pos;
	}

	/**
	 * Set the {@link #pos} of this {@link TabSymbol}
	 * @param pos The position
	 */
	public void setPos(NotePosition pos){
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
		String symbol = this.getSymbol(string);
		if(this.getModifier() == null) return symbol;
		return this.getModifier().modifySymbol(symbol);
	}
	
	/**
	 * Quantize this {@link TabSymbol} position to the nearest place in a measure
	 * @param sig The time signature to base the quantization off of
	 * @param divisor The amount to divide up the units of a whole note.<br>
	 * 	i.e. use 4 to quantize to quarter notes, use 6 to quantize to dotted quarter notes, etc
	 */
	public void quantize(TimeSignature sig, int divisor){
		this.getPos().quantize(sig, divisor);
	}
	
	/***/
	@Override
	public int compareTo(TabSymbol t){
		double p1 = this.getPos().getValue();
		double p2 = t.getPos().getValue();
		if(p1 < p2) return -1;
		return (p1 > p2) ? 1 : 0;
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		TabSymbol s = (TabSymbol)obj;
		TabModifier m1 = this.getModifier();
		TabModifier m2 = s.getModifier();
		return	super.equals(obj) ||
				this.getPos().equals(s.getPos()) &&
				(m1 == null && m2 == null || m1.equals(m2));
	}
	
}
