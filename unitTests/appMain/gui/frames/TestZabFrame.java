package appMain.gui.frames;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appUtils.ZabAppSettings;

public class TestZabFrame{
	
	private ZabFrame frame;
	
	private class TestFrame extends ZabFrame{
		public TestFrame(ZabGui gui){super(gui);}
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
		frame = new TestFrame(null);
	}

	@Test
	public void constructor(){
		assertEquals(new Color(40, 40, 40), frame.getBackground(), "Checking background set");
	}
	
	@AfterEach
	public void end(){}

}
