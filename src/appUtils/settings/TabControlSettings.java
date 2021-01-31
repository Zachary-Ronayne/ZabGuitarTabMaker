package appUtils.settings;

import settings.SettingBoolean;
import settings.SettingDouble;
import settings.Settings;
import tab.TabPosition;

/**
 * A {@link Settings} object which contains all of the information related to controlling a tab in the main gui
 * @author zrona
 */
public class TabControlSettings extends Settings{
	/** Default for {@link #moveDeleteInvalid} */
	public static final boolean MOVE_DELETE_INVALID = false;
	/** Default for {@link #moveCancelInvalid} */
	public static final boolean MOVE_CANCEL_INVALID = false;
	/** Default for {@link #zoomFactor} */
	public static final double ZOOM_FACTOR = 0.1;
	/** Default for {@link #zoomInverted} */
	public static final boolean ZOOM_INVERTED = true;
	/** Default for {@link #scrollFactor} */
	public static final double SCROLL_FACTOR = 40;
	/** Default for {@link #scrollXInverted} */
	public static final boolean SCROLL_X_INVERTED = false;
	/** Default for {@link #scrollYInverted} */
	public static final boolean SCROLL_Y_INVERTED = false;
	
	/**
	 * true if, when moving a {@link TabPosition} selection, 
	 * they should be deleted if they are in an invalid position, 
	 * false if they should remain unmoved 
	 */
	private SettingBoolean moveDeleteInvalid;
	/**
	 * true if, when moving a {@link TabPosition} selection, 
	 * the entire move should be canceled if any {@link TabPosition} objects couldn't be moved. 
	 * False to determine behavior for individual notes based on {@link #moveDeleteInvalid}
	 */
	private SettingBoolean moveCancelInvalid;
	/** The value which determines how fast the camera zooms */
	private SettingDouble zoomFactor;
	/** true if zooming should be inverted, i.e. moving the mouse wheel towards the user should zoom out, false otherwise */
	private SettingBoolean zoomInverted;
	/** The value that determines how quickly scrolling happens. Larger values mean scrolling faster, lower values mean scrolling slower */
	private SettingDouble scrollFactor;
	/** true to reverse the direction of scrolling on the x axis, false otherwise */
	private SettingBoolean scrollXInverted;
	/** true to reverse the direction of scrolling on the y axis, false otherwise */
	private SettingBoolean scrollYInverted;

	/**
	 * Create a new set of {@link TabControlSettings} with all default values loaded
	 */
	public TabControlSettings(){
		super();
		this.moveDeleteInvalid = this.addBoolean(MOVE_DELETE_INVALID);
		this.moveCancelInvalid = this.addBoolean(MOVE_CANCEL_INVALID);
		this.zoomFactor = this.addDouble(ZOOM_FACTOR);
		this.zoomInverted = this.addBoolean(ZOOM_INVERTED);
		this.scrollFactor = this.addDouble(SCROLL_FACTOR);
		this.scrollXInverted = this.addBoolean(SCROLL_X_INVERTED);
		this.scrollYInverted = this.addBoolean(SCROLL_Y_INVERTED);
	}
	
	/** @return See {@link #moveDeleteInvalid} */
	public SettingBoolean getMoveDeleteInvalid(){ return this.moveDeleteInvalid; }
	/** @return See {@link #moveCancelInvalid} */
	public SettingBoolean getMoveCancelInvalid(){ return this.moveCancelInvalid; }
	/** @return See {@link #zoomFactor} */
	public SettingDouble getZoomFactor(){ return this.zoomFactor; }
	/** @return See {@link #zoomInverted} */
	public SettingBoolean getZoomInverted(){ return this.zoomInverted; }
	/** @return See {@link #scrollFactor} */
	public SettingDouble getScrollFactor(){ return this.scrollFactor; }
	/** @return See {@link #scrollXInverted} */
	public SettingBoolean getScrollXInverted(){ return this.scrollXInverted; }
	/** @return See {@link #scrollYInverted} */
	public SettingBoolean getScrollYInverted(){ return this.scrollYInverted; }
	/** @return See {@link #moveDeleteInvalid} */
	
	public Boolean moveDeleteInvalid(){ return this.getMoveDeleteInvalid().get(); }
	/** @return See {@link #moveCancelInvalid} */
	public Boolean cancelInvalid(){ return this.getMoveCancelInvalid().get(); }
	/** @return See {@link #zoomFactor} */
	public Double zoomFactor(){ return this.getZoomFactor().get(); }
	/** @return See {@link #zoomInverted} */
	public Boolean zoomInverted(){ return this.getZoomInverted().get(); }
	/** @return See {@link #scrollFactor} */
	public Double scrollFactor(){ return this.getScrollFactor().get(); }
	/** @return See {@link #scrollXInverted} */
	public Boolean scrollXInverted(){ return this.getScrollXInverted().get(); }
	/** @return See {@link #scrollYInverted} */
	public Boolean scrollYInverted(){ return this.getScrollYInverted().get(); }
	
}
