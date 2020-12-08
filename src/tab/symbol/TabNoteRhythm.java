package tab.symbol;

import music.Pitch;
import music.Rhythm;

/**
 * An instantiation of a {@link TabPitch} with rhythmic information
 * @author zrona
 */
public class TabNoteRhythm extends TabPitch{

	/** The rhythmic information of this {@link TabNoteRhythm} */
	private Rhythm rhythm;
	
	/**
	 * Create a new {@link TabNoteRhythm} using the given objects
	 * @param pitch Initial value for {@link TabPitch#pitch}
	 * @param rhythm Initial value for {@link #rhythm}
	 * @param pos Initial value for {@link TabSymbol#pos}
	 * @param modifier Initial value for {@link TabSymbol#modifier}
	 */
	public TabNoteRhythm(Pitch pitch, Rhythm rhythm, TabPosition pos, TabModifier modifier){
		super(pitch, pos, modifier);
		this.setRhythm(rhythm);
	}
	
	/**
	 * Create a new {@link TabNoteRhythm} using the given objects and no modifier
	 * @param pitch Initial value for {@link TabPitch#pitch}
	 * @param rhythm Initial value for {@link #rhythm}
	 * @param pos Initial value for {@link TabSymbol#pos}
	 */
	public TabNoteRhythm(Pitch pitch, Rhythm rhythm, TabPosition pos){
		this(pitch, rhythm, pos, null);
	}

	/**
	 * Create a new {@link TabNoteRhythm} using the given values and no modifier
	 * @param pitch Initial pitch value for {@link TabPitch#pitch}
	 * @param rhythm Initial value for {@link #rhythm}
	 * @param pos Initial position value for {@link TabSymbol#pos}
	 */
	public TabNoteRhythm(int pitch, Rhythm rhythm, int pos){
		this(new Pitch(pitch), rhythm, new TabPosition(pos));
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
		this.rhythm = rhythm;
	}
}
