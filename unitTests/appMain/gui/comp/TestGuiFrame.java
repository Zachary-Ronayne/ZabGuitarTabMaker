package appMain.gui.comp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestGuiFrame{

	private GuiFrame frame;
	
	private class TestFrame extends GuiFrame{
		private static final long serialVersionUID = 1L;
		@Override
		public void parentResized(int w, int h){}
	}
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		frame = new TestFrame();
	}
	
	@Test
	public void parentResized(){
		// Calling empty method for completeness
		frame.parentResized(0, 0);
	}
	
	@Test
	public void focused(){
		// Calling empty method for completeness
		frame.focused();
	}
	
	@AfterEach
	public void end(){}

}
