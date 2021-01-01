package appMain.gui.frames;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JLabel;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appMain.gui.comp.TabPainter;
import appMain.gui.comp.ZabPanel;
import appMain.gui.layout.ZabLayoutHandler;
import appMain.gui.util.Camera;
import tab.InstrumentFactory;
import tab.Tab;

/**
 * A class holding the information for the primary editor for the Zab application
 */
public class EditorFrame extends ZabFrame{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabGui} which is using this {@link EditorFrame} */
	private ZabGui gui;
	
	/** The {@link ZabPanel} where all of the graphics for the {@link Tab} are drawn */
	private TabPainter tabScreen;
	
	/** The {@link ZabPanel} holding all of the extra components acting as the menu of this {@link GuiFrame} */
	private ZabPanel menuHolder;
	
	/** The {@link ZabPanel} holding the {@link TabPainter} used by this {@link EditorFrame} */
	private ZabPanel paintHolder;
	
	/** The {@link Tab} which is currently being edited by this {@link EditorFrame} */
	private Tab openedTab;
	
	/** The {@link MouseAdapter} containing the functionality for mouse input in this {@link EditorFrame} for the editor */
	private EditorMouse mouseControl;
	
	/**
	 * Create an {@link EditorFrame} at a default state
	 * @param gui The {@link ZabGui} which this {@link EditorFrame} will be a part of
	 */
	public EditorFrame(ZabGui gui){
		super();
		
		// Setup the gui reference
		this.gui = gui;
		
		// Set up the tab
		this.openedTab = InstrumentFactory.guitarStandard();

		// Set up the layout
		ZabLayoutHandler.createVerticalLayout(this);
		
		// Set up the menu holder
		menuHolder = new ZabPanel();
		ZabLayoutHandler.createHorizontalLayout(menuHolder);
		this.add(menuHolder);
		
		// Temporary title placeholder
		JLabel lab = new JLabel();
		ZabTheme.setToTheme(lab);
		lab.setText("Editor");
		lab.setFont(new Font("Arial", Font.PLAIN, 20));
		menuHolder.add(lab);
		
		// Set up the graphics objects
		this.tabScreen = new TabPainter(0, 0, this.getOpenedTab());
		this.paintHolder = new ZabPanel();
		this.paintHolder.add(tabScreen);
		this.add(paintHolder);
		
		// Add mouse listener
		mouseControl = new EditorMouse();
		this.addMouseListener(mouseControl);
		this.addMouseMotionListener(mouseControl);
		this.addMouseWheelListener(mouseControl);
		
		// Update the screen's display
		repaint();
	}
	
	/**
	 * Update the size of the painting area in this {@link EditorFrame} with the new width and height
	 * @param width The new width of the painting area
	 * @param height The new height of the painting area
	 */
	public void updatePaintSize(int width, int height){
		// This doesn't make sense, but adding and removing tabScreen, which is a JPanel, updates the size, so whatever, it works
		paintHolder.remove(tabScreen);
		paintHolder.add(tabScreen);
		
		// Update the actual size
		this.tabScreen.setPaintSize(width, height);
	}
	
	/**
	 * Updates the size of the painting canvas
	 */
	@Override
	public void parentResized(int w, int h){
		this.updatePaintSize(w, h - menuHolder.getHeight());
	}
	
	/**
	 * @return See {@link #gui}
	 */
	public ZabGui getGui(){
		return this.gui;
	}
	
	/**
	 * Get the {@link TabPainter} used by this {@link EditorFrame}
	 * @return See {@link #tabScreen}
	 */
	public TabPainter getTabScreen(){
		return this.tabScreen;
	}
	
	/**
	 * Get the {@link ZabPanel} used by this {@link EditorFrame} for the menu
	 * @return {@link #menuHolder}
	 */
	public ZabPanel getMenuHolder(){
		return this.menuHolder;
	}
	
	/**
	 * Get the {@link ZabPanel} used by this {@link EditorFrame} for drawing graphics
	 * @return {@link #paintHolder}
	 */
	public ZabPanel getPaintHolder(){
		return this.paintHolder;
	}
	
	/**
	 * @return See {@link #openedTab}
	 */
	public Tab getOpenedTab(){
		return this.openedTab;
	}
	
	/**
	 * Set the tab used by this EditorFrame
	 * @param tab See {@link #openedTab}
	 */
	public void setOpenedTab(Tab tab){
		this.openedTab = tab;
		this.getTabScreen().setTab(tab);
	}
	
	/**
	 * Get the {@link Camera} used by {@link #tabScreen} for rendering
	 * @return The camera
	 */
	public Camera getCamera(){
		return this.getTabScreen().getCamera();
	}
	
	/**
	 * @return See {@link #mouseControl}
	 */
	public EditorMouse getMouseInput(){
		return this.mouseControl;
	}
	
	/**
	 * A class used by {@link EditorFrame} to handle mouse input on the graphics based editor
	 * @author zrona
	 */
	public class EditorMouse extends MouseAdapter{
		
		/**
		 * Place a note based on the position of a mouse event. Can do nothing if the mouse isn't near the tab
		 * @param e The {@link MouseEvent}
		 * @param fret The fret number to place
		 */
		public void placeNote(MouseEvent e, int fret){
			TabPainter screen = getTabScreen();
			Tab tab = getOpenedTab();
			double x = screen.xToTabPos(e.getX());
			int y = (int)Math.round(screen.yToTabPos(e.getY()) - 1);
			if(x < 0 || y < 0 || y >= tab.getStrings().size()) return;
			tab.placeQuantizedNote(y, fret, x);
		}
		
		/**
		 * When the left mouse button is pressed, place a note on the tab, if the mouse is close enough
		 * When the middle mouse button is pressed, it sets an anchor point
		 */
		@Override
		public void mousePressed(MouseEvent e){
			Camera cam = getCamera();
			switch(e.getButton()){
				case MouseEvent.BUTTON1: placeNote(e, 0); break;
				case MouseEvent.BUTTON2: cam.setAnchor(e.getX(), e.getY()); break;
			}
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
			if(e.isShiftDown() && b == MouseEvent.BUTTON2) getTabScreen().resetCamera();
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
	
}
