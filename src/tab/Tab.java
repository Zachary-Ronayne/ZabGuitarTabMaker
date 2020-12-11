package tab;

import java.util.ArrayList;

import music.Pitch;
import music.TimeSignature;
import util.Copyable;
import util.ObjectUtils;

/**
 * An object representing a tablature diagram for a string instrument.
 * @author zrona
 */
public class Tab implements Copyable<Tab>{
	
	/**
	 * An ordered list of all of the strings in this tab.
	 * The first element is the string typically with the highest pitch,
	 * 	and the last element is the string typically with the lowest pitch.<br>
	 * For example, for a guitar in standard tuning, index 0 should be the high E string,
	 * 	and index 5 should be the low E string
	 */
	private ArrayList<TabString> strings;
	
	/** The {@link TimeSignature} used by this {@link Tab} to track time */
	private TimeSignature timeSignature;
	
	/**
	 * Create a Tab using the given list of Strings in 4/4 time
	 * @param strings see {@link #strings}
	 * @param timeSignature The {@link TimeSignature} to use
	 */
	public Tab(ArrayList<TabString> strings, TimeSignature timeSignature){
		this.setStrings(strings);
		this.setTimeSignature(timeSignature);
	}
	
	/**
	 * Create a Tab using the given list of Strings in 4/4 time
	 * @param strings see {@link #strings}
	 */
	public Tab(ArrayList<TabString> strings){
		this(strings, new TimeSignature(4, 4));
	}
	
	/**
	 * Create an empty Tab with no strings in 4/4 time
	 */
	public Tab(){
		this(new ArrayList<TabString>());
	}

	/***/
	@Override
	public Tab copy(){
		// Make an empty tab
		Tab tab = new Tab();
		
		// Copy over each string
		for(TabString s : this.getStrings()){
			tab.getStrings().add(s.copy());
		}
		return tab;
	}
	
	/**
	 * Get the {@link TabString} objects used in this {@link Tab}
	 * @return See {@link #strings}
	 */
	public ArrayList<TabString> getStrings(){
		return strings;
	}

	/**
	 * Set the {@link TabString} objects used in this {@link Tab}
	 * @param strings See {@link #strings}
	 */
	public void setStrings(ArrayList<TabString> strings){
		this.strings = strings;
	}
	
	/**
	 * Get the {@link TimeSignature} used by this {@link Tab}
	 * @return see {@link #timeSignature}
	 */
	public TimeSignature getTimeSignature(){
		return timeSignature;
	}

	/**
	 * Set the {@link TimeSignature} used by this {@link Tab}
	 * @param timeSignature see {@link #timeSignature}
	 */
	public void setTimeSignature(TimeSignature timeSignature){
		this.timeSignature = timeSignature;
	}
	
	/**
	 * Quantize all {@link TabString} objects in this {@link Tab}
	 * @param divisor The amount to divide up the units of a whole note.<br>
	 * 	i.e. use 4 to quantize to quarter notes, use 6 to quantize to dotted quarter notes, etc
	 */
	public void quantize(int divisor){
		for(TabString s : this.getStrings()) s.quantize(this.getTimeSignature(), divisor);
	}

	/**
	 * Get the note integer, as defined in {@link Pitch#note} of the root note of the specified string
	 * @param string The index of the string, not the actual string number,
	 * 	i.e. use 0 for the first string, 1 for the second string, etc.
	 * @return The note integer
	 */
	public int getRootNote(int string){
		return this.getStrings().get(string).getRootNote();
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		Tab t = (Tab)obj;
		return	super.equals(obj) ||
				this.getStrings().equals(t.getStrings());
	}
	
}