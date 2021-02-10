package appMain.gui.editor.paint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import appMain.gui.editor.paint.event.RemovePlaceNotesEvent;
import tab.Tab;
import tab.TabPosition;
import tab.TabString;

/**
 * An object which tracks selection, located in a tab, which can then be placed down on the tab of {@link #getPainter()}
 * @author zrona
 */
public class SelectionPlacer extends TabPaintController{

	/** A {@link Tab} containing all of the notes which will be placed when the placement is made. Will be null if no selection is made */
	private Tab selectedTab;
	
	/**
	 * Create a new {@link SelectionPlacer} in a default state
	 * @param painter
	 */
	public SelectionPlacer(TabPainter painter){
		super(painter);
		this.selectedTab = null;
	}
	
	/** @return See {@link #selectedTab} */
	public Tab getSelectedTab(){
		return this.selectedTab;
	}
	
	/**
	 * Cancel the current state of the placement
	 */
	public void reset(){
		this.selectedTab = null;
	}
	
	/**
	 * Get a list containing all of the notes which are selected by this {@link SelectionPlacer}, which will be placed on its associated {@link TabPainter} tab. 
	 * If there is no selection, or the size of the painter tab doesn't match the size of the selection tab, the list will be empty, not null.
	 * @return The list
	 */
	public ArrayList<Selection> getSelectedList(){
		TabPainter paint = this.getPainter();
		Tab paintTab = paint.getTab();
		
		// If the selection tab is null, or the painter tab is null, or they have an unequal number of strings, return an empty list
		ArrayList<Selection> notes = new ArrayList<Selection>();
		Tab placeTab = this.getSelectedTab();
		if(placeTab == null || paintTab == null ||
				placeTab.getStrings().size() != paintTab.getStrings().size()){
			return notes;
		}
		
		// Otherwise, add all of the notes to the list as selections
		ArrayList<TabString> strs = placeTab.getStrings();
		for(int i = 0; i < strs.size(); i++){
			TabString str = strs.get(i);
			for(TabPosition p : str){
				notes.add(new Selection(p, paintTab.getStrings().get(i), i));
			}
		}
		return notes;
	}
	
	/**
	 * Initialize the selection with the selected notes of the given list
	 * @param list The list of selections which should be added to this placer
	 * @return true if the selection succeeded, false otherwise. This call will fail if the resulting selected tab is empty
	 */
	public boolean select(SelectionList list){
		TabPainter paint = this.getPainter();
		// If the tab of the painter is null, the selection fails
		Tab pTab = paint.getTab();
		if(pTab == null) return false;

		// Copy over all the given TabPositions to the selected tab
		this.selectedTab = pTab.copyWithoutSymbols();
		ArrayList<TabString> strs = this.selectedTab.getStrings();
		for(Selection sel : list){
			strs.get(sel.getStringIndex()).add(sel.getPos());
		}
		
		// Return success depending on if the tab selected any notes
		if(this.selectedTab.isEmpty()){
			this.selectedTab = null;
			return false;
		}
		return true;
	}
	
	/**
	 * Place the current {@link #selectedTab} based on the given position. 
	 * Depending on settings, may not place any, or may only place some, of the selection if the given position cannot place the selection. 
	 * @param oldBasePosValue The origin note position, in measures, of the selection. Essentially, the location where the 
	 * 	initialization for {@link #selectedTab} occurred.
	 * @param oldBaseStringIndex The string index which oldBasePosValue lies
	 * @param newBasePosValue The new origin note position, in measures, of the selection. Essentially, the location where the 
	 *  the origin of {@link #selectedTab} will be placed. 
	 * @param newBaseStringIndex The string index which newBasePosValue lies
	 * @param keepPitch Only applies when the note uses pitch. Use true to make the pitch stay the same, 
	 * 	i.e. the fret number will change if the strings have a different tuning, 
	 * 	use false to make the fret number stay the same, modifying the pitch if necessary
	 * @param replaceOriginal true if, when a single note fails to place, that single note, before it was moved, should be placed
	 * @param overwrite true if, when a placement is requested to be placed on an existing note, it should remove the original note, false
	 * 	if that note's movement should fail.
	 * @param cancelInvalid true if, when any selection cannot be placed for any reason, the entire placement should be undone, i.e. this method will do nothing. 
	 * 	false to place only the notes that can be placed in a valid way
	 * @return A {@link RemovePlaceNotesEvent} which contains all of the notes which were removed or added to the painter during this method. 
	 * 	Note that this method does not do anything to the undo stack of the painter, it only generates the event. 
	 * 	This object also can be null if the method failed to place anything, i.e. this method returns null when it makes no changes 
	 */
	public RemovePlaceNotesEvent place(
			double oldBasePosValue, double newBasePosValue, 
			int oldBaseStringIndex, int newBaseStringIndex,
			boolean keepPitch,
			boolean replaceOriginal, boolean overwrite, boolean cancelInvalid){
		
		TabPainter paint = this.getPainter();
		// If tab of the painter or the tab of this SelectionPlacer, is null, the selection cannot be placed, do nothing
		Tab paintTab = paint.getTab();
		Tab placeTab = this.getSelectedTab();
		if(paintTab == null || placeTab == null) return null;
		
		// Find the amount that needs to be added to all TabPosition position values when they are moved
		double posValueChange = newBasePosValue - oldBasePosValue;
		
		// Find the amount that needs to be added to all TabPosition objects when they are moved
		int stringIndexChange = newBaseStringIndex - oldBaseStringIndex;
		
		// Keep track of all of the notes which are removed and placed
		SelectionList added = new SelectionList();
		SelectionList removed = new SelectionList();
		
		// Find a list of all of the positions to add, based on the selected tab
		ArrayList<Selection> toAdd = this.getSelectedList();
		
		/*
		 * Sort the list based on the order of how the notes are being moved. 
		 * This is to ensure that, when a note is moved to where another selected note 
		 * 	that also needs to move, there are no overlapping issues
		 */
		PlaceSorter sorter = new PlaceSorter(posValueChange < 0, stringIndexChange < 0, true);
		sorter.sort(toAdd);
		
		boolean failedToPlace = true;
		
		// Go through all of the Selection objects to see which can be placed, and placed the ones that can be placed
		for(int i = 0; i < toAdd.size(); i++){
			// Find the original location of the TabPosition before it was moved
			Selection oldPos = toAdd.get(i);

			// Try to place the selection and find if it was placed successfully
			Selection moved = paint.findMovePosition(oldPos, newBasePosValue, posValueChange, stringIndexChange, keepPitch);
			
			// There is no need to find an overlap position unless moved is not null
			TabPosition overlapPosition = null;
			
			// If a Selection is found, verify that it is valid for placement
			if(moved != null){
				// Find the position, if one exists, which placing the note would replace
				overlapPosition = moved.getString().findPosition(moved.getPosition());
				
				/*
				 * If moved has a negative position value, 
				 * or overwrite is disabled and a TabPosition already exists at that location, 
				 * then it is not in a valid location, set moved to null
				 */
				if(moved.getPosition() < 0 || !overwrite && overlapPosition != null){
					moved = null;
				}
				
				// If not overwriting, must set the overlap position to null so that no overwriting happens
				if(!overwrite) overlapPosition = null;
			}
			
			// If moved cannot be placed, advance to the next loop cycle, or fail the placement depending on settings
			if(moved == null){
				// Fail the entire move and end the loop
				if(cancelInvalid){
					failedToPlace = true;
					break;
				}
				// If replacing original positions, and the original position could be replaced, add it to the list of added Selections
				else if(replaceOriginal && paint.placeAndSelect(oldPos)) added.add(oldPos);
			}
			// Otherwise, the placement can happen. Replace the Selection with its new location, also mark that a note was placed
			else{
				// If there was an overlapped position, must first remove the overlapping position, then add the new note 
				if(overlapPosition != null){
					Selection overlapSel = new Selection(overlapPosition, moved.getString(), moved.getStringIndex());
					paint.removeSelection(overlapSel); // This removal should happen regardless
					removed.add(overlapSel);
				}
				
				// Replace the selection
				paint.placeAndSelect(moved); // This placement should happen regardless
				toAdd.set(i, moved);
				added.add(moved);
				failedToPlace = false;
			}
		}
		RemovePlaceNotesEvent event;
		/*
		 * If the entire move should be canceled if one note fails to place, and a note failed to place, 
		 * restore the original positions by removing notes placed, and adding back in the notes removed. 
		 * Also set the failed placement to null, as the placement failed 
		 */
		if(cancelInvalid && failedToPlace){
			paint.removeSelections(added);
			paint.placeNotes(removed);
			added.clear();
			removed.clear();
			// The event is null if the entire placement fails
			event = null;
		}
		// Otherwise, create an event holding the notes which were placed and removed
		else event = new RemovePlaceNotesEvent(added, removed);
		
		// Ensure the correct number of tab lines is updated
		paint.updateLineTabCount();

		// If notes were removed or added, return the event, otherwise return null
		if(!removed.isEmpty() || !added.isEmpty()) return event;
		return null;
	}

	/**
	 * A {@link Comparator} used for sorting a list of {@link Selection} objects in various orders. 
	 */
	public static class PlaceSorter implements Comparator<Selection>{
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
		public PlaceSorter(boolean moveRight, boolean moveDown, boolean prioritizeStrings){
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
