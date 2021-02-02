package appMain.gui.comp;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JDialog;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appMain.gui.frames.ExporterFrame;
import appMain.gui.frames.editor.EditorFrame;
import appUtils.ZabConstants;
import tab.Tab;
import tab.TabTextExporter;

/**
 * A class used for tracking a pop up GUI used to select settings for exporting a {@link Tab}
 * @author zrona
 */
public class ZabExporterDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabGui} which uses this {@link ZabExporterDialog} */
	private ZabGui gui;
	
	/** The {@link ExporterFrame} used by this {@link ZabExporterDialog} */
	private ExporterFrame frame;
	
	/**
	 * Create a {@link ZabExporterDialog} dialog box in the default state
	 * @param gui See {@link #gui}
	 */
	public ZabExporterDialog(ZabGui gui){
		super(gui);
		
		// Ensure this JDialog is not initially shown
		this.setVisible(false);
		
		// Set the gui
		this.gui = gui;
		
		// Load appropriate theme
		ZabTheme.setToTheme(this);
		
		// Set up and add the frame
		this.frame = new ExporterFrame(gui);
		this.add(this.frame);
		
		// Set up the preferred size
		this.setMinimumSize(new Dimension(ZabConstants.MIN_APP_EXPORT_WIDTH, ZabConstants.MIN_APP_EXPORT_HEIGHT));
		
		// Finalize the dialog box
		this.pack();
	}
	
	/** @return See {@link #gui} */
	public ZabGui getGui(){
		return this.gui;
	}
	
	/** @return See {@link #frame} */
	public ExporterFrame getFrame(){
		return this.frame;
	}
	
	/**
	 * Open up the exporter 
	 */
	public void open(){
		// Ensure this Dialog box remains on the top
		this.setAlwaysOnTop(true);
		
		// Open the JDialog
		if(ZabConstants.ENABLE_DIALOG) this.setVisible(true);
		
		// Center it based on the GUI
		this.setLocationRelativeTo(this.getGui());
	}
	
	/**
	 * Perform the export to a file as specified by the fields of this {@link ZabExporterDialog}
	 * @return true if the export was successful, false otherwise
	 */
	public boolean export(){
		EditorFrame editFrame = this.getGui().getEditorFrame();
		Tab tab = editFrame.getOpenedTab();
		File file = this.getFrame().getExportFile();
		boolean success = TabTextExporter.exportToFile(tab, file);
		editFrame.getEditorBar().getFileStatusLab().updateExportStatus(success); 
		return success;
	}
	
}
