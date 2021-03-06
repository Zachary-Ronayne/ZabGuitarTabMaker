package tab;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import appUtils.ZabAppSettings;
import appUtils.ZabConstants;
import appUtils.settings.TabTextSettings;
import appUtils.settings.ZabSettings;
import gui.FileUtilsUntested;
import music.Music;
import tab.symbol.TabSymbol;
import util.FileUtils;
import util.StringUtils;

/**
 * A utility class containing methods to convert a {@link Tab} into a String
 * @author zrona
 */
public final class TabTextExporter{
	
	/** The number used for representing displaying the note names of a tab as all flat notes, no sharp notes */
	public static final int NOTE_FORMAT_ALL_FLAT = 0;
	/** The number used for representing displaying the note names of a tab as all sharp notes, no flat notes */
	public static final int NOTE_FORMAT_ALL_SHARP = 1;
	
	/**
	 * Export a tab to a text String using all of the values defined in the {@link ZabSettings} of {@link ZabAppSettings#get()}
	 * @param tab The {@link Tab} to export
	 * @return The exported String, or null if an issue occurred with exporting. Should be viewed with a monospace font.
	 */
	public static String export(Tab tab){
		if(tab == null) return null;
		
		TabTextSettings settings = ZabAppSettings.get().text();
		int measuresPerLine = settings.measuresPerLine();
		
		// String for the final result of the tab
		String result = "";
		
		// Generate each line of tab
		double length = tab.length();
		double checkLength = length;
		for(int i = 0; i <= checkLength; i += measuresPerLine){
			// Add the line
			result = result.concat(exportLine(tab, i, i + measuresPerLine, false));
			// Add an extra new line if this is not the last line
			if(i + measuresPerLine < checkLength) result = result.concat("\n");
		}
		
		// Return the final tab string
		return result;
	}
	
	/**
	 * Export, to a String, all of the symbols in the specified area to a single line of tab
	 * @param tab The tab to export
	 * @param start The position, in measures, to begin the region of export
	 * @param end The position, in measures, to end the region of export
	 * @param hardEnd true to include notes lining up with the exact end of the range, false otherwise
	 * @return The exported line of tab, or null if tab is null
	 */
	public static String exportLine(Tab tab, double start, double end, boolean hardEnd){
		if(tab == null) return null;
		
		TabTextSettings settings = ZabAppSettings.get().text();
		
		// The text string added before each tab string
		String offset = settings.preString();
		// The text placed directly after the note name
		String afterNoteName = settings.postNoteName();
		// The character used to fill the space between note names of differing length
		char noteNameFiller = settings.noteNameFiller();
		// Whether or not the note name filler should come before or after the name
		boolean noteNameFillerBefore = settings.noteNameAlignEnd();
		// Whether or not to include the octave number for each number
		boolean noteNameOctave = settings.noteNameOctave();
		// The way to display the text notes for flat/sharp notes, 0 for all flats, 1 for all sharps.
		// This is an integer to keep it open to other formats, i.e. mix of flat and sharps, including double sharps, using E# instead of F
		int noteNameFormat = settings.noteNameFormat();
		// The string to place at the end of every tabString
		String tabEnd = settings.textEnd();
		// true if the amount of space between each note in a measure should be used when exporting the text, 
		// false to ignore space and pack all notes together as much as possible
		boolean useSpacing = settings.useSpacing();
		
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
				case NOTE_FORMAT_ALL_FLAT:
				case NOTE_FORMAT_ALL_SHARP:
				default:
					s = Music.intToNote(pitch, noteNameFormat == NOTE_FORMAT_ALL_FLAT, noteNameOctave);
					break;
			}
			toAdd[i] = s;
			
			// Set the base string for exporting
			exportStrings[i] = "";
		}
		
		// Combine the strings, placing exportStrings after the toAdd strings
		StringUtils.combineStringsWithFiller(exportStrings, toAdd, offset, afterNoteName, noteNameFiller, noteNameFillerBefore);
		
		// Add all of the symbols with no extra space between them
		if(useSpacing) appendSymbolsWithSpace(exportStrings, start, end, tabStrings, hardEnd);
		// Add all of the symbols with extra space based on their positions
		else appendSymbolsNoSpace(exportStrings, start, end, tabStrings, hardEnd);
		
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
	 * Given an array of of strings, add the text of all of the given symbols, so that all of the symbols are spaced based on their relative positions.
	 * @param exportStrings The strings to use initially, must be the same size as tabStrings
	 * @param start The starting note position in the TabStrings, symbols with a lower position value will not be included 
	 * @param end The ending note position in the TabStrings, symbols with a higher position value will not be included 
	 * @param tabStrings The strings which contain the symbols to place
	 * 	Must have the same number of elements as exportStrings
	 * @param hardEnd true to include notes lining up with the exact end of the range, false otherwise
	 */
	public static void appendSymbolsNoSpace(String[] exportStrings, double start, double end, ArrayList<TabString> tabStrings, boolean hardEnd){
		TabTextSettings settings = ZabAppSettings.get().text();
		// The text added before each symbol is added
		String symbolBefore = settings.beforeSymbol();
		// The text added after each symbol is added
		String symbolAfter = settings.afterSymbol();
		// The character used to fill up the tab
		char tabFiller = settings.filler();
		// Whether the tab filler should be placed before or after the symbols
		boolean tabFillerBefore = settings.alignSymbolsEnd();
		
		int numStrings = tabStrings.size();

		// Create a list storing each symbol and its position in the region
		ArrayList<IndexAndPos> symbols = new ArrayList<IndexAndPos>();
		for(int i = 0; i < numStrings; i++){
			TabString s = tabStrings.get(i);
			
			// Find the indexes of the positions
			int startI = s.findIndex(start);
			int endI = s.findIndex(end);
			
			for(int j = startI; j <= endI; j++){
				// If the index would insert a note outside of the string, don't add it, 
				//	otherwise this is needed to keep notes in the middle of a line being added, if it is at the exact edge of the bounds
				if(j >= s.size()) continue;
				// If an index is reached which is greater than the end point, then the end of the string has been reached
				double p = s.get(j).getPos();
				if(hardEnd && p > end || !hardEnd && p >= end) break;
				symbols.add(new IndexAndPos(s.get(j), i));
			}
		}
		// Sort all of the symbols in increasing order based on position
		Collections.sort(symbols);
		
		String[] toAdd = new String[numStrings];
		
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
	}

	/**
	 * Add on to the end of exportStrings, all of the symbols in the given range of measures.
	 * @param exportStrings The text Strings to add the text
	 * @param lowPos The beginning point to start looking for the symbols, any symbol with a position less than this value will not be included
	 * @param highPos The ending point for looking for the symbols, any symbol with a position less greater than this value will not be included
	 * @param tabStrings The list of {@link TabString} objects which contain the symbols at the desired export positions. 
	 * 	Must have the same number of elements as exportStrings
	 * @param hardEnd true to include notes lining up with the exact end of the range, false otherwise
	 */
	public static void appendSymbolsWithSpace(String[] exportStrings, double lowPos, double highPos, ArrayList<TabString> tabStrings, boolean hardEnd){
		TabTextSettings settings = ZabAppSettings.get().text();
		// The text added before each symbol is added
		String symbolBefore = settings.beforeSymbol();
		// The text added after each symbol is added
		String symbolAfter = settings.afterSymbol();
		// The character used to fill up the tab
		char tabFiller = settings.filler();
		// Whether the tab filler should be placed before or after the symbols
		boolean tabFillerBefore = settings.alignSymbolsEnd();
		// the text used to represent a measure line
		String measureSeparator = settings.measureSeparator();
		
		int numStrings = tabStrings.size();
		String[] toAdd = new String[numStrings];
		
		// Find the maximum space between each symbol, in measures
		double measureSpace = 1 / ZabAppSettings.get().tab().quantizeDivisor();
		
		// Go through each space in the tab, adding notes if they are found, or an empty space if they are not
		for(double p = lowPos; p < highPos || hardEnd && p == highPos; p += measureSpace){
			
			// Find the symbol at each position, and place it in the list, or an empty string if there is no symbol
			for(int i = 0; i < numStrings; i++){
				TabString str = tabStrings.get(i);
				TabPosition pos = str.findPosition(p);
				toAdd[i] = (pos == null) ? "" : pos.getSymbol().getModifiedSymbol(str);
			}
			
			// Combine the export text strings with the text for the next tab character
			StringUtils.combineStringsWithFiller(exportStrings, toAdd, symbolBefore, symbolAfter, tabFiller, tabFillerBefore);
			
			// If the next space will be a measure, add the measure separator to each line
			// Mod a double by 1 to get its decimal remainder
			double measurePos = p % 1 + measureSpace;
			// If the next position will be on or after a measure, and it is not the end of the measure, add the line
			if(measurePos >= 1 && p + measureSpace != highPos){
				for(int i = 0; i < numStrings; i++) exportStrings[i] = exportStrings[i].concat(measureSeparator);
			}
		}
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
		// Ensure the parent path exists
		FileUtilsUntested.ensureParentExists(file);
		
		// If the path was made, or already existed, continue saving
		// Make the export string. If it fails to make, then don't write anything to a file
		String s = export(tab);
		if(s == null){
			success = false;
			if(ZabConstants.PRINT_ERRORS) System.err.println("Export of a tab failed");
		}
		// Otherwise write it to the file
		else{
			PrintWriter writer = FileUtilsUntested.createFilePrintWriter(file);
			writer.print(s);
			writer.close();
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
	 * Helper object for storing both an index in a list of Strings, and an associated {@link TabSymbol}
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
