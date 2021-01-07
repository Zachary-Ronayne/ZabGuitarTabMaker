package util;

import java.io.PrintWriter;
import java.util.Scanner;

import appUtils.ZabConstants;

/**
 * An interface representing an object which can be saved to a file using {@link Scanner} and {@link PrintWriter} objects.<br>
 * Also contains static utility methods for loading in values and handling error checking.<br>
 * When using the utility methods, be sure to put new lines between different objects in the same file.<br>
 * For example, if multiple integers are stored on one line, and a string on the next line, a new line must be added so that 
 * the exact string can be loaded on its own line
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
	
	/**
	 * Load a single primitive values, the entire line for a string, into an object array.
	 * When loading a string, automatically advances to the next line, when loading any other value, remains on the same line 
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @param type The type to load:
	 * 	<ul>
	 * 		<li>0: boolean</li>
	 * 		<li>1: int</li>
	 * 		<li>2: double</li>
	 * 		<li>3: string</li>
	 * 	</ul>
	 * @return null if any error occurred with loading the value, otherwise the resulting array of values of the given size
	 */
	public static Object loadObject(Scanner reader, int type){
		if(reader == null) return null;
		try{
			switch(type){
				case 0: return reader.nextBoolean();
				case 1: return reader.nextInt();
				case 2: return reader.nextDouble();
				case 3:	return reader.nextLine();
				default: return null;
			}
		}
		catch(Exception e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return null;
		}
	}
	/**
	 * Load all of the primitive values, each separated by a space, or a new line in the case of strings, into an object array. 
	 * All objects loaded must be of the same type
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @param size The number of values to be loaded
	 * @param type The type to load:
	 * 	<ul>
	 * 		<li>0: boolean</li>
	 * 		<li>1: int</li>
	 * 		<li>2: double</li>
	 * 		<li>3: string</li>
	 * 	</ul>
	 * @return null if any error occurred with loading the values, otherwise the resulting array of values of the given size
	 */
	public static Object[] loadObjects(Scanner reader, int size, int type){
		Object[] arr = new Object[size];
		for(int i = 0; i < size; i++){
			arr[i] = loadObject(reader, type);
			if(arr[i] == null) return null;
		}
		return arr;
	}
	
	/**
	 * Load a single boolean value
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @return null if any error occurred with loading the values, otherwise the value
	 */
	public static Boolean loadBool(Scanner reader){
		try{
			return (Boolean)loadObject(reader, 0);
		}catch(ClassCastException e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Load all of the boolean values from one line, each separated by a space, into an int array
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @param size The number of values to be loaded
	 * @return null if any error occurred with loading the values, otherwise the resulting array of values of the given size
	 */
	public static Boolean[] loadBools(Scanner reader, int size){
		Boolean[] bools = new Boolean[size];
		for(int i = 0; i < size; i++){
			bools[i] = loadBool(reader);
			if(bools[i] == null) return null;
		}
		return bools;
	}
	
	/**
	 * Load a single integer value
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @return null if any error occurred with loading the values, otherwise the resulting value
	 */
	public static Integer loadInt(Scanner reader){
		try{
			return (Integer)loadObject(reader, 1);
		}catch(ClassCastException e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Load all of the integer values from one line, each separated by a space, into an int array
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @param size The number of values to be loaded
	 * @return null if any error occurred with loading the values, otherwise the resulting array of values of the given size
	 */
	public static Integer[] loadInts(Scanner reader, int size){
		Integer[] ints = new Integer[size];
		for(int i = 0; i < size; i++){
			ints[i] = loadInt(reader);
			if(ints[i] == null) return null;
		}
		return ints;
	}
	
	/**
	 * Load a single double value
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @return null if any error occurred with loading the values, otherwise the resulting value
	 */
	public static Double loadDouble(Scanner reader){
		try{
			return (Double)loadObject(reader, 2);
		}catch(ClassCastException e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Load all of the double values from one line, each separated by a space, into an double array
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @param size The number of values to be loaded
	 * @return null if any error occurred with loading the values, otherwise the resulting array of values of the given size
	 */
	public static Double[] loadDoubles(Scanner reader, int size){
		Double[] dubs = new Double[size];
		for(int i = 0; i < size; i++){
			dubs[i] = loadDouble(reader);
			if(dubs[i] == null) return null;
		}
		return dubs;
	}
	
	/**
	 * Load a single string value
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @return null if any error occurred with loading the values, otherwise the resulting value
	 */
	public static String loadString(Scanner reader){
		try{
			return (String)loadObject(reader, 3);
		}catch(ClassCastException e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Load all of the string values from one line, each separated by a new line, into an string array
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @param size The number of values to be loaded
	 * @return null if any error occurred with loading the values, otherwise the resulting array of values of the given size
	 */
	public static String[] loadStrings(Scanner reader, int size){
		String[] strs = new String[size];
		for(int i = 0; i < size; i++){
			strs[i] = loadString(reader);
			if(strs[i] == null) return null;
		}
		return strs;
	}
	
	/**
	 * Load a single {@link Saveable} object.<br>
	 * Automatically checks for null values and errors and returns false on either.
	 * @param reader The {@link Scanner} to use for loading
	 * @return toLoad The object to load
	 * @return true if loading was successful, false otherwise.
	 */
	public static boolean load(Scanner reader, Saveable toLoad){
		if(reader == null || toLoad == null) return false;
		try{
			if(!toLoad.load(reader)) return false;
		}
		catch(Exception e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Load an array of {@link Saveable} objects.<br>
	 * Automatically checks for null values and errors and returns false on either.
	 * @param reader The {@link Scanner} to use for loading
	 * @return toLoad The list of objects to load
	 * @return true if loading was successful, false otherwise.
	 */
	public static boolean loadMultiple(Scanner reader, Saveable[] toLoad){
		if(reader == null || toLoad == null) return false;
		for(Saveable s : toLoad){
			if(s == null) return false;
			if(!load(reader, s)) return false;
		}
		return true;
	}
	
	/**
	 * Save a single object on one line, using its toString value, followed by whitespace<br>
	 * Automatically checks for null values and errors and returns false on either. 
	 * Cannot save a string with new line characters.
	 * @param value The value to save
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @param newLine true to put a new line after the object, false for a space
	 * @return true if the save was successful, false otherwise.
	 */
	public static boolean saveToString(PrintWriter writer, Object value, boolean newLine){
		if(writer == null || value == null) return false;
		try{
			String save = "";
			save = save.concat(String.valueOf(value));
			
			if(save.contains("\n")) throw new IllegalArgumentException("Cannot save a string with new line characters");
			
			save = save.concat(newLine ? "\n" : " ");
			writer.print(save);
			return true;
		}
		catch(Exception e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Save a single object on one line, using its toString value, followed by a space<br>
	 * Automatically checks for null values and errors and returns false on either.
	 * Cannot save a string with new line characters.
	 * @param value The value to save
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @return true if the save was successful, false otherwise.
	 */
	public static boolean saveToString(PrintWriter writer, Object value){
		return saveToString(writer, value, false);
	}
	
	/**
	 * Save an array of {@link Saveable} objects on one line, using their toString values, each separated by whitespace.<br>
	 * Automatically checks for null values and errors and returns false on either.
	 * Cannot save a string with new line characters.
	 * @param values The values to save
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @param newLine true to put a new line between each object, false for a space
	 * @return true if the save was successful, false otherwise.
	 */
	public static boolean saveToStrings(PrintWriter writer, Object[] values, boolean newLine){
		if(writer == null || values == null) return false;
		for(Object obj : values){
			if(obj == null) return false;
			if(!saveToString(writer, obj, newLine)) return false;
		}
		return true;
	}
	
	/**
	 * Save an array of {@link Saveable} objects on one line, using their toString values, each separated by a space<br>
	 * Automatically checks for null values and errors and returns false on either.
	 * Cannot save a string with new line characters.
	 * @param values The values to save
	 * @param writer The {@link PrintWriter} to use to save the object
	 * @return true if the save was successful, false otherwise.
	 */
	public static boolean saveToStrings(PrintWriter writer, Object[] values){
		return saveToStrings(writer, values, false);
	}
	
	/**
	 * Save a single {@link Saveable} object, followed by a new line<br>
	 * Automatically checks for null values and errors and returns false on either.
	 * @param writer The {@link PrintWriter} to use for saving
	 * @param toSave The of object to save
	 * @return true if saving was successful, false otherwise.
	 */
	public static boolean save(PrintWriter writer, Saveable toSave){
		if(writer == null || toSave == null) return false;
		try{
			if(!toSave.save(writer)) return false;
		}
		catch(Exception e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Save an array of {@link Saveable} objects, each on their own line.<br>
	 * Automatically checks for null values and errors and returns false on either.
	 * @param writer The {@link PrintWriter} to use for saving
	 * @param toSave The list of objects to save
	 * @return true if saving was successful, false otherwise.
	 */
	public static boolean saveMultiple(PrintWriter writer, Saveable[] toSave){
		if(writer == null || toSave == null) return false;
		for(Saveable s : toSave){
			if(!save(writer, s)) return false;
		}
		return true;
	}
	
	/**
	 * Utility method for advancing a {@link Scanner} by one line and handling error checking.
	 * @param reader The {@link Scanner} to advance
	 * @return true if no error occurred, false otherwise
	 */
	public static boolean nextLine(Scanner reader){
		try{
			reader.nextLine();
			return true;
		}catch(Exception e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Utility method for writing a new line with a {@link Scanner} and handling error checking.
	 * @param writer The {@link PrintWriter} to write the new line
	 * @return true if no error occurred, false otherwise
	 */
	public static boolean newLine(PrintWriter writer){
		try{
			writer.println();
			return true;
		}catch(Exception e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			return false;
		}
	}
	
}
