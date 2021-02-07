package music;

import java.io.PrintWriter;
import java.util.Scanner;

import util.Copyable;
import util.ObjectUtils;
import util.Saveable;

/**
 * An object tracking the position of a musical note in a measure
 * @author zrona
 */
public class NotePosition implements Copyable<NotePosition>, Comparable<NotePosition>, Saveable{
	
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
	private void setValue(double value){
		this.value = value;
	}
	
	/**
	 * Given a position value, both in number of measures, determine if it is in the same measure as this position
	 * @param pos The other position
	 * @return true if they are in the same measure, false otherwise
	 */
	public boolean sameMeasure(NotePosition pos){
		return this.sameMeasure(pos.getValue());
	}
	
	/**
	 * Given a position value, both in number of measures, determine if it is in the same measure as this position
	 * @param pos The other position
	 * @return true if they are in the same measure, false otherwise
	 */
	public boolean sameMeasure(double pos){
		return (int)pos == (int)this.getValue();
	}
	
	/**
	 * Create a note position with the same value as this one, plus specified amount
	 * @param value The amount to add to {@link #value}
	 * @return The new {@link NotePosition}
	 */
	public NotePosition added(double value){
		return new NotePosition(this.getValue() + value);
	}
	
	/**
	 * Create a version of this {@link NotePosition} value so that it is the same number of whole notes, but in the new time signature
	 * @param newTime The {@link TimeSignature} to convert to
	 * @param oldTime The {@link TimeSignature} which this position was in
	 * @return The converted {@link NotePosition}
	 */
	public NotePosition retime(TimeSignature newTime, TimeSignature oldTime){
		return new NotePosition(newTime.retime(oldTime, this.getValue()));
	}
	
	/**
	 * Create a version of this {@link NotePosition} value so that it stays in the same measure and same relative position in the measure.<br>
	 * i.e. if a note was one quarter note after the second measure, it will still be one quarter note after the second measure, but in the new time signature.
	 * @param newTime The {@link TimeSignature} to convert to
	 * @param oldTime The {@link TimeSignature} which this position was in
	 * @return The new NotePosition
	 */
	public NotePosition retimeMeasure(TimeSignature newTime, TimeSignature oldTime){
		double v = this.getValue();
		
		// Determine the measure of this position, as well as its position in the measure
		int measure = (int)v;
		double space = v - measure;
		
		// Find the rescaled position
		double newSpace = newTime.retime(oldTime, space);
		return new NotePosition(measure + newSpace);
	}
	
	/**
	 * Create a quantized version of this position to the nearest place in a measure
	 * @param sig The time signature to base the quantization off of
	 * @param divisor The amount to divide up the units of a whole note.<br>
	 * 	i.e. use 4 to quantize to quarter notes, use 6 to quantize to dotted quarter notes, etc
	 */
	public NotePosition quantize(TimeSignature sig, double divisor){
		return new NotePosition(sig.quantize(this.getValue(), divisor));
	}
	
	/***/
	@Override
	public boolean load(Scanner reader){
		// Load the position value
		Double load = Saveable.loadDouble(reader);
		// If the load failed, return false
		if(load == null) return false;

		// Advance to the next line
		if(!Saveable.nextLine(reader)) return false;
		
		// Set the value and return success
		this.setValue(load);
		return true;
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Save the position value and nothing else
		if(!Saveable.saveToString(writer, this.getValue())) return false;
		
		// End the line
		return Saveable.newLine(writer);
	}
	
	/***/
	@Override
	public int compareTo(NotePosition p){
		double v1 = this.getValue();
		double v2 = p.getValue();
		if(v1 < v2) return -1;
		return v1 > v2 ? 1 : 0;
	}
	
	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		NotePosition p = (NotePosition)obj;
		return 	super.equals(obj) ||
				this.getValue() == p.getValue();
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[NotePosition, position: ");
		b.append(this.getValue());
		b.append("]");
		return b.toString();
	}
	
}
