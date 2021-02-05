package appMain.gui.comp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;

import appMain.gui.layout.ZabLayoutHandler;

/**
 * A {@link ZabPanel} for the Zab application which is a vertical list of {@link ZabLabel} objects. 
 * This also contains a title, which is at the top of the list, and by default in a bigger font than the rest of the text. 
 * By default the title is empty 
 * @author zrona
 */
public class ZabLabelList extends ZabPanel{
	private static final long serialVersionUID = 1L;
	
	/** The list of {@link ZabLabel} objects in this list */
	private ArrayList<ZabLabel> labels;
	
	/** The title of the list, displayed at the top */
	private ZabLabel title;
	
	/**
	 * Create a new {@link ZabLabelList} with all of the given text
	 * @param text The text for each label. The list will have one label for each element in text, in the order of the {@link Iterable}
	 */
	public ZabLabelList(List<String> text){
		super();
		
		// Set up layout
		ZabLayoutHandler.createVerticalLayout(this);
		
		// Add the title
		this.title = new ZabLabel("");
		this.title.setFontSize(20);
		this.add(title);
		
		// Add all text
		this.labels = new ArrayList<ZabLabel>();
		for(String s : text){
			ZabLabel lab = new ZabLabel(s);
			lab.setFontSize(15);
			this.add(lab);
			this.labels.add(lab);
		}
		
		// Set up a border
		this.setBorderSize(10);
	}
	
	/**
	 * Create a new {@link ZabLabelList} with all of the given text
	 * @param text The text for each label. The list will have one label for each element in text, in the order of the {@link Iterable}
	 */
	public ZabLabelList(String[] text){
		this(Arrays.asList(text));
	}
	
	/**
	 * Create a new {@link ZabLabelList} with no text
	 */
	public ZabLabelList(){
		this(new ArrayList<String>());
	}
	
	/** @return See {@link #labels} */
	public ArrayList<ZabLabel> getLabels(){
		return this.labels;
	}
	
	/** @return See {@link #title} */
	public ZabLabel getTitle(){
		return this.title;
	}
	
	/**
	 * Set the text of {@link #title}
	 * @param title The new text, can be empty to remove the title
	 */
	public void setTitle(String title){
		this.getTitle().setText(title);
	}
	
	/**
	 * Set the font size of every {@link ZabLabel} in this list
	 * @param size The new font size
	 */
	public void setFontSize(int size){
		for(ZabLabel lab : this.getLabels()) lab.setFontSize(size);
	}
	
	/**
	 * Set this label to use a line border with the background of this {@link ZabPanel} with the given size
	 * @param size The size of the border
	 */
	public void setBorderSize(int size){
		this.setBorder(BorderFactory.createLineBorder(getBackground(), size));
	}
	
}
