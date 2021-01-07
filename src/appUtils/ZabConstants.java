package appUtils;

/** A class holding constants for turning on and off certain parts of the app for testing purposes */
public final class ZabConstants{

	/**
	 * Keeping track of if the code should build as normal or as a test build. 
	 * true for a normal build, false for a test build
	 */
	public static final boolean BUILD_NORMAL = true;
	
	/**
	 * True if ZabGui objects should show on creation, false otherwise,
	 * should be false for testing purposes, true for a proper build
	 */
	public static final boolean SHOW_GUI_ON_INIT = BUILD_NORMAL;
	
	/** 
	 * true if the dialog boxes for loading, saving, and exporting should be displayed when choosing a file, false otherwise. 
	 * Should only be false during testing, should be true in any proper build
	 */
	public static final boolean ENABLE_DIALOG = BUILD_NORMAL;

	
	/**
	 * Set to true to print error messages when the app runs into errors while running utility methods. 
	 * Set to false to disable error messages
	 */
	public static final boolean PRINT_ERRORS = false;
	
	/** Cannot instantiate {@link ZabConstants} */
	private ZabConstants(){};
	
}