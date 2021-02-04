package appMain.gui.editor.paint;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import appMain.gui.ZabTheme;
import appMain.gui.util.Camera;
import appUtils.ZabAppSettings;
import tab.Tab;
import tab.TabPosition;
import tab.TabString;

/**
 * A helper object used by TabPainter to track a box used to select an area of notes
 * @author zrona
 */
public class SelectionBox extends TabPaintController{
	
	/** The first corner of this box which the user is using to select an area of notes, or null if no selection is being made */
	private Point2D.Double firstCorner;
	/** 
	 * The new corner of this box which the user is using to select an area of notes, or null if no selection is being made. 
	 * The first corner doesn't change when the new corner is being selected 
	 */
	private Point2D.Double newCorner;
	
	/** true if this selection box should update it's positions when the update method is called */
	private boolean selecting;
	
	/** All of the {@link Selection} objects which are selected by the selection box */
	private SelectionList contents;
	
	/**
	 * Create a {@link SelectionBox} with nothing currently selected
	 * @param painter See {@link #painter}
	 */
	public SelectionBox(TabPainter painter){
		super(painter);
		this.firstCorner = null;
		this.newCorner = null;
		this.selecting = false;
		this.contents = new SelectionList();
	}
	
	/** @return See {@link #firstCorner} */
	public Point2D.Double getFirstCorner(){
		return this.firstCorner;
	}
	/** @return See {@link #firstCorner} */
	public void setFirstCorner(Point2D.Double firstCorner){
		this.firstCorner = firstCorner;
	}
	
	/** @return See {@link #newCorner} */
	public Point2D.Double getNewCorner(){
		return this.newCorner;
	}
	/** @return See {@link #selectionBoxNewCorner} */
	public void setNewCorner(Point2D.Double newCorner){
		this.newCorner = newCorner;
	}
	
	/** @return See {@link #selecting} */
	public boolean isSelecting(){
		return this.selecting;
	}
	
	/** @return See {@link #selecting} */
	public void setSelecting(boolean selecting){
		this.selecting = selecting;
	}
	
	/** See {@link #contents} */
	public SelectionList getContents(){
		return this.contents;
	}
	
	/**
	 * Determine if both of the corners of this {@link SelectionBox} have been set
	 * @return true if both have been set, false otherwise
	 */
	public boolean hasCorners(){
		return this.getFirstCorner() != null && this.getNewCorner() != null;
	}
	
	/**
	 * Update the state of this selection box to the given coordinates. 
	 * If no positions are set, then set the first coordinate, otherwise, set the new coordinate
	 * @param mX The x painter coordinate of where to bring the selection box, usually a mouse position
	 * @param mY The y painter coordinate of where to bring the selection box, usually a mouse position
	 * @return  true if an update happened, false otherwise
	 */
	public boolean updateSelectionBox(double mX, double mY){
		if(!this.isSelecting()) return false;
		TabPainter paint = this.getPainter();
		Camera cam = paint.getCamera();
		
		Point2D.Double newPos = new Point2D.Double(cam.toCamX(mX), cam.toCamY(mY));
		if(this.getFirstCorner() == null) this.setFirstCorner(newPos);
		else this.setNewCorner(newPos);
		
		// If both corners have not yet been set, do not continue with the update, but return true because a corner was updated
		if(!this.hasCorners()) return true;
		
		// If the tab is null, the update cannot continue, no update happens
		Tab t = paint.getTab();
		if(t == null) return false;
		
		this.getContents().clear();
		ArrayList<TabString> strs = t.getStrings();
		Rectangle2D.Double bounds = this.getBounds();
		
		// The following only checks the symbols on the lines of tab which the selection box currently contains
		// This means it only checks a subset of the lines, rather than the entire tab
		
		// Find the line indexes containing notes
		int startLine = paint.lineNumber(bounds.getY());
		int endLine = paint.lineNumber(bounds.getMaxY());
		
		// Find the beginning of each line, end has +1 to bring it to the end of the line, rather than the beginning
		// This also ensures the positions are within the range of the tab
		double maxPos = t.length();
		double startPos = Math.max(0, Math.min(maxPos, paint.lineNumberMeasures(startLine)));
		double endPos = Math.max(0, Math.min(maxPos, paint.lineNumberMeasures(endLine + 1)));
		
		for(int i = 0; i < strs.size(); i++){
			TabString s = strs.get(i);
			// Find the indexes of the start and end positions for the particular line
			int startIndex = s.findIndex(startPos);
			int endIndex = s.findIndex(endPos);
			
			// Add the each note to the list of TabPositions contained by this SelectionBox
			for(int j = startIndex; j <= endIndex && j < s.size(); j++){
				TabPosition p = s.get(j);
				if(cam.camToPixelBounds(bounds).intersects(cam.stringCamToPixelBounds(paint.symbolBounds(p, i)))){
					this.contents.add(new Selection(p, s, i));
				}
			}
		}
		return true;
	}

	/**
	 * Determine the rectangular bounds of this {@link SelectionBox}
	 * @return The bounds, or an empty rectangle if the selection points don't exist
	 */
	public Rectangle2D.Double getBounds(){
		if(!this.hasCorners()) return new Rectangle2D.Double();
		Point2D.Double selFirst = this.getFirstCorner();
		Point2D.Double selNew = this.getNewCorner();
		double lowX = Math.min(selFirst.getX(), selNew.getX());
		double lowY = Math.min(selFirst.getY(), selNew.getY());
		double w = Math.abs(selFirst.getX() - selNew.getX());
		double h = Math.abs(selFirst.getY() - selNew.getY());
		return new Rectangle2D.Double(lowX, lowY, w, h);
	}
	
	/**
	 * Select all of the {@link TabPosition} objects held in the selection box
	 * @param add true to add to the current selection of the painter, false otherwise
	 */
	public void selectInPainter(boolean add){
		// Do nothing if there is no selection
		if(this.contents.isEmpty()) return;

		TabPainter paint = this.getPainter();
		if(!add) paint.clearSelection();
		for(Selection s : this.getContents()){
			paint.select(s.getPos(), s.getString());
		}
	}
	
	/**
	 * Clear all status of the selection box and add all of the selected notes to the painter, i.e. set both points defining it, to null
	 */
	public void clear(){
		// Reset all the values to having no selection
		this.setFirstCorner(null);
		this.setNewCorner(null);
		this.setSelecting(false);
		this.getContents().clear();
	}
	
	/**
	 * Draw this {@link Selection} box to the given graphics object
	 * @param g The graphics object used to draw the box
	 * @return true if the box was drawn, false otherwise
	 */
	public boolean draw(Graphics2D g){
		ZabTheme theme = ZabAppSettings.theme();
		TabPainter paint = this.getPainter();
		Camera cam = paint.getCamera();
		
		// Only draw the box if both of the corners have been set
		if(this.hasCorners()){
			g.setColor(theme.tabSymbolHighlight());
			Rectangle2D.Double bounds = this.getBounds();
			cam.fillRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
			return true;
		}
		return false;
	}
	
}
