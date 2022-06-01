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
	
	/** The {@link ZabMenuItem} used for the subtract fret function */
	private ZabMenuItem subtractFretItem;
	/** The {@link RedoListener} used by {@link #redoItem} to perform the subtract fret action */
	private FretModifyListener subtractFretListener;
	
	/** The {@link ZabMenuItem} used for the add fret function */
	private ZabMenuItem addFretItem;
	/** The {@link RedoListener} used by {@link #redoItem} to perform the subtract fret action */
	private FretModifyListener addFretListener;
	
	/** The {@link ZabMenuItem} used for the subtract octave function */
	private ZabMenuItem subtractOctaveItem;
	/** The {@link RedoListener} used by {@link #redoItem} to perform the subtract octave action */
	private FretModifyListener subtractOctaveListener;
	
	/** The {@link ZabMenuItem} used for the add octave function */
	private ZabMenuItem addOctaveItem;
	/** The {@link RedoListener} used by {@link #redoItem} to perform the add octave action */
	private FretModifyListener addOctaveListener;
	
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

		// Fret modify
		this.subtractFretItem = new ZabMenuItem(lang.subtractFretShort());
		this.addFretItem = new ZabMenuItem(lang.addFretShort());
		this.subtractOctaveItem = new ZabMenuItem(lang.subtractOctaveShort());
		this.addOctaveItem = new ZabMenuItem(lang.addOctaveShort());

		this.subtractFretListener = new FretModifyListener(-1, false);
		this.addFretListener = new FretModifyListener(1, false);
		this.subtractOctaveListener = new FretModifyListener(-1, true);
		this.addOctaveListener = new FretModifyListener(1, true);

		this.subtractFretItem.addActionListener(this.subtractFretListener);
		this.addFretItem.addActionListener(this.addFretListener);
		this.subtractOctaveItem.addActionListener(this.subtractOctaveListener);
		this.addOctaveItem.addActionListener(this.addOctaveListener);

		this.add(subtractFretItem);
		this.add(addFretItem);
		this.add(subtractOctaveItem);
		this.add(addOctaveItem);
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
	
	/**
	 * An {@link ActionListener} which adds or subtracts a certain number of frets from the selection of the {@link TabPainter} of the {@link ZabGui}
	 * 
	 * @author zrona
	 */
	public class FretModifyListener implements ActionListener{
		/** The amount to add or subtract, negative numbers to subtract */
		private int amount;
		/** true if {@link #amount} refers to a number of octaves, false for a number of frets */
		private boolean octave;

		public FretModifyListener(int amount, boolean octave){
			this.amount = amount;
			this.octave = octave;
		}

		@Override
		public void actionPerformed(ActionEvent e){
			TabPainter paint = getPainter();
			if(this.octave) paint.modifyOctave(this.amount, true);
			else paint.addPitchToSelection(this.amount, true);
		}
	}
	
}
