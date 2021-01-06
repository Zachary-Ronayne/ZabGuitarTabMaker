package appMain.gui.frames;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;

/**
 * A GuiFrame specifically used by the Zab Application
 * @author zrona
 */
public abstract class ZabFrame extends GuiFrame{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabGui} which uses this {@link ZabFrame} */
	private ZabGui gui;
	
	/**
	 * Initialize an initial ZabFrame
	 */
	public ZabFrame(ZabGui gui){
		super();
		
		this.gui = gui;
		
		// Set the Theme appropriately
		ZabTheme.setToTheme(this);
	}
	
	/** @return See {@link #gui} */
	public ZabGui getGui(){
		return this.gui;
	}
	
}
