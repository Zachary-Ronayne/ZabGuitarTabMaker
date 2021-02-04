package appMain.gui.editor.frame;

import java.io.File;

import appMain.gui.ZabGui;
import appMain.gui.comp.GuiFrame;
import appMain.gui.comp.ZabFrame;
import appMain.gui.comp.ZabPanel;
import appMain.gui.editor.paint.TabPainter;
import appMain.gui.layout.ZabLayoutHandler;
import appUtils.ZabAppSettings;
import appUtils.ZabFileSaver;
import appUtils.settings.ZabSettings;
import tab.InstrumentFactory;
import tab.Tab;
import tab.TabTextExporter;

/**
 * A class holding the information for the primary editor for the Zab application
 */
public class EditorFrame extends ZabFrame{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabPanel} where all of the graphics for the {@link Tab} are drawn */
	private TabPainter tabScreen;
	
	/** The {@link EditorBar} holding all of the extra components acting as the menu of this {@link GuiFrame} */
	private EditorBar editorBar;
	
	/** The {@link ZabPanel} holding the {@link TabPainter} used by this {@link EditorFrame} */
	private ZabPanel paintHolder;
	
	/** The {@link Tab} which is currently being edited by this {@link EditorFrame} */
	private Tab openedTab;
	
	/**
	 * Create an {@link EditorFrame} at a default state
	 * @param gui The {@link ZabGui} which this {@link EditorFrame} will be a part of
	 */
	public EditorFrame(ZabGui gui){
		super(gui);
		
		// Set up the tab
		this.openedTab = InstrumentFactory.guitarStandard();

		// Set up the layout
		ZabLayoutHandler.createVerticalLayout(this);
		
		// Set up the menu holder
		this.editorBar = new EditorBar();
		this.add(this.editorBar);
		
		// Set up the graphics objects
		this.tabScreen = new TabPainter(this.getGui(), 0, 0, this.getOpenedTab());
		this.paintHolder = new ZabPanel();
		this.paintHolder.add(tabScreen);
		this.add(paintHolder);

		// Ensure the blank tab is marked as saved, this is so that an unmodified tab is not considered unsaved
		this.getTabScreen().getUndoStack().markSaved();
		
		// Update the screen's display
		repaint();
	}
	
	/**
	 * Update the size of the painting area in this {@link EditorFrame} with the new width and height
	 * @param width The new width of the painting area
	 * @param height The new height of the painting area
	 */
	public void updatePaintSize(int width, int height){
		// This doesn't make sense, but adding and removing tabScreen, which is a JPanel, updates the size, so whatever, it works
		paintHolder.remove(tabScreen);
		paintHolder.add(tabScreen);
		
		// Update the actual size
		this.tabScreen.setPaintSize(width, height);
	}
	
	/** @return See {@link #editorBar} */
	public EditorBar getEditorBar(){
		return this.editorBar;
	}
	
	/**
	 * Updates the size of the painting canvas
	 */
	@Override
	public void parentResized(int w, int h){
		this.updatePaintSize(w, h - editorBar.getHeight());
	}
	
	/** @return See {@link #tabScreen} */
	public TabPainter getTabScreen(){
		return this.tabScreen;
	}
	
	/** @return {@link #paintHolder} */
	public ZabPanel getPaintHolder(){
		return this.paintHolder;
	}
	
	/** @return See {@link #openedTab} */
	public Tab getOpenedTab(){
		return this.openedTab;
	}
	/** @param tab See {@link #openedTab} */
	public void setOpenedTab(Tab tab){
		this.openedTab = tab;
		this.getTabScreen().setTab(tab);
	}
	
	/**
	 * Called when the {@link ZabGui} holding this {@link EditorFrame} 
	 */
	@Override
	public void focused(){
		this.getTabScreen().requestFocusInWindow();
	}
	
	/**
	 * Using the given file, save {@link #openedTab} along with the current instance of {@link ZabSettings} in {@link ZabAppSettings}. 
	 * This method also handles all visual and update related events with saving.
	 * @param The file to use for saving, which should already have the correct extension
	 * @return true if the save was successful, false otherwise
	 */
	public boolean save(File file){
		boolean success;
		
		// Don't save the tab if it is null
		Tab tab = this.getOpenedTab();
		if(tab == null) success = false;
		else{
			// Perform the save
			success = ZabFileSaver.save(file, tab, true);
		}

		// If the save was successful, mark the editor as saved
		if(success) this.getTabScreen().getUndoStack().markSaved();
		
		// Update the save status
		this.getEditorBar().getFileStatusLab().updateSaveStatus(success);
		
		return success;
	}
	/**
	 * Using the given file, load {@link #openedTab} along with the current instance of {@link ZabSettings} in {@link ZabAppSettings}. 
	 * This method also handles all visual and update related events with loading.
	 * @param The file to use for loading, which should already have the correct extension
	 * @return true if the load was successful, false otherwise
	 */
	public boolean load(File file){
		// Ensure the opened tab exists, if it doesn't, set it to an empty tab
		Tab tab = this.getOpenedTab();
		if(tab == null){
			this.setOpenedTab(new Tab());
			tab = this.getOpenedTab();
		}
		
		// Load the tab from the file
		boolean success = ZabFileSaver.load(file, tab, true);
		
		// If the load succeeded, mark the editor as saved
		if(success) this.getTabScreen().getUndoStack().markSaved();
		
		// Update the load status
		this.getEditorBar().getFileStatusLab().updateLoadStatus(success);
		
		// Update the state of the TabPainter
		this.getTabScreen().updateLineTabCount();
		
		// Update the GUI to reflect the loaded tab
		getGui().repaint();
		
		return success;
	}
	
	/**
	 * Export {@link #openedTab} to the given file
	 * This method also handles all visual and update related events with exporting.
	 * @param The file to use for exporting 
	 * @return true if the export was successful, false otherwise
	 */
	public boolean export(File file){
		boolean success = TabTextExporter.exportToFile(this.getOpenedTab(), file);
		this.getEditorBar().getFileStatusLab().updateExportStatus(success); 
		return success;
	}
	
}
