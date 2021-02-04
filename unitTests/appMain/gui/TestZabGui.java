package appMain.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.comp.ZabFrame;
import appMain.gui.editor.frame.EditorFrame;
import appMain.gui.editor.paint.event.DummyEditorEvent;
import appUtils.ZabAppSettings;
import gui.ConfirmNotSavedPopup;
import util.testUtils.Assert;

public class TestZabGui{

	private ZabGui gui;
	
	private ZabGui.ZabWindowListener resizer;
	
	private ComponentEvent compEvent;
	private WindowEvent winEvent;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		gui = new ZabGui();
		gui.setVisible(false);
		compEvent = new ComponentEvent(gui, 0);
		winEvent = new WindowEvent(gui, 0);
		resizer = gui.getResizer();
	}
	
	@Test
	public void updateTheme(){
		ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), null, false);
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		gui.updateTheme();
		assertEquals(new Color(240, 240, 240), gui.getBackground(), "Checking background set for theme");
	}
	
	@Test
	public void updateName(){
		assertEquals("Zab Guitar Tab Editor", gui.getTitle(), "Checking title set with no string");
		
		gui.updateTitle(null);
		assertEquals("Zab Guitar Tab Editor", gui.getTitle(), "Checking title set with null string");
		
		gui.updateTitle("");
		assertEquals("Zab Guitar Tab Editor", gui.getTitle(), "Checking title set with empty string");
		
		gui.updateTitle(" ");
		assertEquals("Zab Guitar Tab Editor", gui.getTitle(), "Checking title set with blank string");
		
		gui.updateTitle("words");
		assertEquals("Zab Guitar Tab Editor | words", gui.getTitle(), "Checking title set with used string");
	}
	
	@Test
	public void copyData(){
		ZabGui newGui = new ZabGui();
		newGui.setVisible(false);
		gui.getEditorFrame().getTabScreen().getUndoStack().addEvent(new DummyEditorEvent());
		gui.copyData(newGui);
		assertEquals(gui.getEditorFrame().getOpenedTab(), newGui.getEditorFrame().getOpenedTab(),
				"Checking tab copied");
		
		assertFalse(newGui.getEditorFrame().getTabScreen().getUndoStack().isSaved(), "Checking stack is unsaved");
		
		newGui.dispose();
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
	public void getZabMenuBar(){
		assertNotEquals(null, gui.getZabMenuBar(), "Checking menu bar initialized");
		assertNotEquals(gui.getMenuBar(), gui.getZabMenuBar(), "Checking menu bar set");
	}
	
	@Test
	public void getCurrentFrame(){
		ZabFrame frame = gui.getCurrentFrame();
		Assert.isInstance(EditorFrame.class, frame, "Checking current frame is correct type, was " + frame.getClass());
	}
	
	@Test
	public void setCurrentFrame(){
		ZabFrame frame = new ZabFrame(gui){
			private static final long serialVersionUID = 1L;
			@Override
			public void parentResized(int w, int h){}
		};
		
		assertFalse(gui.setCurrentFrame(null), "Checking frame was not set");
		
		assertTrue(gui.setCurrentFrame(frame), "Checking frame was set successfully");
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
		assertTrue(gui.isAncestorOf(gui.getPrimaryPanel()), "Checking primary panel in GUI");
	}
	
	@Test
	public void getResizer(){
		assertNotEquals(null, gui.getResizer(), "Checking resizer in listener not null");
		assertEquals(resizer, gui.getResizer(), "Checking resizer set in listener");
	}
	
	@Test
	public void getGuiZabWindowListener(){
		assertEquals(gui, resizer.getGui(), "Checking gui set in listener");
	}
	
	@Test
	public void resizeZabWindowListener(){
		resizer.resize();
	}
	
	@Test
	public void componentResizedZabWindowListener(){
		resizer.componentResized(compEvent);
	}
	
	@Test
	public void windowStateChangedZabWindowListener(){
		resizer.windowStateChanged(winEvent);
	}
	
	@Test
	public void windowOpenedZabWindowListener(){
		resizer.windowOpened(winEvent);
	}
	
	@Test
	public void windowClosingZabWindowListener(){
		// Running case of not saved, pop up fails
		ConfirmNotSavedPopup.setDisableState(false);
		gui.getEditorFrame().getTabScreen().getUndoStack().markNotSaved();
		resizer.windowClosing(winEvent);
		
		// Running case of not saved, pop up succeeds
		ConfirmNotSavedPopup.setDisableState(true);
		gui.getEditorFrame().getTabScreen().getUndoStack().markNotSaved();
		resizer.windowClosing(winEvent);
		
		// Running case of saved
		gui.getEditorFrame().getTabScreen().getUndoStack().markSaved();
		resizer.windowClosing(winEvent);
	}
	
	@Test
	public void windowClosedZabWindowListener(){
		resizer.windowClosed(winEvent);}
	
	@Test
	public void windowIconifiedZabWindowListener(){
		resizer.windowIconified(winEvent);
	}
	
	@Test
	public void windowDeiconifiedZabWindowListener(){
		resizer.windowDeiconified(winEvent);
	}
	
	@Test
	public void windowActivatedZabWindowListener(){
		resizer.windowActivated(winEvent);
	}
	
	@Test
	public void windowDeactivatedZabWindowListener(){
		resizer.windowDeactivated(winEvent);
	}
	
	@Test
	public void windowGainedFocusZabWindowListener(){
		resizer.windowGainedFocus(winEvent);
	}
	
	@Test
	public void windowLostFocusZabWindowListener(){
		resizer.windowLostFocus(winEvent);
	}
	
	@AfterEach
	public void end(){
		gui.dispose();
	}
	
}
