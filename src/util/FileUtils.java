package util;

import java.io.File;

/**
 * A class containing methods for operating on, and using, File objects 
 * @author zrona
 */
public final class FileUtils{

	/** The file extension representing a .zab file, not including the dot */
	public static final String ZAB_EXTENSION = "zab";
	
	/**
	 * Create a String which can be used to create a new {@link File}
	 * @param path The path to the file, no name
	 * @param name The name of the file, do not include file extension
	 * @param extension The file extension to use, do not include a dot, can be null for a directory
	 * @return The file string
	 */
	public static String makeFileName(String path, String name, String extension){
		if(path == null) path = "";
		if(name == null) name = "";
		if(extension == null) extension = "";
		else extension = ".".concat(extension);
		if(!path.endsWith("/")) path = path.concat("/");
		
		return String.join("", path, name, extension);
	}
	
	/**
	 * Create a String which can be used to create a new {@link File} as a directory
	 * @param path The path to the file, no name
	 * @param name The name of the file, do not include file extension
	 * @return The file string
	 */
	public static String makeFileName(String path, String name){
		return makeFileName(path, name, null);
	}
	
	/**
	 * Get a version of the given file with the given extension
	 * @param file The file to base the extension
	 * @param ext The extension
	 * @return The new file with the extension, or null if file or ext is null. 
	 * 	Can be a copy of the given file if it already has the given extension
	 */
	public static File extendTo(File file, String ext){
		if(file == null || ext == null) return null;
		String path = file.getPath();
		
		// Check if the extension does or does not have a dot
		if(!ext.startsWith(".")) ext = ".".concat(ext);
		
		// Check if the path needs the extension
		if(!path.endsWith(ext)) path = path.concat(ext);
		return new File(path);
	}
	
	/**
	 * Get a version of the given file with the extension of a .zab file
	 * @param f The file to base the extension
	 * @return The new file with the extension, or null if f null. 
	 * 	Can be a copy of the given file if it already has the extension .zab extension
	 */
	public static File extendToZab(File f){
		return extendTo(f, ZAB_EXTENSION);
	}
	
	/** Cannot instantiate FileUtils */
	private FileUtils(){}
	
}
