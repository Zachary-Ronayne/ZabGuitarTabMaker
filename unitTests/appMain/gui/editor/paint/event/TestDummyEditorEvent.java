package appMain.gui.editor.paint.event;

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
		event.undo(paint);
	}
	
	@Test
	public void redo(){
		event.redo(paint);
	}
	
	@AfterEach
	public void end(){}
	
}
