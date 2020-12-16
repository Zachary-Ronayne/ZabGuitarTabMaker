package tab.symbol;

import music.NotePosition;
import music.Rhythm;
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

	/** The position {@link NotePosition} object of this {@link TabSymbol} on a {@link TabString}. Cannot be null. */
	private NotePosition position;
	
	/**
	 * The {@link TabModifier} used by this {@link TabSymbol} to specify how it is played on an instrument. Cannot be null.
	 */
	private TabModifier modifier;
	
	/**
	 * Create a new {@link TabSymbol} using the given position and modifier
	 * @param pos The {@link #position}
	 * @param modifier The {@link #modifier}
	 */
	public TabSymbol(NotePosition position, TabModifier modifier){
		if(position == null) throw new IllegalArgumentException("Pos cannot be null");
		if(modifier == null) throw new IllegalArgumentException("Modifier cannot be null");
		this.setPosition(position);
		this.setModifier(modifier);
	}
	
	/**
	 * Get the {@link #position} of this {@link TabSymbol}
	 * @return See {@link #position}
	 */
	public NotePosition getPosition(){
		return this.position;
	}
	
	/**
	 * Get the value of the {@link #position} of this {@link TabSymbol}
	 * @return The position
	 */
	public double getPos(){
		return this.getPosition().getValue();
	}
	
	/**
	 * Set the {@link #position} of this {@link TabSymbol}
	 * @param pos See {@link #position}
	 */
	public void setPosition(NotePosition position){
		if(position == null) return;
		this.position = position;
	}

	/**
	 * Set the value for the {@link #position} of this {@link TabSymbol} creating a new {@link NotePosition} object
	 * @param pos See {@link #position}
	 */
	public void setPos(double pos){
		this.setPosition(new NotePosition(pos));
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
	 * Quantize this {@link TabSymbol} position to the nearest place in a measure
	 * @param sig The time signature to base the quantization off of
	 * @param divisor The amount to divide up the units of a whole note.<br>
	 * 	i.e. use 4 to quantize to quarter notes, use 6 to quantize to dotted quarter notes, etc
	 */
	public void quantize(TimeSignature sig, int divisor){
		this.getPosition().quantize(sig, divisor);
	}
	
	/**
	 * Convert this {@link TabSymbol} objects position so that it is the same number of whole notes, but in the new time signature
	 * @param newTime The {@link TimeSignature} to convert to
	 * @param oldTime The {@link TimeSignature} which this position was in
	 */
	public void retime(TimeSignature newTime, TimeSignature oldTime){
		this.getPosition().retime(newTime, oldTime);
	}
	
	/**
	 * Convert this {@link NoteSymbol} object position so that it stays in the same measure and same relative position in the measure.<br>
	 * @param newTime The {@link TimeSignature} to convert to
	 * @param oldTime The {@link TimeSignature} which the position was in
	 * @return true if the position is in the same measure, false otherwise
	 */
	public boolean retimeMeasure(TimeSignature newTime, TimeSignature oldTime){
		return this.getPosition().retimeMeasure(newTime, oldTime);
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
	public int compareTo(TabSymbol t){
		double p1 = this.getPosition().getValue();
		double p2 = t.getPosition().getValue();
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
				this.getPosition().equals(s.getPosition()) &&
				(m1 == null && m2 == null || m1.equals(m2));
	}
	
}
