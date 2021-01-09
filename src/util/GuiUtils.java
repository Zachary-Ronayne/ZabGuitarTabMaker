package util;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;

/**
 * A class with utility methods for handling processing Gui related objects
 * @author zrona
 */
public class GuiUtils{
	
	/**
	 * Get a list of every {@link Component} contained within the given {@link Component}
	 * @param c The component
	 * @return The list, or an empty list if c is null
	 */
	public static ArrayList<Component> getAllComponents(Component c){
		ArrayList<Component> list = new ArrayList<Component>();
		if(c == null) return list;
		
		if(!(c instanceof Container)){
			list.add(c);
			return list;
		}
		
		Container cont = (Container)c;
		
		Component[] comps = cont.getComponents();
		
		for(Component comp : comps){
			if(!list.contains(comp)){
				list.add(comp);
			}
			list.addAll(getAllComponents(comp));
		}
		
		return list;
	}
	
	/** Cannot instantiate {@link GuiUtils} */
	private GuiUtils(){}
	
}
