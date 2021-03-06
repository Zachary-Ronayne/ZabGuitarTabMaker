package tab;

import music.Music;
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
	 * Generate a {@link TabPosition} containing a {@link TabNote} with all of the given data
	 * @param note The string constant for the note as defined in {@link Music}
	 * @param octave The octave to use, i.e. use 4 for the octave of middle C 
	 * @param pos The position value, see {@link TabPosition#position}
	 * @param mod The modifier to use, see {@link TabSymbol#modifier}
	 * @return The {@link TabPosition}
	 */
	public static TabPosition modifiedPitch(String note, int octave, double pos, TabModifier mod){
		return new TabPosition(new TabNote(new Pitch(Music.createNote(note, octave)), mod), pos);
	}

	/**
	 * Generate a {@link TabNoteRhythm} containing a {@link TabNote} with all of the given data
	 * @param note The string constant for the note as defined in {@link Music}
	 * @param octave The octave to use, i.e. use 4 for the octave of middle C
	 * @param r The rhythm to use for the note
	 * @param pos The position value, see {@link TabPosition#position}
	 * @param mod The modifier to use, see {@link TabSymbol#modifier}
	 * @return The {@link TabPosition}
	 */
	public static TabPosition modifiedRhythm(String note, int octave, Rhythm r, double pos, TabModifier mod){
		TabPosition p = modifiedPitch(note, octave, pos, mod);
		return p.copySymbol(p.getSymbol().convertToRhythm(r));
	}
	
	/**
	 * Generate a {@link TabPosition} containing a {@link TabNote} based on the string and fret number
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param pos The position value, see {@link TabPosition#position}
	 * @param mod The modifier to use
	 * @return The {@link TabPosition}
	 */
	public static TabPosition modifiedFret(TabString string, int fret, double pos, TabModifier mod){
		Pitch p = string.createPitch(fret);
		return new TabPosition(new TabNote(p, mod), pos);
	}
	
	/**
	 * Generate a {@link TabPosition} containing a {@link TabNote}  based on the string and fret number, and with no modifier
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param pos The position value, see {@link TabPosition#position}
	 * @return The {@link TabPosition}
	 */
	public static TabPosition modifiedFret(TabString string, int fret, double pos){
		return modifiedFret(string, fret, pos, new TabModifier());
	}
	
	/**
	 * Generate a {@link TabPosition} containing a {@link TabNoteRhythm} based on the string and fret number
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param r The rhythm to use for the note
	 * @param pos The position value, see {@link TabPosition#position}
	 * @param mod The modifier to use
	 * @return The {@link TabPosition}
	 */
	public static TabPosition modifiedFretRhythm(TabString string, int fret, Rhythm r, double pos, TabModifier mod){
		TabPosition p = modifiedFret(string, fret, pos, mod);
		return p.copySymbol(p.getSymbol().convertToRhythm(r));
	}
	
	/**
	 * Generate a {@link TabPosition} containing a {@link TabNote} hammer on note based on the string and fret number
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param pos The position value, see {@link TabPosition#position}
	 * @return The {@link TabPosition}
	 */
	public static TabPosition hammerOn(TabString string, int fret, double pos){
		return modifiedFret(string, fret, pos, ModifierFactory.hammerOn());
	}
	
	/**
	 * Generate a {@link TabPosition} containing a {@link TabNote} pull off note based on the string and fret number
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param pos The position value, see {@link TabPosition#position}
	 * @return The {@link TabPosition}
	 */
	public static TabPosition pullOff(TabString string, int fret, double pos){
		return modifiedFret(string, fret, pos, ModifierFactory.pullOff());
	}
	
	/**
	 * Generate a {@link TabPosition} containing a {@link TabNote} natural harmonic note based on the string and fret number
	 * @param string The string to use to generate the note
	 * @param fret The fret number on the string, can be zero for open string, or negative for a normally invalid fret position
	 * @param pos The position value, see {@link TabPosition#position}
	 * @return The note
	 */
	public static TabPosition harmonic(TabString string, int fret, double pos){
		return modifiedFret(string, fret, pos, ModifierFactory.harmonic());
	}
	
	/** Cannot instantiate {@link TabFactory} */
	private TabFactory(){}
	
}
