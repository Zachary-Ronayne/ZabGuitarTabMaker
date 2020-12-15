package music;

import util.Copyable;
import util.MathUtils;
import util.ObjectUtils;

/**
 * An object representing a musical rhythm via a fraction of the duration and the number of units dividing up a whole note
 * @author zrona
 */
public class Rhythm implements Copyable<Rhythm>{
	
	/** The amount of units of this {@link Rhythm} i.e. if this.units is 4, and this.duration is 1, then it is 1 quarter note */
	private int duration;
	
	/**
	 * The unit dividing up a whole note. If this value is 1, then this.duration represents a number of whole notes, 
	 * 	if this number is a 2, then this.duration represents a number of half notes
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
		if(this.getUnit() == 0) return;
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
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		Rhythm r = (Rhythm)obj;
		return	super.equals(obj) ||
				this.getDuration() == r.getDuration() &&
				this.getUnit() == r.getUnit();
	}
}
