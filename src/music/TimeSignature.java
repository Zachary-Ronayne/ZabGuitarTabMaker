package music;

import util.ObjectUtils;

/**
 * An object keeping track of a musical time signature
 * @author zrona
 */
public class TimeSignature{
	
	/** The upper number of the time signature, i.e. the "how many" part, must be greater than 0 */
	private int upper;
	/** The lower number of the time signature, i.e. the "how long" part, must be greater than 0, can be values besides a power of 2 */
	private int lower;
	
	/**
	 * Create a new {@link TimeSignature} with the given values
	 * @param upper See {@link #upper}
	 * @param lower See {@link #lower}
	 */
	public TimeSignature(int upper, int lower){
		this.setUpper(upper);
		this.setLower(lower);
	}
	
	/**
	 * Get the upper value of this {@link TimeSignature}
	 * @return see {@link #upper}
	 */
	public int getUpper(){
		return upper;
	}

	/**
	 * Set the upper value of this {@link TimeSignature}
	 * @param upper see {@link #upper}
	 */
	public void setUpper(int upper){
		if(upper <= 0) throw new IllegalArgumentException("Upper must be greater than 0");
		this.upper = upper;
	}

	/**
	 * Get the lower value of this {@link TimeSignature}
	 * @return see {@link #lower}
	 */
	public int getLower(){
		return lower;
	}

	/**
	 * Set the lower value of this {@link TimeSignature}
	 * @param lower see {@link #lower}
	 */
	public void setLower(int lower){
		if(lower <= 0) throw new IllegalArgumentException("lower must be greater than 0");
		this.lower = lower;
	}
	
	/**
	 * Get the ratio of the upper value to the lower value
	 * @return The ratio
	 */
	public double getRatio(){
		return (double)this.getUpper() / this.getLower();
	}
	
	/**
	 * Get the Symbol representing this {@link TimeSignature}, i.e. four four time would be "4/4"
	 * @return The symbol
	 */
	public String symbol(){
		String s = String.valueOf(this.getUpper());
		s = s.concat("/");
		s = s.concat(String.valueOf(this.getLower()));
		return s;
	}
	
	/**
	 * Quantize the given position to the nearest place in a measure
	 * @param pos The position in the time signature 
	 * @param divisor The amount to divide up the units of a whole note.<br>
	 * 	i.e. use 4 to quantize to quarter notes, 3 for whole note triplets, use 6 for dotted quarter notes, etc
	 * @return The quantized value for pos
	 */
	public double quantize(double pos, double divisor){
		double d = divisor * this.getRatio();
		return Math.round(pos * d) / d;
	}
	
	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		TimeSignature t = (TimeSignature)obj;
		return	super.equals(obj) ||
				this.getUpper() == t.getUpper() &&
				this.getLower() == t.getLower();
	}
	
}
