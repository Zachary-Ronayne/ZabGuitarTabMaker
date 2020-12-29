package appMain.gui.frames;

import javax.swing.JPanel;

/**
 * A JPanel holding all of the components used for a screen of a GUI, i.e. a main menu screen, main work screen, etc.
 * @author zrona
 */
public abstract class GuiFrame extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialize a GuiFrame to a default state
	 */
	public GuiFrame(){
		super();
	}
	
	/**
	 * Call this method when the parent of this {@link GuiFrame} is resized.<br>
	 * Parameters can be modified before this is called
	 * @param w The new width of the parent
	 * @param h The new height of the parent
	 */
	public abstract void parentResized(int w,  int h);
	
}
