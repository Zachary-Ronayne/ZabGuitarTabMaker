package tab;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import appUtils.ZabAppSettings;
import appUtils.settings.TabSettings;
import music.Music;
import music.NotePosition;
import music.Pitch;
import music.TimeSignature;
import tab.symbol.TabDeadNote;
import tab.symbol.TabNote;
import tab.symbol.TabPitch;
import tab.symbol.TabSymbol;
import util.ArrayUtils;
import util.Copyable;
import util.ObjectUtils;
import util.Saveable;

/**
 * A class representing one string of a tablature
 * @author zrona
 */
public class TabString extends ArrayList<TabPosition> implements Copyable<TabString>, Saveable{
	private static final long serialVersionUID = 1L;

	/** The {@link Pitch} which this {@link TabString} is tuned to, i.e. the note of this string when played open. */
	private Pitch rootPitch;
	
	/**
	 * Create a new empty string for a tab with the given root
	 * @param root See {@link #rootPitch}
	 */
	public TabString(Pitch root){
		super();
		this.setRootPitch(root);
	}
	
	/**
	 * Create a new {@link TabString} with the given note and octave as the root note
	 * @param note The note, defined by constants in {@link Music}
	 * @param octave The octave number, i.e. use 4 for middle C
	 */
	public TabString(String note, int octave){
		this(new Pitch(Music.createNote(note, octave)));
	}
	
	/***/
	@Override
	public TabString copy(){
		// Make a new TabString with the same root pitch
		TabString string = new TabString(this.getRootPitch().copy());
		
		// Copy over each of the symbols
		for(TabPosition p : this){
			string.add(p.copy());
		}
		return string;
	}
	
	/**
	 * Get the root pitch of this tab string
	 * @return See {@link #rootPitch}
	 */
	public Pitch getRootPitch(){
		return rootPitch;
	}

	/**
	 * Set the root pitch of this tab string
	 * @param rootPitch See {@link #rootPitch}
	 */
	public void setRootPitch(Pitch rootPitch){
		this.rootPitch = rootPitch;
	}
	
	/**
	 * Get the note integer, as defined in {@link Pitch#note} of the root note of this string
	 * @return The note integer
	 */
	public int getRootNote(){
		return this.getRootPitch().getNote();
	}
	
	/**
	 * Get the {@link TabSymbol} at the given index
	 * @param i The index
	 * @return The {@link TabString}
	 */
	public TabSymbol symbol(int i){
		return this.get(i).getSymbol();
	}
	
	/**
	 * Replace the {@link TabSymbol} at the given index with the given symbol.<br>
	 * Note that the position of the original note will not be changed, i.e. this only sets pitch, rhythmic, and modifier data, 
	 * the position of the given symbol is ignored, and the given symbol's position is replaced by the position of the symbol it is replacing
	 * @param i The index
	 * @param symbol The new symbol
	 * @return The symbol which was replaced
	 */
	public TabSymbol replace(int i, TabSymbol symbol){
		TabPosition p = this.get(i);
		TabSymbol old = p.getSymbol();
		TabPosition newP = p.copySymbol(symbol);
		this.set(i, newP);
		return old;
	}

	/**
	 * Adds the given {@link TabPosition} to the list based on its position in increasing order.<br>
	 * Essentially, ensures that all positions are always sorted by the position field
	 * @param e The {@link TabPosition} to add
	 * @return true if the position was added, false otherwise
	 */
	@Override
	public boolean add(TabPosition e){
		if(e == null) return false;
		return ArrayUtils.insertSorted(this, e, false);
	}
	
	/**
	 * Adds the given {@link TabSymbol} to the string at the given position.<br>
	 * Essentially, ensures that all positions are always sorted by the position field
	 * @param e The {@link TabSymbol} to add
	 * @param pos The {@link NotePosition} to add the symbol at
	 * @return true if the symbol was added, false otherwise
	 */
	public boolean add(TabSymbol e, NotePosition pos){
		return this.add(new TabPosition(e, pos));
	}
	
	/**
	 * Adds the given {@link TabSymbol} to the string at the given position.<br>
	 * Essentially, ensures that all positions are always sorted by the position field
	 * @param e The {@link TabSymbol} to add
	 * @param pos The value for a {@link NotePosition} to add the symbol at
	 * @return true if the symbol was added, false otherwise
	 */
	public boolean add(TabSymbol e, double pos){
		return this.add(e, new NotePosition(pos));
	}
	
	/**
	 * Set the {@link TabSymbol} of the note at the given index
	 * @param i The index
	 * @param symbol The new symbol
	 * @return The {@link TabPosition} which used to be at the index, 
	 * or null if the index was invalid, or if symbol was null
	 */
	public TabPosition setSymbol(int i, TabSymbol symbol){
		if(i < 0 || i > this.size() - 1 || symbol == null) return null;
		return this.set(i, this.get(i).copySymbol(symbol));
	}
	
	/**
	 * Create a list of all of the notes in this {@link TabString}. This allocates a new array, 
	 * and should not be used when high performance is needed
	 * @return The list
	 */
	public ArrayList<TabSymbol> getAll(){
		ArrayList<TabSymbol> all = new ArrayList<TabSymbol>();
		for(TabPosition p : this) all.add(p.getSymbol());
		return all;
	}
	
	/**
	 * Remove the {@link TabPosition} containing the given {@link TabSymbol}, at the specified position
	 * @param s The symbol
	 * @param pos The position
	 * @return True if the position was removed, false otherwise
	 */
	public boolean remove(TabSymbol s, double pos){
		TabPosition p = findPosition(pos);
		if(p == null) return false;
		else return this.remove(p);
	}
	
	/**
	 * Use a binary search to find the given Object and remove it from this {@link TabString}. 
	 * This will only remove an object if the parameter is a {@link TabPosition}
	 */
	@Override
	public boolean remove(Object o){
		if(!ObjectUtils.isType(o, TabPosition.class)) return false;
		int index = this.findIndex(((TabPosition)o).getPos());
		if(index >= this.size() || !o.equals(this.get(index))) return false;
		super.remove(index); // At this point, index is guaranteed to be valid
		return true;
	}
	
	/**
	 * Find the index of the symbol at the exact given position
	 * @param pos The position of the symbol to find, if the floating point positions do not match exactly, the position will not be found 
	 * @return The index found, or the index to insert a note to insert it in a sorted order. 
	 * 	This returned value will always be at least zero, and at most equal to the number of notes on the string
	 */
	public int findIndex(double pos){
		// The TabDeadNote is used as a simple placeholder for the position, because TabPositions compare based on their NotePosition value
		return ArrayUtils.binarySearch(this, new TabPosition(new TabDeadNote(), pos), true);
	}

	/**
	 * Find the {@link TabPosition} at the exact given position
	 * @param pos The position of the symbol to find, if the floating point positions do not match exactly, the position will not be found
	 * @return The found {@link TabPosition} or null if one was not found
	 */
	public TabPosition findPosition(double pos){
		int index = this.findIndex(pos);
		if(index < this.size()){
			// If the same position at the index is found, return it
			TabPosition p = this.get(index);
			if(p.getPos() == pos) return p;
		}
		
		// Otherwise the exact position was not found, return null
		return null;
	}
	
	/**
	 * Overrides the standard contains method with a binary search
	 */
	@Override
	public boolean contains(Object obj){
		if(!ObjectUtils.isType(obj, TabPosition.class)) return false;
		TabPosition p = (TabPosition)obj;
		return p.equals(this.findPosition(p.getPos()));
	}
	
	/**
	 * Find the position of the symbol with the highest value
	 * @return The farthest out this tab goes, in measures, or zero if this string is empty
	 */
	public double tabLength(){
		if(this.isEmpty()) return 0;
		return this.get(this.size() - 1).getPos();
	}
	
	/**
	 * Put a single {@link TabNote} on this string with the given pitch at the given position.<br>
	 * The position value is also automatically quantized
	 * @param sig The {@link TimeSignature} to use for quantizing the note
	 * @param fret The fret number of the note
	 * @param pos The position value of the note. See {@link TabSymbol#position}
	 * @return The {@link SymbolHolder} containing the placed {@link TabNote}, 
	 * 	this method guarantees that the {@link TabSymbol} in the returned {@link SymbolHolder} is a {@link TabNote}, 
	 * 	or that it is null because the note could not be placed.
	 */
	public TabPosition placeQuantizedNote(TimeSignature sig, int fret, double pos){
		TabSettings settings = ZabAppSettings.get().tab();
		TabPosition p = TabFactory.modifiedFret(this, fret, pos);
		p = p.quantize(sig, settings.quantizeDivisor());
		if(!this.add(p)) return null;
		return p;
	}
	
	/**
	 * Get the number representing the fret position of given pitch on this {@link TabString}
	 * @param pitch The pitch
	 * @return The number, normally positive. Can be 0 for an open string, can be negative for an inaccessible fret number
	 */
	public int getTabNumber(Pitch pitch){
		return pitch.getNote() - this.getRootPitch().getNote();
	}
	
	/**
	 * Get the number representing the fret position of given pitch on this {@link TabString}
	 * @param tab The {@link TabPitch} containing the pitch
	 * @return The number, normally positive. Can be 0 for an open string, can be negative for an inaccessible fret number
	 */
	public int getTabNumber(TabPitch tab){
		return this.getTabNumber(tab.getPitch());
	}
	
	/**
	 * Create a {@link Pitch} which will be the note played on the given fret of this {@link TabString}
	 * @param fret The fret number to use, can use 0 for open string, can also use negative numbers
	 * @return The correct pitch
	 */
	public Pitch createPitch(int fret){
		return new Pitch(this.getRootPitch().getNote() + fret);
	}
	
	/**
	 * Quantize all {@link TabPosition} objects on this {@link TabString}
	 * @param sig The time signature to base the quantization off of
	 * @param divisor The amount to divide up the units of a whole note.<br>
	 * 	i.e. use 4 to quantize to quarter notes, use 6 to quantize to dotted quarter notes, etc
	 */
	public void quantize(TimeSignature sig, int divisor){
		for(int i = 0; i < this.size(); i++){
			TabPosition p = this.get(i);
			this.set(i, p.quantize(sig, divisor));
		}
	}
	
	/***/
	@Override
	public boolean load(Scanner reader){
		// Load the pitch of the string
		if(!Saveable.load(reader, this.getRootPitch())) return false;

		// Load in the number of symbols
		Integer size = Saveable.loadInt(reader);
		if(size == null) return false;
		
		// Advance to the next line
		if(!Saveable.nextLine(reader)) return false;
		
		// Ensure this TabString is empty
		this.clear();
		
		// Load in each value
		for(int i = 0; i < size; i++){
			// load the position and add it to this TabString
			TabPosition p = new TabPosition(new TabNote(0), 0);
			if(!Saveable.load(reader, p)) return false;
			this.add(p);
		}

		// Loading was successful
		return true;
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Save the pitch of the string
		if(!Saveable.save(writer, this.getRootPitch())) return false;
		
		// Save the number of symbols, this cannot fail so long that previous saves have succeeded and the writer is not running on a different thread
		Saveable.saveToString(writer, this.size(), true);
		
		// Save each symbol, first by saving its class name, then the symbol itself
		for(TabPosition p : this){
			if(!Saveable.save(writer, p)) return false;
		}
		
		// Return successful save
		return true;
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		TabString s = (TabString)obj;

		// Checking the strings have the same TabPositions
		boolean equalPositions = this.size() == s.size();
		for(int i = 0; i < this.size() && equalPositions; i++){
			equalPositions = this.get(i).equals(s.get(i));
		}
		
		// Check if the root pitches are equal and if each element on the string is equal
		return equalPositions && this.getRootPitch().equals(s.getRootPitch());
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[TabString, rootPitch: ");
		b.append(this.getRootPitch());
		b.append(", [Notes: ");
		if(this.size() > 0){
			TabPosition last = this.get(this.size() - 1);
			for(TabPosition p : this){
				b.append(p);
				if(last != p) b.append(", ");
			}
		}
		b.append("]]");
		return b.toString();
	}

}
