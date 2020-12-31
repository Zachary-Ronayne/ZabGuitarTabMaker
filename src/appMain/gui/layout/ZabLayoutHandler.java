package appMain.gui.layout;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BoxLayout;

/**
 * A class used to generate and set the layout of {@link Container} objects.<br>
 * This is recycled from a previous project.
 * @author zrona
 */
public class ZabLayoutHandler{
	
	/**
	 * Create a BoxLayout as vertical and associate it with to the given container
	 * @param target The {@link Container} to set the layout
	 */
	public static void createVerticalLayout(Container target){
		BoxLayout layout = new BoxLayout(target, BoxLayout.Y_AXIS);
		target.setLayout(layout);
	}

	/**
	 * Create a BoxLayout as horizontal and associate it with to the given container
	 * @param target The {@link Container} to set the layout
	 */
	public static void createHorizontalLayout(Container target){
		BoxLayout layout = new BoxLayout(target, BoxLayout.X_AXIS);
		target.setLayout(layout);
	}

	/**
	 * Create a GridLayout with the given rows and cols and associate it with to the given container
	 * @param target The {@link Container} to set the layout
	 * @param rows The number of rows
	 * @param cols The number of columns
	 */
	public static void createGridLayout(Container target, int rows, int cols){
		GridLayout layout = new GridLayout(rows, cols);
		target.setLayout(layout);
	}
}
