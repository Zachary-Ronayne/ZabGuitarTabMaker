package appMain.gui.comp;

import javax.swing.JPanel;

import appMain.gui.ZabTheme;

/**
 * A {@link JPanel} used by the Zab Application
 * @author zrona
 */
public class ZabPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialize an initial ZabPanel
	 */
	public ZabPanel(){
		super();
		
		// Set the Theme appropriately
		ZabTheme.setToTheme(this);
	}
	
}
