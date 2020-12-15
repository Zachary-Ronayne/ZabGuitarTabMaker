package tab.symbol;

import music.NotePosition;
import music.Pitch;
import music.Rhythm;
import util.ObjectUtils;

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
	public TabNote(Pitch pitch, NotePosition pos, TabModifier modifier){
		super(pitch, pos, modifier);
	}

	/**
	 * Create a new {@link TabNote} using the given values and no modifier
	 * @param pitch Initial value for {@link TabPitch#pitch}
	 * @param pos Initial value for {@link TabSymbol#pos}
	 */
	public TabNote(Pitch pitch, NotePosition pos){
		super(pitch, pos, new TabModifier());
	}
	
	/**
	 * Create a new {@link TabNote} using specified pitch and position as values.
	 * @param pitch The numerical value for the pitch of {@link TabPitch#pitch}
	 * @param pos The numerical value for the position of {@link TabPitch#pos}
	 */
	public TabNote(int pitch, double pos){
		this(new Pitch(pitch), new NotePosition(pos));
	}
	
	/**
	 * Create a new version of this {@link TabNote} as a {@link TabNoteRhythm} which uses the given {@link Rhythm}
	 * @param r The rhythm, uses the object itself, not a copy
	 * @return The new {@link TabNoteRhythm}
	 */
	@Override
	public TabNoteRhythm convertToRhythm(Rhythm r){
		return new TabNoteRhythm(
				this.getPitch().copy(), r,
				this.getPos().copy(),
				this.getModifier().copy()
		);
	}
	
	/**
	 * Create a new version of this {@link TabNote} as a {@link TabNoteRhythm} which uses the given {@link Rhythm}
	 * @param duraiton See {@link Rhythm#duration}
	 * @param unit See {@link Rhythm#unit}
	 * @return The new {@link TabNoteRhythm}
	 */
	public TabNoteRhythm convertToRhythm(int duraiton, int unit){
		return convertToRhythm(new Rhythm(duraiton, unit));
	}
	
	/***/
	@Override
	public TabNote removeRhythm(){
		return this;
	}
	
	/***/
	@Override
	public boolean usesRhythm(){
		return false;
	}
	
	/***/
	@Override
	public TabNote copy(){
		Pitch p = ObjectUtils.copy(this.getPitch());
		NotePosition pos = ObjectUtils.copy(this.getPos());
		TabModifier mod = ObjectUtils.copy(this.getModifier());
		return new TabNote(p, pos, mod);
	}

}
