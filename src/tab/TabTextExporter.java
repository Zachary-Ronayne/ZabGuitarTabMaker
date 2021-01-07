package tab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import appUtils.ZabConstants;
import music.Music;
import tab.symbol.TabSymbol;
import util.FileUtils;
import util.Saveable;
import util.StringUtils;

/**
 * A utility class containing methods to convert a {@link Tab} into a String
 * @author zrona
 */
public final class TabTextExporter{
	
	/**
	 * Export a tab to a text String with no special parameters
	 * @param tab The {@link Tab} to export
	 * @return The exported String, or null if an issue occurred with exporting. Should be viewed with a monospace font.
	 */
	public static String export(Tab tab){
		if(tab == null) return null;
		
		// TODO add settings for all of these values
		
		// The text string added before each tab string
		String offset = "";
		// The text placed directly after the note name
		String afterNoteName = "|";
		// The character used to fill the space between note names of differing length
		char noteNameFiller = ' ';
		// Whether or not the note name filler should come before or after the name
		boolean noteNameFillerBefore = false;
		// Whether or not to include the octave number for each number
		boolean noteNameOctave = false;
		// The way to display the text notes for flat/sharp notes, 0 for all flats, 1 for all sharps.
		// This is an integer to keep it open to other formats, i.e. mix of flat and sharps, including double sharps, using E# instead of F
		int noteNameFormat = 1;
		// The text added before each symbol is added
		String symbolBefore = "-";
		// The text added after each symbol is added
		String symbolAfter = "-";
		// The character used to fill up the tab
		char tabFiller = '-';
		// Whether the tab filler should be placed before or after the symbols
		boolean tabFillerBefore = false;
		// The string to place at the end of every tabString
		String tabEnd = "|";
		
		// Get all of the TabStrings
		ArrayList<TabString> tabStrings = tab.getStrings();
		int numStrings = tabStrings.size();
		
		// Make one text String for each of the TabStrings for exporting, and a list for adding the strings
		String[] exportStrings = new String[numStrings];
		String[] toAdd = new String[numStrings];
		// For each string, add the initial note name
		for(int i = 0; i < numStrings; i++){
			// Get the integer representing the pitch of the note
			int pitch = tabStrings.get(i).getRootNote();
			
			// Find the name of the pitch to the export string and set it as to adding it
			String s;
			switch(noteNameFormat){
				case 0:
					if(noteNameOctave) s = Music.intToNoteFlat(pitch);
					else s = Music.intToNoteNameFlat(pitch);
					break;
				case 1:
				default:
					if(noteNameOctave) s = Music.intToNoteSharp(pitch);
					else s = Music.intToNoteNameSharp(pitch);
					 break;
			}
			toAdd[i] = s;
			
			// Set the base string for exporting
			exportStrings[i] = "";
		}
		
		// Combine the strings, placing exportStrings after the toAdd strings
		StringUtils.combineStringsWithFiller(exportStrings, toAdd, offset, afterNoteName, noteNameFiller, noteNameFillerBefore);
		
		// Create a list storing each symbol and its position
		ArrayList<IndexAndPos> symbols = new ArrayList<IndexAndPos>();
		for(int i = 0; i < numStrings; i++){
			TabString s = tabStrings.get(i);
			for(TabPosition p : s){
				symbols.add(new IndexAndPos(p, i));
			}
		}
		// Sort all of the symbols in increasing order based on position
		Collections.sort(symbols);
		
		// Add each note one at a time, placing a dash between each one
		// Notes with the same position value are placed at the same character position
		for(int s = 0; s < symbols.size(); s++){
			// Initialize array to store all of the text Strings which will be added for the set of notes
			for(int i = 0; i < numStrings; i++){
				toAdd[i] = "";
			}
			
			// For each note at the same position, add it to the symbols to add
			IndexAndPos stringObj;
			double pos;
			do{
				// Get they symbol to add to the Text String
				stringObj = symbols.get(s);
				// Get the position of that to add
				pos = stringObj.pos.getPos();
				// Set the text to that of the TabSymbol
				toAdd[stringObj.index] = stringObj.pos.getSymbol().getModifiedSymbol(tabStrings.get(stringObj.index));
				// Increment the counter to the next symbol
				s++;
				
				// Continue looping only if the position of the next symbol is the same
			}while(s < symbols.size() && pos == symbols.get(s).pos.getPos());
			
			// Go back one symbol to account for the added symbol
			s--;
			
			// Combine the export text strings with the text for the next tab character
			StringUtils.combineStringsWithFiller(exportStrings, toAdd, symbolBefore, symbolAfter, tabFiller, tabFillerBefore);
		}
		
		// String for the final result of the tab
		String result = "";
		
		// Combine all of the strings together, and add the final vertical bar
		for(int i = 0; i < numStrings; i++){
			result = String.join("", result, exportStrings[i], tabEnd, "\n");
		}
		
		// Return the final tab string
		return result;
	}
	
	/**
	 * Export a {@link Tab} to a file
	 * @param tab The {@link Tab} to export
	 * @param file The {@link File} to export the {@link Tab} to
	 * @return true if the export was successful, false otherwise
	 */
	public static boolean exportToFile(Tab tab, File file){
		if(file == null) return false;
		boolean success = true;
		try{
			// Ensure the parent path exists
			File parent = file.getParentFile();
			if(parent != null && !parent.exists()){
				if(!parent.mkdirs()){
					success = false;
				}
			}
			// If the path was made, or already existed, continue saving
			if(success){
				PrintWriter writer = new PrintWriter(file);
				try{
					String s = export(tab);
					if(s == null) success = false;
					else writer.print(s);
				}finally{
					writer.close();
				}
			}
		}
		catch(FileNotFoundException | SecurityException e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/**
	 * Export a {@link Tab} to a file
	 * @param tab The {@link Tab} to export
	 * @param filePath The path at which to export the file
	 * @param name The name of the file, no extension
	 * @return true if the export was successful, false otherwise
	 */
	public static boolean exportToFile(Tab tab, String filePath, String name){
		File file = new File(FileUtils.makeFileName(filePath, name, "txt"));
		return exportToFile(tab, file);
	}
	
	/** Cannot instantiate {@link TabTextExporter} */
	private TabTextExporter(){}
	
	/**
	 * Helper object for storing both an index, and an associated {@link TabSymbol}
	 * @author zrona
	 */
	public static class IndexAndPos implements Comparable<IndexAndPos>{
		public TabPosition pos;
		public int index;
		public IndexAndPos(TabPosition pos, int index){
			this.pos = pos;
			this.index = index;
		}
		/**
		 * Sort the elements in increasing order of the position of the {@link TabSymbol}
		 */
		@Override
		public int compareTo(IndexAndPos obj){
			double p1 = this.pos.getPos();
			double p2 = obj.pos.getPos();
			if(p1 < p2) return -1;
			return (p1 == p2) ? 0 : 1;
		}
	}
	
}
