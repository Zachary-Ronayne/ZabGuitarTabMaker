package appMain.gui.frames;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.comp.TabPainter;
import appMain.gui.comp.ZabPanel;
import appUtils.ZabAppSettings;

public class TestEditorFrame{

	private EditorFrame frame;
	
	private ZabGui gui;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		gui = new ZabGui();
		gui.setVisible(false);
		frame = new EditorFrame(gui);
		JFrame gui = new JFrame();
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
	
	@AfterEach
	public void end(){}

}
