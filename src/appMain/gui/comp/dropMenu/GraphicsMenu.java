package appMain.gui.comp.dropMenu;

import appMain.gui.ZabGui;

/**
 * The {@link ZabMenu} in {@link ZabMenuBar} handling graphical related items
 * @author zrona
 */
public class GraphicsMenu extends ZabMenu{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabMenu} used to keep track of the theme options */
	private ThemeMenu themeSubMenu;

	/**
	 * Create a new default {@link GraphicsMenu}
	 * @param gui See {@link ZabMenu#gui}
	 */
	public GraphicsMenu(ZabGui gui){
		super("Graphics", gui);
		
		this.themeSubMenu = new ThemeMenu(this.getGui());
		this.add(themeSubMenu);
	}
	
	/** See {@link #themeSubMenu} */
	public ThemeMenu getThemeSubMenu(){
		return this.themeSubMenu;
	}

}
