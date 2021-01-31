package music;

import java.io.PrintWriter;
import java.util.Scanner;

import util.Copyable;
import util.ObjectUtils;
import util.Saveable;

/**
 * An object representing an individual pitch in 12 tone equal temperament
 * @author zrona
 */
public class Pitch implements Copyable<Pitch>, Saveable{
	
	/**
	 * The number of semitones this {@link Pitch} is from middle C, i.e. C4
	 * Zero is middle C, positive values are higher than middle C, negative values are lower than middle C
	 */
	private int note;
	
	/**
	 * Set the integer note of this Pitch
	 * @param note
	 */
	public Pitch(int note){
		this.setNote(note);
	}
	
	/***/
	@Override
	public Pitch copy(){
		return new Pitch(this.getNote());
	}
	
	/**
	 * Get the integer representing the {@link #note} of his {@link Pitch}
	 * @return The integer
	 */
	public int getNote(){
		return note;
	}

	/**
	 * Set the integer representing the {@link #note} of his {@link Pitch}
	 * @param note The integer
	 */
	private void setNote(int note){
		this.note = note;
	}
	
	/**
	 * Create a tuned version of this pitch up or down by a number of semitones
	 * @param change The change in semitones, use positive for higher pitch, negative for lower pitch
	 * @return The new {@link Pitch}
	 */
	public Pitch tune(int change){
		return new Pitch(this.getNote() + change);
	}
	
	/**
	 * Get the name of this pitch using sharps, i.e. if this note represents E4, then "E4" is returned
	 * @return The pitch name
	 */
	public String getPitchName(){
		return this.getPitchName(false);
	}

	/**
	 * Get the name of this pitch, i.e. if this note represents E4, then "E4" is returned
	 * @param useFlats true to represent relevant notes with flats, false for sharps
	 * @return The pitch name
	 */
	public String getPitchName(boolean useFlats){
		return Music.intToNote(this.getNote(), useFlats);
	}
	
	/***/
	@Override
	public boolean load(Scanner reader){
		// Load the integer for the pitch value
		Integer load = Saveable.loadInt(reader);
		// If the loading failed, return false
		if(load == null) return false;

		// Advance to the next line
		if(!Saveable.nextLine(reader)) return false;
		
		// Set the pitch value and return success
		this.setNote(load);
		return true;
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Save the pitch value and nothing else
		if(!Saveable.saveToString(writer, this.getNote())) return false;
		// End the line
		return Saveable.newLine(writer);
	}
	
	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		Pitch p = (Pitch)obj;
		return 	super.equals(obj) ||
				this.getNote() == p.getNote();
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[Pitch: ");
		b.append(Music.intToNote(this.getNote(), false));
		b.append("]");
		return b.toString();
	}
	
}
