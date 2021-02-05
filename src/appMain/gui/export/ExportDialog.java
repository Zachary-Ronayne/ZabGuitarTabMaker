package appMain.gui.export;

import java.awt.Dimension;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabDialog;
import appMain.gui.editor.frame.EditorFrame;
import appUtils.ZabConstants;
import tab.Tab;

/**
 * A class used for tracking a pop up GUI used to select settings for exporting a {@link Tab}
 * @author zrona
 */
public class ExportDialog extends ZabDialog{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a {@link ExportDialog} dialog box in the default state
	 * @param gui See {@link #getGui()}
	 */
	public ExportDialog(ZabGui gui){
		super(gui, new ExportFrame(gui));
		
		// Ensure this JDialog is not initially shown
		this.setVisible(false);
		
		// Set up the preferred size
		this.setMinimumSize(new Dimension(ZabConstants.MIN_APP_EXPORT_WIDTH, ZabConstants.MIN_APP_EXPORT_HEIGHT));
		
		// Finalize the dialog box
		this.pack();
	}
	
	/**
	 * This method is in place for convenience as the frame of this {@link ExportDialog} will always be an {@link ExportFrame}
	 * @return See {@link #frame}
	 */
	public ExportFrame getFrame(){
		return (ExportFrame)super.getFrame();
	}
	
	/**
	 * Open up the exporter 
	 */
	public void open(){
		// Ensure this Dialog box remains on the top
		this.toFront();
		
		// Open the JDialog
		if(ZabConstants.ENABLE_DIALOG) this.setVisible(true);
		
		// Center it based on the GUI
		this.setLocationRelativeTo(this.getGui());
	}
	
	/**
	 * Perform the export to a file as specified by the fields of this {@link ExportDialog}
	 * @return true if the export was successful, false otherwise
	 */
	public boolean export(){
		EditorFrame editFrame = this.getGui().getEditorFrame();
		return editFrame.export(this.getFrame().getExportFile());
	}
	
}
