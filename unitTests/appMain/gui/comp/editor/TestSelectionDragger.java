package appMain.gui.comp.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.comp.editor.SelectionDragger.DragSorter;
import appUtils.ZabAppSettings;
import appUtils.ZabSettings;
import music.Music;
import music.Pitch;
import tab.InstrumentFactory;
import tab.Tab;
import tab.TabFactory;
import tab.TabPosition;
import tab.TabString;
import tab.symbol.TabPitch;
import util.testUtils.Assert;

public class TestSelectionDragger extends AbstractTestTabPainter{

	private SelectionDragger drag;

	private TabPosition pos;
	private double pX;
	private double pY;
	
	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
		drag = new SelectionDragger(paint);

		pos = strs.get(0).get(0);
		pX = 324.0;
		pY = 75.0;
		AbstractTestTabPainter.initZoomedOutCam(cam);
	}
	
	@Test
	public void getPainter(){
		assertEquals(paint, drag.getPainter(), "Checking painter initialized");
	}
	
	@Test
	public void getBaseSelection(){
		assertEquals(null, drag.getBaseSelection(), "Checking base selection null initially");
	}
	
	@Test
	public void setBaseSelection(){
		Selection s = paint.createSelection(3.5, 0);
		drag.setBaseSelection(s);
		assertEquals(s, drag.getBaseSelection(), "Checking base selection set");
	}
	
	@Test
	public void getDragPoint(){
		assertEquals(null, drag.getDragPoint(), "Checking drag point null initially");
	}
	
	@Test
	public void setDragPoint(){
		Point2D.Double p = new Point2D.Double(2, 4);
		drag.setDragPoint(p);
		assertEquals(p, drag.getDragPoint(), "Checking drag point set");
	}
	
	@Test
	public void getAnchorPoint(){
		assertEquals(null, drag.getAnchorPoint(), "Checking anchor point initially null");
	}
	
	@Test
	public void setAnchorPoint(){
		Point2D.Double p = new Point2D.Double(3, 4);
		drag.setAnchorPoint(p);
		assertEquals(p, drag.getAnchorPoint(), "Checking anchor point set");
	}
	
	@Test
	public void getDraggedTab(){
		assertEquals(null, drag.getDraggedTab(), "Checking dragged tab initially null");
	}
	
	@Test
	public void reset(){
		Selection s = paint.createSelection(3.5, 0);
		drag.setBaseSelection(s);
		Point2D.Double p = new Point2D.Double(2, 4);
		drag.setDragPoint(p);
		
		drag.reset();
		assertEquals(null, drag.getBaseSelection(), "Checking base selection null after reset");
		assertEquals(null, drag.getDragPoint(), "Checking drag point null after reset");
		assertEquals(null, drag.getDraggedTab(), "Checking dragged tab null after reset");
		assertEquals(null, drag.getAnchorPoint(), "Checking anchor point null after reset");
	}
	
	@Test
	public void isDragging(){
		assertFalse(drag.isDragging(), "Checking not initially dragging");

		Selection s = paint.createSelection(3.5, 0);
		drag.setBaseSelection(s);
		assertTrue(drag.isDragging(), "Checking dragging with a base selection");
	}
	
	@Test
	public void begin(){
		assertFalse(drag.begin(-1000, -1000), "Checking drag fails to begin with not finding a position");
		assertFalse(drag.begin(pX, pY), "Checking drag fails to begin a not selected position");
		
		paint.select(0, 0);
		assertTrue(drag.begin(pX, pY), "Checking drag begins with a selected position");
		assertEquals(pos, drag.getBaseSelection().getPos(), "Checking correct base position set");
		Tab t = InstrumentFactory.guitarStandard();
		t.placeQuantizedNote(0, 0, 3.5);
		assertEquals(t, drag.getDraggedTab(), "Checking selection tab is correct");
		assertEquals(new Point2D.Double(cam.toCamX(pX), cam.toCamY(pY)), drag.getAnchorPoint(), "Checking anchor point set");
		
		assertFalse(drag.begin(pX, pY), "Checking drag fails to begin with a drag in progress");
	}
	
	@Test
	public void update(){
		paint.select(0, 0);
		assertFalse(drag.update(pX, pY), "Checking update fails with no drag initiated");
		
		drag.begin(pX, pY);
		assertTrue(drag.update(pX, pY), "Checking update happens");
		Point2D.Double p = drag.getDragPoint();
		assertEquals(cam.toCamX(pX), p.getX(), "Checking x set in the drag point");
		assertEquals(cam.toCamY(pY), p.getY(), "Checking y set in the drag point");
		
		assertTrue(drag.update(100, 20), "Checking another update happens");
		p = drag.getDragPoint();
		assertEquals(cam.toCamX(100), p.getX(), "Checking x set in the drag point");
		assertEquals(cam.toCamY(20), p.getY(), "Checking y set in the drag point");
	}
	
	@Test
	public void place(){
		/**
		 * Checking invalid placement scenarios
		 */
		assertFalse(drag.place(pX, pY, false), "Checking place fails with null base selection");
		
		drag.setBaseSelection(paint.stringSelection(0, 0));
		paint.setTab(null);
		assertFalse(drag.place(pX, pY, false), "Checking place fails with null tab");

		paint.setTab(tab);
		assertFalse(drag.place(88.0, 140.0, false), "Checking place fails with invalid x position");
		assertFalse(drag.place(289.0, 27.0, false), "Checking place fails with invalid y position");
		
		/*
		 * Testing scenarios where notes that, during a placement, 
		 * can't be moved are not moved, but notes that can be moved are moved
		 */
		drag.reset();
		ZabSettings settings = ZabAppSettings.get();
		settings.getTabControlMoveDeleteInvalid().set(false);
		settings.getTabControlMoveCancelInvalid().set(false);
		// Moving one note to the left on the same string
		assertTrue(paint.select(0, 0), "Checking note selected");
		assertTrue(drag.begin(pX, pY), "Checking drag begins");
		assertTrue(drag.place(250.0, 75.0, false), "Checking note moved");
		assertEquals(2, str0.get(0).getPos(), "Checking note's position moved");
		
		// Moving a line of notes up, all 4 notes on the low E string, where the base note moves to an empty position
		paint.clearSelection();
		drag.reset();
		assertTrue(paint.selectLine(149.0, 200.0, false), "Checking line selection begins");
		assertTrue(paint.selectLine(299.0, 202.0, false), "Checking line selection ends");
		assertTrue(drag.begin(149.0, 200.0), "Checking drag begins");
		assertTrue(drag.place(151.0, 174.0, true), "Checking moving a line of notes with the base note going to an empty space");
		// Checking notes moved to A string
		assertEquals(0, str4.get(0).getPos(), "Checking position of note");
		assertEquals(Music.createPitch(Music.E, 2), ((TabPitch)str4.get(0).getSymbol()).getPitch(), "Checking pitch of note");
		assertEquals(1, str4.get(1).getPos(), "Checking position of note");
		assertEquals(Music.createPitch(Music.A, 2), ((TabPitch)str4.get(1).getSymbol()).getPitch(), "Checking pitch of note");
		assertEquals(2, str4.get(2).getPos(), "Checking position of note");
		assertEquals(Music.createPitch(Music.E, 2), ((TabPitch)str4.get(2).getSymbol()).getPitch(), "Checking pitch of note");
		assertEquals(3, str4.get(3).getPos(), "Checking position of note");
		assertEquals(Music.createPitch(Music.E, 2), ((TabPitch)str4.get(3).getSymbol()).getPitch(), "Checking pitch of note");
		// Checking note remains on E string
		assertEquals(1, str5.get(0).getPos(), "Checking position of note remaining on E string");
		assertEquals(Music.createPitch(Music.E, 2), ((TabPitch)str5.get(0).getSymbol()).getPitch(), "Checking pitch of note");
		
		// Checking moving notes from separate strings to a new place, and the base position is moved to an occupied space.
		resetPlace();
		assertTrue(paint.select(0, 4), "Checking selection succeeds");
		assertTrue(paint.select(1, 5), "Checking selection succeeds");
		assertTrue(drag.begin(201.0, 201.0), "Checking drag begins");
		assertTrue(drag.place(248.0, 196.0, true), "Checking moving notes from separate strings to a new place, and the base position is moved to an occupied space.");
		// Checking A string note moved
		assertEquals(2, str4.get(0).getPos(), "Checking position of note");
		assertEquals(Music.createPitch(Music.A, 2), ((TabPitch)str4.get(0).getSymbol()).getPitch(), "Checking pitch of note");
		// Checking low E string note not moved
		assertEquals(1, str5.get(1).getPos(), "Checking position of note");
		assertEquals(Music.createPitch(Music.E, 2), ((TabPitch)str5.get(1).getSymbol()).getPitch(), "Checking pitch of note");

		// The note is moved to the position of the other moving note
		resetPlace();
		assertTrue(paint.select(0, 2), "Checking selection succeeds");
		assertTrue(paint.select(0, 3), "Checking selection succeeds");
		// Beginning at note 0 2
		assertTrue(drag.begin(298.0, 128.0), "Checking drag begins");
		// Dragging to note 0 3
		assertFalse(drag.place(250.0, 151.0, true), "Checking moving notes to where other notes in the selection to drag exist, and no notes move.");
		assertEquals(3, str2.get(0).getPos(), "Checking position of note unchanged");
		assertEquals(2, str3.get(0).getPos(), "Checking position of note unchanged");
		
		/*
		 * Testing scenarios where notes that, during a placement, 
		 * can't be moved are deleted, but notes that can be moved are moved
		 */
		settings.getTabControlMoveDeleteInvalid().set(true);
		
		// Moving notes on different strings, and base is moving to an empty spot
		resetPlace();
		assertTrue(paint.select(0, 4), "Checking selection succeeds");
		assertTrue(paint.select(1, 5), "Checking selection succeeds");
		assertTrue(drag.begin(201.0, 201.0), "Checking drag begins");
		assertTrue(drag.place(248.0, 196.0, true), "Checking moving notes from separate strings to a new place, and the base position is moved to an occupied space.");
		// Checking A string note moved
		assertEquals(2, str4.get(0).getPos(), "Checking position of note");
		assertEquals(Music.createPitch(Music.A, 2), ((TabPitch)str4.get(0).getSymbol()).getPitch(), "Checking pitch of note");
		// Checking low E string note deleted
		assertEquals(3, str5.size(), "Checking string has one removed note");
		assertEquals(2, str5.get(1).getPos(), "Checking position of note");
		assertEquals(Music.createPitch(Music.E, 2), ((TabPitch)str5.get(1).getSymbol()).getPitch(), "Checking pitch of note");
		
		/*
		 * Testing scenarios where during a placement, if any note cannot be moved, 
		 * then none move
		 */
		settings.getTabControlMoveCancelInvalid().set(true);
		
		// Moving notes on different strings, and base is moving to an occupied spot
		resetPlace();
		assertTrue(paint.select(0, 4), "Checking selection succeeds");
		assertTrue(paint.select(1, 5), "Checking selection succeeds");
		assertTrue(drag.begin(201.0, 201.0), "Checking drag begins");
		assertFalse(drag.place(248.0, 196.0, true), "Checking moving notes from separate strings to a new place, but the entire selection cannot be moved, so nothing moves");
		// Checking A string note not moved
		assertEquals(1, str4.get(0).getPos(), "Checking position of note");
		assertEquals(Music.createPitch(Music.A, 2), ((TabPitch)str4.get(0).getSymbol()).getPitch(), "Checking pitch of note");
		// Checking low E string note not moved
		assertEquals(0, str5.get(0).getPos(), "Checking position of note");
		assertEquals(1, str5.get(1).getPos(), "Checking position of note");
		assertEquals(2, str5.get(2).getPos(), "Checking position of note");
		assertEquals(3, str5.get(3).getPos(), "Checking position of note");
		
		// Moving notes on different strings, moving to an empty space
		resetPlace();
		assertTrue(paint.select(0, 2), "Checking selection succeeds");
		assertTrue(paint.select(0, 3), "Checking selection succeeds");
		assertTrue(drag.begin(298.0, 124.0), "Checking drag begins");
		assertTrue(drag.place(276.0, 100.0, true), "Checking moving notes from separate strings to a new line and position");
		// Checking G string note not moved
		assertEquals(2.5, str1.get(0).getPos(), "Checking position of note");
		assertEquals(Music.createPitch(Music.G, 3), ((TabPitch)str1.get(0).getSymbol()).getPitch(), "Checking pitch of note");
		// Checking D string note not moved
		assertEquals(1.5, str2.get(0).getPos(), "Checking position of note");
		assertEquals(Music.createPitch(Music.D, 3), ((TabPitch)str2.get(0).getSymbol()).getPitch(), "Checking pitch of note");
		assertTrue(str3.isEmpty(), "Checking no notes remain on the D string");
		
		// Moving many notes to a different line
		resetPlace();
		assertTrue(paint.select(0, 0), "Checking selection succeeds");
		assertTrue(paint.select(0, 1), "Checking selection succeeds");
		assertTrue(paint.select(0, 2), "Checking selection succeeds");
		assertTrue(drag.begin(299.0, 123.0), "Checking drag begins");
		assertTrue(drag.place(261.0, 775.0, true), "Checking moving multiple notes to a new line.");
		assertTrue(str0.isEmpty(), "Checking no notes on high E string");
		assertEquals(6.75, str1.get(0).getPos(), "Checking position of note");
		assertEquals(6.5, str2.get(0).getPos(), "Checking position of note");
		assertEquals(6.25, str3.get(1).getPos(), "Checking position of note");
		
		// Checking cases of moving between strings
		settings.getTabControlMoveCancelInvalid().set(false);
		settings.getTabControlMoveDeleteInvalid().set(false);
		
		// Moving multiple notes on different lines
		resetPlace();
		AbstractTestTabPainter.initAditionalNotes(tab);
		// First note on B string, first line of tab
		assertTrue(paint.select(paint.findPosition(311.0, 101.0)), "Checking selection succeeds");
		// Second note on B string, second line of tab
		assertTrue(paint.select(paint.findPosition(313.0, 720.0)), "Checking selection succeeds");
		assertTrue(drag.begin(313.0, 727.0), "Checking drag begins");
		assertTrue(drag.place(314.0, 748.0, true), "Checking moving multiple notes on different lines.");
		assertTrue(str1.isEmpty(), "Checking notes removed from B string");
		assertEquals(3.25, str2.get(1).getPos(), "Checking position of note");
		assertEquals(7.25, str2.get(3).getPos(), "Checking position of note");
		
		// Case of trying to move notes into the negative positions, and they stay in the line
		resetPlace();
		AbstractTestTabPainter.initAditionalNotes(tab);
		assertTrue(paint.select(paint.findPosition(149.0, 200.0)), "Checking selection succeeds");
		assertTrue(paint.select(paint.findPosition(200.0, 199.0)), "Checking selection succeeds");
		assertTrue(paint.select(paint.findPosition(200.0, 174.0)), "Checking selection succeeds");
		assertTrue(paint.select(paint.findPosition(249.0, 151.0)), "Checking selection succeeds");
		assertTrue(drag.begin(249.0, 151.0), "Checking drag begins");
		assertTrue(drag.place(148.0, 150.0, true), "Checking moving notes to negative positions.");
		assertEquals(0, str3.get(0).getPos(), "Checking position of note");
		assertEquals(1, str4.get(0).getPos(), "Checking position of note");
		assertEquals(0, str5.get(0).getPos(), "Checking position of note");
		assertEquals(1, str5.get(1).getPos(), "Checking position of note");
		
		// Case of moving notes on multiple lines of tab all onto new lines of tab
		resetPlace();
		AbstractTestTabPainter.initAditionalNotes(tab);
		assertTrue(paint.select(paint.findPosition(150.0, 201.0)), "Checking selection succeeds");
		assertTrue(paint.select(paint.findPosition(148.0, 826.0)), "Checking selection succeeds");
		assertTrue(drag.begin(150.0, 200.0), "Checking drag begins");
		assertTrue(drag.place(152.0, 800.0, true), "Checking moving notes along multiple lines.");
		assertEquals(1, str5.get(0).getPos(), "Checking position of note");
		assertEquals(2, str5.get(1).getPos(), "Checking position of note");
		assertEquals(3, str5.get(2).getPos(), "Checking position of note");
		assertEquals(5, str5.get(3).getPos(), "Checking position of note");
		assertEquals(6, str5.get(4).getPos(), "Checking position of note");
		assertEquals(7, str5.get(5).getPos(), "Checking position of note");
		assertEquals(1, str4.get(0).getPos(), "Checking position of note");
		assertEquals(4, str4.get(1).getPos(), "Checking position of note");
		assertEquals(5, str4.get(2).getPos(), "Checking position of note");
		assertEquals(8, str4.get(3).getPos(), "Checking position of note");
		
		// Putting settings back to normal for the test of the tests in this test case file
		AbstractTestTabPainter.init();
	}
	
	/**
	 * Helper method for testing place, resets all of the data needed to the default state
	 */
	private void resetPlace(){
		tab.clearNotes();
		AbstractTestTabPainter.initNotes(tab);
		paint.clearSelection();
		drag.reset();
	}
	
	@Test
	public void draw(){
		assertFalse(drag.draw(), "Checking draw fails when not dragging");
		
		paint.select(0, 0);
		drag.setBaseSelection(paint.stringSelection(0, 0));
		assertFalse(drag.draw(), "Checking draw fails with no selected tab");

		drag.reset();
		paint.select(0, 0);
		drag.begin(pX, pY);
		drag.setDragPoint(null);
		assertFalse(drag.draw(), "Checking draw fails with no drag point");
		
		drag.reset();
		paint.select(0, 0);
		drag.begin(pX, pY);
		drag.update(pX, pY);
		drag.setAnchorPoint(null);
		assertFalse(drag.draw(), "Checking draw fails with no anchor point");
		
		drag.reset();
		paint.select(0, 0);
		drag.begin(pX, pY);
		drag.update(pX, pY);
		assertTrue(drag.draw(), "Checking draw succeeds with all objects set");
	}
	
	@Test
	public void compareDragSorter(){
		TabString str = new TabString(new Pitch(0));
		Selection lowPosLowStr = new Selection(TabFactory.modifiedFret(str, 0, 0.0), str, 0);
		Selection highPosLowStr = new Selection(TabFactory.modifiedFret(str, 0, 3.0), str, 0);
		Selection lowPosHighStr = new Selection(TabFactory.modifiedFret(str, 0, 0.0), str, 3);
		Selection highPosHighStr = new Selection(TabFactory.modifiedFret(str, 0, 3.0), str, 3);

		DragSorter sort = new DragSorter(true, true, false);
		assertEquals(0, sort.compare(lowPosLowStr, null), "Checking comparing null s1 is 0");
		assertEquals(0, sort.compare(null, highPosHighStr), "Checking comparing null s2 is 0");
		
		highPosLowStr = new Selection(null, str, 0);
		assertEquals(0, sort.compare(highPosLowStr, highPosHighStr), "Checking comparing null TabPosition in s1 is 0");
		
		lowPosHighStr = new Selection(null, str, 3);
		assertEquals(0, sort.compare(lowPosLowStr, lowPosHighStr), "Checking comparing null TabPosition in s2 is 0");
		
		compareDragSortHelper(false, false, false);
		compareDragSortHelper(false, false, true);
		compareDragSortHelper(false, true, false);
		compareDragSortHelper(false, true, true);
		compareDragSortHelper(true, false, false);
		compareDragSortHelper(true, false, true);
		compareDragSortHelper(true, true, false);
		compareDragSortHelper(true, true, true);
	}
	
	/**
	 * A helper method for checking that drag sorter compares correctly by checking every case
	 * @param moveRight Same field for new {@link DragSorter}
	 * @param moveDown Same field for new {@link DragSorter}
	 * @param prioritizeStrings Same field for new {@link DragSorter}
	 */
	private void compareDragSortHelper(boolean moveRight, boolean moveDown, boolean prioritizeStrings){
		TabString str = new TabString(new Pitch(0));
		Selection lowPosLowStr = new Selection(TabFactory.modifiedFret(str, 0, 0.0), str, 0);
		Selection highPosLowStr = new Selection(TabFactory.modifiedFret(str, 0, 3.0), str, 0);
		Selection lowPosHighStr = new Selection(TabFactory.modifiedFret(str, 0, 0.0), str, 3);
		Selection highPosHighStr = new Selection(TabFactory.modifiedFret(str, 0, 3.0), str, 3);
		
		DragSorter sort = new DragSorter(moveRight, moveDown, prioritizeStrings);
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
	public void sortDragSorter(){
		ArrayList<Selection> list;
		DragSorter sorter;
		TabString str = new TabString(new Pitch(0));
		Selection s0 = new Selection(TabFactory.modifiedFret(str, 0, 0), str, 0);
		Selection s1 = new Selection(TabFactory.modifiedFret(str, 0, 0), str, 1);
		Selection s2 = new Selection(TabFactory.modifiedFret(str, 0, 3), str, 0);
		Selection s3 = new Selection(TabFactory.modifiedFret(str, 0, 3), str, 1);
		
		// Case of sorting increasing tab position and increasing string index
		sorter = new DragSorter(true, true, false);
		list = sortDragSorterReset();
		sorter.sort(list);
		Assert.listSame(list, s0, s1, s2, s3);
		
		// Case of sorting increasing tab position and decreasing string index
		sorter = new DragSorter(true, false, false);
		list = sortDragSorterReset();
		sorter.sort(list);
		Assert.listSame(list, s1, s0, s3, s2);
		
		// Case of sorting decreasing tab position and increasing string index
		sorter = new DragSorter(false, true, true);
		list = sortDragSorterReset();
		sorter.sort(list);
		Assert.listSame(list, s2, s0, s3, s1);
		
		// Case of sorting decreasing tab position and decreasing string index
		sorter = new DragSorter(false, false, true);
		list = sortDragSorterReset();
		sorter.sort(list);
		Assert.listSame(list, s3, s1, s2, s0);
	}
	
	/**
	 * Utility for testing DragSorter.sort for resetting the list
	 * @return The list with elements
	 */
	private ArrayList<Selection> sortDragSorterReset(){
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
