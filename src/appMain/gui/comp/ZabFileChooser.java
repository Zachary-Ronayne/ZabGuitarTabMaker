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
import tab.Tab;

/**
 * A {@link JFileChooser} designed for the Zab application for selecting files
 * @author zrona
 */
public class ZabFileChooser extends JFileChooser{
	private static final long serialVersionUID = 1L;

	/** The file extension representing a .zab file, not including the dot */
	public static final String ZAB_EXTENSION = "zab";
	
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
		this.zabFileFilter = new FileNameExtensionFilter("Zab stringed instrument tablature", ZAB_EXTENSION);
		
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
	 * Prepare for loading or saving a file by ensuring the path to the specified file exists, 
	 * setting the directory of this {@link JFileChooser} to the given path, and setting the appropriate file filter
	 * @param path The path to set
	 */
	public void filePrep(String path){
		// Ensure the directory to the save location exists
		File savesLoc = new File(path);
		if(!savesLoc.exists()) savesLoc.mkdirs();
		this.setCurrentDirectory(savesLoc);
		
		// Set the filter to only use .zab files
		this.setFileFilter(this.getZabFileFilter());
	}
	
	/**
	 * Request that the user selects a file for exporting, and return that file
	 * @param extension The file extension, no dot
	 * @return The selected file, can be null if no file was selected. This file is also the same which can be obtained by {@link #getSelectedFile()}
	 */
	public File exportSelect(String extension){
		// Prep for selection
		this.filePrep(DEFAULT_SAVCE_LOC);
		this.setFileFilter(getAcceptAllFileFilter());
		
		// Request the file selection
		this.showDialog(this.getGui(), "Select location");
		
		// Add the file extension and return the selected file
		this.setSelectedFile(extendTo(getSelectedFile(), extension));
		return this.getSelectedFile();
	}
	
	/**
	 * Load the {@link Tab} of {@link #gui} from the file selected by the user
	 */
	public void loadTab(){
		this.filePrep(DEFAULT_SAVCE_LOC);
		
		// Open the save window and wait for the user to pick a file name
		this.setApproveButtonText("Load tab");
		this.showOpenDialog(null);

		// Ensure a file was selected
		File file = this.getSelectedFile();
		if(file == null) return;
		
		// Ensure a tab exists
		EditorFrame frame = this.getGui().getEditorFrame();
		Tab tab = frame.getOpenedTab();
		if(tab == null) frame.setOpenedTab(new Tab());
		
		// Load the tab from the file
		ZabAppSettings.load(file, tab, true);
		
		// Update the GUI to reflect the loaded tab
		getGui().repaint();
	}
	
	/**
	 * Save the {@link Tab} of {@link #gui} to the file selected by the user
	 */
	public void saveTab(){
		this.filePrep(DEFAULT_SAVCE_LOC);
		
		// Open the save window and wait for the user to pick a file name
		this.setApproveButtonText("Save tab");
		this.showSaveDialog(null);

		// Ensure a file was selected
		File file = this.getSelectedFile();
		if(file == null) return;
		
		// Ensure a tab exists
		Tab tab = this.getGui().getEditorFrame().getOpenedTab();
		if(tab == null) return;
		
		// Ensure the file has an appropriate extension
		file = extendToZab(file);
		
		// Save the file
		ZabAppSettings.save(file, tab, true);
	}
	
	/**
	 * Get a version of the given file with the extension of a .zab file
	 * @param f The file to base the extension
	 * @return The new file with the extension
	 */
	public static File extendToZab(File f){
		return extendTo(f, ZAB_EXTENSION);
	}
	
	/**
	 * Get a version of the given file with the given extension
	 * @param f The file to base the extension
	 * @param ext The extension with no dot
	 * @return The new file with the extension
	 */
	public static File extendTo(File file, String ext){
		String path = file.getPath();
		ext = ".".concat(ext);
		if(!path.endsWith(ext)) path = path.concat(ext);
		return new File(path);
	}
	
}
