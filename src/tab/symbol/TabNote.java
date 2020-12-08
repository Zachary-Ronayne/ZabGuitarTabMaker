package tab.symbol;

import music.Pitch;

/**
 * An instantiation of a {@link TabPitch} with no rhythmic information
 * @author zrona
 */
public class TabNote extends TabPitch{
	
	/**
	 * Create a new {@link TabNote} using the given values
	 * @param pitch Initial value for {@link TabPitch#pitch}
	 * @param pos Initial value for {@link TabSymbol#pos}
	 * @param modifier Initial value for {@link TabSymbol#modifier}
	 */
	public TabNote(Pitch pitch, TabPosition pos, TabModifier modifier){
		super(pitch, pos, modifier);
	}

	/**
	 * Create a new {@link TabNote} using the given values and no modifier
	 * @param pitch Initial value for {@link TabPitch#pitch}
	 * @param pos Initial value for {@link TabSymbol#pos}
	 */
	public TabNote(Pitch pitch, TabPosition pos){
		super(pitch, pos, null);
	}
	
	/**
	 * Create a new {@link TabNote} using specified pitch and position as values.
	 * @param pitch The numerical value for the pitch of {@link TabPitch#pitch}
	 * @param pos The numerical value for the position of {@link TabPitch#pos}
	 */
	public TabNote(int pitch, double pos){
		this(new Pitch(pitch), new TabPosition(pos));
	}

}
