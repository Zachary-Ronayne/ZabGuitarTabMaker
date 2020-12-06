package music;

import util.MathUtils;

/**
 * An object representing a musical rhythm via a fraction of the duration and the number of units dividing up a whole note
 * @author zrona
 */
public class Rhythm{
	
	/** The amount of units of this {@link Rhythm} i.e. if this.units is 4, and this.duration is 1, then it is 1 quarter note */
	private int duration;
	
	/**
	 * The unit dividing up a whole note. If this value is 1, then this.duration represents a number of whole notes,
	 * 	if this number is a 2, then this.duration represents a number of half notes
	 */
	private int unit;
	
	/**
	 * Create a new {@link Rhythm} with the given duration and unit
	 * @param duration see {@link #duration}
	 * @param unit see {@link #unit}
	 */
	public Rhythm(int duration, int unit){
		this.setDuration(duration);
		this.setUnit(unit);
	}
	
	/**
	 * Get the duration of this {@link Rhythm}
	 * @return The duration, see {@link #duration}
	 */
	public int getDuration(){
		return duration;
	}
	
	/**
	 * Set the duration of this {@link Rhythm}
	 * @param duration The duration, see {@link #duration}
	 */
	public void setDuration(int duration){
		this.duration = duration;
	}

	/**
	 * Get the unit of this {@link Rhythm}
	 * @return The unit, see {@link #unit}
	 */
	public int getUnit(){
		return unit;
	}

	/**
	 * Set the unit of this {@link Rhythm}
	 * @param unit The unit, see {@link #unit}
	 */
	public void setUnit(int unit){
		this.unit = unit;
	}
	
	/**
	 * Bring the {@link #duration} and {@link #unit} to their most simple form
	 */
	public void simplify(){
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
	
}
