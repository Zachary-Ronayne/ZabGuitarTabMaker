package appMain.gui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.frames.EditorFrame;
import appMain.gui.frames.ZabFrame;
import appUtils.ZabAppSettings;

public class TestZabGui{

	private static ZabGui gui;
	
	private ZabGui.GuiResizeListener resizer;
	
	private ComponentEvent compEvent;
	private WindowEvent winEvent;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
	}
	
	@BeforeEach
	public void setup(){
		
		compEvent = new ComponentEvent(gui, 0);
		winEvent = new WindowEvent(gui, 0);
		resizer = gui.getResizer();
	}
	
	@Test
	public void maximize(){
		gui.maximize();
		assertEquals(JFrame.MAXIMIZED_BOTH, gui.getExtendedState(), "Checking window maximized");
	}
	
	@Test
	public void updateSize(){
		gui.updateSize();
	}
	
	@Test
	public void getCurrentFrame(){
		ZabFrame frame = gui.getCurrentFrame();
		assertTrue("Checking current frame is correct type, was " + frame.getClass(),
				frame instanceof EditorFrame);
	}
	
	@Test
	public void setCurrentFrame(){
		ZabFrame frame = new ZabFrame(gui){
			private static final long serialVersionUID = 1L;
			@Override
			public void parentResized(int w, int h){}
		};
		
		assertFalse("Checking frame was not set", gui.setCurrentFrame(null));
		
		assertTrue("Checking frame was set successfully", gui.setCurrentFrame(frame));
		assertEquals(frame, gui.getCurrentFrame(), "Checking frame was set correctly");
	}
	
	@Test
	public void getEditorFrame(){
		assertNotEquals(null, gui.getEditorFrame(), "Checking editor frame initialized");
	}
	
	@Test
	public void openEditor(){
		ZabFrame frame = new ZabFrame(gui){
			private static final long serialVersionUID = 1L;
			@Override
			public void parentResized(int w, int h){}
		};
		gui.setCurrentFrame(frame);
		
		EditorFrame edit = gui.getEditorFrame();
		assertNotEquals(edit, gui.getCurrentFrame(), "Checking editor frame not opened");
		gui.openEditor();
		assertEquals(edit, gui.getCurrentFrame(), "Checking editor frame opened");
	}
	
	@Test
	public void getPrimaryPanel(){
		assertTrue("Checking primary panel in GUI", gui.isAncestorOf(gui.getPrimaryPanel()));
	}
	
	@Test
	public void getResizer(){
		assertNotEquals(null, gui.getResizer(), "Checking resizer in listener not null");
		assertEquals(resizer, gui.getResizer(), "Checking resizer set in listener");
	}
	
	@Test
	public void getGuiGuiResizeListener(){
		assertEquals(gui, resizer.getGui(), "Checking gui set in listener");
	}
	
	@Test
	public void resizeGuiResizeListener(){
		resizer.resize();
	}
	
	@Test
	public void componentResizedGuiResizeListener(){
		resizer.componentResized(compEvent);
	}
	
	@Test
	public void windowStateChangedGuiResizeListener(){
		resizer.windowStateChanged(winEvent);
	}
	
	@Test
	public void windowOpenedGuiResizeListener(){
		resizer.windowOpened(winEvent);
	}
	
	@Test
	public void windowClosingGuiResizeListener(){
		resizer.windowClosing(winEvent);
	}
	
	@Test
	public void windowClosedGuiResizeListener(){
		resizer.windowClosed(winEvent);
	}
	
	@Test
	public void windowIconifiedGuiResizeListener(){
		resizer.windowIconified(winEvent);
	}
	
	@Test
	public void windowDeiconifiedGuiResizeListener(){
		resizer.windowDeiconified(winEvent);
	}
	
	@Test
	public void windowActivatedGuiResizeListener(){
		resizer.windowActivated(winEvent);
	}
	
	@Test
	public void windowDeactivatedGuiResizeListener(){
		resizer.windowDeactivated(winEvent);
	}
	
	@AfterEach
	public void end(){}
	
}
