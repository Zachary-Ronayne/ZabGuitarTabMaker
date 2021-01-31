package appUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appUtils.settings.ZabSettings;
import lang.Language;
import tab.Tab;
import util.FileUtils;

/**
 * A class holding an instance of {@link ZabSettings} and the current theme for the application<br>
 * Must call {@link #init()} before using any settings in this class
 * @author zrona
 */
public class ZabAppSettings{
	/** The location of all saved data specific to the Zab Application */
	public static final String DATA_LOCATION = "./ZabData";
	/** The location of the file for theme data */
	public static final String THEME_SAVE_LOCATION = DATA_LOCATION + "/Theme.txt";
	
	/** The settings instance */
	private static ZabSettings settings;
	
	/** The {@link ZabTheme} instance of the Zab application */
	private static ZabTheme theme;
	
	/**
	 * Initialize the settings to a default state, also initializes the language and theme
	 */
	public static void init(){
		Language.init();
		settings = new ZabSettings();
		loadTheme();
	}
	
	/**
	 * Remove references to the static settings and theme objects held by this class
	 */
	public static void reset(){
		settings = null;
		theme = null;
	}
	
	/** @return See {@link #settings} */
	public static ZabSettings get(){
		return settings;
	}
	
	/** @return See {@link #theme} */
	public static ZabTheme theme(){
		return theme;
	}
	
	/**
	 * Set the current theme of the application.
	 * 	This method will recursively set the theme of every one of it's components to the new theme. 
	 * 	It will also replace the given {@link ZabGui} with a new version containing the objects in that gui
	 * @param theme The theme to set
	 * @param gui The {@link ZabGui} which needs to be updated to the given theme.
	 * @param save true to save the theme to the theme file location, false otherwise
	 * 	Can be null to not update anything.
	 */
	public static void setTheme(ZabTheme theme, ZabGui gui, boolean save){
		// Update the theme variable
		ZabAppSettings.theme = theme;
		
		// Save the theme to the file if applicable
		if(save) saveTheme();
		
		// Set the relevant UIManager values
		ZabTheme.setUIMangerTheme(theme);
		
		// If the gui given is null, do nothing more
		if(gui == null) return;
		
		// Hide the old gui
		gui.setVisible(false);
		
		// Create a new gui and copy over the data from that old gui
		ZabGui newGui = new ZabGui();
		gui.copyData(newGui);
		newGui.updateTheme();
		if(ZabConstants.SHOW_GUI_ON_INIT) newGui.setVisible(true);
		newGui.repaint();
		
		// Get rid of the old gui
		gui.dispose();
	}
	
	/**
	 * Get the {@link File} used to save the {@link ZabTheme}
	 * @return The file
	 */
	public static File getThemeFile(){
		return new File(THEME_SAVE_LOCATION);
	}
	
	/**
	 * Set the theme currently used to that of the default
	 */
	public static void loadDefaultTheme(){
		setTheme(ZabTheme.getDefaultTheme(), null, true);
	}
	
	/**
	 * Load the theme from the standard location and set it. If the location cannot be found, the default theme is loaded
	 * @return true if the theme was loaded, false otherwise
	 */
	public static boolean loadTheme(){
		boolean success = true;

		File file = getThemeFile();
		try{
			// Make the scanner
			Scanner read = new Scanner(file);
			try{
				String line = read.nextLine();
				line = line.replace("\n", "").replace("\r", "");
				ZabTheme t;
				// If the dark or light theme text is found, load that theme, otherwise set the default theme
				switch(line){
					case "DarkTheme": t = new ZabTheme.DarkTheme(); break;
					case "LightTheme": t = new ZabTheme.LightTheme(); break;
					default: t = ZabTheme.getDefaultTheme(); success = false; break;
				}
				setTheme(t, null, false);
				
			}finally{
				read.close();
			}
		}catch(Exception e){
			// Accounting for if the file is not found, load the default theme
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			loadDefaultTheme();
			return false;
		}
		
		return success;
	}
	
	/**
	 * Save the theme to the standard location. Will create the location if needed
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean saveTheme(){
		// If the file doesn't exist, ensure it has the correct directory
		File file = getThemeFile();
		if(!file.exists()) file.getParentFile().mkdirs();
		
		try{
			PrintWriter write = new PrintWriter(file);
			try{
				write.println(theme().getClass().getSimpleName());
			}finally{
				write.close();
			}
		}catch(Exception e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Create a String which can be used to create a new {@link File}
	 * @param filePath The path to the file, no name
	 * @param name The name of the file, do not include file extension
	 * @return The file string
	 */
	public static String makeFileName(String filePath, String name){
		return FileUtils.makeFileName(filePath, name, "zab");
	}
	
	/**
	 * Load the static instance of settings from the given {@link Scanner}, along with the given tab. 
	 * This method does nothing and returns false if tab is null and saveSettings is false
	 * @param reader The {@link Scanner} to use for loading
	 * @param tab The tab to load, or null to not load a tab
	 * @param loadSettings true if settings should also be loaded, false otherwise
	 * @return true if the load was successful, false otherwise
	 */
	public static boolean load(Scanner reader, Tab tab, boolean loadSettings){
		if(tab == null && !loadSettings) return false;

		boolean success = true;
		
		try{
			if(loadSettings){
				if(!settings.load(reader)) success = false;
			}
			if(tab != null){
				if(!tab.load(reader)) success = false;
			}
		}
		catch(Exception e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/**
	 * Load the static instance of settings from the given file path and name, along with the given tab. 
	 * This method does nothing and returns false if tab is null and saveSettings is false, or if the file does not exist
	 * @param file The file to load the tab or settings from
	 * @param tab The tab to load, or null to not load a tab
	 * @param loadSettings true if settings should also be loaded, false otherwise
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean load(File file, Tab tab, boolean loadSettings){
		if(file == null || !file.exists()) return false;
		
		boolean success = true;
		try{
			Scanner reader = new Scanner(file);
			try{
				success = load(reader, tab, loadSettings);
			}finally{
				reader.close();
			}
		}
		catch(FileNotFoundException e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/**
	 * Load the static instance of settings from the given file path and name, along with the given tab. 
	 * This method does nothing and returns false if tab is null and saveSettings is false
	 * @param filePath The path to the file, not including the file itself
	 * @param name The name of the file, not including a file extension
	 * @param tab The tab to load, or null to not load a tab
	 * @param loadSettings true if settings should also be loaded, false otherwise
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean load(String filePath, String name, Tab tab, boolean loadSettings){
		File file = new File(makeFileName(filePath, name));
		return load(file, tab, loadSettings);
	}

	/**
	 * Load a file which is only a tab
	 * @param filePath The path to the file, not including the file itself
	 * @param name The name of the file, not including a file extension
	 * @param tab The tab to load
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean load(String filePath, String name, Tab tab){
		return load(filePath, name, tab, false);
	}
	
	/**
	 * Load the static instance of settings from the given file path and name
	 * @param filePath The path to the file, not including the file itself
	 * @param name The name of the file, not including a file extension
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean load(String filePath, String name){
		return load(filePath, name, null, true);
	}
	
	/**
	 * Save the static instance of settings with the given {@link PrintWriter}, along with the given tab. 
	 * This method does nothing and returns false if tab is null and saveSettings is false
	 * @param writer The {@link PrintWriter} to use for saving
	 * @param tab The tab to save, or null to not save a tab
	 * @param saveSettings true to save the settings with the writer, false otherwise
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean save(PrintWriter writer, Tab tab, boolean saveSettings){
		if(tab == null && !saveSettings) return false;
		boolean success = true;
		
		if(saveSettings){
			if(!settings.save(writer)) success = false;
		}
		if(success && tab != null){
			if(!tab.save(writer)) success = false;
		}
		
		return success;
	}
	
	/**
	 * Save the static instance of settings to the given file path and name, along with the given tab. 
	 * This method does nothing and returns false if tab is null and saveSettings is false
	 * @param file The {@link File} in which to save the settings and or tab
	 * @param tab The tab to save, or null to not save a tab
	 * @param saveSettings true to save the settings with the file, false otherwise
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean save(File file, Tab tab, boolean saveSettings){
		boolean success = true;
		try{
			PrintWriter writer = new PrintWriter(file);
			try{
				success = save(writer, tab, saveSettings);
			}finally{
				writer.close();
			}
		}
		catch(FileNotFoundException | SecurityException e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/**
	 * Save the static instance of settings with the given {@link PrintWriter}, along with the given tab
	 * @param writer The {@link PrintWriter} to use for saving
	 * @param tab The tab to save, or null to not save a tab
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean save(PrintWriter writer, Tab tab){
		return save(writer, tab, true);
	}
	
	/**
	 * Save the static instance of settings to the given file path and name, along with the given tab. 
	 * This method does nothing and returns false if tab is null and saveSettings is false
	 * @param filePath The path to the file, not including the file itself
	 * @param name The name of the file, not including a file extension
	 * @param tab The tab to save, or null to not save a tab
	 * @param saveSettings true to save the settings with the file, false otherwise
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean save(String filePath, String name, Tab tab, boolean saveSettings){
		File file = new File(makeFileName(filePath, name));
		return save(file, tab, saveSettings);
	}
	
	/**
	 * Save the given tab to a file
	 * @param filePath The path to the file, not including the file itself
	 * @param name The name of the file, not including a file extension
	 * @param tab The tab to save
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean save(String filePath, String name, Tab tab){
		return save(filePath, name, tab, false);
	}
	
	/**
	 * Save the static instance of settings to the given file path and name
	 * @param filePath The path to the file, not including the file itself
	 * @param name The name of the file, not including a file extension
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean save(String filePath, String name){
		return save(filePath, name, null, true);
	}
	
	/** Cannot instantiate {@link ZabAppSettings} */
	private ZabAppSettings(){}
	
}
