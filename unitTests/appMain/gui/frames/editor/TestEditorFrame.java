package appMain.gui.frames.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabPanel;
import appMain.gui.comp.editor.TabPainter;
import appUtils.ZabAppSettings;
import tab.InstrumentFactory;
import tab.Tab;

public class TestEditorFrame{

	private EditorFrame frame;
	
	private static ZabGui gui;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		frame = new EditorFrame(gui);
		gui.setVisible(false);
		gui.add(frame);
		gui.pack();
	}
	
	@Test
	public void updatePaintSize(){
		frame.updatePaintSize(550, 350);
		TabPainter paint = frame.getTabScreen();
		assertEquals(550, paint.getPaintWidth(), "Checking width updated");
		assertEquals(350, paint.getPaintHeight(), "Checking height updated");
		assertTrue(frame.isAncestorOf(paint), "Checking graphics panel still in the frame");
	}
	
	@Test
	public void parentResized(){
		frame.parentResized(550, 350);
		TabPainter paint = frame.getTabScreen();
		assertEquals(550, paint.getPaintWidth(), "Checking width updated");
		assertEquals(332, paint.getPaintHeight(), "Checking height updated based on height of menu");
	}
	
	@Test
	public void getTabScreen(){
		assertTrue(frame.isAncestorOf(frame.getTabScreen()), "Checking graphics panel in the frame");
	}
	
	@Test
	public void getEditorBar(){
		EditorBar bar = frame.getEditorBar();
		assertTrue(frame.isAncestorOf(bar), "Checking menu holder in the frame");
	}
	
	@Test
	public void getPaintHolder(){
		ZabPanel holder = frame.getPaintHolder();
		assertTrue(frame.isAncestorOf(holder), "Checking graphics holder in the frame");
		assertTrue(holder.isAncestorOf(frame.getTabScreen()), "Checking graphics panel in holder");
	}
	
	@Test
	public void getOpenedTab(){
		assertEquals(InstrumentFactory.guitarStandard(), frame.getOpenedTab(), "Checking tab is initialized");
	}
	
	@Test
	public void setOpenedTab(){
		Tab guitar = InstrumentFactory.guitarEbStandard();
		frame.setOpenedTab(guitar);
		assertEquals(guitar, frame.getOpenedTab(), "Checking tab is set");
		assertEquals(guitar, frame.getTabScreen().getTab(), "Checking painter tab is set");
	}
	
	@Test
	public void focused(){
		// Calling focus method for completeness
		frame.focused();
	}
	
	@AfterEach
	public void end(){}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
	}

}
