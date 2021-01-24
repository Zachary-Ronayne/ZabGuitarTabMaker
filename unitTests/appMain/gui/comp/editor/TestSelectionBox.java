package appMain.gui.comp.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.util.Camera;
import tab.InstrumentFactory;
import tab.Tab;
import tab.TabString;
import util.testUtils.UtilsTest;

public class TestSelectionBox{

	private BufferedImage img;
	private Graphics2D g;
	private SelectionBox box;
	private Tab tab;
	private ArrayList<TabString> strs;
	private TabPainter paint;
	private Camera cam;
	
	@BeforeAll
	public static void init(){
		TestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		img = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		g = (Graphics2D)img.getGraphics();
		tab = InstrumentFactory.guitarStandard();
		paint = new TabPainter(1000, 1000, tab);
		box = new SelectionBox(paint);
		cam = paint.getCamera();
		TestTabPainter.initNotes(tab);
		TestTabPainter.initCam(cam);
		strs = tab.getStrings();
		paint.paint(g);
	}
	
	@Test
	public void getFirstCorner(){
		assertEquals(null, box.getFirstCorner(), "Checking corner initially null");
	}
	
	@Test
	public void setFirstCorner(){
		Point2D.Double p = new Point2D.Double(10, 20);
		box.setFirstCorner(p);
		assertEquals(p, box.getFirstCorner(), "Checking corner set");
	}
	
	@Test
	public void getNewCorner(){
		assertEquals(null, box.getNewCorner(), "Checking corner initially null");
	}
	
	@Test
	public void setNewCorner(){
		Point2D.Double p = new Point2D.Double(10, 20);
		box.setNewCorner(p);
		assertEquals(p, box.getNewCorner(), "Checking corner set");
	}
	
	@Test
	public void isSelecting(){
		assertFalse(box.isSelecting(), "Checking box initially not selecting");
	}
	
	@Test
	public void setSelecting(){
		box.setSelecting(true);
		assertTrue(box.isSelecting(), "Checking box now selecting");
	}
	
	@Test
	public void getContents(){
		assertTrue(box.getContents().isEmpty(), "Checking contents initially empty");
	}
	
	@Test
	public void hasCorners(){
		assertFalse(box.hasCorners(), "Checking box initially has no corners");
		
		box.setFirstCorner(new Point2D.Double(110, 120));
		assertFalse(box.hasCorners(), "Checking box initially has no corners with only new corner defined");
		
		box.setFirstCorner(null);
		box.setNewCorner(new Point2D.Double(120, 140));
		assertFalse(box.hasCorners(), "Checking box initially has no corners with only first corner defined");
		
		box.setFirstCorner(new Point2D.Double(110, 120));
		box.setNewCorner(new Point2D.Double(120, 140));
		assertTrue(box.hasCorners(), "Checking box initially has corners with both corners defined");
	}
	
	@Test
	public void updateSelectionBox(){
		assertFalse(box.updateSelectionBox(0, 0), "Checking box fails to update when not set to select");
		
		box.setSelecting(true);
		assertEquals(null, box.getFirstCorner(), "Checking first corner null before update");
		assertEquals(null, box.getNewCorner(), "Checking new corner null before update");

		assertTrue(box.updateSelectionBox(15, 40), "Checking box begins the update");
		Point2D.Double p = box.getFirstCorner();
		assertEquals(cam.toCamX(15), p.getX(), UtilsTest.DELTA, "Checking x correct for first corner");
		assertEquals(cam.toCamY(40), p.getY(), UtilsTest.DELTA, "Checking y correct for first corner");
		assertEquals(null, box.getNewCorner(), "Checking new corner still null after first update");
		
		paint.setTab(null);
		assertFalse(box.updateSelectionBox(0, 0), "Checking box fails to update with null tab");
		
		paint.setTab(tab);
		assertTrue(box.updateSelectionBox(20, 45), "Checking box begins the update");
		p = box.getFirstCorner();
		assertEquals(cam.toCamX(15), p.getX(), UtilsTest.DELTA, "Checking x still correct for first corner");
		assertEquals(cam.toCamY(40), p.getY(), UtilsTest.DELTA, "Checking y still correct for first corner");
		p = box.getNewCorner();
		assertEquals(cam.toCamX(20), p.getX(), UtilsTest.DELTA, "Checking x correct for new corner");
		assertEquals(cam.toCamY(45), p.getY(), UtilsTest.DELTA, "Checking y correct for new corner");
		
		// Resetting selection
		box.clear();
		box.setSelecting(true);
		assertTrue(box.updateSelectionBox(848.0, 304.0), "Checking first position set");
		assertTrue(box.updateSelectionBox(1241.0, 657.0), "Checking new position set");
		SelectionList conts = box.getContents();
		assertEquals(3, conts.size(), "Checking correct number of TabPositions selected");
		assertTrue(conts.isSelected(paint.stringSelection(0, 0)), "Checking correct TabPosition selected");
		assertTrue(conts.isSelected(paint.stringSelection(0, 1)), "Checking correct TabPosition selected");
		assertTrue(conts.isSelected(paint.stringSelection(0, 2)), "Checking correct TabPosition selected");
		
		// Testing a case with notes not all on the same line
		TestTabPainter.initAditionalNotes(tab);
		TestTabPainter.initZoomedOutCam(cam);
		paint.clearSelection();
		box.clear();
		box.setSelecting(true);
		assertTrue(box.updateSelectionBox(233.0, 762.0), "Checking first position set");
		assertTrue(box.updateSelectionBox(267.0, 789.0), "Checking new position set");
		conts = box.getContents();
		assertEquals(1, conts.size(), "Checking correct number of TabPositions selected");
		assertTrue(conts.isSelected(paint.stringSelection(1, 3)), "Checking correct TabPosition selected");

		// Testing a case with notes on multiple different lines
		TestTabPainter.initAditionalNotes(tab);
		TestTabPainter.initZoomedOutCam(cam);
		box.clear();
		box.setSelecting(true);
		assertTrue(box.updateSelectionBox(275.0, 847.0), "Checking first position set");
		assertTrue(box.updateSelectionBox(180.0, 185.0), "Checking new position set");
		conts = box.getContents();
		assertEquals(6, conts.size(), "Checking correct number of TabPositions selected");
		assertTrue(conts.isSelected(paint.stringSelection(1, 5)), "Checking correct TabPosition selected");
		assertTrue(conts.isSelected(paint.stringSelection(2, 5)), "Checking correct TabPosition selected");
		assertTrue(conts.isSelected(paint.stringSelection(5, 5)), "Checking correct TabPosition selected");
		assertTrue(conts.isSelected(paint.stringSelection(6, 5)), "Checking correct TabPosition selected");
		assertTrue(conts.isSelected(paint.stringSelection(1, 4)), "Checking correct TabPosition selected");
		assertTrue(conts.isSelected(paint.stringSelection(1, 3)), "Checking correct TabPosition selected");
	}
	
	@Test
	public void getBounds(){
		assertEquals(new Rectangle2D.Double(), box.getBounds(), "Checking bounds is empty when both of the points are null");
		
		box.setFirstCorner(new Point2D.Double(110, 120));
		assertEquals(new Rectangle2D.Double(), box.getBounds(), "Checking bounds is empty when last corner is null");

		box.setFirstCorner(null);
		box.setNewCorner(new Point2D.Double(120, 140));
		assertEquals(new Rectangle2D.Double(), box.getBounds(), "Checking bounds is empty when first corner is null");
		
		box.setFirstCorner(new Point2D.Double(110, 120));
		box.setNewCorner(new Point2D.Double(120, 140));
		assertEquals(new Rectangle2D.Double(110, 120, 10, 20), box.getBounds(), "Checking correct bounds with not null points of box");
		
		box.setFirstCorner(new Point2D.Double(130, 170));
		box.setNewCorner(new Point2D.Double(100, 120));
		assertEquals(new Rectangle2D.Double(100, 120, 30, 50), box.getBounds(), "Checking correct bounds with points in reversed order");

		box.setFirstCorner(new Point2D.Double(100, 170));
		box.setNewCorner(new Point2D.Double(130, 120));
		assertEquals(new Rectangle2D.Double(100, 120, 30, 50), box.getBounds(), "Checking correct bounds with points in opposite corners");
	}
	
	@Test
	public void selectInPainter(){
		SelectionList conts = box.getContents();
		SelectionList sel = paint.getSelected();
		conts.add(paint.createSelection(3.5, 0));
		conts.add(paint.createSelection(3.25, 1));
		
		paint.select(0, 2);
		box.selectInPainter(true);
		assertEquals(3, sel.size(), "Checking correct number of selections made with adding selection");
		assertEquals(strs.get(0).get(0), sel.get(0).getPos(), "Checking correct elements selected");
		assertEquals(strs.get(1).get(0), sel.get(1).getPos(), "Checking correct elements selected");
		assertEquals(strs.get(2).get(0), sel.get(2).getPos(), "Checking correct elements selected");
		
		paint.clearSelection();
		paint.select(0, 2);
		box.selectInPainter(false);
		assertEquals(2, sel.size(), "Checking correct number of selections made");
		assertEquals(strs.get(0).get(0), sel.get(0).getPos(), "Checking correct elements selected");
		assertEquals(strs.get(1).get(0), sel.get(1).getPos(), "Checking correct elements selected");

		// Running case of empty box
		conts.clear();
		box.selectInPainter(false);
	}
	
	@Test
	public void clear(){
		Point2D.Double p1 = new Point2D.Double(120, 140);
		Point2D.Double p2 = new Point2D.Double(110, 120);
		box.setSelecting(true);
		box.setFirstCorner(p1);
		box.setNewCorner(p2);
		box.getContents().add(paint.createSelection(3.5, 0));

		assertEquals(p1, box.getFirstCorner(), "Checking corner set before reset");
		assertEquals(p2, box.getNewCorner(), "Checking corner set before reset");
		assertTrue(box.isSelecting(), "Checking selecting set before reset");
		assertFalse(box.getContents().isEmpty(), "Checking contents set before reset");
		
		box.clear();
		assertEquals(null, box.getFirstCorner(), "Checking corner reset to null");
		assertEquals(null, box.getNewCorner(), "Checking corner reset to null");
		assertFalse(box.isSelecting(), "Checking selecting reset to false");
		assertTrue(box.getContents().isEmpty(), "Checking contents empty after reset");
	}
	
	@Test
	public void draw(){
		assertFalse(box.draw(g), "Checking box fails to draw with invalid corners");

		box.setFirstCorner(new Point2D.Double(110, 120));
		box.setNewCorner(new Point2D.Double(120, 140));
		assertTrue(box.draw(g), "Checking box draws with valid corners");
	}
	
	@AfterEach
	public void end(){}
	
}
