package appMain.gui.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
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
		cam.setG(g);
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
	public void drawX(){
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
	public void fillRect(){
		assertFalse(cam.fillRect(-100, -100, 10, 10), "Checking rectangle not drawn outside of camera");
		assertTrue(cam.fillRect(-9, -9, 10, 10), "Checking rectangle drawn inside of camera");
	}
	
	@Test
	public void fillOval(){
		assertFalse(cam.fillOval(-100, -100, 10, 10), "Checking oval not drawn outside of camera");
		assertTrue(cam.fillOval(-9, -9, 10, 10), "Checking oval drawn inside of camera");
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
		assertTrue(cam.drawString("word", -10, 15, 10), "Checking string drawn inside camera");
		assertFalse(cam.drawString("word", -25, 1, 10), "Checking string not drawn outside the camera");
		assertEquals(f, g.getFont(), "Checking font is unchanged after drawing strings");
		
		f = new Font("Courier New", Font.PLAIN, 10);
		g.setFont(f);
		assertTrue(cam.drawString("word", -10, 15), "Checking string drawn inside camera no font given");
		assertFalse(cam.drawString("word", -25, 1), "Checking string not drawn outside the camera no font given");
		assertEquals(f, g.getFont(), "Checking font is unchanged after drawing strings");
	}
	
	@Test
	public void drawScaleString(){
		g.setFont(new Font("Courier New", Font.PLAIN, 10));
		cam.setXZoomFactor(1);
		assertTrue(cam.drawString("word", -20, 15), "Checking string drawn inside camera");
		assertFalse(cam.drawString("word", -49, 1), "Checking string not drawn outside the camera");
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
	public void getG(){
		assertEquals(g, cam.getG(), "Checking graphics initialized");
	}
	
	@Test
	public void setG(){
		BufferedImage newImg = new BufferedImage(60, 40, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D newG = (Graphics2D)newImg.getGraphics();
		cam.setG(newG);
		assertEquals(newG, cam.getG(), "Checking graphics set");
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
	public void zoom(){
		assertEquals(2, Camera.zoom(1, 1), UtilsTest.DELTA, "Checking zoom value");
		assertEquals(1, Camera.zoom(1, 0), UtilsTest.DELTA, "Checking zoom value");
		assertEquals(0.5, Camera.zoom(1, -1), UtilsTest.DELTA, "Checking zoom value");
		assertEquals(16, Camera.zoom(2, 3), UtilsTest.DELTA, "Checking zoom value");
	}
	
	@Test
	public void inverseZoom(){
		assertEquals(0.5, Camera.inverseZoom(1, 1), UtilsTest.DELTA, "Checking inverse zoom value");
		assertEquals(1, Camera.inverseZoom(1, 0), UtilsTest.DELTA, "Checking inverse zoom value");
		assertEquals(2, Camera.inverseZoom(1, -1), UtilsTest.DELTA, "Checking inverse zoom value");
		assertEquals(0.25, Camera.inverseZoom(2, 3), UtilsTest.DELTA, "Checking inverse zoom value");
	
	}
	
	@Test
	public void zoomValue(){
		assertEquals(12.5, Camera.zoomValue(5, 600, 10, 0, 1), UtilsTest.DELTA, "Checking zoomed value");
		assertEquals(10, Camera.zoomValue(0, 600, 10, 0, 1), UtilsTest.DELTA, "Checking zoomed value");
		assertEquals(10.5, Camera.zoomValue(1, 600, 10, 0, 1), UtilsTest.DELTA, "Checking zoomed value");
		assertEquals(13, Camera.zoomValue(4, 600, 10, 0, 2), UtilsTest.DELTA, "Checking zoomed value");
		assertEquals(5, Camera.zoomValue(5, 600, 10, 0, -1), UtilsTest.DELTA, "Checking zoomed value");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("1.0 2.3 3.4 4.4 5.4 6.7 7.8 8.6 9.3 0.2 false\n");
		assertTrue(cam.load(scan), "Checking load succeeds");
		assertEquals(1.0, cam.getX(), "Checking x loaded");
		assertEquals(2.3, cam.getY(), "Checking y loaded");
		assertEquals(3.4, cam.getWidth(), "Checking width loaded");
		assertEquals(4.4, cam.getHeight(), "Checking height loaded");
		assertEquals(5.4, cam.getXZoomFactor(), "Checking x zoom factor loaded");
		assertEquals(6.7, cam.getYZoomFactor(), "Checking y zoom factor loaded");
		assertEquals(7.8, cam.getMinX(), "Checking min x factor loaded");
		assertEquals(8.6, cam.getMinY(), "Checking min y factor loaded");
		assertEquals(9.3, cam.getMaxX(), "Checking max x factor loaded");
		assertEquals(0.2, cam.getMaxY(), "Checking max y factor loaded");
		assertEquals(false, cam.isDrawOnlyInBounds(), "Checking draw in bounds factor loaded");
		
		scan.close();
		scan = new Scanner("a 2.3 3.4 4.4 5.4 6.7 7.8 8.6 9.3 0.2 false\n");
		assertFalse(cam.load(scan), "Checking load fails with invalid doubles");
		
		scan.close();
		scan = new Scanner("1.0 2.3 3.4 4.4 5.4 6.7 7.8 8.6 9.3 0.2 g\n");
		assertFalse(cam.load(scan), "Checking load fails with invalid boolean");
		
		scan.close();
	}
	
	@Test
	public void save(){
		assertEquals("0.0 0.0 600.0 400.0 0.0 0.0 -1000.0 -2000.0 1000.0 2000.0 true \n", UtilsTest.testSave(cam), "Checking save successful");
	}
	
	@AfterEach
	public void end(){}

}
