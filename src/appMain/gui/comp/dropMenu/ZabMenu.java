package appMain.gui.comp.dropMenu;

import javax.swing.JMenu;

import appMain.gui.ZabTheme;

/**
 * A particular menu used by the Zab Application for the drop down menu
 * @author zrona
 */
public class ZabMenu extends JMenu{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new ZabMenu and bring it to a default state
	 * @param name The name of the menu, i.e. the text to display
	 */
	public ZabMenu(String name){
		super(name);
		
		// Set the Theme appropriately
		ZabTheme.setToTheme(this);
	}
	
}
