package appMain.gui.comp;

import javax.swing.JButton;

import appMain.gui.ZabTheme;
import util.GuiUtils;

/**
 * A {@link JButton} used by the Zab application
 * @author zrona
 */
public class ZabButton extends JButton{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new empty {@link ZabButton}
	 */
	public ZabButton(){
		super();
		
		// Set the theme
		ZabTheme.setToTheme(this);
	}
	
	/**
	 * Modify only the font size of this {@link ZabButton}.<br>
	 * Does nothing if size is negative
	 * @param size The new size
	 */
	public void setFontSize(int size){
		GuiUtils.setFontSize(this, size);
	}
	
}
