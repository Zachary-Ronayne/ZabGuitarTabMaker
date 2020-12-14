package tab.symbol;

import music.NotePosition;
import music.Pitch;
import music.Rhythm;
import util.ObjectUtils;

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
	 * @param pos Initial value for {@link TabSymbol#pos}
	 * @param modifier Initial value for {@link TabSymbol#modifier}
	 */
	public TabNoteRhythm(Pitch pitch, Rhythm rhythm, NotePosition pos, TabModifier modifier){
		super(pitch, pos, modifier);
		if(rhythm == null) throw new IllegalArgumentException("Rhythm cannot be null");
		this.setRhythm(rhythm);
	}
	
	/**
	 * Create a new {@link TabNoteRhythm} using the given objects and no modifier
	 * @param pitch Initial value for {@link TabPitch#pitch}
	 * @param rhythm Initial value for {@link #rhythm}
	 * @param pos Initial value for {@link TabSymbol#pos}
	 */
	public TabNoteRhythm(Pitch pitch, Rhythm rhythm, NotePosition pos){
		this(pitch, rhythm, pos, new TabModifier());
	}

	/**
	 * Create a new {@link TabNoteRhythm} using the given values and no modifier
	 * @param pitch Initial pitch value for {@link TabPitch#pitch}
	 * @param rhythm Initial value for {@link #rhythm}
	 * @param pos Initial position value for {@link TabSymbol#pos}
	 */
	public TabNoteRhythm(int pitch, Rhythm rhythm, int pos){
		this(new Pitch(pitch), rhythm, new NotePosition(pos));
	}
	
	/***/
	@Override
	public TabNoteRhythm copy(){
		Pitch p = ObjectUtils.copy(this.getPitch());
		Rhythm r = ObjectUtils.copy(this.getRhythm());
		NotePosition pos = ObjectUtils.copy(this.getPos());
		TabModifier mod = ObjectUtils.copy(this.getModifier());
		return new TabNoteRhythm(p, r, pos, mod);
	}
	
	/**
	 * Get the rhythm of this {@link TabNoteRhythm}
	 * @return see {@link #rhythm}
	 */
	public Rhythm getRhythm(){
		return rhythm;
	}

	/**
	 * Set the rhythm of this {@link TabNoteRhythm}
	 * @param rhythm see {@link #rhythm}
	 */
	public void setRhythm(Rhythm rhythm){
		if(rhythm == null) return;
		this.rhythm = rhythm;
	}
	
	/**
	 * Convert this {@link TabNoteRhythm} to a version with no rhythmic information as a {@link TabNote}
	 * @return The {@link TabNote}
	 */
	public TabNote convertToNoRhythm(){
		return new TabNote(this.getPitch().copy(), this.getPos().copy(), this.getModifier().copy());
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		TabNoteRhythm n = (TabNoteRhythm)obj;
		return	super.equals(obj) &&
				this.getRhythm().equals(n.getRhythm());
	}
}
