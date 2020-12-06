package music;

/**
 * An object representing an individual pitch in 12 tone equal temperament
 * @author zrona
 */
public class Pitch{
	
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
	public void setNote(int note){
		this.note = note;
	}

	/**
	 * Get the name of this pitch, i.e. if this note represents E4, then "E4" is returned
	 * @return The pitch name
	 */
	public String getPitchName(){ // TODO
		return "";
	}
	
}
