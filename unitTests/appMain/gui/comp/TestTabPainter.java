package appMain.gui.comp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.comp.TabPainter.EditorKeyboard;
import appMain.gui.comp.TabPainter.EditorMouse;
import appMain.gui.comp.TabPainter.Selection;
import appMain.gui.util.Camera;
import appUtils.ZabAppSettings;
import tab.InstrumentFactory;
import tab.Tab;
import tab.TabPosition;
import tab.TabString;
import util.testUtils.UtilsTest;

public class TestTabPainter{

	private TabPainter paint;
	private BufferedImage img;
	private Graphics2D g;
	private Tab tab;
	private ArrayList<TabString> strs;
	private Camera cam;
	private Selection sel;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		paint = new TabPainter(400, 340, InstrumentFactory.guitarStandard());
		tab = InstrumentFactory.guitarStandard();
		paint.setTab(tab);
		tab.placeQuantizedNote(5, 0, 0);
		tab.placeQuantizedNote(5, 0, 1);
		tab.placeQuantizedNote(4, 0, 1);
		tab.placeQuantizedNote(3, 0, 2);
		tab.placeQuantizedNote(2, 0, 3);
		tab.placeQuantizedNote(1, 0, 4);
		tab.placeQuantizedNote(0, 0, 5);
		strs = tab.getStrings();
		img = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		g = (Graphics2D)img.getGraphics();
		cam = paint.getCamera();
		sel = new Selection(strs.get(0).get(0), strs.get(0));
	}
	
	@Test
	public void getPaintWidth(){
		assertEquals(400, paint.getPaintWidth(), "Checking width initialized");
	}
	
	@Test
	public void setPaintWidth(){
		paint.setPaintWidth(450);
		assertEquals(450, paint.getPaintWidth(), "Checking width set");
		assertEquals(340, paint.getPaintHeight(), "Checking height unchanged");
	}
	
	@Test
	public void getPaintHeight(){
		assertEquals(340, paint.getPaintHeight(), "Checking height initialized");
	}
	
	@Test
	public void setPaintHeight(){
		paint.setPaintHeight(420);
		assertEquals(400, paint.getPaintWidth(), "Checking width unchanged");
		assertEquals(420, paint.getPaintHeight(), "Checking height set");
	}
	
	@Test
	public void setPaintSize(){
		paint.setPaintSize(320, 520);
		assertEquals(320, paint.getPaintWidth(), "Checking width set");
		assertEquals(520, paint.getPaintHeight(), "Checking height set");

		Camera cam = paint.getCamera();
		assertEquals(320, cam.getWidth(), "Checking camera width set");
		assertEquals(520, cam.getHeight(), "Checking camera height set");
		
		Dimension d = paint.getPreferredSize();
		assertEquals(320, d.getWidth(), "Checking preferred width set");
		assertEquals(520, d.getHeight(), "Checking preferred height set");
	}
	
	@Test
	public void getSelected(){
		assertNotEquals(null, paint.getSelected(), "Checking selected initialized");
		assertTrue(paint.getSelected().isEmpty(), "Checking selected initially empty");
	}
	
	@Test
	public void isSelected(){
		paint.select(0, 0);
		assertTrue(paint.isSelected(strs.get(0).get(0)), "Checking position selected");
		
		paint.select(0, 1);
		assertTrue(paint.isSelected(strs.get(0).get(0)), "Checking position still selected");
		assertTrue(paint.isSelected(strs.get(1).get(0)), "Checking position selected");
		assertFalse(paint.isSelected(strs.get(2).get(0)), "Checking position not selected");
	}
	
	@Test
	public void selected(){
		paint.select(0, 0);
		paint.select(0, 1);
		assertEquals(strs.get(0).get(0).getSymbol(), paint.selected(0), "Checking correct symbol found");
		assertEquals(strs.get(1).get(0).getSymbol(), paint.selected(1), "Checking correct symbol found");
		assertEquals(null, paint.selected(-1), "Checking null returned on invalid index");
		assertEquals(null, paint.selected(2), "Checking null returned on invalid index");
	}

	@Test
	public void selectedPosition(){
		paint.select(0, 0);
		paint.select(0, 1);
		assertEquals(strs.get(0).get(0), paint.selectedPosition(0), "Checking correct selection found");
		assertEquals(strs.get(1).get(0), paint.selectedPosition(1), "Checking correct selection found");
		assertEquals(null, paint.selectedPosition(-1), "Checking null returned on invalid index");
		assertEquals(null, paint.selectedPosition(2), "Checking null returned on invalid index");
	}

	@Test
	public void select(){
		paint.clearSelection();
		assertTrue(paint.select(0, 0), "Checking selection succeeds");
		assertFalse(paint.select(0, 0), "Checking selection fails with position already selected");
		assertTrue(paint.isSelected(strs.get(0).get(0)), "Checking position selected");
		assertFalse(paint.isSelected(strs.get(1).get(0)), "Checking position not selected");
		
		paint.clearSelection();
		assertTrue(paint.select(0, strs.get(1)), "Checking selection succeeds");
		assertTrue(paint.isSelected(strs.get(1).get(0)), "Checking position selected");
		assertFalse(paint.isSelected(strs.get(2).get(0)), "Checking position not selected");
		
		paint.clearSelection();
		assertTrue(paint.select(strs.get(2).get(0), 2), "Checking selection succeeds");
		assertTrue(paint.isSelected(strs.get(2).get(0)), "Checking position selected");
		assertFalse(paint.isSelected(strs.get(3).get(0)), "Checking position not selected");
		
		paint.clearSelection();
		assertTrue(paint.select(strs.get(3).get(0), strs.get(3)), "Checking selection succeeds");
		assertTrue(paint.isSelected(strs.get(3).get(0)), "Checking position selected");
		assertFalse(paint.isSelected(strs.get(4).get(0)), "Checking position not selected");
		
		paint.clearSelection();
		assertFalse(paint.select(strs.get(3).get(0), strs.get(2)), "Checking selection fails with note not on string");
		assertTrue(paint.getSelected().isEmpty(), "Checking no selection");
	}
	
	@Test
	public void selectOne(){
		TabString s = strs.get(0);
		TabPosition p = s.get(0);
		paint.appendSelectedTabNum('0');
		assertFalse(paint.selectOne(p, strs.get(1)), "Checking note fails to select");
		assertTrue(paint.selectOne(p, s), "Checking note selected");
		assertEquals(p, paint.selectedPosition(0), "Checking note was selected");
		assertEquals(1, paint.getSelected().size(), "Checking only one note was selected");
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking selected new tab num set to null");
	}
	
	@Test
	public void selectNote(){
		assertTrue(paint.getSelected().isEmpty(), "Checking nothing is selected");
		
		assertTrue(paint.selectNote(1151, 299), "Checking note selected");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected");
		assertEquals(tab.getStrings().get(0).get(0), paint.selectedPosition(0), "Checking string 0 note selected");
		
		assertTrue(paint.selectNote(940, 350), "Checking note selected");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected");
		assertEquals(tab.getStrings().get(1).get(0), paint.selectedPosition(0), "Checking string 1 note selected");
		
		assertFalse(paint.selectNote(940, -10350), "Checking note not selected");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected, no change from not finding a note");
		assertEquals(tab.getStrings().get(1).get(0), paint.selectedPosition(0), "Checking string 1 note still selected");
		
		assertFalse(paint.selectNote(-10940, 350), "Checking note not selected");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected, no change from not finding a note");
		assertEquals(tab.getStrings().get(1).get(0), paint.selectedPosition(0), "Checking string 1 note still selected");
		
		paint.clearSelection();
		assertTrue(paint.getSelected().isEmpty(), "Checking none selected");
		assertTrue(paint.selectNote(350, 500), "Checking note selected");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected");
		assertEquals(tab.getStrings().get(5).get(1), paint.selectedPosition(0), "Checking string 5 note 1 selected");
		
		tab.getStrings().get(0).clear();
		paint.clearSelection();
		assertFalse(paint.selectNote(1150, 300), "Checking no note selected with no note on the string");
	}

	@Test
	public void selectAllNotes(){
		paint.selectAllNotes();
		ArrayList<Selection> sel = paint.getSelected();
		assertEquals(7, sel.size(), "Checking correct number of notes selected");
	}
	
	@Test
	public void clearSelection(){
		TabString s = tab.getStrings().get(0);
		TabPosition p = s.get(0);
		paint.selectOne(p, s);
		paint.clearSelection();
		assertTrue(paint.getSelected().isEmpty(), "Checking selected empty");
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking selected new tab num set to null");
	}
	
	@Test
	public void removeSelectedNotes(){
		paint.selectAllNotes();
		assertFalse(paint.getSelected().isEmpty(), "Checking notes are selected");
		
		paint.removeSelectedNotes();
		assertTrue(paint.getSelected().isEmpty(), "Checking no notes are selected");
		assertTrue(tab.isEmpty(), "Checking the tab has no notes are selected");
		
		TabString str = tab.getStrings().get(0);
		tab.placeQuantizedNote(0, 0, 0);
		tab.placeQuantizedNote(0, 0, 1);
		paint.selectOne(str.get(0), str);
		assertEquals(1, paint.getSelected().size(), "Checking one note selected");
		assertEquals(2, str.size(), "Checking string has 2 notes");
		
		paint.removeSelectedNotes();
		assertTrue(paint.getSelected().isEmpty(), "Checking no notes selected");
		assertEquals(1, str.size(), "Checking string has 1 note");
	}
	
	@Test
	public void getSelectedNewTabNum(){
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking tab num initialized to null");
	}
	
	@Test
	public void appendSelectedTabNum(){
		paint.appendSelectedTabNum('1');
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking tab num null with no selection");

		TabString s = tab.getStrings().get(0);
		TabPosition p = s.get(0);
		paint.selectOne(p, s);
		paint.appendSelectedTabNum('j');
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking tab num null with non number");
		
		paint.appendSelectedTabNum('-');
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking tab num null with minus sign");
		
		paint.appendSelectedTabNum('1');
		assertEquals(1, paint.getSelectedNewTabNum(), "Checking tab num with number");
		
		paint.appendSelectedTabNum('2');
		assertEquals(12, paint.getSelectedNewTabNum(), "Checking tab num with 2 numbers");
		
		paint.appendSelectedTabNum('3');
		assertEquals(3, paint.getSelectedNewTabNum(), "Checking tab for a third number reset to given number");
		
		paint.appendSelectedTabNum('-');
		assertEquals(-3, paint.getSelectedNewTabNum(), "Checking tab sign change with a minus sign");
		
		paint.appendSelectedTabNum('-');
		assertEquals(3, paint.getSelectedNewTabNum(), "Checking tab sign change back with a minus sign");
		
		paint.appendSelectedTabNum('s');
		assertEquals(3, paint.getSelectedNewTabNum(), "Checking tab num not changed adding a non number or minus sign");
		
		paint.appendSelectedTabNum('-');
		assertEquals(-3, paint.getSelectedNewTabNum(), "Checking appending a minus sign sets it");
		paint.appendSelectedTabNum('5');
		assertEquals(-35, paint.getSelectedNewTabNum(), "Checking appending a negative number adds it");

		paint.appendSelectedTabNum('7');
		assertEquals(-7, paint.getSelectedNewTabNum(), "Checking appending a negative number resets it");
	}
	
	@Test
	public void placeNote(){
		assertTrue(paint.placeNote(950, 300, 0), "Checking note was placed");
		assertEquals(2, tab.getStrings().get(0).size(), "Checking a new note added");
		assertEquals(5, tab.getStrings().get(0).get(1).getPos(), "Checking note has correct position");
		
		assertFalse(paint.placeNote(950, 300, 0), "Checking note in the same position not placed");

		assertFalse(paint.placeNote(-950, 300, 0), "Checking a no new note added with too low of x");
		assertFalse(paint.placeNote(950, -300300, 0), "Checking a no new note added with invalid y");
	}

	@Test
	public void getCamera(){
		assertEquals(400, cam.getWidth(), "Checking camera width initialized");
		assertEquals(340, cam.getHeight(), "Checking camera height initialized");
	}
	
	@Test
	public void resetCamera(){
		cam.setX(2);
		cam.setY(2);
		cam.setXZoomFactor(2);
		cam.setYZoomFactor(2);
		cam.setDrawOnlyInBounds(true);
		paint.resetCamera();
		assertEquals(-50, cam.getX(), "Checking camera x reset");
		assertEquals(-100, cam.getY(), "Checking camera y reset");
		assertEquals(0, cam.getXZoomFactor(), "Checking camera x zoom factor reset");
		assertEquals(0, cam.getYZoomFactor(), "Checking camera y zoom factor reset");
		assertEquals(true, cam.isDrawOnlyInBounds(), "Checking camera drawing in bounds reset");
	}
	
	@Test
	public void getTab(){
		assertEquals(tab, paint.getTab(), "Checking tab initialized");
	}
	
	@Test
	public void setTab(){
		Tab t = InstrumentFactory.bassStandard();
		paint.setTab(t);
		assertEquals(t, paint.getTab(), "Checking tab set");
	}
	
	@Test
	public void getMouseInput(){
		assertNotEquals(null, paint.getMouseInput(), "Checking mouse input initialized");
		assertTrue(Arrays.asList(paint.getMouseListeners()).contains(paint.getMouseInput()), "Checking painter contains mouse listener");
	}
	
	@Test
	public void getKeyInput(){
		assertNotEquals(null, paint.getKeyInput(), "Checking key input initialized");
		assertTrue(Arrays.asList(paint.getKeyListeners()).contains(paint.getKeyInput()), "Checking painter contains key listener");
	}
	
	@Test
	public void xToTabPos(){
		cam.setX(10);
		cam.setXZoomFactor(1);
		assertEquals(-0.45, paint.xToTabPos(0), UtilsTest.DELTA, "Checking conversion to tab pos");
		assertEquals(-0.2, paint.xToTabPos(100), UtilsTest.DELTA, "Checking conversion to tab pos");
		
		assertEquals(-0.2, paint.tabPosToX(paint.xToTabPos(-0.2)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
		assertEquals(2, paint.tabPosToX(paint.xToTabPos(2)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
		assertEquals(200, paint.tabPosToX(paint.xToTabPos(200)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
	}
	
	@Test
	public void tabPosToX(){
		cam.setX(10);
		cam.setXZoomFactor(1);
		assertEquals(0, paint.tabPosToX(-0.45), UtilsTest.DELTA, "Checking conversion to paint pos");
		assertEquals(100, paint.tabPosToX(-0.2), UtilsTest.DELTA, "Checking conversion to paint pos");
		
		assertEquals(-0.2, paint.xToTabPos(paint.tabPosToX(-0.2)), UtilsTest.DELTA, "Checking conversion to paint pos and back");
		assertEquals(2, paint.xToTabPos(paint.tabPosToX(2)), UtilsTest.DELTA, "Checking conversion to paint pos and back");
		assertEquals(200, paint.xToTabPos(paint.tabPosToX(200)), UtilsTest.DELTA, "Checking conversion to paint pos and back");
	}
	
	@Test
	public void camXToTabPos(){
		assertEquals(1, paint.camXToTabPos(300), UtilsTest.DELTA, "Checking conversion to tab pos");
		assertEquals(-0.5, paint.camXToTabPos(0), UtilsTest.DELTA, "Checking conversion tab pos");
		
		assertEquals(0, paint.camXToTabPos(paint.tabPosToCamX(0)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
		assertEquals(10, paint.camXToTabPos(paint.tabPosToCamX(10)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
		assertEquals(-67, paint.camXToTabPos(paint.tabPosToCamX(-67)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
	}
	
	@Test
	public void tabPosToCamX(){
		assertEquals(300, paint.tabPosToCamX(1), UtilsTest.DELTA, "Checking conversion to camera pos");
		assertEquals(0, paint.tabPosToCamX(-0.5), UtilsTest.DELTA, "Checking conversion camera pos");
		
		assertEquals(0, paint.tabPosToCamX(paint.camXToTabPos(0)), UtilsTest.DELTA, "Checking conversion to camera pos and back");
		assertEquals(10, paint.tabPosToCamX(paint.camXToTabPos(10)), UtilsTest.DELTA, "Checking conversion to camera pos and back");
		assertEquals(-67, paint.tabPosToCamX(paint.camXToTabPos(-67)), UtilsTest.DELTA, "Checking conversion to camera pos and back");
	}
	
	@Test
	public void yToTabPos(){
		cam.setY(10);
		cam.setYZoomFactor(1);
		assertEquals(-4.75, paint.yToTabPos(0), UtilsTest.DELTA, "Checking conversion to tab pos");
		assertEquals(-3.5, paint.yToTabPos(100), UtilsTest.DELTA, "Checking conversion to tab pos");
		
		assertEquals(-0.2, paint.tabPosToY(paint.yToTabPos(-0.2)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
		assertEquals(2, paint.tabPosToY(paint.yToTabPos(2)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
		assertEquals(200, paint.tabPosToY(paint.yToTabPos(200)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
	}
	
	@Test
	public void tabPosToY(){
		cam.setY(10);
		cam.setYZoomFactor(1);
		assertEquals(0, paint.tabPosToY(-4.75), UtilsTest.DELTA, "Checking conversion to paint pos");
		assertEquals(100, paint.tabPosToY(-3.5), UtilsTest.DELTA, "Checking conversion to paint pos");
		
		assertEquals(-0.2, paint.yToTabPos(paint.tabPosToY(-0.2)), UtilsTest.DELTA, "Checking conversion to paint pos and back");
		assertEquals(2, paint.yToTabPos(paint.tabPosToY(2)), UtilsTest.DELTA, "Checking conversion to paint pos and back");
		assertEquals(200, paint.yToTabPos(paint.tabPosToY(200)), UtilsTest.DELTA, "Checking conversion to paint pos and back");
	}
	
	@Test
	public void camYToTabPos(){
		assertEquals(2.5, paint.camYToTabPos(300), UtilsTest.DELTA, "Checking conversion to tab pos");
		assertEquals(-5, paint.camYToTabPos(0), UtilsTest.DELTA, "Checking conversion tab pos");
		
		assertEquals(0, paint.camYToTabPos(paint.tabPosToCamY(0)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
		assertEquals(1.5, paint.camYToTabPos(paint.tabPosToCamY(1.5)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
		assertEquals(-1.2, paint.camYToTabPos(paint.tabPosToCamY(-1.2)), UtilsTest.DELTA, "Checking conversion to tab pos and back");
	}
	
	@Test
	public void tabPosToCamY(){
		assertEquals(300, paint.tabPosToCamY(2.5), UtilsTest.DELTA, "Checking conversion to camera pos");
		assertEquals(0, paint.tabPosToCamY(-5), UtilsTest.DELTA, "Checking conversion camera pos");
		
		assertEquals(0, paint.tabPosToCamY(paint.camYToTabPos(0)), UtilsTest.DELTA, "Checking conversion to camera pos and back");
		assertEquals(1.5, paint.tabPosToCamY(paint.camYToTabPos(1.5)), UtilsTest.DELTA, "Checking conversion to camera pos and back");
		assertEquals(-1.2, paint.tabPosToCamY(paint.camYToTabPos(-1.2)), UtilsTest.DELTA, "Checking conversion to camera pos and back");
	}
	
	@Test
	public void pixelYToStringNum(){
		cam.setY(-100);
		cam.setYZoomFactor(1);
		assertEquals(0, paint.pixelYToStringNum(610), "Checking conversion to string num");
		assertEquals(0, paint.pixelYToStringNum(590), "Checking conversion to string num, close to too low");
		assertEquals(5, paint.pixelYToStringNum(1010), "Checking conversion to string num, close to too high");
		assertEquals(-1, paint.pixelYToStringNum(0), "Checking conversion to string num, too low");
		assertEquals(-1, paint.pixelYToStringNum(1050), "Checking conversion to string num, too high");
	}
	
	@Test
	public void paint(){
		paint.paint(g);
		assertEquals(g, paint.getCamera().getG(), "Checking graphics object set and used");
	}
	
	@Test
	public void drawTab(){
		assertTrue(paint.drawTab(g), "Checking painting occurred");
		assertEquals(g, paint.getCamera().getG(), "Checking graphics object set and used");

		TabString s = tab.getStrings().get(0);
		paint.selectOne(s.get(0), s);
		assertTrue(paint.drawTab(g), "Checking painting occurred with drawing a selection");
		
		paint.setTab(null);
		assertFalse(paint.drawTab(g), "Checking painting failed with null tab");
	}
	
	@Test
	public void mousePressedEditorMouse(){
		EditorMouse m = paint.getMouseInput();
		m.mousePressed(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON3));
		assertEquals(2, tab.getStrings().get(0).size(), "Checking a new note added on right click");
		
		cam.releaseAnchor();
		m.mousePressed(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertTrue(cam.isAchored(), "Checking camera anchored on middle click");
		
		paint.clearSelection();
		m.mousePressed(new MouseEvent(paint, 0, 0, 0, 1150, 300, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertEquals(tab.getStrings().get(0).get(1), paint.selectedPosition(0), "Checking a note selected on left click");
		
		paint.clearSelection();
		
		// Running case of invalid button number
		m.mousePressed(new MouseEvent(paint, 0, 0, 0, 1150, 300, 0, 0, 0, false, MouseEvent.NOBUTTON));
	}
	
	@Test
	public void mouseReleasedEditorMouse(){
		EditorMouse m = paint.getMouseInput();
		cam.setAnchor(0, 0);
		m.mouseReleased(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertTrue(cam.isAchored(), "Checking left click doesn't release camera anchor");
		
		m.mouseReleased(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertFalse(cam.isAchored(), "Checking middle click releases camera anchor");
	}
	
	@Test
	public void mouseClickedEditorMouse(){
		EditorMouse m = paint.getMouseInput();
		cam.setXZoomFactor(1);
		
		m.mouseClicked(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertEquals(1, cam.getXZoomFactor(), "Checking zoom factor unchanged on left click");
		
		m.mouseClicked(new MouseEvent(paint, 0, 0, MouseEvent.SHIFT_DOWN_MASK, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertEquals(1, cam.getXZoomFactor(), "Checking zoom factor unchanged on left click shift down");
		
		m.mouseClicked(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertEquals(1, cam.getXZoomFactor(), "Checking zoom factor unchanged on middle click without shift");
		
		m.mouseClicked(new MouseEvent(paint, 0, 0, MouseEvent.SHIFT_DOWN_MASK, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertEquals(0, cam.getXZoomFactor(), "Checking zoom factor reset on middle click with shift");
	}
	
	@Test
	public void mouseDraggedEditorMouse(){
		EditorMouse m = paint.getMouseInput();
		double x = cam.getX();
		cam.setAnchor(0, 0);
		m.mouseDragged(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertNotEquals(x, cam.getX(), "Checking camera was panned");

		x = cam.getX();
		cam.releaseAnchor();
		m.mouseDragged(new MouseEvent(paint, 0, 0, 0, 950, 300, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertEquals(x, cam.getX(), "Checking camera was not panned");
	}
	
	@Test
	public void mouseWheelMovedEditorMouse(){
		EditorMouse m = paint.getMouseInput();
		
		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, 0, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-0.1, cam.getXZoomFactor(), "Checking zooming in with no modifiers");
		
		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, 0, 950, 300, 0, false, 
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, -1));
		assertEquals(0.1, cam.getXZoomFactor(), "Checking zooming out with no modifiers");

		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.SHIFT_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-0.2, cam.getXZoomFactor(), "Checking zooming in with shift");
		
		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.SHIFT_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, -1));
		assertEquals(0.2, cam.getXZoomFactor(), "Checking zooming out with shift");

		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.ALT_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-0.2, cam.getXZoomFactor(), "Checking zooming in with alt");
		
		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.CTRL_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-0.2, cam.getXZoomFactor(), "Checking zooming in with ctrl");

		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0,
				MouseWheelEvent.CTRL_DOWN_MASK | MouseWheelEvent.SHIFT_DOWN_MASK | MouseWheelEvent.ALT_DOWN_MASK,
				950, 300, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-0.8, cam.getXZoomFactor(), "Checking zooming in with all modifiers");
	}
	
	@Test
	public void keyPressedEditorKeyboard(){
		EditorKeyboard k = paint.getKeyInput();
		paint.select(0, 0);
		cam.setXZoomFactor(1);
		
		k.keyPressed(new KeyEvent(paint, 0, 0, 0, 0, '1'));
		assertEquals(1, paint.getSelectedNewTabNum(), "Checking tab num updated with number");
		
		k.keyPressed(new KeyEvent(paint, 0, 0, 0, 0, '-'));
		assertEquals(-1, paint.getSelectedNewTabNum(), "Checking tab num updated with minus sign");
		
		paint.clearSelection();
		k.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_A, 'a'));
		assertTrue(paint.getSelected().isEmpty(), "Checking no selection made on a press no ctrl");
		
		k.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_A, 'a'));
		assertEquals(7, paint.getSelected().size(), "Checking all notes selected on a press with ctrl");
		
		k.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_D, 'd'));
		assertFalse(tab.isEmpty(), "Checking tab notes were not removed on d press no ctrl");
		
		k.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_D, 'd'));
		assertTrue(tab.isEmpty(), "Checking tab notes were removed on d press with ctrl");

		tab.placeQuantizedNote(0, 0, 0);
		tab.placeQuantizedNote(1, 0, 1);
		k.keyPressed(new KeyEvent(paint, 0, 0, 0, KeyEvent.VK_R, 'r'));
		assertFalse(tab.isEmpty(), "Checking tab notes were not removed on r press no ctrl");
		
		k.keyPressed(new KeyEvent(paint, 0, 0, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_R, 'r'));
		assertTrue(tab.isEmpty(), "Checking tab notes were removed on r press no ctrl");
	}
	
	@Test
	public void getPosSelection(){
		assertEquals(strs.get(0).get(0), sel.getPos(), "Checking position initialized");
	}
	
	@Test
	public void setPosSelection(){
		sel.setPos(strs.get(1).get(0));
		assertEquals(strs.get(1).get(0), sel.getPos(), "Checking position set");
	}
	
	@Test
	public void getStringSelection(){
		assertEquals(strs.get(0), sel.getString(), "Checking string initialized");
	}
	
	@Test
	public void setStringSelection(){
		sel.setString(strs.get(1));
		assertEquals(strs.get(1), sel.getString(), "Checking set");
	}
	
	@AfterEach
	public void end(){}

}
