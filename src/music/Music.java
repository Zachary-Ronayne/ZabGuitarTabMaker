package music;

/**
 * A class used to hold constants for musical names of notes. <br>
 * Is this class a silly one and kind of unnecessary? Yes, but OOP.
 * @author zrona
 */
public final class Music{
	
	/** The symbol used to represent a sharp note */
	public static final String SHARP = "#";
	/** The symbol used to represent a flat note */
	public static final String FLAT = "b";
	
	/** The musical name for the note A */
	public static final String A = "A";
	/** The musical name for the note B */
	public static final String B = "B";
	/** The musical name for the note C */
	public static final String C = "C";
	/** The musical name for the note D */
	public static final String D = "D";
	/** The musical name for the note E */
	public static final String E = "E";
	/** The musical name for the note F */
	public static final String F = "F";
	/** The musical name for the note G */
	public static final String G = "G";
	
	/** The musical name for the note A sharp, has the same meaning as B flat */
	public static final String A_SHARP = A + SHARP;
	/** The musical name for the note A flat, has the same meaning as G sharp */
	public static final String A_FLAT = A + FLAT;
	/** The musical name for the note B sharp, has the same meaning as C */
	public static final String B_SHARP = B + SHARP;
	/** The musical name for the note B flat, has the same meaning as A sharp */
	public static final String B_FLAT = B + FLAT;
	/** The musical name for the note C sharp, has the same meaning as D flat */
	public static final String C_SHARP = C + SHARP;
	/** The musical name for the note C flat, has the same meaning as B */
	public static final String C_FLAT = C + FLAT;
	/** The musical name for the note D sharp, has the same meaning as E flat */
	public static final String D_SHARP = D + SHARP;
	/** The musical name for the note D flat, has the same meaning as C sharp */
	public static final String D_FLAT = D + FLAT;
	/** The musical name for the note E sharp, has the same meaning as F */
	public static final String E_SHARP = E + SHARP;
	/** The musical name for the note E flat, has the same meaning as D sharp */
	public static final String E_FLAT = E + FLAT;
	/** The musical name for the note F sharp, has the same meaning as G flat */
	public static final String F_SHARP = F + SHARP;
	/** The musical name for the note F flat, has the same meaning as E */
	public static final String F_FLAT = F + FLAT;
	/** The musical name for the note G sharp, has the same meaning as A flat */
	public static final String G_SHARP = G + SHARP;
	/** The musical name for the note G flat, has the same meaning as F sharp */
	public static final String G_FLAT = G + FLAT;

	/** The 12 equal temperament notes, beginning with C, of an octave, represented as flats */
	public static final String[] FLATS = new String[]{C, D_FLAT, D, E_FLAT, E, F, G_FLAT, G, A_FLAT, A, B_FLAT, B};
	/** The 12 equal temperament notes, beginning with C, of an octave, represented as sharps */
	public static final String[] SHARPS = new String[]{C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B};
	
	/**
	 * Convert the given integer into its corresponding note name.
	 * @param note The integer representing the note, defined the same as {@link Pitch#note}
	 * @param useFlats true to use only flats for representing notes which can be represented as a sharp or flat, false to use only sharps
	 * @return The note as a string with the note name
	 */
	public static String intToNoteName(int note, boolean useFlats){
		// Mod by 12 for the 12 notes of equal temperament tuning
		int noteValue = note % 12;
		
		// Must account for the case of negative notes, adding 12 to keep the number positive
		if(noteValue < 0) noteValue += 12;
		return useFlats ? FLATS[noteValue] : SHARPS[noteValue];
	}
	
	/**
	 * Convert the given integer into its corresponding note name using flats when needed.
	 * @param note The integer representing the note, defined the same as {@link Pitch#note}
	 * @return The note as a string with the note name
	 */
	public static String intToNoteNameFlat(int note){
		return intToNoteName(note, true);
	}

	/**
	 * Convert the given integer into its corresponding note name using sharps when needed.
	 * @param note The integer representing the note, defined the same as {@link Pitch#note}
	 * @return The note as a string with the note name
	 */
	public static String intToNoteNameSharp(int note){
		return intToNoteName(note, false);
	}
	
	/**
	 * Convert the given integer into its corresponding note.
	 * @param note The integer representing the note, defined the same as {@link Pitch#note}
	 * @param useFlats true to use only flats for representing notes which can be represented as a sharp or flat, false to use only sharps
	 * @return The note as a string with the note name followed by the octave number
	 */
	public static String intToNote(int note, boolean useFlats){
		int octave = toOctave(note);
		String name = intToNoteName(note, useFlats);
		return name + octave;
	}
	
	/**
	 * Convert the given integer into its corresponding note using flats for notes which need them.
	 * @param note The integer representing the note, defined the same as {@link Pitch#note}
	 * @return The note as a string with the note name followed by the octave number
	 */
	public static String intToNoteFlat(int note){
		return intToNote(note, true);
	}

	/**
	 * Convert the given integer into its corresponding note name using sharps for notes which need them.
	 * @param note The integer representing the note, defined in {@link Pitch#note}
	 * @return The note as a string with the note name followed by the octave number
	 */
	public static String intToNoteSharp(int note){
		return intToNote(note, false);
	}
	
	/**
	 * Convert the given integer to its corresponding octave
	 * @param note The note integer to convert, defined in {@link Pitch#note}
	 * @return The octave number
	 */
	public static int toOctave(int note){
		// Add 48 for the 4 octaves where middle C lies, then divide by 12 to get the octave number
		return (note + 48) / 12;
	}
	
	/**
	 * Instances of MusicConstants are not permitted
	 */
	private Music(){}
}
