package appMain.gui.util;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import util.Size2D;
import util.testUtils.Assert;
import util.testUtils.UtilsTest;

public class TestCamera{

	private BufferedImage img;
	private Camera cam;
	private Graphics2D g;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		cam = new Camera(600, 400);
		img = new BufferedImage(600, 400, BufferedImage.TYPE_4BYTE_ABGR);
		g = (Graphics2D)img.getGraphics();
		cam.setGraphics(g);
		cam.setMaxX(1000);
		cam.setMaxY(2000);
		cam.setMinX(-1000);
		cam.setMinY(-2000);
	}
	
	@Test
	public void isAchored(){
		assertFalse(cam.isAchored(), "Checking anchored initialized");
	}
	@Test
	public void getAnchorX(){
		assertEquals(0, cam.getAnchorX(), UtilsTest.DELTA, "Checking anchoredX initialized");
	}
	@Test
	public void getAnchorY(){
		assertEquals(0, cam.getAnchorY(), UtilsTest.DELTA, "Checking anchoredX initialized");
	}
	
	@Test
	public void setAnchor(){
		cam.setAnchor(5, 17);
		assertTrue(cam.isAchored(), "Checking anchored set");
		assertEquals(5, cam.getAnchorX(), UtilsTest.DELTA, "Checking anchoredX set");
		assertEquals(17, cam.getAnchorY(), UtilsTest.DELTA, "Checking anchoredX set");
		
		cam.setAnchor(2, 3);
		assertTrue(cam.isAchored(), "Checking anchored still set");
		assertEquals(5, cam.getAnchorX(), UtilsTest.DELTA, "Checking anchoredX not changed without removing anchor");
		assertEquals(17, cam.getAnchorY(), UtilsTest.DELTA, "Checking anchoredX not changed without removing anchor");
	}
	
	@Test
	public void releaseAnchor(){
		cam.setAnchor(5, 17);
		assertTrue(cam.isAchored(), "Checking anchored set");
		
		cam.releaseAnchor();
		assertFalse(cam.isAchored(), "Checking anchored removed");
	}
	
	@Test
	public void pan(){
		cam.pan(100, 50);
		assertEquals(0, cam.getX(), UtilsTest.DELTA, "Checking x not changed with no anchor set");
		assertEquals(0, cam.getY(), UtilsTest.DELTA, "Checking y not changed with no anchor set");
		
		cam.setAnchor(1, 2);
		cam.pan(100, 50);
		assertEquals(-99, cam.getX(), UtilsTest.DELTA, "Checking x panned");
		assertEquals(-48, cam.getY(), UtilsTest.DELTA, "Checking y panned");
		
		cam.releaseAnchor();
		cam.setAnchor(10, 20);
		cam.pan(12, 5);
		assertEquals(-101, cam.getX(), UtilsTest.DELTA, "Checking x panned a second time");
		assertEquals(-33, cam.getY(), UtilsTest.DELTA, "Checking y panned a second time");
	}
	
	@Test
	public void center(){
		cam.center(100, 250);
		assertEquals(-200, cam.getX(), UtilsTest.DELTA, "Checking centered x");
		assertEquals(50, cam.getY(), UtilsTest.DELTA, "Checking centered y");
	}
	
	@Test
	public void zoomIn(){
		cam.setX(10);
		cam.setY(10);
		cam.zoomIn(5, 4, 1);
		Assert.greaterThan(cam.getX(), 10, "Checking x zoomed in");
		assertEquals(1, cam.getXZoomFactor(), UtilsTest.DELTA, "Checking x zoom factor changed");
		Assert.greaterThan(cam.getY(), 10, "Checking y zoomed in");
		assertEquals(1, cam.getYZoomFactor(), UtilsTest.DELTA, "Checking y zoom factor changed");
	}
	
	@Test
	public void zoomInX(){
		cam.setX(10);
		cam.setXZoomFactor(0);
		cam.zoomInX(5, 1);
		Assert.greaterThan(cam.getX(), 10, "Checking x zoomed in");
		assertEquals(1, cam.getXZoomFactor(), UtilsTest.DELTA, "Checking x zoom factor changed");
		
		cam.setX(10);
		cam.setXZoomFactor(0);
		cam.zoomInX(5, -2);
		Assert.lessThan(cam.getX(), 10, "Checking x zoomed out");
		assertEquals(-2, cam.getXZoomFactor(), UtilsTest.DELTA, "Checking x zoom factor changed");
	}
	@Test
	public void zoomInY(){
		cam.setY(10);
		cam.setYZoomFactor(0);
		cam.zoomInY(5, 1);
		Assert.greaterThan(cam.getY(), 10, "Checking y zoomed in");
		assertEquals(1, cam.getYZoomFactor(), UtilsTest.DELTA, "Checking y zoom factor changed");
		
		cam.setY(10);
		cam.setYZoomFactor(0);
		cam.zoomInY(5, -2);
		Assert.lessThan(cam.getY(), 10, "Checking y zoomed out");
		assertEquals(-2, cam.getYZoomFactor(), UtilsTest.DELTA, "Checking y zoom factor changed");
	}
	
	@Test
	public void zoomX(){
		cam.setXZoomFactor(2.1);
		cam.setZoomBase(1.6);
		assertEquals(8.049579951121597, cam.zoomX(3), "Checking zoomed amount");
		
		cam.setXZoomFactor(3.1);
		cam.setZoomBase(1.2);
		assertEquals(5.279382365946173, cam.zoomX(3), "Checking zoomed amount");
	}
	@Test
	public void inverseZoomX(){
		cam.setXZoomFactor(2.1);
		cam.setZoomBase(1.6);
		assertEquals(1.1180707632757874, cam.inverseZoomX(3), "Checking zoomed amount");
		
		cam.setXZoomFactor(3.1);
		cam.setZoomBase(1.2);
		assertEquals(1.7047448690310987, cam.inverseZoomX(3), "Checking zoomed amount");
	
	}
	
	@Test
	public void zoomY(){
		cam.setYZoomFactor(2.1);
		cam.setZoomBase(1.6);
		assertEquals(8.049579951121597, cam.zoomY(3), "Checking zoomed amount");
		
		cam.setYZoomFactor(3.1);
		cam.setZoomBase(1.2);
		assertEquals(5.279382365946173, cam.zoomY(3), "Checking zoomed amount");
	}
	@Test
	public void inverseZoomY(){
		cam.setYZoomFactor(2.1);
		cam.setZoomBase(1.6);
		assertEquals(1.1180707632757874, cam.inverseZoomY(3), "Checking zoomed amount");
		
		cam.setYZoomFactor(3.1);
		cam.setZoomBase(1.2);
		assertEquals(1.7047448690310987, cam.inverseZoomY(3), "Checking zoomed amount");
	}
	
	@Test
	public void drawX(){
		cam.setZoomBase(2);
		cam.setX(10);
		cam.setXZoomFactor(0);
		assertEquals(-10, cam.drawX(0), "Checking draw position");
		assertEquals(-9, cam.drawX(1), "Checking draw position");
		assertEquals(-9, cam.drawX(1.001), "Checking draw position");

		cam.setX(10);
		cam.setXZoomFactor(1);
		assertEquals(-18, cam.drawX(1), "Checking draw position");
		cam.setX(10);
		cam.setXZoomFactor(1);
		assertEquals(-18, cam.drawX(1.001), "Checking draw position");
	}
	@Test
	public void drawY(){
		cam.setZoomBase(2);
		cam.setY(10);
		cam.setYZoomFactor(0);
		assertEquals(-10, cam.drawY(0), "Checking draw position");
		assertEquals(-9, cam.drawY(1), "Checking draw position");
		assertEquals(-9, cam.drawY(1.001), "Checking draw position");

		cam.setY(10);
		cam.setYZoomFactor(1);
		assertEquals(-18, cam.drawY(1), "Checking draw position");
		cam.setY(10);
		cam.setYZoomFactor(1);
		assertEquals(-18, cam.drawY(1.001), "Checking draw position");
	}
	@Test
	public void drawW(){
		cam.setZoomBase(2);
		cam.setXZoomFactor(0);
		assertEquals(0, cam.drawW(0), "Checking draw size");
		assertEquals(1, cam.drawW(1), "Checking draw size");
		
		cam.setXZoomFactor(1);
		assertEquals(0, cam.drawW(0), "Checking draw size");
		assertEquals(2, cam.drawW(1), "Checking draw size");
		
		cam.setXZoomFactor(2);
		assertEquals(0, cam.drawW(0), "Checking draw size");
		assertEquals(6, cam.drawW(1.4), "Checking draw size");
	}
	@Test
	public void drawH(){
		cam.setZoomBase(2);
		cam.setYZoomFactor(0);
		assertEquals(0, cam.drawH(0), "Checking draw size");
		assertEquals(1, cam.drawH(1), "Checking draw size");
		
		cam.setYZoomFactor(1);
		assertEquals(0, cam.drawH(0), "Checking draw size");
		assertEquals(2, cam.drawH(1), "Checking draw size");
		
		cam.setYZoomFactor(2);
		assertEquals(0, cam.drawH(0), "Checking draw size");
		assertEquals(6, cam.drawH(1.4), "Checking draw size");
	}
	
	@Test
	public void toCamX(){
		cam.setZoomBase(2);
		cam.setX(3);
		cam.setXZoomFactor(0);
		assertEquals(13, cam.toCamX(10), UtilsTest.DELTA, "Checking mouse position correct");
		
		cam.setX(7);
		cam.setXZoomFactor(1);
		assertEquals(12, cam.toCamX(10), UtilsTest.DELTA, "Checking mouse position correct");
		
		cam.setX(7);
		cam.setXZoomFactor(-1);
		assertEquals(27, cam.toCamX(10), UtilsTest.DELTA, "Checking mouse position correct");
		
		assertEquals(0, cam.toCamX(cam.toPixelX(0)), UtilsTest.DELTA, "Checking converting to pixel and back to camera is the same value");
		assertEquals(30, cam.toCamX(cam.toPixelX(30)), UtilsTest.DELTA, "Checking converting to pixel and back to camera is the same value");
		assertEquals(-7, cam.toCamX(cam.toPixelX(-7)), UtilsTest.DELTA, "Checking converting to pixel and back to camera is the same value");
	}
	@Test
	public void toPixelX(){
		cam.setZoomBase(2);
		cam.setX(3);
		cam.setXZoomFactor(0);
		assertEquals(10, cam.toPixelX(13), UtilsTest.DELTA, "Checking pixel position correct");
		
		cam.setX(7);
		cam.setXZoomFactor(1);
		assertEquals(10, cam.toPixelX(12), UtilsTest.DELTA, "Checking pixel position correct");
		
		cam.setX(7);
		cam.setXZoomFactor(-1);
		assertEquals(10, cam.toPixelX(27), UtilsTest.DELTA, "Checking pixel position correct");
		
		assertEquals(0, cam.toPixelX(cam.toCamX(0)), UtilsTest.DELTA, "Checking converting to camera and back to pixel is the same value");
		assertEquals(10, cam.toPixelX(cam.toCamX(10)), UtilsTest.DELTA, "Checking converting to camera and back to pixel is the same value");
		assertEquals(-4, cam.toPixelX(cam.toCamX(-4)), UtilsTest.DELTA, "Checking converting to camera and back to pixel is the same value");
	}
	
	@Test
	public void toCamY(){
		cam.setZoomBase(2);
		cam.setY(3);
		cam.setYZoomFactor(0);
		assertEquals(13, cam.toCamY(10), UtilsTest.DELTA, "Checking mouse position correct");
		
		cam.setY(7);
		cam.setYZoomFactor(1);
		assertEquals(12, cam.toCamY(10), UtilsTest.DELTA, "Checking mouse position correct");
		
		cam.setY(7);
		cam.setYZoomFactor(-1);
		assertEquals(27, cam.toCamY(10), UtilsTest.DELTA, "Checking mouse position correct");

		assertEquals(0, cam.toCamY(cam.toPixelY(0)), UtilsTest.DELTA, "Checking converting to pixel and back to camera is the same value");
		assertEquals(30, cam.toCamY(cam.toPixelY(30)), UtilsTest.DELTA, "Checking converting to pixel and back to camera is the same value");
		assertEquals(-7, cam.toCamY(cam.toPixelY(-7)), UtilsTest.DELTA, "Checking converting to pixel and back to camera is the same value");
	}
	@Test
	public void toPixelY(){
		cam.setZoomBase(2);
		cam.setY(3);
		cam.setYZoomFactor(0);
		assertEquals(10, cam.toPixelY(13), UtilsTest.DELTA, "Checking pixel position correct");
		
		cam.setY(7);
		cam.setYZoomFactor(1);
		assertEquals(10, cam.toPixelY(12), UtilsTest.DELTA, "Checking pixel position correct");
		
		cam.setY(7);
		cam.setYZoomFactor(-1);
		assertEquals(10, cam.toPixelY(27), UtilsTest.DELTA, "Checking pixel position correct");
		
		assertEquals(0, cam.toPixelY(cam.toCamY(0)), UtilsTest.DELTA, "Checking converting to camera and back to pixel is the same value");
		assertEquals(10, cam.toPixelY(cam.toCamY(10)), UtilsTest.DELTA, "Checking converting to camera and back to pixel is the same value");
		assertEquals(-4, cam.toPixelY(cam.toCamY(-4)), UtilsTest.DELTA, "Checking converting to camera and back to pixel is the same value");
	}
	
	@Test
	public void camToPixelBounds(){
		cam.setX(100);
		cam.setY(-100);
		cam.setZoomBase(2);
		cam.setXZoomFactor(1);
		cam.setYZoomFactor(1);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(-180, 160, 60, 100),
				cam.camToPixelBounds(10, -20, 30, 50), "Checking bounds correct");
		
		cam.setYZoomFactor(2);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(-180, 320, 60, 200),
				cam.camToPixelBounds(10, -20, 30, 50), "Checking bounds correct with differing zoom factors");
		
		Rectangle2D r = new Rectangle2D.Double(23, -23.6, 322, 204.29);
		Assert.rectangleAproxEqual(r, cam.camToPixelBounds(cam.pixelToCamBounds(r)),
				"Checking converting to pixel and back is equivalent");
	}
	
	@Test
	public void pixelToCamBounds(){
		cam.setX(100);
		cam.setY(-100);
		cam.setZoomBase(2);
		cam.setXZoomFactor(1);
		cam.setYZoomFactor(1);
		
		Assert.rectangleAproxEqual(new Rectangle2D.Double(10, -20, 30, 50),
				cam.pixelToCamBounds(-180, 160, 60, 100), "Checking bounds correct");
		
		cam.setYZoomFactor(2);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(10, -20, 30, 50),
				cam.pixelToCamBounds(-180, 320, 60, 200), "Checking bounds correct with differing zoom factors");

		Rectangle2D r = new Rectangle2D.Double(23, -23.6, 322, 204.29);
		Assert.rectangleAproxEqual(r, cam.pixelToCamBounds(cam.camToPixelBounds(r)),
				"Checking converting to camera and back is equivalent");
	}
	
	@Test
	public void fillRect(){
		assertFalse(cam.fillRect(-100, -100, 10, 10), "Checking rectangle not drawn outside of camera");
		assertTrue(cam.fillRect(-9, -9, 10, 10), "Checking rectangle drawn inside of camera");
		
		assertFalse(cam.fillRect(new Rectangle2D.Double(-100, -100, 10, 10)), "Checking rectangle not drawn outside of camera");
		assertTrue(cam.fillRect(new Rectangle2D.Double(-9, -9, 10, 10)), "Checking rectangle drawn inside of camera");
	}
	
	@Test
	public void fillStringRect(){
		assertTrue(cam.fillStringRect(new Rectangle2D.Double(10, 20, 30, 40)), "Checking valid rectangle is drawn");
		
		assertTrue(cam.fillStringRect(0, 20, 30, 40), "Checking rectangle is drawn with no zooming");
		
		cam.setStringScaleMode(Camera.STRING_SCALE_X_AXIS);
		cam.setZoomBase(2);
		cam.setXZoomFactor(-1);
		cam.setX(7);
		assertFalse(cam.fillStringRect(-25, 20, 30, 40), "Checking rectangle isn't drawn with zooming the string out");
	}
	
	@Test
	public void fillOval(){
		assertFalse(cam.fillOval(-100, -100, 10, 10), "Checking oval not drawn outside of camera");
		assertTrue(cam.fillOval(-9, -9, 10, 10), "Checking oval drawn inside of camera");
		
		assertFalse(cam.fillOval(new Rectangle2D.Double(-100, -100, 10, 10)), "Checking oval not drawn outside of camera");
		assertTrue(cam.fillOval(new Ellipse2D.Double(-9, -9, 10, 10)), "Checking oval drawn inside of camera");
	}
	
	@Test
	public void drawLine(){
		assertFalse(cam.drawLine(-1, -1, -5, -5), "Checking line drawn outside of camera");
		assertFalse(cam.drawLine(-5, -5, -1, -1), "Checking line drawn outside of camera");
		assertFalse(cam.drawLine(-5, -1, -1, -5), "Checking line drawn outside of camera");
		
		assertTrue(cam.drawLine(-5, 1, 1, -5), "Checking line drawn inside of camera");
		assertTrue(cam.drawLine(-5, 1, 5, -1), "Checking line drawn inside of camera");
		assertTrue(cam.drawLine(-1, 1, 1, -5), "Checking line drawn inside of camera");
		assertTrue(cam.drawLine(1, 1, 1, 5), "Checking line drawn inside of camera");
		assertTrue(cam.drawLine(1, 5, 1, 1), "Checking line drawn inside of camera");
	}
	
	@Test
	public void drawString(){
		Font f = new Font("Courier New", Font.PLAIN, 1);
		g.setFont(f);
		assertNotEquals(null, cam.drawString("word", -10, 15, 10), "Checking string drawn inside camera");
		assertEquals(null, cam.drawString("word", -25, 1, 10), "Checking string not drawn outside the camera");
		assertEquals(f, g.getFont(), "Checking font is unchanged after drawing strings");
		
		f = new Font("Courier New", Font.PLAIN, 10);
		g.setFont(f);
		assertNotEquals(null, cam.drawString("word", -10, 15), "Checking string drawn inside camera no font given");
		assertEquals(null, cam.drawString("word", -25, 1), "Checking string not drawn outside the camera no font given");
		assertEquals(f, g.getFont(), "Checking font is unchanged after drawing strings");
	}
	
	@Test
	public void drawImage(){
		assertFalse(cam.drawImage(img, -10, -10, 5, 5), "Checking image not drawn out of bounds");
		assertTrue(cam.drawImage(img, -10, -10, 11, 11), "Checking image drawn in bounds");
		
		assertFalse(cam.drawImage(img, -601, 0), "Checking image not drawn out of bounds, no width height params");
		assertTrue(cam.drawImage(img, -599, 0), "Checking image drawn in bounds, no width height params");
	}
	
	@Test
	public void inBounds(){
		cam.setDrawOnlyInBounds(false);
		assertTrue(cam.inBounds(-2, -2, 1, 1), "Checking out of bounds, but true with disabled drawing only in bounds");

		cam.setDrawOnlyInBounds(true);
		assertFalse(cam.inBounds(-2, -2, 1, 1), "Checking out of bounds");
		assertFalse(cam.inBounds(-2, -2, 2, 2), "Checking out bounds touching corner");
		assertFalse(cam.inBounds(-2, -2, 2, 20), "Checking out bounds touching side");
		assertTrue(cam.inBounds(-2, -2, 2.01, 2.01), "Checking out bounds in corner");
		assertTrue(cam.inBounds(-2, -2, 2.01, 20), "Checking out bounds in side");
	}
	
	@Test
	public void inPixelBounds(){
		assertTrue(cam.inPixelBounds(0, 0, 100, 50), "Checking in bounds inside");
		assertTrue(cam.inPixelBounds(new Rectangle2D.Double(203, 104, 10300, 3892)), "Checking in bounds inside");
		
		assertTrue(cam.inPixelBounds(-99.999, 0, 100, 50), "Checking in bounds left of the camera, but inside");
		assertTrue(cam.inPixelBounds(599.999, 0, 100, 50), "Checking in bounds right of the camera, but inside");
		assertTrue(cam.inPixelBounds(0, -49.999, 100, 50), "Checking in bounds above camera, but inside");
		assertTrue(cam.inPixelBounds(0, 399.999, 100, 50), "Checking in bounds below camera, but inside");
		
		assertFalse(cam.inPixelBounds(-100.001, 0, 100, 50), "Checking not in bounds left of the camera, but outside");
		assertFalse(cam.inPixelBounds(600.001, 0, 100, 50), "Checking not in bounds right of the camera, but outside");
		assertFalse(cam.inPixelBounds(0, -50.001, 100, 50), "Checking not in bounds above camera, but outside");
		assertFalse(cam.inPixelBounds(0, 400.001, 100, 50), "Checking not in bounds below camera, but outside");
		
		cam.setDrawOnlyInBounds(false);
		assertTrue(cam.inPixelBounds(0, 400.001, 100, 50), "Checking out of bounds is drawn with drawing only in bounds disabled");
	}
	
	@Test
	public void updateFontSize(){
		Font old = new Font("Arial", Font.PLAIN, 40);
		g.setFont(old);
		Font expectedFont = new Font("Arial", Font.PLAIN, 20);
		cam.setStringScaleMode(Camera.STRING_SCALE_Y_AXIS);
		assertEquals(old, cam.updateFontSize(20), "Checking original font returned");
		assertEquals(expectedFont, g.getFont(), "Checking font size set accordingly");
		
		g.setFont(old);
		cam.setStringScaleMode(Camera.STRING_SCALE_X_AXIS);
		assertEquals(old, cam.updateFontSize(), "Checking original font returned");
		assertEquals(old, g.getFont(), "Checking font the same when scaling on the x axis");
	}
	
	@Test
	public void stringPixelSize(){
		g.setFont(new Font("Times New Roman", Font.PLAIN, 45));
		Font f = g.getFont();
		assertEquals(new Size2D(12, 42), cam.stringPixelSize("j"), "Checking size correct");
		assertEquals(f, g.getFont(), "Checking font unchanged after method call");
		
		assertEquals(new Size2D(24, 42), cam.stringPixelSize("jj"), "Checking size correct");
		
		assertEquals(new Size2D(197, 22), cam.stringPixelSize("nnnnnoooo"), "Checking size correct");
	}
	
	@Test
	public void stringPixelOffset(){
		g.setFont(new Font("Times New Roman", Font.PLAIN, 45));
		Font f = g.getFont();
		assertEquals(new Size2D(4, 32), cam.stringPixelOffset("j"), "Checking offset correct");
		assertEquals(f, g.getFont(), "Checking font unchanged after method call");
		
		assertEquals(new Size2D(4, 32), cam.stringPixelOffset("jj"), "Checking offset correct");
		
		assertEquals(new Size2D(0, 21), cam.stringPixelOffset("nnnnnoooo"), "Checking offset correct");
	}
	
	@Test
	public void stringPixelBoundsPos(){
		assertEquals(new Point2D.Double(99, 91),
				cam.stringPixelBoundsPos("j|aWl", 100, 100),
				"Checking position in default alignment");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_MIN);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MIN);
		assertEquals(new Point2D.Double(100, 100),
				cam.stringPixelBoundsPos("j|aWl", 100, 100),
				"Checking position unchanged with minimum alignment, i.e. the upper left hand corner");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_CENTER);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MAX);
		assertEquals(new Point2D.Double(86.5, 89),
				cam.stringPixelBoundsPos("j|aWl", 100, 100),
				"Checking position with center and max");
	}
	
	@Test
	public void stringPixelDrawPos(){
		assertEquals(new Point2D.Double(100, 100),
				cam.stringPixelDrawPos("j|aWl", 100, 100),
				"Checking position unchanged in default alignment");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_MIN);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MIN);
		assertEquals(new Point2D.Double(101, 109),
				cam.stringPixelDrawPos("j|aWl", 100, 100),
				"Checking position with minimum alignment, i.e. the upper left hand corner");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_CENTER);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MAX);
		assertEquals(new Point2D.Double(87.5, 98),
				cam.stringPixelDrawPos("j|aWl", 100, 100),
				"Checking position with center and max");
	}
	
	@Test
	public void stringPixelBounds(){
		g.setFont(new Font("Dialog", Font.PLAIN, 12));
		Assert.rectangleAproxEqual(new Rectangle2D.Double(99, 91, 27, 11),
				cam.stringPixelBounds("j|aWl", 100, 100),
				"Checking bounds default alignment");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_MIN);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MIN);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(100, 100, 22, 11),
				cam.stringPixelBounds("|aWl", 100, 100),
				"Checking bounds with minimum alignment, i.e. the upper left hand corner");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_CENTER);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MAX);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(94, 93, 12, 7),
				cam.stringPixelBounds("oo", 100, 100),
				"Checking bounds with center and max");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_DEFAULT);
		cam.setStringYAlignment(Camera.STRING_ALIGN_DEFAULT);
		cam.setXZoomFactor(1);
		cam.setYZoomFactor(2);
		cam.setZoomBase(2);
		cam.setStringScaleMode(Camera.STRING_SCALE_X_AXIS);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(201, 387, 24, 13),
				cam.stringPixelBounds("oo", 100, 100),
				"Checking bounds with differing x and y axes zoom levels");
		
		cam.setX(140);
		cam.setY(-478);
		cam.setXZoomFactor(1);
		cam.setYZoomFactor(2);
		cam.setZoomBase(2);
		cam.setStringScaleMode(Camera.STRING_SCALE_Y_AXIS);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(-78, 2285, 50, 27),
				cam.stringPixelBounds("oo", 100, 100),
				"Checking bounds with differing x and y axes zoom levels");
	}
	
	@Test
	public void stringCamBounds(){
		g.setFont(new Font("Dialog", Font.PLAIN, 12));
		Assert.rectangleAproxEqual(new Rectangle2D.Double(98, 82, 27, 11),
				cam.stringCamBounds("j|aWl", 99, 91),
				"Checking bounds default alignment");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_MIN);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MIN);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(100, 100, 22, 11),
				cam.stringCamBounds("|aWl", 100, 100),
				"Checking bounds with minimum alignment, i.e. the upper left hand corner");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_CENTER);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MAX);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(88, 86, 12, 7),
				cam.stringCamBounds("oo", 94, 93),
				"Checking bounds with center and max");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_DEFAULT);
		cam.setStringYAlignment(Camera.STRING_ALIGN_DEFAULT);
		cam.setXZoomFactor(1);
		cam.setYZoomFactor(2);
		cam.setZoomBase(2);
		cam.setStringScaleMode(Camera.STRING_SCALE_X_AXIS);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(201.5, 383.75, 12, 6.5),
				cam.stringCamBounds("oo", 201, 387),
				"Checking bounds with differing x and y axes zoom levels");
		
		cam.setX(140);
		cam.setY(-478);
		cam.setXZoomFactor(1);
		cam.setYZoomFactor(2);
		cam.setZoomBase(2);
		cam.setStringScaleMode(Camera.STRING_SCALE_Y_AXIS);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(-77, 2278.25, 12.5, 6.75),
				cam.stringCamBounds("oo", -78, 2285),
				"Checking bounds with differing x and y axes zoom levels");
	}
	
	@Test
	public void stringCamToPixelBounds(){
		cam.setZoomBase(2);
		cam.setXZoomFactor(1);
		cam.setYZoomFactor(0);
		cam.setX(15);
		cam.setY(30);
		cam.setStringScaleMode(Camera.STRING_SCALE_X_AXIS);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(-30, -30, 48, 20),
				cam.stringCamToPixelBounds(new Rectangle2D.Double(0, 0, 24, 10)),
				"Checking bounds updated from left hand corner with x axis zooming");

		cam.setX(0);
		cam.setY(0);
		cam.setStringScaleMode(Camera.STRING_SCALE_Y_AXIS);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(60, 20, 24, 10),
				cam.stringCamToPixelBounds(30, 20, 24, 10),
				"Checking bounds unchanged from left hand corner with y axis zooming");
		
		cam.setStringScaleMode(Camera.STRING_SCALE_X_AXIS);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(60, 20, 48, 20),
				cam.stringCamToPixelBounds(30, 20, 24, 10),
				"Checking bounds updated based on upper left hand corner");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_CENTER);
		cam.setStringYAlignment(Camera.STRING_ALIGN_CENTER);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(60, 15, 48, 20),
				cam.stringCamToPixelBounds(30, 20, 24, 10),
				"Checking bounds updated based on center");

		cam.setStringXAlignment(Camera.STRING_ALIGN_CENTER);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MAX);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(60, 10, 48, 20), 
				cam.stringCamToPixelBounds(30, 20, 24, 10),
				"Checking bounds updated based on bottom edge center");
		
		Rectangle2D r = new Rectangle2D.Double(32.345, -2345.12, 123.4, 238.638);
		Assert.rectangleAproxEqual(r, cam.stringCamToPixelBounds(cam.stringPixelToCamBounds(r)),
				"Checking bounds equal after converting to camera bounds and back");
	}
	
	@Test
	public void stringPixelToCamBounds(){
		cam.setZoomBase(2);
		cam.setXZoomFactor(1);
		cam.setYZoomFactor(0);
		cam.setX(15);
		cam.setY(30);
		cam.setStringScaleMode(Camera.STRING_SCALE_X_AXIS);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(15, 30, 20, 20),
				cam.stringPixelToCamBounds(new Rectangle2D.Double(0, 0, 40, 40)),
				"Checking bounds updated from left hand corner with x axis zooming");
		
		cam.setX(0);
		cam.setY(0);
		cam.setStringScaleMode(Camera.STRING_SCALE_Y_AXIS);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(9, 30, 20, 40),
				cam.stringPixelToCamBounds(18, 30, 20, 40),
				"Checking bounds unchanged from left hand corner with y axis zooming");
		
		cam.setStringScaleMode(Camera.STRING_SCALE_X_AXIS);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(9, 30, 20, 40),
				cam.stringPixelToCamBounds(18, 30, 40, 80),
				"Checking bounds updated based on upper left hand corner");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_CENTER);
		cam.setStringYAlignment(Camera.STRING_ALIGN_CENTER);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(9, 50, 20, 40),
				cam.stringPixelToCamBounds(18, 30, 40, 80),
				"Checking bounds updated based on center");

		cam.setStringXAlignment(Camera.STRING_ALIGN_CENTER);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MAX);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(9, 70, 20, 40),
				cam.stringPixelToCamBounds(18, 30, 40, 80),
				"Checking bounds updated based on bottom edge center");
		
		Rectangle2D r = new Rectangle2D.Double(32.345, -2345.12, 123.4, 238.638);
		Assert.rectangleAproxEqual(r, cam.stringPixelToCamBounds(cam.stringCamToPixelBounds(r)),
				"Checking bounds equal after converting to pixel bounds and back");
	}
	
	@Test
	public void stringZoom(){
		cam.setZoomBase(2);
		cam.setXZoomFactor(1);
		cam.setYZoomFactor(2);
		assertEquals(4, cam.stringZoom(4), "Checking value unchanged with string scaling disabled");
		
		cam.setStringScaleMode(Camera.STRING_SCALE_X_AXIS);
		assertEquals(8, cam.stringZoom(4), "Checking value zoomed on x axis with string scaling on x axis");
		
		cam.setStringScaleMode(Camera.STRING_SCALE_Y_AXIS);
		assertEquals(16, cam.stringZoom(4), "Checking value zoomed on y axis with string scaling on y axis");
	}
	
	@Test
	public void stringInverseZoom(){
		cam.setZoomBase(2);
		cam.setXZoomFactor(1);
		cam.setYZoomFactor(2);
		assertEquals(4, cam.stringInverseZoom(4), "Checking value unchanged with string scaling disabled");
		
		cam.setStringScaleMode(Camera.STRING_SCALE_X_AXIS);
		assertEquals(2, cam.stringInverseZoom(4), "Checking value zoomed on x axis with string scaling on x axis");
		
		cam.setStringScaleMode(Camera.STRING_SCALE_Y_AXIS);
		assertEquals(1, cam.stringInverseZoom(4), "Checking value zoomed on y axis with string scaling on y axis");
	}
	
	@Test
	public void alignPixelString(){
		cam.setStringScaleMode(Camera.STRING_SCALE_Y_AXIS);
		cam.setZoomBase(2);
		cam.setYZoomFactor(1);
		g.setFont(new Font("Arial", Font.PLAIN, 50));
		assertEquals(new Size2D(0, 0), cam.alignPixelString("j"), "Checking offset is zero with default alignment mode");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_MIN);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MAX);
		assertEquals(new Size2D(5, -21), cam.alignPixelString("j"), "Checking offset near default");
		
		assertEquals(new Size2D(-7, 0), cam.alignPixelString("i"), "Checking offset with no height alignment");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_MAX);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MIN);
		assertEquals(new Size2D(-318, 72), cam.alignPixelString("10mqD"), "Checking offset with large alignment");
	}
	
	@Test
	public void alignCameraString(){
		g.setFont(new Font("Arial", Font.PLAIN, 100));
		assertEquals(new Size2D(0, 0), cam.alignCameraString("j"), "Checking offset is zero with default alignment mode");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_MIN);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MAX);
		assertEquals(new Size2D(5, -21), cam.alignCameraString("j"), "Checking offset near default");
		
		assertEquals(new Size2D(-7, 0), cam.alignCameraString("i"), "Checking offset with no height alignment");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_MAX);
		cam.setStringYAlignment(Camera.STRING_ALIGN_MIN);
		assertEquals(new Size2D(-318, 72), cam.alignCameraString("10mqD"), "Checking offset with large alignment");
	}
	
	@Test
	public void alignStringX(){
		assertEquals(0, cam.alignStringX(10, 2), "Checking x alignment is initialized default");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_CENTER);
		assertEquals(-3, cam.alignStringX(10, 2), "Checking x alignment updated");
	}
	@Test
	public void alignStringY(){
		assertEquals(0, cam.alignStringY(10, 2), "Checking y alignment is initialized default");
		
		cam.setStringYAlignment(Camera.STRING_ALIGN_CENTER);
		assertEquals(-3, cam.alignStringY(10, 2), "Checking y alignment updated");
	}
	
	@Test
	public void alignString(){
		assertEquals(0, Camera.alignString(-1, 10, 2), "Checking unrecognized alignment uses default alignment");
		assertEquals(0, Camera.alignString(Camera.STRING_ALIGN_DEFAULT, 10, 2), "Checking default alignment");
		assertEquals(-3, Camera.alignString(Camera.STRING_ALIGN_CENTER, 10, 2), "Checking center alignment");
		assertEquals(2, Camera.alignString(Camera.STRING_ALIGN_MIN, 10, 2), "Checking min alignment");
		assertEquals(-8, Camera.alignString(Camera.STRING_ALIGN_MAX, 10, 2), "Checking max alignment");
	}
	
	@Test
	public void getX(){
		assertEquals(0, cam.getX(), "Checking x initialized");
	}
	@Test
	public void setX(){
		cam.setX(100);
		assertEquals(100, cam.getX(), UtilsTest.DELTA, "Checking x set");
		
		cam.setX(1001);
		assertEquals(1000, cam.getX(), UtilsTest.DELTA, "Checking x capped at max range");
		
		cam.setX(-1001);
		assertEquals(-1000, cam.getX(), UtilsTest.DELTA, "Checking x capped at min range");
	}
	@Test
	public void addX(){
		cam.setX(3);
		cam.addX(200);
		assertEquals(203, cam.getX(), UtilsTest.DELTA, "Checking x added correctly");
	}
	
	@Test
	public void getY(){
		assertEquals(0, cam.getY(), "Checking y initialized");
	}
	@Test
	public void setY(){
		cam.setY(201);
		assertEquals(201, cam.getY(), UtilsTest.DELTA, "Checking y set");
		
		cam.setY(2001);
		assertEquals(2000, cam.getY(), UtilsTest.DELTA, "Checking y capped at max range");
		
		cam.setY(-2001);
		assertEquals(-2000, cam.getY(), UtilsTest.DELTA, "Checking y capped at min range");
	}
	@Test
	public void addY(){
		cam.setY(6);
		cam.addY(500);
		assertEquals(506, cam.getY(), UtilsTest.DELTA, "Checking y added correctly");
	}
	
	@Test
	public void getWidth(){
		assertEquals(600, cam.getWidth(), "Checking width initialized");
	}
	@Test
	public void setWidth(){
		cam.setWidth(300);
		assertEquals(300, cam.getWidth(), "Checking width set");
	}
	
	@Test
	public void getHeight(){
		assertEquals(400, cam.getHeight(), "Checking height initialized");
	}
	@Test
	public void setHeight(){
		cam.setHeight(250);
		assertEquals(250, cam.getHeight(), "Checking height set");
	}
	
	@Test
	public void getXZoomFactor(){
		assertEquals(0, cam.getXZoomFactor(), "Checking x zoom factor initialized");
	}
	@Test
	public void setXZoomFactor(){
		cam.setXZoomFactor(1);
		assertEquals(1, cam.getXZoomFactor(), "Checking x zoom factor set");
	}
	
	@Test
	public void getYZoomFactor(){
		assertEquals(0, cam.getYZoomFactor(), "Checking y zoom factor initialized");
	}
	@Test
	public void setYZoomFactor(){
		cam.setYZoomFactor(-2);
		assertEquals(-2, cam.getYZoomFactor(), "Checking y zoom factor set");
	}
	
	@Test
	public void getZoomBase(){
		assertEquals(Camera.DEFAULT_ZOOM_BASE, cam.getZoomBase(), "Checking default zoom base set");
	}
	@Test
	public void setZoomBase(){
		cam.setZoomBase(2);
		assertEquals(2, cam.getZoomBase(), "Checking zoom base set");
		
		cam.setZoomBase(3.1);
		assertEquals(3.1, cam.getZoomBase(), "Checking zoom base set");
	}
	
	@Test
	public void getMinX(){
		assertEquals(-1000, cam.getMinX(), "Checking min x initialized");
	}
	@Test
	public void setMinX(){
		cam.setMinX(-500);
		assertEquals(-500, cam.getMinX(), "Checking min x set");
	}
	
	@Test
	public void getMinY(){
		assertEquals(-2000, cam.getMinY(), "Checking min y initialized");
	}
	@Test
	public void setMinY(){
		cam.setMinY(-300);
		assertEquals(-300, cam.getMinY(), "Checking min y set");
	}
	
	@Test
	public void getMaxX(){
		assertEquals(1000, cam.getMaxX(), "Checking max x initialized");
	}
	@Test
	public void setMaxX(){
		cam.setMaxX(600);
		assertEquals(600, cam.getMaxX(), "Checking max x set");
	}
	
	@Test
	public void getMaxY(){
		assertEquals(2000, cam.getMaxY(), "Checking max y initialized");
	}
	@Test
	public void setMaxY(){
		cam.setMaxY(700);
		assertEquals(700, cam.getMaxY(), "Checking max y set");
	}
	
	@Test
	public void getGraphics(){
		assertEquals(g, cam.getGraphics(), "Checking graphics initialized");
	}
	@Test
	public void setGraphics(){
		BufferedImage newImg = new BufferedImage(60, 40, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D newG = (Graphics2D)newImg.getGraphics();
		cam.setGraphics(newG);
		assertEquals(newG, cam.getGraphics(), "Checking graphics set");
	}
	
	@Test
	public void isDrawOnlyInBounds(){
		assertTrue(cam.isDrawOnlyInBounds(), "Checking draw in bounds initialized");
	}
	@Test
	public void setDrawOnlyInBounds(){
		cam.setDrawOnlyInBounds(false);
		assertFalse(cam.isDrawOnlyInBounds(), "Checking draw in bounds set");
	}
	
	@Test
	public void getStringScaleMode(){
		assertEquals(Camera.STRING_SCALE_NONE, cam.getStringScaleMode(), "Checking default scale mode set");
	}
	@Test
	public void setStringScaleMode(){
		cam.setStringScaleMode(Camera.STRING_SCALE_Y_AXIS);
		assertEquals(Camera.STRING_SCALE_Y_AXIS, cam.getStringScaleMode(), "Checking scale mode set");
		
		cam.setStringScaleMode(Camera.STRING_SCALE_NONE);
		assertEquals(Camera.STRING_SCALE_NONE, cam.getStringScaleMode(), "Checking scale mode set");
	}
	
	@Test
	public void getStringXAlignment(){
		assertEquals(Camera.STRING_ALIGN_DEFAULT, cam.getStringXAlignment(), "Checking default x string alignment");
	}
	@Test
	public void setStringXAlignment(){
		cam.setStringXAlignment(Camera.STRING_ALIGN_MAX);
		assertEquals(Camera.STRING_ALIGN_MAX, cam.getStringXAlignment(), "Checking x string alignment set");
		
		cam.setStringXAlignment(Camera.STRING_ALIGN_MIN);
		assertEquals(Camera.STRING_ALIGN_MIN, cam.getStringXAlignment(), "Checking x string alignment set");
	}
	
	@Test
	public void getStringYAlignment(){
		assertEquals(Camera.STRING_ALIGN_DEFAULT, cam.getStringYAlignment(), "Checking default y string alignment");
	}
	@Test
	public void setStringYAlignment(){
		cam.setStringYAlignment(Camera.STRING_ALIGN_MAX);
		assertEquals(Camera.STRING_ALIGN_MAX, cam.getStringYAlignment(), "Checking y string alignment set");
		
		cam.setStringYAlignment(Camera.STRING_ALIGN_MIN);
		assertEquals(Camera.STRING_ALIGN_MIN, cam.getStringYAlignment(), "Checking y string alignment set");
	}
	
	@Test
	public void zoomed(){
		cam.setZoomBase(1.5);
		assertEquals(32.7235580491, cam.zoomed(7.3, 3.7), UtilsTest.DELTA, "Checking zoom value");

		cam.setZoomBase(1.2);
		assertEquals(14.33156014117766, Camera.zoom(7.3, 3.7), UtilsTest.DELTA, "Checking zoom value");
	}
	
	@Test
	public void inverseZoomed(){
		cam.setZoomBase(3.2);
		assertEquals(0.13819790847, cam.inverseZoomed(9.1, 3.6), UtilsTest.DELTA, "Checking inverse zoom value");

		cam.setZoomBase(1.2);
		assertEquals(4.720509832217587, cam.inverseZoomed(9.1, 3.6), UtilsTest.DELTA, "Checking inverse zoom value");
	}
	
	@Test
	public void zoomedValue(){
		cam.setZoomBase(1.7);
		assertEquals(327.9400745358637, cam.zoomedValue(240, 300, 329, 10, -1.2), UtilsTest.DELTA, "Checking zoomed value");

		cam.setZoomBase(1.2);
		assertEquals(319.5203427076433, cam.zoomedValue(240, 300, 329, 10, -1.2), UtilsTest.DELTA, "Checking zoomed value");
	}
	
	@Test
	public void zoom(){
		assertEquals(2, Camera.zoom(1, 2, 1), UtilsTest.DELTA, "Checking zoom value");
		assertEquals(1, Camera.zoom(1, 2, 0), UtilsTest.DELTA, "Checking zoom value");
		assertEquals(0.5, Camera.zoom(1, 2, -1), UtilsTest.DELTA, "Checking zoom value");
		assertEquals(16, Camera.zoom(2, 2, 3), UtilsTest.DELTA, "Checking zoom value");
		
		assertEquals(128, Camera.zoom(2, 4, 3), UtilsTest.DELTA, "Checking zoom value");
		assertEquals(32, Camera.zoom(0.5, 4, 3), UtilsTest.DELTA, "Checking zoom value");
		assertEquals(32.7235580491, Camera.zoom(7.3, 1.5, 3.7), UtilsTest.DELTA, "Checking zoom value");
		
		assertEquals(14.33156014117766, Camera.zoom(7.3, 3.7), UtilsTest.DELTA, "Checking zoom value");
	}
	
	@Test
	public void inverseZoom(){
		assertEquals(0.5, Camera.inverseZoom(1, 2, 1), UtilsTest.DELTA, "Checking inverse zoom value");
		assertEquals(1, Camera.inverseZoom(1, 2, 0), UtilsTest.DELTA, "Checking inverse zoom value");
		assertEquals(2, Camera.inverseZoom(1, 2, -1), UtilsTest.DELTA, "Checking inverse zoom value");
		assertEquals(0.25, Camera.inverseZoom(2, 2, 3), UtilsTest.DELTA, "Checking inverse zoom value");
		
		assertEquals(0.13819790847, Camera.inverseZoom(9.1, 3.2, 3.6), UtilsTest.DELTA, "Checking inverse zoom value");
		assertEquals(4.720509832217587, Camera.inverseZoom(9.1, 3.6), UtilsTest.DELTA, "Checking inverse zoom value");
	}
	
	@Test
	public void zoomValue(){
		assertEquals(12.5, Camera.zoomValue(5, 600, 10, 0, 2, 1), UtilsTest.DELTA, "Checking zoomed value");
		assertEquals(10, Camera.zoomValue(0, 600, 10, 0, 2, 1), UtilsTest.DELTA, "Checking zoomed value");
		assertEquals(10.5, Camera.zoomValue(1, 600, 10, 0, 2, 1), UtilsTest.DELTA, "Checking zoomed value");
		assertEquals(13, Camera.zoomValue(4, 600, 10, 0, 2, 2), UtilsTest.DELTA, "Checking zoomed value");
		assertEquals(5, Camera.zoomValue(5, 600, 10, 0, 2, -1), UtilsTest.DELTA, "Checking zoomed value");
		
		assertEquals(327.9400745358637, Camera.zoomValue(240, 300, 329, 10, 1.7, -1.2), UtilsTest.DELTA, "Checking zoomed value");
		assertEquals(319.5203427076433, Camera.zoomValue(240, 300, 329, 10, -1.2), UtilsTest.DELTA, "Checking zoomed value");
	}
	
	@Test
	public void drawValue(){
		assertEquals(10, Camera.drawValue(10), "Checking number rounds to itself");
		assertEquals(10, Camera.drawValue(10.1), "Checking number rounds down");
		assertEquals(10, Camera.drawValue(10.49), "Checking number rounds down");
		assertEquals(11, Camera.drawValue(10.5), "Checking number rounds up");
		assertEquals(11, Camera.drawValue(10.9), "Checking number rounds up");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("1.0 2.3 3.4 4.4 5.4 6.7 6.8 7.8 8.6 9.3 0.2 false 0 1 2 \n");
		assertTrue(cam.load(scan), "Checking load succeeds");
		assertEquals(1.0, cam.getX(), "Checking x loaded");
		assertEquals(2.3, cam.getY(), "Checking y loaded");
		assertEquals(3.4, cam.getWidth(), "Checking width loaded");
		assertEquals(4.4, cam.getHeight(), "Checking height loaded");
		assertEquals(5.4, cam.getXZoomFactor(), "Checking x zoom factor loaded");
		assertEquals(6.7, cam.getYZoomFactor(), "Checking y zoom factor loaded");
		assertEquals(6.8, cam.getZoomBase(), "Checking zoom base loaded");
		assertEquals(7.8, cam.getMinX(), "Checking min x factor loaded");
		assertEquals(8.6, cam.getMinY(), "Checking min y factor loaded");
		assertEquals(9.3, cam.getMaxX(), "Checking max x factor loaded");
		assertEquals(0.2, cam.getMaxY(), "Checking max y factor loaded");
		assertEquals(false, cam.isDrawOnlyInBounds(), "Checking draw in bounds factor loaded");
		assertEquals(0, cam.getStringScaleMode(), "Checking string scale mode loaded");
		assertEquals(1, cam.getStringXAlignment(), "Checking string x alignment loaded");
		assertEquals(2, cam.getStringYAlignment(), "Checking string y alignment loaded");
		
		scan.close();
		scan = new Scanner("a 2.3 3.4 4.4 5.4 6.7 6.8 7.8 8.6 9.3 0.2 false \n");
		assertFalse(cam.load(scan), "Checking load fails with invalid doubles");
		
		scan.close();
		scan = new Scanner("1.0 2.3 3.4 4.4 5.4 6.7 6.8 7.8 8.6 9.3 0.2 g \n");
		assertFalse(cam.load(scan), "Checking load fails with invalid boolean");
		
		scan.close();
		scan = new Scanner("1.0 2.3 3.4 4.4 5.4 6.7 6.8 7.8 8.6 9.3 0.2 true 1.0 0 0 \n");
		assertFalse(cam.load(scan), "Checking load fails with invalid ints");
		
		scan.close();
	}
	
	@Test
	public void save(){
		assertEquals("0.0 0.0 600.0 400.0 0.0 0.0 1.2 -1000.0 -2000.0 1000.0 2000.0 true 0 0 0 \n", UtilsTest.testSave(cam), "Checking save successful");
		
		cam.setX(100);
		cam.setY(200);
		cam.setDrawOnlyInBounds(false);
		assertEquals("100.0 200.0 600.0 400.0 0.0 0.0 1.2 -1000.0 -2000.0 1000.0 2000.0 false 0 0 0 \n", UtilsTest.testSave(cam), "Checking save successful");
	}
	
	@AfterEach
	public void end(){}

}
