package appMain;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;
import appUtils.ZabConstants;

/**
 * The main file used to start up the primary Zab GUI application
 * @author zrona
 */
public final class Main{

	public static void main(String[] args){
		// Initialize settings before doing anything else
		ZabAppSettings.init();

		// Start up the main GUI
		ZabGui gui = new ZabGui();
		if(ZabConstants.BUILD_NORMAL) gui.setVisible(true);
	}
	
	/** Cannot instantiate {@link Main} */
	private Main(){};
	
}
