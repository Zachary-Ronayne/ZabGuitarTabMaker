package appMain.gui.comp;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import appMain.gui.ZabTheme;
import appMain.gui.util.Camera;
import appUtils.ZabAppSettings;
import appUtils.ZabSettings;
import tab.Tab;
import tab.TabPosition;
import tab.TabString;
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
	
	/** A list containing {@link Selection} objects for every user selected {@link TabPositiono} */
	private ArrayList<Selection> selected;
	
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
		this.selected = new ArrayList<Selection>();
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
	public ArrayList<Selection> getSelected(){
		return this.selected;
	}
	
	/**
	 * Check if the current {@link #selected} contains the given {@link TabPosition}
	 * @param h The {@link TabPosition}
	 * @return true if it is contained, false otherwise
	 */
	public boolean isSelected(TabPosition p){
		for(Selection s : this.getSelected()){
			if(s.getPos().equals(p)) return true;
		}
		return false;
	}

	/**
	 * Get the {@link TabSymbol} in {@link #selected} at the given index
	 * @param i The index
	 * @return The {@link TabSymbol}, or null if it is out of bounds
	 */
	public TabSymbol selected(int i){
		TabPosition p = this.selectedPosition(i);
		if(p == null) return null;
		return p.getSymbol();
	}
	
	/**
	 * Get the {@link TabPosition} in {@link #selected} at the given index
	 * @param i The index
	 * @return The {@link TabPosition}, or null if it is out of bounds
	 */
	public TabPosition selectedPosition(int i){
		if(i < 0 || i >= this.getSelected().size()) return null;
		return this.getSelected().get(i).getPos();
	}
	
	/**
	 * Select the given {@link TabPosition} on the given string. This does not unselect anything else. 
	 * The selection only occurs if the note is not already selected, and the {@link TabPosition} is on the {@link TabString}
	 * @param index index The index on the string of the {@link TabPosition} to select
	 * @param string The {@link TabString} which to select a note on
	 * @return true if the note was selected, false otherwise
	 */
	public boolean select(TabPosition pos, TabString string){
		if(this.isSelected(pos) || !string.contains(pos)) return false;
		
		this.getSelected().add(new Selection(pos, string));
		return true;
	}
	
	/**
	 * Select the {@link TabPosition} on the given string at the given index. This does not unselect anything else. 
	 * The selection only occurs if the note is not already selected
	 * @param index index The index on the string of the {@link TabPosition} to select
	 * @param string The {@link TabString} which to select a note on
	 * @return true if the note was selected, false otherwise
	 */
	public boolean select(int index, TabString string){
		TabPosition p = string.get(index);
		return this.select(p, string);
	}
	
	/**
	 * Select the given {@link TabPosition} on the string of the given index. This does not unselect anything else. 
	 * The selection only occurs if the note is not already selected
	 * @param p The {@link TabPosition} to select
	 * @param string The {@link TabString} which to select a note on
	 * @return true if the note was selected, false otherwise
	 */
	public boolean select(TabPosition p, int string){
		TabString s = this.getTab().getStrings().get(string);
		return this.select(p, s);
	}
	
	/**
	 * Select the {@link TabPosition} on the given string index at the given index. This does not unselect anything else. 
	 * The selection only occurs if the note is not already selected
	 * @param index index The index on the string of the {@link TabPosition} to select
	 * @param string The index of the string to select a note a note
	 * @return true if the note was selected, false otherwise
	 */
	public boolean select(int index, int string){
		TabString s = this.getTab().getStrings().get(string);
		TabPosition p = s.get(index);
		return this.select(p, s);
	}
	
	/**
	 * Unselect all but the specified {@link TabPosition}
	 * @param p The {@link TabPosition} containing the {@link TabSymbol} to select
	 * @param string The String which h is on
	 * @return true if the note was selected, false otherwise. Regardless of the return value, the selection is cleared
	 */
	public boolean selectOne(TabPosition p, TabString string){
		this.clearSelection();
		return this.select(p, string);
	}
	
	/**
	 * Select only one note near the given position, this also unselects all other notes.<br>
	 * Does nothing if no valid note can be found
	 * @param mX The x coordinate, usually a mouse position
	 * @param mY The y coordinate, usually a mouse position
	 * @return true if the selection took place, false otherwise
	 */
	public boolean selectNote(double mX, double mY){
		ZabSettings settings = ZabAppSettings.get();
		double x = tab.getTimeSignature().quantize(xToTabPos(mX), settings.quantizeDivisor());
		int y = pixelYToStringNum(mY);
		if(x < 0 || y < 0) return false;
		
		//	i.e. binary search
		TabString str = tab.getStrings().get(y);
		
		// Only look for a note if the string has notes
		if(str.size() <= 0) return false;
		
		// Find the note of the note
		TabPosition p = str.findPosition(x);
		if(p != null) return selectOne(p, str);
		return false;
	}
	
	/**
	 * Select every note in the tab
	 */
	public void selectAllNotes(){
		this.clearSelection();
		for(TabString s : this.getTab().getStrings()){
			for(TabPosition p : s){
				this.select(p, s);
			}
		}
	}
	
	/**
	 * Unselect all selected {@link TabSymbol} objects
	 */
	public void clearSelection(){
		this.getSelected().clear();
		this.selectedNewTabNum = null;
	}
	
	/**
	 * Remove every selected note from the tab
	 */
	public void removeSelectedNotes(){
		for(Selection s : this.getSelected()){
			s.getString().remove(s.getPos());
		}
		clearSelection();
	}
	
	/**
	 * @return See {@link #selectedNewTabNum}
	 */
	public Integer getSelectedNewTabNum(){
		return this.selectedNewTabNum;
	}
	
	/**
	 * Add the given numerical character to the selected tab number.<br>
	 * Only contributes to the number when a selection is made.<br>
	 * Automatically resets the number to null if it goes beyond the range
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
		
		// Update the number display
		if(n != null){
			for(Selection sel : selected){
				// Unpack selection
				TabPosition p = sel.getPos();
				TabSymbol t = p.getSymbol();
				TabString s = sel.getString();
				
				// Ensure the note stays within only 2 digits
				if(n < -99 || n > 99){
					n %= 10;
					this.selectedNewTabNum = n;
				}
				
				// Set the note
				p.setSymbol(new TabNote(s.createPitch(n), t.getModifier().copy()));
			}
		}
		this.repaint();
		
		return this.selectedNewTabNum;
	}

	/**
	 * Place a note based on the given coordinates in pixel space.<br>
	 * Can do nothing if the coordinates aren't near the tab, or if the note cannot be placed
	 * @param mX The x coordinate, usually a mouse position
	 * @param mY The y coordinate, usually a mouse position
	 * @param fret The fret number to place
	 * @return true if the note was placed, false otherwise
	 */
	public boolean placeNote(double mX, double mY, int fret){
		double x = xToTabPos(mX);
		int y = pixelYToStringNum(mY);
		if(x < 0 || y < 0) return false;
		TabPosition p = tab.placeQuantizedNote(y, fret, x);
		// Only add and select the note if it was placed
		
		boolean placed = p != null;
		if(placed){
			this.clearSelection();
			this.selected.add(new Selection(p, tab.getStrings().get(y)));
		}
		return placed;
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
		ZabTheme theme = ZabAppSettings.theme();
		
		// Set up camera
		Graphics2D g = (Graphics2D)gr;
		Camera cam = this.tabCamera;
		cam.setG(g);
		
		// Draw background
		g.setColor(theme.background());
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

		ZabTheme theme = ZabAppSettings.theme();
		
		// Set up camera
		Camera cam = this.tabCamera;
		cam.setG(g);

		// Set up for drawing strings
		g.setStroke(STRING_LINE_WEIGHT);
		g.setFont(SYMBOL_FONT);
		// Enable antialiasing
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
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
		g.setColor(theme.tabString());
		double measureLineEnd = y + (strings.size() - 1) * STRING_SPACE;
		for(int i = 0; i < measureLines; i++){
			double x = this.tabPosToCamX(i);
			cam.drawLine(x, y, x, measureLineEnd);
		}

		String str;
		for(TabString s : strings){
			// Draw the string
			g.setColor(theme.tabString());
			cam.drawLine(labelX, y, stringLength, y);
			
			// Draw string note labels
			g.setColor(theme.tabSymbolText());
			str = s.getRootPitch().getPitchName(false);
			cam.drawScaleString(str, labelX - g.getFontMetrics().stringWidth(str), y + 8);
			
			// Draw symbols
			for(TabPosition p : s){
				// Get the symbol as a string
				TabSymbol t = p.getSymbol();
				str = t.getSymbol(s);
				
				// Finding the size of the space the symbol will take up
				double sW = g.getFontMetrics().stringWidth(str);
				double sH = g.getFont().getSize();
				// Draw the symbol centered at the x and y position
				double sX = this.tabPosToCamX(p.getPos()) - sW * 0.5;
				double sY = y + sH * 0.5;
				
				// If the symbol is selected, draw a highlight under it
				if(this.isSelected(p)){
					g.setColor(theme.tabSymbolHighlight());
					cam.fillRect(sX, sY - sH, sW, sH);
					g.setColor(theme.tabSymbolText());
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
	 * A class used by {@link TabPainter} to handle mouse input on the graphics based editor
	 * @author zrona
	 */
	public class EditorMouse extends MouseAdapter{
		
		/**
		 * When the left mouse button is pressed, attempt to select a note, if possible.<br>
		 * When the middle mouse button is pressed, it sets an anchor point.<br>
		 * When the right mouse button is pressed, place a note on the tab, if the mouse is close enough.<br>
		 * Regardless of what button is pressed, give focus to the window when the mouse clicks
		 */
		@Override
		public void mousePressed(MouseEvent e){
			Camera cam = getCamera();
			double x = e.getX();
			double y = e.getY();
			switch(e.getButton()){
				case MouseEvent.BUTTON1: selectNote(x, y); break;
				case MouseEvent.BUTTON2: cam.setAnchor(x, y); break;
				case MouseEvent.BUTTON3: placeNote(x, y, 0); break;
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
			ZabSettings settings = ZabAppSettings.get();
			Camera cam = getCamera();
			
			double factor = settings.zoomFactor();
			if(settings.zoomInverted()) factor *= -1;
			
			double zoomMult = settings.zoomModifierFactor();
			if(e.isShiftDown()) factor *= zoomMult;
			if(e.isAltDown()) factor *= zoomMult;
			if(e.isControlDown()) factor *= zoomMult;
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
			switch(e.getKeyCode()){
				case KeyEvent.VK_R: if(e.isControlDown()) getTab().clearNotes(); break;
				case KeyEvent.VK_D: if(e.isControlDown()) removeSelectedNotes(); break;
				case KeyEvent.VK_A: if(e.isControlDown()) selectAllNotes(); break;
				default: appendSelectedTabNum(e.getKeyChar()); break;
			}
			repaint();
		}
	}
	
	/**
	 * A helper object used to track an individually selected symbol with both its {@link TabPosition} and held string
	 * @author zrona
	 */
	public static class Selection{
		/** The {@link TabPosition} of this selection */
		private TabPosition pos;
		/** The {@link TabString} which {@link #hold} is on */
		private TabString string;
		
		/**
		 * Create a new selection
		 * @param hold
		 * @param string
		 */
		public Selection(TabPosition pos, TabString string){
			super();
			this.pos = pos;
			this.string = string;
		}
		
		/** @return See {@link #pos} */
		public TabPosition getPos(){
			return pos;
		}
		/** @param pos See {@link #pos} */
		public void setPos(TabPosition pos){
			this.pos = pos;
		}
		/** @return See {@link #string} */
		public TabString getString(){
			return string;
		}
		/** @param string See {@link #string} */
		public void setString(TabString string){
			this.string = string;
		}
	}
	
}
