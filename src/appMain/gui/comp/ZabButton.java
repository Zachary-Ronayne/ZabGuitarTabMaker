package appMain.gui.comp;

import java.awt.Font;

import javax.swing.JButton;

import appMain.gui.ZabTheme;

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
		if(size < 0) return;
		Font f = this.getFont();
		this.setFont(new Font(f.getFontName(), f.getStyle(), size));
	}
	
}
