package tab.symbol;

import java.io.PrintWriter;
import java.util.Scanner;

import music.Pitch;
import music.Rhythm;
import music.TimeSignature;
import util.ObjectUtils;
import util.Saveable;

/**
 * An instantiation of a {@link TabPitch} with rhythmic information
 * @author zrona
 */
public class TabNoteRhythm extends TabPitch{

	/** The rhythmic information of this {@link TabNoteRhythm}. Cannot be null. */
	private Rhythm rhythm;
	
	/**
	 * Create a new {@link TabNoteRhythm} using the given objects
	 * @param pitch Initial value for {@link TabPitch#pitch}
	 * @param rhythm Initial value for {@link #rhythm}
	 * @param modifier Initial value for {@link TabSymbol#modifier}
	 */
	public TabNoteRhythm(Pitch pitch, Rhythm rhythm, TabModifier modifier){
		super(pitch, modifier);
		if(rhythm == null) throw new IllegalArgumentException("Rhythm cannot be null");
		this.rhythm = rhythm;
	}
	
	/**
	 * Create a new {@link TabNoteRhythm} using the given objects and no modifier
	 * @param pitch Initial value for {@link TabPitch#pitch}
	 * @param rhythm Initial value for {@link #rhythm}
	 */
	public TabNoteRhythm(Pitch pitch, Rhythm rhythm){
		this(pitch, rhythm, new TabModifier());
	}

	/**
	 * Create a new {@link TabNoteRhythm} using the given values and no modifier
	 * @param pitch Initial pitch value for {@link TabPitch#pitch}
	 * @param rhythm Initial value for {@link #rhythm}
	 */
	public TabNoteRhythm(int pitch, Rhythm rhythm){
		this(new Pitch(pitch), rhythm);
	}
	
	/***/
	@Override
	public TabNoteRhythm copy(){
		Pitch p = ObjectUtils.copy(this.getPitch());
		Rhythm r = ObjectUtils.copy(this.getRhythm());
		TabModifier mod = ObjectUtils.copy(this.getModifier());
		return new TabNoteRhythm(p, r, mod);
	}
	
	/** @return see {@link #rhythm}  */
	public Rhythm getRhythm(){
		return rhythm;
	}

	/**
	 * Convert this {@link TabNoteRhythm} to a version with no rhythmic information as a {@link TabNote}
	 * @return The {@link TabNote}
	 */
	@Override
	public TabNote removeRhythm(){
		return new TabNote(this.getPitch().copy(), this.getModifier().copy());
	}

	/***/
	@Override
	public TabNoteRhythm convertToRhythm(Rhythm r){
		return this.copy();
	}
	
	/***/
	@Override
	public boolean usesRhythm(){
		return true;
	}
	
	/**
	 * Create a new {@link TabNoteRhythm} which is the same as this object, but with the new pitch
	 */
	@Override
	public TabNoteRhythm createPitchNote(Pitch p){
		return new TabNoteRhythm(p, this.getRhythm(), this.getModifier());
	}
	
	/**
	 * Create a new {@link TabNoteRhythm} which is identical to this one, but with its rhythm quantized to a grid
	 */
	public TabNoteRhythm quantized(TimeSignature sig, int divisor){
		TabNoteRhythm note = this.copy();
		note.rhythm = sig.guessRhythmWholeNotes(this.getRhythm().getLength());
		return note;
	}

	/**
	 * Utility method for getting all objects which must be saved for this object
	 * @return The array of all objects
	 */
	public Saveable[] getSaveObjects(){
		return new Saveable[]{this.getPitch(), this.getRhythm(), this.getModifier()};
	}
	
	/***/
	@Override
	public boolean load(Scanner reader){
		// Save the pitch, position, rhythm, and modifier
		return Saveable.loadMultiple(reader, this.getSaveObjects());
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Load the pitch, position, rhythm, and modifier
		return Saveable.saveMultiple(writer, this.getSaveObjects());
	}
	
	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, TabNoteRhythm.class)) return false;
		TabNoteRhythm n = (TabNoteRhythm)obj;
		return	super.equals(obj) &&
				this.getRhythm().equals(n.getRhythm());
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[TabNoteRhyhtm, ");
		b.append(super.toString());
		b.append(", ");
		b.append(this.getRhythm());
		return b.toString();
	}
	
}
