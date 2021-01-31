package music;

import java.io.PrintWriter;
import java.util.Scanner;

import util.Copyable;
import util.MathUtils;
import util.ObjectUtils;
import util.Saveable;

/**
 * An object representing a musical rhythm via a fraction of the duration and the number of units dividing up a whole note
 * @author zrona
 */
public class Rhythm implements Copyable<Rhythm>, Saveable{
	
	/** 
	 * The amount of units of this {@link Rhythm} i.e. if this.units is 4, and this.duration is 1, then it is 1 quarter note. 
	 * Cannot be less than zero, if it is set to less than zero, it will be set to zero 
	 */
	private int duration;
	
	/**
	 * The unit dividing up a whole note. If this value is 1, then this.duration represents a number of whole notes, 
	 * 	if this number is a 2, then this.duration represents a number of half notes. 
	 * Cannot be less than 1, if it is less than 1, it will be set to 1
	 */
	private int unit;
	
	/**
	 * Create a new {@link Rhythm} with the given duration and unit.<br>
	 * Also simplifies the ratio of duration and unit if applicable
	 * @param duration see {@link #duration}
	 * @param unit see {@link #unit}
	 */
	public Rhythm(int duration, int unit){
		this.setDuration(duration);
		this.setUnit(unit);
		this.simplify();
	}
	
	/***/
	@Override
	public Rhythm copy(){
		return new Rhythm(this.getDuration(), this.getUnit());
	}
	
	/** @return See {@link #duration} */
	public int getDuration(){
		return duration;
	}
	
	/** @param duration See {@link #duration} */
	private void setDuration(int duration){
		if(duration < 0) duration = 0;
		this.duration = duration;
	}

	/** @return The See {@link #unit} */
	public int getUnit(){
		return unit;
	}

	/** @param unit See {@link #unit} */
	private void setUnit(int unit){
		if(unit < 1) unit = 1;
		this.unit = unit;
	}
	
	/**
	 * Bring the {@link #duration} and {@link #unit} to their most simple form
	 */
	private void simplify(){
		int divisor = MathUtils.gcd(this.getDuration(), this.getUnit());
		this.setDuration(this.getDuration() / divisor);
		this.setUnit(this.getUnit() / divisor);
	}

	/**
	 * Determine the length of this {@link Rhythm} based on the duration and unit 
	 * @return The length in terms of number of whole notes
	 */
	public double getLength(){
		return (double)this.getDuration() / this.getUnit();
	}
	
	/***/
	@Override
	public boolean load(Scanner reader){
		// Load in the two values for a Rhythm
		Integer[] load = Saveable.loadInts(reader, 2);
		// Return false if loading caused an error
		if(load == null) return false;

		// Advance to the next line
		if(!Saveable.nextLine(reader)) return false;
		
		// Set both the values and return success
		this.setDuration(load[0]);
		this.setUnit(load[1]);
		return true;
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Save both the duration and unit on one line
		if(!Saveable.saveToStrings(writer, new Integer[]{this.getDuration(), this.getUnit()})) return false;
		
		// End the line
		return Saveable.newLine(writer);
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		Rhythm r = (Rhythm)obj;
		return	super.equals(obj) ||
				this.getDuration() == r.getDuration() &&
				this.getUnit() == r.getUnit();
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[Rhythm: ");
		b.append(this.getDuration());
		b.append(" ");
		b.append(this.getUnit());
		b.append(" notes]");
		return b.toString();
	}
	
}
