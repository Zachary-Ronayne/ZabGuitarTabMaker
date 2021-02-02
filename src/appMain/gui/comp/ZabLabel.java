package appMain.gui.comp;

import java.awt.Font;

import javax.swing.JLabel;

import appMain.gui.ZabTheme;
import util.GuiUtils;

/**
 * A {@link JLabel} which is in the theme of the Zab application
 * @author zrona
 */
public class ZabLabel extends JLabel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a {@link ZabLabel} with the given text
	 * @param text The text to display
	 */
	public ZabLabel(String text){
		super(text);
		ZabTheme.setToTheme(this);
		this.setFont(new Font("Arial", Font.PLAIN, 20));
	}
	
	/**
	 * Create a blank {@link ZabLabel}
	 */
	public ZabLabel(){
		this("");
	}
	
	/**
	 * Set the font size of this label, does nothing if size is negative
	 * @param size The new size
	 */
	public void setFontSize(int size){
		GuiUtils.setFontSize(this, size);
	}

}
