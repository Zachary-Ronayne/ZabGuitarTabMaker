package tab.symbol;

import java.io.PrintWriter;
import java.util.Scanner;

import music.NotePosition;
import music.Rhythm;
import tab.Tab;
import tab.TabString;
import util.ObjectUtils;
import util.Saveable;

/**
 * A symbol for a {@link Tab} representing a dead note or muted note
 * @author zrona
 */
public class TabDeadNote extends TabSymbol{
	
	/**
	 * Create a dead note for a tab
	 * @param pos The {@link TabSymbol#pos} of this {@link TabDeadNote}
	 */
	public TabDeadNote(NotePosition pos){
		super(pos, new TabModifier());
	}
	
	/***/
	@Override
	public TabDeadNote copy(){
		return new TabDeadNote(ObjectUtils.copy(this.getPosition()));
	}
	
	/***/
	@Override
	public TabDeadNote convertToRhythm(Rhythm r){
		return this;
	}

	/***/
	@Override
	public TabDeadNote removeRhythm(){
		return this;
	}
	
	@Override
	public boolean usesRhythm(){
		return false;
	}

	/**
	 * Get the text representing this dead note
	 * @return Always returns an X
	 */
	@Override
	public String getSymbol(TabString string){
		return "X";
	}
	
	/**
	 * Utility method for getting all objects which must be saved for this object
	 * @return The array of all objects
	 */
	public Saveable[] getSaveObjects(){
		return new Saveable[]{this.getPosition(), this.getModifier()};
	}
	
	/***/
	@Override
	public boolean load(Scanner reader){
		// Load the position and modifier
		return Saveable.loadMultiple(reader, this.getSaveObjects());
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Save the position and modifier
		return Saveable.saveMultiple(writer, this.getSaveObjects());
	}

}
