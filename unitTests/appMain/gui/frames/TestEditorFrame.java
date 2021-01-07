package appMain.gui.frames;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.BoxLayout;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.comp.TabPainter;
import appMain.gui.comp.ZabPanel;
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
		assertTrue("Checking graphics panel still in the frame", frame.isAncestorOf(paint));
	}
	
	@Test
	public void parentResized(){
		frame.parentResized(550, 350);
		TabPainter paint = frame.getTabScreen();
		assertEquals(550, paint.getPaintWidth(), "Checking width updated");
		assertEquals(326, paint.getPaintHeight(), "Checking height updated based on height of menu");
	}
	
	@Test
	public void getTabScreen(){
		assertTrue("Checking graphics panel in the frame", frame.isAncestorOf(frame.getTabScreen()));
	}
	
	@Test
	public void getMenuHolder(){
		ZabPanel menu = frame.getMenuHolder();
		assertTrue("Checking menu holder in the frame", frame.isAncestorOf(menu));
		assertEquals(BoxLayout.X_AXIS, ((BoxLayout)menu.getLayout()).getAxis(), "Checking menu has correct layout");
	}
	
	@Test
	public void getPaintHolder(){
		ZabPanel holder = frame.getPaintHolder();
		assertTrue("Checking graphics holder in the frame", frame.isAncestorOf(holder));
		assertTrue("Checking graphics panel in holder", holder.isAncestorOf(frame.getTabScreen()));
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
	
	@AfterEach
	public void end(){}

}
