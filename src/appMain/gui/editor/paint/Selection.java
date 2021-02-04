package appMain.gui.editor.paint;

import tab.TabPosition;
import tab.TabString;
import util.Copyable;
import util.ObjectUtils;

/**
 * A helper object used to track an individually selected {@link TabPosition} and the {@link TabString} holding it string
 * @author zrona
 */
public class Selection implements Comparable<Selection>, Copyable<Selection>{
	/** The {@link TabPosition} of this selection */
	private TabPosition pos;
	/** The {@link TabString} which {@link #hold} is on */
	private TabString string;
	/** The index of {@link #string} in a tab */
	private int stringIndex;
	
	/**
	 * Create a new selection
	 * @param pos See #pos
	 * @param string See {@link #string}
	 * @param stringIndex See {@link #stringIndex}
	 */
	public Selection(TabPosition pos, TabString string, int stringIndex){
		super();
		this.pos = pos;
		this.string = string;
		this.stringIndex = stringIndex;
	}
	
	/***/
	@Override
	public Selection copy(){
		return new Selection(ObjectUtils.copy(this.getPos()), ObjectUtils.copy(this.getString()), stringIndex);
	}
	
	/** @return See {@link #pos} */
	public TabPosition getPos(){
		return pos;
	}
	/** @return The position value of the symbol in {@link #pos}, or -1 if pos is null */
	public double getPosition(){
		if(pos == null) return -1;
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
