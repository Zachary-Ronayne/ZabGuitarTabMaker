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
	 * Using a the {@link Saveable#save(PrintWriter)} method, simulate save the given object, 
	 * 	and return a string representing the saved text.<br>
	 * Automatically removes excess characters
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
		return bytes.toString().replace((char)13 + "", "");
	}
	
}
