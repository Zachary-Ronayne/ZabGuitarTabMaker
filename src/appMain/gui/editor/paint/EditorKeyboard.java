package appMain.gui.editor.paint;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;

import appMain.gui.dropMenu.FileMenu;
import tab.ModifierFactory;
import tab.Tab;
import tab.TabPosition;
import tab.symbol.TabModifier;

/**
 * A class used by {@link TabPainter} to control key input
 * 
 * @author zrona
 */
public class EditorKeyboard extends TabPaintController implements KeyListener{
	
	/**
	 * Create a default state {@link EditorMouse}
	 * 
	 * @param paint See {@link TabPaintController#paint}
	 */
	public EditorKeyboard(TabPainter painter){
		super(painter);
	}
	
	/**
	 * @return The {@link EditorMouse#lastX} of the {@link TabPainter} of this
	 *         {@link EditorKeyboard}
	 */
	public double mouseX(){
		return this.getPainter().getMouseInput().x();
	}
	
	/**
	 * @return The {@link EditorMouse#lastY} of the {@link TabPainter} of this
	 *         {@link EditorKeyboard}
	 */
	public double mouseY(){
		return this.getPainter().getMouseInput().y();
	}
	
	/**
	 * In addition to calling all methods for the appropriate key press, also
	 * repaints {@link TabPaintController#painter}
	 */
	@Override
	public void keyPressed(KeyEvent e){
		TabPainter paint = this.getPainter();
		
		// Only do normal key presses if a modifier action was not used
		if(!this.keyTypeSetModifier(e)){
			switch(e.getKeyCode()){
				case KeyEvent.VK_R:
					this.keyReset(e);
					break;
				case KeyEvent.VK_D:
					this.keySelectionRemoval(e);
					break;
				case KeyEvent.VK_DELETE:
					this.keySelectionDelete(e);
					break;
				case KeyEvent.VK_A:
					this.keySelectAll(e);
					break;
				case KeyEvent.VK_ESCAPE:
					this.keyCancelActions(e);
					break;
				case KeyEvent.VK_Z:
					this.keyUndo(e);
					break;
				case KeyEvent.VK_S:
					this.keySave(e);
					break;
				case KeyEvent.VK_L:
					this.keyLoad(e);
					break;
				case KeyEvent.VK_E:
					this.keyExport(e);
					break;
				case KeyEvent.VK_N:
					this.keyNewFile(e);
					break;
				case KeyEvent.VK_C:
					this.keyCopy(e);
					break;
				case KeyEvent.VK_V:
					this.keyPaste(e);
					break;
				case KeyEvent.VK_X:
					this.keyCut(e);
					break;
				case KeyEvent.VK_OPEN_BRACKET:
				case KeyEvent.VK_CLOSE_BRACKET:
					this.keyAddNotePitch(e);
					break;
			}
			if(!e.isControlDown()) this.keyTypeTabPitch(e);
			else this.keyZoom(e);
		}
		paint.repaint();
	}
	
	/** Does nothing */
	@Override
	public void keyTyped(KeyEvent e){
	}
	
	/** Does nothing */
	@Override
	public void keyReleased(KeyEvent e){
	}
	
	/**
	 * Modify the octave of the current selection by the given amount, only if alt is held down
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate action
	 */
	public void keyAddNotePitch(KeyEvent e){
		int c = (e.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) ? -1 : 1;
		if(e.isShiftDown()) c *= 12;
		this.getPainter().addPitchToSelection(c, true);
	}
	
	/**
	 * Called when the a key associated with zooming in and out, is pressed.
	 * This method zooms in by simulating a scroll wheel event based on the
	 * mouseWheelListener used by {@link #getPainter()}
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 * @return true if a zoom happened, false otherwise
	 */
	public boolean keyZoom(KeyEvent e){
		int key = e.getKeyCode();
		boolean down = key == KeyEvent.VK_MINUS;
		boolean up = key == KeyEvent.VK_EQUALS;
		if(!down && !up) return false;
		
		int modifiers = MouseWheelEvent.CTRL_DOWN_MASK;
		if(e.isShiftDown()) modifiers |= MouseWheelEvent.SHIFT_DOWN_MASK;
		if(e.isAltDown()) modifiers |= MouseWheelEvent.ALT_DOWN_MASK;
		
		MouseWheelEvent me = new MouseWheelEvent(getPainter(), MouseWheelEvent.MOUSE_WHEEL, System.currentTimeMillis(), modifiers, (int)this.mouseX(), (int)this.mouseY(), 1, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, down ? -1 : 1);
		this.getPainter().getMouseInput().mouseWheelMoved(me);
		
		return true;
	}
	
	/**
	 * Called when the key associated with resetting the {@link Tab}, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 */
	public void keyReset(KeyEvent e){
		TabPainter paint = this.getPainter();
		if(e.isControlDown()) paint.resetCamera();
	}
	
	/**
	 * Called when the key associated with removing or unselecting notes, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 */
	public void keySelectionRemoval(KeyEvent e){
		TabPainter paint = this.getPainter();
		// If D is pressed, if only control is pressed, clear the selection, if shift is
		// also pressed, delete the selection
		if(e.isControlDown()){
			if(e.isShiftDown()) this.keySelectionDelete(e);
			else paint.clearSelection();
		}
	}
	
	/**
	 * Called when the key associated with deleting selected notes, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 */
	public void keySelectionDelete(KeyEvent e){
		TabPainter paint = this.getPainter();
		paint.removeSelectedNotes(true);
	}
	
	/**
	 * Called when the key associated with selecting every note, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 */
	public void keySelectAll(KeyEvent e){
		TabPainter paint = this.getPainter();
		if(e.isControlDown()) paint.selectAllNotes();
	}
	
	/**
	 * Called when the key associated with canceling actions such as the selection
	 * box and selection dragging, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 */
	public void keyCancelActions(KeyEvent e){
		TabPainter paint = this.getPainter();
		paint.getDragger().reset();
		paint.getSelectionBox().clear();
	}
	
	/**
	 * Called when the key associated with the undo or redo action, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 */
	public void keyUndo(KeyEvent e){
		TabPainter paint = this.getPainter();
		if(e.isControlDown()){
			if(e.isShiftDown()) paint.redo();
			else paint.undo();
		}
	}
	
	/**
	 * Get the {@link FileMenu} associated with this {@link EditorKeyboard}
	 * 
	 * @return The {@link FileMenu}
	 */
	public FileMenu findFileMenu(){
		return this.getPainter().getGui().getZabMenuBar().getFileMenu();
	}
	
	/**
	 * Called when the key associated with saving to a file, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 * @return true if a save occurred, i.e. valid keys pressed to initiate save,
	 *         and save was successful, false otherwise
	 */
	public boolean keySave(KeyEvent e){
		FileMenu file = this.findFileMenu();
		if(e.isControlDown()){
			if(e.isShiftDown()) return file.saveAs();
			else return file.save();
		}
		return false;
	}
	
	/**
	 * Called when the key associated with loading a file, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 * @return true if a load occurred, i.e. valid keys pressed to initiate load,
	 *         and load was successful, false otherwise
	 */
	public boolean keyLoad(KeyEvent e){
		if(!e.isControlDown()) return false;
		FileMenu file = this.findFileMenu();
		return file.load();
	}
	
	/**
	 * Called when the key associated with exporting to a file, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 * @return true if the export dialog was opened, false otherwise
	 */
	public boolean keyExport(KeyEvent e){
		if(!e.isControlDown() || !e.isShiftDown()) return false;
		FileMenu file = this.findFileMenu();
		file.openExportDialog();
		return true;
	}
	
	/**
	 * Called when the key associated with creating a new file, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 */
	public void keyNewFile(KeyEvent e){
		if(!e.isControlDown()) return;
		FileMenu file = this.findFileMenu();
		file.newFile();
	}
	
	/**
	 * Called when the key associated with copying a selection of notes, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 */
	public void keyCopy(KeyEvent e){
		if(!e.isControlDown()) return;
		TabPainter paint = this.getPainter();
		SelectionCopyPaster paster = paint.getCopyPaster();
		paster.runCopy();
	}
	
	/**
	 * Called when the key associated with pasting a selected copying a selection of
	 * notes, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 */
	public void keyPaste(KeyEvent e){
		if(!e.isControlDown()) return;
		TabPainter paint = this.getPainter();
		SelectionCopyPaster paster = paint.getCopyPaster();
		paster.paste(this.mouseX(), this.mouseY(), e.isShiftDown(), true);
	}
	
	/**
	 * Called when the key associated with copying and then delete a selected
	 * copying a selection of notes, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 */
	public void keyCut(KeyEvent e){
		if(!e.isControlDown()) return;
		TabPainter paint = this.getPainter();
		SelectionCopyPaster paster = paint.getCopyPaster();
		paster.runCopy();
		paint.removeSelectedNotes(true);
	}
	
	/**
	 * Called when the key associated with typing a letter or other symbol into a
	 * tab pitch, is pressed
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 */
	public void keyTypeTabPitch(KeyEvent e){
		TabPainter paint = this.getPainter();
		paint.appendSelectedTabNum(e.getKeyChar(), true);
		
		// Special case for pressing X to type a dead note
		if(e.getKeyCode() == KeyEvent.VK_X) paint.placeDeadNote(true);
	}
	
	/**
	 * Called when a key is pressed which should be used for the modifier.
	 * This will process the key event and set the modifier appropriately for select
	 * {@link TabPosition} objects
	 * 
	 * @param e The event of the key, it is assumed the event is for the appropriate
	 *        action
	 * @return true if a modifier was set, false otherwise
	 */
	public boolean keyTypeSetModifier(KeyEvent e){
		TabPainter paint = this.getPainter();
		// Do nothing if no selection is made, or if control is held down
		if(paint.getSelected().isEmpty() || e.isControlDown()) return false;
		
		int key = e.getKeyCode();
		boolean shift = e.isShiftDown();
		
		// If space and shift were held down, remove the modifier and end the method
		if(key == KeyEvent.VK_SPACE && shift){
			paint.placeModifier(null, 2, true);
			return true;
		}
		// Otherwise, use a modifier if an applicable one was selected
		TabModifier mod = null;
		
		// If shift was held down, add to the modifier, otherwise replace it
		int mode = shift ? 1 : 0;
		
		// Determine the modifier
		switch(key){
			case KeyEvent.VK_P:
				mod = ModifierFactory.pullOff();
				break;
			case KeyEvent.VK_H:
				mod = ModifierFactory.hammerOn();
				break;
			case KeyEvent.VK_SLASH:
				mod = ModifierFactory.slideUp();
				break;
			case KeyEvent.VK_BACK_SLASH:
				mod = ModifierFactory.slideDown();
				break;
			case KeyEvent.VK_B:
				mod = ModifierFactory.bend();
				break;
			case KeyEvent.VK_R:
				mod = ModifierFactory.pinchHarmonic();
				break;
			case KeyEvent.VK_BACK_QUOTE:
				mod = ModifierFactory.vibrato();
				break;
			case KeyEvent.VK_T:
				mod = ModifierFactory.tap();
				break;
			
			case KeyEvent.VK_G:
				mod = ModifierFactory.ghostNote();
				mode = 0;
				break;
			
			case KeyEvent.VK_COMMA:
			case KeyEvent.VK_PERIOD:
				mod = ModifierFactory.harmonic();
				mode = 0;
				break;
		}
		// If no valid modifier was found, do nothing
		if(mod == null) return false;
		
		paint.placeModifier(mod, mode, true);
		
		return true;
	}
	
}
