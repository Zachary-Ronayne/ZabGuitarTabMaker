package appMain.gui.comp;

import javax.swing.JDialog;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;

public class ZabDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabGui} which uses this {@link ZabDialog} */
	private ZabGui gui;
	
	/** The {@link ZabFrame} used by this {@link ZabDialog} */
	private ZabFrame frame;
	
	/**
	 * Create a {@link ZabDialog} which is initially hidden
	 * @param gui
	 */
	public ZabDialog(ZabGui gui, ZabFrame frame){
		super(gui);
		this.setVisible(false);
		
		// Set the frame and gui
		this.frame = frame;
		this.add(this.frame);
		this.gui = gui;
		
		// Load appropriate theme
		ZabTheme.setToTheme(this);
	}
	
	/** @return See {@link #gui} */
	public ZabGui getGui(){
		return this.gui;
	}

	/** @return See {@link #frame} */
	public ZabFrame getFrame(){
		return this.frame;
	}
	
}
