package util;

/**
 * A class containing methods to interact with {@link String} objects
 * @author zrona
 */
public final class StringUtils{
	
	/**
	 * Take an an array of Strings and combine them so that each string in add is appended to the end of each string in main, and that the strings in add 
	 * are filled with extra characters to be the the same length as the longest string in that list. 
	 * Must ensure that main and add have the same length
	 * @param main The source strings
	 * @param add The strings to add
	 * @param before The text to add before the add strings are added
	 * @param after The text to add after the add strings are added
	 * @param fill The text to fill the length of each add string
	 * @param fillBefore true to add the filler at the beginning of the string, false to add it at the end
	 */
	public static void combineStringsWithFiller(
			String[] main, String[] add,
			String before, String after, 
			char fill, boolean fillBefore){
		
		int size = main.length;
		if(size != add.length) throw new IllegalArgumentException("main and add String arrays must be the same length");
		
		// Find the longest string in add
		int longest = 0;
		for(int i = 0; i < size; i++){
			int length = add[i].length();
			if(longest < length) longest = length;
		}
		
		// Combine add strings with the main strings
		for(int i = 0; i < size; i++){
			main[i] = combineStringWithFiller(main[i], add[i], longest, before, after, fill, fillBefore);
		}
	}
	
	/**
	 * Take a string, main, and combine it with another string, add. The add string will be appended with fill until it reaches length totalLength. 
	 * It is then combined with after and before, and appended to the end of main
	 * @param main The source string
	 * @param add The string to add
	 * @param before The text to add before the add string is added
	 * @param after The text to add after the add string is added
	 * @param fill The text to fill the length of the add string
	 * @param totalLength The length of the add string with its filler, if add.length is larger than this value,
	 * 	it is not reduced, it remains the same length.
	 * @param fillBefore true to add the filler at the beginning of the string, false to add it at the end
	 * @return The final string
	 */
	public static String combineStringWithFiller(
			String main, String add, int totalLength,
			String before, String after,
			char fill, boolean fillBefore){
		
		String addMid = String.valueOf(fill);
		
		// Null checking for initial string
		String s = String.valueOf(add);
		// Ensure the string is the desired length
		while(s.length() < totalLength){
			if(fillBefore) s = addMid.concat(s);
			else s = s.concat(addMid);
		}
		add = s;
		
		// Combine add string with the main string, as well as the before and after
		return String.join("", main, before, add, after);
	}
	
	/** Cannot instantiate {@link StringUtils} */
	private StringUtils(){}
}
