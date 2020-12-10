package util;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * An interface representing an object which can be saved to a file using {@link Scanner} and {@link PrintWriter} objects
 * @author zrona
 */
public interface Saveable{

	/**
	 * Load in all values for this object from the given {@link Scanner}<br>
	 * Must ensure that the associated save method will save the object in an exact format which can be loaded by this save method.<br>
	 * After this method is called, no artifacts from this {@link Saveable} should be left in the {@link Scanner}
	 * @param reader The {@link Scanner}, usually a file.<br>
	 * 	Do not close this object, that should be handled by the creator of the object
	 * @return true if the load was successful, false otherwise
	 */
	public boolean load(Scanner reader);
	
	/**
	 * Save all values for this object to the given {@link PrintWriter}<br>
	 * Must ensure that the associated load method will load the object from the exact format which this save method used<br>
	 * After this method is called, the {@link PrintWriter} should be ready for another {@link #save(PrintWriter)} call from any other {@link Saveable}
	 * @param writer The {@link PrintWriter}, usually a file
	 * 	Do not close this object, that should be handled by the creator of the object
	 * @return true if the save was successful, false otherwise
	 */
	public boolean save(PrintWriter writer);
}
