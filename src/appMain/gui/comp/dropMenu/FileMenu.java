package appMain.gui.comp.dropMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabExporterDialog;
import appMain.gui.comp.ZabFileChooser;
import appMain.gui.comp.editor.TabPainter;
import appMain.gui.frames.editor.FileStatusLabel;
import appUtils.ZabConstants;
import lang.AbstractLanguage;
import lang.Language;
import tab.InstrumentFactory;
import tab.Tab;

/**
 * The {@link ZabMenu} in {@link ZabMenuBar} handling file related items
 * @author zrona
 */
public class FileMenu extends ZabMenu{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabFileChooser} used for all actions involving selecting files */
	private ZabFileChooser fileChooser;
	
	/** The {@link File} Which is currently loaded in the editor to save and load to. Can be null if no file is loaded */
	private File loadedFile;
	
	/** The {@link ZabMenuItem} used for creating a new tab file */
	private ZabMenuItem newFileItem;
	/** The {@link ActionListener} which is used by {@link #newFileItem} */
	private NewFileListener newFileMaker;
	
	/** The {@link ZabMenuItem} used for quickly saving a loaded tab to a file */
	private ZabMenuItem saveItem;
	/** The {@link ZabMenuItem} used for saving a tab to a file by selecting the file location */
	private ZabMenuItem saveAsItem;
	/** The {@link ActionListener} which handles quickly saving to an opened file */
	private SaveListener saver;
	/** The {@link ActionListener} which handles selecting a file to save to to a file */
	private SaveAsListener saveAsAction;

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
		super("", gui);
		AbstractLanguage lang = Language.get();
		this.setText(lang.file());
		
		// File related items
		this.createFileChooser();
		this.loadedFile = null;
		
		// new
		this.newFileItem = new ZabMenuItem(lang.newFile());
		this.newFileMaker = new NewFileListener();
		this.newFileItem.addActionListener(newFileMaker);
		this.add(newFileItem);
		
		// save
		this.saveItem = new ZabMenuItem(lang.save());
		this.saveAsItem = new ZabMenuItem(lang.saveAs());
		this.saver = new SaveListener();
		this.saveAsAction = new SaveAsListener();
		this.saveItem.addActionListener(this.saver);
		this.saveAsItem.addActionListener(this.saveAsAction);
		this.add(saveItem);
		this.add(saveAsItem);
		
		// load
		this.loadItem = new ZabMenuItem(lang.load());
		this.loader = new LoadListener();
		this.loadItem.addActionListener(loader);
		this.add(loadItem);
		
		// export
		this.exportItem = new ZabMenuItem(lang.export());
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
	/** @return See {@link #loadedFile} */
	public File getLoadedFile(){
		return this.loadedFile;
	}
	/** 
	 * Set the file currently loaded by the menu. Also updates the displayed name in the gui
	 * @param f See {@link #loadedFile} Can be null to get rid of the file 
	 */
	public void setLoadedFile(File f){
		this.loadedFile = f;
		String s = (this.loadedFile == null) ? "" : this.loadedFile.getName();
		this.getGui().updateTitle(s);
	}
	
	/** @return See {@link #newFileItem} */
	public ZabMenuItem getNewFileItem(){
		return this.newFileItem;
	}
	/** @return See {@link #newFileMaker} */
	public NewFileListener getNewFileMaker(){
		return this.newFileMaker;
	}
	
	/** @return See {@link #saveItem} */
	public ZabMenuItem getSaveItem(){
		return this.saveItem;
	}
	/** @return See {@link #saveAsItem} */
	public ZabMenuItem getSaveAsItem(){
		return this.saveAsItem;
	}
	/** @return {@link #saver} */
	public SaveListener getSaver(){
		return this.saver;
	}
	/** @return {@link #saveAsAction} */
	public SaveAsListener getSaveAsAction(){
		return this.saveAsAction;
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
	
	/**
	 * Remove the current tab and file of the Zab Application, creating a new file
	 */
	public void newFile(){
		this.setLoadedFile(null);
		TabPainter paint = this.getGui().getEditorFrame().getTabScreen();
		this.getGui().getEditorFrame().setOpenedTab(InstrumentFactory.guitarStandard());
		paint.resetCamera();
		paint.repaint();
	}
	
	/**
	 * Utility method used by methods like save and load, for getting the label which must be updated
	 * @return The label
	 */
	private FileStatusLabel getFileStatusLabel(){
		return this.getGui().getEditorFrame().getEditorBar().getFileStatusLab();
	}
	
	/**
	 * Quickly save the current {@link Tab} to the current loaded file. 
	 * Or, if no file is loaded, save the file as.
	 * @return true on a successful save, false otherwise
	 */
	public boolean save(){
		ZabFileChooser choose = getFileChooser();
		File f = getLoadedFile();
		
		boolean success;
		// If there is is no file, then save as, asking for a new file
		if(f == null) success = this.saveAs();
		// Otherwise, save to that file
		else success = choose.saveTab(f);
		
		// Set the current file status based on if the save succeeded
		this.getFileStatusLabel().updateSaveStatus(success);
		
		// Return the success status
		return success;
	}
	
	/**
	 * Save the current {@link Tab} as a file, opening the dialog and saving the file
	 * @return true on a successful save, false otherwise
	 */
	public boolean saveAs(){
		// If the file is saved successfully, grab the file and use it as the one to load
		ZabFileChooser choose = this.getFileChooser();
		boolean success = choose.saveTab();
		if(success) this.setLoadedFile(choose.getSelectedFile());
		
		// Update the save status
		this.getFileStatusLabel().updateSaveStatus(success);
		
		return success;
	}
	
	/**
	 * Load a tab from a file by opening the dialog box and prompting the user to select a file. 
	 * If the load succeeds, the loaded file will be used as the file to save to
	 * @return true if the load succeeded, false otherwise
	 */
	public boolean load(){
		boolean success = this.getFileChooser().loadTab();
		if(success) this.setLoadedFile(this.getFileChooser().getSelectedFile());

		// Update the load status
		this.getFileStatusLabel().updateLoadStatus(success);
		return success;
	}
	
	/**
	 * Open the dialog for exporting the tab of the editor to a file
	 */
	public void openExportDialog(){
		this.getExportDialog().open();
	}
	
	/** Used by {@link FileMenu#newFileItem} when the item is clicked */
	public class NewFileListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			newFile();
		}
	}
	
	/** Used by {@link FileMenu#saveItem} when the item is clicked */
	public class SaveListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			save();
		}
	}
	
	/** Used by {@link FileMenu#saveAsItem} when the item is clicked */
	public class SaveAsListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			saveAs();
		}
	}
	
	/** Used by {@link FileMenu#loadItem} when the item is clicked */
	public class LoadListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			load();
		}
	}
	
	/** Used by {@link FileMenu#exportItem} when the item is clicked */
	public class ExportListener implements ActionListener{
		/** When the button is clicked, open the dialog for exporting the tab of the editor to a file */
		@Override
		public void actionPerformed(ActionEvent e){
			openExportDialog();
		}
	}
}
