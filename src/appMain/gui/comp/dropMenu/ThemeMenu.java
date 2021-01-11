package appMain.gui.comp.dropMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appUtils.ZabAppSettings;

/**
 * The {@link ZabMenu} in {@link ZabMenuBar} handling theme related items. This is designed as a sub menu of {@link GraphicsMenu}
 * @author zrona
 */
public class ThemeMenu extends ZabMenu{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabMenuItem} which handles setting the theme to the dark theme */
	private ZabMenuItem darkThemeItem;
	/** The {@link ActionListener} used by {@link #darkThemeItem} to set the theme to dark */
	private DarkThemeListener darkSetter;
	
	/** The {@link ZabMenuItem} which handles setting the theme to the light theme */
	private ZabMenuItem lightThemeItem;
	/** The {@link ActionListener} used by {@link #lightThemeItem} to set the theme to light */
	private LightThemeListener lightSetter;
	
	/**
	 * Create a new default {@link ThemeMenu}
	 * @param gui See {@link ZabMenu#gui}
	 */
	public ThemeMenu(ZabGui gui){
		super("Theme", gui);
		
		this.darkThemeItem = new ZabMenuItem("Dark Theme");
		this.darkSetter = new DarkThemeListener();
		this.darkThemeItem.addActionListener(this.darkSetter);
		this.add(this.darkThemeItem);
		
		this.lightThemeItem = new ZabMenuItem("Light Theme");
		this.lightSetter = new LightThemeListener();
		this.lightThemeItem.addActionListener(this.lightSetter);
		this.add(this.lightThemeItem);
	}
	
	/** @return See {@link #darkThemeItem} */
	public ZabMenuItem getDarkThemeItem(){
		return this.darkThemeItem;
	}
	/** @return See {@link #darkSetter} */
	public DarkThemeListener getDarkSetter(){
		return this.darkSetter;
	}
	
	/** @return See {@link #lightThemeItem} */
	public ZabMenuItem getLightThemeItem(){
		return this.lightThemeItem;
	}
	/** @return See {@link #lightSetter} */
	public LightThemeListener getLightSetter(){
		return this.lightSetter;
	}
	
	/**
	 * An {@link ActionListener} which sets the theme of the Zab application to the dark theme 
	 * @author zrona
	 */
	public class DarkThemeListener implements ActionListener{
		/***/
		@Override
		public void actionPerformed(ActionEvent e){
			ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), getGui(), true);
		}
	}
	
	/**
	 * An {@link ActionListener} which sets the theme of the Zab application to the light theme 
	 * @author zrona
	 */
	public class LightThemeListener implements ActionListener{
		/***/
		@Override
		public void actionPerformed(ActionEvent e){
			ZabAppSettings.setTheme(new ZabTheme.LightTheme(), getGui(), true);
		}
	}

}
