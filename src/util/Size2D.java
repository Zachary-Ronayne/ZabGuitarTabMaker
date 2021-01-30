package util;

/**
 * A utility class for holding a width and height in double precision
 * @author zrona
 */
public class Size2D{
	
	/** The width amount */
	private double width;
	/** The height amount */
	private double height;
	
	/**
	 * Create a new {@link Size2D} with the given values
	 * @param width See {@link #width}
	 * @param height See {@link #height}
	 */
	public Size2D(double width, double height){
		this.width = width;
		this.height = height;
	}
	
	/** @return See {@link #width} */
	public double getWidth(){
		return width;
	}
	
	/** @return See {@link #height} */
	public double getHeight(){
		return height;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, Size2D.class)) return false;
		Size2D s = (Size2D)obj;
		return super.equals(obj) ||
				this.getWidth() == s.getWidth() &&
				this.getHeight() == s.getHeight();
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("[Size2D: width:");
		sb.append(this.getWidth());
		sb.append(", height:");
		sb.append(this.getHeight());
		sb.append("]");
		return sb.toString();
	}
	
}
