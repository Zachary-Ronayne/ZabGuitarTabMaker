package lang;

import appMain.gui.ZabTheme;

/**
 * An interface used to return all text values as displayed to a user for compatibility with other spoken languages. 
 * This simply defines a method for each piece of text which gets displayed to the user. 
 * @author zrona
 */
public interface AbstractLanguage{
	
	/** Get the text for displaying a title label which means a file, a directory in a computer */
	public String file();
	/** Get the text for displaying a title label which means to save something */
	public String save();
	/** Get the text for displaying a title label which means to load something */
	public String load();
	/** Get the text for displaying a title label which means to export something */
	public String export();
	
	/** Get the text for displaying a title label which means to edit something */
	public String edit();
	/** Get the text for displaying a title label which means to undo an action */
	public String undo();
	/** Get the text for displaying a title label which means to redo an action */
	public String redo();
	
	/** Get the text for displaying a title label which means graphics related settings */
	public String graphics();
	
	/** Get the text for displaying a title label which means refers to a {@link ZabTheme}, or simply a theme */
	public String theme();
	/** Get the text for displaying a title label which means refers to the default dark {@link ZabTheme} */
	public String darkTheme();
	/** Get the text for displaying a title label which means refers to the default light {@link ZabTheme} */
	public String lightTheme();
	
	/** Get the text for describing the file type .zab, which is the file extension for saved, not exported, versions of tabs */
	public String zabExtensionDescription();
	/** Get the text for describing that a file path needs to be selected, and one has not yet been selected */
	public String noPathSelected();
	
}