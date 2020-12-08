package tab.symbol;

import tab.Tab;

/**
 * An object tracking the position of a {@link TabSymbol} in a {@link Tab}
 * @author zrona
 */
public class TabPosition{
	
	/** The position held by this {@link TabPosition} representing the number of whole notes to where the position is */
	private double value;
	
	/**
	 * Create a new {@link TabPosition} with the given value
	 * @param value See {@link #value}
	 */
	public TabPosition(double value){
		this.setPosition(value);
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
	 * @param position The position
	 */
	public void setPosition(double position){
		this.value = position;
	}
	
}
