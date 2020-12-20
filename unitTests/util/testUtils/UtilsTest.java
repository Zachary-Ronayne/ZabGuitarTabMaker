package util.testUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import util.Saveable;

/**
 * A class holding some utilities for writing test cases
 * @author zrona
 */
public final class UtilsTest{
	
	/** Constant for testing approximate equality of floating point values */
	public static final double DELTA = 0.00000001;
	
	/**
	 * Utility for testing {@link Saveable} objects
	 * Using the {@link Saveable#save(PrintWriter)} method, simulate saving the given object, 
	 * 	and return a string representing the saved text.<br>
	 * Automatically removes excess \r characters
	 * @param s The {@link Saveable} object
	 * @return The string, or null if saving failed
	 */
	public static String testSave(Saveable s){
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		BufferedOutputStream bs = new BufferedOutputStream(bytes);
		PrintWriter write = new PrintWriter(bs);
		boolean success = s.save(write);
		if(!success) return null;
		write.close();
		return removeSlashR(bytes.toString());
	}
	
	/**
	 * Utility method for removing \r characters from strings
	 * @param s The string to remove
	 * @return The same String as s, but with no \r characters
	 */
	public static String removeSlashR(String s){
		return s.replace((char)13 + "", "");
	}
	
}
