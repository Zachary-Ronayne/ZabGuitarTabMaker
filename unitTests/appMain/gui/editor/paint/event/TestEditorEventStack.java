package appMain.gui.editor.paint.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.editor.paint.AbstractTestTabPainter;
import appUtils.ZabAppSettings;
import settings.SettingInt;

public class TestEditorEventStack extends AbstractTestTabPainter{

	private EditorEventStack stack;
	
	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
		stack = new EditorEventStack(paint);
		ZabAppSettings.get().paint().getMaxUndo().set(100);
	}
	
	@Test
	public void markSaved(){
		stack.markSaved();
		assertTrue(stack.isSaved(), "Checking saved after marking");
	}
	
	@Test
	public void markNotSaved(){
		stack.markNotSaved();
		assertFalse(stack.isSaved(), "Checking not saved after marking");
	}
	
	@Test
	public void isSaved(){
		assertFalse(stack.isSaved(), "Checking not saved by default");
	}
	
	@Test
	public void isEmpty(){
		assertTrue(stack.isEmpty(), "Checking the stack is empty by default");
		
		stack.addEvent(new DummyEditorEvent());
		assertFalse(stack.isEmpty(), "Checking the stack is not empty with an event in the undo stack");
		
		stack.undo();
		assertFalse(stack.isEmpty(), "Checking the stack is not empty with an event in the redo stack");
		
		stack.clearStack();
		assertTrue(stack.isEmpty(), "Checking cleared stack is empty");
	}
	
	/** Test cases for size, undoSize, and redoSize */
	@Test
	public void size(){
		assertEquals(0, stack.size(), "Checking size initially 0");
		assertEquals(0, stack.undoSize(), "Checking size initially 0");
		assertEquals(0, stack.redoSize(), "Checking size initially 0");
		
		stack.addEvent(new DummyEditorEvent());
		assertEquals(1, stack.size(), "Checking size increased");
		assertEquals(1, stack.undoSize(), "Checking undo size increased");
		assertEquals(0, stack.redoSize(), "Checking redo size is still 0");
		
		stack.addEvent(new DummyEditorEvent());
		assertEquals(2, stack.size(), "Checking size increased");
		assertEquals(2, stack.undoSize(), "Checking undo size increased");
		assertEquals(0, stack.redoSize(), "Checking redo size is still 0");
		
		stack.undo();
		assertEquals(2, stack.size(), "Checking size remains the same after undo");
		assertEquals(1, stack.undoSize(), "Checking undo size decreased");
		assertEquals(1, stack.redoSize(), "Checking redo size increased");
		
		stack.addEvent(new DummyEditorEvent());
		assertEquals(2, stack.size(), "Checking size stays the same after clearing redo");
		assertEquals(2, stack.undoSize(), "Checking undo size increased");
		assertEquals(0, stack.redoSize(), "Checking redo stack cleared after adding a new event");
		
		stack.undo();
		assertEquals(2, stack.size(), "Checking size remains the same after undo");
		assertEquals(1, stack.undoSize(), "Checking undo size decreased");
		assertEquals(1, stack.redoSize(), "Checking redo size increased");
		
		stack.undo();
		assertEquals(2, stack.size(), "Checking size remains the same after undo");
		assertEquals(0, stack.undoSize(), "Checking undo size decreased");
		assertEquals(2, stack.redoSize(), "Checking redo size increased");
		
		stack.undo();
		assertEquals(2, stack.size(), "Checking size remains the same after failed undo");
		assertEquals(0, stack.undoSize(), "Checking undo size unchanged");
		assertEquals(2, stack.redoSize(), "Checking redo size unchanged");
		
		stack.redo();
		assertEquals(2, stack.size(), "Checking size remains the same after redo");
		assertEquals(1, stack.undoSize(), "Checking undo size increased");
		assertEquals(1, stack.redoSize(), "Checking redo size decreased");
		
		stack.redo();
		assertEquals(2, stack.size(), "Checking size remains the same after redo");
		assertEquals(2, stack.undoSize(), "Checking undo size increased");
		assertEquals(0, stack.redoSize(), "Checking redo size decreased");
		
		stack.redo();
		assertEquals(2, stack.size(), "Checking size remains the same after failed redo");
		assertEquals(2, stack.undoSize(), "Checking undo size unchanged");
		assertEquals(0, stack.redoSize(), "Checking redo size unchanged");

		stack.undo();
		stack.undo();
		assertEquals(2, stack.size(), "Checking size remains the same after undoing");
		assertEquals(0, stack.undoSize(), "Checking undo size now 0");
		assertEquals(2, stack.redoSize(), "Checking redo size now has all both events");
		
		stack.addEvent(new DummyEditorEvent());
		assertEquals(1, stack.size(), "Checking size now is 1 after removing all redo events");
		assertEquals(1, stack.undoSize(), "Checking undo size increased");
		assertEquals(0, stack.redoSize(), "Checking redo cleared after adding new event");
		
		stack.clearStack();
		assertEquals(0, stack.size(), "Checking size zero after clear");
		assertEquals(0, stack.undoSize(), "Checking size zero after clear");
		assertEquals(0, stack.redoSize(), "Checking size zero after clear");
	}
	
	@Test
	public void clearStack(){
		stack.addEvent(new DummyEditorEvent());
		assertFalse(stack.isEmpty(), "Checking the stack is not empty after adding an event");

		stack.addEvent(new DummyEditorEvent());
		stack.addEvent(new DummyEditorEvent());
		stack.undo();
		assertEquals(2, stack.undoSize(), "Checking undo has elements");
		assertEquals(1, stack.redoSize(), "Checking redo has elements");
		stack.clearStack();
		assertEquals(0, stack.undoSize(), "Checking undo has no elements after clear");
		assertEquals(0, stack.redoSize(), "Checking redo has no elements after clear");
		assertTrue(stack.isEmpty(), "Checking the stack is empty after clearing it");
	}
	
	@Test
	public void contains(){
		EditorEvent e1 = new DummyEditorEvent();
		EditorEvent e2 = new PlaceNotesEvent(paint.stringSelection(0, 0));
		EditorEvent e3 = new PlaceNotesEvent(paint.stringSelection(0, 1));

		stack.addEvent(e1);
		stack.addEvent(e2);
		
		assertTrue(stack.contains(e1), "Checking contains event");
		assertTrue(stack.contains(e2), "Checking contains event");
		assertFalse(stack.contains(e3), "Checking does not contain event which was not added");
		
		stack.undo();
		assertTrue(stack.contains(e1), "Checking contains event");
		assertTrue(stack.contains(e2), "Checking does not contains undone event");
		
		stack.addEvent(new DummyEditorEvent());
		stack.undo();
		assertFalse(stack.contains(e2), "Checking does not contain removed event");
		
		stack.undo();
		stack.addEvent(new DummyEditorEvent());
		assertFalse(stack.contains(e1), "Checking does not contain removed event");
	}
	
	@Test
	public void addEvent(){
		stack.markSaved();
		assertTrue(stack.isSaved(), "Checking is saved");
	
		assertFalse(stack.undo(), "Checking an undo action cannot be performed");
		assertTrue(stack.addEvent(new DummyEditorEvent()), "Checking event added");
		assertFalse(stack.isSaved(), "Checking not saved after event added");
		assertTrue(stack.undo(), "Checking an undo action can be performed");
		
		stack.clearStack();
		SettingInt maxStack = ZabAppSettings.get().paint().getMaxUndo();
		maxStack.set(0);
		assertFalse(stack.addEvent(new DummyEditorEvent()), "Checking not added with no stack size");
		
		maxStack.set(2);
		PlaceNotesEvent place = new PlaceNotesEvent(paint.stringSelection(0, 0));
		assertTrue(stack.addEvent(place), "Checking event added");
		assertTrue(stack.addEvent(new DummyEditorEvent()), "Checking second event added");
		assertFalse(stack.addEvent(new DummyEditorEvent()), "Checking third event added, but a previous event was removed");
		assertFalse(stack.contains(place), "Checking event at the end of the stack is removed");
		
		maxStack.set(-1);
		for(int i = 0; i < 100; i++) assertTrue(stack.addEvent(new DummyEditorEvent()), "Checking event added with negative maximum for unlimited");
		assertEquals(102, stack.size(), "Checking all events added");
		
		maxStack.set(1);
		stack.addEvent(place);
		assertEquals(1, stack.size(), "Checking number of events reduced to 1");

		maxStack.set(100);
		for(int i = 0; i < 110; i++) stack.addEvent(new DummyEditorEvent());
		assertEquals(100, stack.size(), "Checking now has 100 events after trying to add more than the max of 100");
		assertEquals(100, stack.undoSize(), "Checking stack has 100 undo events");
		assertEquals(0, stack.redoSize(), "Checking stack has no redo events");
		
		for(int i = 0; i < 100; i++) stack.undo();
		assertEquals(100, stack.size(), "Checking sill has 100 events");
		assertEquals(0, stack.undoSize(), "Checking stack has no undo events");
		assertEquals(100, stack.redoSize(), "Checking stack has 100 redo events");
		
		stack.addEvent(new DummyEditorEvent());
		assertEquals(1, stack.size(), "Checking now has 1 event");
		assertEquals(1, stack.undoSize(), "Checking stack has 1 undo event");
		assertEquals(0, stack.redoSize(), "Checking stack has no redo events");
	}

	@Test
	public void undo(){
		assertFalse(stack.undo(), "Checking undo fails with empty undo stack");
		stack.addEvent(new DummyEditorEvent());
		assertTrue(stack.undo(), "Checking undo succeeds with an item in the stack");
		assertEquals(1, stack.redoSize(), "Checking redo stack now has an element");
	}
	
	@Test
	public void redo(){
		assertFalse(stack.redo(), "Checking redo fails with empty redo stack");
		stack.addEvent(new DummyEditorEvent());
		assertFalse(stack.redo(), "Checking redo still fails with empty redo stack");
		assertTrue(stack.undo(), "Checking undo succeeds with an item in the stack");
		assertEquals(0, stack.undoSize(), "Checking undo stack now has no elements");

		assertTrue(stack.redo(), "Checking redo succeeds after performing an undo");
		assertEquals(1, stack.undoSize(), "Checking redo stack now has an element");
	}
	
	@AfterEach
	public void end(){}
	

}
