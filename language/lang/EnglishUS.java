package lang;

/**
 * An implementation of {@link AbstractLanguage} for English in the US
 * @author zrona
 */
public class EnglishUS implements AbstractLanguage{
	
	@Override
	public String file(){ return "File"; }
	@Override
	public String save(){ return "Save"; }
	@Override
	public String load(){ return "Load"; }
	@Override
	public String export(){ return "Export"; }

	@Override
	public String edit(){ return "Edit"; }
	@Override
	public String undo(){ return "Undo"; }
	@Override
	public String redo(){ return "Redo"; }
	
	@Override
	public String graphics(){ return "Graphics"; }
	
	@Override
	public String theme(){ return "Theme"; }
	@Override
	public String darkTheme(){ return "Dark Theme"; }
	@Override
	public String lightTheme(){ return "Light Theme"; }
	
	@Override
	public String zabExtensionDescription(){ return "Zab stringed instrument tablature"; }
	@Override
	public String noPathSelected(){ return "No path selected"; }
	
}
