package appMain.gui.editor.paint;

import music.NotePosition;
import tab.TabPosition;
import tab.TabString;
import util.Copyable;
import util.ObjectUtils;

/**
 * A helper object used to track an individually selected {@link TabPosition} and the {@link TabString} holding it string
 * @author zrona
 */
public class Selection implements Comparable<Selection>, Copyable<Selection>{
	/** A copy of the {@link TabPosition} of this selection, this does not represent the object which was used to create this selection */
	private TabPosition pos;
	/** The {@link TabString} which {@link #hold} is on */
	private TabString string;
	/** The index of {@link #string} in a tab */
	private int stringIndex;
	
	/**
	 * Create a new selection which will keep track of the {@link NotePosition} of a {@link TabPosition}.
	 * 	This object tracks the position on a particular string, any changes to the original {@link TabPosition} 
	 * sent in this constructor will not carry over to this object.
	 * @param pos See {@link #pos} This object will not be stored, but a copy of it. Future modifications to the given object will not effect this selection.
	 * @param string See {@link #string}
	 * @param stringIndex See {@link #stringIndex}
	 * @throws IllegalArgumentException If pos is null or string is null
	 */
	public Selection(TabPosition pos, TabString string, int stringIndex){
		super();
		if(pos == null) throw new IllegalArgumentException("The TabPosition of a selection cannot be null");
		if(string == null) throw new IllegalArgumentException("The TabString of a selection cannot be null");
		
		this.pos = pos.copy();
		this.string = string;
		this.stringIndex = stringIndex;
	}
	
	/***/
	@Override
	public Selection copy(){
		return new Selection(ObjectUtils.copy(this.getPos()), ObjectUtils.copy(this.getString()), stringIndex);
	}
	
	/**
	 * Find the {@link TabPosition} on {@link #string} which was used to create this selection.
	 * If that position is not on the string anymore, this method returns null
	 * @return The position, or null if it is not on the string
	 */
	public TabPosition getStringPos(){
		TabPosition p = this.getString().findPosition(this.getPosition());
		if(this.getPos().equals(p)) return p;
		else return null;
	}
	/** @return See {@link #pos} */
	public TabPosition getPos(){
		return pos;
	}
	/** @return See {@link #pos} */
	public double getPosition(){
		return pos.getPos();
	}
	
	/** @return See {@link #string} */
	public TabString getString(){
		return string;
	}
	public int getStringIndex(){
		return this.stringIndex;
	}
	
	/**
	 * First compare the string indexes, sort by that, otherwise compare the positions
	 */
	@Override
	public int compareTo(Selection s){
		if(s == null || s.getPos() == null || this.getPos() == null) return -1;
		
		int thisI = this.getStringIndex();
		int sI = s.getStringIndex();
		if(thisI < sI) return -1;
		else if(thisI > sI) return 1;
		
		return this.getPos().compareTo(s.getPos());
	}
	
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		Selection s = (Selection)obj;
		return super.equals(obj) ||
				this.getPos().equals(s.getPos()) &&
				this.getString().equals(s.getString()) &&
				this.getStringIndex() == s.getStringIndex();
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("[Selection: ");
		sb.append(this.getPos());
		sb.append(", ");
		sb.append(this.getString());
		sb.append(", String index: ");
		sb.append(this.getStringIndex());
		sb.append("]");
		return sb.toString();
	}
	
}
