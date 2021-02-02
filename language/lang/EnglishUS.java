package lang;

/**
 * An implementation of {@link AbstractLanguage} for English in the US
 * @author zrona
 */
public class EnglishUS implements AbstractLanguage{

	@Override
	public String appName(){ return "Zab Guitar Tab Editor"; }
	@Override
	public String file(){ return "File"; }
	@Override
	public String newFile(){ return "New"; }
	@Override
	public String save(){ return "Save"; }
	@Override
	public String saveAs(){ return "Save As"; }
	@Override
	public String saveFail(){ return this.save().concat(" failed"); }
	@Override
	public String saveSuccess(){ return this.save().concat(" successful"); }
	@Override
	public String load(){ return "Load"; }
	@Override
	public String loadFail(){ return this.load().concat(" failed"); }
	@Override
	public String loadSuccess(){ return this.load().concat(" successful"); }
	@Override
	public String export(){ return "Export"; }
	@Override
	public String exportFail(){ return this.export().concat(" failed"); }
	@Override
	public String exportSuccess(){ return this.export().concat(" successful"); }

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
