package tab;

import music.Rhythm;
import tab.symbol.TabDeadNote;
import tab.symbol.TabNote;
import tab.symbol.TabNoteRhythm;
import tab.symbol.TabSymbol;

/**
 * A class containing utilities for processing {@link Tab} objects and associated objects
 * @author zrona
 */
public class TabUtils{
	
	/**
	 * Given the class name of a {@link TabSymbol}, return an object of that type.
	 * @param type The string holding the type
	 * @return The {@link TabSymbol}. This method makes no guarantees about the contents of the returned object,
	 * 	only that it is of the desired type. If no valid type is found, returns null
	 */
	public static TabSymbol stringToSymbol(String type){
		switch(type){
			case "TabDeadNote": return new TabDeadNote();
			case "TabNote": return new TabNote(0);
			case "TabNoteRhythm": return new TabNoteRhythm(0, new Rhythm(1, 1));
			default: return null;
		}
	}

	/** Cannot instantiate {@link TabUtils} */
	private TabUtils(){}
	
}
