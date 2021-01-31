package appMain.gui.comp.editor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import tab.ModifierFactory;
import tab.Tab;
import tab.TabPosition;
import tab.symbol.TabModifier;

/**
 * A class used by {@link TabPainter} to control key input
 * @author zrona
 */
public class EditorKeyboard extends TabPaintController implements KeyListener{

	/**
	 * Create a default state {@link EditorMouse}
	 * @param paint See {@link TabPaintController#paint}
	 */
	public EditorKeyboard(TabPainter painter){
		super(painter);
	}

	/** @return The {@link EditorMouse#lastX} of the {@link TabPainter} of this {@link EditorKeyboard} */
	public double mouseX(){
		return this.getPainter().getMouseInput().x();
	}
	/** @return The {@link EditorMouse#lastY} of the {@link TabPainter} of this {@link EditorKeyboard} */
	public double mouseY(){
		return this.getPainter().getMouseInput().y();
	}
	
	/** In addition to calling all methods for the appropriate key press, also repaints {@link TabPaintController#painter} */
	@Override
	public void keyPressed(KeyEvent e){
		TabPainter paint = this.getPainter();
		
		switch(e.getKeyCode()){
			case KeyEvent.VK_R: this.keyReset(e); break;
			case KeyEvent.VK_D: this.keySelectionRemoval(e); break;
			case KeyEvent.VK_DELETE: this.keySelectionDelete(e); break;
			case KeyEvent.VK_A: this.keySelectAll(e); break;
			case KeyEvent.VK_ESCAPE: this.keyCancelActions(e); break;
		}
		this.keyTypeTabPitch(e);
		this.keyTypeSetModifier(e);
		
		paint.repaint();
	}
	
	/** Does nothing */
	@Override
	public void keyTyped(KeyEvent e){}

	/** Does nothing */
	@Override
	public void keyReleased(KeyEvent e){}
	
	/**
	 * Called when the key associated with resetting the {@link Tab}, is pressed
	 * @param e The event of the key, it is assumed the event is for the appropriate action
	 */
	public void keyReset(KeyEvent e){
		TabPainter paint = this.getPainter();
		if(e.isControlDown()) paint.reset(); 
	}
	
	/**
	 * Called when the key associated with removing or unselecting notes, is pressed
	 * @param e The event of the key, it is assumed the event is for the appropriate action
	 */
	public void keySelectionRemoval(KeyEvent e){
		TabPainter paint = this.getPainter();
		// If D is pressed, if only control is pressed, clear the selection, if shift is also pressed, delete the selection
		if(e.isControlDown()){
			if(e.isShiftDown()) this.keySelectionDelete(e);
			else paint.clearSelection();
		}
	}
	
	/**
	 * Called when the key associated with deleting selected notes, is pressed
	 * @param e The event of the key, it is assumed the event is for the appropriate action
	 */
	public void keySelectionDelete(KeyEvent e){
		TabPainter paint = this.getPainter();
		paint.removeSelectedNotes();
	}
	
	/**
	 * Called when the key associated with selecting every note, is pressed
	 * @param e The event of the key, it is assumed the event is for the appropriate action
	 */
	public void keySelectAll(KeyEvent e){
		TabPainter paint = this.getPainter();
		if(e.isControlDown()) paint.selectAllNotes();
	}
	
	/**
	 * Called when the key associated with canceling actions such as the selection box and selection dragging, is pressed
	 * @param e The event of the key, it is assumed the event is for the appropriate action
	 */
	public void keyCancelActions(KeyEvent e){
		TabPainter paint = this.getPainter();
		paint.getDragger().reset();
		paint.getSelectionBox().clear();
	}
	
	/**
	 * Called when the key associated with typing a letter or other symbol into a tab pitch, is pressed
	 * @param e The event of the key, it is assumed the event is for the appropriate action
	 */
	public void keyTypeTabPitch(KeyEvent e){
		TabPainter paint = this.getPainter();
		paint.appendSelectedTabNum(e.getKeyChar());
	}
	
	/**
	 * Called when a key is pressed which should be used for the modifier. 
	 * This will process the key event and set the modifier appropriately for select {@link TabPosition} objects
	 * @param e The event of the key, it is assumed the event is for the appropriate action
	 * @return true if a modifier was set, false otherwise
	 */
	public boolean keyTypeSetModifier(KeyEvent e){
		TabPainter paint = this.getPainter();
		// Do nothing if no selection is made
		if(paint.getSelected().isEmpty()) return false;
		
		int key = e.getKeyCode();
		boolean shift = e.isShiftDown();
		
		// If space and shift were held down, remove the modifier and end the method
		if(key == KeyEvent.VK_SPACE && shift){
			paint.placeModifier(null, 2);
			return true;
		}
		
		// Otherwise, use a modifier if an applicable one was selected
		TabModifier mod = null;

		// If shift was held down, add to the modifier, otherwise replace it
		int mode = shift ? 1 : 0;
		
		// Determine the modifier
		switch(key){
			case KeyEvent.VK_P: mod = ModifierFactory.pullOff(); break;
			case KeyEvent.VK_H: mod = ModifierFactory.hammerOn(); break;
			case KeyEvent.VK_SLASH: mod = ModifierFactory.slideUp(); break;
			case KeyEvent.VK_BACK_SLASH: mod = ModifierFactory.slideDown(); break;
			// Special case of a harmonic, it will always replace
			case KeyEvent.VK_COMMA:
			case KeyEvent.VK_PERIOD:
				mod = ModifierFactory.harmonic();
				mode = 0;
				break;
		}
		
		// If no valid modifier was found, do nothing
		if(mod == null) return false;
		
		paint.placeModifier(mod, mode);
		
		return true;
	}
	
}
