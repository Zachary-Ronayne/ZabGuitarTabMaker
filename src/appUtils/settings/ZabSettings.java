package appUtils.settings;

import java.util.ArrayList;

import settings.Setting;
import settings.Settings;

/**
 * An object containing all settings used by the entire Zab application.<br>
 * To create a new setting, go to the appropriate file, or create a new category of settings, and do the following:
 * <ul>
 * 	<li>1. Make a static final for the default value</li>
 * 	<li>2. Define a new instance variable for it, create a getter for both the value and the object, and no setter</li>
 * 	<li>3. In the empty constructor, initialize it with default values using a variation {@link #add(Object, Object)}</li>
 * 	<li>4. Add test cases for new methods</li>
 * </ul>
 * @author zrona
 */
public class ZabSettings extends Settings{
	
	/** The {@link SymbolSettings} used by this {@link ZabSettings} */
	private SymbolSettings symbol;
	/** The {@link TabTextSettings} used by this {@link ZabSettings} */
	private TabTextSettings text;
	/** The {@link TabPaintSettings} used by this {@link ZabSettings} */
	private TabPaintSettings paint;
	/** The {@link TabControlSettings} used by this {@link ZabSettings} */
	private TabControlSettings control;
	/** The {@link TabControlSettings} used by this {@link ZabSettings} */
	private TabSettings tab;
	
	/**
	 * Create a new version of settings with all of the default values loaded
	 */
	public ZabSettings(){
		super();
		// Initialize all settings
		this.symbol = new SymbolSettings();
		this.text = new TabTextSettings();
		this.paint = new TabPaintSettings();
		this.control = new TabControlSettings();
		this.tab = new TabSettings();
		
		// Add them all to this instance of ZabSettings
		ArrayList<Setting<?>> all = this.getAll();
		all.addAll(this.symbol.getAll());
		all.addAll(this.text.getAll());
		all.addAll(this.paint.getAll());
		all.addAll(this.control.getAll());
		all.addAll(this.tab.getAll());
	}
	
	/** @return See {@link #symbol} */
	public SymbolSettings symbol(){
		return this.symbol;
	}

	/** @return See {@link #text} */
	public TabTextSettings text(){
		return this.text;
	}

	/** @return See {@link #paint} */
	public TabPaintSettings paint(){
		return this.paint;
	}

	/** @return See {@link #control} */
	public TabControlSettings control(){
		return this.control;
	}

	/** @return See {@link #tab} */
	public TabSettings tab(){
		return this.tab;
	}
	
	
}
