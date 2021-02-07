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

public class TestRemovePlaceNotesEvent extends AbstractTestTabPainter{

	private RemovePlaceNotesEvent eventList;
	private RemovePlaceNotesEvent eventSingle;
	private RemovePlaceNotesEvent eventOverlap;
	
	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
		SelectionList added = new SelectionList();
		added.add(paint.stringSelection(0, 5));
		added.add(paint.stringSelection(1, 5));
		SelectionList removed = new SelectionList();
		removed.add(paint.stringSelection(2, 5));
		removed.add(paint.stringSelection(3, 5));
		eventList = new RemovePlaceNotesEvent(added, removed);
		
		eventSingle = new RemovePlaceNotesEvent(paint.stringSelection(0, 0), paint.stringSelection(0, 1));

		eventOverlap = new RemovePlaceNotesEvent(
				new Selection(TabFactory.hammerOn(str2, 0, 3), str2, 2),
				new Selection(TabFactory.pullOff(str2, 0, 3), str2, 2));
	}
	
	@Test
	public void undo(){
		str1.clear();
		assertNotEquals(null, str0.findPosition(3.5), "Checking note is there before event");
		assertEquals(null, str1.findPosition(3.25), "Checking note is not there before event");
		assertTrue(eventSingle.undo(paint), "Checking undo succeeds");
		assertEquals(null, str0.findPosition(3.5), "Checking note is now removed");
		assertNotEquals(null, str1.findPosition(3.25), "Checking note is now there");
		
		str5.remove(str5.findPosition(2));
		str5.remove(str5.findPosition(3));
		assertNotEquals(null, str5.findPosition(0), "Checking note is there before event");
		assertNotEquals(null, str5.findPosition(1), "Checking note is there before event");
		assertEquals(null, str5.findPosition(2), "Checking note is not there before event");
		assertEquals(null, str5.findPosition(3), "Checking note is not there before event");
		assertTrue(eventList.undo(paint), "Checking undo succeeds");
		assertEquals(null, str5.findPosition(0), "Checking note is now removed");
		assertEquals(null, str5.findPosition(1), "Checking note is now removed");
		assertNotEquals(null, str5.findPosition(2), "Checking note is now there");
		assertNotEquals(null, str5.findPosition(3), "Checking note is now there");
		
		assertFalse(eventList.undo(paint), "Checking undo fails with original note not in place");
		
		TabPosition p = TabFactory.hammerOn(str2, 0, 3);
		str2.setSymbol(0, p.getSymbol());
		p = str2.findPosition(3);
		assertNotEquals(null, p, "Checking note is there before event");
		assertEquals(ModifierFactory.hammerOn(), p.getSymbol().getModifier(), "Checking modifier is set");
		assertTrue(eventOverlap.undo(paint), "Checking undo succeeds");
		p = str2.findPosition(3);
		assertNotEquals(null, p, "Checking note is still there");
		assertEquals(ModifierFactory.pullOff(), p.getSymbol().getModifier(), "Checking note has pull off modifier from event");
		
		this.setup();
		str1.clear();
		str1.placeQuantizedNote(tab.getTimeSignature(), 1, 3.25);
		assertNotEquals(null, str0.findPosition(3.5), "Checking note is there before event");
		assertNotEquals(null, str1.findPosition(3.25), "Checking note is there before event");
		assertFalse(eventSingle.undo(paint), "Checking undo fails with being unable to place a note");
	}
	
	@Test
	public void redo(){
		str0.clear();
		assertEquals(null, str0.findPosition(3.5), "Checking note is not there before event");
		assertNotEquals(null, str1.findPosition(3.25), "Checking note is there before event");
		assertTrue(eventSingle.redo(paint), "Checking redo succeeds");
		assertNotEquals(null, str0.findPosition(3.5), "Checking note is now there");
		assertEquals(null, str1.findPosition(3.25), "Checking note is now removed");

		str5.remove(str5.findPosition(0));
		str5.remove(str5.findPosition(1));
		assertEquals(null, str5.findPosition(0), "Checking note is not there before event");
		assertEquals(null, str5.findPosition(1), "Checking note is not there before event");
		assertNotEquals(null, str5.findPosition(2), "Checking note is there before event");
		assertNotEquals(null, str5.findPosition(3), "Checking note is there before event");
		assertTrue(eventList.redo(paint), "Checking redo succeeds");
		assertNotEquals(null, str5.findPosition(0), "Checking note is now there");
		assertNotEquals(null, str5.findPosition(1), "Checking note is now there");
		assertEquals(null, str5.findPosition(2), "Checking note is now removed");
		assertEquals(null, str5.findPosition(3), "Checking note is now removed");

		assertFalse(eventList.redo(paint), "Checking redo fails with original note not in place");
		
		TabPosition p = TabFactory.pullOff(str2, 0, 3);
		str2.setSymbol(0, p.getSymbol());
		p = str2.findPosition(3);
		assertNotEquals(null, p, "Checking note is there before event");
		assertEquals(ModifierFactory.pullOff(), p.getSymbol().getModifier(), "Checking modifier is set");
		assertTrue(eventOverlap.redo(paint), "Checking redo succeeds");
		p = str2.findPosition(3);
		assertNotEquals(null, p, "Checking note is still there");
		assertEquals(ModifierFactory.hammerOn(), p.getSymbol().getModifier(), "Checking note has hammer on modifier from event");

		this.setup();
		str0.clear();
		str0.placeQuantizedNote(tab.getTimeSignature(), 1, 3.5);
		assertNotEquals(null, str0.findPosition(3.5), "Checking note is there before event");
		assertNotEquals(null, str1.findPosition(3.25), "Checking note is there before event");
		assertFalse(eventSingle.redo(paint), "Checking undo fails with being unable to place a note");
	}
	
	@AfterEach
	public void end(){}
	
}
