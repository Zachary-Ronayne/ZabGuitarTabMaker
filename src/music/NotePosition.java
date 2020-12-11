package music;

import util.Copyable;
import util.ObjectUtils;

/**
 * An object tracking the position of a musical note in a measure
 * @author zrona
 */
public class NotePosition implements Copyable<NotePosition>{
	
	/**
	 * The position held by this {@link NotePosition} representing the number of measures to this note, 
	 * i.e. 1 means end of the first measure, 1.5 means halfway between the first and second measure
	 */
	private double value;
	
	/**
	 * Create a new {@link NotePosition} with the given value
	 * @param value See {@link #value}
	 */
	public NotePosition(double value){
		this.setValue(value);
	}
	
	/***/
	@Override
	public NotePosition copy(){
		return new NotePosition(this.getValue());
	}
	
	/**
	 * Get the {@link #value} of this {@link NotePosition}
	 * @return The position value
	 */
	public double getValue(){
		return value;
	}

	/**
	 * Set the {@link #value} of this {@link NotePosition}
	 * @param value The position value
	 */
	public void setValue(double value){
		this.value = value;
	}
	
	/**
	 * Add the specified amount to this position
	 * @param value The amount to add to {@link #value}
	 */
	public void addValue(double value){
		this.setValue(this.getValue() + value);
	}
	
	/**
	 * Quantize this position to the nearest place in a measure
	 * @param sig The time signature to base the quantization off of
	 * @param divisor The amount to divide up the units of a whole note.<br>
	 * 	i.e. use 4 to quantize to quarter notes, use 6 to quantize to dotted quarter notes, etc
	 */
	public void quantize(TimeSignature sig, int divisor){
		this.setValue(sig.quantize(this.getValue(), divisor));
	}
	
	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		NotePosition p = (NotePosition)obj;
		return 	super.equals(obj) ||
				this.getValue() == p.getValue();
	}
	
}
