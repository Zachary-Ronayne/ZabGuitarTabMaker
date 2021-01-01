package appMain.gui.comp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import appMain.gui.util.Camera;
import tab.Tab;
import tab.TabString;
import tab.symbol.TabSymbol;

/**
 * A class used to handle drawing a {@link Tab} to a screen
 * @author zrona
 */
public class TabPainter extends ZabPanel{
	private static final long serialVersionUID = 1L;
	
	/** The x coordinate at which the {@link Tab} of a {@link TabPainter} is rendered */
	public static final double BASE_X = 100;
	/** The y coordinate at which the {@link Tab} of a {@link TabPainter} is rendered */
	public static final double BASE_Y = 200;
	/** The base size of a rendered measure in number of pixels */
	public static final double MEASURE_WIDTH = 200;
	/** The amount of space between strings drawn for a tab */
	public static final double STRING_SPACE = 40;
	/** The color of strings drawn for a tab */
	public static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
	/** The color of strings drawn for a tab */
	public static final Color STRING_COLOR = new Color(60, 60, 60);
	/** The color of symbols drawn for a tab */
	public static final Color SYMBOL_COLOR = new Color(200, 200, 200);
	/** The font of symbols drawn for a tab */
	public static final Font SYMBOL_FONT = new Font("Arial", Font.PLAIN, 20);
	/** The font stroke used for strings drawn for a tab */
	public static final Stroke STRING_LINE_WEIGHT = new BasicStroke(3);
	
	/** The camera used to control drawing the graphics with the {@link #tabScreen} */
	private Camera tabCamera;
	
	/** The width, in pixels, of the paintable area */
	private int paintWidth;
	/** The height, in pixels, of the paintable area */
	private int paintHeight;
	
	/** The {@link Tab} used for painting */
	private Tab tab;
	
	/**
	 * Create a new {@link TabPainter} at the given size
	 * @param width See {@link #width}
	 * @param height See {@link #height}
	 */
	public TabPainter(int width, int height, Tab tab){
		super();
		this.setTab(tab);
		this.tabCamera = new Camera(width, height);
		this.resetCamera();
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
	
	/**
	 * Bring the camera to a default state of its origin
	 */
	public void resetCamera(){
		this.tabCamera.setX(-50);
		this.tabCamera.setY(-50);
		this.tabCamera.setXZoomFactor(0);
		this.tabCamera.setYZoomFactor(0);
		this.tabCamera.setDrawOnlyInBounds(false);
	}
	
	/**
	 * @return See {@link #tab}
	 */
	public Tab getTab(){
		return tab;
	}
	/**
	 * Set the {@link Tab} used by this {@link TabPainter}
	 * @param tab See {@link #tab}
	 */
	public void setTab(Tab tab){
		this.tab = tab;
	}
	
	/**
	 * Convert an x coordinate on the painter, to a rhythmic position in a tab
	 * @param x The x coordinate on the painter
	 * @return The position in number measures
	 */
	public double xToTabPos(double x){
		return this.camXToTabPos(this.getCamera().toCamX(x));
	}
	
	/**
	 * Convert a rhythmic position in a tab, to an x coordinate on the painter
	 * @param pos The position on the tab in measures
	 * @return The x position as a painter coordinate
	 */
	public double tabPosToX(double pos){
		return this.tabPosToCamX(this.getCamera().toPixelX(pos));
	}
	
	/**
	 * Convert an x coordinate on the camera, to a rhythmic position in a tab
	 * @param x The x coordinate on the camera
	 * @return The position in number measures
	 */
	public double camXToTabPos(double x){
		return (x - BASE_X) / MEASURE_WIDTH;
	}
	
	/**
	 * Convert a rhythmic position in a tab, to an x coordinate on the camera
	 * @param pos The position on the tab in measures
	 * @return The x coordinate on the camera
	 */
	public double tabPosToCamX(double pos){
		return pos * MEASURE_WIDTH + BASE_X;
	}
	
	/**
	 * Convert a y coordinate on the painter, to the string number relative to the coordinate. 
	 * Can be a decimal number describing which string it is closest to
	 * @param y The y coordinate on the painter
	 * @return The position string number
	 */
	public double yToTabPos(double y){
		return this.camYToTabPos(this.getCamera().toCamY(y));
	}
	
	/**
	 * Convert a string number in a tab, to a y coordinate on the painter
	 * @param pos The string number
	 * @return The y position as a painter coordinate
	 */
	public double tabPosToY(double pos){
		return this.tabPosToCamY(this.getCamera().toPixelY(pos));
	}
	
	/**
	 * Convert a y coordinate on the camera, to a string number
	 * @param y The y coordinate on the camera
	 * @return The string number
	 */
	public double camYToTabPos(double y){
		return (y - BASE_Y) / STRING_SPACE;
	}
	
	/**
	 * Convert the string number, to a y coordinate on the camera
	 * @param pos The string number
	 * @return The y coordinate on the camera
	 */
	public double tabPosToCamY(double pos){
		return pos * STRING_SPACE + BASE_Y;
	}

	/**
	 * Draw the {@link Tab} to the screen<br>
	 * This method will reassign the graphics object in {@link #tabCamera}
	 */
	@Override
	public void paint(Graphics gr){
		// Set up camera
		Graphics2D g = (Graphics2D)gr;
		Camera cam = this.tabCamera;
		cam.setG(g);
		
		// Draw background
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, this.getPaintWidth(), this.getPaintHeight());
		
		// Draw the tab
		this.drawTab(g);
	}
	
	/**
	 * Draw the {@link #tabCamera} of this {@link TabPainter}<br>
	 * This method will reassign the graphics object in {@link #tabCamera}
	 * @param g The graphics object to use
	 */
	public void drawTab(Graphics2D g){
		// Set up camera
		Camera cam = this.tabCamera;
		cam.setG(g);

		// Set up for drawing strings
		g.setStroke(STRING_LINE_WEIGHT);
		g.setFont(SYMBOL_FONT);
		
		// Draw strings, string note labels, and note symbols
		ArrayList<TabString> strings = this.tab.getStrings();
		// Length, in pixels, to render each string
		double stringLength = 1500;
		// Number of measure lines to draw
		double measureLines = 8;
		// Starting y position for the first string on the tab
		double y = this.tabPosToCamY(0);
		// The x position to draw the string labels
		double labelX = this.tabPosToCamX(-0.1);
		
		// Draw a vertical line at the beginning of each measure
		g.setColor(STRING_COLOR);
		double measureLineEnd = y + (strings.size() - 1) * STRING_SPACE;
		for(int i = 0; i < measureLines; i++){
			double x = this.tabPosToCamX(i);
			cam.drawLine(x, y, x, measureLineEnd);
		}

		String str;
		for(TabString s : strings){
			// Draw the string
			g.setColor(STRING_COLOR);
			cam.drawLine(labelX, y, stringLength, y);
			
			// Draw string note labels
			g.setColor(SYMBOL_COLOR);
			str = s.getRootPitch().getPitchName(false);
			cam.drawScaleString(str, labelX - g.getFontMetrics().stringWidth(str), y + 8);
			
			// Draw symbols
			for(TabSymbol t : s){
				str = t.getSymbol(s);
				cam.drawScaleString(str, this.tabPosToCamX(
						// Draw the symbol centered at the position
						t.getPos()) - g.getFontMetrics().stringWidth(str) * 0.5,
						y + g.getFont().getSize() * 0.5);
			}
			
			// Increment to y coordinate of next string
			y += STRING_SPACE;
		}
	}
}