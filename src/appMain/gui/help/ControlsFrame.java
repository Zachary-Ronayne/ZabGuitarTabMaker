package appMain.gui.help;

import javax.swing.BorderFactory;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabFrame;
import appMain.gui.comp.ZabLabel;
import appMain.gui.comp.ZabLabelList;
import appMain.gui.comp.ZabPanel;
import appMain.gui.layout.ZabLayoutHandler;
import lang.AbstractLanguage;
import lang.Language;

/**
 * A {@link ZabFrame} which displays text for showing the controls of a gui
 * @author zrona
 */
public class ControlsFrame extends ZabFrame{
	private static final long serialVersionUID = 1L;
	
	/** The title of this {@link ControlsFrame}, displayed at the top */
	private ZabLabel title;
	
	/** The main {@link ZabPanel} which holds all of the {@link ZabLabelList} objects holding all of the text explaining controls */
	private ZabPanel mainPanel;
	
	/** The {@link ZabLabelList} containing text about mouse controls */
	private ZabLabelList mouseInfo;
	
	/** The {@link ZabLabelList} containing text about keyboard controls */
	private ZabLabelList keyInfo;
	
	/** The {@link ZabLabelList} containing text about keyboard controls to add modifiers */
	private ZabLabelList modifiersInfo;
	
	/** The {@link ZabLabelList} containing text about camera zoom related controls */
	private ZabLabelList zoomInfo;
	
	/**
	 * Create a {@link ControlsFrame} in the default state
	 * @param gui
	 */
	public ControlsFrame(ZabGui gui){
		super(gui);
		AbstractLanguage lang = Language.get();
		
		// Set up the layout
		ZabLayoutHandler.createVerticalLayout(this);

		// Add the title
		title = new ZabLabel(lang.controls());
		title.setFontSize(25);
		this.add(title);
		
		// Set up panel for holding the main controls
		this.mainPanel = new ZabPanel();
		ZabLayoutHandler.createHorizontalLayout(mainPanel);
		this.add(this.mainPanel);
		
		// Set up each list
		this.mouseInfo = new ZabLabelList(new String[]{
			lang.leftClick() + ":",
			"    " + lang.clickSelectNote(),
			"        " + lang.clickSelectNoteLine(),
			"    " + lang.createBoxSelection(),
			"    " + lang.selectingReplaces(),
			"         " + lang.ctrlKey() + ": " + lang.addToSelection(),
			"    " + lang.clickToUnselect(),
			"    " + lang.clickDragMoveSelection(),
			lang.middleClick() + ": " + lang.clickDragMoveCamera(),
			lang.rightClick() + ": " + lang.placeOpenNote(),
			lang.mouseWheel() + ": " + lang.mouseWheelZoom()
		});
		this.mouseInfo.setTitle(lang.mouse());
		this.mainPanel.add(this.mouseInfo);
		
		this.keyInfo = new ZabLabelList(new String[]{
			lang.ctrlKey() + " R: " + lang.resetCamera(),
			lang.ctrlKey() + " D: " + lang.unselectSelection(),
			lang.ctrlKey() + " " + lang.shiftKey() + " D / " + lang.deleteKey() + ": " + lang.deleteSelection(),
			lang.ctrlKey() + " A: " + lang.selectAllNotes(),
			lang.escapeKey() + ": " + lang.cancelSelection(),
			lang.ctrlKey() + " S: " + lang.saveTab(),
			lang.ctrlKey() + " S: " + lang.saveAsTab(),
			lang.ctrlKey() + " L: " + lang.loadTab(),
			lang.ctrlKey() + " " + lang.shiftKey() + " e: " + lang.exportWindow(),
			lang.ctrlKey() + " n: " + lang.newTab(),
			lang.ctrlKey() + " " + lang.minus() + ": " + lang.zoomOut(),
			lang.ctrlKey() + " " + lang.plus() + ":" + lang.zoomIn(),
			lang.number() + " / " + lang.minus() + ": " + lang.typeTabNumber()
		});
		this.keyInfo.setTitle(lang.keyboard());
		this.mainPanel.add(this.keyInfo);
		
		this.modifiersInfo = new ZabLabelList(new String[]{
			lang.pressKeyApplyModifier(),
			lang.shiftKey() + ": " + lang.combineModifiers(),
			lang.shiftKey() + " " + lang.spacebar() + " : " + lang.clearModifiers(),
			"P: " + lang.pullOff(),
			"H: " + lang.hammerOn(),
			"/: " + lang.slideUp(),
			"\\: " + lang.slideDown(),
			"B: " + lang.bend(),
			"R: " + lang.pinchHarmonic(),
			"~: " + lang.vibrato(),
			"T: " + lang.tap(),
			"( / ): " + lang.ghostNote(),
			"< / >: " + lang.naturalHarmonic(),
		});
		this.modifiersInfo.setTitle(lang.modifiersDescription());
		this.mainPanel.add(this.modifiersInfo);
		
		this.zoomInfo = new ZabLabelList(new String[]{
			lang.altKey() + " : " + lang.zoomOnlyX(),
			lang.shiftKey() + " : " + lang.zoomOnlyY()
		});
		this.zoomInfo.setTitle(lang.cameraZooming());
		this.add(this.zoomInfo);
		
		// Set up a border
		this.setBorder(BorderFactory.createLineBorder(this.getBackground(), 10));
	}

	/** @return See {@link #title} */
	public ZabLabel getTitle(){
		return this.title;
	}
	
	/** @return See {@link #mainPanel} */
	public ZabPanel getMainPanel(){
		return this.mainPanel;
	}
	/** @return See {@link #mouseInfo} */
	public ZabLabelList getMouseInfo(){
		return this.mouseInfo;
	}
	/** @return See {@link #keyInfo} */
	public ZabLabelList getKeyInfo(){
		return this.keyInfo;
	}
	/** @return See {@link #modifiersInfo} */
	public ZabLabelList getModifiersInfo(){
		return this.modifiersInfo;
	}
	/** @return See {@link #zoomInfo} */
	public ZabLabelList getZoomInfo(){
		return this.zoomInfo;
	}
	
	/** Does nothing */
	@Override
	public void parentResized(int w, int h){}
	
}
