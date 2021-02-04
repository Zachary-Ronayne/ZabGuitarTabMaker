package appMain.gui.comp;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appMain.gui.editor.frame.EditorFrame;
import appUtils.ZabConstants;
import lang.AbstractLanguage;
import lang.Language;
import tab.Tab;
import util.FileUtils;

/**
 * A {@link JFileChooser} designed for the Zab application for selecting files
 * @author zrona
 */
public class ZabFileChooser extends JFileChooser{
	private static final long serialVersionUID = 1L;
	
	/** The path to the default directory for saving and loading files */
	public static final String DEFAULT_SAVES_LOC = "./saves";
	
	/** The {@link ZabFrame} which uses this {@link ZabFileChooser} */
	private ZabFrame frame;
	
	/** A {@link FileFilter} that accepts .zab files */
	private FileFilter zabFileFilter;
	
	/** The last directory this FileChooser was opened to. If this information is unknown, this will default to the path defined by {@link #DEFAULT_SAVES_LOC} */
	private File lastLocation;
	
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
		AbstractLanguage lang = Language.get();
		this.zabFileFilter = new FileNameExtensionFilter(lang.zabExtensionDescription(), FileUtils.ZAB_EXTENSION);
		
		// Set this file chooser to only allow one file, not multiple files, and only files, not directories
		this.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.setMultiSelectionEnabled(false);
		
		// The last location is always the saves folder initially
		this.loadDefaultSaveLocation();
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
	
	/** @return See {@link #lastLocation} */
	public File getLastLocation(){
		return this.lastLocation;
	}
	
	/**
	 * Set the last loaded location to the default saved location
	 */
	private void loadDefaultSaveLocation(){
		this.lastLocation = new File(DEFAULT_SAVES_LOC);
	}
	
	/**
	 * Prepare for processing a file by ensuring the path to the specified file exists  
	 * and setting the directory of this {@link ZabFileChooser} to the given path
	 * @param file The file to use
	 */
	public void filePrep(File file){
		if(!file.exists()) file.mkdirs();
		this.setCurrentDirectory(file);
	}
	
	/**
	 * Take the current location of this {@link ZabFileChooser} and save it. That location will later 
	 * be loaded the next time the loaded is opened
	 * @return true if the location was set, false otherwise
	 */
	public boolean saveCurrentLocation(){
		File f = this.getSelectedFile();
		// If there is no file, do not set it
		if(f == null) return false;
		
		String path = f.getParent();
		if(path == null) return false;
		this.lastLocation = new File(path);
		
		return true;
	}
	
	/**
	 * Save the {@link Tab} of {@link #getGui()} to the file selected by the user
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveTab(){
		this.filePrep(this.getLastLocation());
		// Set the filter to only use .zab files
		this.setFileFilter(this.getZabFileFilter());
		
		// Set up the file to initially be nothing, if one is not selected
		File file = null;
		int state;
		// Open the save window and wait for the user to pick a file name, only if the dialog is enabled
		if(ZabConstants.ENABLE_DIALOG) state = this.showDialog(null, "Save tab");
		// If it is not enabled, whatever file is selected is automatically approved if it is not null
		else state = (this.getSelectedFile() == null) ? JFileChooser.CANCEL_OPTION : JFileChooser.APPROVE_OPTION;
		// If the save was not canceled, get the selected file
		// Otherwise a null file is sent, which cancels the save
		if(state != JFileChooser.CANCEL_OPTION) file = this.getSelectedFile();
		
		// After the file was selected, save that location
		this.saveCurrentLocation();
		
		// Save the file
		return this.saveTab(file);
	}
	
	/**
	 * Save the tab currently loaded by the {@link EditorFrame} of {@link #getGui()} to the given file. 
	 * This method assumes the file is a valid file, if anything goes wrong with saving the file, this method returns false.
	 * @param f The {@link File} to use
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveTab(File f){
		// Ensure the file has an appropriate extension
		f = FileUtils.extendToZab(f);

		// Save the tab
		return this.getGui().getEditorFrame().save(f);
	}

	/**
	 * Load the {@link Tab} of {@link #gui} from the file selected by the user
	 * @return true if the load was successful, false otherwise
	 */
	public boolean loadTab(){
		this.filePrep(this.getLastLocation());
		// Set the filter to only use .zab files
		this.setFileFilter(this.getZabFileFilter());
		
		// Open the save window and wait for the user to pick a file name
		if(ZabConstants.ENABLE_DIALOG){
			int state = this.showDialog(null, "Load tab");
			// If the load was canceled, fail the load
			if(state == JFileChooser.CANCEL_OPTION) return false;
		}
		
		// After the file was selected, even if one was not selected, save that location
		this.saveCurrentLocation();
		
		// Perform the load
		return this.getGui().getEditorFrame().load(this.getSelectedFile());
	}
	
	/**
	 * Request that the user selects a file for exporting, and return that file
	 * @param extension The file extension, no dot
	 * @return The selected file, can be null if no file was selected. This file is also the same which can be obtained by {@link #getSelectedFile()}
	 */
	public File exportSelect(String extension){
		// Prep for selection
		this.filePrep(this.getLastLocation());
		
		// Set filter to all files
		this.setFileFilter(getAcceptAllFileFilter());

		// Request the file selection
		if(ZabConstants.ENABLE_DIALOG) this.showDialog(null, "Select location");
		
		// Add the file extension
		this.setSelectedFile(FileUtils.extendTo(this.getSelectedFile(), extension));

		// After the file was selected, save that location
		this.saveCurrentLocation();
		
		// Return the selected file
		return this.getSelectedFile();
	}
	
}
