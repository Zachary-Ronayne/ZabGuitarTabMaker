package tab.symbol;

import java.io.PrintWriter;
import java.util.Scanner;

import appUtils.ZabAppSettings;
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
		return this.copy();
	}

	/***/
	@Override
	public TabDeadNote removeRhythm(){
		return this.copy();
	}
	
	@Override
	public boolean usesRhythm(){
		return false;
	}
	
	/** Does nothing, {@link TabDeadNote} objects don't change going from string to string */
	@Override
	public void updateOnNewString(TabString oldStr, TabString newStr){}
	
	/**
	 * Get the text representing this dead note
	 */
	@Override
	public String getSymbol(TabString string){
		return ZabAppSettings.get().symbol().deadNote();
	}
	
	/***/
	@Override
	public boolean load(Scanner reader){
		// Load the modifier
		return Saveable.load(reader, this.getModifier());
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Save the modifier
		return Saveable.save(writer, this.getModifier());
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[TabDeadNote, ");
		b.append(this.getSymbol(null));
		b.append(", ");
		b.append(this.getModifier());
		b.append("]");
		return b.toString();
	}

}
