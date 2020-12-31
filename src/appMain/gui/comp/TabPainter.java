package appMain.gui.comp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import appMain.gui.util.Camera;
import tab.Tab;

/**
 * A class used to handle drawing a {@link Tab} to a screen
 * @author zrona
 */
public class TabPainter extends ZabPanel{
	private static final long serialVersionUID = 1L;
	
	/** The camera used to control drawing the graphics with the {@link #tabScreen} */
	private Camera tabCamera;
	
	/** The width, in pixels, of the paintable area */
	private int paintWidth;
	/** The height, in pixels, of the paintable area */
	private int paintHeight;
	
	/**
	 * Create a new {@link TabPainter} at the given size
	 * @param width See {@link #width}
	 * @param height See {@link #height}
	 */
	public TabPainter(int width, int height){
		super();
		this.tabCamera = new Camera(width, height);
		this.setPaintSize(width, height);
	}
	
	/**
	 * Get the width of this {@link TabPainter}
	 * @return See {@link #width}
	 */
	public int getPaintWidth(){
		return this.paintWidth;
	}

	/**
	 * Set the width of this {@link TabPainter}<br>
	 * Will also update all relevant objects, and repaint the object to update.
	 * @param width See {@link #width}
	 */
	public void setPaintWidth(int width){
		this.setPaintSize(width, this.getPaintHeight());
	}

	/**
	 * Get the height of this {@link TabPainter}
	 * @return See {@link #height}
	 */
	public int getPaintHeight(){
		return this.paintHeight;
	}

	/**
	 * Set the height of this {@link TabPainter}<br>
	 * Will also update all relevant objects, and repaint the object to update.
	 * @param height See {@link #height}
	 */
	public void setPaintHeight(int height){
		this.setPaintSize(this.getPaintWidth(), height);
	}
	
	/**
	 * Set both the height and width of the paintable area.<br>
	 * Will also update all relevant objects, and repaint the object to update.
	 * @param width See {@link #width}
	 * @param height See {@link #height}
	 */
	public void setPaintSize(int width, int height){
		this.paintWidth = width;
		this.paintHeight = height;
		this.tabCamera.setWidth(width);
		this.tabCamera.setHeight(height);
		
		this.setPreferredSize(new Dimension(width, height));
		this.repaint();
	}
	
	/**
	 * Get the {@link Camera} this {@link TabPainter} uses for rendering
	 * @return
	 */
	public Camera getCamera(){
		return this.tabCamera;
	}
	
	/** Draw the {@link Tab} to the screen */
	@Override
	public void paint(Graphics gr){
		// TODO This is only placeholder rendering code
		Graphics2D g = (Graphics2D)gr;
		this.tabCamera.setG(g);
		
		g.setColor(new Color(100, 100, 100));
		this.tabCamera.fillRect(0, 0, this.getPaintWidth(), this.getPaintHeight());
		
		g.setColor(new Color(150, 0, 0));
		this.tabCamera.fillRect(100, 100, 100, 100);
	}
}