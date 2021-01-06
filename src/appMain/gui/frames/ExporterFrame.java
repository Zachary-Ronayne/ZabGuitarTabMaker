package appMain.gui.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabButton;
import appMain.gui.comp.ZabExporterDialog;
import appMain.gui.comp.ZabFileChooser;
import appMain.gui.layout.ZabLayoutHandler;
import tab.Tab;

/**
 * A {@link ZabFrame} for selecting how a {@link Tab} is exported
 * @author zrona
 */
public class ExporterFrame extends ZabFrame{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabButton} used for the final export */
	private ZabButton exportButton;
	
	/** The {@link ZabButton} used for selecting a file */
	private ZabButton fileSelectButton;
	/** The {@link File} which will be exported to, can be null if no file is elected */
	private File exportFile;
	/** The {@link ZabFileChooser} used by this {@link ExporterFrame} to select files */
	private ZabFileChooser fileChooser;
	
	/**
	 * Create an {@link ExporterFrame} in the default state
	 * @param gui See {@link ZabFrame#gui}
	 */
	public ExporterFrame(ZabGui gui){
		super(gui);

		// Set up the layout
		ZabLayoutHandler.createVerticalLayout(this);
		
		// Set up the file select button
		this.fileChooser = new ZabFileChooser(this);
		this.fileSelectButton = new ZabButton();
		this.fileSelectButton.addActionListener(new FileSelectListener());
		this.setExportFile(null);
		this.add(this.fileSelectButton);
		
		// Set up the export button
		this.exportButton = new ZabButton();
		this.exportButton.setText("Export");
		this.exportButton.setFontSize(30);
		this.exportButton.addActionListener(new ExportListener());
		this.add(this.exportButton);
	}
	
	/** @return See {@link #exporter} */
	public ZabExporterDialog getExporter(){
		return this.getGui().getZabMenuBar().getExportDialog();
	}
	
	/** @return See {@link #getFileSelectButton} */
	public ZabButton getFileSelectButton(){
		return this.fileSelectButton;
	}
	/** @return See {@link #fileChooser} */
	public ZabFileChooser getFileChooser(){
		return this.fileChooser;
	}
	
	/** @return See {@link #exportFile} */
	public File getExportFile(){
		return this.exportFile;
	}
	/** @param exportFile See {@link #exportFile} */
	public void setExportFile(File exportFile){
		this.exportFile = exportFile;
		String text = (exportFile == null) ? "no path selected" : exportFile.getPath();
		this.fileSelectButton.setText(text);
	}
	
	/** @return See {@link #exportButton} */
	public ZabButton getExportButton(){
		return this.exportButton;
	}
	
	/** Does nothing */
	@Override
	public void parentResized(int w, int h){}
	
	/**
	 * The {@link ActionListener} used by {@link ExporterFrame#exportButton} 
	 * @author zrona
	 */
	public class ExportListener implements ActionListener{
		/***/
		@Override
		public void actionPerformed(ActionEvent e){
			getExporter().export();
		}
	}
	
	/**
	 * The {@link ActionListener} used by {@link ExporterFrame#fileSelectButton} 
	 * @author zrona
	 */
	public class FileSelectListener implements ActionListener{
		/***/
		@Override
		public void actionPerformed(ActionEvent e){
			// Get the file chooser and have the user select a file
			ZabFileChooser choose = getFileChooser();
			ZabExporterDialog dialog = getExporter();
			dialog.setVisible(false);
			setExportFile(choose.exportSelect("txt"));
			dialog.setVisible(true);
		}
	}
	
}
