package appMain.gui.dropMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import appMain.gui.ZabGui;
import appMain.gui.editor.paint.TabPainter;
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
	/** The {@link UndoListener} used by {@link #undoItem} to perform an undo action */
	private UndoListener undoListener;
	
	/** The {@link ZabMenuItem} used for to redo action */
	private ZabMenuItem redoItem;
	/** The {@link RedoListener} used by {@link #redoItem} to perform a redo action */
	private RedoListener redoListener;
	
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
		this.undoListener = new UndoListener();
		this.undoItem.addActionListener(this.undoListener);
		this.add(this.undoItem);
		
		// Edit redo
		this.redoItem = new ZabMenuItem(lang.redo());
		this.redoListener = new RedoListener();
		this.redoItem.addActionListener(this.redoListener);
		this.add(this.redoItem);
	}
	
	/** @return See {@link #undoItem} */
	public ZabMenuItem getUndoItem(){
		return undoItem;
	}
	/** @return See {@link #undoListener} */
	public UndoListener getUndoListener(){
		return this.undoListener;
	}

	/** @return See {@link #redoItem} */
	public ZabMenuItem getRedoItem(){
		return redoItem;
	}
	/** @return See {@link #redoListener} */
	public RedoListener getRedoListener(){
		return this.redoListener;
	}
	
	/** @return The {@link TabPainter} associated with the {@link ZabGui} of this {@link EditMenu} */
	public TabPainter getPainter(){
		return this.getGui().getEditorFrame().getTabScreen();
	}
	
	/**
	 * An {@link ActionListener} which calls the undo method of the {@link TabPainter} of the {@link ZabGui}
	 * @author zrona
	 */
	public class UndoListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			TabPainter paint = getPainter();
			paint.undo();
			paint.repaint();
		}
	}
	
	/**
	 * An {@link ActionListener} which calls the redo method of the {@link TabPainter} of the {@link ZabGui}
	 * @author zrona
	 */
	public class RedoListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			TabPainter paint = getPainter();
			paint.redo();
			paint.repaint();
		}
	}
	
}
