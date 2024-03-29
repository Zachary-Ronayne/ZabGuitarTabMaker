package appMain.gui.editor.paint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appMain.gui.comp.ZabPanel;
import appMain.gui.editor.paint.event.EditorEventStack;
import appMain.gui.editor.paint.event.PlaceNotesEvent;
import appMain.gui.editor.paint.event.RemoveNotesEvent;
import appMain.gui.editor.paint.event.RemovePlaceNotesEvent;
import appMain.gui.editor.paint.event.SelectedTabNumberEvent;
import appMain.gui.util.Camera;
import appUtils.ZabAppSettings;
import appUtils.settings.TabPaintSettings;
import appUtils.settings.TabSettings;
import tab.Tab;
import tab.TabFactory;
import tab.TabPosition;
import tab.TabString;
import tab.symbol.TabDeadNote;
import tab.symbol.TabModifier;
import tab.symbol.TabPitch;
import tab.symbol.TabSymbol;
import util.ObjectUtils;

/**
 * A class used to handle drawing a {@link Tab} with a graphics object. Additionally, handles storing objects for input and output.
 * @author zrona
 */
public class TabPainter extends ZabPanel{
	private static final long serialVersionUID = 1L;
	
	/** The font of symbols drawn for a tab */
	public static final Font SYMBOL_FONT = new Font("Arial", Font.PLAIN, 20);
	/** The font stroke used for strings drawn for a tab */
	public static final Stroke STRING_LINE_WEIGHT = new BasicStroke(3);
	
	/** The {@link ZabGui} which uses this {@link TabPainter} */
	private ZabGui gui;
	
	/** The camera used to control drawing the graphics with the {@link #tabScreen} */
	private Camera tabCamera;
	
	/** The width, in pixels, of the paintable area */
	private int paintWidth;
	/** The height, in pixels, of the paintable area */
	private int paintHeight;
	
	/** The {@link Tab} used for painting, can be null */
	private Tab tab;
	
	/** The {@link FontMetrics} used the last time the symbols were painted, will be null until this {@link TabPainter} is painted at least once */
	private FontMetrics lastSymbolFont;
	
	/** The number of lines of tab to display */
	private int tabLineCount;
	
	/** A list containing {@link Selection} objects for every user selected {@link TabPosition} */
	private SelectionList selected;
	
	/** A copy of the {@link Selection} which was last selected by the user, i.e. the most recent {@link TabSymbol} they clicked on, null if no selection exists */
	private Selection lastSelected;
	
	/** The {@link TabPosition} which the user is currently hovering, and should have an extra highlight. Can be null none is hovered */
	private TabPosition hoveredPosition;
	
	/** The tab number which will be applied to the selected symbols, null if not set */
	private Integer selectedNewTabNum;
	
	/** The {@link SelectionBox} currently used by this {@link TabPainter} to track selecting notes via a click and drag */
	private SelectionBox selectionBox;
	
	/** The {@link SelectionDragger} used by this {@link TabPainter} to track moving selected notes around */
	private SelectionDragger dragger;
	
	/** The {@link SelectionCopyPaster} used by this {@link TabPainter} to track copy and pasting notes */
	private SelectionCopyPaster copyPaster;
	
	/**
	 * The stack keeping track of all actions which the user applied to the painter. 
	 * This also tracks if the painter has been saved recently
	 */
	private EditorEventStack undoStack;
	
	/** The {@link MouseAdapter} containing the functionality for mouse input in this {@link TabPainter} for the editor */
	private EditorMouse mouseControl;

	/** The {@link KeyAdapter} containing the functionality for key input in this {@link TabPainter} for the editor */
	private EditorKeyboard keyControl;
	
	/**
	 * Create a new {@link TabPainter} at the given size
	 * @param gui See {@link #gui)}
	 * @param width See {@link #width}
	 * @param height See {@link #height}
	 * @param tab See {@link #tab}
	 */
	public TabPainter(ZabGui gui, int width, int height, Tab tab){
		super();
		this.gui = gui;
		
		// Set up tab and camera
		this.setTab(tab);
		this.tabCamera = new Camera(width, height);
		this.tabCamera.setDrawOnlyInBounds(false);
		this.setPaintSize(width, height);
		
		// Set up objects for controlling selection
		this.selected = new SelectionList();
		this.lastSelected = null;
		this.selectedNewTabNum = null;
		this.selectionBox = new SelectionBox(this);
		this.dragger = new SelectionDragger(this);
		this.copyPaster = new SelectionCopyPaster(this);
		this.lastSymbolFont = null;
		this.hoveredPosition = null;
		
		// Set up undo/redo stack
		this.undoStack = new EditorEventStack(this);
		
		// Add the mouse input to the panel
		this.mouseControl = new EditorMouse(this);
		this.addMouseListener(mouseControl);
		this.addMouseMotionListener(mouseControl);
		this.addMouseWheelListener(mouseControl);
		
		// Create the key listener
		this.keyControl = new EditorKeyboard(this);
		this.addKeyListener(keyControl);
		
		// Ensure this JPanel has focus
		this.requestFocusInWindow();
		
		// Initialize the state of the number of tab lines to draw
		this.updateLineTabCount();
		
		// Update the camera position at the end
		this.resetCamera();
		
		// Final repaint to ensure the panel is updated
		this.repaint();
	}
	
	/** @return See {@link #gui} */
	public ZabGui getGui(){
		return this.gui;
	}
	
	/**
	 * Get the width of this {@link TabPainter}
	 * @return See {@link #width}
	 */
	public int getPaintWidth(){
		return this.paintWidth;
	}

	/**
	 * Set the width of this {@link TabPainter}<br>
	 * Will also update all relevant objects, and repaint the object to update.
	 * @param width See {@link #width}
	 */
	public void setPaintWidth(int width){
		this.setPaintSize(width, this.getPaintHeight());
	}

	/**
	 * Get the height of this {@link TabPainter}
	 * @return See {@link #height}
	 */
	public int getPaintHeight(){
		return this.paintHeight;
	}

	/**
	 * Set the height of this {@link TabPainter}<br>
	 * Will also update all relevant objects, and repaint the object to update.
	 * @param height See {@link #height}
	 */
	public void setPaintHeight(int height){
		this.setPaintSize(this.getPaintWidth(), height);
	}
	
	/**
	 * Set both the height and width of the paintable area.<br>
	 * Will also update all relevant objects, and repaint the object to update.
	 * @param width See {@link #width}
	 * @param height See {@link #height}
	 */
	public void setPaintSize(int width, int height){
		this.paintWidth = width;
		this.paintHeight = height;
		this.tabCamera.setWidth(width);
		this.tabCamera.setHeight(height);
		
		this.setPreferredSize(new Dimension(width, height));
		this.repaint();
	}
	
	/** @return See {@link #selected} */
	public SelectionList getSelected(){
		return this.selected;
	}
	
	/**
	 * Determine if the given {@link TabPosition} on the given {@link TabString} index is selected
	 * @param p The {@link TabPosition} to test
	 * @param stringIndex The index of the {@link TabString}
	 * @return true if it is selected on the given {@link TabString}, false otherwise
	 */
	public boolean isSelected(TabPosition p, int stringIndex){
		Tab t = this.getTab();
		if(t == null) return false;
		return this.getSelected().isSelected(p, t.getStrings().get(stringIndex), stringIndex);
	}
	
	/**
	 * Determine if the given {@link TabPosition} on the given {@link TabString} index is selected
	 * @param p The {@link TabPosition} to test
	 * @param stringIndex The index of the {@link TabString}
	 * @return true if it is selected on the given {@link TabString}, false otherwise
	 */
	public boolean isSelected(TabPosition p, TabString string){
		Tab t = this.getTab();
		if(t == null) return false;
		
		int index = t.getStrings().indexOf(string);
		if(index < 0) return false;
		
		return this.getSelected().isSelected(p, string, index);
	}
	
	/** @return See {@link #lastSelected} */
	public Selection getLastSelected(){
		return this.lastSelected;
	}
	
	/** @param lastSelected See {@link #lastSelected} */
	public void setLastSelected(Selection lastSelected){
		this.lastSelected = lastSelected;
	}
	
	/** @return See {@link #hoveredPosition} */
	public TabPosition getHoveredPosition(){
		return hoveredPosition;
	}

	/** @param hoveredPosition See {@link #hoveredPosition} */
	public void setHoveredPosition(TabPosition hoveredPosition){
		this.hoveredPosition = ObjectUtils.copy(hoveredPosition);
	}
	
	/**
	 * Update the hover position to the position at the given coordinate. This will remove the hover if the coordinates are not on a note
	 * @param mX The x coordinate of the position, usually a mouse position
	 * @param mY The y coordinate of the position, usually a mouse position
	 * @return true if the position was changed, false otherwise
	 */
	public boolean updateHoveredPosition(double mX, double mY){
		// Find the position
		Selection s = findPosition(mX, mY);
		
		// Either set the position to null, or the position of the selection 
		TabPosition old = this.getHoveredPosition();
		setHoveredPosition((s == null) ? null : s.getPos());
		TabPosition newPos = this.getHoveredPosition();
		
		// If both objects were null, the hover was not changed
		if(old == null) return newPos != null;
		
		// Otherwise, check if they are equal
		return !old.equals(newPos);
	}

	/** @return See {@link #selectionBox} */
	public SelectionBox getSelectionBox(){
		return this.selectionBox;
	}
	
	/** @return See {@link #dragger} */
	public SelectionDragger getDragger(){
		return this.dragger;
	}
	
	/** @return See {@link #copyPaster} */
	public SelectionCopyPaster getCopyPaster(){
		return this.copyPaster;
	}
	
	/**
	 * Get the {@link TabSymbol} in {@link #selected} at the given index
	 * @param i The index
	 * @return The {@link TabSymbol}, or null if it is out of bounds
	 */
	public TabSymbol selected(int i){
		TabPosition p = this.getSelected().selectedPosition(i);
		if(p == null) return null;
		return p.getSymbol();
	}
	
	/**
	 * Place the given selection into {@link #selected}, only if the given selection is a valid selection
	 * @param s The {@link Selection} to add
	 * @return true if it was added, false otherwise
	 */
	public boolean select(Selection s){
		Tab t = this.getTab();
		// Ensure the selection and tab both exist
		if(s == null || t == null) return false;
		
		SelectionList lis = this.getSelected();
		
		/*
		 * Return false if the note is already selected, 
		 * the string doesn't contain the position, 
		 * or the tab doesn't contain the string, 
		 * meaning it is an invalid selection
		 */
		TabPosition p = s.getPos();
		TabString str = s.getString();
		if(!str.contains(p) | !tab.getStrings().contains(str) | lis.isSelected(s)) return false;
		
		lis.add(s);
		this.setLastSelected(s);
		return true;
	}
	
	/**
	 * Select the given {@link TabPosition} on the given string. This does not unselect anything else. 
	 * The selection only occurs if the note is not already selected, and the {@link TabPosition} is on the {@link TabString}
	 * @param pos index The {@link TabPosition} to select
	 * @param string The {@link TabString} which to select a note on
	 * @return true if the note was not selected and now is selected, false otherwise
	 */
	public boolean select(TabPosition pos, TabString string){
		Tab t = this.getTab();
		if(t == null) return false;
		
		int stringIndex = t.getStrings().indexOf(string);
		if(stringIndex < 0) return false;

		return this.select(new Selection(pos, string, stringIndex));
	}
	
	/**
	 * Select the {@link TabPosition} on the given string at the given index. This does not unselect anything else. 
	 * The selection only occurs if the note is not already selected
	 * @param index index The index on the string of the {@link TabPosition} to select
	 * @param string The {@link TabString} which to select a note on
	 * @return true if the note was selected, false otherwise
	 */
	public boolean select(int index, TabString string){
		TabPosition p = string.get(index);
		return this.select(p, string);
	}
	
	/**
	 * Select the given {@link TabPosition} on the string of the given index. This does not unselect anything else. 
	 * The selection only occurs if the note is not already selected
	 * @param p The {@link TabPosition} to select
	 * @param string The {@link TabString} which to select a note on
	 * @return true if the note was selected, false otherwise
	 */
	public boolean select(TabPosition p, int string){
		Tab t = this.getTab();
		if(t == null) return false;
		TabString s = t.getStrings().get(string);
		return this.select(p, s);
	}
	
	/**
	 * Select the {@link TabPosition} on the given string index at the given index. This does not unselect anything else. 
	 * The selection only occurs if the note is not already selected
	 * @param index index The index on the string of the {@link TabPosition} to select
	 * @param string The index of the string to select a note a note
	 * @return true if the note was selected, false otherwise
	 */
	public boolean select(int index, int string){
		Tab t = this.getTab();
		if(t == null) return false;
		TabString s = t.getStrings().get(string);
		TabPosition p = s.get(index);
		return this.select(p, s);
	}

	/**
	 * Create a new selection based on a given string index and a position on that string. 
	 * This does not select the note, it only creates a selection object referring to that note
	 * @param pos The position on the string to find a note
	 * @param strIndex The index of the {@link TabString} to make the selection
	 * @return The selection, or null if the {@link TabPosition} couldn't be found
	 */
	public Selection createSelection(double pos, int strIndex){
		Tab t = this.getTab();
		if(t == null) return null;
		TabString str = t.getStrings().get(strIndex);
		TabPosition p = str.findPosition(pos);
		if(p == null) return null;
		return new Selection(p, str, strIndex);
	}
	
	/**
	 * Create a {@link Selection} from {@link #tab} based on a string index and index in the {@link TabString}
	 * @param index The index on the {@link TabString}
	 * @param string the index of the {@link TabString}
	 * @return The {@link Selection}, or null if the tab doesn't exist
	 */
	public Selection stringSelection(int index, int string){
		Tab t = this.getTab();
		if(t == null) return null;
		TabString str = t.getStrings().get(string);
		return new Selection(str.get(index), str, string);
	}
	
	/**
	 * Remove the specified {@link Selection} from the currently selected
	 * @param s The {@link Selection} to unselect
	 * @param mX The x coordinate of the selected symbol to remove, usually a mouse position
	 * @param mY The y coordinate of the selected symbol to remove, usually a mouse position
	 * @return true if the selection was unselected, false otherwise
	 */
	public boolean deselect(double mX, double mY){
		Selection s = this.findPosition(mX, mY);
		if(s == null) return false;
		return this.getSelected().deselect(s);
	}
	
	/**
	 * Unselect all but the specified {@link TabPosition}
	 * @param p The {@link TabPosition} containing the {@link TabSymbol} to select
	 * @param string The {@link TabString} which p is on
	 * @return true if the note was selected and was not already selected, false otherwise. 
	 * 	If the given note is not already selected, this method clears the selection other than the given note
	 */
	public boolean selectOne(TabPosition p, TabString string){
		boolean selected = this.isSelected(p, string);
		this.clearSelection();

		return this.select(p, string) && !selected;
	}
	
	/**
	 * Select only one note near the given position<br>
	 * Does nothing if no valid note can be found
	 * @param mX The x coordinate, usually a mouse position
	 * @param mY The y coordinate, usually a mouse position
	 * @param add If this note is selected successfully, true to add to the existing selection, false to replace the selection with this selected note
	 * @return true if the selection took place, false otherwise
	 */
	public boolean selectNote(double mX, double mY, boolean add){
		// Find the position
		Selection s = this.findPosition(mX, mY);
		if(s == null) return false;
		TabPosition p = s.getPos();
		TabString str = s.getString();
		
		// No need to check if p is not null, findPosition will always have a non null TabPosition if it doesn't return null
		if(add) return this.select(s);
		else return this.selectOne(p, str);
	}
	
	/**
	 * Select only one note near the given position, this also unselects all other notes.<br>
	 * Does nothing if no valid note can be found
	 * @param mX The x coordinate, usually a mouse position
	 * @param mY The y coordinate, usually a mouse position
	 */
	public boolean selectNote(double mX, double mY){
		return this.selectNote(mX, mY, true);
	}
	
	/**
	 * Select all of the symbols which are on the same {@link TabString} as {@link #lastSelected}, and are also on the given note. 
	 * This only selects notes on the same string and same line of tab, not notes on any other strings
	 * @param mX The x coordinate of where to select a line, usually a mouse position
	 * @param mY The y coordinate of where to select a line, usually a mouse position
	 * @param add true if the newly selected notes should be added to the current selection, false to replace the previous selection
	 * @return true if anything was selected, false otherwise
	 */
	public boolean selectLine(double mX, double mY, boolean add){
		Selection originalSel = this.getLastSelected();
		Selection newSelection = this.findPosition(mX, mY);
		// Select the position, only if the newly selected selection is not already selected
		boolean success = !this.getSelected().isSelected(newSelection) && this.selectNote(mX, mY, add);

		// Must return at this point if either there was no note originally selected, or if a note was not selected
		boolean hasOriginal = originalSel != null;
		if(!hasOriginal || !success) return success;
		// Get the newly selected position
		Selection baseSel = originalSel;
		Selection endSel = this.getLastSelected();
		
		// If the two selections are not on the same string, return false
		TabString str = baseSel.getString();
		if(str != endSel.getString()) return false;
		
		// Ensure that baseSel is the lower position
		if(endSel.getPosition() < baseSel.getPosition()){
			Selection s = endSel;
			endSel = baseSel;
			baseSel = s;
		}
		
		// Select all notes between the two positions
		int lowI = baseSel.getString().findIndex(baseSel.getPosition());
		int highI = endSel.getString().findIndex(endSel.getPosition());
		for(int i = lowI; i <= highI; i++){
			this.select(i, str);
		}
		
		// Set the last selected to the value originally set
		this.setLastSelected(originalSel);
		
		// Return success
		return true;
	}
	
	/**
	 * Select every note in the tab
	 */
	public void selectAllNotes(){
		Tab t = this.getTab();
		if(t == null) return;
		this.clearSelection();
		for(TabString s : t.getStrings()){
			for(TabPosition p : s){
				this.select(p, s);
			}
		}
	}
	
	/**
	 * Unselect all selected {@link TabSymbol} objects
	 */
	public void clearSelection(){
		this.getSelected().clear();
		this.lastSelected = null;
		this.selectedNewTabNum = null;
	}
	
	/**
	 * Remove the {@link TabPosition} contained by the given {@link Selection}
	 * @param s The selection
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 * @return true if the position was removed, false otherwise
	 */
	public boolean removeSelection(Selection s, boolean recordUndo){
		this.getSelected().deselect(s);
		boolean success = s.getString().remove(s.getPos());
		
		// If recording undo, and the remove occurred, add the event to the stack
		if(recordUndo && success) this.getUndoStack().addEvent(new RemoveNotesEvent(s));
		
		return success;
	}
	
	/**
	 * Remove the {@link TabPosition} contained by the given {@link Selection}, not recording undo
	 * @param s The selection
	 * @return true if the position was removed, false otherwise
	 */
	public boolean removeSelection(Selection s){
		return this.removeSelection(s, false);
	}
	
	/**
	 * Remove every {@link TabPosition} in the given {@link SelectionList} from the {@link Tab}
	 * @param list The list of selections to remove
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 * @return The removed {@link Selection} objects, can be an empty list if nothing was removed
	 */
	public SelectionList removeSelections(List<Selection> list, boolean recordUndo){
		SelectionList removed = new SelectionList();
		if(list == null) return removed;
		for(int i = 0; i < list.size(); i++){
			Selection s = list.get(i);
			if(this.removeSelection(s)){
				removed.add(s);
				i--;
			}
		}
		
		// If recording undo, and at least one item was removed, record the undo
		if(recordUndo && !removed.isEmpty()){
			this.getUndoStack().addEvent(new RemoveNotesEvent(removed));
		}
		
		return removed;
	}
	
	/**
	 * Remove every {@link TabPosition} in the given list of {@link Selection} objects from the {@link Tab}, with not recording the undo
	 * @param list The list of selections to remove
	 * @return The removed {@link Selection} objects, can be an empty list if nothing was removed
	 */
	public SelectionList removeSelections(List<Selection> list){
		return this.removeSelections(list, false);
	}
	
	/**
	 * Remove every selected {@link TabPosition} from the tab
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 * @return The removed {@link Selection} objects
	 */
	public SelectionList removeSelectedNotes(boolean recordUndo){
		SelectionList removed = this.removeSelections(this.getSelected(), recordUndo);
		this.updateLineTabCount();
		this.clearSelection();
		return removed;
	}
	
	/**
	 * Remove every selected {@link TabPosition} from the tab, without recording the undo
	 * @return The removed {@link Selection} objects
	 */
	public SelectionList removeSelectedNotes(){
		return this.removeSelectedNotes(false);
	}

	
	/**
	 * Create a selection where the given selection would be moved by the given amount in terms of number of strings and tab position distance.<br>
	 * This method does not remove or add notes, it only determines the position a note should be placed.
	 * @param s The selection to move
	 * @param newBaseValue The position as a reference location to the new location of s. 
	 * 	Essentially, the new location of s must be on the same line of tab as this base position. If it is not, then the movement will fail.
	 * @param posValueChange The amount to add to the position, can be negative
	 * @param stringIndexChange The amount to add to the string index, can be negative
	 * @param keepPitch true to keep the pitch the same if the note moves position, false to keep the tab number
	 * @return The selection in the moved place if the given selection can be moved there, null otherwise. The position value 
	 * can be negative. This method will return a selection even if the selection would be on the same location as the given note. 
	 */
	public Selection findMovePosition(Selection s, double newBaseValue, double posValueChange, int stringIndexChange, boolean keepPitch){
		Tab t = this.getTab();
		if(s == null || t == null) return null;
		
		ArrayList<TabString> strs = t.getStrings();
		
		// The string the current Selection object used to be on
		TabString oldString = s.getString();
		// The TabPosition to be moved
		TabPosition newTabPos = s.getPos();
		// The new index and position value for the note
		int oldStringIndex = s.getStringIndex();
		int newStringIndex = oldStringIndex + stringIndexChange;
		double oldPosValue = s.getPosition();
		double newPosValue = oldPosValue + posValueChange;
		
		/*
		 * Move on to the next TabPosition it can't be placed in the new spot. 
		 * It can't be placed if:
		 * 	the string index is not in the range of the tab's string index size, 
		 *	or the TabPosition would be placed outside of a measure. 
		 *		This means that the position on a new line of tab, 
		 *		shifted over by the amount from the base selection, 
		 *		would be outside a tab
		*/ 
		// Checking inside the string range
		double oldBaseValue = newBaseValue - posValueChange;
		double measures = ZabAppSettings.get().paint().lineMeasures();
		if(!this.validStringIndex(newStringIndex) || 
				!this.tabPosInTabLine(newBaseValue, oldPosValue % measures - oldBaseValue % measures)){
			return null;
		}
		
		// Find the new valid string index
		TabString newString = strs.get(newStringIndex);
		
		// If necessary, update the pitch
		if(!keepPitch){
			TabSymbol newSymbol = newTabPos.getSymbol();
			newTabPos = new TabPosition(newSymbol.movingToNewString(oldString, newString), newTabPos.getPos());
		}
		
		// Create the selection
		newTabPos = new TabPosition(newTabPos.getSymbol(), newPosValue);
		return new Selection(newTabPos, newString, newStringIndex);
	}
	
	/**
	 * Find a {@link TabPosition} at the specified coordinates
	 * @param mX The x coordinate, usually a mouse position
	 * @param mY The y coordinate, usually a mouse position
	 * @return A {@link Selection} containing the position, or null if no position could be found
	 */
	public Selection findPosition(double mX, double mY){
		TabSettings settings = ZabAppSettings.get().tab();
		
		// Ensure the tab exists
		Tab t = this.getTab();
		if(t == null) return null;
		
		// Find the positions to look for a note
		double x = t.getTimeSignature().quantize(xToTabPos(mX, mY), settings.quantizeDivisor());
		int y = pixelYToStringNum(mY);

		// If either coordinate is invalid, no note is found
		if(x < 0 || y < 0) return null;
		// Only look for a note if the string has notes
		TabString str = t.getStrings().get(y);
		if(str.size() <= 0) return null;

		// Find the position
		TabPosition p = str.findPosition(x);
		if(p == null) return null;
		return new Selection(p, str, y);
	}
	
	/**
	 * Determine the hit box bounds, for rendering and detection,
	 * 	of the given {@link TabPosition} based on it's position and string index
	 * @param p The {@link TabPosition} to find the bounds
	 * @param string The string index
	 * @return The bounds in camera coordinates. These are special bounds, and must be treated as string bounds, not a normal rectangle
	 */
	public Rectangle2D symbolBounds(TabPosition p, int string){
		// Return an empty rectangle if the metrics are null, or if the tab is null
		FontMetrics fm = this.getLastSymbolFont();
		Tab t = this.getTab();
		if(fm == null || t == null) return new Rectangle2D.Double();
		
		TabPaintSettings settings = ZabAppSettings.get().paint();
		Camera cam = this.getCamera(); 
		// Save current alignment
		int oldXAlign = cam.getStringXAlignment();
		int oldYAlign = cam.getStringYAlignment();
		int oldScale = cam.getStringScaleMode();
		
		cam.setStringXAlignment(settings.symbolXAlign());
		cam.setStringYAlignment(settings.symbolYAlign());
		cam.setStringScaleMode(settings.symbolScaleMode());
		
		double x = this.tabPosToCamX(p.getPos());
		double y = this.tabPosToCamY(p.getPos(), string);
		
		TabSymbol symbol = p.getSymbol();
		String str = symbol.getModifiedSymbol(t.getStrings().get(string));

		// Finding the size of the space the symbol will take up based on the string which will be drawn;
		Rectangle2D r = cam.stringCamBounds(str, x, y);
		
		// Restore previous alignment
		cam.setStringXAlignment(oldXAlign);
		cam.setStringYAlignment(oldYAlign);
		cam.setStringScaleMode(oldScale);
		
		// Expand the bounds by a border
		// Add a small border for the width and height, zooming out so that the border size is the same regardless of zoom level
		double border = settings.symbolBorderSize();
		double borderX = cam.stringInverseZoom(border);
		double borderY = cam.stringInverseZoom(border);
		double rx = r.getX() - borderX;
		double ry = r.getY() - borderY;
		double rw = r.getWidth() + borderX * 2;
		double rh = r.getHeight() + borderY * 2;
		return new Rectangle2D.Double(rx, ry, rw, rh);
	}
	
	/**
	 * Remove every note in {@link #tab}
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 */
	public void clearNotes(boolean recordUndo){
		Tab t = this.getTab();
		
		// If recording undo, and there is a tab with notes, add a new event containing all of the notes
		if(recordUndo && t != null && !t.isEmpty()){
			this.selectAllNotes();
			this.getUndoStack().addEvent(new RemoveNotesEvent(this.getSelected()));
			this.clearSelection();
		}
		
		if(t != null) t.clearNotes();
	}

	/**
	 * Remove every note in {@link #tab} without recording undo
	 */
	public void clearNotes(){
		this.clearNotes(false);
	}

	/**
	 * Reset the entire tab painter to a default state, removing all notes, and resetting the camera
	 */
	public void reset(){
		Tab t = this.getTab();
		
		if(t != null) this.clearNotes();
		this.updateLineTabCount();
		this.getDragger().reset();
		this.getSelectionBox().clear();
		this.resetCamera();
	}
	
	/**
	 * Undo the last recorded action in this {@link TabPainter}
	 * @return true if the undo was successful i.e. there was an event to undo, false otherwise
	 */
	public boolean undo(){
		boolean success = this.getUndoStack().undo();
		this.updateLineTabCount();
		return success;
	}
	
	/**
	 * Redo the last undone action in this {@link TabPainter}
	 * @return true if the redo was successful i.e. there was an event to redo, false otherwise
	 */
	public boolean redo(){
		boolean success = this.getUndoStack().redo();
		this.updateLineTabCount();
		return success;
	}
	
	/**
	 * @return See {@link #tabLineCount}
	 */
	public int getLineTabCount(){
		return this.tabLineCount;
	}
	
	/**
	 * Based on the current state of this {@link TabPainter} determine how many lines should be displayed
	 */
	public void updateLineTabCount(){
		this.tabLineCount = 0;
		// If there are no strings, or tab is null, leave the line count at 1
		if(this.numStrings() == 0) return;

		// Find the line number to use
		int lineNum;
		TabPosition p = this.getTab().lastPosition();
		// If the position could not be found, then no note was found, default to zero lines
		if(p == null) lineNum = 0;
		// Otherwise, use the position value to find the line number
		// The string position doesn't matter, because any string index will give the same line number, so just use zero
		else lineNum = this.lineNumberFromPos(p.getPos());
		
		// One after the proper line number is the minimum line amount, meaning a minimum of 2
		// The 1 inside Math.max is because lineNum is an index, so one must be added to make it a proper number
		// The 1 outside Math.max is to make it one after the line number
		this.tabLineCount = Math.max(1, lineNum + 1) + 1;
	}
	
	/**
	 * @return See {@link #selectedNewTabNum}
	 */
	public Integer getSelectedNewTabNum(){
		return this.selectedNewTabNum;
	}
	
	/**
	 * Add the given numerical character to the selected tab number.<br>
	 * Only contributes to the number when a selection is made.<br>
	 * Automatically resets the number to null if it goes beyond the range
	 * @param num The number, does nothing if the character is not a minus sign or a number
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 * @return The new value of selectedNewTabNum
	 */
	public Integer appendSelectedTabNum(char num, boolean recordUndo){
		if(this.getSelected().isEmpty()) return this.selectedNewTabNum;
		
		// Keep track of the original number
		Integer oldNum = this.selectedNewTabNum;
		
		// If recording undo, keep track of the list of notes that will be changed
		SelectionList oldSel = new SelectionList();
		if(recordUndo) oldSel.addAll(this.getSelected());
		
		// Determine the new number based on the given character
		Integer n = oldNum;
		boolean isMinus = num == '-';
		boolean isNum = num >= '0' && num <= '9';
		int newNum = num - '0';
		if(n == null){
			if(isNum) n = newNum;
		}
		else{
			if(isMinus) n *= -1;
			else if(isNum){
				n *= 10;
				if(n < 0) n -= newNum;
				else n += newNum;
			}
		}
		this.selectedNewTabNum = n;
		
		// Update the number display
		if(n != null){
			SelectionList list = this.getSelected();
			for(int i = 0; i < list.size(); i++){
				Selection sel = list.get(i);
				
				// Unpack selection
				TabPosition p = sel.getStringPos();
				// Ensure the note exists
				if(p == null) continue;
				
				TabString s = sel.getString();
				
				// Ensure the note stays within a certain range
				int maxFretNum = ZabAppSettings.get().control().maxFretNum();
				if(n < -maxFretNum || n > maxFretNum){
					n %= 10;
					this.selectedNewTabNum = n;
				}
				// Set the note on the string and update the selection
				p = p.copySymbol(p.getSymbol().createPitchNote(s.createPitch(n)));
				sel.getString().setSymbol(sel.getString().findIndex(p.getPos()), p.getSymbol());
				list.set(i, new Selection(p, s, sel.getStringIndex()));
			}
		}
		this.repaint();
		
		// If recording undo, and a valid character was given, record the action
		if(recordUndo && (isNum || isMinus)){
			// The event places the current state of the selected notes, and removes the original placements
			this.getUndoStack().addEvent(new SelectedTabNumberEvent(this.getSelected(), oldSel));
		}
		return this.selectedNewTabNum;
	}
	
	/**
	 * Add the given numerical character to the selected tab number, without recording undo.<br>
	 * Only contributes to the number when a selection is made.<br>
	 * Automatically resets the number to null if it goes beyond the range
	 * @param num The number, does nothing if the character is not a minus sign or a number
	 * @return The new value of selectedNewTabNum
	 */
	public Integer appendSelectedTabNum(char num){
		return this.appendSelectedTabNum(num, false);
	}

	/**
	 * Modify the octave of the current selection by the given amount
	 * 
	 * @param octaves The number of octaves to add or subtract
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 */
	public void modifyOctave(int octaves, boolean recordUndo){
		this.addPitchToSelection(octaves * 12, recordUndo);
	}
	
	/**
	 * Modify the pitch of the current selection by the given number of frets
	 * 
	 * @param octaves The number of frets to add or subtract
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 */
	public void addPitchToSelection(int frets, boolean recordUndo){
		// If recording undo, keep track of the list of notes that will be changed
		SelectionList oldSel = new SelectionList();
		if(recordUndo) oldSel.addAll(this.getSelected());

		// TODO fix a bug where typing a note then trying to immediately change the octave will not work
		SelectionList list = this.getSelected();
		for(int i = 0; i < list.size(); i++){
			Selection sel = list.get(i);
			
			// Unpack selection
			TabPosition p = sel.getStringPos();
			// Ensure the note exists and uses a pitch
			if(p == null || !(p.getSymbol() instanceof TabPitch)) continue;
			TabPitch pitch = (TabPitch)(p.getSymbol());
			
			TabString s = sel.getString();
			
			// Set the note on the string to the modified note and update the selection
			p = p.copySymbol(p.getSymbol().createPitchNote(s.createPitch(s.getTabNumber(pitch) + frets)));
			sel.getString().setSymbol(sel.getString().findIndex(p.getPos()), p.getSymbol());
			list.set(i, new Selection(p, s, sel.getStringIndex()));
		}
		
		// If recording undo, record the action
		if(recordUndo){
			// The event places the current state of the selected notes, and removes the original placements
			this.getUndoStack().addEvent(new SelectedTabNumberEvent(this.getSelected(), oldSel));
		}
		this.repaint();
	}
	
	/**
	 * Apply the given modifier to every selected {@link TabPosition}.<br>
	 * Will use a empty modifier for mod if it is null
	 * @param mod The modifier to give to the notes
	 * @param mode The way to apply the modifier. Values to use:
	 * <ul>
	 * <li>0: Replace: remove the modifier currently used by the selection, and apply the new one</li>
	 * <li>1: Add: If the modifier is empty, use the given modifier, otherwise don't change the modifier </li>
	 * <li>2: Remove: Completely remove the modifier from the selected symbols. In this case the value of mod is not used </li>
	 * </ul>
	 * 	An unrecognized value will default to 0, i.e. replace mode.
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 */
	public void placeModifier(TabModifier mod, int mode, boolean recordUndo){
		// If recording undo, keep track of the list of notes that will be changed
		SelectionList oldSel = new SelectionList();
		if(recordUndo) oldSel.addAll(this.getSelected());
		boolean changed = false;
		
		if(mod == null) mod = new TabModifier();
		SelectionList list = this.getSelected();
		for(int i = 0; i < list.size(); i++){
			Selection s = list.get(i);
			
			// Find the new modifier
			TabSymbol sym = s.getPos().getSymbol();
			switch(mode){
				default:
				// Replace
				case 0: sym = sym.copyNewModifier(mod); break;
				// Add
				case 1: sym = sym.copyAddModifier(mod); break;
				// Remove
				case 2: sym = sym.copyNewModifier(new TabModifier()); break;
			}
			
			// Set the symbol of the position on the string only if the new symbol is different
			TabPosition p = s.getStringPos();
			if(p != null && !p.getSymbol().equals(sym)){
				// If any symbols are set, mark this as having changed
				changed = true;
				p = p.copySymbol(sym);
				
				// If a symbol modified, set the new version in the selection and place it in the tab
				s.getString().setSymbol(s.getString().findIndex(p.getPos()), p.getSymbol());
				list.set(i, new Selection(p, s.getString(), s.getStringIndex()));
			}
		}

		// If recording undo, and a modifier was changed, add the undo event
		if(recordUndo && changed){
			// The event places the current state of the selected notes, and removes the original placements
			this.getUndoStack().addEvent(new RemovePlaceNotesEvent(this.getSelected(), oldSel));
		}
	}
	
	/**
	 * Apply the given modifier to every selected {@link TabPosition}, without recording undo.<br>
	 * Will use a empty modifier for mod if it is null
	 * @param mod The modifier to give to the notes
	 * @param mode The way to apply the modifier. Values to use:
	 * <ul>
	 * <li>0: Replace: remove the modifier currently used by the selection, and apply the new one</li>
	 * <li>1: Add: If the modifier is empty, use the given modifier, otherwise don't change the modifier </li>
	 * <li>2: Remove: Completely remove the modifier from the selected symbols. In this case the value of mod is not used </li>
	 * </ul>
	 * 	An unrecognized value will default to 0, i.e. replace mode.
	 */
	public void placeModifier(TabModifier mod, int mode){
		this.placeModifier(mod, mode, false);
	}
	
	/**
	 * Replace all of the notes in {@link #selected} with dead notes, removing their modifiers
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 * @return true if at least one dead note was placed, false otherwise
	 */
	public boolean placeDeadNote(boolean recordUndo){
		Tab t = this.getTab();
		if(t == null) return false;
		
		SelectionList list = this.getSelected();
		SelectionList added = new SelectionList();
		SelectionList removed = new SelectionList();
		if(list.isEmpty()) return false;
		
		for(int i = 0; i < list.size(); i++){
			// Find the string and the index on the string to make the replacement
			Selection s = list.get(i);
			int strI = s.getStringIndex();
			TabString str = t.getStrings().get(strI);
			TabPosition oldPos = str.findPosition(s.getPosition());
			
			// If the position on the selection does not match that of the string, or it is already a dead note, skip that selection
			if(oldPos == null || oldPos.getSymbol().equals(new TabDeadNote())) continue;
			
			// Replace the symbol
			str.remove(s.getPos().getSymbol(), s.getPosition());
			TabPosition newPos = s.getPos().copySymbol(new TabDeadNote());
			str.add(newPos);
			
			// Remove the selection from the list
			list.remove(i);
			removed.add(s);
			Selection toAdd = new Selection(newPos, str, strI);
			added.add(toAdd);
			i--;
		}
		for(Selection s : added) list.add(s);
		
		boolean changed = added.size() > 0;
		
		// If recording undo and at least one note was added, add an undo event
		if(recordUndo && changed){
			this.getUndoStack().addEvent(new RemovePlaceNotesEvent(added, removed));
		}
		
		return changed;
	}
	
	/**
	 * Replace all of the notes in {@link #selected} with dead notes, removing their modifiers, without recording undo
	 * @return true if at least one dead note was placed, false otherwise
	 */
	public boolean placeDeadNote(){
		return this.placeDeadNote(false);
	}
	
	/**
	 * Place a note based on the given coordinates in pixel space.<br>
	 * Can do nothing if the coordinates aren't near the tab, or if the note cannot be placed
	 * @param mX The x coordinate, usually a mouse position
	 * @param mY The y coordinate, usually a mouse position
	 * @param fret The fret number to place
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 * @return true if the note was placed, false otherwise
	 */
	public boolean placeNote(double mX, double mY, int fret, boolean recordUndo){
		double x = xToTabPos(mX, mY);
		int y = pixelYToStringNum(mY);
		Tab t = this.getTab();
		// If either coordinate is invalid, or the tab is null, the placement fails
		if(x < 0 || y < 0 || t == null) return false;
		
		// Find the string and create a position to place there
		TabString str = t.getStrings().get(y);
		TabPosition p = TabFactory.modifiedFret(str, fret, x);
		p = p.quantize(t.getTimeSignature(), ZabAppSettings.get().tab().quantizeDivisor());
		
		// Try to add the note, and select it if it was placed
		boolean placed = this.placeNote(p, str, recordUndo);
		if(placed){
			this.selectOne(p, str);
		}
		
		this.updateLineTabCount();
		return placed;
	}
	
	/**
	 * Place a note based on the given coordinates in pixel space, without recording the undo.<br>
	 * Can do nothing if the coordinates aren't near the tab, or if the note cannot be placed
	 * @param mX The x coordinate, usually a mouse position
	 * @param mY The y coordinate, usually a mouse position
	 * @param fret The fret number to place
	 * @return true if the note was placed, false otherwise
	 */
	public boolean placeNote(double mX, double mY, int fret){
		return this.placeNote(mX, mY, fret, false);
	}
	
	/**
	 * Place the given {@link TabPosition} on the given {@link TabString}
	 * @param p The {@link TabPosition} to place
	 * @param s The {@link TabString}
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 * @return true if the note was placed, false otherwise
	 */
	public boolean placeNote(TabPosition p, TabString s, boolean recordUndo){
		Tab t = this.getTab();
		if(t == null) return false;
		
		int index = t.getStrings().indexOf(s);
		if(!this.validStringIndex(index)) return false;
		
		return this.placeNote(new Selection(p, s, index), recordUndo);
	}
	
	/**
	 * Place the given {@link TabPosition} on the given {@link TabString}, without recording the undo
	 * @param p The {@link TabPosition} to place
	 * @param s The {@link TabString}
	 * @return true if the note was placed, false otherwise
	 */
	public boolean placeNote(TabPosition p, TabString s){
		return this.placeNote(p, s, false);
	}
	
	/**
	 * Place the {@link TabPosition} of the given {@link Selection} on its {@link TabString}
	 * @param s The {@link Selection}
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 * @return true if the selection was placed, false otherwise
	 */
	public boolean placeNote(Selection s, boolean recordUndo){
		boolean success = s.getString().add(s.getPos());
		
		// If the note was placed, and recording undo, then record this placement action
		if(success && recordUndo) this.getUndoStack().addEvent(new PlaceNotesEvent(s));
		this.updateLineTabCount();
		
		return success;
	}
	
	/**
	 * Place the {@link TabPosition} of the given {@link Selection} on its {@link TabString}, without recording undo
	 * @param s The {@link Selection}
	 * @return true if the selection was placed, false otherwise
	 */
	public boolean placeNote(Selection s){
		return this.placeNote(s, false);
	}
	
	/**
	 * Place the every {@link TabPosition} of the given {@link List} of {@link Selection} objects
	 * @param list The {@link List}
	 * @param recordUndo true to record this action in {@link #undoStack}, false otherwise
	 * @return true if at least one selection was placed, false otherwise
	 */
	public boolean placeNotes(List<Selection> list, boolean recordUndo){
		SelectionList placed = new SelectionList();
		for(Selection s : list){
			if(this.placeNote(s)) placed.add(s);
		}
		
		// If recording undo, record the placed notes as an action in the undo
		if(recordUndo && placed.size() > 0) this.getUndoStack().addEvent(new PlaceNotesEvent(placed));
		
		return !placed.isEmpty();
	}
	
	/**
	 * Place the every {@link TabPosition} of the given {@link List} of {@link Selection} objects without recording the undo
	 * @param list The {@link List}
	 * @return true if at least one selection was placed, false otherwise
	 */
	public boolean placeNotes(List<Selection> list){
		return this.placeNotes(list, false);
	}
	
	/**
	 * Place and select the given {@link Selection}
	 * @param s The {@link Selection} to place and select
	 * @return true if the {@link Selection} was placed, false otherwise
	 */
	public boolean placeAndSelect(Selection s){
		boolean success = this.placeNote(s);
		this.select(s);
		return success;
	}
	
	/**
	 * Place and select every {@link TabPosition} in the given list
	 * @param list The list of {@link Selection} objects containing {@link TabPosition} objects to be placed and selected
	 */
	public void placeAndSelect(List<Selection> list){
		if(list == null) return;
		for(Selection s : list) this.placeAndSelect(s);
	}

	/**
	 * Get the {@link Camera} this {@link TabPainter} uses for rendering
	 * @return
	 */
	public Camera getCamera(){
		return this.tabCamera;
	}
	
	/**
	 * Bring the camera to a default state of its origin
	 */
	public void resetCamera(){
		Camera cam = this.tabCamera;
		TabPaintSettings settings = ZabAppSettings.get().paint();
		
		cam.setZoomBase(2);
		cam.setXZoomFactor(0);
		cam.setYZoomFactor(0);
		cam.setDrawOnlyInBounds(true);

		// Center the camera around the tab
		double numMeasures = settings.lineMeasures();
		cam.center(cam.zoomX(this.tabPosToCamX(numMeasures * 0.5)), this.tabPosToCamY(numMeasures, 0));
	}
	
	/**
	 * @return See {@link #tab}
	 */
	public Tab getTab(){
		return this.tab;
	}
	/**
	 * Set the {@link Tab} used by this {@link TabPainter}
	 * @param tab See {@link #tab}
	 */
	public void setTab(Tab tab){
		this.tab = tab;
		this.updateLineTabCount();
	}
	
	/** @return See {@link #undoStack} */
	public EditorEventStack getUndoStack(){
		return this.undoStack;
	}
	
	/**
	 * @return See {@link #mouseControl}
	 */
	public EditorMouse getMouseInput(){
		return this.mouseControl;
	}

	/**
	 * @return See {@link #keyControl}
	 */
	public EditorKeyboard getKeyInput(){
		return this.keyControl;
	}
	
	/**
	 * Convert the given x painter coordinate to account for the offset of the tab's position
	 * @param x The painter coordinate
	 * @return A painter coordinate with the x offset removed
	 */
	public double removeXOffset(double x){
		return x - ZabAppSettings.get().paint().baseX();
	}
	
	/**
	 * Convert the given y painter coordinate to account for the offset of the tab's position
	 * @param y The painter coordinate
	 * @return A painter coordinate with the y offset removed
	 */
	public double removeYOffset(double y){
		return y - ZabAppSettings.get().paint().baseY();
	}
	
	/**
	 * @return The width, in painter pixels, of each line of tab
	 */
	public double tabWidth(){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		return settings.measureWidth() * settings.lineMeasures();
	}
	
	/**
	 * @return The height, in painter pixels, of each line of tab
	 */
	public double tabHeight(){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		return Math.max(0, settings.stringSpace() * (this.numStrings() - 1));
	}

	/**
	 * Find the y painter coordinate of the beginning of a line of tab, excluding the space before and after the line of tab
	 * @param lineNum The line number, 
	 * @return The coordinate
	 */
	public double tabLineStart(int lineNum){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		return this.lineHeight() * lineNum + settings.aboveSpace();
	}

	/**
	 * Find the y painter coordinate of the ending of a line of tab, excluding the space before and after the line of tab
	 * @param lineNum The line number
	 * @return The coordinate
	 */
	public double tabLineEnd(int lineNum){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		return this.lineHeight() * (lineNum + 1) - settings.belowSpace();
	}
	
	/**
	 * Find the y painter coordinate of the beginning of a line of tab, excluding the space before and after the line of tab, 
	 *  with the extra buffer space above the line for checking positions near the note
	 * @param lineNum The line number
	 * @return The coordinate
	 */
	public double tabLineStartBuffer(int lineNum){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		return this.tabLineStart(lineNum) - settings.selectionBuffer();
	}

	/**
	 * Find the y painter coordinate of the ending of a line of tab, excluding the space before and after the line of tab, 
	 *  with the extra buffer space above the line for checking positions near the note
	 * @param lineNum The line number
	 * @return The coordinate
	 */
	public double tabLineEndBuffer(int lineNum){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		return this.tabLineEnd(lineNum) + settings.selectionBuffer();
	}
	
	/**
	 * Get the space to the side of a tab line which goes to the left or right of the line, 
	 * 	allowing for extra detection space on either side
	 * @return The buffer size
	 */
	public double tabLineSideBuffer(){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		return settings.measureWidth() * (1 / ZabAppSettings.get().tab().quantizeDivisor());
	}
	
	/**
	 * Determine if the given x camera coordinate is inside the valid range of a tab string with the buffer size
	 * @param x The camera coordinate to test
	 * @return true if it is in the range, false otherwise
	 */
	public boolean camXInTabLine(double x){
		double xOff = this.removeXOffset(x);
		return  -tabLineSideBuffer() < xOff && xOff < this.tabWidth();
	}
	
	/**
	 * Determine if the given x camera coordinate is inside the valid range of a tab string without the buffer size
	 * @param x The camera coordinate to test
	 * @return true if it is in the range, false otherwise
	 */
	public boolean camXInTabLineNoBuffer(double x){
		double xOff = this.removeXOffset(x);
		return  0 <= xOff && xOff < this.tabWidth();
	}
	
	/**
	 * Determine if a tab position, as located on a line of tab in painter coordinates, extended by the given change in length, 
	 * will still be on the same line, or if that change would place it in a new line.
	 * @param old The initial position, in measures
	 * @param change The number of measures to add
	 * @return true if it will be in a valid line of tab, false otherwise
	 */
	public boolean tabPosInTabLine(double old, double change){
		return this.camXInTabLineNoBuffer(this.tabPosToCamX(old) + this.measuresToPaintWidth(change));
	}
	
	/**
	 * @return The number of strings in the current tab, or 0 if the tab is null
	 */
	public int numStrings(){
		Tab t = this.getTab();
		return (t == null) ? 0 : t.getStrings().size();
	}
	
	/**
	 * Determine if the given string index is a valid one for the currently loaded tab
	 * @param i The index to test
	 * @return true if the index is valid, false otherwise
	 */
	public boolean validStringIndex(int i){
		return i >= 0 && i < this.numStrings();
	}
	
	/**
	 * Find the line number of the tab, in floating point, meaning how many lines of tab down is the given coordinate. 
	 * 	This will find a line even if the coordinate is not on a line of tab, but the space between.
	 * @param y The painter coordinate
	 * @return The tab number, can be a decimal number or negative
	 */
	public double lineNumberValue(double y){
		return this.removeYOffset(y) / this.lineHeight();
	}
	
	/**
	 * Find the line number of the tab, meaning how many lines of tab down is the given coordinate. 
	 * 	This will find a line even if the coordinate is not on a line of tab, but the space between.
	 * @param y The painter coordinate
	 * @return The line number, can be negative
	 */
	public int lineNumber(double y){
		double num = this.lineNumberValue(y);
		if(num < 0) num--;
		return (int)num;
	}
	
	/**
	 * Find the line number of the tab, meaning how many lines of tab down is the given measure distance. 
	 * @param pos The position on the tab to find the line number, in measures.
	 * @return The line number, or -1 if pos is negative
	 */
	public int lineNumberFromPos(double pos){
		if(pos < 0 ) return -1;
		// The string position doesn't matter, because any string index will give the same line number, so just use zero
		return this.lineNumber(this.tabPosToCamY(pos, 0));
	}
	
	/**
	 * Determine the distance in painter coordinates, which the beginning of the line number of the given coordinate travels
	 * @param y The painter coordinate
	 * @return The x position in painter coordinates
	 */
	public double lineLength(double y){
		return this.lineNumberLength(this.lineNumber(y));
	}
	
	/**
	 * Determine the distance in painter coordinates, which the beginning of the given line number travels
	 * @param num The line number
	 * @return The x position in painter coordinates
	 */
	public double lineNumberLength(int num){
		return this.tabWidth() * num;
	}
	
	/**
	 * Determine the tab position of the beginning of the given line number
	 * @param num The line number
	 * @return The tab position, in measures
	 */
	public double lineNumberMeasures(int num){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		return num * settings.lineMeasures();
	}
	
	/**
	 * @return The height, in painter pixels, of each line of tab, along with the space between each line
	 */
	public double lineHeight(){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		return settings.aboveSpace() + this.tabHeight() + settings.belowSpace();
	}
	
	/**
	 * Convert the given width in painter space into the equivalent number of measures
	 * @param width The width of the space in pixels
	 * @return The measure position
	 */
	public double paintWidthToMeasures(double width){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		return width / settings.measureWidth();
	}

	/**
	 * Convert the given number of measures to a width in painter space
	 * @param measure The measure position
	 * @return The width of the space in pixels
	 */
	public double measuresToPaintWidth(double measure){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		return measure * settings.measureWidth();
	}
	
	/**
	 * Take two painter coordinates and determine the quantized tab position at the given painter coordinates
	 * @param mX The x coordinate on the painter
	 * @param mY The y coordinate on the painter
	 * @return The tab position, or -1 if no valid position could be generated
	 */
	public double quantizedTabPos(double mX, double mY){
		Tab t = this.getTab();
		if(t == null) return -1;
		TabSettings settings = ZabAppSettings.get().tab();
		double pos = this.xToTabPos(mX, mY);
		if(pos < 0) return -1;
		return t.getTimeSignature().quantize(this.xToTabPos(mX, mY), settings.quantizeDivisor());
	}
	
	/**
	 * Convert an x coordinate on the painter, to a rhythmic position in a tab
	 * @param x The x coordinate on the painter
	 * @param y The y coordinate on the painter
	 * @return The position in number measures, can be negative for an invalid tab position
	 */
	public double xToTabPos(double x, double y){
		return this.camXToTabPos(this.getCamera().toCamX(x), this.getCamera().toCamY(y));
	}
	
	/**
	 * Convert a rhythmic position in a tab, to an x coordinate on the painter
	 * @param pos The position on the tab in measures
	 * @return The x position as a painter coordinate
	 */
	public double tabPosToX(double pos){
		return this.getCamera().toPixelX(this.tabPosToCamX(pos));
	}
	
	/**
	 * Convert an x coordinate on the camera, to a rhythmic position in a tab
	 * @param x The x coordinate on the camera
	 * @param y The y coordinate on the camera
	 * @return The position in number measures, can be negative to represent an invalid position
	 */
	public double camXToTabPos(double x, double y){
		// If the note is attempted to be placed at the outside the line, don't place it
		if(!this.camXInTabLine(x)) return -1;
		// Keep the x coordinate in the range of the tab string so that it will only be placed on a tab string of the same line of tab
		// 	and not on the next or previous line of tab
		x = Math.max(0, Math.min(this.tabWidth() - this.tabLineSideBuffer(), this.removeXOffset(x)));
		// Convert the position combined with the offset from the y position, to a measure position, to find the final tab position
		return this.paintWidthToMeasures(x + this.lineLength(y)); 
	}
	
	/**
	 * Convert a rhythmic position in a tab, to an x coordinate on the camera
	 * @param pos The position on the tab in measures
	 * @return The x coordinate on the camera
	 */
	public double tabPosToCamX(double pos){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		// Find the relative portion of a line it will be on, and give it the offset
		return this.measuresToPaintWidth(pos) % this.tabWidth() + settings.baseX();
	}
	
	/**
	 * Convert a y coordinate on the painter, to the string number relative to the coordinate. 
	 * Can be a decimal number describing which string it is closest to
	 * @param y The y coordinate on the painter
	 * @return The position string number, decimal numbers represent the closeness to that string
	 */
	public double yToTabPos(double y){
		return this.camYToTabPos(this.getCamera().toCamY(y));
	}
	
	/**
	 * Convert a string number in a tab, to a y coordinate on the painter
	 * @param pos The position on the tab, in number of measures
	 * @param num The string number, decimal numbers represent the closeness to that string
	 * @return The y position as a painter coordinate
	 */
	public double tabPosToY(double pos, double num){
		return this.getCamera().toPixelY(this.tabPosToCamY(pos, num));
	}
	
	/**
	 * Convert a y coordinate on the camera, to a string number
	 * @param y The y coordinate on the camera
	 * @return The string number, decimal numbers represent the closeness to that string. 
	 * Can be negative if no valid line number is found
	 */
	public double camYToTabPos(double y){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		
		// Get the line number as a double, and if it is a negative value, return -1 as it is not a valid line
		int lineNum = this.lineNumber(y);
		if(lineNum < 0) return -1;
		
		// Get rid of the tab offset, as well as 
		y = this.removeYOffset(y);
		
		// Find the start and end position of the tab itself, including and excluding the buffer
		double tabStart = this.tabLineStart(lineNum);
		double tabStartBuff = this.tabLineStartBuffer(lineNum);
		double tabEndBuff = this.tabLineEndBuffer(lineNum);
		
		// If the found coordinate is not within the buffered bounds, return -1, it is not a valid line
		if(tabStartBuff > y || y > tabEndBuff) return -1;

		// Find the position in the unbuffered bounds
		/*
		 * Find the percentage of the space through the line they position is, and determine the string
		 * 	First find the difference between the y position and the proper start position, 
		 * 	then divide that value by the total space of all of the lines of tab, 
		 * 	then multiply that percentage by the number of strings to get the string position
		 */
		double tabPos = y - tabStart;
		double stringNum = tabPos / (this.tabHeight() + settings.stringSpace()) * this.numStrings();
		
		// Ensure the string number will round to a valid string index
		return Math.max(0, Math.min(this.numStrings() - 1, stringNum));
	}
	
	/**
	 * Convert the string number, to a y coordinate on the camera
	 * @param pos The position on the tab, in measures
	 * @param num The string number, decimal numbers represent the closeness to that string
	 * @return The y coordinate on the camera
	 */
	public double tabPosToCamY(double pos, double num){
		TabPaintSettings settings = ZabAppSettings.get().paint();
		// Find the height which must be added to account for the line of that the tab position represents
		double heightOffset = this.lineHeight() * (int)(pos / settings.lineMeasures());
		// First add the space above each tab line, then the total space in the line, then the space from extra lines, and the final offset
		return settings.aboveSpace() + (num * settings.stringSpace()) + heightOffset + settings.baseY();
	}
	
	/**
	 * Get the string number which corresponds to the given y position
	 * @param y The y coordinate in pixel space
	 * @return The string number, or -1 if the position is not on a string
	 */
	public int pixelYToStringNum(double y){
		y = yToTabPos(y);
		if(y < 0) return -1;
		return (int)Math.round(y);
	}
	
	/**
	 * Get the {@link FontMetrics} for the font last used to repaint this {@link TabPainter}
	 * @return The {@link FontMetrics}, which will be in camera coordinates, can be null if the tab was never repainted
	 */
	public FontMetrics getLastSymbolFont(){
		return this.lastSymbolFont;
	}
	
	/**
	 * Draw the {@link Tab} to the screen<br>
	 * This method will reassign the graphics object in {@link #tabCamera}
	 */
	@Override
	public void paint(Graphics gr){
		ZabTheme theme = ZabAppSettings.theme();
		
		// Set up camera
		Graphics2D g = (Graphics2D)gr;
		Camera cam = this.tabCamera;
		cam.setGraphics(g);
		
		// Draw background
		g.setColor(theme.background());
		g.fillRect(0, 0, this.getPaintWidth(), this.getPaintHeight());
		
		// Draw the tab
		this.drawTab();
		
		// Draw the selection box
		this.getSelectionBox().draw(g);
		
		// Draw the selection being dragged
		g.setColor(theme.tabSymbolDragText());
		this.getDragger().draw();
	}
	
	/**
	 * Draw the {@link #tabCamera} of this {@link TabPainter}<br>
	 * This method will do nothing, and will not assign a graphics object, if {@link #tab} is null
	 * @return true if the drawing took place, false otherwise
	 */
	public boolean drawTab(){
		// Do nothing if the tab is null
		if(this.getTab() == null) return false;
		Tab tab = this.getTab();
		
		ZabTheme theme = ZabAppSettings.theme();
		TabPaintSettings settings = ZabAppSettings.get().paint();
		
		// Set up camera
		Camera cam = this.tabCamera;
		Graphics2D g = cam.getGraphics();

		// Set up for drawing strings and their labels
		cam.setStringXAlignment(settings.stringLabelXAlign());
		cam.setStringYAlignment(settings.stringLabelYAlign());
		cam.setStringScaleMode(settings.stringLabelScaleMode());
		g.setStroke(STRING_LINE_WEIGHT);
		g.setFont(SYMBOL_FONT);
		// Update the font metrics
		this.lastSymbolFont = g.getFontMetrics();
		// Enable antialiasing
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		// Draw strings, string note labels, and note symbols
		ArrayList<TabString> strings = tab.getStrings();
		// Starting y position for the first string on the tab
		double y = this.tabPosToCamY(0, 0);
		// The x position to draw the string labels
		double labelX = this.tabPosToCamX(0) + cam.stringInverseZoom(-settings.stringLabelSpace());
		
		int measuresPerLine = settings.lineMeasures();
		
		// Drawing all strings and measure lines
		String str;
		// Iterate through each line of tab
		for(int k = 0; k < this.getLineTabCount(); k++){
			// Iterate through each string in the line of tab
			for(int i = 0; i < this.numStrings(); i++){
				// Find the position to draw the line
				y = this.tabPosToCamY(k * measuresPerLine, i);
				// Find the string which the drawn line will represent
				TabString s = strings.get(i);
				str = s.getRootPitch().getPitchName(false);
				
				// Draw the line for TabString
				g.setColor(theme.tabString());
				cam.drawLine(labelX, y, this.measuresToPaintWidth(measuresPerLine) + settings.baseX(), y);
				
				// Draw string note label
				g.setColor(theme.tabSymbolText());
				cam.drawString(str, labelX, y);
			}
			
			// Draw a vertical line at the beginning of each measure
			g.setColor(theme.tabString());
			double measureLineEnd = y;
			double measureLineStart = measureLineEnd - this.tabHeight();
			for(int i = 0; i < measuresPerLine; i++){
				double x = this.tabPosToCamX(i);
				cam.drawLine(x, measureLineStart, x, measureLineEnd);
			}
		}
		
		// Drawing all main tab symbols
		g.setColor(theme.tabSymbolText());
		this.drawSymbols(tab, 0, 0);
		
		return true;
	}
	
	/**
	 * Draw the symbols of a {@link Tab} to this TabPainter.<br>
	 * This will use the current {@link #tabCamera} for rendering.<br>
	 * This method does not set graphics attributes such as the color of the symbol text, 
	 * that should be set by the method calling this method
	 * @param tab The {@link Tab} to render
	 * @param xOff The amount to offset the render on the x axis
	 * @param YOff The amount to offset the render on the y axis
	 * @return true if the render was successful, false otherwise
	 */
	public boolean drawSymbols(Tab tab, double xOff, double yOff){
		// Ensure the tab is not null
		if(tab == null) return false;
		
		ZabTheme theme = ZabAppSettings.theme();
		TabPaintSettings settings = ZabAppSettings.get().paint();
		Camera cam = this.getCamera();
		Graphics2D g = cam.getGraphics();
		String str;
		
		// Align the strings so that they are drawn in the upper left hand corner of the bounds
		cam.setStringScaleMode(settings.symbolScaleMode());
		cam.setStringXAlignment(Camera.STRING_ALIGN_CENTER);
		cam.setStringYAlignment(Camera.STRING_ALIGN_CENTER);
		
		// The current color is that which should be used for drawing symbols
		Color symbolColor = g.getColor();
		
		ArrayList<TabString> strs = tab.getStrings();
		for(int i = 0; i < strs.size(); i++){
			TabString s = strs.get(i);
			
			// Draw symbols on that string
			for(int j = 0; j < s.size(); j++){
				TabPosition p = s.get(j);

				// Get the symbol as a string
				TabSymbol t = p.getSymbol();
				str = t.getModifiedSymbol(s);
				
				// Finding the size of the space the symbol will take up
				Rectangle2D bounds = this.symbolBounds(p, i);
				double sX = bounds.getCenterX();
				double sY = bounds.getCenterY();
				
				// Draw any applicable highlights
				g.setColor(theme.tabSymbolHighlight());
				this.drawSymbolHighlight(this.getSelected(), p, i, bounds);
				g.setColor(theme.tabSymbolBoxHighlight());
				this.drawSymbolHighlight(this.getSelectionBox().getContents(), p, i, bounds);
				g.setColor(theme.tabSymbolHoverHighlight());
				this.drawSymbolHighlight(this.getHoveredPosition(), p, bounds);

				
				// Draw the symbol
				g.setColor(symbolColor);
				cam.drawString(str, sX + xOff, sY + yOff);
			}
		}
		// Restore the original color and return success
		g.setColor(symbolColor);
		return true;
	}
	
	/**
	 * If applicable, draw a highlight containing the given bounds.<br>
	 * This method will fail if the given {@link TabPosition} is not in the given {@link SelectionList}.<br>
	 * This method will also fail if the current {@link #tab} is null.<br>
	 * This method uses the camera of this {@link TabPainter} to draw graphics, 
	 * if no graphics object is set in that camera, this method fails 
	 * @param list The list to check, can be null to not check a list
	 * @param p The {@link TabPosition} which should be in the list
	 * @param i The index of the {@link TabString} which the {@link TabPosition} is on
	 * @param bounds The bounds of the highlight to draw
	 * @return true if the highlight was drawn, false otherwise
	 */
	public boolean drawSymbolHighlight(SelectionList list, TabPosition p, int i, Rectangle2D bounds){
		Tab t = this.getTab();
		if(t != null && (list == null || list.isSelected(p, t.getStrings().get(i), i))){
			Camera cam = this.getCamera();
			cam.fillStringRect(bounds);
			return true;
		}
		return false;
	}
	
	/**
	 * If applicable, draw a highlight where the given {@link TabPosition} would be drawn<br>
	 * This method will fail if the given {@link TabPosition} is not equal to the given {@link TabPosition}, or check is null.<br>
	 * This method uses the camera of this {@link TabPainter} to draw graphics, 
	 * if no graphics object is set in that camera, this method fails 
	 * @param check Only draw the highlight of the given {@link TabPosition} is the same object as this
	 * @param p The {@link TabPosition} which should be in the list
	 * @param bounds The bounds of the highlight to draw
	 * @return true if the highlight was drawn, false otherwise
	 */
	public boolean drawSymbolHighlight(TabPosition check, TabPosition p, Rectangle2D bounds){
		if(check != null && check.equals(p)) return this.drawSymbolHighlight((SelectionList)null, p, 0, bounds);
		return false;
	}
	
}
