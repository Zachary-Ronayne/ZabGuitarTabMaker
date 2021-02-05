package appMain.gui.export;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabButton;
import appMain.gui.comp.ZabFileChooser;
import appMain.gui.comp.ZabFrame;
import appMain.gui.dropMenu.ZabMenuBar;
import appMain.gui.layout.ZabLayoutHandler;
import lang.AbstractLanguage;
import lang.Language;
import tab.Tab;

/**
 * A {@link ZabFrame} for selecting how a {@link Tab} is exported
 * @author zrona
 */
public class ExportFrame extends ZabFrame{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabButton} used for selecting a file */
	private ZabButton fileSelectButton;
	/** The {@link ZabFileChooser} used to select files */
	private ZabFileChooser fileChooser;
	/** The {@link FileSelectListener} used by {@link #fileSelectButton} */
	private FileSelectListener fileSelector;
	
	/** The {@link ZabButton} used for the final export */
	private ZabButton exportButton;
	/** The {@link ExportListener} used by {@link #exportButton} */
	private ExportListener exporter;
	/** The {@link File} which will be exported to, can be null if no file is elected */
	private File exportFile;
	
	/**
	 * Create an {@link ExportFrame} in the default state
	 * @param gui See {@link ZabFrame#gui}
	 */
	public ExportFrame(ZabGui gui){
		super(gui);
		AbstractLanguage lang = Language.get();

		// Set up the layout
		ZabLayoutHandler.createVerticalLayout(this);
		
		// Set up the file select button
		this.fileChooser = new ZabFileChooser(this);
		this.fileSelectButton = new ZabButton();
		this.fileSelector = new FileSelectListener();
		this.fileSelectButton.addActionListener(this.fileSelector);
		this.setExportFile(null);
		this.add(this.fileSelectButton);
		
		// Set up the export button
		this.exportButton = new ZabButton();
		this.exportButton.setText(lang.export());
		this.exportButton.setFontSize(30);
		this.exporter = new ExportListener();
		this.exportButton.addActionListener(this.exporter);
		this.add(this.exportButton);
	}
	
	/** @return See {@link #getFileSelectButton} */
	public ZabButton getFileSelectButton(){
		return this.fileSelectButton;
	}
	/** @return See {@link #fileSelector} */
	public FileSelectListener getFileSelector(){
		return this.fileSelector;
	}
	
	/** @return See {@link #fileChooser} */
	public ZabFileChooser getFileChooser(){
		return this.fileChooser;
	}
	
	/** @return The {@link ExportDialog} which this {@link ExportFrame#gui} uses from the {@link ZabMenuBar#exportDialog} */
	public ExportDialog getExportDialog(){
		return this.getGui().getZabMenuBar().getFileMenu().getExportDialog();
	}
	/** @return See {@link #exportButton} */
	public ZabButton getExportButton(){
		return this.exportButton;
	}
	/** @return See {@link #exporter} */
	public ExportListener getExporter(){
		return this.exporter;
	}
	
	/** @return See {@link #exportFile} */
	public File getExportFile(){
		return this.exportFile;
	}
	/** @param exportFile See {@link #exportFile} */
	public void setExportFile(File exportFile){
		this.exportFile = exportFile;
		String text = (exportFile == null) ? Language.get().noPathSelected() : exportFile.getPath();
		this.fileSelectButton.setText(text);
	}
	
	/** Does nothing */
	@Override
	public void parentResized(int w, int h){}
	
	/**
	 * The {@link ActionListener} used by {@link ExportFrame#fileSelectButton} 
	 * @author zrona
	 */
	public class FileSelectListener implements ActionListener{
		/***/
		@Override
		public void actionPerformed(ActionEvent e){
			// Get the file chooser and have the user select a file
			ZabFileChooser choose = getFileChooser();
			ExportDialog dialog = getExportDialog();
			dialog.setAlwaysOnTop(false);
			setExportFile(choose.exportSelect("txt"));
			dialog.setAlwaysOnTop(true);
		}
	}
	
	/**
	 * The {@link ActionListener} used by {@link ExportFrame#exportButton} 
	 * @author zrona
	 */
	public class ExportListener implements ActionListener{
		/***/
		@Override
		public void actionPerformed(ActionEvent e){
			getExportDialog().export();
		}
	}
	
}
