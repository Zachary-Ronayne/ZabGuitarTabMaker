package appMain.gui.comp.editor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import appMain.gui.comp.dropMenu.FileMenu;
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

		// Only do normal key presses if a modifier action was not used
		if(!this.keyTypeSetModifier(e)){
			switch(e.getKeyCode()){
				case KeyEvent.VK_R: this.keyReset(e); break;
				case KeyEvent.VK_D: this.keySelectionRemoval(e); break;
				case KeyEvent.VK_DELETE: this.keySelectionDelete(e); break;
				case KeyEvent.VK_A: this.keySelectAll(e); break;
				case KeyEvent.VK_ESCAPE: this.keyCancelActions(e); break;
				case KeyEvent.VK_S: this.keySave(e); break;
				case KeyEvent.VK_L: this.keyLoad(e); break;
				case KeyEvent.VK_E: this.keyExport(e); break;
				case KeyEvent.VK_N: this.keyNewFile(e); break;
			}
			this.keyTypeTabPitch(e);
		}
		
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
		if(e.isControlDown()) paint.resetCamera(); 
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
	 * Get the {@link FileMenu} associated with this {@link EditorKeyboard}
	 * @return The {@link FileMenu}
	 */
	public FileMenu findFileMenu(){
		return this.getPainter().getGui().getZabMenuBar().getFileMenu();
	}
	
	/**
	 * Called when the key associated with saving to a file, is pressed
	 * @param e The event of the key, it is assumed the event is for the appropriate action
	 * @return true if a save occurred, i.e. valid keys pressed to initiate save, and save was successful, false otherwise
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
	 * @param e The event of the key, it is assumed the event is for the appropriate action
	 * @return true if a load occurred, i.e. valid keys pressed to initiate load, and load was successful, false otherwise
	 */
	public boolean keyLoad(KeyEvent e){
		if(!e.isControlDown()) return false;
		FileMenu file = this.findFileMenu();
		return file.load();
	}
	
	/**
	 * Called when the key associated with exporting to a file, is pressed
	 * @param e The event of the key, it is assumed the event is for the appropriate action
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
	 * @param e The event of the key, it is assumed the event is for the appropriate action
	 */
	public void keyNewFile(KeyEvent e){// TODO test in gui
		FileMenu file = this.findFileMenu();
		file.newFile();
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
		// Do nothing if no selection is made, or if control is held down
		if(paint.getSelected().isEmpty() || e.isControlDown()) return false;
		
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
			case KeyEvent.VK_B: mod = ModifierFactory.bend(); break;
			case KeyEvent.VK_R: mod = ModifierFactory.pinchHarmonic(); break;
			case KeyEvent.VK_BACK_QUOTE: mod = ModifierFactory.vibrato(); break;
			case KeyEvent.VK_T: mod = ModifierFactory.tap(); break;
			
			case KeyEvent.VK_9:
			case KeyEvent.VK_0:
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
		
		paint.placeModifier(mod, mode);
		
		return true;
	}
	
}
