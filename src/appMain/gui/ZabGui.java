package appMain.gui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

import appMain.gui.comp.ZabPanel;
import appMain.gui.comp.dropMenu.ZabMenuBar;
import appMain.gui.frames.EditorFrame;
import appMain.gui.frames.GuiFrame;
import appMain.gui.frames.ZabFrame;

/**
 * An object representing the main GUI used by the Zab application
 * @author zrona
 */
public class ZabGui extends JFrame{
	private static final long serialVersionUID = 1L;
	
	/**
	 * True if ZabGui objects should show on creation, false otherwise,
	 * should be false for testing purposes, true for a proper build
	 */
	public static final boolean SHOW_GUI_ON_INIT = true;
	
	/** The {@link GuiFrame} currently displayed by this {@link ZabGui} */
	private ZabFrame currentFrame;
	
	/** The ZabPanel holding everything in this {@link ZabGui} */
	private ZabPanel primaryPanel;
	
	/** The {@link ZabMenuBar} used by the Zab Application */
	private ZabMenuBar menuBar;
	
	/** The listener used for resizing the window by this {@link ZabGui} */
	private GuiResizeListener resizer;
	
	/** The {@link EditorFrame} used by this {@link ZabGui} */
	private EditorFrame editorFrame;
	
	/**
	 * Create the default {@link ZabGui}
	 */
	public ZabGui(){
		super();
		
		// Keep hidden initially
		this.setVisible(false);
		
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
		this.menuBar = new ZabMenuBar(this);
		this.setJMenuBar(this.menuBar);
		
		// Set the default frame state
		this.editorFrame = new EditorFrame(this);
		this.openEditor();
		
		// Pack the frame
		this.pack();

		// Adding listener for changing the window size
		resizer = new GuiResizeListener(this);
		this.addComponentListener(resizer);
		this.addWindowStateListener(resizer);
		this.addWindowListener(resizer);
		
		// Show the GUI
		this.setVisible(SHOW_GUI_ON_INIT);

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
	public ZabFrame getCurrentFrame(){
		return this.currentFrame;
	}

	/**
	 * Set the currently used {@link GuiFrame}. This will remove the old frame from the GUI, and will add the given frame to the GUI. 
	 * Does nothing if currentFrame is null
	 * @param currentFrame See {@link #currentFrame}
	 * @return true if the frame was set, false otherwise
	 */
	public boolean setCurrentFrame(ZabFrame currentFrame){
		if(currentFrame == null) return false;
		
		GuiFrame f = this.getCurrentFrame();
		if(f != null) this.primaryPanel.remove(f);
		this.primaryPanel.add(currentFrame);
		this.currentFrame = currentFrame;
		this.updateSize();
		
		return true;
	}
	
	/**
	 * @return See {@link #editorFrame}
	 */
	public EditorFrame getEditorFrame(){
		return this.editorFrame;
	}
	
	/**
	 * Open the {@link EditorFrame} into the gui
	 * @return true if the editor was opened, false otherwise
	 */
	public boolean openEditor(){
		return this.setCurrentFrame(this.getEditorFrame());
	}
	
	/**
	 * Get the currently used frame holding all of the objects in the GUI
	 * @return See {@link #primaryPanel}
	 */
	public ZabPanel getPrimaryPanel(){
		return this.primaryPanel;
	}
	
	/**
	 * Get the listener responsible for handling resizing of this {@link ZabGui}
	 * @return See {@link #resizer}
	 */
	public GuiResizeListener getResizer(){
		return this.resizer;
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
		 * @return See {@link #gui}
		 */
		public ZabGui getGui(){
			return this.gui;
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
