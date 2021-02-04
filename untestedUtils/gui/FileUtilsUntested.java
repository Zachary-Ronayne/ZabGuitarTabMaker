package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import appUtils.ZabConstants;

/**
 * File utility methods which are not tested
 * @author zrona
 */
public final class FileUtilsUntested{
	
	/**
	 * Given a file, ensure that it has a parent. This method assumes either the file will have a parent, or one will be able to be created
	 * If it does not exist, it will attempt to create the directories defined by the file.
	 * @param file The file to check for a parent 
	 * @return true if the file has a parent, or a parent could be created, false otherwise
	 */
	public static boolean ensureParentExists(File file){
		File parent = file.getParentFile();
		if(parent != null){
			if(!parent.exists() && !parent.mkdirs()) return false;
		}
		return true;
	}
	
	/**
	 * Create a {@link PrintWriter} which writes to the given {@link File}.
	 * It is assumed exceptions will not happen, i.e. no {@link FileNotFoundException} or {@link SecurityException}.
	 * @param file The file to use to generate the {@link PrintWriter}
	 * @return The {@link PrintWriter}, or null if an error occurred
	 */
	public static PrintWriter createFilePrintWriter(File file){
		try{
			return new PrintWriter(file);
		}
		catch(FileNotFoundException | SecurityException e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return null;
		}
	}
	
	/** Cannot instantiate {@link FileUtilsUntested} */
	private FileUtilsUntested(){}
	
}
