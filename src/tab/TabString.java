package tab;

import java.util.ArrayList;

import music.Music;
import music.Pitch;
import music.TimeSignature;
import tab.symbol.TabPitch;
import tab.symbol.TabSymbol;
import util.ArrayUtils;
import util.Copyable;
import util.ObjectUtils;

/**
 * A class representing one string of a tablature
 * @author zrona
 */
public class TabString extends ArrayList<TabSymbol> implements Copyable<TabString>{
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
		for(TabSymbol s: this){
			string.add(s.copy());
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
	 * Adds the given {@link TabSymbol} based on their {@link TabSymbol#pos} fields in increasing order.<br>
	 * Essentially, ensures that all symbols are always sorted by the pos field
	 */
	@Override
	public boolean add(TabSymbol e){
		return ArrayUtils.insertSorted(this, e);
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
		for(TabSymbol s : this) s.getPosition().quantize(sig, divisor);
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		TabString s = (TabString)obj;
		return	super.equals(obj) &&
				this.getRootPitch().equals(s.getRootPitch());
	}
	
}
