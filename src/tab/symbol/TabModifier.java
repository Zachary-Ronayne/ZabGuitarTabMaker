package tab.symbol;

import java.io.PrintWriter;
import java.util.Scanner;

import util.Copyable;
import util.ObjectUtils;
import util.Saveable;

/**
 * An object tracking the modifier for a symbol, i.e. for hammer ons, pull offs, slides, and so on
 * @author zrona
 */
public class TabModifier implements Copyable<TabModifier>, Saveable{
	
	/** The text modifier which goes before symbol */
	private String before;
	
	/** The text modifier which goes after symbol */
	private String after;
	
	/**
	 * Create a modifier for a {@link TabSymbol}
	 * @param before See {@link #before}
	 * @param after See {@link #after}
	 */
	public TabModifier(String before, String after){
		this.setBefore(before);
		this.setAfter(after);
	}
	
	/**
	 * Create an empty {@link TabModifier}
	 */
	public TabModifier(){
		this("", "");
	}
	
	/***/
	@Override
	public TabModifier copy(){
		return new TabModifier(this.getBefore(), this.getAfter());
	}
	
	/**
	 * Get the {@link #before} of this {@link TabModifier}
	 * @return The string
	 */
	public String getBefore(){
		return before;
	}

	/**
	 * Set the {@link #before} of this {@link TabModifier}
	 * @param before The string
	 */
	public void setBefore(String before){
		this.before = before;
	}

	/**
	 * Get the {@link #after} of this {@link TabModifier}
	 * @return The string
	 */
	public String getAfter(){
		return after;
	}

	/**
	 * Set the {@link #after} of this {@link TabModifier}
	 * @return The string
	 */
	public void setAfter(String after){
		this.after = after;
	}
	
	/**
	 * Modify the given symbol based on this {@link TabModifier}
	 * @param s The symbol to modify
	 * @return The modified symbol
	 */
	public String modifySymbol(String s){
		return this.getBefore().concat(s).concat(this.getAfter());
	}

	/***/
	@Override
	public boolean load(Scanner reader){
		// Load the two strings needed for this object
		String[] load = Saveable.loadStrings(reader, 2);
		// If loading fails, return false
		if(load == null) return false;
		
		// Set the values and return success
		this.setBefore(load[0]);
		this.setAfter(load[1]);
		return true;
	}
	
	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Save both the before and after values
		return Saveable.saveToStrings(writer, new String[]{this.getBefore(), this.getAfter()}, true);
	}
	
	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, TabModifier.class)) return false;
		TabModifier m = (TabModifier)obj;
		return	super.equals(obj) ||
				this.getBefore().equals(m.getBefore()) &&
				this.getAfter().equals(m.getAfter());
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[TabModifier: \"");
		b.append(this.getBefore());
		b.append("\" \"");
		b.append(this.getAfter());
		b.append("\"]");
		return b.toString();
	}
	
}
