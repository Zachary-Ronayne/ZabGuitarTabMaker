package appMain.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import appMain.gui.comp.GuiFrame;
import appMain.gui.comp.ZabFrame;
import appMain.gui.comp.ZabPanel;
import appMain.gui.dropMenu.ZabMenuBar;
import appMain.gui.editor.frame.EditorFrame;
import appMain.gui.export.ZabExporterDialog;
import appUtils.ZabConstants;
import gui.ConfirmNotSavedPopup;
import lang.AbstractLanguage;
import lang.Language;
import util.GuiUtils;

/**
 * An object representing the main GUI used by the Zab application
 * @author zrona
 */
public class ZabGui extends JFrame{
	private static final long serialVersionUID = 1L;
	
	/** The {@link GuiFrame} currently displayed by this {@link ZabGui} */
	private ZabFrame currentFrame;
	
	/** The ZabPanel holding everything in this {@link ZabGui} */
	private ZabPanel primaryPanel;
	
	/** The {@link ZabMenuBar} used by the Zab Application */
	private ZabMenuBar menuBar;
	
	/** The listener used for resizing the window by this {@link ZabGui} */
	private ZabWindowListener resizer;
	
	/** The {@link EditorFrame} used by this {@link ZabGui} */
	private EditorFrame editorFrame;
	
	/**
	 * Create the default {@link ZabGui}
	 */
	public ZabGui(){
		super();
		this.updateTitle(null);
		
		// Keep hidden initially
		this.setVisible(false);
		
		// Set the Theme appropriately
		ZabTheme.setToTheme(this);
		
		// Set the minimum size to the constant minimum size
		this.setMinimumSize(new Dimension(ZabConstants.MIN_APP_WIDTH, ZabConstants.MIN_APP_HEIGHT));
		
		// Set the window to do nothing when it closes, the window listener will handle closing
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		// Set up the main panel holding everything
		this.primaryPanel = new ZabPanel();
		this.add(primaryPanel);
		
		// Set the default frame state
		this.editorFrame = new EditorFrame(this);
		this.openEditor();

		// Add the menu bar
		this.menuBar = new ZabMenuBar(this);
		this.setJMenuBar(this.menuBar);
		
		// Pack the frame
		this.pack();

		// Adding listener for changing the window size
		resizer = new ZabWindowListener(this);
		this.addComponentListener(resizer);
		this.addWindowStateListener(resizer);
		this.addWindowListener(resizer);
		this.addWindowFocusListener(resizer);
		
		// Show the GUI
		this.setVisible(ZabConstants.SHOW_GUI_ON_INIT);

		// Put the GUI in the full window and update the painter camera when the GUI is initially made
		this.requestFocus();
		this.maximize();
		this.getEditorFrame().getTabScreen().resetCamera();
	}
	
	/***
	 * Update every component in this ZabGui, including all associated objects associated with this GUI, to the current theme of the Zab application
	 */
	public void updateTheme(){
		ArrayList<Component> list = new ArrayList<Component>();
		list.add(this);
		list.addAll(GuiUtils.getAllComponents(this.getContentPane()));
		list.addAll(GuiUtils.getAllComponents(this.getZabMenuBar()));
		ZabExporterDialog export = this.getZabMenuBar().getFileMenu().getExportDialog();
		list.addAll(GuiUtils.getAllComponents(export));
		
		for(Component c : list){
			ZabTheme.setToTheme(c);
		}
		
		this.repaint();
	}
	
	/**
	 * Update the name of this {@link ZabGui}, extending it with the given string
	 * @param ext The string to append to the end of the standard portion of the name. Can be null or empty to not append anything
	 */
	public void updateTitle(String ext){
		boolean append = !(ext == null || ext.isBlank() || ext.isEmpty());
		AbstractLanguage lang = Language.get();
		
		String base = lang.appName();
		if(append) base = base.concat(" | ").concat(ext);
		this.setTitle(base);
	}
	
	/**
	 * Copy the data of this {@link ZabGui} into the given gui. 
	 * This will copy references of the data, i.e. the opened tab will be copied as a reference, not a new object
	 * @param gui The gui to copy into
	 */
	public void copyData(ZabGui gui){
		boolean isSaved = this.getEditorFrame().getTabScreen().getUndoStack().isSaved();
		gui.getEditorFrame().setOpenedTab(this.getEditorFrame().getOpenedTab());
		
		if(!isSaved) gui.getEditorFrame().getTabScreen().getUndoStack().markNotSaved();
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
	
	/** @return See {@link #menuBar} */
	public ZabMenuBar getZabMenuBar(){
		return this.menuBar;
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
	public ZabWindowListener getResizer(){
		return this.resizer;
	}
	
	/**
	 * A class used for handling resizing the {@link ZabGui}, and all other window related functions of a {@link ZabGui}
	 * @author zrona
	 */
	public class ZabWindowListener extends ComponentAdapter implements WindowStateListener, WindowListener, WindowFocusListener{
		/** The {@link ZabGui} which this listener uses */
		private ZabGui gui;
		
		/**
		 * Create the listener
		 * @param gui The {@link ZabGui} which uses this listener
		 */
		public ZabWindowListener(ZabGui gui){
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
		public void windowClosing(WindowEvent e){
			// If the tab is not saved, confirm that the user wants to close the window
			// If the user says not to close, then cancel the close
			if(!getEditorFrame().getTabScreen().getUndoStack().isSaved()){
				if(!ConfirmNotSavedPopup.show()){
					return;
				}
			}
			// Otherwise, terminate the program, only if this is a normal build
			if(ZabConstants.BUILD_NORMAL) System.exit(0);
		}
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

		@Override
		public void windowGainedFocus(WindowEvent e){
			// Provides focus to the needed frame
			getCurrentFrame().focused();
		}

		@Override
		public void windowLostFocus(WindowEvent e){}
	}
	
}
