package tab.symbol;

import java.io.PrintWriter;
import java.util.Scanner;

import music.Rhythm;
import tab.Tab;
import tab.TabString;
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
	public TabDeadNote(){
		super(new TabModifier());
	}
	
	/***/
	@Override
	public TabDeadNote copy(){
		return new TabDeadNote();
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
	
	/***/
	@Override
	public boolean load(Scanner reader){
		// Load the position and modifier
		return Saveable.load(reader, this.getModifier());
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Save the position and modifier
		return Saveable.save(writer, this.getModifier());
	}

}
