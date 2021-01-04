package tab;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import music.Music;
import music.Pitch;
import music.TimeSignature;
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
public class TabString extends ArrayList<TabString.SymbolHolder> implements Copyable<TabString>, Saveable{
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
		for(SymbolHolder h : this){
			string.add(h.getSymbol().copy());
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
		SymbolHolder h = this.get(i);
		TabSymbol old = h.getSymbol();
		symbol.setPosition(old.getPosition());
		h.setSymbol(symbol);
		return old;
	}

	/**
	 * Adds the given {@link SymbolHolder} to the list based on their symbol's {@link TabSymbol#pos} fields in increasing order.<br>
	 * Essentially, ensures that all symbols are always sorted by the pos field
	 * @return true if the holder was added, false otherwise
	 */
	@Override
	public boolean add(SymbolHolder e){
		return ArrayUtils.insertSorted(this, e, false);
	}
	
	/**
	 * Adds the given {@link TabSymbol} based on their {@link TabSymbol#pos} fields in increasing order.<br>
	 * Essentially, ensures that all symbols are always sorted by the pos field
	 * @return true if the symbol was added, false otherwise
	 */
	public boolean add(TabSymbol e){
		return this.add(new SymbolHolder(e));
	}
	
	/**
	 * Create a list of all of the notes in this {@link TabString}. This allocates a new array, 
	 * and should not be used when high performance is needed
	 * @return The list
	 */
	public ArrayList<TabSymbol> getAll(){
		ArrayList<TabSymbol> all = new ArrayList<TabSymbol>();
		for(SymbolHolder h : this) all.add(h.getSymbol());
		return all;
	}
	
	/**
	 * Remove the {@link SymbolHolder} containing the given {@link TabSymbol}
	 * @param s The symbol
	 * @return True if the symbol was removed, false otherwise
	 */
	public boolean remove(TabSymbol s){
		for(SymbolHolder h : this){
			if(h.getSymbol().equals(s)){
				this.remove(h);
				return true;
			}
		}
		return false;
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
	public SymbolHolder placeQuantizedNote(TimeSignature sig, int fret, double pos){
		TabNote n = TabFactory.modifiedFret(this, fret, pos);
		n.quantize(sig, 8); // TODO make this a setting
		SymbolHolder h = new SymbolHolder(n);
		if(!this.add(h)) return null;
		return h;
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
	 * Quantize all {@link TabSymbol} objects on this {@link TabString}
	 * @param sig The time signature to base the quantization off of
	 * @param divisor The amount to divide up the units of a whole note.<br>
	 * 	i.e. use 4 to quantize to quarter notes, use 6 to quantize to dotted quarter notes, etc
	 */
	public void quantize(TimeSignature sig, int divisor){
		for(SymbolHolder h : this) h.getSymbol().getPosition().quantize(sig, divisor);
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
			// Load in the name of the symbol
			String[] loadStr = Saveable.loadStrings(reader, 1);
			if(loadStr == null) return false;
			String type = loadStr[0];
			
			// load the symbol itself and add it to this TabString
			TabSymbol s = TabUtils.stringToSymbol(type);
			if(!Saveable.load(reader, s)) return false;
			this.add(s);
		}

		// Loading was successful
		return true;
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Save the pitch of the string
		if(!Saveable.save(writer, this.getRootPitch())) return false;
		
		// Save the number of symbols
		if(!Saveable.saveToString(writer, this.size(), true)) return false;
		
		// Save each symbol, first by saving its class name, then the symbol itself
		for(SymbolHolder h : this){
			TabSymbol s = h.getSymbol();
			if(!Saveable.saveToString(writer, s.getClass().getSimpleName(), true)) return false;
			if(!Saveable.save(writer, s)) return false;
		}
		
		// Return successful save
		return true;
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		TabString s = (TabString)obj;
		return	super.equals(obj) &&
				this.getRootPitch().equals(s.getRootPitch());
	}
	
	/**
	 * A class used as a buffer to hold {@link TabSymbol} objects in the string, to make replacing notes easier
	 * @author zrona
	 */
	public static class SymbolHolder implements Comparable<SymbolHolder>{
		/** The {@link TabSymbol} held by this holder */ 
		private TabSymbol symbol;
		
		/**
		 * Create a {@link SymbolHolder} with the given {@link TabSymbol}
		 * @param symbol see {@link #symbol}
		 */
		public SymbolHolder(TabSymbol symbol){
			this.setSymbol(symbol);
		}
		
		/**
		 * @return See {@link #symbol}
		 */
		public TabSymbol getSymbol(){
			return this.symbol;
		}
		
		/**
		 * @param symbol See {@link #symbol}
		 */
		public void setSymbol(TabSymbol symbol){
			this.symbol = symbol;
		}
		
		/***/
		@Override
		public int compareTo(SymbolHolder h){
			return this.getSymbol().compareTo(h.getSymbol());
		}
		
		@Override
		public boolean equals(Object obj){
			if(!ObjectUtils.isType(obj, this.getClass())) return false;
			TabSymbol t = ((SymbolHolder)obj).getSymbol();
			return t.equals(this.getSymbol());
		}
	}
	
}
