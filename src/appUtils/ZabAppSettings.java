package appUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import tab.Tab;
import util.FileUtils;

/**
 * A class holding an instance of {@link ZabSettings}. for the application using {@link ZabSettings}<br>
 * Must call {@link #init()} before using any settings in this class
 * @author zrona
 */
public class ZabAppSettings{
	
	/** The settings used */
	private static ZabSettings settings;
	
	/**
	 * Initialize the settings to a default state
	 */
	public static void init(){
		settings = new ZabSettings();
	}
	
	/**
	 * Get the instance of settings
	 * @return The instance
	 */
	public static ZabSettings get(){
		return settings;
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
		if(!file.exists()) return false;
		
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
		
		try{
			if(saveSettings){
				if(!settings.save(writer)) success = false;
			}
			if(tab != null){
				if(!tab.save(writer)) success = false;
			}
		}
		catch(Exception e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			success = false;
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
