package tab.symbol;

import tab.Tab;

/**
 * An object tracking the position of a {@link TabSymbol} in a {@link Tab}
 * @author zrona
 */
public class TabPosition{
	
	/** The position held by this {@link TabPosition} */
	private double position;
	
	/**
	 * Create a new {@link TabPosition} with the given value
	 * @param position The position to use
	 */
	public TabPosition(double position){
		this.setPosition(position);
	}
	
	/**
	 * Get the {@link #position} of this {@link TabPosition}
	 * @return The position
	 */
	public double getPosition(){
		return position;
	}

	/**
	 * Set the {@link #position} of this {@link TabPosition}
	 * @param position The position
	 */
	public void setPosition(double position){
		this.position = position;
	}
	
}
