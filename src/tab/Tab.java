package tab;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import appUtils.ZabAppSettings;
import appUtils.ZabSettings;
import music.Pitch;
import music.Rhythm;
import music.TimeSignature;
import tab.symbol.TabNote;
import tab.symbol.TabSymbol;
import util.Copyable;
import util.ObjectUtils;
import util.Saveable;

/**
 * An object representing a tablature diagram for a string instrument.
 * @author zrona
 */
public class Tab implements Copyable<Tab>, Saveable{
	
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
	 * Keeps track of if this {@link Tab} should hold notes with rhythmic information or not.<br>
	 * true to hold rhythmic information, false to not hold it
	 */
	private boolean usesRhythm;
	
	/**
	 * Create a Tab using the given list of Strings in 4/4 time
	 * @param strings see {@link #strings}
	 * @param timeSignature  See {@link #timeSignature}
	 * @param usesRhythm See {@link #usesRhythm}
	 */
	public Tab(ArrayList<TabString> strings, TimeSignature timeSignature, boolean usesRhythm){
		this.setStrings(strings);
		this.setTimeSignature(timeSignature);
		this.setUsesRhythm(usesRhythm);
	}
	
	/**
	 * Create a Tab using the given list of Strings in 4/4 time using no rhythmic information
	 * @param strings see {@link #strings}
	 * @param timeSignature The {@link TimeSignature} to use
	 */
	public Tab(ArrayList<TabString> strings, TimeSignature timeSignature){
		this(strings, timeSignature, false);
	}
	
	/**
	 * Create a Tab using the given list of Strings in 4/4 time using no rhythmic information
	 * @param strings see {@link #strings}
	 */
	public Tab(ArrayList<TabString> strings){
		this(strings, new TimeSignature(4, 4));
	}
	
	/**
	 * Create an empty Tab with no strings in 4/4 time using no rhythmic information
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
		
		// Copy other fields
		tab.setTimeSignature(ObjectUtils.copy(this.getTimeSignature()));
		tab.setUsesRhythm(this.usesRhythm());
		
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
	 * Determine if this {@link Tab} uses rhythmic information
	 * @return see {@link #usesRhythm}
	 */
	public boolean usesRhythm(){
		return usesRhythm;
	}
	
	/**
	 * Set whether or not this {@link Tab} uses rhythmic information.<br>
	 * Also updates all notes currently in this tab. If rhythms are set to be used, all notes rhythmic values are guessed
	 * @return see {@link #usesRhythm}
	 */
	public void setUsesRhythm(boolean usesRhythm){
		this.usesRhythm = usesRhythm;
		if(this.usesRhythm) this.addRhythm(null);
		else this.removeRhythms();
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
	 * @param deleteExtra If rescale is true, this parameter does nothing.<br>
	 * 	If rescale is false, then if deleteExtra is true, then all symbols remain in the same numerical measure at the same position ratios, 
	 * 	and any symbols which begin outside of that measure are deleted from the tab.<br>
	 * 	If deleteExtra is false, then all symbols stay the same duration and position relative to one another, ignoring the time signature's new measure boundaries. 
	 * 	i.e. 3 measures of 4/4 will become 4 measures of 3/4 where all symbols keep the same spacing
	 */
	public void retime(TimeSignature newTime, boolean rescale, boolean deleteExtra){
		TimeSignature oldTime = this.getTimeSignature();
		this.setTimeSignature(newTime);
		
		// Nothing needs to happen if rescale is true
		if(rescale) return;
		
		if(deleteExtra){
			// Go through every note on every string
			for(TabString s : this.getStrings()){
				// Make a list to store all of the symbols to be removed
				ArrayList<TabPosition> toDelete = new ArrayList<TabPosition>();
				for(TabPosition p : s){
					// Retime the note
					boolean outside = !p.retimeMeasure(newTime, oldTime);
					// If the position is outside the measure, remove it
					if(outside) toDelete.add(p);
				}
				// Remove all symbols which were set to be removed
				s.removeAll(toDelete);
			}
		}
		else{
			// Retime every note on every string
			for(TabString s : this.getStrings()){
				for(TabPosition p : s) p.retime(newTime, oldTime);
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
	 * Put a single {@link TabNote} on the given string with the given pitch at the given position.<br>
	 * The position value is also automatically quantized
	 * @param string The index of the string to place the note on
	 * @param fret The fret number of the note
	 * @param pos The position value of the note. See {@link TabSymbol#position}
	 * @return The {@link TabPosition} containing the placed {@link TabNote}, 
	 * 	this method guarantees that the {@link TabSymbol} in the returned {@link SymbolHolder} is a {@link TabNote}, 
	 * 	or that it is null because the note could not be placed.
	 */
	public TabPosition placeQuantizedNote(int string, int fret, double pos){
		return this.getStrings().get(string).placeQuantizedNote(this.getTimeSignature(), fret, pos);
	}
	
	/**
	 * Remove all notes from the tab
	 */
	public void clearNotes(){
		for(TabString s : this.getStrings()) s.clear();
	}
	
	/**
	 * Determine if any of the strings in this {@link Tab} have symbols
	 * @return true if this {@link Tab} has no strings, or all of its strings have no notes, false otherwise
	 */
	public boolean isEmpty(){
		if(this.getStrings().isEmpty()) return true;
		for(TabString s : this.getStrings()){
			if(!s.isEmpty()) return false;
		}
		return true;
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
	
	/**
	 * Remove all rhythmic information from notes in this tab
	 */
	public void removeRhythms(){
		this.usesRhythm = false;
		for(TabString s : this.getStrings()){
			for(int i = 0; i < s.size(); i++){
				s.replace(i, s.symbol(i).removeRhythm());
			}
		}
	}
	
	/**
	 * Provide rhythmic information for every not in this tab
	 * @param rhythm The rhythm to apply, or null to give rhythms based on the space between symbols.
	 */
	public void addRhythm(Rhythm rhythm){
		// Guess the rhythms if one wasn't provided
		if(rhythm == null) this.guessRhythms();
		// Set all of the rhythms otherwise
		else this.setRhythmAll(rhythm);
	}
	
	/**
	 * For each note on each string of this Tab, give each of them a rhythm based on the space between other notes
	 */
	public void guessRhythms(){
		ZabSettings settings = ZabAppSettings.get();
		this.usesRhythm = true;
		for(TabString s : this.getStrings()){
			int size = s.size();
			
			// Skip the string if there is not at least one note
			if(size < 1) continue;
			
			// Going one less than the number of notes on the string
			for(int i = 0; i < size - 1; i++){
				// Guess and set the rhythm based on the space between the notes
				TabPosition p = s.get(i);
				Rhythm r = this.getTimeSignature().guessRhythmMeasures(s.get(i + 1).getPos() - p.getPos());
				s.replace(i, p.getSymbol().convertToRhythm(r));
			}
			// Set the rhythm of the last note
			s.replace(size - 1, s.symbol(size - 1).convertToRhythm(settings.rhythmConversionEndValue()));
		}
	}
	
	/**
	 * Set the rhythm of every symbol in this tab, converting it to a rhythmic note where applicable
	 * @param r The {@link Rhythm} to use
	 */
	public void setRhythmAll(Rhythm r){
		this.usesRhythm = true;
		for(TabString s : this.getStrings()){
			for(int i = 0; i < s.size(); i++){
				s.replace(i, s.symbol(i).convertToRhythm(r));
			}
		}
	}
	
	/***/
	@Override
	public boolean load(Scanner reader){
		// Load if the tab uses rhythm
		Boolean loadBool = Saveable.loadBool(reader);
		if(loadBool == null) return false;
		this.setUsesRhythm(loadBool);
		
		// Load the time signature
		if(!this.getTimeSignature().load(reader)) return false;
		
		// Load the number of strings in the tab
		Integer size = Saveable.loadInt(reader);
		if(size == null) return false;
		
		// Advance to the next line
		if(!Saveable.nextLine(reader)) return false;
		
		// Load in all of the strings
		ArrayList<TabString> strings = new ArrayList<TabString>();
		this.setStrings(strings);
		for(int i = 0; i < size; i++){
			TabString string = new TabString(new Pitch(0));
			// If a string failed to load, return false
			if(!Saveable.load(reader, string)) return false;
			strings.add(string);
		}
		
		// Loading was successful
		return true;
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Save if this Tab uses rhythm,
		if(!Saveable.saveToString(writer, this.usesRhythm())) return false;

		// Save the time signature
		if(!Saveable.save(writer, this.getTimeSignature())) return false;
		
		// Save the number of strings of this tab
		int size = this.getStrings().size();
		if(!Saveable.saveToString(writer, size, true)) return false;

		// Save each string
		// Put the strings in a usable array for saving
		for(TabString s : this.getStrings()){
			if(!Saveable.save(writer, s)) return false;
		}
		
		// Return save successful
		return true;
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		Tab t = (Tab)obj;
		return	super.equals(obj) ||
				this.getStrings().equals(t.getStrings()) &&
				this.getTimeSignature().equals(t.getTimeSignature()) &&
				this.usesRhythm() == t.usesRhythm();
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[Tab, ");
		b.append(this.getTimeSignature());
		b.append(", usesRhythm: ");
		b.append(this.usesRhythm());
		b.append(", [Strings: ");
		ArrayList<TabString> strs = this.getStrings();
		if(strs.size() > 0){
			TabString last = strs.get(strs.size() - 1);
			for(TabString s : strs){
				b.append(s);
				if(last != s) b.append(", ");
			}
		}
		b.append("]]");
		return b.toString();
	}
	
}