package tab;

import appUtils.ZabAppSettings;
import appUtils.ZabSettings;
import music.Music;
import music.NotePosition;
import music.Pitch;
import music.Rhythm;
import tab.symbol.TabModifier;
import tab.symbol.TabNote;
import tab.symbol.TabNoteRhythm;
import tab.symbol.TabSymbol;

/**
 * A utility class for generating notes for use in tabs, specifically when using symbols
 * @author zrona
 */
public final class TabFactory{
	
	/**
	 * Generate a {@link TabNote} with all of the given data
	 * @param note The string constant for the note as defined in {@link Music}
	 * @param octave The octave to use, i.e. use 4 for the octave of middle C 
	 * @param pos The position value, see {@link TabSymbol#position}
	 * @param mod The modifier to use, see {@link TabSymbol#modifier}
	 * @return The {@link TabNote}
	 */
	public static TabNote modifiedPitch(String note, int octave, double pos, TabModifier mod){
		return new TabNote(new Pitch(Music.createNote(note, octave)), new NotePosition(pos), mod);
	}

	/**
	 * Generate a {@link TabNoteRhythm} with all of the given data
	 * @param note The string constant for the note as defined in {@link Music}
	 * @param octave The octave to use, i.e. use 4 for the octave of middle C
	 * @param r The rhythm to use for the note
	 * @param pos The position value, see {@link TabSymbol#position}
	 * @param mod The modifier to use, see {@link TabSymbol#modifier}
	 * @return The {@link TabNote}
	 */
	public static TabNoteRhythm modifiedRhythm(String note, int octave, Rhythm r, double pos, TabModifier mod){
		return modifiedPitch(note, octave, pos, mod).convertToRhythm(r);
	}
	
	/**
	 * Generate a {@link TabNote} based on the string and fret number
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param pos The position value, see {@link TabSymbol#position}
	 * @param mod The modifier to use
	 * @return The note
	 */
	public static TabNote modifiedFret(TabString string, int fret, double pos, TabModifier mod){
		Pitch p = string.createPitch(fret);
		return new TabNote(p, new NotePosition(pos), mod);
	}
	
	/**
	 * Generate a {@link TabNote} based on the string and fret number, and with no modifier
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param pos The position value, see {@link TabSymbol#position}
	 * @return The note
	 */
	public static TabNote modifiedFret(TabString string, int fret, double pos){
		return modifiedFret(string, fret, pos, new TabModifier());
	}
	
	/**
	 * Generate a {@link TabNoteRhythm} based on the string and fret number
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param r The rhythm to use for the note
	 * @param pos The position value, see {@link TabSymbol#position}
	 * @param mod The modifier to use
	 * @return The note
	 */
	public static TabNoteRhythm modifiedFretRhythm(TabString string, int fret, Rhythm r, double pos, TabModifier mod){
		return modifiedFret(string, fret, pos, mod).convertToRhythm(r);
	}
	
	/**
	 * Generate a {@link TabNote} hammer on note based on the string and fret number
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param pos The position value, see {@link TabSymbol#position}
	 * @return The note
	 */
	public static TabNote hammerOn(TabString string, int fret, double pos){
		ZabSettings s = ZabAppSettings.get();
		return modifiedFret(string, fret, pos, new TabModifier(s.hammerOn(), ""));
	}
	
	/**
	 * Generate a {@link TabNote} pull off note based on the string and fret number
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param pos The position value, see {@link TabSymbol#position}
	 * @return The note
	 */
	public static TabNote pullOff(TabString string, int fret, double pos){
		ZabSettings s = ZabAppSettings.get();
		return modifiedFret(string, fret, pos, new TabModifier(s.pullOff(), ""));
	}
	
	/**
	 * Generate a {@link TabNote} natural harmonic note based on the string and fret number
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param pos The position value, see {@link TabSymbol#position}
	 * @return The note
	 */
	public static TabNote harmonic(TabString string, int fret, double pos){
		ZabSettings s = ZabAppSettings.get();
		return modifiedFret(string, fret, pos, new TabModifier(s.harmonicBefore(), s.harmonicAfter()));
	}
	
	/** Cannot instantiate {@link TabFactory} */
	private TabFactory(){}
	
}
