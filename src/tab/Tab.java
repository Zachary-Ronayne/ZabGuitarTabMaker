package tab;

import java.util.ArrayList;

import music.Pitch;
import util.Copyable;
import util.ObjectUtils;

/**
 * An object representing a tablature diagram for a string instrument.
 * @author zrona
 */
public class Tab implements Copyable<Tab>{
	
	/**
	 * An ordered list of all of the strings in this tab.
	 * The first element is the string typically with the highest pitch,
	 * 	and the last element is the string typically with the lowest pitch.<br>
	 * For example, for a guitar in standard tuning, index 0 should be the high E string,
	 * 	and index 5 should be the low E string
	 */
	private ArrayList<TabString> strings;
	
	/**
	 * Create a Tab using the given list of Strings
	 * @param strings see {@link #strings}
	 */
	public Tab(ArrayList<TabString> strings){
		this.strings = strings;
	}
	
	/**
	 * Create an empty Tab with no strings
	 */
	public Tab(){
		this(new ArrayList<TabString>());
	}

	/***/
	@Override
	public Tab copy(){
		// Make an empty tab
		Tab tab = new Tab();
		
		// Copy over each string
		for(TabString s : this.getStrings()){
			tab.getStrings().add(s.copy());
		}
		return tab;
	}
	
	/**
	 * Get the {@link TabString} objects used in this {@link Tab}
	 * @return See {@link #strings}
	 */
	public ArrayList<TabString> getStrings(){
		return strings;
	}

	/**
	 * Set the {@link TabString} objects used in this {@link Tab}
	 * @param strings See {@link #strings}
	 */
	public void setStrings(ArrayList<TabString> strings){
		this.strings = strings;
	}
	
	/**
	 * Get the note integer, as defined in {@link Pitch#note} of the root note of the specified string
	 * @param string The index of the string, not the actual string number,
	 * 	i.e. use 0 for the first string, 1 for the second string, etc.
	 * @return The note integer
	 */
	public int getRootNote(int string){
		return this.getStrings().get(string).getRootNote();
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		Tab t = (Tab)obj;
		return	super.equals(obj) ||
				this.getStrings().equals(t.getStrings());
	}
	
}