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
import appUtils.ZabSettings;
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
	
	/**
	 * High precision values in test cases were generated by printing out expected values from interacting with the GUI
	 */
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		
		ZabSettings s = ZabAppSettings.get();
		s.getTabPaintBaseX().set(200.0);
		s.getTabPaintBaseY().set(150.0);
		s.getTabPaintMeasureWidth().set(100.0);
		s.getTabPaintLineMeasures().set(4);
		s.getTabPaintStringSpace().set(50.0);
		s.getTabPaintSelectionBuffer().set(10.0);
		s.getTabPaintAboveSpace().set(400.0);
		s.getTabPaintBelowSpace().set(600.0);
		s.getQuantizeDivisor().set(4.0);
		s.getZoomFactor().set(2.0);
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
		tab.placeQuantizedNote(1, 0, 3.25);
		tab.placeQuantizedNote(0, 0, 3.5);
		strs = tab.getStrings();
		img = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		g = (Graphics2D)img.getGraphics();
		cam = paint.getCamera();
		cam.setX(100.1);
		cam.setY(400.21);
		cam.setXZoomFactor(1.2);
		cam.setYZoomFactor(1.2);
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
		
		paint.setTab(null);
		assertFalse(paint.select(strs.get(3).get(0), 3), "Checking selection fails with null tab");
		assertFalse(paint.select(0, 3), "Checking selection fails with null tab");
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

		assertTrue(paint.selectNote(1031.0, 347.0), "Checking note selected");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected");
		assertEquals(tab.getStrings().get(0).get(0), paint.selectedPosition(0), "Checking string 0 note selected");
		
		assertTrue(paint.selectNote(969.0, 472.0), "Checking note selected");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected");
		assertEquals(tab.getStrings().get(1).get(0), paint.selectedPosition(0), "Checking string 1 note selected");
		
		assertFalse(paint.selectNote(455.0, 962.0), "Checking note not selected below the line");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected, no change from not finding a note");
		assertEquals(tab.getStrings().get(1).get(0), paint.selectedPosition(0), "Checking string 1 note still selected");
		
		assertFalse(paint.selectNote(1032.0, 15.0), "Checking note not selected selecting above the string");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected, no change from not finding a note");
		assertEquals(tab.getStrings().get(1).get(0), paint.selectedPosition(0), "Checking string 1 note still selected");
		
		assertFalse(paint.selectNote(565.0, 696.0), "Checking note not selected selecting between 2 notes");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected, no change from not finding a note");
		assertEquals(tab.getStrings().get(1).get(0), paint.selectedPosition(0), "Checking string 1 note still selected");
		
		assertFalse(paint.selectNote(1455.0, 570.0), "Checking note not selected with invalid x position");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected, no change from not finding a note");
		assertEquals(tab.getStrings().get(1).get(0), paint.selectedPosition(0), "Checking string 1 note still selected");
		
		paint.clearSelection();
		assertTrue(paint.getSelected().isEmpty(), "Checking none selected");
		assertTrue(paint.selectNote(458.0, 934.0), "Checking note selected");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected");
		assertEquals(tab.getStrings().get(5).get(1), paint.selectedPosition(0), "Checking string 5 note 1 selected");
		
		tab.getStrings().get(0).clear();
		paint.clearSelection();
		assertFalse(paint.selectNote(686.0, 346.0), "Checking no note selected with no note on the string");
		
		paint.setTab(null);
		assertFalse(paint.selectNote(1031.0, 347.0), "checking selection fails with null tab");
	}

	@Test
	public void selectAllNotes(){
		paint.selectAllNotes();
		ArrayList<Selection> sel = paint.getSelected();
		assertEquals(7, sel.size(), "Checking correct number of notes selected");
		
		paint.clearSelection();
		paint.setTab(null);
		paint.selectAllNotes();
		assertEquals(0, sel.size(), "Checking no notes selected with null tab");
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
	public void reset(){
		paint.setTab(null);
		paint.reset();
		assertFalse(tab.isEmpty(), "Checking no notes removed with null tab in painter");
		assertEquals(0, paint.getLineTabCount(), "Checking line count with null tab");
		
		paint.setTab(tab);
		paint.reset();
		assertTrue(tab.isEmpty(), "Checking all notes removed");
		assertEquals(2, paint.getLineTabCount(), "Checking line count");

	}
	
	@Test
	public void getLineTabCount(){
		assertEquals(2, paint.getLineTabCount(), "Checking line count initialized");
	}
	
	@Test
	public void updateLineTabCount(){
		paint.setTab(null);
		paint.updateLineTabCount();
		assertEquals(0, paint.getLineTabCount(), "Checking line count with null tab");
		
		tab.clearNotes();
		paint.setTab(tab);
		paint.updateLineTabCount();
		assertEquals(2, paint.getLineTabCount(), "Checking line count with tab and no notes");
		
		tab.placeQuantizedNote(0, 0, 5);
		paint.updateLineTabCount();
		assertEquals(3, paint.getLineTabCount(), "Checking placing note causes a new line");
		
		tab.placeQuantizedNote(0, 0, 1);
		paint.updateLineTabCount();
		assertEquals(3, paint.getLineTabCount(), "Checking placing note at the beginning doesn't make a new line");
		
		tab.placeQuantizedNote(1, 0, 6);
		paint.updateLineTabCount();
		assertEquals(3, paint.getLineTabCount(), "Checking placing note on the same line doesn't make a new line");
		
		tab.placeQuantizedNote(1, 0, 13);
		paint.updateLineTabCount();
		assertEquals(5, paint.getLineTabCount(), "Checking placing note multiple lines down line adds multiple lines");
		
		tab.placeQuantizedNote(2, 0, 10);
		paint.updateLineTabCount();
		assertEquals(5, paint.getLineTabCount(), "Checking placing note on same line, on a later string");
		
		tab.getStrings().clear();
		paint.updateLineTabCount();
		assertEquals(0, paint.getLineTabCount(), "Checking line count with no strings");
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
		assertTrue(paint.placeNote(460.0, 340.0, 0), "Checking note was placed");
		assertEquals(2, tab.getStrings().get(0).size(), "Checking a new note added");
		assertEquals(1, tab.getStrings().get(0).get(0).getPos(), "Checking note has correct position");
		
		assertFalse(paint.placeNote(460.0, 340.0, 0), "Checking note in the same position not placed");

		assertFalse(paint.placeNote(44.0, 628.0, 0), "Checking no note added with too low of x");
		assertFalse(paint.placeNote(629.0, 26.0, 0), "Checking a no new note added with invalid y");
		
		paint.setTab(null);
		assertFalse(paint.placeNote(687.0, 339.0, 0), "Checking note fails to place with null tab");
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
	public void removeXOffset(){
		assertEquals(0, paint.removeXOffset(200), UtilsTest.DELTA, "Checking correct amount removed");
		assertEquals(-25, paint.removeXOffset(175), UtilsTest.DELTA, "Checking correct amount removed");
	}
	
	@Test
	public void removeYOffset(){
		assertEquals(50, paint.removeYOffset(200), UtilsTest.DELTA, "Checking correct amount removed");
		assertEquals(25, paint.removeYOffset(175), UtilsTest.DELTA, "Checking correct amount removed");
	}
	
	@Test
	public void tabWidth(){
		assertEquals(400, paint.tabWidth(), UtilsTest.DELTA, "Checking correct width");
	}
	
	@Test
	public void tabHeight(){
		assertEquals(250, paint.tabHeight(), "Checking correct height 6 strings");
		
		tab.getStrings().remove(0);
		assertEquals(200, paint.tabHeight(), "Checking correct height 5 strings");
		
		tab.getStrings().clear();
		assertEquals(0, paint.tabHeight(), "Checking correct height zero strings");
	}

	@Test
	public void tabLineStart(){
		assertEquals(-850, paint.tabLineStart(-1), "Checking start of line before the first line");
		assertEquals(400, paint.tabLineStart(0), "Checking start of first line");
		assertEquals(1650, paint.tabLineStart(1), "Checking start of second line");
		assertEquals(2900, paint.tabLineStart(2), "Checking start of third line");
	}
	
	@Test
	public void tabLineEnd(){
		assertEquals(-600, paint.tabLineEnd(-1), "Checking end of line before the first line");
		assertEquals(650, paint.tabLineEnd(0), "Checking end of first line");
		assertEquals(1900, paint.tabLineEnd(1), "Checking end of second line");
		assertEquals(3150, paint.tabLineEnd(2), "Checking end of third line");
	}
	
	@Test
	public void tabLineStartBuffer(){
		assertEquals(-860, paint.tabLineStartBuffer(-1), "Checking start of line before the first line");
		assertEquals(390, paint.tabLineStartBuffer(0), "Checking start of first line");
		assertEquals(1640, paint.tabLineStartBuffer(1), "Checking start of second line");
		assertEquals(2890, paint.tabLineStartBuffer(2), "Checking start of third line");
	}
	
	@Test
	public void tabLineEndBuffer(){
		assertEquals(-590, paint.tabLineEndBuffer(-1), "Checking end of line before the first line");
		assertEquals(660, paint.tabLineEndBuffer(0), "Checking end of first line");
		assertEquals(1910, paint.tabLineEndBuffer(1), "Checking end of second line");
		assertEquals(3160, paint.tabLineEndBuffer(2), "Checking end of third line");
	}
	
	@Test
	public void tabLineSideBuffer(){
		assertEquals(25, paint.tabLineSideBuffer(), "Checking side buffer correct");
	}
	
	@Test
	public void camXInTabLine(){
		assertTrue(paint.camXInTabLine(200), "Checking point in the middle of the line");
		assertTrue(paint.camXInTabLine(175.001), "Checking point just inside the line on the left");
		assertTrue(paint.camXInTabLine(599.999), "Checking point just inside the line on the right");
		assertFalse(paint.camXInTabLine(174.999), "Checking point just outside the line on the left");
		assertFalse(paint.camXInTabLine(600.001), "Checking point just outside the line on the right");
		assertFalse(paint.camXInTabLine(100.0), "Checking point far outside the line on the left");
		assertFalse(paint.camXInTabLine(700.0), "Checking point far outside the line on the right");
	}
	
	@Test
	public void numStrings(){
		assertEquals(6, paint.numStrings(), "Checking number of strings with tab");
		
		tab.getStrings().remove(0);
		assertEquals(5, paint.numStrings(), "Checking number of strings with a removed string");
		
		paint.setTab(null);
		assertEquals(0, paint.numStrings(), "Checking zero strings with null tab");
	}
	
	@Test
	public void lineNumberValue(){
		assertEquals(0.5, paint.lineNumberValue(775), UtilsTest.DELTA, "Checking halfway between the first and second lines");
		assertEquals(0, paint.lineNumberValue(150), UtilsTest.DELTA, "Checking at the first line");
		assertEquals(-0.5, paint.lineNumberValue(-475), UtilsTest.DELTA, "Checking halfway between the first and line before the first");
		assertEquals(0.9479411292300564, paint.lineNumberValue(1334.9264115375704), UtilsTest.DELTA, "Checking between the first and second lines");
		assertEquals(2.180207084061017, paint.lineNumberValue(2875.258855076271), UtilsTest.DELTA, "Checking between the second and third lines");
	}
	
	@Test
	public void lineNumber(){
		assertEquals(0, paint.lineNumber(775), UtilsTest.DELTA, "Checking halfway between the first and second lines");
		assertEquals(0, paint.lineNumber(150), UtilsTest.DELTA, "Checking at the first line");
		assertEquals(-1, paint.lineNumber(-475), UtilsTest.DELTA, "Checking halfway between the first and line before the first");
		assertEquals(0, paint.lineNumber(1334.9264115375704), UtilsTest.DELTA, "Checking between the first and second lines");
		assertEquals(2, paint.lineNumber(2875.258855076271), UtilsTest.DELTA, "Checking between the second and third lines");
	}
	
	@Test
	public void lineLength(){
		assertEquals(0.0, paint.lineLength(633.5551752921597), UtilsTest.DELTA, "Checking the first line");
		assertEquals(400.0, paint.lineLength(1452.390100669888), UtilsTest.DELTA, "Checking the first line");
		assertEquals(800.0, paint.lineLength(3008.6051682725856), UtilsTest.DELTA, "Checking the third line");
		assertEquals(-400.0, paint.lineLength(41.93622397212516), UtilsTest.DELTA, "Checking before the first line");
	}
	
	@Test
	public void lineHeight(){
		assertEquals(1250, paint.lineHeight(), "Checking line height with 6 strings");
		
		tab.getStrings().remove(0);
		assertEquals(1200, paint.lineHeight(), "Checking line height with 5 strings");
		
		paint.setTab(null);
		assertEquals(1000, paint.lineHeight(), "Checking line height with null tab");
	}
	
	@Test
	public void paintWidthToMeasures(){
		assertEquals(4, paint.paintWidthToMeasures(400), UtilsTest.DELTA, "Checking integer width");
		assertEquals(1.3, paint.paintWidthToMeasures(130), UtilsTest.DELTA, "Checking decimal width");
		assertEquals(0, paint.paintWidthToMeasures(0), UtilsTest.DELTA, "Checking zero width");
		assertEquals(-2.008, paint.paintWidthToMeasures(-200.8), UtilsTest.DELTA, "Checking negative width");
	}
	
	@Test
	public void measuresToPaintWidth(){
		assertEquals(400, paint.measuresToPaintWidth(4), UtilsTest.DELTA, "Checking integer width");
		assertEquals(130, paint.measuresToPaintWidth(1.3), UtilsTest.DELTA, "Checking decimal width");
		assertEquals(0, paint.measuresToPaintWidth(0), UtilsTest.DELTA, "Checking zero width");
		assertEquals(-200.8, paint.measuresToPaintWidth(-2.008), UtilsTest.DELTA, "Checking negative width");
	}
	
	@Test
	public void xToTabPos(){
		assertEquals(0.43305567662212413, paint.xToTabPos(329.0, 399.0), UtilsTest.DELTA, "Checking middle of first measure");
		assertEquals(3.2884615242334108, paint.xToTabPos(985.0, 535.0), UtilsTest.DELTA, "Checking middle of fourth measure");
		assertEquals(2.0522797243529145, paint.xToTabPos(701.0, 949.0), UtilsTest.DELTA, "Checking below line still finds a tab position");
		assertEquals(1.9913411849221858, paint.xToTabPos(687.0, 30.0), UtilsTest.DELTA, "Checking above line still finds a tab position");
		assertEquals(-1.0, paint.xToTabPos(73.0, 517.0), UtilsTest.DELTA, "Checking left of line finds invalid position");
		assertEquals(-1.0, paint.xToTabPos(1433.0, 591.0), UtilsTest.DELTA, "Checking right of line finds invalid position");
	}
	
	@Test
	public void tabPosToX(){
		assertEquals(329.0, paint.tabPosToX(0.43305567662212413), UtilsTest.DELTA, "Checking middle of first measure");
		assertEquals(985.0, paint.tabPosToX(3.2884615242334108), UtilsTest.DELTA, "Checking middle of fourth measure");
		assertEquals(701.0, paint.tabPosToX(2.0522797243529145), UtilsTest.DELTA, "Checking middle of third measure");
		assertEquals(687.0, paint.tabPosToX(1.9913411849221858), UtilsTest.DELTA, "Checking middle of second measure");
	}
	
	@Test
	public void camXToTabPos(){
		assertEquals(1.4385415772291474, paint.camXToTabPos(343.85415772291475, 624.8120453304), UtilsTest.DELTA, "Checking position in the middle of a measure");
		assertEquals(2.5310825341657837, paint.camXToTabPos(453.10825341657835, 717.5256803214372), UtilsTest.DELTA, "Checking position in the middle of a measure");
		assertEquals(0.0, paint.camXToTabPos(188.4608821745566, 737.548343277248), UtilsTest.DELTA, "Checking position in the at the left edge of a measure");
		assertEquals(3.75, paint.camXToTabPos(589.3494165724218, 675.3039780015752), UtilsTest.DELTA, "Checking position in the at the right edge of a measure");
		assertEquals(1.1991401723227135, paint.camXToTabPos(319.91401723227136, 515.5579496367365), UtilsTest.DELTA, "Checking position above a line finds the correct measure");
		assertEquals(3.271050512967489, paint.camXToTabPos(527.1050512967489, 818.9448209454357), UtilsTest.DELTA, "Checking position below a line finds the correct measure");
		assertEquals(-1.0, paint.camXToTabPos(120.12266295581085, 646.5758094128031), UtilsTest.DELTA, "Checking position left of a line is invalid");
		assertEquals(-1.0, paint.camXToTabPos(669.0047931140172, 683.5742083528884), UtilsTest.DELTA, "Checking position right of a line is invalid");
	}
	
	@Test
	public void tabPosToCamX(){
		assertEquals(343.85415772291475, paint.tabPosToCamX(1.4385415772291474), UtilsTest.DELTA, "Checking position in the middle of a measure");
		assertEquals(453.10825341657835, paint.tabPosToCamX(2.5310825341657837), UtilsTest.DELTA, "Checking position in the middle of a measure");
		assertEquals(200.0, paint.tabPosToCamX(0.0), UtilsTest.DELTA, "Checking position in the at the left edge of a measure");
		assertEquals(575.0, paint.tabPosToCamX(3.75), UtilsTest.DELTA, "Checking position in the at the right edge of a measure");
	}
	
	@Test
	public void yToTabPos(){
		assertEquals(3.6203842810505424, paint.yToTabPos(760.0), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(0.32099764615823234, paint.yToTabPos(381.0), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(0.0, paint.yToTabPos(333.0), UtilsTest.DELTA, "Checking string in the top edge of the tab line");
		assertEquals(5.0, paint.yToTabPos(929.0), UtilsTest.DELTA, "Checking string in the bottom edge of the tab line");
		assertEquals(-1.0, paint.yToTabPos(251.0), UtilsTest.DELTA, "Checking position above tab is invalid");
		assertEquals(-1.0, paint.yToTabPos(958.0), UtilsTest.DELTA, "Checking position below tab is invalid");
		assertEquals(2.523490571297425, paint.yToTabPos(634.0), UtilsTest.DELTA, "Checking position to the right of a tab finds a string");
		assertEquals(1.3830693333795034, paint.yToTabPos(503.0), UtilsTest.DELTA, "Checking position to the left of a tab finds a string");
	}
	
	@Test
	public void tabPosToY(){
		assertEquals(760.0, paint.tabPosToY(2, 3.6203842810505424), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(381.0, paint.tabPosToY(2, 0.32099764615823234), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(344.12705319001174, paint.tabPosToY(2, 0.0), UtilsTest.DELTA, "Checking string in the top edge of the tab line");
		assertEquals(918.4762306885292, paint.tabPosToY(2, 5.0), UtilsTest.DELTA, "Checking string in the bottom edge of the tab line");
	}
	
	@Test
	public void camYToTabPos(){
		assertEquals(1.91410517699014, paint.camYToTabPos(645.705258849507), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(3.898960461305303, paint.camYToTabPos(744.9480230652651), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(0.0, paint.camYToTabPos(543.4155676622124), UtilsTest.DELTA, "Checking string in the top edge of the tab line");
		assertEquals(5.0, paint.camYToTabPos(803.7101860877535), UtilsTest.DELTA, "Checking string in the bottom edge of the tab line");
		assertEquals(-1.0, paint.camYToTabPos(472.90097203522635), UtilsTest.DELTA, "Checking position above tab is invalid");
		assertEquals(-1.0, paint.camYToTabPos(816.3331692555473), UtilsTest.DELTA, "Checking position below tab is invalid");
		assertEquals(-1.0, paint.camYToTabPos(-369.3566979537738), UtilsTest.DELTA, "Checking negative line number returns -1");
	}
	
	@Test
	public void tabPosToCamY(){
		assertEquals(645.705258849507, paint.tabPosToCamY(2, 1.91410517699014), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(744.9480230652651, paint.tabPosToCamY(2, 3.898960461305303), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(550.0, paint.tabPosToCamY(2, 0.0), UtilsTest.DELTA, "Checking string in the top edge of the tab line");
		assertEquals(800.0, paint.tabPosToCamY(2, 5.0), UtilsTest.DELTA, "Checking string in the bottom edge of the tab line");
	}
	
	@Test
	public void pixelYToStringNum(){
		assertEquals(4, paint.pixelYToStringNum(760.0), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(0, paint.pixelYToStringNum(381.0), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(0.0, paint.pixelYToStringNum(333.0), UtilsTest.DELTA, "Checking string in the top edge of the tab line");
		assertEquals(5.0, paint.pixelYToStringNum(929.0), UtilsTest.DELTA, "Checking string in the bottom edge of the tab line");
		assertEquals(-1.0, paint.pixelYToStringNum(251.0), UtilsTest.DELTA, "Checking position above tab is invalid");
		assertEquals(-1.0, paint.pixelYToStringNum(958.0), UtilsTest.DELTA, "Checking position below tab is invalid");
		assertEquals(3, paint.pixelYToStringNum(634.0), UtilsTest.DELTA, "Checking position to the right of a tab finds a string");
		assertEquals(1, paint.pixelYToStringNum(503.0), UtilsTest.DELTA, "Checking position to the left of a tab finds a string");
	
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
		
		paint.clearSelection();
		m.mousePressed(new MouseEvent(paint, 0, 0, 0, 1031, 347, 0, 0, 0, false, MouseEvent.BUTTON1));
		assertEquals(tab.getStrings().get(0).get(0), paint.selectedPosition(0), "Checking a note selected on left click");
		paint.clearSelection();
		
		m.mousePressed(new MouseEvent(paint, 0, 0, 0, 460, 340, 0, 0, 0, false, MouseEvent.BUTTON3));
		assertEquals(2, tab.getStrings().get(0).size(), "Checking a new note added on right click");
		
		cam.releaseAnchor();
		m.mousePressed(new MouseEvent(paint, 0, 0, 0, 100, 101, 0, 0, 0, false, MouseEvent.BUTTON2));
		assertTrue(cam.isAchored(), "Checking camera anchored on middle click");
		
		// Running case of invalid button number
		m.mousePressed(new MouseEvent(paint, 0, 0, 0, 1, 2, 0, 0, 0, false, MouseEvent.NOBUTTON));
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
		assertEquals(-2.0, cam.getXZoomFactor(), "Checking zooming in with no modifiers");
		
		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, 0, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, -1));
		assertEquals(2.0, cam.getXZoomFactor(), "Checking zooming out with no modifiers");

		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.SHIFT_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-4.0, cam.getXZoomFactor(), "Checking zooming in with shift");
		
		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.SHIFT_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, -1, -1));
		assertEquals(4.0, cam.getXZoomFactor(), "Checking zooming out with shift");
		
		ZabAppSettings.get().getZoomInverted().set(false);
		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.SHIFT_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, -2, -2));
		assertEquals(-8.0, cam.getXZoomFactor(), "Checking zooming out with shift with opposite setting");
		ZabAppSettings.get().getZoomInverted().set(true);
		
		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.ALT_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-4.0, cam.getXZoomFactor(), "Checking zooming in with alt");
		
		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0, MouseWheelEvent.CTRL_DOWN_MASK, 950, 300, 0, false,
				MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-4.0, cam.getXZoomFactor(), "Checking zooming in with ctrl");

		paint.resetCamera();
		m.mouseWheelMoved(new MouseWheelEvent(paint, 0, 0,
				MouseWheelEvent.CTRL_DOWN_MASK | MouseWheelEvent.SHIFT_DOWN_MASK | MouseWheelEvent.ALT_DOWN_MASK,
				950, 300, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 0, 1));
		assertEquals(-16.0, cam.getXZoomFactor(), "Checking zooming in with all modifiers");
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
