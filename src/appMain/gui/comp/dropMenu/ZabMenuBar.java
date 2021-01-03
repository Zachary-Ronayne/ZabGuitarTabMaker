package appMain.gui.comp.dropMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appMain.gui.frames.EditorFrame;
import tab.Tab;
import tab.TabTextExporter;

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
	
	/** The {@link ZabMenuItem} used for exporting a tab to a file */
	private ZabMenuItem exportItem;
	
	/** The {@link ZabGui} which uses this {@link ZabMenuBar} */
	private ZabGui gui;
	
	/** The {@link ActionListener} which handles exporting to a file */
	private ExportListener exporter;
	
	/**
	 * Create the drop down menu as a default state
	 */
	public ZabMenuBar(ZabGui gui){
		super();
		// Set the GUI
		this.gui = gui;
		
		// Set the Theme appropriately
		ZabTheme.setToTheme(this);
		
		// Set up and add all of the menu bars
		this.fileMenu = new ZabMenu("File");
		this.fileMenu.add(new ZabMenuItem("Save"));
		this.fileMenu.add(new ZabMenuItem("Load"));
		this.exportItem = new ZabMenuItem("Export");
		this.exporter = new ExportListener();
		this.exportItem.addActionListener(this.exporter);
		
		this.fileMenu.add(exportItem);
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
	
	/**
	 * @return See {@link #exportItem}
	 */
	public ZabMenuItem getExportItem(){
		return this.exportItem;
	}
	
	/**
	 * @return See {@link #exporter}
	 */
	public ExportListener getExporter(){
		return this.exporter;
	}
	
	/**
	 * @return See {@link #gui}
	 */
	public ZabGui getGui(){
		return this.gui;
	}
	
	/** Used by {@link ZabMenuBar#exportItem} when the item is clicked */
	public class ExportListener implements ActionListener{
		/** When the button is clicked, export the tab of the editor to a file */
		@Override
		public void actionPerformed(ActionEvent e){
			EditorFrame frame = getGui().getEditorFrame();
			Tab tab = frame.getOpenedTab();
			TabTextExporter.exportToFile(tab, "./saves", "export");
		}
		
	}
	
}
