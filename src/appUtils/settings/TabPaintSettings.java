package appUtils.settings;

import appMain.gui.util.Camera;
import settings.SettingDouble;
import settings.SettingInt;
import settings.Settings;
import tab.Tab;
import tab.TabPosition;
import tab.TabString;

/**
 * A {@link Settings} object which contains all of the information related to painting a tab with graphics
 * @author zrona
 */
public class TabPaintSettings extends Settings{

	/** Default for {@link #baseX} */
	public static final double BASE_X = 100;
	/** Default for {@link #baseY} */
	public static final double BASE_Y = -100;
	/** Default for {@link #measureWidth} */
	public static final double MEASURE_WIDTH = 400;
	/** Default for {@link #lineMeasures} */
	public static final int LINE_MEASURES = 4;
	/** Default for {@link #stringSpace} */
	public static final double STRING_SPACE = 30;
	/** Default for {@link #selectionBuffer} */
	public static final double SELECTION_BUFFER = 15;
	/** Default for {@link #aboveSpace} */
	public static final double ABOVE_SPACE = 40;
	/** Default for {@link #belowSpace} */
	public static final double BELOW_SPACE = 40;
	/** Default for {@link #symbolScaleMode} */
	public static final int SYMBOL_SCALE_MODE = Camera.STRING_SCALE_Y_AXIS;
	/** Default for {@link #symbolXAlign} */
	public static final int SYMBOL_X_ALIGN = Camera.STRING_ALIGN_CENTER;
	/** Default for {@link #symbolYAlign} */
	public static final int SYMBOL_Y_ALIGN = Camera.STRING_ALIGN_CENTER;
	/** Default for {@link #stringLabelScaleMode} */
	public static final int STRING_LABEL_SCALE_MODE = Camera.STRING_SCALE_Y_AXIS;
	/** Default for {@link #stringLabelXAlign} */
	public static final int STRING_LABEL_X_ALIGN = Camera.STRING_ALIGN_MAX;
	/** Default for {@link #stringLabelYAlign} */
	public static final int STRING_LABEL_Y_ALIGN = Camera.STRING_ALIGN_CENTER;
	/** Default for {@link #symbolBorderSize} */
	public static final double SYMBOL_BORDER_SIZE = 2;
	/** Default for {@link #stringLabelSpace} */
	public static final double STRING_LABEL_SPACE = 14;
	/** Default for {@link #maxUndo} */
	public static final int MAX_UNDO = 500;
	
	/** The base x coordinate at which the painted {@link Tab} of a {@link TabPainter} is rendered */
	private SettingDouble baseX;
	/** The base y coordinate at which the painted {@link Tab} of a {@link TabPainter} is rendered */
	private SettingDouble baseY;
	/** The base size of a rendered measure of a {@link TabPainter}, in pixels */
	private SettingDouble measureWidth;
	/** The number of measures a {@link TabPainter} draws per line */
	private SettingInt lineMeasures;
	/** The amount of space between painted strings in a {@link TabPainter}, in pixels */
	private SettingDouble stringSpace;
	/** The amount of space above and below a tab where a coordinate can still be detected in a {@link TabPainter}, in pixels */
	private SettingDouble selectionBuffer;
	/** The amount of space above each line of tab in a {@link TabPainter}, in pixels */
	private SettingDouble aboveSpace;
	/** The amount of space below each line of tab in a {@link TabPainter}, in pixels */
	private SettingDouble belowSpace;
	/** The {@link Camera} scale mode to use when scaling the text of {@link TabPosition} symbols */
	private SettingInt symbolScaleMode;
	/** The {@link Camera} alignment mode to use for the x axis of the text of {@link TabPosition} symbols */
	private SettingInt symbolXAlign;
	/** The {@link Camera} alignment mode to use for the y axis of the text of {@link TabPosition} symbols */
	private SettingInt symbolYAlign;
	/** The {@link Camera} scale mode to use when scaling the text of the pitch labels of drawn {@link TabString} objects */
	private SettingInt stringLabelScaleMode;
	/** The {@link Camera} alignment mode to use for the x axis of the text of text of the pitch labels of drawn {@link TabString} objects */
	private SettingInt stringLabelXAlign;
	/** The {@link Camera} alignment mode to use for the y axis of the text of text of the pitch labels of drawn {@link TabString} objects */
	private SettingInt stringLabelYAlign;
	/** The extra amount of space added to the bounds of a rendered {@link TabPosition} when it has a highlight, in pixels. This pixel amount is the same regardless of zoom level */
	private SettingDouble symbolBorderSize;
	/** The extra amount of space added between the pitch labels of rendered {@link TabString} objects, in pixels. This pixel amount is the same regardless of zoom level */
	private SettingDouble stringLabelSpace;
	/** The maximum number of undo events which can be stored in the undo stack, use 0 to disable, use a negative value for unlimited */
	private SettingInt maxUndo;

	/**
	 * Create a new set of {@link TabPaintSettings} with all default values loaded
	 */
	public TabPaintSettings(){
		super();
		this.baseX = this.addDouble(BASE_X);
		this.baseY = this.addDouble(BASE_Y);
		this.measureWidth = this.addDouble(MEASURE_WIDTH);
		this.lineMeasures = this.addInt(LINE_MEASURES);
		this.stringSpace = this.addDouble(STRING_SPACE);
		this.selectionBuffer = this.addDouble(SELECTION_BUFFER);
		this.aboveSpace = this.addDouble(ABOVE_SPACE);
		this.belowSpace = this.addDouble(BELOW_SPACE);
		this.symbolScaleMode = this.addInt(SYMBOL_SCALE_MODE);
		this.symbolXAlign = this.addInt(SYMBOL_X_ALIGN);
		this.symbolYAlign = this.addInt(SYMBOL_Y_ALIGN);
		this.stringLabelScaleMode = this.addInt(STRING_LABEL_SCALE_MODE);
		this.stringLabelXAlign = this.addInt(STRING_LABEL_X_ALIGN);
		this.stringLabelYAlign = this.addInt(STRING_LABEL_Y_ALIGN);
		this.symbolBorderSize = this.addDouble(SYMBOL_BORDER_SIZE);
		this.stringLabelSpace = this.addDouble(STRING_LABEL_SPACE);
		this.maxUndo = this.addInt(MAX_UNDO);
	}

	/** @return See {@link #baseX} */
	public SettingDouble getBaseX(){ return this.baseX; }
	/** @return See {@link #baseY} */
	public SettingDouble getBaseY(){ return this.baseY; }
	/** @return See {@link #measureWidth} */
	public SettingDouble getMeasureWidth(){ return this.measureWidth; }
	/** @return See {@link #lineMeasures} */
	public SettingInt getLineMeasures(){ return this.lineMeasures; }
	/** @return See {@link #stringSpace} */
	public SettingDouble getStringSpace(){ return this.stringSpace; }
	/** @return See {@link #selectionBuffer} */
	public SettingDouble getSelectionBuffer(){ return this.selectionBuffer; }
	/** @return See {@link #aboveSpace} */
	public SettingDouble getAboveSpace(){ return this.aboveSpace; }
	/** @return See {@link #belowSpace} */
	public SettingDouble getBelowSpace(){ return this.belowSpace; }
	/** @return See {@link #symbolScaleMode} */
	public SettingInt getSymbolScaleMode(){ return this.symbolScaleMode; }
	/** @return See {@link #symbolXAlign} */
	public SettingInt getSymbolXAlign(){ return this.symbolXAlign; }
	/** @return See {@link #symbolYAlign} */
	public SettingInt getSymbolYAlign(){ return this.symbolYAlign; }
	/** @return See {@link #stringLabelScaleMode} */
	public SettingInt getStringLabelScaleMode(){ return this.stringLabelScaleMode; }
	/** @return See {@link #stringLabelXAlign} */
	public SettingInt getStringLabelXAlign(){ return this.stringLabelXAlign; }
	/** @return See {@link #stringLabelYAlign} */
	public SettingInt getStringLabelYAlign(){ return this.stringLabelYAlign; }
	/** @return See {@link #symbolBorderSize} */
	public SettingDouble getSymbolBorderSize(){ return this.symbolBorderSize; }
	/** @return See {@link #stringLabelSpace} */
	public SettingDouble getStringLabelSpace(){ return this.stringLabelSpace; }
	/** @return See {@link #maxUndo} */
	public SettingInt getMaxUndo(){ return this.maxUndo; }

	/** @return See {@link #baseX} */
	public Double baseX(){ return this.getBaseX().get(); }
	/** @return See {@link #baseY} */
	public Double baseY(){ return this.getBaseY().get(); }
	/** @return See {@link #measureWidth} */
	public Double measureWidth(){ return this.getMeasureWidth().get(); }
	/** @return See {@link #lineMeasures} */
	public Integer lineMeasures(){ return this.getLineMeasures().get(); }
	/** @return See {@link #stringSpace} */
	public Double stringSpace(){ return this.getStringSpace().get(); }
	/** @return See {@link #selectionBuffer} */
	public Double selectionBuffer(){ return this.getSelectionBuffer().get(); }
	/** @return See {@link #aboveSpace} */
	public Double aboveSpace(){ return this.getAboveSpace().get(); }
	/** @return See {@link #belowSpace} */
	public Double belowSpace(){ return this.getBelowSpace().get(); }
	/** @return See {@link #symbolScaleMode} */
	public Integer symbolScaleMode(){ return this.getSymbolScaleMode().get(); }
	/** @return See {@link #symbolXAlign} */
	public Integer symbolXAlign(){ return this.getSymbolXAlign().get(); }
	/** @return See {@link #symbolYAlign} */
	public Integer symbolYAlign(){ return this.getSymbolYAlign().get(); }
	/** @return See {@link #stringLabelScaleMode} */
	public Integer stringLabelScaleMode(){ return this.getStringLabelScaleMode().get(); }
	/** @return See {@link #stringLabelXAlign} */
	public Integer stringLabelXAlign(){ return this.getStringLabelXAlign().get(); }
	/** @return See {@link #stringLabelYAlign} */
	public Integer stringLabelYAlign(){ return this.getStringLabelYAlign().get(); }
	/** @return See {@link #symbolBorderSize} */
	public Double symbolBorderSize(){ return this.getSymbolBorderSize().get(); }
	/** @return See {@link #stringLabelSpace} */
	public Double stringLabelSpace(){ return this.getStringLabelSpace().get(); }
	/** @return See {@link #maxUndo} */
	public Integer maxUndo(){ return this.getMaxUndo().get(); }
}
