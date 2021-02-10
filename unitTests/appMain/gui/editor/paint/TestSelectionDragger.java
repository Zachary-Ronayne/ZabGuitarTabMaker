package appMain.gui.editor.paint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.editor.paint.event.EditorEventStack;
import appUtils.ZabAppSettings;
import appUtils.settings.ZabSettings;
import music.Music;
import tab.InstrumentFactory;
import tab.Tab;
import tab.TabPosition;
import tab.symbol.TabNote;
import tab.symbol.TabPitch;

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
	public void reset(){
		Selection s = paint.createSelection(3.5, 0);
		drag.setBaseSelection(s);
		Point2D.Double p = new Point2D.Double(2, 4);
		drag.setDragPoint(p);
		
		drag.reset();
		assertEquals(null, drag.getBaseSelection(), "Checking base selection null after reset");
		assertEquals(null, drag.getDragPoint(), "Checking drag point null after reset");
		assertEquals(null, drag.getSelectedTab(), "Checking dragged tab null after reset");
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
		assertEquals(t, drag.getSelectedTab(), "Checking selection tab is correct");
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
		// Checking invalid placement scenarios
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
		settings.control().getMoveOverwrite().set(false);
		settings.control().getMoveDeleteInvalid().set(false);
		settings.control().getMoveCancelInvalid().set(false);
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
		settings.control().getMoveDeleteInvalid().set(true);
		
		// Moving notes on different strings, and base is moving to an occupied space
		resetPlace();
		assertTrue(paint.select(0, 4), "Checking selection succeeds");
		assertTrue(paint.select(1, 5), "Checking selection succeeds");
		// Beginning selection on string 5, tab pos 1
		assertTrue(drag.begin(201.0, 201.0), "Checking drag begins");
		// Placing on string 5, tab pos 2
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
		settings.control().getMoveCancelInvalid().set(true);
		
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
		settings.control().getMoveCancelInvalid().set(false);
		settings.control().getMoveDeleteInvalid().set(false);
		
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
		
		// Checking case of undo recorded
		this.setup();
		EditorEventStack stack = paint.getUndoStack();
		// Moving a line of notes up, all 4 notes on the low E string, where the base note moves to an empty position
		paint.clearSelection();
		drag.reset();
		stack.markSaved();
		Tab oldTab = tab.copy();
		assertTrue(stack.isEmpty(), "Checking stack has no events");
		assertTrue(stack.isSaved(), "Checking stack is saved");
		assertTrue(paint.selectLine(149.0, 200.0, false), "Checking line selection begins");
		assertTrue(paint.selectLine(299.0, 202.0, false), "Checking line selection ends");
		assertTrue(drag.begin(149.0, 200.0), "Checking drag begins");
		assertTrue(drag.place(151.0, 174.0, true, true), "Checking moving a line of notes with the base note going to an empty space");
		
		// Checking undo was recorded, and performs the actions correctly
		assertFalse(stack.isEmpty(), "Checking stack has an event");
		assertFalse(stack.isSaved(), "Checking stack is no longer saved");
		assertNotEquals(oldTab, tab, "Checking original tab state is different from current state");
		assertTrue(paint.undo(), "Checking undo succeeded");
		assertEquals(0, stack.undoSize(), "Checking stack has no events");
		assertFalse(stack.isSaved(), "Checking stack is still not saved");
		assertEquals(oldTab, tab, "Checking original tab state was restored");
		
		// Checking case of undo recorded, but nothing is moved
		this.setup();
		stack = paint.getUndoStack();
		// Moving a line of notes up, all 4 notes on the low E string, where the base note moves to an empty position
		paint.clearSelection();
		drag.reset();
		stack.markSaved();
		oldTab = tab.copy();
		assertTrue(stack.isEmpty(), "Checking stack has no events");
		assertTrue(stack.isSaved(), "Checking stack is saved");
		assertTrue(paint.selectLine(149.0, 200.0, false), "Checking line selection begins");
		assertTrue(paint.selectLine(299.0, 202.0, false), "Checking line selection ends");
		assertTrue(drag.begin(149.0, 200.0), "Checking drag begins");
		tab.clearNotes();
		Tab expect = tab.copy();
		expect.placeQuantizedNote(4, -5, 0);
		expect.placeQuantizedNote(4, -5, 1);
		expect.placeQuantizedNote(4, -5, 2);
		expect.placeQuantizedNote(4, -5, 3);
		assertTrue(drag.place(151.0, 174.0, true, true), "Checking place still happens with no notes in the tab");
		assertEquals(expect, tab, "Checking notes placed");
		// Checking undo was recorded, the placement
		assertFalse(stack.isEmpty(), "Checking stack has events");
		assertFalse(stack.isSaved(), "Checking stack not saved");
		assertTrue(paint.undo(), "Checking undo succeeds");
		assertTrue(tab.isEmpty(), "Checking tab now has no notes after undo");
		
		settings.control().getMoveOverwrite().set(true);
		tab.clearNotes();
		AbstractTestTabPainter.initNotes(tab);
		drag.reset();
		assertTrue(paint.select(0, 1), "Checking selection succeeds");
		assertTrue(drag.begin(311.0, 101.0), "Checking drag begins");
		assertTrue(drag.place(pX, pY, true), "Checking drag places");
		assertTrue(str1.isEmpty(), "Checking string no longer has notes");
		assertEquals(Music.createPitch(Music.B, 3), ((TabNote)(str0.get(0).getSymbol())).getPitch(), "Checking correct pitch moved");
		
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
	
	@AfterEach
	public void end(){}
	
}
