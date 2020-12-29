package appMain.gui.comp;

import javax.swing.JMenuItem;

import appMain.gui.ZabTheme;

/**
 * A particular item in the list of items in a menu used by the Zab Application drop down menu
 * @author zrona
 */
public class ZabMenuItem extends JMenuItem{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a {@link ZabMenuItem} in a default state
	 * @param name The text for the item
	 */
	public ZabMenuItem(String name){
		super(name);
		
		// Set the Theme appropriately
		ZabTheme.setToTheme(this);
	}
	
}
