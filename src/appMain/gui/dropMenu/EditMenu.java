package appMain.gui.dropMenu;

import appMain.gui.ZabGui;
import lang.AbstractLanguage;
import lang.Language;

/**
 * The {@link ZabMenu} in {@link ZabMenuBar} handling editing related items
 * @author zrona
 */
public class EditMenu extends ZabMenu{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabMenuItem} used for to undo action */
	private ZabMenuItem undoItem;
	/** The {@link ZabMenuItem} used for to redo action */
	private ZabMenuItem redoItem;
	

	/**
	 * Create a new default {@link EditMenu}
	 * @param gui See {@link ZabMenu#gui}
	 */
	public EditMenu(ZabGui gui){
		super("", gui);
		AbstractLanguage lang = Language.get();
		this.setText(lang.edit());
		
		// Edit undo
		this.undoItem = new ZabMenuItem(lang.undo());
		this.add(this.undoItem);
		
		// Edit redo
		this.redoItem = new ZabMenuItem(lang.redo());
		this.add(this.redoItem);
	}
	
	/** @return See {@link #undoItem} */
	public ZabMenuItem getUndoItem(){
		return undoItem;
	}

	/** @return See {@link #redoItem} */
	public ZabMenuItem getRedoItem(){
		return redoItem;
	}

}
