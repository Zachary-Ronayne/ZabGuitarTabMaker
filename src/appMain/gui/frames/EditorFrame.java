package appMain.gui.frames;

import java.awt.Font;

import javax.swing.JLabel;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appMain.gui.comp.ZabPanel;
import appMain.gui.comp.editor.TabPainter;
import appMain.gui.layout.ZabLayoutHandler;
import lang.Language;
import tab.InstrumentFactory;
import tab.Tab;

/**
 * A class holding the information for the primary editor for the Zab application
 */
public class EditorFrame extends ZabFrame{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabPanel} where all of the graphics for the {@link Tab} are drawn */
	private TabPainter tabScreen;
	
	/** The {@link ZabPanel} holding all of the extra components acting as the menu of this {@link GuiFrame} */
	private ZabPanel menuHolder;
	
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
		menuHolder = new ZabPanel();
		ZabLayoutHandler.createHorizontalLayout(menuHolder);
		this.add(menuHolder);
		
		// Temporary title placeholder
		JLabel lab = new JLabel();
		ZabTheme.setToTheme(lab);
		lab.setText(Language.get().edit());
		lab.setFont(new Font("Arial", Font.PLAIN, 20));
		menuHolder.add(lab);
		
		// Set up the graphics objects
		this.tabScreen = new TabPainter(0, 0, this.getOpenedTab());
		this.paintHolder = new ZabPanel();
		this.paintHolder.add(tabScreen);
		this.add(paintHolder);

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
	
	/**
	 * Updates the size of the painting canvas
	 */
	@Override
	public void parentResized(int w, int h){
		this.updatePaintSize(w, h - menuHolder.getHeight());
	}
	
	/** @return See {@link #tabScreen} */
	public TabPainter getTabScreen(){
		return this.tabScreen;
	}
	
	/** @return {@link #menuHolder} */
	public ZabPanel getMenuHolder(){
		return this.menuHolder;
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
	
}
