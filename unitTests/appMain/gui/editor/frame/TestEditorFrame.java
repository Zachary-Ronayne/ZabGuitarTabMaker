package appMain.gui.editor.frame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabPanel;
import appMain.gui.editor.paint.TabPainter;
import appMain.gui.editor.paint.event.EditorEventStack;
import appUtils.ZabAppSettings;
import tab.InstrumentFactory;
import tab.Tab;
import util.testUtils.UtilsTest;

public class TestEditorFrame{

	private EditorFrame frame;
	private EditorEventStack stack;
	private EditorBar bar;
	
	private static ZabGui gui;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
		
		UtilsTest.createUnitFolder();
	}
	
	@BeforeEach
	public void setup(){
		frame = new EditorFrame(gui);
		gui.setVisible(false);
		gui.add(frame);
		gui.pack();
		
		stack = frame.getTabScreen().getUndoStack();
		bar = frame.getEditorBar();
	}
	
	@Test
	public void constructor(){
		assertTrue(frame.getTabScreen().getUndoStack().isSaved(), "Checking tab is marked as saved initially");
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
	
	@Test
	public void save(){
		File file = new File(UtilsTest.UNIT_PATH + "/EditorFrameSaveTest.zab");
		
		frame.setOpenedTab(null);
		assertFalse(frame.save(file), "Checking save fails with null tab");
		
		stack.markNotSaved();
		assertFalse(frame.save(null), "Checking save fails with null file");
		assertFalse(stack.isSaved(), "Checking undo stack is not saved");
		assertEquals("Save failed", bar.getFileStatusLab().getText(), "Checking label text set on failed save");

		stack.markNotSaved();
		frame.setOpenedTab(InstrumentFactory.guitarStandard());
		assertTrue(frame.save(file), "Checking normal save works correctly");
		assertTrue(stack.isSaved(), "Checking undo stack is now saved");
		assertEquals("Save successful", bar.getFileStatusLab().getText(), "Checking label text set on save");
	}
	
	@Test
	public void load(){
		File file = new File(UtilsTest.UNIT_PATH + "/EditorFrameLoadTest.zab");
		assertTrue(frame.save(file), "Checking file was saved");
		
		stack.markNotSaved();
		assertTrue(frame.load(file), "Checking load succeeds in normal case");
		assertEquals("Load successful", bar.getFileStatusLab().getText(), "Checking label text set on load");
		assertTrue(stack.isSaved(), "Checking undo stack is now saved");

		stack.markNotSaved();
		frame.setOpenedTab(null);
		assertTrue(frame.load(file), "Checking load succeeds with null tab");
		assertEquals("Load successful", bar.getFileStatusLab().getText(), "Checking label text set on load");
		assertTrue(stack.isSaved(), "Checking undo stack is now saved");
		
		stack.markNotSaved();
		assertFalse(frame.load(null), "Checking load fails with null file");
		assertFalse(stack.isSaved(), "Checking undo stack is not saved");
		assertEquals("Load failed", bar.getFileStatusLab().getText(), "Checking label text set on failed load");
	}
	
	@Test
	public void export(){
		File file = new File(UtilsTest.UNIT_PATH + "/EditorFrameExportTest.zab");
		
		assertFalse(frame.export(null), "Checking export fails with null file");
		assertEquals("Export failed", bar.getFileStatusLab().getText(), "Checking label text set on failed export");
		
		assertTrue(frame.export(file), "Checking export succeeds");
		assertEquals("Export successful", bar.getFileStatusLab().getText(), "Checking label text set export");
		
		frame.setOpenedTab(null);
		assertFalse(frame.export(file), "Checking export fails with null tab set");
	}
	
	@AfterEach
	public void end(){}
	
	@AfterAll
	public static void endAll(){
		gui.dispose();
		UtilsTest.deleteUnitFolder();
	}

}
