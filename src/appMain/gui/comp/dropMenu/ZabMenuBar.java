package appMain.gui.comp.dropMenu;

import javax.swing.JMenuBar;

import appMain.gui.ZabTheme;

/**
 * The bar of drop down menus used by the Zab application
 * @author zrona
 */
public class ZabMenuBar extends JMenuBar{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabMenu} used for the file options */
	private ZabMenu fileMenu;
	
	/** The {@link ZabMenu} used for the edit options */
	private ZabMenu editMenu;
	
	/**
	 * Create the drop down menu as a default state
	 */
	public ZabMenuBar(){
		super();
		
		// Set the Theme appropriately
		ZabTheme.setToTheme(this);
		
		// Set up and add all of the menu bars
		this.fileMenu = new ZabMenu("File");
		this.fileMenu.add(new ZabMenuItem("Save"));
		this.fileMenu.add(new ZabMenuItem("Load"));
		this.fileMenu.add(new ZabMenuItem("Export"));
		this.add(this.fileMenu);
		
		this.editMenu = new ZabMenu("Edit");
		this.editMenu.add(new ZabMenuItem("Undo"));
		this.editMenu.add(new ZabMenuItem("Redo"));
		this.add(this.editMenu);
	}
	
	/**
	 * Get the {@link ZabMenuBar} for file
	 * @return See {@link #fileMenu}
	 */
	public ZabMenu getFileMenu(){
		return this.fileMenu;
	}

	/**
	 * Get the {@link ZabMenuBar} for edit
	 * @return See {@link #editMenu}
	 */
	public ZabMenu getEditMenu(){
		return this.editMenu;
	}
	
}
