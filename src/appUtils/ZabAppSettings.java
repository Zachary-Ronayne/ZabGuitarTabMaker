package appUtils;

/**
 * A class holding an instance of {@link ZabSettings}. for the application using {@link ZabSettings}<br>
 * Must call {@link #init()} before using any settings in this class
 * @author zrona
 */
public class ZabAppSettings{
	
	/** The settings used */
	private static ZabSettings settings;
	
	/**
	 * Initialize the settings to a default state
	 */
	public static void init(){
		settings = new ZabSettings();
	}
	
	/**
	 * Get the instance of settings
	 * @return The instance
	 */
	public static ZabSettings get(){
		return settings;
	}
	
	/** Cannot instantiate {@link ZabAppSettings} */
	private ZabAppSettings(){}
	
}
