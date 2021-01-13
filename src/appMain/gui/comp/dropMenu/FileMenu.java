package appMain.gui.comp.dropMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabExporterDialog;
import appMain.gui.comp.ZabFileChooser;
import appUtils.ZabConstants;

/**
 * The {@link ZabMenu} in {@link ZabMenuBar} handling file related items
 * @author zrona
 */
public class FileMenu extends ZabMenu{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabFileChooser} used for all actions involving selecting files */
	private ZabFileChooser fileChooser;
	
	/** The {@link ZabMenuItem} used for saving a tab to a file */
	private ZabMenuItem saveItem;
	/** The {@link ActionListener} which handles saving to a file */
	private SaveListener saver;

	/** The {@link ZabMenuItem} used for loading a tab from a file */
	private ZabMenuItem loadItem;
	/** The {@link ActionListener} which handles loading from a file */
	private LoadListener loader;

	/** The {@link ZabMenuItem} used for exporting a tab to a file */
	private ZabMenuItem exportItem;
	/** The {@link ActionListener} which handles click the JMenuItem for exporting to a file */
	private ExportListener exporter;
	/** The {@link ZabExporterDialog} which handles user input for exporting to a file */
	private ZabExporterDialog exportDialog;
	
	/**
	 * Create a new default {@link FileMenu}
	 * @param gui See {@link ZabMenu#gui}
	 */
	public FileMenu(ZabGui gui){
		super("File", gui);
		
		// File related items
		this.createFileChooser();
		
		// save
		this.saveItem = new ZabMenuItem("Save");
		this.saver = new SaveListener();
		this.saveItem.addActionListener(this.saver);
		this.add(saveItem);
		
		// load
		this.loadItem = new ZabMenuItem("Load");
		this.loader = new LoadListener();
		this.loadItem.addActionListener(loader);
		this.add(loadItem);
		
		// export
		this.exportItem = new ZabMenuItem("Export");
		this.exporter = new ExportListener();
		this.exportItem.addActionListener(this.exporter);
		this.exportDialog = new ZabExporterDialog(this.getGui());
		this.add(exportItem);
	}
	
	/**
	 * Private utility method for setting up the {@link ZabFileChooser} to have the desired theme
	 */
	private void createFileChooser(){
		// Save the original look and feel
		LookAndFeel old = UIManager.getLookAndFeel();
		
		try{
			// Load the system look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			// Create the file chooser
			this.fileChooser = new ZabFileChooser(this.getGui().getEditorFrame());
			
			// Restore the old look and feel
			UIManager.setLookAndFeel(old);
		}catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
			if(ZabConstants.PRINT_ERRORS) e.printStackTrace();
		}
	}
	
	/** @return See {@link #fileChooser} */
	public ZabFileChooser getFileChooser(){
		return this.fileChooser;
	}
	
	/** @return See {@link #saveItem} */
	public ZabMenuItem getSaveItem(){
		return this.saveItem;
	}
	/** @return {@link #saver} */
	public SaveListener getSaver(){
		return this.saver;
	}
	
	/** @return See {@link #loadItem} */
	public ZabMenuItem getLoadItem(){
		return this.loadItem;
	}
	/** @return {@link #loader} */
	public LoadListener getLoader(){
		return this.loader;
	}

	/** @return See {@link #exportItem} */
	public ZabMenuItem getExportItem(){
		return this.exportItem;
	}
	/** @return See {@link #exporter} */
	public ExportListener getExporter(){
		return this.exporter;
	}
	
	/** @return See {@link #exportDialog} */
	public ZabExporterDialog getExportDialog(){
		return this.exportDialog;
	}
	
	/** Used by {@link ZabMenuBar#exportItem} when the item is clicked */
	public class ExportListener implements ActionListener{
		/** When the button is clicked, export the tab of the editor to a file */
		@Override
		public void actionPerformed(ActionEvent e){
			getExportDialog().open();
		}
	}
	
	/** Used by {@link ZabMenuBar#saveItem} when the item is clicked */
	public class SaveListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			getFileChooser().saveTab();
		}
	}
	
	/** Used by {@link ZabMenuBar#loadItem} when the item is clicked */
	public class LoadListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			getFileChooser().loadTab();
		}
	}
}
