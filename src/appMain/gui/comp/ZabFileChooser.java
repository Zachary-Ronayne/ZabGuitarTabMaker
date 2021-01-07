package appMain.gui.comp;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appMain.gui.frames.EditorFrame;
import appMain.gui.frames.ZabFrame;
import appUtils.ZabAppSettings;
import appUtils.ZabConstants;
import tab.Tab;
import util.FileUtils;

/**
 * A {@link JFileChooser} designed for the Zab application for selecting files
 * @author zrona
 */
public class ZabFileChooser extends JFileChooser{
	private static final long serialVersionUID = 1L;
	
	/** The path to the default directory for saving and loading files */
	public static final String DEFAULT_SAVCE_LOC = "./saves";
	
	/** The {@link ZabFrame} which uses this {@link ZabFileChooser} */
	private ZabFrame frame;
	
	/** A {@link FileFilter} that accepts .zab files */
	private FileFilter zabFileFilter;
	
	/**
	 * Create a new {@link ZabFileChooser} using the given {@link ZabGui}i
	 * @param gui See {@link #gui}
	 */
	public ZabFileChooser(ZabFrame frame){
		// Load the correct theme
		ZabTheme.setToTheme(this);
		
		// Set the base gui
		this.frame = frame;
		
		// Set up the file filter
		this.zabFileFilter = new FileNameExtensionFilter("Zab stringed instrument tablature", FileUtils.ZAB_EXTENSION);
		
		// Set this file chooser to only allow one file, not multiple files, and only files, not directories
		this.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.setMultiSelectionEnabled(false);
	}
	
	/**
	 * @return See {@link #gui}
	 */
	public ZabGui getGui(){
		return this.frame.getGui();
	}
	
	/** @return See {@link #zabFileFilter} */
	public FileFilter getZabFileFilter(){
		return this.zabFileFilter;
	}
	
	/**
	 * Prepare for processing a file by ensuring the path to the specified file exists  
	 * and setting the directory of this {@link ZabFileChooser} to the given path
	 * @param path The path to set
	 */
	public void filePrep(String path){
		// Ensure the directory to the save location exists
		File file = new File(path);
		if(!file.exists()) file.mkdirs();
		this.setCurrentDirectory(file);
	}
	
	/**
	 * Request that the user selects a file for exporting, and return that file
	 * @param extension The file extension, no dot
	 * @return The selected file, can be null if no file was selected. This file is also the same which can be obtained by {@link #getSelectedFile()}
	 */
	public File exportSelect(String extension){
		// Prep for selection
		this.filePrep(DEFAULT_SAVCE_LOC);
		
		// Set filter to all files
		this.setFileFilter(getAcceptAllFileFilter());

		// Request the file selection
		if(ZabConstants.ENABLE_DIALOG) this.showDialog(null, "Select location");
		
		// Add the file extension and return the selected file
		this.setSelectedFile(FileUtils.extendTo(getSelectedFile(), extension));
		return this.getSelectedFile();
	}
	
	/**
	 * Load the {@link Tab} of {@link #gui} from the file selected by the user
	 * @return true if the load was successful, false otherwise
	 */
	public boolean loadTab(){
		this.filePrep(DEFAULT_SAVCE_LOC);
		// Set the filter to only use .zab files
		this.setFileFilter(this.getZabFileFilter());
		
		// Open the save window and wait for the user to pick a file name
		if(ZabConstants.ENABLE_DIALOG) this.showDialog(null, "Load tab");

		// Ensure a file was selected
		File file = this.getSelectedFile();
		if(file == null) return false;
		
		// Ensure a tab exists
		EditorFrame frame = this.getGui().getEditorFrame();
		Tab tab = frame.getOpenedTab();
		if(tab == null){
			frame.setOpenedTab(new Tab());
			tab = frame.getOpenedTab();
		}
		
		// Load the tab from the file
		boolean success = ZabAppSettings.load(file, tab, true);
		
		// Update the GUI to reflect the loaded tab
		getGui().repaint();
		
		return success;
	}
	
	/**
	 * Save the {@link Tab} of {@link #gui} to the file selected by the user
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveTab(){
		this.filePrep(DEFAULT_SAVCE_LOC);
		// Set the filter to only use .zab files
		this.setFileFilter(this.getZabFileFilter());
		
		// Open the save window and wait for the user to pick a file name
		if(ZabConstants.ENABLE_DIALOG) this.showDialog(null, "Save tab");

		// Ensure a file was selected
		File file = this.getSelectedFile();
		if(file == null) return false;
		
		// Ensure a tab exists
		Tab tab = this.getGui().getEditorFrame().getOpenedTab();
		if(tab == null) return false;
		
		// Ensure the file has an appropriate extension
		file = FileUtils.extendToZab(file);
		
		// Save the file
		return ZabAppSettings.save(file, tab, true);
	}
	
}
