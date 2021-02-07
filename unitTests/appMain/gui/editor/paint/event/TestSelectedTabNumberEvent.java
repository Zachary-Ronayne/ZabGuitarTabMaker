package appMain.gui.editor.paint.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.editor.paint.AbstractTestTabPainter;
import appMain.gui.editor.paint.Selection;
import appMain.gui.editor.paint.SelectionList;
import music.Music;
import tab.symbol.TabNote;

public class TestSelectedTabNumberEvent extends AbstractTestTabPainter{
	
	private SelectedTabNumberEvent event;
	
	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
		SelectionList placed = new SelectionList();
		Selection s = paint.stringSelection(0, 0);
		placed.add(s);
		SelectionList removed = new SelectionList();
		removed.add(new Selection(s.getPos().copySymbol(new TabNote(Music.createPitch(Music.F, 4))), s.getString(), s.getStringIndex()));
		event = new SelectedTabNumberEvent(placed, removed);
	}
	
	@Test
	public void undo(){
		paint.selectAllNotes();
		assertTrue(event.undo(paint), "Checking undo succeeds");
		assertEquals(Music.createPitch(Music.F, 4), ((TabNote)(str0.symbol(0))).getPitch(), "Checking pitch restored after undo");
		assertTrue(paint.getSelected().isEmpty(), "Checking selection cleared");
		
		tab.clearNotes();
		assertFalse(event.undo(paint), "Checking undo fails with no note on the position");
	}
	
	@Test
	public void redo(){
		assertTrue(event.undo(paint), "Checking undo succeeds");
		
		paint.selectAllNotes();
		assertTrue(event.redo(paint), "Checking redo succeeds");
		assertEquals(Music.createPitch(Music.E, 4), ((TabNote)(str0.symbol(0))).getPitch(), "Checking pitch restored after redo");
		assertTrue(paint.getSelected().isEmpty(), "Checking selection cleared");
		
		tab.clearNotes();
		assertFalse(event.redo(paint), "Checking redo fails with no note on the position");
	}
	
	@AfterEach
	public void end(){}
	
}
