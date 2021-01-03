package util;

import java.io.File;

/**
 * A class containing methods for operating on, and using, File objects 
 * @author zrona
 */
public final class FileUtils{
	
	/**
	 * Create a String which can be used to create a new {@link File}
	 * @param path The path to the file, no name
	 * @param name The name of the file, do not include file extension
	 * @param extension The file extension to use, do not include a dot
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
	
	/** Cannot instantiate FileUtils */
	private FileUtils(){}
	
}
