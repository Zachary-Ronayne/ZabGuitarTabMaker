package appMain;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;

/**
 * The main file used to run the primary GUI application
 * @author zrona
 */
public class Main{

	public static void main(String[] args){
		// Initialize settings before doing anything else
		ZabAppSettings.init();
		
		// Start up the main GUI
		ZabGui gui = new ZabGui();
		
		// Close the application if the GUI should not be shown, this should only occur during testing
		if(!ZabGui.SHOW_GUI_ON_INIT) System.exit(0);
	}
	
}
