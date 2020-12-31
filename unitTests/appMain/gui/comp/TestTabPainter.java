package appMain.gui.comp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.util.Camera;
import appUtils.ZabAppSettings;

public class TestTabPainter{

	private TabPainter paint;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		paint = new TabPainter(400, 340);
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
	public void getCamera(){
		Camera cam = paint.getCamera();
		assertEquals(400, cam.getWidth(), "Checking camera width initialized");
		assertEquals(340, cam.getHeight(), "Checking camera height initialized");
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
	public void paint(){
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = (Graphics2D)img.getGraphics();
		paint.paint(g);
		assertEquals(g, paint.getCamera().getG(), "Checking graphics object set and used");
	}
	
	@AfterEach
	public void end(){}

}
