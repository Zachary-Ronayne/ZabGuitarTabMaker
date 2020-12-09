package tab.symbol;

import tab.Tab;
import util.Copyable;
import util.ObjectUtils;

/**
 * An object tracking the position of a {@link TabSymbol} in a {@link Tab}
 * @author zrona
 */
public class TabPosition implements Copyable<TabPosition>{
	
	/** The position held by this {@link TabPosition} representing the number of whole notes to where the position is */
	private double value;
	
	/**
	 * Create a new {@link TabPosition} with the given value
	 * @param value See {@link #value}
	 */
	public TabPosition(double value){
		this.setValue(value);
	}
	
	/***/
	@Override
	public TabPosition copy(){
		return new TabPosition(this.getValue());
	}
	
	/**
	 * Get the {@link #value} of this {@link TabPosition}
	 * @return The position value
	 */
	public double getValue(){
		return value;
	}

	/**
	 * Set the {@link #value} of this {@link TabPosition}
	 * @param value The position value
	 */
	public void setValue(double value){
		this.value = value;
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		TabPosition p = (TabPosition)obj;
		return 	super.equals(obj) ||
				this.getValue() == p.getValue();
	}
	
}
