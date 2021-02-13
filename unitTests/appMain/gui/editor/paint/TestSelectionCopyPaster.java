package appMain.gui.editor.paint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.editor.paint.event.EditorEventStack;
import appUtils.ZabAppSettings;
import appUtils.settings.TabControlSettings;
import tab.Tab;

public class TestSelectionCopyPaster extends AbstractTestTabPainter{

	private SelectionCopyPaster paster;
	
	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
		paster = new SelectionCopyPaster(paint);
	}
	
	@Test
	public void getBaseSelection(){
		assertEquals(null, paster.getBaseSelection(), "Checking base selection initially null");
	}
	
	@Test
	public void reset(){
		paint.select(0, 0);
		paster.runCopy();
		assertNotEquals(null, paster.getBaseSelection(), "Checking base selection set");
		assertNotEquals(null, paster.getSelectedTab(), "Checking selected tab set");
		
		paster.reset();
		assertEquals(null, paster.getBaseSelection(), "Checking base selection null after reset");
		assertEquals(null, paster.getSelectedTab(), "Checking selected tab null after reset");
	}
	
	@Test
	public void runCopy(){
		assertFalse(paster.runCopy(), "Checking copy fails with no selection");
		
		paint.select(1, 5);
		paint.select(0, 5);
		paint.select(0, 4);
		assertTrue(paster.runCopy(), "Checking copy succeeds with a selection");
		assertEquals(paint.stringSelection(0, 4), paster.getBaseSelection(), "Checking base selection set to the lowest string index and position value");
		
		Tab copyTab = tab.copyWithoutSymbols();
		copyTab.placeQuantizedNote(4, 0, 1);
		copyTab.placeQuantizedNote(5, 0, 0);
		copyTab.placeQuantizedNote(5, 0, 1);
		assertEquals(copyTab, paster.getSelectedTab(), "Checking selected tab has the correct notes");
	}
	
	@Test
	public void paste(){
		TabControlSettings settings = ZabAppSettings.get().control();
		Tab copyTab, baseTab;
		EditorEventStack stack = paint.getUndoStack();
		
		// Pasting on an existing note and it fails
		settings.getPasteOverwrite().set(false);
		paint.select(0, 0);
		copyTab = tab.copy();
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		assertFalse(paster.paste(1032.0, 347.0, true), "Checking paste fails with no overwriting and placing on an existing note");
		assertEquals(copyTab, tab, "Checking tab unchanged");
		resetPaste();
		
		// Pasting over an existing note
		settings.getPasteOverwrite().set(true);
		paint.select(0, 0);
		copyTab = tab.copy();
		copyTab.getStrings().get(1).clear();
		copyTab.placeQuantizedNote(1, 5, 3.25);
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		assertTrue(paster.paste(971.0, 452.0, true), "Checking paste succeeds with overwriting a note");
		assertEquals(copyTab, tab, "Checking tab has overwritten note");
		resetPaste();
		
		// Pasting a note, pitch is not changed, i.e. tab number stays the same
		paint.select(0, 0);
		copyTab = tab.copy();
		copyTab.placeQuantizedNote(1, 0, 2);
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		assertTrue(paster.paste(691.0, 460.0, false), "Checking paste succeeds placing a note");
		assertEquals(copyTab, tab, "Checking tab has a note with the same tab number");
		resetPaste();
		
		// Pasting multiple notes, all succeed
		paint.select(0, 0);
		paint.select(0, 1);
		paint.select(0, 2);
		copyTab = tab.copy();
		copyTab.getStrings().get(3).clear();
		copyTab.placeQuantizedNote(3, 14, 2);
		copyTab.placeQuantizedNote(4, 14, 1.75);
		copyTab.placeQuantizedNote(5, 15, 1.5);
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		assertTrue(paster.paste(689.0, 687.0, true), "Checking paste succeeds placing notes");
		assertEquals(copyTab, tab, "Checking tab has the pasted notes");
		resetPaste();
		
		// Pasting multiple notes, some one note is not overwritten
		settings.getPasteOverwrite().set(false);
		paint.select(0, 0);
		paint.select(0, 1);
		paint.select(0, 2);
		copyTab = tab.copy();
		copyTab.placeQuantizedNote(4, 14, 1.75);
		copyTab.placeQuantizedNote(5, 15, 1.5);
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		assertTrue(paster.paste(689.0, 687.0, true), "Checking paste succeeds placing notes");
		assertEquals(copyTab, tab, "Checking tab has the pasted notes");
		settings.getPasteOverwrite().set(true);
		resetPaste();
		
		// Pasting multiple notes, some would be placed outside of the tab line
		paint.select(0, 0);
		paint.select(0, 1);
		paint.select(0, 2);
		copyTab = tab.copy();
		copyTab.placeQuantizedNote(4, 19, 2);
		copyTab.placeQuantizedNote(5, 19, 1.75);
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		assertTrue(paster.paste(683.0, 799.0, true), "Checking paste succeeds placing notes");
		assertEquals(copyTab, tab, "Checking tab has the pasted notes");
		resetPaste();
		
		// Pasting multiple notes, some would be placed outside of the tab line, making it fail
		settings.getPasteCancelInvalid().set(true);
		paint.select(0, 0);
		paint.select(0, 1);
		paint.select(0, 2);
		copyTab = tab.copy();
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		assertFalse(paster.paste(683.0, 799.0, true), "Checking paste fails with canceled placement");
		assertEquals(copyTab, tab, "Checking tab unchanged");
		resetPaste();
		
		// Undo not recorded with no changes
		settings.getPasteOverwrite().set(false);
		stack.markSaved();
		stack.clearStack();
		assertTrue(stack.isEmpty(), "Checking stack has no events");
		assertTrue(stack.isSaved(), "Checking stack is not saved");
		paint.select(0, 0);
		copyTab = tab.copy();
		baseTab = tab.copy();
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		assertFalse(paster.paste(1024.0, 343.0, true, true), "Checking paste fails with no changes the note");
		assertEquals(copyTab, tab, "Checking tab has placed note");
		assertTrue(stack.isEmpty(), "Checking stack still has no events");
		assertTrue(stack.isSaved(), "Checking stack still saved");
		assertFalse(paint.undo(), "Checking undo fails");
		assertEquals(copyTab, tab, "Checking tab still has the placed note");
		resetPaste();
		
		// Undo recorded with changes occurring
		stack.markSaved();
		stack.clearStack();
		assertTrue(stack.isEmpty(), "Checking stack has no events");
		assertTrue(stack.isSaved(), "Checking stack is not saved");
		paint.select(0, 0);
		copyTab = tab.copy();
		baseTab = tab.copy();
		copyTab.placeQuantizedNote(0, 0, 3);
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		assertTrue(paster.paste(917.0, 345.0, true, true), "Checking paste places the note");
		assertEquals(copyTab, tab, "Checking tab has placed note");
		assertFalse(stack.isEmpty(), "Checking stack has an event");
		assertFalse(stack.isSaved(), "Checking stack is no longer saved");
		assertTrue(paint.undo(), "Checking undo succeeds");
		assertEquals(baseTab, tab, "Checking the note is removed after the undo");
		resetPaste();
		
		// Pasting fails with negative x tab position
		settings.getPasteCancelInvalid().set(false);
		paint.select(0, 0);
		copyTab = tab.copy();
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		assertFalse(paster.paste(70.0, 515.0, true), "Checking paste fails with trying to paste to the negative position of a tab");
		assertEquals(copyTab, tab, "Checking tab unchanged");
		
		// Pasting fails with negative string number
		paint.select(0, 0);
		copyTab = tab.copy();
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		assertFalse(paster.paste(766.0, 280.0, true), "Checking paste fails with trying to paste above a line");
		assertEquals(copyTab, tab, "Checking tab unchanged");
		
		// Pasting fails with null base tab
		paster.reset();
		assertFalse(paster.paste(971.0, 452.0, true), "Checking paste fails with null base tab");
		resetPaste();
		
		// Pasting fails with null painter tab
		paint.select(0, 0);
		assertTrue(paster.runCopy(), "Checking copy succeeds");
		paint.setTab(null);
		assertFalse(paster.paste(1032.0, 347.0, true), "Checking pasting fails with null painter tab");
	}
	
	/**
	 * Utility method for resetting between paste test cases
	 */
	private void resetPaste(){
		tab.clearNotes();
		AbstractTestTabPainter.initNotes(tab);
		paint.clearSelection();
	}
	
	@AfterEach
	public void end(){}
	
}
