package appMain.gui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

import appMain.gui.comp.ZabMenuBar;
import appMain.gui.comp.ZabPanel;
import appMain.gui.frames.EditorFrame;
import appMain.gui.frames.GuiFrame;

/**
 * An object representing the main GUI used by the Zab application
 * @author zrona
 */
public class ZabGui extends JFrame{
	private static final long serialVersionUID = 1L;
	
	/** The {@link GuiFrame} currently displayed by this {@link ZabGui} */
	private GuiFrame currentFrame;
	
	/** The ZabPanel holding everything in this {@link ZabGui} */
	private ZabPanel primaryPanel;
	
	/**
	 * The {@link ZabMenuBar} used by the Zab Application
	 */
	private ZabMenuBar menuBar;
	
	/**
	 * Create the default {@link ZabGui}
	 */
	public ZabGui(){
		super();
		
		// Set the Theme appropriately
		ZabTheme.setToTheme(this);
		
		// Set the minimum size to not be too small
		this.setMinimumSize(new Dimension(300, 300));
		
		// Set the default size of the window
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Set up the main panel holding everything
		this.primaryPanel = new ZabPanel();
		this.add(primaryPanel);

		// Add the menu bar
		this.menuBar = new ZabMenuBar();
		this.setJMenuBar(this.menuBar);
		
		// Set the default frame state
		this.setCurrentFrame(new EditorFrame());
		
		// Pack the frame
		this.pack();

		// Adding listener for changing the window size
		GuiResizeListener lis = new GuiResizeListener(this);
		this.addComponentListener(lis);
		this.addWindowStateListener(lis);
		this.addWindowListener(lis);
		
		// Show the GUI
		this.setVisible(true);

		// Put the GUI in the full window
		this.requestFocus();
		this.maximize();
	}
	
	/**
	 * Bring this {@link ZabGui} to take up the entire screen, not going to full screen, but to the maximum size.
	 */
	public void maximize(){
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		this.updateSize();
	}
	
	/**
	 * Update the size of internal objects based on the current size of this {@link ZabGui}
	 */
	public void updateSize(){
		Dimension d = this.getSize();
		this.currentFrame.parentResized((int)d.getWidth(), (int)d.getHeight());
		this.repaint();
	}
	
	/**
	 * Get the currently used frame
	 * @return See {@link #currentFrame}
	 */
	public GuiFrame getCurrentFrame(){
		return this.currentFrame;
	}

	/**
	 * Set the currently used {@link GuiFrame}. This will remove the old frame from the GUI, and will add the given frame to the GUI
	 * @param currentFrame See {@link #currentFrame}
	 */
	public void setCurrentFrame(GuiFrame currentFrame){
		GuiFrame f = this.getCurrentFrame();
		if(f != null) this.primaryPanel.remove(f);
		if(currentFrame != null) this.primaryPanel.add(currentFrame);
		this.currentFrame = currentFrame;
		this.updateSize();
	}
	
	/**
	 * A class used for handling resizing the {@link ZabGui}
	 * @author zrona
	 */
	public class GuiResizeListener extends ComponentAdapter implements WindowStateListener, WindowListener{
		/** The {@link ZabGui} which this listener uses */
		private ZabGui gui;
		
		/**
		 * Create the listener
		 * @param gui The {@link ZabGui} which uses this listener
		 */
		public GuiResizeListener(ZabGui gui){
			this.gui = gui;
		}
		
		/**
		 * Called during any point when the GUI is resized
		 */
		public void resize(){
			this.gui.updateSize();
		}
		
		@Override
		public void componentResized(ComponentEvent e){
			resize();
		}
		
		@Override
		public void windowStateChanged(WindowEvent e){
			resize();
		}

		@Override
		public void windowOpened(WindowEvent e){
			resize();
		}
		@Override
		public void windowClosing(WindowEvent e){}
		@Override
		public void windowClosed(WindowEvent e){}
		@Override
		public void windowIconified(WindowEvent e){
			resize();
		}
		@Override
		public void windowDeiconified(WindowEvent e){
			resize();
		}
		@Override
		public void windowActivated(WindowEvent e){
			resize();
		}
		@Override
		public void windowDeactivated(WindowEvent e){
			resize();
		}
	}
	
}
