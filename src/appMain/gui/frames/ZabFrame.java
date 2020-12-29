package appMain.gui.frames;

import appMain.gui.ZabTheme;

/**
 * A GuiFrame specifically used by the Zab Application
 * @author zrona
 */
public abstract class ZabFrame extends GuiFrame{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialize an initial ZabFrame
	 */
	public ZabFrame(){
		super();
		
		// Set the Theme appropriately
		ZabTheme.setToTheme(this);
	}
	
}
