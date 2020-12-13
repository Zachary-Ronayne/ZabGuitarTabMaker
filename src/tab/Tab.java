package tab;

import java.util.ArrayList;

import music.NotePosition;
import music.Pitch;
import music.TimeSignature;
import tab.symbol.TabSymbol;
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
	 * Change the {@link TimeSignature} of this {@link Tab} and update the positions of all symbols in this {@link Tab} 
	 * 	based on the given parameters 
	 * @param newTime The new time signature to use. See {@link #timeSignature}
	 * @param rescale true to scale the symbols to the new time signature, meaning all symbols filling a measure will have the same ratios between them. 
	 * 	i.e. a 4/4 measure with 4 quarter symbols converted to a 3/4 measure will have 4 evenly spaced symbols in a single measure of 3/4<br>
	 * 	Use false to keep the positions of symbols on the same intervals. i.e. a 4/4 measure with 4 quarter symbols converted to a 3/4 measure 
	 * 	will have 3 quarter symbols in that 3/4 measure, with one left over.<br>
	 * 	What is done with the left overs is determined via deleteExtra
	 * @param deleteExtra If rescale is true, this parameter does nothing. If rescale is false, then if deleteExtra is true, 
	 * 	then all symbols remain in the same numerical measure, and any symbols which begin outside of that measure are deleted from the tab.<br>
	 * 	If deleteExtra is false, then all symbols stay the same duration and position relative to one another, ignoring the time signature's new measure boundries. 
	 * 	i.e. 3 measures of 4/4 will become 4 measures of 3/4 where all symbols keep the same spacing
	 */
	public void retime(TimeSignature newTime, boolean rescale, boolean deleteExtra){
		this.setTimeSignature(newTime);
		
		// Nothing needs to happen if rescale is true
		if(!rescale){
			// TODO abstract out some of this functionality
			
			if(deleteExtra){
				// Go through every note on every string
				for(TabString s : this.getStrings()){
					// Make a list to store all of the symbols to be removed
					ArrayList<TabSymbol> toDelete = new ArrayList<TabSymbol>();
					for(TabSymbol t : s){
						// TODO abstract out this calculation?
						
						// Get the position object and value of the symbol
						NotePosition p = t.getPos();
						double v = p.getValue();
						
						// Determine the measure of the position, as well as its position in the measure
						int measure = (int)v;
						double space = v - measure;
						
						// Find the rescaled position
						double newSpace = space / newTime.getRatio();
						
						// If the position is outside the measure, remove it
						if(newSpace > 1) toDelete.add(t);
						// Otherwise, set the new value for the note
						else p.setValue(measure + newSpace);
					}
					// Remove all symbols which were set to be removed
					s.removeAll(toDelete);
				}
			}
			else{
				// Go through every note on every string
				for(TabString s : this.getStrings()){
					for(TabSymbol t : s){
						// TODO abstract out this calculation?
						
						// Update the position based on the new time signature
						NotePosition p = t.getPos();
						p.setValue(p.getValue() / newTime.getRatio());
					}
				}
			}
		}
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