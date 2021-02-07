package appMain.gui.editor.paint.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.editor.paint.AbstractTestTabPainter;
import appMain.gui.editor.paint.Selection;
import appMain.gui.editor.paint.SelectionList;
import tab.ModifierFactory;
import tab.TabFactory;
import tab.TabPosition;
import util.testUtils.Assert;

public class TestPlaceNotesEvent extends AbstractTestTabPainter{

	private PlaceNotesEvent placeOne;
	private PlaceNotesEvent placeMany;
	
	private PlaceNotesEvent emptyEvent;
	
	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
		TabPosition p = TabFactory.hammerOn(str0, 0, 3.5);
		str0.setSymbol(0, p.getSymbol());
		placeOne = new PlaceNotesEvent(new Selection(p, str0, 0));
		
		SelectionList list = new SelectionList();
		list.add(paint.stringSelection(0, 5));
		list.add(paint.stringSelection(1, 5));
		placeMany = new PlaceNotesEvent(list);
		
		emptyEvent = new PlaceNotesEvent(new SelectionList());
	}
	
	@Test
	public void getPlaced(){
		assertEquals(1, placeOne.getPlaced().size(), "Checking size correct");
		// Checking list contains the correct elements
		Assert.contains(placeOne.getPlaced(), paint.stringSelection(0, 0));
		
		assertEquals(2, placeMany.getPlaced().size(), "Checking size correct");
		// Checking list contains the correct elements
		Assert.contains(placeMany.getPlaced(), paint.stringSelection(0, 5));
		Assert.contains(placeMany.getPlaced(), paint.stringSelection(1, 5));
		
		SelectionList list = new SelectionList();
		list.add(paint.stringSelection(0, 0));
		placeOne = new PlaceNotesEvent(list);
		list.add(paint.stringSelection(0, 1));
		assertEquals(2, list.size(), "Checking list was modified");
		assertEquals(1, placeOne.getPlaced().size(), "Checking modifying the list which created the event, doesn't modify the event");
	}
	
	@Test
	public void undo(){
		assertNotEquals(null, str0.findPosition(3.5), "Checking note not removed before undo");
		assertTrue(placeOne.undo(paint), "Checking undo succeeds");
		assertEquals(null, str0.findPosition(3.5), "Checking note removed");

		assertNotEquals(null, str5.findPosition(0), "Checking note not removed before undo");
		assertNotEquals(null, str5.findPosition(1), "Checking note not removed before undo");
		assertTrue(placeMany.undo(paint), "Checking undo succeeds");
		assertEquals(null, str5.findPosition(0), "Checking note removed");
		assertEquals(null, str5.findPosition(1), "Checking note removed");

		assertFalse(placeMany.undo(paint), "Checking undo fails with notes still removed");
		
		assertTrue(emptyEvent.undo(paint), "Checking undo succeeds with empty event");
	}
	
	@Test
	public void redo(){
		tab.clearNotes();
		
		assertEquals(null, str0.findPosition(3.5), "Checking note removed");
		assertTrue(placeOne.redo(paint), "Checking redo succeeds");
		TabPosition p = str0.findPosition(3.5);
		assertNotEquals(null, p, "Checking note added after redo");
		assertEquals(ModifierFactory.hammerOn(), p.getSymbol().getModifier(), "Checking replaced note has correct modifier");
		
		assertEquals(null, str5.findPosition(0), "Checking note removed");
		assertEquals(null, str5.findPosition(1), "Checking note removed");
		assertTrue(placeMany.redo(paint), "Checking redo succeeds");
		assertNotEquals(null, str5.findPosition(0), "Checking note added after redo");
		assertNotEquals(null, str5.findPosition(1), "Checking note added after redo");

		assertFalse(placeMany.redo(paint), "Checking redo fails with notes still in place");
		
		assertTrue(emptyEvent.redo(paint), "Checking redo succeeds with empty event");
	}
	
	@AfterEach
	public void end(){}
	
}
