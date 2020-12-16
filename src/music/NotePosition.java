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
	 * Convert this {@link NotePosition} value so that it is the same number of whole notes, but in the new time signature
	 * @param newTime The {@link TimeSignature} to convert to
	 * @param oldTime The {@link TimeSignature} which this position was in
	 */
	public void retime(TimeSignature newTime, TimeSignature oldTime){
		this.setValue(newTime.retime(oldTime, this.getValue()));
	}
	
	/**
	 * Convert this {@link NotePosition} value so that it stays in the same measure and same relative position in the measure.<br>
	 * i.e. if a note was one quarter note after the second measure, it will still be one quarter note after the second measure, but in the new time signature.
	 * @param newTime The {@link TimeSignature} to convert to
	 * @param oldTime The {@link TimeSignature} which this position was in
	 * @return true if the position is in the same measure, false otherwise
	 */
	public boolean retimeMeasure(TimeSignature newTime, TimeSignature oldTime){
		double v = this.getValue();
		
		// Determine the measure of this position, as well as its position in the measure
		int measure = (int)v;
		double space = v - measure;
		
		// Find the rescaled position
		double newSpace = newTime.retime(oldTime, space);
		this.setValue(measure + newSpace);
		
		// Determine if the position was inside the measure
		return newSpace < 1;
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
