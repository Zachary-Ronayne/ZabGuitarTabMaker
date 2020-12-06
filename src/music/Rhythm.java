package music;

/**
 * An object representing a musical Rhythm via a fraction of the duration and the number of units dividing up a whole note
 * @author zrona
 */
public class Rhythm{
	
	/**
	 * The amount of units of this {@link Rhythm} i.e. if this.units is 4, and this.duration is 1, then it is 1 quarter note
	 */
	private int duration;
	
	/**
	 * The unit dividing up a whole note. If this value is 1, then this.duration represents a number of whole notes,
	 * 	if this number is a 2, then this.duration represents a number of half notes
	 */
	private int unit;
	
	/**
	 * TODO
	 */
	public Rhythm(){
		
	}
	
	/**
	 * TODO rename?
	 * Determine the length of this {@link Rhythm} based on the duration and unit 
	 * @return The length
	 */
	public double getRatioLength(){
		// TODO
		return 1;
	}
	
}
