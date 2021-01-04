package appMain.gui.comp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import appMain.gui.util.Camera;
import tab.Tab;
import tab.TabString;
import tab.TabString.SymbolHolder;
import tab.symbol.TabNote;
import tab.symbol.TabSymbol;

/**
 * A class used to handle drawing a {@link Tab} with a graphics object. Additionally, handles storing objects for input and output.
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
	/** The color of a highlight showing a symbol is selected */
	public static final Color HIGHLIGHT_COLOR = new Color(170, 170, 255, 100);
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
	
	/** A list of every user selected {@link TabSymbol} */
	private ArrayList<SymbolHolder> selected;
	
	/** The tab number which will be applied to the selected symbols, null if not set */
	private Integer selectedNewTabNum;
	
	/** The {@link MouseAdapter} containing the functionality for mouse input in this {@link TabPainter} for the editor */
	private EditorMouse mouseControl;

	/** The {@link KeyAdapter} containing the functionality for key input in this {@link TabPainter} for the editor */
	private EditorKeyboard keyControl;
	
	/**
	 * Create a new {@link TabPainter} at the given size
	 * @param width See {@link #width}
	 * @param height See {@link #height}
	 */
	public TabPainter(int width, int height, Tab tab){
		super();
		// Set up tab and camera
		this.setTab(tab);
		this.tabCamera = new Camera(width, height);
		this.tabCamera.setDrawOnlyInBounds(false);
		this.resetCamera();
		this.setPaintSize(width, height);
		
		// Set up objects for controlling the painter
		this.selected = new ArrayList<SymbolHolder>();
		this.selectedNewTabNum = null;
		
		// Add the mouse input to the panel
		this.mouseControl = new EditorMouse();
		this.addMouseListener(mouseControl);
		this.addMouseMotionListener(mouseControl);
		this.addMouseWheelListener(mouseControl);
		
		// Create the key listener
		this.keyControl = new EditorKeyboard();
		this.addKeyListener(keyControl);
		
		// Ensure this JPanel has focus
		this.requestFocusInWindow();
		
		// Final repaint to ensure the panel is updated
		this.repaint();
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
	 * @return See {@link #selected}
	 */
	public ArrayList<SymbolHolder> getSelected(){
		return this.selected;
	}
	
	/**
	 * Unselect all but the specified {@link SymbolHolder}
	 * @param symbol
	 */
	public void selectOne(SymbolHolder h){
		this.clearSelection();
		this.getSelected().add(h);
		this.selectedNewTabNum = null;
	}
	
	/**
	 * Unselect all selected {@link TabSymbol} objects
	 */
	public void clearSelection(){
		this.getSelected().clear();
		this.selectedNewTabNum = null;
	}
	
	/**
	 * @return See {@link #selectedNewTabNum}
	 */
	public Integer getSelectedNewTabNum(){
		return this.selectedNewTabNum;
	}
	
	/**
	 * Add the given numerical character to the selected tab number.<br>
	 * Only contributes to the number when a selection is made
	 * @param num The number, does nothing if the character is not a minus sign or a number
	 * @return The new value of selectedNewTabNum
	 */
	public Integer appendSelectedTabNum(char num){
		if(this.getSelected().isEmpty()) return this.selectedNewTabNum;
		
		Integer n = this.selectedNewTabNum;
		boolean isMinus = num == '-';
		boolean isNum = num >= '0' && num <= '9';
		int newNum = num - '0';
		if(n == null){
			if(isNum) n = newNum;
		}
		else{
			if(isMinus) n *= -1;
			else if(isNum){
				n *= 10;
				if(n < 0) n -= newNum;
				else n += newNum;
			}
		}
		this.selectedNewTabNum = n;
		
		updateSelectedNewTabNum();
		
		return this.selectedNewTabNum;
	}
	
	/**
	 * Set every {@link #selected} TabSymbol to the current selectedTabNum. 
	 * Automatically resets the number to null if it goes beyond the range
	 */
	public void updateSelectedNewTabNum(){
		Integer n = this.getSelectedNewTabNum();
		if(n != null){
			for(SymbolHolder h : selected){
				TabSymbol t = h.getSymbol();
				// Ensure the note stays within only 2 digits
				if(n < -99 || n > 99){
					n %= 10;
					this.selectedNewTabNum = n;
				}
				// TODO rework this so that it works for all strings by storing the associated string with each pitch
				//	rather than only working for the first string
				// Set the note
				TabString s = this.getTab().getStrings().get(0);
				h.setSymbol(new TabNote(s.createPitch(n), t.getPosition().copy(), t.getModifier().copy()));
			}
		}
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
		this.tabCamera.setY(-100);
		this.tabCamera.setXZoomFactor(0);
		this.tabCamera.setYZoomFactor(0);
		this.tabCamera.setDrawOnlyInBounds(true);
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
	 * @return See {@link #mouseControl}
	 */
	public EditorMouse getMouseInput(){
		return this.mouseControl;
	}

	/**
	 * @return See {@link #keyControl}
	 */
	public EditorKeyboard getKeyInput(){
		return this.keyControl;
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
		return this.getCamera().toPixelX(this.tabPosToCamX(pos));
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
	 * @return The position string number, decimal numbers represent the closeness to that string
	 */
	public double yToTabPos(double y){
		return this.camYToTabPos(this.getCamera().toCamY(y));
	}
	
	/**
	 * Convert a string number in a tab, to a y coordinate on the painter
	 * @param num The string number, decimal numbers represent the closeness to that string
	 * @return The y position as a painter coordinate
	 */
	public double tabPosToY(double num){
		return this.getCamera().toPixelY(this.tabPosToCamY(num));
	}
	
	/**
	 * Convert a y coordinate on the camera, to a string number
	 * @param y The y coordinate on the camera
	 * @return The string number, decimal numbers represent the closeness to that string
	 */
	public double camYToTabPos(double y){
		return (y - BASE_Y) / STRING_SPACE;
	}
	
	/**
	 * Convert the string number, to a y coordinate on the camera
	 * @param num The string number, decimal numbers represent the closeness to that string
	 * @return The y coordinate on the camera
	 */
	public double tabPosToCamY(double num){
		return num * STRING_SPACE + BASE_Y;
	}
	
	/**
	 * Get the string number which corresponds to the given y position
	 * @param pos The y coordinate in pixel space
	 * @return The string number, or -1 if the position is not on a string
	 */
	public int pixelYToStringNum(double pos){
		int s = (int)Math.round(yToTabPos(pos));
		if(s < 0 || s >= this.getTab().getStrings().size()) return -1;
		return s;
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
	 * This method will reassign the graphics object in {@link #tabCamera}.<br>
	 * This method will do nothing, and will not assign a graphics object, if {@link #tab} is null
	 * @param g The graphics object to use
	 * @return true if the drawing took place, false otherwise
	 */
	public boolean drawTab(Graphics2D g){
		// Do nothing if the tab is null
		if(this.getTab() == null) return false;
		
		// Set up camera
		Camera cam = this.tabCamera;
		cam.setG(g);

		// Set up for drawing strings
		g.setStroke(STRING_LINE_WEIGHT);
		g.setFont(SYMBOL_FONT);
		
		// Draw strings, string note labels, and note symbols
		ArrayList<TabString> strings = this.getTab().getStrings();
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
			for(SymbolHolder h : s){
				// Get the symbol as a string
				TabSymbol t = h.getSymbol();
				str = t.getSymbol(s);
				
				// Finding the size of the space the symbol will take up
				double sW = g.getFontMetrics().stringWidth(str);
				double sH = g.getFont().getSize();
				// Draw the symbol centered at the x and y position
				double sX = this.tabPosToCamX(t.getPos()) - sW * 0.5;
				double sY = y + sH * 0.5;
				
				// If the symbol is selected, draw a highlight under it
				if(this.selected.contains(h)){
					g.setColor(HIGHLIGHT_COLOR);
					cam.fillRect(sX, sY - sH, sW, sH);
					g.setColor(SYMBOL_COLOR);
				}
				// Draw the symbol
				cam.drawScaleString(str, sX, sY);
				
			}
			
			// Increment to y coordinate of next string
			y += STRING_SPACE;
		}
		
		return true;
	}
	
	/**
	 * Select only one note near the given position, this also unselects all other notes.<br>
	 * Does nothing if no valid note can be found
	 * @param mX The x coordinate, usually a mouse position
	 * @param mY The y coordinate, usually a mouse position
	 */
	public void selectNote(double mX, double mY){
		double x = tab.getTimeSignature().quantize(xToTabPos(mX), 8); // TODO make this a setting
		int y = pixelYToStringNum(mY);
		if(x < 0 || y < 0) return;
		
		// TODO make a more efficient way of searching for a note, considering they are sorted
		//	i.e. binary search
		TabString str = tab.getStrings().get(y);
		for(SymbolHolder h : str){
			TabSymbol t = h.getSymbol();
			if(t.getPos() == x){
				selectOne(h);
				break;
			}
		}
	}
	
	/**
	 * Place a note based on the given coordinates in pixel space.<br>
	 * Can do nothing if the coordinates aren't near the tab, or if the note cannot be placed
	 * @param mX The x coordinate, usually a mouse position
	 * @param mY The y coordinate, usually a mouse position
	 * @param fret The fret number to place
	 */
	public void placeNote(double mX, double mY, int fret){
		double x = xToTabPos(mX);
		int y = pixelYToStringNum(mY);
		if(x < 0 || y < 0) return;
		this.clearSelection();
		this.selected.add(tab.placeQuantizedNote(y, fret, x));
	}
	
	/**
	 * A class used by {@link TabPainter} to handle mouse input on the graphics based editor
	 * @author zrona
	 */
	public class EditorMouse extends MouseAdapter{
		
		/**
		 * When the left mouse button is pressed, place a note on the tab, if the mouse is close enough.<br>
		 * When the middle mouse button is pressed, it sets an anchor point.<br>
		 * When the right mouse button is pressed, attempt to select a note, if possible.<br>
		 * Regardless of what button is pressed, give focus to the window when the mouse clicks
		 */
		@Override
		public void mousePressed(MouseEvent e){
			Camera cam = getCamera();
			double x = e.getX();
			double y = e.getY();
			switch(e.getButton()){
				case MouseEvent.BUTTON1: placeNote(x, y, 0); break;
				case MouseEvent.BUTTON2: cam.setAnchor(x, y); break;
				case MouseEvent.BUTTON3: selectNote(x, y); break;
			}

			requestFocusInWindow();
			
			repaint();
		}
		/**
		 * When the middle mouse button is released, it releases the anchor point
		 */
		@Override
		public void mouseReleased(MouseEvent e){
			Camera cam = getCamera();
			int b = e.getButton();
			if(b == MouseEvent.BUTTON2) cam.releaseAnchor();
			repaint();
		}
		
		/**
		 * When the middle mouse button is clicked while holding shift, reset the camera
		 */
		@Override
		public void mouseClicked(MouseEvent e){
			int b = e.getButton();
			if(e.isShiftDown() && b == MouseEvent.BUTTON2) resetCamera();
		}
		
		/**
		 * When the middle mouse button is dragged, it pans the camera
		 */
		@Override
		public void mouseDragged(MouseEvent e){
			Camera cam = getCamera();
			cam.pan(e.getX(), e.getY());
			repaint();
		}
		
		/**
		 * When the mouse wheel is moved, zoom in or out based on the mouse position and wheel movement
		 */
		@Override
		public void mouseWheelMoved(MouseWheelEvent e){
			Camera cam = getCamera();
			double factor = -0.1;
			if(e.isShiftDown()) factor *= 2;
			if(e.isAltDown()) factor *= 2;
			if(e.isControlDown()) factor *= 2;
			cam.zoomIn(e.getX(), e.getY(), e.getWheelRotation() * factor);
			repaint();
		}
	}
	
	/**
	 * A class used by {@link TabPainter} to handle key input on the graphics based editor
	 * @author zrona
	 */
	public class EditorKeyboard extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
			appendSelectedTabNum(e.getKeyChar());
		}
	}
	
}