package appMain.gui.dropMenu;

import javax.swing.JMenuBar;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;

/**
 * The bar of drop down menus used by the Zab application
 * @author zrona
 */
public class ZabMenuBar extends JMenuBar{
	private static final long serialVersionUID = 1L;

	/** The {@link ZabGui} which uses this {@link ZabMenuBar} */
	private ZabGui gui;
	
	/** The {@link FileMenu} used for the file options */
	private FileMenu fileMenu;
	
	/** The {@link ZabMenu} used for the edit options */
	private EditMenu editMenu;
	
	/** The {@link ZabMenu} used for the graphics options */
	private GraphicsMenu graphicsMenu;
	
	/**
	 * Create the drop down menu as a default state
	 */
	public ZabMenuBar(ZabGui gui){
		super();
		// Set the GUI
		this.gui = gui;
		
		// Set the Theme appropriately
		ZabTheme.setToTheme(this);
		
		// Set up and add menu bar
		
		// File menu
		this.fileMenu = new FileMenu(this.getGui());
		this.add(this.fileMenu);
		
		// Edit menu
		this.editMenu = new EditMenu(this.getGui());
		this.add(this.editMenu);
		
		// Graphics menu
		this.graphicsMenu = new GraphicsMenu(this.getGui());
		this.add(this.graphicsMenu);
	}
	
	/** @return See {@link #gui} */
	public ZabGui getGui(){
		return this.gui;
	}

	/** @return See {@link #fileMenu} */
	public FileMenu getFileMenu(){
		return this.fileMenu;
	}
	
	/** @return See {@link #editMenu} */
	public EditMenu getEditMenu(){
		return this.editMenu;
	}

	/** @return See {@link #graphicsMenu} */
	public GraphicsMenu getGraphicsMenu(){
		return this.graphicsMenu;
	}
	
}
