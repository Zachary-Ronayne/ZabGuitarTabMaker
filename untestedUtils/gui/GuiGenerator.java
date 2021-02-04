package gui;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import appMain.gui.comp.ZabFileChooser;
import appMain.gui.editor.frame.EditorFrame;
import appUtils.ZabConstants;

/**
 * A utility class containing methods for setting the state of a GUI. 
 * This class contains code which is not accounted for in test cases
 * @author zrona
 */
public final class GuiGenerator{
	
	/**
	 * Create a file chooser in the theme of the Zab Application
	 * @param frame The EditorFrame to be associated with the file chooser
	 * @return The file chooser, or null if an unexpected error happens
	 */
	public static ZabFileChooser createThemedFileChooser(EditorFrame frame){
		// Save the original look and feel
		LookAndFeel old = UIManager.getLookAndFeel();
		
		try{
			// Load the system look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			// Create the file chooser
			ZabFileChooser fileChooser = new ZabFileChooser(frame);
			
			// Restore the old look and feel
			UIManager.setLookAndFeel(old);
			
			return fileChooser;
			
		}catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return null;
		}
	}
	
	/** Cannot instantiate {@link GuiGenerator} */
	private GuiGenerator(){}
	
}
