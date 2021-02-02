package util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.ArrayList;

/**
 * A class with utility methods for handling processing Gui related objects and graphics
 * @author zrona
 */
public class GuiUtils{
	
	/**
	 * Get a list of every {@link Component} contained within the given {@link Component}
	 * @param c The component
	 * @return The list, or an empty list if c is null
	 */
	public static ArrayList<Component> getAllComponents(Component c){
		ArrayList<Component> list = new ArrayList<Component>();
		if(c == null) return list;

		ArrayUtils.addWithoutDuplicate(list, c);
		
		if(!(c instanceof Container)){
			return list;
		}
		
		Container cont = (Container)c;
		
		Component[] comps = cont.getComponents();
		
		for(Component comp : comps){
			ArrayUtils.addWithoutDuplicate(list, comp);
			ArrayList<Component> newComps = getAllComponents(comp);
			ArrayUtils.addManyWithoutDuplicate(list, newComps);
		}
		
		return list;
	}
	
	/**
	 * Set the font size of the given {@link Component}, other font related fields stay the same
	 * @param c The {@link Component} to change the font
	 * @param size The new font size, if this value is negative, the font change fails
	 * @return true if the font was changed, false otherwise
	 */
	public static boolean setFontSize(Component c, int size){
		if(size < 0) return false;
		Font f = c.getFont();
		c.setFont(new Font(f.getFontName(), f.getStyle(), size));
		return true;
	}
	
	/**
	 * Find the bounding box of a string drawn at a location, with some slight margin of error
	 * @param str The string to find the size
	 * @param g The graphics object to use
	 * @param x The x coordinate to render the string in at the location a string would normally be drawn
	 * @param y The y coordinate to render the string in at the location a string would normally be drawn
	 * @return The bounding box, where x and y represent the upper left hand corner of the string's bounds, they do not represent the baseline position of a string. 
	 * 	This is the entire bounds of the string, including going below a above the baseline and normal top of the string, as well as the left and right. 
	 *  If a pixel is drawn as a part of the string, this bounding box will contain it. 
	 *  If g is null, this will return an empty rectangle
	 */
	public static Rectangle stringBounds(String str, Graphics2D g, double x, double y){
		if(g == null) return new Rectangle();
		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector gv = g.getFont().createGlyphVector(frc, str);
		
		return gv.getPixelBounds(null, (float)x, (float)y);
	}
	
	/**
	 * Find the width and height of a string, in pixel coordinates
	 * @param str The string to find the size
	 * @param g The graphics determining the string size
	 * @return The bounding box of the width and height
	 */
	public static Size2D stringBounds(String str, Graphics2D g){
		Rectangle baseBounds = stringBounds(str, g, 0, 0);
		double w = baseBounds.getWidth();
		double h = baseBounds.getHeight();
		return new Size2D(w, h);
	}
	
	/**
	 * Get the distance a rendered string moves from its baseline to the upper left hand corner of its bounds 
	 * @param str The string
	 * @param g The graphics object which would render the string
	 * @return The width and height which represent the offset to be added to the baseline position to get the upper left hand corner of the bounds
	 */
	public static Size2D stringBaselineOffset(String str, Graphics2D g){
		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector gv = g.getFont().createGlyphVector(frc, str);
		Rectangle bounds = gv.getPixelBounds(frc, 0, 0);
		return new Size2D(-bounds.getX(), -bounds.getY());
	}
	
	/** Cannot instantiate {@link GuiUtils} */
	private GuiUtils(){}
	
}
