package appMain.gui.editor.paint;

/**
 * An abstract class to assist with creating objects to help manage the code in {@link TabPainter}
 * @author zrona
 */
public abstract class TabPaintController{
	
	/**
	 * The {@link TabPainter} which uses this {@link TabPaintController}
	 */
	private TabPainter painter;
	
	/**
	 * Initialize The {@link TabPaintController}
	 * @param painter See {@link #painter}
	 */
	public TabPaintController(TabPainter painter){
		if(painter == null) throw new IllegalArgumentException("The TabPainter of a TabPaintSelector must not be null.");
		this.painter = painter;
	}
	
	/** @return See {@link #painter}  */
	public TabPainter getPainter(){
		return painter;
	}
	
}
