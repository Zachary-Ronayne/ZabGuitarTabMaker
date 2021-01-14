package lang;

/**
 * This class holds a reference to one particular {@link AbstractLanguage} object, which can be used as a static instance. 
 * @author zrona
 */
public class Language{
	
	/** The static instance used for languages */
	private static AbstractLanguage language;
	
	/**
	 * Initialize the static {@link AbstractLanguage} instance. Currently this only goes to English, 
	 * but allows for easy modification to a new language if one is made 
	 */
	public static void init(){
		language = new EnglishUS();
	}
	
	/** @return The static {@link AbstractLanguage} instance */
	public static AbstractLanguage get(){
		return language;
	}
	
	/** Cannot instantiate {@link Language} */
	private Language(){};
	
}
