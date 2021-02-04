package appMain.gui.editor.paint;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import appMain.gui.util.Camera;
import appUtils.ZabAppSettings;
import appUtils.settings.TabControlSettings;
import tab.Tab;
import tab.TabPosition;
import tab.TabString;

/**
 * A class used by {@link TabPainter} to keep track of and control moving a selection of notes around
 * @author zrona
 */
public class SelectionDragger extends TabPaintController{
	
	/** The {@link Selection} which was clicked to initialize a drag, null if nothing is selected */
	private Selection baseSelection;
	
	/** The point where this {@link #draggedTab} should be drawn, null if no point is selected */
	private Point2D.Double dragPoint;
	/** 
	 * The camera coordinates of the mouse click when the drag is initiated. 
	 * This is used, when rendering the selection to line up the selection to where it was with the mouse when the selection was initiated
	 */
	private Point2D.Double dragAnchorPoint;
	
	/**
	 * The {@link TabPosition} objects which are being moved by the current drag. Can be null when a drag is not initialized.<br>
	 * This should only be used for display purposes 
	 */
	private Tab draggedTab;
	
	/**
	 * Create a new {@link SelectionDragger} which is used by the given {@link TabPainter}
	 * @param painter See {@link #painter}
	 */
	public SelectionDragger(TabPainter painter){
		super(painter);
		this.reset();
	}
	
	/** @return See {@link #baseSelection} */
	public Selection getBaseSelection(){
		return this.baseSelection;
	}
	/** @param baseSelection See {@link #baseSelection} */
	public void setBaseSelection(Selection baseSelection){
		this.baseSelection = baseSelection;
	}
	
	/** @return See {@link #dragPoint} */
	public Point2D.Double getDragPoint(){
		return this.dragPoint;
	}
	/** @param dragPoint See {@link #dragPoint} */
	public void setDragPoint(Point2D.Double dragPoint){
		this.dragPoint = dragPoint;
	}
	/** @return See {@link #dragAnchorPoint} */
	public Point2D.Double getAnchorPoint(){
		return this.dragAnchorPoint;
	}
	/** @param anchorPoint See {@link #dragAnchorPoint} */
	public void setAnchorPoint(Point2D.Double anchorPoint){
		this.dragAnchorPoint = anchorPoint;
	}
	
	/** @return See {@link #draggedTab} */
	public Tab getDraggedTab(){
		return this.draggedTab;
	}
	
	/**
	 * Cancel the current drag selection
	 */
	public void reset(){
		this.setBaseSelection(null);
		this.setDragPoint(null);
		this.draggedTab = null;
		this.dragAnchorPoint = null;
	}
	
	/** @return true if a selection drag has been initialized, false otherwise */
	public boolean isDragging(){
		return this.getBaseSelection() != null;
	}
	
	/**
	 * Begin dragging selected notes based on the given position. 
	 * Will not initialize a drag if the given position does not land on a symbol.
	 * @param mX The x coordinate to base the drag, usually a mouse position
	 * @param mY The y coordinate to base the drag, usually a mouse position
	 * @return true if the call began the drag, false otherwise
	 */
	public boolean begin(double mX, double mY){
		TabPainter paint = this.getPainter();
		
		// If a drag is in progress, do not begin a new one
		if(this.isDragging()) return false;
		
		Selection s = paint.findPosition(mX, mY);
		// If a selected note was not found, do nothing
		if(s == null || !paint.getSelected().isSelected(s)) return false;
		
		// Otherwise set the base selection, set the notes to be dragged, and return success
		
		// Base selection
		this.setBaseSelection(s);
		// Copy over all relevant TabPositions
		Tab pTab = paint.getTab();
		this.draggedTab = pTab.copyWithoutSymbols();
		ArrayList<TabString> strs = this.draggedTab.getStrings();
		for(Selection sel : paint.getSelected()){
			strs.get(sel.getStringIndex()).add(sel.getPos());
		}
		// Set the internal anchor position appropriately so that the moved notes are around the mouse
		Camera cam = paint.getCamera();
		this.dragAnchorPoint = new Point2D.Double(cam.toCamX(mX), cam.toCamY(mY));
			
		// Return success
		return true;
	}
	
	/**
	 * Update the state of the dragging position to the given coordinates.
	 * @param mX The x coordinate of the drag, usually a mouse position
	 * @param mY The y coordinate of the drag, usually a mouse position
	 * @return true if the update happened, false otherwise
	 */
	public boolean update(double mX, double mY){
		// If dragging has not begun, don't do anything
		if(!this.isDragging()) return false;
		
		// Set the dragging point to the given coordinates
		Camera pCam = this.getPainter().getCamera();
		this.setDragPoint(new Point2D.Double(pCam.toCamX(mX), pCam.toCamY(mY)));
		
		return true;
	}
	
	/**
	 * End the selection drag, placing the current selection based on the given position. 
	 * Depending on settings, may not move, or may delete parts, of the selection if the given position cannot place the selection
	 * @param mX The x painter coordinate to place the selection, usually a mouse position
	 * @param mY The y painter coordinate to place the selection, usually a mouse position
	 * @param keepPitch Only applies when the note uses pitch. Use true to make the pitch stay the same, 
	 * i.e. the fret number will change if the strings have a different tuning, 
	 * 	use false to make the fret number stay the same, modifying the pitch if necessary 
	 * @return true if the selection was placed, false otherwise
	 */
	public boolean place(double mX, double mY, boolean keepPitch){
		TabPainter paint = this.getPainter();
		// If the position to place is not on a valid line of tab, do nothing
		if(paint.xToTabPos(mX, mY) < 0 || paint.pixelYToStringNum(mY) < 0) return false;
		
		TabControlSettings settings = ZabAppSettings.get().control();
		boolean deleteInvalid = settings.moveDeleteInvalid();
		boolean cancelInvalid = settings.cancelInvalid();
		
		// If tab or the base position is null, the selection cannot be placed, do nothing
		Selection base = this.getBaseSelection();
		Tab t = paint.getTab();
		if(t == null || base == null) return false;
		// Find the position values for the newly placed position for the base TabPosition, and the old position value
		double newBasePosValue = paint.quanitzedTabPos(mX, mY);
		double oldBasePosValue = base.getPosition();
		// Find the amount that needs to be added to all TabPosition position values when they are moved
		double posValueChange = newBasePosValue - oldBasePosValue;
		
		// Find the indexes of the new and old strings for the base selection TabPosition
		int newBaseStringIndex = paint.pixelYToStringNum(mY);
		int oldBaseStringIndex = base.getStringIndex();
		// Find the amount that needs to be added to all TabPosition objects when they are moved
		int stringIndexChange = newBaseStringIndex - oldBaseStringIndex;

		// Remove all of the notes and save them in a list
		ArrayList<Selection> oldPositions = paint.removeSelectedNotes();
		
		/*
		 * Create a copy of all of the Selections which will be the newly placed notes
		 * Copying each object to avoid objects being modified elsewhere
		 */
		ArrayList<Selection> added = new ArrayList<Selection>();
		for(Selection s : oldPositions){
			TabPosition p = s.getPos().copy();
			p.setPos(s.getPosition());
			added.add(new Selection(p, s.getString(), s.getStringIndex()));
		}
		/*
		 * Sort the list copy based on the order of how the notes are being moved. 
		 * This is to ensure that, when a note is moved to where another selected note 
		 * 	that also needs to move, there are no overlapping issues
		 */
		DragSorter sorter = new DragSorter(posValueChange < 0, stringIndexChange < 0, true);
		sorter.sort(added);
		
		boolean failedToPlace = true;
		
		// Go through all of the Selection objects to see which can be moved, and move the ones that can be moved
		for(int i = 0; i < added.size(); i++){
			// Try to move the selection and find if it was moved successfully
			Selection moved = paint.findMovePosition(added.get(i), newBasePosValue, posValueChange, stringIndexChange, keepPitch);
			// If it cannot be moved, then move onto the next item, or fail the move depending on settings
			if(moved == null){
				// Fail the entire move and end the loop
				if(cancelInvalid){
					failedToPlace = true;
					break;
				}
				// Move onto the next Selection
				else{
					/*
					 * If the elements which could not be moved need to be deleted,
					 *	remove from the list the selection which could not be moved
					 */
					if(!deleteInvalid){
						paint.placeAndSelect(added.get(i));
					}
				}
			}
			// Otherwise, replace the Selection with its new location, also state that a note was placed
			else{
				paint.placeAndSelect(moved);
				added.set(i, moved);
				failedToPlace = false;
			}
		}
		
		/*
		 * If the entire move should be canceled if one note fails to place, and a note failed to place, 
		 * restore the original positions
		 */
		if(cancelInvalid && failedToPlace){
			paint.removeSelections(added);
			paint.placeAndSelect(oldPositions);
		}
		
		// Ensure the correct number of tab lines is updated
		paint.updateLineTabCount();
		
		// End the drag selection, returning if the selection was placed in some way
		return !failedToPlace;
	}
	
	/**
	 * Draw the currently being dragged TabPositions to the {@link TabPainter} using this {@link SelectionDragger}.<br>
	 * This method does not handle setting color, font, etc those must be set by the method calling this method
	 * @return true if the draw took place, false otherwise
	 */
	public boolean draw(){
		if(!this.isDragging() || this.draggedTab == null) return false;
		
		TabPainter paint = this.getPainter();
		Point2D.Double p = this.getDragPoint();
		Point2D.Double anchor = this.getAnchorPoint();
		if(p == null || anchor == null) return false;
		return paint.drawSymbols(this.draggedTab, p.getX() - anchor.getX(), p.getY() - anchor.getY());
	}
	
	/**
	 * A {@link Comparator} used for sorting a list of {@link Selection} objects in various orders. 
	 */
	public static class DragSorter implements Comparator<Selection>{
		/** 
		 * true if this sorter should sort with {@link TabPosition} objects moving to the right as greater,  
		 * i.e. increasing tab position value, 
		 * false for moving to the left as greater, i.e. decreasing tab position value
		 */
		private boolean moveRight;
		/** 
		 * true if this sorter should sort with {@link TabPosition} objects moving down as greater,  
		 * i.e. increasing string index
		 * false for moving up as greater, i.e. decreasing string index */
		private boolean moveDown;
		
		/**
		 * true if first string indexes should be compared, then positions, 
		 * false for comparing positions, then string indexes
		 */
		private boolean prioritizeStrings;
		
		/**
		 * Create a new drag sorted with the given sort order
		 * @param moveRight See {@link #moveRight}
		 * @param moveDown See {@link #moveDown}
		 * @param prioritizeStrings See {@link #prioritizeStrings}
		 */
		public DragSorter(boolean moveRight, boolean moveDown, boolean prioritizeStrings){
			this.moveRight = moveRight;
			this.moveDown = moveDown;
			this.prioritizeStrings = prioritizeStrings;
		}
		
		/**
		 * Note, the {@link TabString} stored in each selection is not relevant to this sorting, 
		 * only the string index and {@link TabPosition} object are relevant 
		 */
		@Override
		public int compare(Selection s1, Selection s2){
			// Ensure both selections are not null
			if(s1 == null || s2 == null) return 0;
			
			// Comparing string indexes
			int i1 = s1.getStringIndex();
			int i2 = s2.getStringIndex();
			int str = i2 - i1;
			if(this.moveDown) str *= -1;
			
			// Comparing positions
			TabPosition p1 = s1.getPos();
			TabPosition p2 = s2.getPos();
			int pos;
			if(p1 == null || p2 == null) return 0;
			if(this.moveRight) pos = p1.compareTo(p2);
			else pos = p2.compareTo(p1);
			
			if(prioritizeStrings){
				if(str == 0) return pos;
				else return str;
			}
			else{
				if(pos == 0) return str;
				else return pos;
			}
		}
		
		/**
		 * Sort the given {@link List} of {@link Selection} objects based on this {@link Comparator}
		 * @param list The list to sort
		 */
		public void sort(List<Selection> list){
			list.sort(this);
		}
	}
	
}
