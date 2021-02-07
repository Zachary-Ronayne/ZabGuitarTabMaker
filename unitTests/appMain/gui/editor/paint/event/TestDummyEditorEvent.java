package appMain.gui.editor.paint.event;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.editor.paint.AbstractTestTabPainter;

public class TestDummyEditorEvent extends AbstractTestTabPainter{

	private DummyEditorEvent event;
	
	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
		event = new DummyEditorEvent();
	}
	
	@Test
	public void undo(){
		assertTrue(event.undo(paint), "Checking undo always returns true");
	}
	
	@Test
	public void redo(){
		assertTrue(event.redo(paint), "Checking redo always returns true");
	}
	
	@AfterEach
	public void end(){}
	
}
