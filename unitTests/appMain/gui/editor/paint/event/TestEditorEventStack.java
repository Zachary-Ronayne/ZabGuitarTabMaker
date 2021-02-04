package appMain.gui.editor.paint.event;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.editor.paint.AbstractTestTabPainter;

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
	public void addEvent(){
		stack.markSaved();
		assertTrue(stack.isSaved(), "Checking is saved");
		
		stack.addEvent(new DummyEditorEvent());
		assertFalse(stack.isSaved(), "Checking not saved after event added");
	}
	
	@AfterEach
	public void end(){}
	

}
