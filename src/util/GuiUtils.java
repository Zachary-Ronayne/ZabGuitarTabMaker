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

		ArrayUtils.addWithoutDuplicate(list, c);
		
		if(!(c instanceof Container)){
			return list;
		}
		
		Container cont = (Container)c;
		
		Component[] comps = cont.getComponents();
		
		for(Component comp : comps){
			ArrayUtils.addWithoutDuplicate(list, comp);
			ArrayList<Component> newComps = getAllComponents(comp);
			ArrayUtils.addManyWithoutDuplicate(list, newComps);
		}
		
		return list;
	}
	
	/** Cannot instantiate {@link GuiUtils} */
	private GuiUtils(){}
	
}
