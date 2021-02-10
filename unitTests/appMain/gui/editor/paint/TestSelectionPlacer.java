package appMain.gui.editor.paint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.editor.paint.SelectionPlacer.PlaceSorter;
import appMain.gui.editor.paint.event.RemovePlaceNotesEvent;
import music.Music;
import music.Pitch;
import tab.Tab;
import tab.TabFactory;
import tab.TabPosition;
import tab.TabString;
import tab.symbol.TabNote;
import util.testUtils.Assert;

public class TestSelectionPlacer extends AbstractTestTabPainter{
	
	private SelectionPlacer placer; 
	
	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(false);
		placer = new SelectionPlacer(paint);
	}
	
	@Test
	public void getSelectedTab(){
		assertEquals(null, placer.getSelectedTab(), "Checking selected tab initially null");
	}
	
	@Test
	public void reset(){
		paint.selectAllNotes();
		placer.select(paint.getSelected());
		assertNotEquals(null, placer.getSelectedTab(), "Checking selected tab is not null after a select call");
		
		assertTrue(placer.select(paint.getSelected()), "Checking selection succeeds");
		placer.reset();
		assertEquals(null, placer.getSelectedTab(), "Checking selected tab is null after a reset");
	}
	
	@Test
	public void getSelectedList(){
		ArrayList<Selection> empty = new ArrayList<Selection>();
		paint.select(0, 0);
		placer.select(paint.getSelected());
		paint.setTab(null);
		assertEquals(empty, placer.getSelectedList(), "Checking null painter tab returns an empty list");
		paint.setTab(tab);
		placer.reset();
		
		assertEquals(empty, placer.getSelectedList(), "Checking no selected tab returns an empty list");
		
		paint.select(0, 0);
		placer.select(paint.getSelected());
		tab.getStrings().remove(0);
		assertEquals(empty, placer.getSelectedList(), "Checking unequal tab sizes returns an empty list");
		
		this.setup();
		paint.select(0, 0);
		placer.select(paint.getSelected());
		// Checking a single selected note is contained
		Assert.containsSize(placer.getSelectedList(), paint.stringSelection(0, 0));
		
		placer.reset();
		paint.select(0, 0);
		paint.select(0, 5);
		placer.select(paint.getSelected());
		// Checking a multiple selected notes
		Assert.containsSize(placer.getSelectedList(), paint.stringSelection(0, 0), paint.stringSelection(0, 5));
	}
	
	@Test
	public void select(){
		paint.setTab(null);
		assertFalse(placer.select(new SelectionList()), "Checking selection fails with null tab");
		
		paint.setTab(tab);
		assertFalse(placer.select(new SelectionList()), "Checking selection fails with nothing selected");
		
		paint.select(0, 0);
		paint.select(0, 1);
		assertTrue(placer.select(paint.getSelected()), "Checking selection succeeds");
		Tab selectedTab = placer.getSelectedTab();
		assertEquals(1, selectedTab.getStrings().get(0).size(), "Checking string has correct size");
		assertEquals(Music.createPitch(Music.E, 4), ((TabNote)(selectedTab.getStrings().get(0).symbol(0))).getPitch(), "Checking selected note has correct pitch");
		assertEquals(1, selectedTab.getStrings().get(1).size(), "Checking string has correct size");
		assertEquals(Music.createPitch(Music.B, 3), ((TabNote)(selectedTab.getStrings().get(1).symbol(0))).getPitch(), "Checking selected note has correct pitch");
		for(int i = 2; i < 6; i++) assertTrue(selectedTab.getStrings().get(i).isEmpty(), "Checking other strings have no notes");
	}
	
	@Test
	public void place(){
		ArrayList<Selection> placed = new ArrayList<Selection>();
		ArrayList<Selection> removed = new ArrayList<Selection>();
		Tab expect;
		
		// Invalid placement
		paint.setTab(null);
		assertEquals(null, placer.place(3.5, 1.5, 0, 1, false, true, false, false),
				"Checking paint with no tab returns null");
		
		paint.setTab(tab);
		assertEquals(null, placer.place(3.5, 1.5, 0, 1, false, true, false, false),
				"Checking no selected tab in the placer returns null");
		
		// Placing a single TabPosition at an empty location
		// Moving both the note position and string index
		expect = tab.copy();
		expect.placeQuantizedNote(1, 0, 1.5);
		placed.clear();
		removed.clear();
		placed.add(placeCreator(Music.createNote(Music.B, 3), 1.5, 1));
		paint.select(0, 0);
		placeHelper(placed, removed, expect, 3.5, 1.5, 0, 1, false, true, false, false);
		
		// Placing a TabPosition at an empty location, and trying, and failing, to place a TabPosition on an existing note
		// Moving only the string index
		expect = tab.copy();
		expect.placeQuantizedNote(4, 0, 0);
		placed.clear();
		removed.clear();
		placed.add(placeCreator(Music.createNote(Music.A, 2), 0, 4));
		paint.select(0, 5);
		paint.select(1, 5);
		placeHelper(placed, removed, expect, 0, 0, 5, 4, false, true, false, false);

		// Placing notes on separate strings, keeping the pitch
		expect = tab.copy();
		expect.placeQuantizedNote(4, -5, 0.5);
		expect.placeQuantizedNote(3, -5, 1.5);
		placed.clear();
		removed.clear();
		placed.add(placeCreator(Music.createNote(Music.E, 2), 0.5, 4));
		placed.add(placeCreator(Music.createNote(Music.A, 2), 1.5, 3));
		paint.select(0, 5);
		paint.select(0, 4);
		placeHelper(placed, removed, expect, 0, 0.5, 5, 4, true, true, false, false);

		// Placing notes on separate strings, keeping the pitch, and placing back the original notes
		// One note fails to place, the other places
		tab.placeQuantizedNote(4, 0, 0.5);
		expect = tab.copy();
		expect.placeQuantizedNote(3, -5, 1.5);
		expect.getStrings().get(4).remove(1);
		placed.clear();
		removed.clear();
		placed.add(placeCreator(Music.createNote(Music.A, 2), 1.5, 3));
		paint.select(0, 5);
		paint.select(1, 4);
		str4.remove(1);
		placeHelper(placed, removed, expect, 0, 0.5, 5, 4, true, true, false, false);

		// Placing notes where one fails to place, causing it to be replaced, which causes another placement to fail
		// No notes should be placed or removed
		expect = tab.copy();
		paint.select(0, 0);
		paint.select(0, 1);
		placeHelper(expect, 3.5, 3.25, 0, 1, false, true, false, true);
		
		// Same as above, but in the opposite direction
		expect = tab.copy();
		paint.select(0, 1);
		paint.select(0, 2);
		placeHelper(expect, 3.25, 3.5, 2, 1, false, true, false, true);
		
		// A note fails to place, so it's replacement is attempted to be placed, but it's replacement cannot be placed
		expect = tab.copy();
		paint.select(0, 5);
		placeHelper(expect, 0, 1, 5, 5, false, true, false, false);

		// A note fails to place, so it's replacement is attempted to be placed, and its replacement is placed
		expect = tab.copy();
		placed.clear();
		placed.add(placeCreator(Music.createNote(Music.E, 2), 0, 5));
		removed.clear();
		paint.select(0, 5);
		tab.getStrings().get(5).remove(0);
		placeHelper(placed, removed, expect, 0, 1, 5, 5, false, true, false, false);

		// Testing the entire selection is canceled with an invalid move
		expect = tab.copy();
		paint.select(0, 5);
		paint.select(0, 4);
		placeHelper(expect, 0, 1, 5, 5, false, true, false, true);
		
		// Testing selection succeeds with all notes placed
		expect = tab.copy();
		expect.placeQuantizedNote(5, 0, 0.5);
		expect.placeQuantizedNote(4, 0, 1.5);
		placed.clear();
		placed.add(placeCreator(Music.createNote(Music.E, 2), 0.5, 5));
		placed.add(placeCreator(Music.createNote(Music.A, 2), 1.5, 4));
		removed.clear();
		paint.select(0, 5);
		paint.select(0, 4);
		placeHelper(placed, removed, expect, 0, 0.5, 5, 5, false, true, false, true);
		
		// Testing selection fails placing outside of a measure in the negatives
		expect = tab.copy();
		paint.select(0, 5);
		paint.select(0, 4);
		placeHelper(expect, 0, -0.5, 5, 5, false, true, false, true);
		
		// Testing selection fails placing outside of a measure in the positives
		expect = tab.copy();
		paint.select(0, 5);
		paint.select(0, 4);
		placeHelper(expect, 0, 3.75, 5, 5, false, true, false, true);
		
		// Testing selection fails placing above of a measure
		expect = tab.copy();
		paint.select(0, 5);
		paint.select(0, 4);
		placeHelper(expect, 0, 0, 5, 0, false, true, false, true);
		
		// Testing selection fails placing below of a measure
		expect = tab.copy();
		paint.select(0, 5);
		paint.select(0, 4);
		placeHelper(expect, 0, 0, 4, 5, false, true, false, true);
		
		// Testing selection succeeds placing onto the next line
		expect = tab.copy();
		expect.placeQuantizedNote(4, 0, 5);
		expect.placeQuantizedNote(5, 0, 4);
		placed.clear();
		placed.add(placeCreator(Music.createNote(Music.E, 2), 4, 5));
		placed.add(placeCreator(Music.createNote(Music.A, 2), 5, 4));
		removed.clear();
		paint.select(0, 5);
		paint.select(0, 4);
		placeHelper(placed, removed, expect, 0, 4, 5, 5, false, true, false, true);
		
		// Testing selection succeeds placing 2 lines down
		expect = tab.copy();
		expect.placeQuantizedNote(4, 0, 9);
		expect.placeQuantizedNote(5, 0, 8);
		placed.clear();
		placed.add(placeCreator(Music.createNote(Music.A, 2), 9, 4));
		placed.add(placeCreator(Music.createNote(Music.E, 2), 8, 5));
		removed.clear();
		paint.select(0, 5);
		paint.select(0, 4);
		placeHelper(placed, removed, expect, 0, 8, 5, 5, false, true, false, true);
		
		// Placing notes on multiple different lines
		expect = tab.copy();
		expect.placeQuantizedNote(4, 0, 1.5);
		expect.placeQuantizedNote(5, 0, 8.5);
		expect.placeQuantizedNote(5, 0, 9);
		tab.placeQuantizedNote(5, 0, 8.5);
		placed.clear();
		placed.add(placeCreator(Music.createNote(Music.A, 2), 1.5, 4));
		placed.add(placeCreator(Music.createNote(Music.E, 2), 9, 5));
		removed.clear();
		paint.select(4, 5);
		paint.select(0, 4);
		placeHelper(placed, removed, expect, 1, 1.5, 5, 5, false, false, false, true);
		
		// Trying to place notes where one is on the top line, the other would be at the -1 line, the later note fails, the former is placed
		expect = tab.copy();
		expect.placeQuantizedNote(5, 0, 4);
		expect.placeQuantizedNote(5, 0, 0.5);
		tab.placeQuantizedNote(5, 0, 4);
		placed.clear();
		placed.add(placeCreator(Music.createNote(Music.E, 2), 0.5, 5));
		removed.clear();
		paint.select(0, 5);
		paint.select(4, 5);
		placeHelper(placed, removed, expect, 4, 0.5, 5, 5, false, false, false, false);
		
		// Overwriting a note
		tab.getStrings().get(5).remove(0);
		tab.placeQuantizedNote(5, 3, 0);
		expect = tab.copy();
		placed.clear();
		placed.add(placeCreator(Music.createNote(Music.G, 2), 0, 5));
		removed.clear();
		removed.add(placeCreator(Music.createNote(Music.E, 2), 0, 5));
		paint.select(0, 5);
		tab.getStrings().get(5).remove(0);
		tab.placeQuantizedNote(5, 0, 0);
		placeHelper(placed, removed, expect, 0, 0, 5, 5, false, false, true, false);
		
		// Overwriting a note still works when canceling the placement on an invalid placement
		tab.getStrings().get(5).remove(0);
		tab.placeQuantizedNote(5, 3, 0);
		expect = tab.copy();
		placed.clear();
		placed.add(placeCreator(Music.createNote(Music.G, 2), 0, 5));
		removed.clear();
		removed.add(placeCreator(Music.createNote(Music.E, 2), 0, 5));
		paint.select(0, 5);
		tab.getStrings().get(5).remove(0);
		tab.placeQuantizedNote(5, 0, 0);
		placeHelper(placed, removed, expect, 0, 0, 5, 5, false, false, true, true);

		// Notes are removed, and then placed back with cancelInvalid
		expect = tab.copy();
		tab.getStrings().get(5).remove(0);
		tab.placeQuantizedNote(5, 3, 0);
		placed.clear();
		placed.add(placeCreator(Music.createNote(Music.G, 2), 0, 5));
		removed.clear();
		removed.add(placeCreator(Music.createNote(Music.E, 2), 0, 5));
		paint.select(0, 5);
		paint.select(0, 0);
		tab.getStrings().get(5).remove(0);
		tab.placeQuantizedNote(5, 0, 0);
		placeHelper(expect, 0, 1, 5, 5, false, false, true, true);
	}
	
	/**
	 * A utility method for testing different cases of place
	 * @param placed The {@link Selection} objects which should be on the tab, and in the placed list of the event from place
	 * @param removed The {@link Selection} objects which should be in the removed list of the event from place
	 * @param expect The expected state of the tab after the place
	 * @param oldP The old position value for the base
	 * @param newP The new position value for the base
	 * @param oldS The old string index for the base
	 * @param newS The new string index for the base
	 * @param keepPitch Param for place call
	 * @param replaceOriginal Param for place call
	 * @param overwrite Param for place call
	 * @param cancelInvalid Param for place call
	 */
	private void placeHelper(
			ArrayList<Selection> placed,
			ArrayList<Selection> removed,
			Tab expect,
			double oldP, double newP, int oldS, int newS, 
			boolean keepPitch, boolean replaceOriginal,
			boolean overwrite, boolean cancelInvalid){
		
		assertTrue(placer.select(paint.getSelected()), "Checking selection succeeds");
		
		RemovePlaceNotesEvent result = placer.place(oldP, newP, oldS, newS, keepPitch, replaceOriginal, overwrite, cancelInvalid);
		
		assertNotEquals(null, result, "Checking place succeeded");
		
		// Checking correct removed notes
		Assert.containsSize(result.getRemoved(), removed.toArray());
		
		// Checking correct placed notes
		Assert.containsSize(result.getPlaced(), placed.toArray());
		
		assertEquals(expect, tab, "Checking tab is in correct state");
		
		placeReset();
	}
	
	/**
	 * Utility for testing place when the placement should fail and do nothing
	 * @param expect The expected state of the tab after the place
	 * @param oldP The old position value for the base
	 * @param newP The new position value for the base
	 * @param oldS The old string index for the base
	 * @param newS The new string index for the base
	 * @param keepPitch Param for place call
	 * @param replaceOriginal Param for place call
	 * @param overwrite Param for place call
	 * @param cancelInvalid Param for place call
	 */
	private void placeHelper(
			Tab expect,
			double oldP, double newP, int oldS, int newS, 
			boolean keepPitch, boolean replaceOriginal,
			boolean overwrite, boolean cancelInvalid){

		assertTrue(placer.select(paint.getSelected()), "Checking selection succeeds");

		RemovePlaceNotesEvent result = placer.place(oldP, newP, oldS, newS, keepPitch, replaceOriginal, overwrite, cancelInvalid);
		
		assertEquals(null, result, "Checking placement fails");
		assertEquals(expect, tab, "Checking tab is in original state");
		placeReset();
	}
	
	/**
	 * Utility for resetting between tests of place 
	 */
	private void placeReset(){
		tab.clearNotes();
		AbstractTestTabPainter.initNotes(tab);
		paint.clearSelection();
		placer.reset();
	}
	
	/**
	 * A utility method for testing place. Creates a selection based on the parameters
	 * @param pitch The pitch for the note
	 * @param pos The new position of the note
	 * @param stringIndex The string to place the note
	 * @return The selection
	 */
	private Selection placeCreator(int pitch, double pos, int stringIndex){
		return new Selection(new TabPosition(
				new TabNote(pitch), pos),
				strs.get(stringIndex), stringIndex);
	}
	
	@Test
	public void comparePlaceSorter(){
		TabString str = new TabString(new Pitch(0));
		Selection lowPosLowStr = new Selection(TabFactory.modifiedFret(str, 0, 0.0), str, 0);
		Selection highPosHighStr = new Selection(TabFactory.modifiedFret(str, 0, 3.0), str, 3);

		PlaceSorter sort = new PlaceSorter(true, true, false);
		assertEquals(0, sort.compare(lowPosLowStr, null), "Checking comparing null s1 is 0");
		assertEquals(0, sort.compare(null, highPosHighStr), "Checking comparing null s2 is 0");
		
		comparePlaceSortHelper(false, false, false);
		comparePlaceSortHelper(false, false, true);
		comparePlaceSortHelper(false, true, false);
		comparePlaceSortHelper(false, true, true);
		comparePlaceSortHelper(true, false, false);
		comparePlaceSortHelper(true, false, true);
		comparePlaceSortHelper(true, true, false);
		comparePlaceSortHelper(true, true, true);
	}
	
	/**
	 * A helper method for checking that drag sorter compares correctly by checking every case
	 * @param moveRight Same field for new {@link PlaceSorter}
	 * @param moveDown Same field for new {@link PlaceSorter}
	 * @param prioritizeStrings Same field for new {@link PlaceSorter}
	 */
	private void comparePlaceSortHelper(boolean moveRight, boolean moveDown, boolean prioritizeStrings){
		TabString str = new TabString(new Pitch(0));
		Selection lowPosLowStr = new Selection(TabFactory.modifiedFret(str, 0, 0.0), str, 0);
		Selection highPosLowStr = new Selection(TabFactory.modifiedFret(str, 0, 3.0), str, 0);
		Selection lowPosHighStr = new Selection(TabFactory.modifiedFret(str, 0, 0.0), str, 3);
		Selection highPosHighStr = new Selection(TabFactory.modifiedFret(str, 0, 3.0), str, 3);
		
		PlaceSorter sort = new PlaceSorter(moveRight, moveDown, prioritizeStrings);
		assertEquals(0, sort.compare(lowPosLowStr, lowPosLowStr), "Checking objects compare to equal");
		if(moveRight){
			Assert.lessThan(sort.compare(lowPosLowStr, highPosLowStr), 0, "Checking low to high position is negative");
			Assert.greaterThan(sort.compare(highPosLowStr, lowPosLowStr), 0, "Checking high to low position is positive");
			if(moveDown){
				Assert.lessThan(sort.compare(lowPosLowStr, lowPosHighStr), 0, "Checking low to high string is negative");
				Assert.greaterThan(sort.compare(lowPosHighStr, lowPosLowStr), 0, "Checking high to low string is positive");
				if(prioritizeStrings){
					Assert.lessThan(sort.compare(highPosLowStr, lowPosHighStr), 0, "Checking low to high string is negative, with opposing positions");
					Assert.greaterThan(sort.compare(lowPosHighStr, highPosLowStr), 0, "Checking high to low string is positive, with opposing positions");
					
					Assert.lessThan(sort.compare(lowPosLowStr, highPosHighStr), 0, "Checking low to high string is negative, with matching positions");
					Assert.greaterThan(sort.compare(highPosHighStr, lowPosLowStr), 0, "Checking high to low string is positive, with matching positions");
				}
				else{
					Assert.lessThan(sort.compare(lowPosHighStr, highPosLowStr), 0, "Checking low to high position is negative, with opposing strings");
					Assert.greaterThan(sort.compare(highPosLowStr, lowPosHighStr), 0, "Checking high to low position is positive, with opposing strings");
					
					Assert.lessThan(sort.compare(lowPosLowStr, highPosHighStr), 0, "Checking low to high position is negative, with matching strings");
					Assert.greaterThan(sort.compare(highPosHighStr, lowPosLowStr), 0, "Checking high to low position is positive, with matching strings");
				}
			}
			else{
				Assert.greaterThan(sort.compare(lowPosLowStr, lowPosHighStr), 0, "Checking low to high string is positive");
				Assert.lessThan(sort.compare(lowPosHighStr, lowPosLowStr), 0, "Checking high to low string is negative");
				if(prioritizeStrings){
					Assert.greaterThan(sort.compare(highPosLowStr, lowPosHighStr), 0, "Checking low to high string is positive, with opposing positions");
					Assert.lessThan(sort.compare(lowPosHighStr, highPosLowStr), 0, "Checking high to low string is negative, with opposing positions");
					
					Assert.greaterThan(sort.compare(lowPosLowStr, highPosHighStr), 0, "Checking low to high string is positive, with matching positions");
					Assert.lessThan(sort.compare(highPosHighStr, lowPosLowStr), 0, "Checking high to low string is negative, with matching positions");
				}
				else{
					Assert.lessThan(sort.compare(lowPosHighStr, highPosLowStr), 0, "Checking low to high position is negative, with opposing strings");
					Assert.greaterThan(sort.compare(highPosLowStr, lowPosHighStr), 0, "Checking high to low position is positive, with opposing strings");
					
					Assert.lessThan(sort.compare(lowPosLowStr, highPosHighStr), 0, "Checking low to high position is negative, with matching strings");
					Assert.greaterThan(sort.compare(highPosHighStr, lowPosLowStr), 0, "Checking high to low position is positive, with matching strings");
				}
			}
		}
		else{
			Assert.greaterThan(sort.compare(lowPosLowStr, highPosLowStr), 0, "Checking low to high position is positive");
			Assert.lessThan(sort.compare(highPosLowStr, lowPosLowStr), 0, "Checking high to low position is negative");
			if(moveDown){
				Assert.lessThan(sort.compare(lowPosLowStr, lowPosHighStr), 0, "Checking low to high string is negative");
				Assert.greaterThan(sort.compare(lowPosHighStr, lowPosLowStr), 0, "Checking high to low string is positive");
				if(prioritizeStrings){
					Assert.lessThan(sort.compare(highPosLowStr, lowPosHighStr), 0, "Checking low to high string is negative, with opposing positions");
					Assert.greaterThan(sort.compare(lowPosHighStr, highPosLowStr), 0, "Checking high to low string is positive, with opposing positions");
					
					Assert.lessThan(sort.compare(lowPosLowStr, highPosHighStr), 0, "Checking low to high string is negative, with matching positions");
					Assert.greaterThan(sort.compare(highPosHighStr, lowPosLowStr), 0, "Checking high to low string is positive, with matching positions");
				}
				else{
					Assert.greaterThan(sort.compare(lowPosHighStr, highPosLowStr), 0, "Checking low to high position is positive, with opposing strings");
					Assert.lessThan(sort.compare(highPosLowStr, lowPosHighStr), 0, "Checking high to low position is negative, with opposing strings");
					
					Assert.greaterThan(sort.compare(lowPosLowStr, highPosHighStr), 0, "Checking low to high position is positive, with matching strings");
					Assert.lessThan(sort.compare(highPosHighStr, lowPosLowStr), 0, "Checking high to low position is negative, with matching strings");
				}
			}
			else{
				Assert.greaterThan(sort.compare(lowPosLowStr, lowPosHighStr), 0, "Checking low to high string is positive");
				Assert.lessThan(sort.compare(lowPosHighStr, lowPosLowStr), 0, "Checking high to low string is negative");
				if(prioritizeStrings){
					Assert.greaterThan(sort.compare(highPosLowStr, lowPosHighStr), 0, "Checking low to high string is positive, with opposing positions");
					Assert.lessThan(sort.compare(lowPosHighStr, highPosLowStr), 0, "Checking high to low string is negative, with opposing positions");
					
					Assert.greaterThan(sort.compare(lowPosLowStr, highPosHighStr), 0, "Checking low to high string is positive, with matching positions");
					Assert.lessThan(sort.compare(highPosHighStr, lowPosLowStr), 0, "Checking high to low string is negative, with matching positions");
				}
				else{
					Assert.greaterThan(sort.compare(lowPosHighStr, highPosLowStr), 0, "Checking low to high position is positive, with opposing strings");
					Assert.lessThan(sort.compare(highPosLowStr, lowPosHighStr), 0, "Checking high to low position is negative, with opposing strings");
					
					Assert.greaterThan(sort.compare(lowPosLowStr, highPosHighStr), 0, "Checking low to high position is positive, with matching strings");
					Assert.lessThan(sort.compare(highPosHighStr, lowPosLowStr), 0, "Checking high to low position is negative, with matching strings");
				}
			}
		
		}
	}
	
	@Test
	public void sortPlaceSorter(){
		ArrayList<Selection> list;
		PlaceSorter sorter;
		TabString str = new TabString(new Pitch(0));
		Selection s0 = new Selection(TabFactory.modifiedFret(str, 0, 0), str, 0);
		Selection s1 = new Selection(TabFactory.modifiedFret(str, 0, 0), str, 1);
		Selection s2 = new Selection(TabFactory.modifiedFret(str, 0, 3), str, 0);
		Selection s3 = new Selection(TabFactory.modifiedFret(str, 0, 3), str, 1);
		
		// Case of sorting increasing tab position and increasing string index
		sorter = new PlaceSorter(true, true, false);
		list = sortPlaceSorterReset();
		sorter.sort(list);
		Assert.listSame(list, s0, s1, s2, s3);
		
		// Case of sorting increasing tab position and decreasing string index
		sorter = new PlaceSorter(true, false, false);
		list = sortPlaceSorterReset();
		sorter.sort(list);
		Assert.listSame(list, s1, s0, s3, s2);
		
		// Case of sorting decreasing tab position and increasing string index
		sorter = new PlaceSorter(false, true, true);
		list = sortPlaceSorterReset();
		sorter.sort(list);
		Assert.listSame(list, s2, s0, s3, s1);
		
		// Case of sorting decreasing tab position and decreasing string index
		sorter = new PlaceSorter(false, false, true);
		list = sortPlaceSorterReset();
		sorter.sort(list);
		Assert.listSame(list, s3, s1, s2, s0);
	}
	
	/**
	 * Utility for testing DragSorter.sort for resetting the list
	 * @return The list with elements
	 */
	private ArrayList<Selection> sortPlaceSorterReset(){
		TabString str = new TabString(new Pitch(0));
		ArrayList<Selection> list = new ArrayList<Selection>();
		Selection s0 = new Selection(TabFactory.modifiedFret(str, 0, 0), str, 0);
		Selection s1 = new Selection(TabFactory.modifiedFret(str, 0, 0), str, 1);
		Selection s2 = new Selection(TabFactory.modifiedFret(str, 0, 3), str, 0);
		Selection s3 = new Selection(TabFactory.modifiedFret(str, 0, 3), str, 1);
		
		// Adding elements in arbitrary order
		list.add(s2);
		list.add(s1);
		list.add(s3);
		list.add(s0);
		return list;
	}
	
	@AfterEach
	public void end(){}

}
