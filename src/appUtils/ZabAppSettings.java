package appUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import tab.Tab;
import util.Saveable;

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
		if(filePath == null) filePath = "";
		if(name == null) name = "";
		if(!filePath.endsWith("/")) filePath = filePath.concat("/");
		
		return filePath.concat(name).concat(".zab");
	}
	
	/**
	 * Load the static instance of settings from the given {@link Scanner}, along with the given tab
	 * @param reader The {@link Scanner} to use for loading
	 * @param tab The tab to load, or null to not load a tab
	 * @return true if the load was successful, false otherwise
	 */
	public static boolean load(Scanner reader, Tab tab){
		try{
			if(!settings.load(reader)) return false;
			if(tab != null){
				if(!tab.load(reader)) return false;
			}
			return true;
		}
		catch(Exception e){
			if(Saveable.printErrors) e.printStackTrace();
			return false;
		}
	}

	/**
	 * Load the static instance of settings from the given file path and name, along with the given tab
	 * @param filePath The path to the file, not including the file itself
	 * @param name The name of the file, not including a file extension
	 * @param tab The tab to load, or null to not load a tab
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean load(String filePath, String name, Tab tab){
		File file = new File(makeFileName(filePath, name));
		if(!file.exists()) return false;
		
		boolean success = true;
		try{
			Scanner reader = new Scanner(file);
			try{
				success = load(reader, tab);
			}finally{
				reader.close();
			}
		}
		catch(FileNotFoundException e){
			if(Saveable.printErrors) e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/**
	 * Load the static instance of settings from the given file path and name
	 * @param filePath The path to the file, not including the file itself
	 * @param name The name of the file, not including a file extension
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean load(String filePath, String name){
		return load(filePath, name, null);
	}
	
	/**
	 * Save the static instance of settings with the given {@link PrintWriter}, along with the given tab
	 * @param writer The {@link PrintWriter} to use for saving
	 * @param tab The tab to save, or null to not save a tab
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean save(PrintWriter writer, Tab tab){
		try{
			if(!settings.save(writer)) return false;
			if(tab != null){
				if(!tab.save(writer)) return false;
			}
			return true;
		}
		catch(Exception e){
			if(Saveable.printErrors) e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Save the static instance of settings to the given file path and name, along with the given tab
	 * @param filePath The path to the file, not including the file itself
	 * @param name The name of the file, not including a file extension
	 * @param tab The tab to save, or null to not save a tab
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean save(String filePath, String name, Tab tab){
		File file = new File(makeFileName(filePath, name));
		
		boolean success = true;
		try{
			PrintWriter writer = new PrintWriter(file);
			try{
				success = save(writer, tab);
			}finally{
				writer.close();
			}
		}
		catch(FileNotFoundException | SecurityException e){
			if(Saveable.printErrors) e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/**
	 * Save the static instance of settings to the given file path and name
	 * @param filePath The path to the file, not including the file itself
	 * @param name The name of the file, not including a file extension
	 * @return true if the save was successful, false otherwise
	 */
	public static boolean save(String filePath, String name){
		return save(filePath, name, null);
	}
	
	/** Cannot instantiate {@link ZabAppSettings} */
	private ZabAppSettings(){}
	
}
