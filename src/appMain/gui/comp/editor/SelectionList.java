package appMain.gui.comp.editor;

import java.util.ArrayList;

import tab.TabPosition;
import tab.TabString;
import util.ArrayUtils;
import util.Copyable;
import util.ObjectUtils;

/**
 * An {@link ArrayList} containing {@link Selection} objects and utility methods for operating on that list
 * @author zrona
 */
public class SelectionList extends ArrayList<Selection> implements Copyable<SelectionList>{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create a new, empty selection list
	 */
	public SelectionList(){
		super();
	}
	
	/***/
	@Override
	public SelectionList copy(){
		SelectionList list = new SelectionList();
		for(Selection s : this) list.add(s.copy());
		return list;
	}

	/***/
	@Override
	public boolean add(Selection e){
		if(e == null) return false;
		return ArrayUtils.insertSorted(this, e, false);
	}
	
	/***/
	@Override
	public boolean remove(Object e){
		if(!ObjectUtils.isType(e, Selection.class)) return false;
		int index = ArrayUtils.binarySearch(this, (Selection)e);
		
		if(index == -1) return false;
		super.remove(index);
		return true;
	}
	
	/***/
	@Override
	public boolean contains(Object e){
		if(!ObjectUtils.isType(e, Selection.class)) return false;
		return this.isSelected((Selection)e);
	}
	
	/**
	 * Check if this list contains the given {@link TabPosition} at the given stringIndex
	 * @param p The {@link TabPosition}
	 * @param stringIndex The string index of the string which p is on
	 * @param str The {@link TabString} which is selected
	 * @return true if it is contained, false otherwise
	 */
	public boolean isSelected(TabPosition p, TabString str, int stringIndex){
		return this.isSelected(new Selection(p, str, stringIndex));
	}
	
	/**
	 * Check if this list contains the given {@link Selection}
	 * @param s The {@link Selection} to check if it is in the list
	 * @return true if it is contained, false otherwise
	 */
	public boolean isSelected(Selection s){
		return ArrayUtils.binarySearch(this, s) != -1;
	}

	/**
	 * Get the {@link TabPosition} in this list at the given index
	 * @param i The index
	 * @return The {@link TabPosition}, or null if it is out of bounds
	 */
	public TabPosition selectedPosition(int i){
		if(i < 0 || i >= this.size()) return null;
		return this.get(i).getPos();
	}
	
	/**
	 * Remove the specified {@link Selection} from this list
	 * @param s The {@link Selection} to unselect
	 * @return true if the selection was removed, false otherwise
	 */
	public boolean deselect(Selection s){
		return this.remove(s);
	}
	
}
