package music;

import util.Copyable;
import util.ObjectUtils;

/**
 * An object keeping track of a musical time signature
 * @author zrona
 */
public class TimeSignature implements Copyable<TimeSignature>{
	
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
	
	/***/
	@Override
	public TimeSignature copy(){
		return new TimeSignature(this.getUpper(), this.getLower());
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
	
	/**
	 * Convert the given time value so that it is the same number of whole notes in the old time signature,
	 * 	but now in this {@link TimeSignature}
	 * @param oldTime The {@link TimeSignature} of the given value
	 * @param time The duration of the note to retime
	 * @return The retimed value
	 */
	public double retime(TimeSignature oldTime, double time){
		return time * oldTime.getRatio() / this.getRatio();
	}
	
	/***
	 * Guess the rhythmic information of a length based on this {@link TimeSignature}
	 * @param duration The length in number of whole notes for the rhythmic information
	 * @return The guessed {@link Rhythm}. Only can guess from whole notes to 16th notes, no dotted notes or triplets
	 */
	public Rhythm guessRhythmWholeNotes(double duration){
		return this.guessRhythm(duration, false);
	}
	
	
	/***
	 * Guess the rhythmic information of a length based on this {@link TimeSignature}
	 * @param duration The length in number of measures for the rhythmic information
	 * @return The guessed {@link Rhythm}. Only can guess from whole notes to 16th notes, no dotted notes or triplets
	 */
	public Rhythm guessRhythmMeasures(double duration){
		return this.guessRhythm(duration, true);
	}
	
	/***
	 * Guess the rhythmic information of a length based on this {@link TimeSignature}
	 * @param duration The length of the note
	 * @param measures true if duration is in terms of the number of measures for the rhythmic information, 
	 * 	false if duration is in terms of the number of whole notes
	 * @return The guessed {@link Rhythm}. Only can guess from whole notes to 16th notes, no dotted notes or triplets
	 */
	public Rhythm guessRhythm(double duration, boolean measures){
		Rhythm r = new Rhythm(0, 1);
		// The length of the rhythm to guess converted to be in terms of number of whole notes
		if(measures) duration = duration * this.getRatio();
		// The closest to a whole number a note has been so far
		double best = 1;
		// The maximum rhythmic division to end the loop
		int loopEnd = 16;
		
		// Go through each note type which can be guessed, and find the one which best approximates the distance
		for(int n = 1; n <= loopEnd; n *= 2){
			// The number of the current number of notes, mean number of whole notes, half notes, etc
			double amount = duration * n;
			
			// The closest integer amount of notes of that type which can be made
			int closeAmount = (int)Math.round(amount);
			
			// The amount of the duration not accounted for by the integer amount of notes
			double remain = Math.abs(closeAmount - amount);
			
			/*
			 * If the amount of the duration remaining is less than the best so far,
			 * 	and there is at least one note, then update the rhythm to use that length.
			 * Also set the rhythm if the lowest interval is found
			 */
			if(amount >= 1 && remain < best || n == loopEnd){
				best = remain;
				r.setDuration(closeAmount);
				r.setUnit(n);
			}
		}
		r.simplify();
		return r;
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
