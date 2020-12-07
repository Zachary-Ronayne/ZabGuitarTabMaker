package tab.symbol;

import music.Rhythm;

/**
 * An instantiation of a {@link TabPitch} with rhythmic information
 * @author zrona
 */
public class TabNoteRhythm extends TabPitch{

	/** The {@link Rhythm} of this {@link TabNoteRhythm} */
	private Rhythm rhythm;
	
	public TabNoteRhythm(TabPosition pos, TabModifier modifier){ // TODO add params
		super(pos, modifier);
	}
	
}
