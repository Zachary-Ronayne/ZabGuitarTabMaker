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

public class TestRemoveNotesEvent extends AbstractTestTabPainter{

	private RemoveNotesEvent removeOne;
	private RemoveNotesEvent removeMany;
	
	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
		TabPosition p = TabFactory.hammerOn(str0, 0, 3.5);
		str0.setSymbol(0, p.getSymbol());
		removeOne = new RemoveNotesEvent(new Selection(p, str0, 0));
		
		SelectionList list = new SelectionList();
		list.add(paint.stringSelection(0, 5));
		list.add(paint.stringSelection(1, 5));
		removeMany = new RemoveNotesEvent(list);
	}
	
	@Test
	public void undo(){
		tab.clearNotes();
		
		assertEquals(null, str0.findPosition(3.5), "Checking note not placed before undo");
		assertTrue(removeOne.undo(paint), "Checking undo succeeds");
		TabPosition p = str0.findPosition(3.5);
		assertNotEquals(null, p, "Checking note placed");
		assertEquals(ModifierFactory.hammerOn(), p.getSymbol().getModifier(), "Checking correct modifier on placed note");
		
		assertEquals(null, str5.findPosition(0), "Checking note not placed before undo");
		assertEquals(null, str5.findPosition(1), "Checking note not placed before undo");
		assertTrue(removeMany.undo(paint), "Checking undo succeeds");
		assertNotEquals(null, str5.findPosition(0), "Checking note placed");
		assertNotEquals(null, str5.findPosition(1), "Checking note placed");
		
		assertFalse(removeMany.undo(paint), "Checking undo fails with notes still in place");
	}
	
	@Test
	public void redo(){
		assertNotEquals(null, str0.findPosition(3.5), "Checking note placed");
		assertTrue(removeOne.redo(paint), "Checking redo succeeds");
		assertEquals(null, str0.findPosition(3.5), "Checking note removed after redo");

		assertNotEquals(null, str5.findPosition(0), "Checking note placed");
		assertNotEquals(null, str5.findPosition(1), "Checking note placed");
		assertTrue(removeMany.redo(paint), "Checking redo succeeds");
		assertEquals(null, str5.findPosition(0), "Checking note removed after redo");
		assertEquals(null, str5.findPosition(1), "Checking note removed after redo");

		assertFalse(removeMany.redo(paint), "Checking redo fails with notes already removed");
	}
	
	@AfterEach
	public void end(){}
	
}
