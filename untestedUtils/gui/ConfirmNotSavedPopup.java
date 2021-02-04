package gui;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import appUtils.ZabConstants;
import lang.AbstractLanguage;
import lang.Language;

/**
 * A class used to create a pop up dialog which asks a user if they want to continue their action, because 
 * if they do, their file will be lost
 * @author zrona
 */
public final class ConfirmNotSavedPopup{
	
	/** Variable that keeps track of what is returned by {@link #show()} when it is disabled */
	private static boolean disableState = true;
	
	/**
	 * Create a pop up window which tells a user that their work has not been saved, and that their requested action 
	 * would erase that work. The box asks them if they want to continue or not.
	 * @return true if the user selects to continue, false otherwise. 
	 * If {@link ZabConstants#ENABLE_DIALOG} is false, this method will not bring up a dialog box, and will always return true
	 */
	public static boolean show(){
		if(!ZabConstants.ENABLE_DIALOG) return disableState;
		
		AbstractLanguage lang = Language.get();
		
		JLabel lab = new JLabel(lang.workNotSavedDescription());
		int result = JOptionPane.showConfirmDialog(null, lab, lang.workNotSavedTitle(), JOptionPane.YES_NO_OPTION);
		
		return result == JOptionPane.YES_OPTION;
	}
	
	/** @return See {@link #disableState} */
	public static boolean getDisableState(){
		return disableState;
	}
	
	/** @return See {@link #disableState} */
	public static void setDisableState(boolean disableState){
		ConfirmNotSavedPopup.disableState = disableState;
	}
	
	/** Cannot instantiate {@link ConfirmNotSavedPopup} */
	private ConfirmNotSavedPopup(){}
	
}
