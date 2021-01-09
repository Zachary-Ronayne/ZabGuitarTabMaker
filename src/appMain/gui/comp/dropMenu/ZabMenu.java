package appMain.gui.comp.dropMenu;

import java.awt.Component;
import java.awt.Container;
import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JMenu;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;

/**
 * A particular menu used by the Zab Application for the drop down menu
 * @author zrona
 */
public class ZabMenu extends JMenu{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabGui} using this {@link ZabMenu}  */
	private ZabGui gui;
	
	/**
	 * Create a new ZabMenu and bring it to a default state
	 * @param name The name of the menu, i.e. the text to display
	 * @param gui See #gui
	 */
	public ZabMenu(String name, ZabGui gui){
		super(name);
		
		this.gui = gui;
		
		// Set the Theme appropriately
		ZabTheme.setToTheme(this);
	}
	
	/** @return See {@link #gui} */
	public ZabGui getGui(){
		return this.gui;
	}
	
	/**
	 * Get all of the components of this {@link ZabMenu}, as well as all of it's sub menus, 
	 * and all of the {@link MenuItem} objects in those menus
	 */
	@Override
	public Component[] getComponents(){
		ArrayList<Component> comps = new ArrayList<Component>();
		for(Component c : super.getComponents()){
			comps.add(c);
		}
		
		Component[] menuComps = this.getMenuComponents();
		for(Component c : menuComps){
			if(!(c instanceof Container)) continue;
			Container cont = (Container)c;
			comps.add(cont);
			comps.addAll(Arrays.asList(cont.getComponents()));
		}
		
		Component[] finalComps = new Component[comps.size()];
		
		for(int i = 0; i < finalComps.length; i++) finalComps[i] = comps.get(i);
		
		return finalComps;
	}
	
}
