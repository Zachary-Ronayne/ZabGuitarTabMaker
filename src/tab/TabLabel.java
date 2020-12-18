package tab;

import music.TimeSignature;
import tab.symbol.TabSymbol;

/**
 * An object representing a section of a tab with a particular label, could be a symbol or name for example
 * @author zrona
 */
public class TabLabel{
	
	/** The text of the label */
	private String text;
	
	/** The symbol representing the start of this label */
	private TabSymbol baseSymbol;
	
	/**
	 * The total length of this label in number of whole notes.<br>
	 * Cannot be less than zero
	 */
	private double length;
	
	/** The distance this label begins before the {@link #baseSymbol} */
	private double offset;
	
	/**
	 * Create a new {@link TabLabel} with all parameters
	 * @param text See {@link #text}
	 * @param baseSymbol See {@link #baseSymbol}
	 * @param length See {@link #length}
	 * @param offset See {@link #offset}
	 */
	public TabLabel(String text, TabSymbol baseSymbol, double length, double offset){
		this.setText(text);
		this.setBaseSymbol(baseSymbol);
		this.setLength(length);
		this.setOffset(offset);
	}
	
	/**
	 * Create a new {@link TabLabel} with no length
	 * @param label See {@link #text}
	 * @param baseSymbol See {@link #baseSymbol}
	 */
	public TabLabel(String text, TabSymbol baseSymbol){
		this(text, baseSymbol, 0, 0);
	}
	
	/**
	 * Get the label text
	 * @return See {@link #text}
	 */
	public String getText(){
		return this.text;
	}

	/**
	 * Set the label text
	 * @param label See {@link #text}
	 */
	public void setText(String text){
		this.text = text;
	}

	/**
	 * Get the {@link TabSymbol} used for the position of this {@link TabLabel}
	 * @return See {@link #baseSymbol}
	 */
	public TabSymbol getBaseSymbol(){
		return this.baseSymbol;
	}

	/**
	 * Set the {@link TabSymbol} used for the position of this {@link TabLabel}
	 * @param baseSymbol See {@link #baseSymbol}
	 */
	public void setBaseSymbol(TabSymbol baseSymbol){
		this.baseSymbol = baseSymbol;
	}

	/**
	 * Get the length in whole notes
	 * @return See {@link #length}
	 */
	public double getLength(){
		return this.length;
	}

	/**
	 * Set the length in whole notes
	 * @param length See {@link #length}
	 */
	public void setLength(double length){
		if(length < 0) length = 0;
		this.length = length;
	}

	/**
	 * Get the offset in whole notes
	 * @return See {@link #offset}
	 */
	public double getOffset(){
		return this.offset;
	}

	/**
	 * Set the offset in whole notes
	 * @param offset See {@link #offset}
	 */
	public void setOffset(double offset){
		this.offset = offset;
	}
	
	/**
	 * Get the number of measures which this {@link TabLabel} takes up, based on the given {@link TimeSignature}
	 * @param sig The {@link TimeSignature} defining the length of a measure
	 * @return The length in measures
	 */
	public double getMeasures(TimeSignature sig){
		return sig.toMeasures(this.getLength());
	}
	
	/**
	 * Set the number of measures which this {@link TabLabel} takes up, based on the given {@link TimeSignature}
	 * @param measures The new length, in measures
	 * @param sig The {@link TimeSignature} defining the length of a measure
	 */
	public void setMeasures(double measures, TimeSignature sig){
		this.setLength(sig.toWholeNotes(measures));
	}
	
	/**
	 * Get the position where this {@link TabLabel} begins
	 * @return The number of whole notes, in number of whole notes
	 */
	public double getBeginningPos(){
		if(this.getBaseSymbol() == null) return 0;
		return this.getBaseSymbol().getPos() + this.getOffset();
	}

	/**
	 * Get the position where this {@link TabLabel} ends
	 * @return The number of whole notes, in number of whole notes
	 */
	public double getEndingPos(){
		return this.getBeginningPos() + this.getLength();
	}

	/**
	 * Change the length of this {@link TabLabel} in the specified direction
	 * @param length The total length to add, in number of whole notes, can be negative to subtract length. 
	 * 	With negative length, the end of the label stays in the same position, and the beginning becomes earlier
	 */
	public void modifyLength(double length){
		this.setLength(this.getLength() + Math.abs(length));
		if(length < 0){
			this.shift(length);
		}
	}
	
	/**
	 * Increase the length of this {@link TabLabel} by the given amount, keeping the end of the label in the same spot.
	 * @param amount The amount to move, values zero and below do nothing
	 */
	public void expandBackwards(double amount){
		if(amount > 0) this.modifyLength(-amount);
	}
	
	/**
	 * Increase the length of this {@link TabLabel} by the given amount, keeping the beginning of the label in the same spot.
	 * @param amount The amount to move, values zero and below do nothing
	 */
	public void expandForwards(double amount){
		if(amount > 0) this.modifyLength(amount);
	}
	
	/**
	 * Shift this entire label over by the given amount
	 * @param length The amount in whole notes, positive to shift forward, negative to shift backwards
	 */
	public void shift(double length){
		this.setOffset(this.getOffset() + length);
	}
	
}
