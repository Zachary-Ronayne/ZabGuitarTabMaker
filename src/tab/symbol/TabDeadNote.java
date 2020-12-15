package tab.symbol;

import music.NotePosition;
import music.Rhythm;
import tab.Tab;
import tab.TabString;
import util.ObjectUtils;

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

}
