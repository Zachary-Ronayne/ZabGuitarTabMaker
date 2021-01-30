package appMain.gui.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.Scanner;

import util.GuiUtils;
import util.Saveable;
import util.Size2D;

/**
 * A class that handles camera movement for drawing graphics. This allows a single object to be used to keep track of all movements of a camera, 
 * meaning translations and scaling on the x and or y axis.<br>
 * All methods that have the same name as those in a Graphics object do the same thing, but take double values as parameters.<br>
 * Pixel space refers to the location of rendered graphics on a screen or image, i.e. the location of the mouse, and the pixels as displayed. 
 * Camera space refers to the location of rendered objects. For example, if a rectangle has a bounds in camera coordinates, then zooming in or panning
 * the camera will not change those coordinates, however their corresponding pixel coordinates will change.<br>
 * This is based on recycled code from an older project.
 */
public class Camera implements Saveable{
	
	/** 
	 * The default zoom value, for zoom methods when no base is provided.
	 *  This value is raised to the power of a zoom factor, which is them multiplied to a value to zoom it in
	 */
	public static final double DEFAULT_ZOOM_BASE = 1.2;
	
	/** The x position of the camera, upper left hand corner */
	private double camX;
	/** The y position of the camera, upper left hand corner */
	private double camY;
	/** The width of the space that this camera takes up, in camera coordinates */
	private double camWidth;
	/** The height of the space that this camera takes up, in camera coordinates */
	private double camHeight;
	/** The amount that will be zoomed in on the x axis. Negative values zoom out, positive values zoom in. */
	private double xZoomFactor;
	/** The amount that will be zoomed in on the y axis. Negative values zoom out, positive values zoom in. */
	private double yZoomFactor;
	/** When zooming, this is the value put to the power of the zoom factor */
	private double zoomBase;
	
	/** The anchor x position used for panning */
	private double anchorX;
	/** The anchor y position used for panning */
	private double anchorY;
	/** The x position the camera was in when the camera was anchored */
	private double anchorCamX;
	/** The y position the camera was in when the camera was anchored */
	private double anchorCamY;
	/** true if the camera currently has an anchor point set, false otherwise */
	private boolean anchored;

	/** The minimum value that {@link #camX} can take */
	private double minX;
	/** The minimum value that {@link #camY} can take */
	private double minY;
	/** The maximum value that {@link #camX} can take */
	private double maxX;
	/** The maximum value that {@link #camY} can take */
	private double maxY;
	
	/** The graphics object that this camera should draw graphics to */
	private Graphics2D graphics;
	
	/** True if objects should only be drawn when they are in bounds of the camera, false otherwise */
	private boolean drawOnlyInBounds;
	
	/** String scale mode that means strings stay the same size when rendered, regardless of zoom level */
	public static final int STRING_SCALE_NONE = 0;
	/** String scale mode that means strings are zoomed based on the x axis, i.e. when the x axis zooms, the string changes size */
	public static final int STRING_SCALE_X_AXIS = 1;
	/** String scale mode that means strings are zoomed based on the y axis, i.e. when the y axis zooms, the string changes size */
	public static final int STRING_SCALE_Y_AXIS = 2;
	
	/** 
	 * Determines how to handle drawing strings when zooming in and out. 
	 * Use constants defined in this class. Defaults to {@link #STRING_SCALE_NONE} if an invalid mode is set
	 */
	private int stringScaleMode;
	
	/** String alignment for drawing strings by their baseline, in the same standard way as Grahpics.drawString */
	public static final int STRING_ALIGN_DEFAULT = 0;
	/** String alignment for drawing strings where the drawing coordinate represents the center of the string */
	public static final int STRING_ALIGN_CENTER = 1;
	/** String alignment for drawing strings where the drawing coordinate represents the minimum coordinate value of the string, i.e. left and top */
	public static final int STRING_ALIGN_MIN = 2;
	/** String alignment for drawing strings where the drawing coordinate represents the maximum coordinate value of the string, i.e. right and bottom */
	public static final int STRING_ALIGN_MAX = 3;
	
	/**
	 * The alignment type for the x axis when drawing strings.
	 * Use constants in this class to set. If this value is invalid, string alignment defaults to {@link #STRING_ALIGN_DEFAULT}
	 */
	private int stringXAlignment;
	/**
	 * The alignment type for the y axis when drawing strings.
	 * Use constants in this class to set. If this value is invalid, string alignment defaults to {@link #STRING_ALIGN_DEFAULT}
	 */
	private int stringYAlignment;
	
	/**
	 * Create a new camera with all default values and the given size
	 * @param camWidth The initial width of the camera
	 * @param camHeight The initial height of the camera
	 */
	public Camera(double camWidth, double camHeight){
		this.camWidth = camWidth;
		this.camHeight = camHeight;
		
		this.camX = 0;
		this.camY = 0;
		
		this.xZoomFactor = 0;
		this.yZoomFactor = 0;
		this.zoomBase = DEFAULT_ZOOM_BASE;
		
		this.anchored = false;
		this.anchorX = 0;
		this.anchorY = 0;
		
		this.minX = Integer.MIN_VALUE;
		this.minY = Integer.MIN_VALUE;
		this.maxX = Integer.MAX_VALUE;
		this.maxY = Integer.MAX_VALUE;
		
		this.graphics = null;
		
		this.drawOnlyInBounds = true;
		
		this.stringScaleMode = STRING_SCALE_NONE;
		this.stringXAlignment = STRING_ALIGN_DEFAULT;
		this.stringYAlignment = STRING_ALIGN_DEFAULT;
	}
	
	/** @return See {@link #anchored} */
	public boolean isAchored(){
		return anchored;
	}
	/** @return See {@link #anchorX} */
	public double getAnchorX(){
		return anchorX;
	}
	/** @return See {@link #anchorY} */
	public double getAnchorY(){
		return anchorY;
	}
	
	/**
	 * Set the anchor position of this camera, used for panning. When anchored, 
	 * all calls to pan the camera will be made relative to this anchor point. <br>
	 * The values should be in pixel coordinates, not camera coordinates
	 * A new anchor position is only set when the camera is not anchored
	 * @param anchorX the x pixel coordinate for the anchor
	 * @param anchorY the y pixel coordinate for the anchor
	 */
	public void setAnchor(double anchorX, double anchorY){
		if(!anchored){
			this.anchorX = anchorX;
			this.anchorY = anchorY;
			
			this.anchorCamX = this.camX;
			this.anchorCamY = this.camY;
			
			this.anchored = true;
		}
	}
	
	/** If an anchor position is set, the anchor is released */
	public void releaseAnchor(){
		this.anchored = false;
	}
	
	/**
	 * Based on the anchor position, pan the camera to the position given.<br>
	 * The position is in pixel coordinates, not camera coordinates.<br>
	 * This method does nothing if no anchor is been set
	 * @param panX the x pixel coordinate to pan to
	 * @param panY the y pixel coordinate to pan to
	 */
	public void pan(double panX, double panY){
		if(anchored){
			this.setX(this.anchorCamX + this.inverseZoomX(this.anchorX - panX));
			this.setY(this.anchorCamY + this.inverseZoomY(this.anchorY - panY));
		}
	}
	
	/**
	 * Set the given point as the center of the camera
	 * @param x the x pixel coordinate
	 * @param y the y pixel coordinate
	 */
	public void center(double x, double y){
		this.setX(x - this.inverseZoomX(this.getWidth()) * 0.5);
		this.setY(y - this.inverseZoomY(this.getHeight()) * 0.5);
	} 
	
	/**
	 * Zoom in on both axes at the specified location on the camera
	 * @param x the x pixel coordinate to zoom to
	 * @param y the y pixel coordinate to zoom to
	 * @param direction the amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 */
	public void zoomIn(double x, double y, double direction){
		this.zoomInX(x, direction);
		this.zoomInY(y, direction);
	}

	/**
	 * Zoom in on only the x axis at the specified location on the camera
	 * @param x The x pixel coordinate to zoom to
	 * @param direction The amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 */
	public void zoomInX(double x, double direction){
		// Zoom the value
		this.setX(this.zoomedValue(x, this.getWidth(), this.getX(), this.getXZoomFactor(), direction));

		// Update the zoom factor
		this.setXZoomFactor(this.getXZoomFactor() + direction);
	}
	/**
	 * Zoom in on only the y axis at the specified location on the camera
	 * @param y the y zoom position on the camera
	 * @param direction the amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 */
	public void zoomInY(double y, double direction){
		// Zoom the value
		this.setY(this.zoomedValue(y, this.getHeight(), this.getY(), this.getYZoomFactor(), direction));

		// Update the zoom factor
		this.setYZoomFactor(this.getYZoomFactor() + direction);
	}
	
	/**
	 * Zoom the given coordinate in on the x axis to a camera coordinate of this {@link Camera}
	 * @param x The coordinate to zoom
	 * @return The zoomed coordinate
	 */
	public double zoomX(double x){
		return this.zoomed(x, this.getXZoomFactor());
	}
	/**
	 * Zoom the given coordinate out on the x axis to a camera coordinate of this {@link Camera}
	 * @param x The coordinate to zoom
	 * @return The zoomed coordinate
	 */
	public double inverseZoomX(double x){
		return this.inverseZoomed(x, this.getXZoomFactor());
	}

	/**
	 * Zoom the given coordinate in on the y axis to a camera coordinate of this {@link Camera}
	 * @param y The coordinate to zoom
	 * @return The zoomed coordinate
	 */
	public double zoomY(double y){
		return this.zoomed(y, this.getYZoomFactor());
	}
	/**
	 * Zoom the given coordinate out on the y axis to a camera coordinate of this {@link Camera}
	 * @param y The coordinate to zoom
	 * @return The zoomed coordinate
	 */
	public double inverseZoomY(double y){
		return this.inverseZoomed(y, this.getYZoomFactor());
	}
	
	/**
	 * Get the pixel coordinate of a value on the x axis as it should be used for a graphics call.<br>
	 * Generally should only be used by the inside of this class.
	 * @param value the value to find the coordinate of
	 * @return the pixel coordinate
	 */
	public int drawX(double value){
		return drawValue(this.zoomX(value - this.camX));
	}
	/**
	 * Get the pixel coordinate of a value on the y axis as it should be used for a graphics call.<br>
	 * Generally should only be used by the inside of this class.
	 * @param value the value to find the coordinate of
	 * @return the pixel coordinate
	 */
	public int drawY(double value){
		return drawValue(this.zoomY(value - this.camY));
	}
	/**
	 * Get the pixel width of a value that will be drawn to the x axis<br>
	 * @param value the value to get the pixel width of
	 * @return the pixel width
	 */
	public int drawW(double value){
		return drawValue(this.zoomX(value));
	}
	/**
	 * Get the pixel height of a value that will be drawn to the y axis<br>
	 * @param value the value to get the pixel height of
	 * @return the pixel height
	 */
	public int drawH(double value){
		return drawValue(this.zoomY(value));
	}
	
	/**
	 * Take an x coordinate in pixel coordinates, and convert it to camera coordinates
	 * @param value The pixel coordinate
	 * @return The x coordinate of the camera
	 */
	public double toCamX(double value){
		return this.inverseZoomX(value) + this.camX;
	}
	/**
	 * Take an x camera coordinate, and convert it to an x coordinate in pixel coordinates
	 * @param value The x camera coordinate
	 * @return The pixel coordinate
	 */
	public double toPixelX(double value){
		return this.zoomX(value - this.camX);
	}

	/**
	 * Take a y coordinate in pixel space, and convert it to camera space
	 * @param value the pixel coordinate
	 * @return the y coordinate of the camera
	 */
	public double toCamY(double value){
		return this.inverseZoomY(value) + this.camY;
	}
	/**
	 * Take a y camera coordinate, and convert it to an x coordinate in pixel space
	 * @param value The y camera coordinate
	 * @return The pixel space coordinate
	 */
	public double toPixelY(double value){
		return this.zoomY(value - this.camY);
	}
	
	/**
	 * Convert the camera bounds of the given rectangle into pixel bounds
	 * @param r The rectangular bounds
	 * @return The bounds in pixel coordinates
	 */
	public Rectangle2D camToPixelBounds(Rectangle2D r){
		return this.camToPixelBounds(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}
	
	/**
	 * Convert the camera bounds of the given rectangle into pixel bounds
	 * @param x The upper left hand x coordinate of the bounds
	 * @param y The upper left hand y coordinate of the bounds
	 * @param w The width of the bounds
	 * @param h The height of the bounds
	 * @return The bounds in pixel coordinates
	 */
	public Rectangle2D camToPixelBounds(double x, double y, double w, double h){
		return new Rectangle2D.Double(
				this.toPixelX(x), this.toPixelY(y),
				this.zoomX(w), this.zoomY(h)
			);
	}
	
	/**
	 * Convert the pixel bounds of the given rectangle into camera bounds
	 * @param r The rectangular bounds
	 * @return The bounds in camera coordinates
	 */
	public Rectangle2D pixelToCamBounds(Rectangle2D r){
		return this.pixelToCamBounds(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	/**
	 * Convert the pixel bounds of the given rectangle into camera bounds
	 * @param x The upper left hand x coordinate of the bounds
	 * @param y The upper left hand y coordinate of the bounds
	 * @param w The width of the bounds
	 * @param h The height of the bounds
	 * @return The bounds in camera coordinates
	 */
	public Rectangle2D pixelToCamBounds(double x, double y, double w, double h){
		return new Rectangle2D.Double(
				this.toCamX(x), this.toCamY(y),
				this.inverseZoomX(w), this.inverseZoomY(h)
			);
	}
	
	/**
	 * See {@link Graphics2D#fillRect(int, int, int, int)}.<br>
	 * All values in parameters must be in camera coordinates
	 * @param r The rectangular bounds of the rectangle to draw
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean fillRect(Rectangle2D r){
		return this.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}
	/**
	 * See {@link Graphics2D#fillRect(int, int, int, int)}.<br>
	 * All values in parameters must be in camera coordinates
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean fillRect(double x, double y, double width, double height){
		if(!inBounds(x, y, width, height)) return false;
		this.getGraphics().fillRect(this.drawX(x), this.drawY(y), this.drawW(width), this.drawH(height));
		return true;
	}
	
	/**
	 * Draw a rectangle that scales with the bounds of a string.
	 * All values in parameters must be in camera coordinates
	 * @param r The rectangular bounds, in camera coordinates, of the rectangle to draw, 
	 * which will be scaled to pixel coordinates when drawn, based on {@link #stringScaleMode}
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean fillStringRect(Rectangle2D r){
		return this.fillStringRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}
	/**
	 * All values in parameters must be in camera coordinates.<br>
	 * The given rectangular bounds, in camera coordinates, of the rectangle to draw, 
	 * will be scaled to pixel coordinates when drawn, based on {@link #stringScaleMode}
	 * @param x The x position upper left hand corner of the rectangle
	 * @param y The y position upper left hand corner of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean fillStringRect(double x, double y, double width, double height){
		Rectangle2D r = this.stringCamToPixelBounds(x, y, width, height);
		
		// Find final bounds moving from pixel center to pixel upper left hand corner
		if(!inPixelBounds(r)) return false;
		this.getGraphics().fillRect(
				drawValue(r.getX()), drawValue(r.getY()),
				drawValue(r.getWidth()), drawValue(r.getHeight())
			);
		return true;
	}
	
	/**
	 * See {@link Graphics2D#fillOval(int, int, int, int)}.<br>
	 * All values in parameters must be in camera coordinates
	 * @param r The rectangular bounds containing the oval to draw
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean fillOval(Rectangle2D r){
		return this.fillOval(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}
	/**
	 * See {@link Graphics2D#fillOval(int, int, int, int)}.<br>
	 * All values in parameters must be in camera coordinates
	 * @param e The oval bounds containing the oval to draw
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean fillOval(Ellipse2D e){
		return this.fillOval(e.getX(), e.getY(), e.getWidth(), e.getHeight());
	}
	/**
	 * See {@link Graphics2D#fillOval(int, int, int, int)}.<br>
	 * All values in parameters must be in camera coordinates
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean fillOval(double x, double y, double width, double height){
		if(!inBounds(x, y, width, height)) return false;
		this.getGraphics().fillOval(this.drawX(x), this.drawY(y), this.drawW(width), this.drawH(height));
		return true;
	}

	/**
	 * See {@link Graphics2D#drawLine(int, int, int, int)}.<br>
	 * All values in parameters must be in camera coordinates
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean drawLine(double x1, double y1, double x2, double y2){
		// Extra little bit of width and height to account for horizontal and vertical lines
		if(!this.inBounds(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2) + 0.000001, Math.abs(y1 - y2) + 0.000001)) return false;
		this.getGraphics().drawLine(this.drawX(x1), this.drawY(y1), this.drawX(x2), this.drawY(y2));
		return true;
	}
	
	/**
	 * Draw a string based on the alignment and scaling settings of this {@link Camera}.<br>
	 * All values in parameters must be in camera coordinates
	 * See {@link Graphics2D#drawString(String, int, int)}
	 * @return The font metrics used to draw the string if the object was drawn, null otherwise
	 */
	public FontMetrics drawString(String s, double x, double y){
		return this.drawString(s, x, y, -1);
	}
	/**
	 * Draw a string based on the alignment and scaling settings of this {@link Camera}.<br>
	 * All values in parameters must be in camera coordinates
	 * @param s The string to draw
	 * @param x The x camera coordinate
	 * @param y The y camera coordinate
	 * @param fontSize The font size to use for the string, use a negative value to base the height on the current graphics font size
	 * @return The font metrics used to draw the string if the object was drawn, null otherwise
	 */
	public FontMetrics drawString(String s, double x, double y, double fontSize){
		Graphics2D g = this.getGraphics();
		Font f = g.getFont();
		
		// If the height wasn't specified, use the font size
		if(fontSize < 0) fontSize = f.getSize();
		
		// Find the exact bounds for the string and determine if they will be rendered
		Rectangle2D b = this.stringPixelBounds(s, x, y);
		boolean success = this.inPixelBounds(b);

		// If the string should be rendered, draw it
		if(success){
			Point2D p = this.stringPixelDrawPos(s, x, y);
			this.updateFontSize(fontSize);
			g.drawString(s, drawValue(p.getX()), drawValue(p.getY()));
		}
		
		FontMetrics metrics = g.getFontMetrics();
		// Set the old font
		graphics.setFont(f);
		// Return either the metrics or null depending on success
		return success ? metrics : null;
	}
	
	/**
	 * Draw an image at the specified location and size.<br>
	 * All values in parameters must be in camera coordinates
	 * @param img The image to draw
	 * @param x The x coordinate to draw at
	 * @param y The y coordinate to draw at
	 * @param w The width at which to draw the image
	 * @param h The height at which to draw the image
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean drawImage(BufferedImage img, double x, double y, double w, double h){
		if(!inBounds(x, y, w, h)) return false;
		this.getGraphics().drawImage(img, this.drawX(x), this.drawY(y), this.drawW(w), this.drawH(h), null);
		return true;
	}
	/**
	 * Draw an image at the specified location, size is determined by the image width and height.<br>
	 * All values in parameters must be in camera coordinates
	 * @param img The image to draw
	 * @param x The x coordinate to draw at
	 * @param y The y coordinate to draw at
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean drawImage(BufferedImage img, double x, double y){
		return this.drawImage(img, x, y, img.getWidth(), img.getHeight());
	}
	
	/**
	 * Determine if the given rectangular bounds, in camera coordinates, are within the size of the camera.<br>
	 * Essentially, determines if graphics the rectangular bounds will be rendered on the graphics object
	 * @param x The x coordinate of the bounds
	 * @param y The y coordinate of the bounds
	 * @param width The width of the bounds
	 * @param height The height of the bounds
	 * @return true if drawing only in bounds is disabled, or if the given bounds intersect the camera bounds, false otherwise
	 */
	public boolean inBounds(double x, double y, double width, double height){
		return this.inBounds(new Rectangle2D.Double(x, y, width, height));
	}
	/**
	 * Determine if the given rectangular bounds, in camera coordinates, are within the size of the camera.<br>
	 * Essentially, determines if graphics the rectangular bounds will be rendered on the graphics object
	 * @param r The bounds of the rectangle
	 * @return true if drawing only in bounds is disabled, or if the given bounds intersect the camera bounds, false otherwise
	 */
	public boolean inBounds(Rectangle2D r){
		return this.inPixelBounds(this.camToPixelBounds(r));
	}
	
	/**
	 * Determine if the given pixel rectangular bounds are within the size of the camera.<br>
	 * Essentially, determines if graphics the rectangular bounds will be rendered on the graphics object
	 * @param x The x coordinate of the bounds
	 * @param y The y coordinate of the bounds
	 * @param width The width of the bounds
	 * @param height The height of the bounds
	 * @return true if drawing only in bounds is disabled, or if the given bounds intersect the camera bounds, false otherwise
	 */
	public boolean inPixelBounds(double x, double y, double width, double height){
		if(!this.isDrawOnlyInBounds()) return true;
		Rectangle2D.Double camBounds = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
		return camBounds.intersects(x, y, width, height);
	}
	/**
	 * Determine if the given pixel rectangular bounds are within the size of the camera.<br>
	 * Essentially, determines if graphics the rectangular bounds will be rendered on the graphics object
	 * @param r The bounds of the rectangle
	 * @return true if drawing only in bounds is disabled, or if the given bounds intersect the camera bounds, false otherwise
	 */
	public boolean inPixelBounds(Rectangle2D r){
		return this.inPixelBounds(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}
	
	/**
	 * Set the font size of {@link #graphics} to the pixel coordinate version of the given height in camera coordinates
	 * @param height The height to set in camera coordinates
	 * @return The old font, must set the font of {@link #graphics} back to this returned value after using the updated font size
	 */
	public Font updateFontSize(double height){
		Graphics2D g = this.getGraphics();
		Font f = g.getFont();
		
		height = this.stringZoom(height);
		g.setFont(new Font(f.getName(), f.getStyle(), drawValue(height)));
		return f;
	}
	
	/**
	 * Set the font size of {@link #graphics} in camera coordinates, treating the height to set as the current font size 
	 * @return The old font, must set the font of {@link #graphics} back to this returned value after using the updated font size
	 */
	public Font updateFontSize(){
		return this.updateFontSize(this.getGraphics().getFont().getSize());
	}
	
	/**
	 * Find the size of the given string in pixel space of the currently set font size of {@link #graphics}
	 * @param str The string to find the size
	 * @return The size, in pixel coordinates, of the string 
	 */
	public Size2D stringPixelSize(String str){
		Graphics2D g = this.getGraphics();
		Font f = this.updateFontSize();
		
		Size2D d = GuiUtils.stringBounds(str, g);
		g.setFont(f);
		return d;
	}

	/**
	 * Find the size of the offset from the baseline of the given string to its upper left hand corner bounds 
	 * in pixel space of the currently set font size in {@link #graphics}
	 * @param str The string to find the size
	 * @return The offset size, in pixel coordinates, of the string 
	 */
	public Size2D stringPixelOffset(String str){
		Graphics2D g = this.getGraphics();
		Font f = this.updateFontSize();
		Size2D d = GuiUtils.stringBaselineOffset(str, g);
		g.setFont(f);
		
		return new Size2D(d.getWidth(), d.getHeight());
	}
	
	/**
	 * Find the pixel coordinates of the upper left hand corner of the given string based on the alignment settings of this {@link Camera}
	 * @param str The string to find the point
	 * @param x The x coordinate where the string would be drawn, in camera coordinates
	 * @param y The y coordinate where the string would be drawn, in camera coordinates
	 * @return The point, in pixel coordinates, of the bounding box of the string
	 */
	public Point2D stringPixelBoundsPos(String str, double x, double y){
		Point2D p = this.stringPixelDrawPos(str, x, y);
		
		Size2D off = this.stringPixelOffset(str);
		double px = p.getX() - off.getWidth();
		double py = p.getY() - off.getHeight();
		
		return new Point2D.Double(px, py);
	}
	
	/**
	 * Find the pixel coordinates point where the given string should be drawn to align with the given coordinates based on the alignment settings of this {@link Camera}
	 * @param str The string to find the point
	 * @param x The x coordinate where the string would be drawn, in camera coordinates
	 * @param y The y coordinate where the string would be drawn, in camera coordinates
	 * @return The point, in pixel coordinates, to draw the string 
	 */
	public Point2D stringPixelDrawPos(String str, double x, double y){
		double px = this.toPixelX(x);
		double py = this.toPixelY(y);
		
		// Find the amount which must be aligned
		Size2D align = this.alignPixelString(str);
		
		return new Point2D.Double(px + align.getWidth(), py + align.getHeight());
	}
	
	/**
	 * Get the full rectangular bounds of the given string in pixel coordinates
	 * @param str The string to find the bounds
	 * @param x The x coordinate, in camera coordinates
	 * @param y The y coordinate, in camera coordinates
	 * @return The rectangle, in pixel coordinates, which will contain the string. This is not the location of the string, but the bounding box the pixels of the string
	 */
	public Rectangle2D stringPixelBounds(String str, double x, double y){
		Size2D d = this.stringPixelSize(str);
		Point2D p = this.stringPixelBoundsPos(str, x, y);
		
		return new Rectangle2D.Double(p.getX(), p.getY(), d.getWidth(), d.getHeight());
	}

	/**
	 * Find the bounding box of a particular string, in camera coordinates, aligned to the current alignment of this {@link Camera}
	 * @param str The string to find the size
	 * @param g The graphics determining the string size, or null to use {@link #graphics}
	 * @param x The x coordinate to render the string in camera coordinates
	 * @param y The y coordinate to render the string in camera coordinates
	 * @return The bounding box, where x and y represent the upper left hand corner of the string, they do not represent the baseline position of a string. 
	 * 	This is the entire bounds of the string, including going below and above the baseline and normal top of the string. 
	 * 	This bounds is in camera coordinates, not pixel coordinates
	 */
	public Rectangle2D stringCamBounds(String str, double x, double y){
		Rectangle2D r = this.stringPixelBounds(str, x, y);
		return this.stringPixelToCamBounds(r);
	}
	
	/**
	 * Given the rectangular bounds of a string in camera coordinates, convert those coordinates to pixel coordinates. 
	 * This method should be used when the bounds needed to represent a string must be converted. 
	 * The converted bounds will be calculated based on the point defined by {@link #stringXAlignment} and {@link #stringYAlignment}. 
	 * Meaning it will use either a corner, the center of an edge, or the center of the rectangle.
	 * If either alignment setting is set to {@link #STRING_ALIGN_DEFAULT}, then it will treat that alignment as {@link #STRING_ALIGN_MIN} for the 
	 * point to calculate off of. This method cannot account for the baseline position of a string, as with only the bounds given, it 
	 * is impossible to know the exact offset of that particular string.
	 * @param r The rectangular bounds
	 * @return The new bounds in pixel coordinates
	 */
	public Rectangle2D stringCamToPixelBounds(Rectangle2D r){
		return this.stringCamToPixelBounds(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}
	/**
	 * Given the rectangular bounds of a string in camera coordinates, convert those coordinates to pixel coordinates. 
	 * This method should be used when the bounds needed to represent a string must be converted. 
	 * The converted bounds will be calculated based on the point defined by {@link #stringXAlignment} and {@link #stringYAlignment}. 
	 * Meaning it will use either a corner, the center of an edge, or the center of the rectangle.
	 * If either alignment setting is set to {@link #STRING_ALIGN_DEFAULT}, then it will treat that alignment as {@link #STRING_ALIGN_MIN} for the 
	 * point to calculate off of. This method cannot account for the baseline position of a string, as with only the bounds given, it 
	 * is impossible to know the exact offset of that particular string.
	 * @param x The x coordinate of the upper left hand corner of the rectangle
	 * @param y The y coordinate of the upper left hand corner of the rectangle
	 * @param width The width of the bounds
	 * @param height The height of the bounds
	 * @return The new bounds in pixel coordinates
	 */
	public Rectangle2D stringCamToPixelBounds(double x, double y, double width, double height){
		// Find aligned before scaling, this is to ensure relative positions stay the same after scaling
		double cx = x - this.alignStringX(width, 0);
		double cy = y - this.alignStringY(height, 0);
		
		// Scale center to pixel coordinates
		double pcx = this.toPixelX(cx);
		double pcy = this.toPixelY(cy);
		
		// Find scaled width and height
		double sw = this.stringZoom(width);
		double sh = this.stringZoom(height);
		
		// Find final bounds moving from pixel realigned to pixel upper left hand corner
		return new Rectangle2D.Double(
				pcx + this.alignStringX(sw, 0),
				pcy + this.alignStringY(sh, 0), sw, sh);
	}
	
	/**
	 * Given the rectangular bounds of a string in pixel coordinates, convert those coordinates to camera coordinates. 
	 * This method should be used when the bounds needed to represent a string must be converted. 
	 * The converted bounds will be calculated based on the point defined by {@link #stringXAlignment} and {@link #stringYAlignment}. 
	 * Meaning it will use either a corner, the center of an edge, or the center of the rectangle.
	 * If either alignment setting is set to {@link #STRING_ALIGN_DEFAULT}, then it will treat that alignment as {@link #STRING_ALIGN_MIN} for the 
	 * point to calculate off of. This method cannot account for the baseline position of a string, as with only the bounds given, it 
	 * is impossible to know the exact offset of that particular string.
	 * @param r The rectangular bounds
	 * @return The new bounds in camera coordinates
	 */
	public Rectangle2D stringPixelToCamBounds(Rectangle2D r){
		return this.stringPixelToCamBounds(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}
	
	/**
	 * Given the rectangular bounds of a string in pixel coordinates, convert those coordinates to camera coordinates. 
	 * This method should be used when the bounds needed to represent a string must be converted. 
	 * The converted bounds will be calculated based on the point defined by {@link #stringXAlignment} and {@link #stringYAlignment}. 
	 * Meaning it will use either a corner, the center of an edge, or the center of the rectangle.
	 * If either alignment setting is set to {@link #STRING_ALIGN_DEFAULT}, then it will treat that alignment as {@link #STRING_ALIGN_MIN} for the 
	 * point to calculate off of. This method cannot account for the baseline position of a string, as with only the bounds given, it 
	 * is impossible to know the exact offset of that particular string.
	 * @param x The x coordinate of the upper left hand corner of the rectangle
	 * @param y The y coordinate of the upper left hand corner of the rectangle
	 * @param width The width of the bounds
	 * @param height The height of the bounds
	 * @return The new bounds in camera coordinates
	 */
	public Rectangle2D stringPixelToCamBounds(double x, double y, double width, double height){
		// Find center before scaling, this is to ensure relative positions stay the same after scaling
		double px = x - this.alignStringX(width, 0);
		double py = y - this.alignStringY(height, 0);
				
		// Scale center to pixel coordinates
		double cx = this.toCamX(px);
		double cy = this.toCamY(py);
				
		// Find scaled width and height
		double sw = this.stringInverseZoom(width);
		double sh = this.stringInverseZoom(height);
		
		// Find final bounds, subtracting new offset
		return new Rectangle2D.Double(
				cx + this.alignStringX(sw, 0),
				cy + this.alignStringY(sh, 0), sw, sh);
	}

	/**
	 * Given a size of a dimension on a string in camera coordinates, modify it so that the final height, when drawn, will be the same after the camera zooms,
	 * 	or will scale with the camera, depending on {@link #stringScaleMode}
	 * @param size The size value
	 * @return The modified value in pixel coordinates
	 */
	public double stringZoom(double size){
		switch(this.getStringScaleMode()){
			default:
			case STRING_SCALE_NONE: return size;
			case STRING_SCALE_X_AXIS: return this.zoomX(size);
			case STRING_SCALE_Y_AXIS: return this.zoomY(size);
		}
	}
	/**
	 * Given a size of a dimension on a string in pixel coordinates, modify it so that the final height, when drawn, will be the same after the camera zooms,
	 * 	or will scale with the camera, depending on {@link #stringScaleXAxis}
	 * @param size The size value
	 * @return The modified value in camera coordinates
	 */
	public double stringInverseZoom(double size){
		switch(this.getStringScaleMode()){
			default:
			case STRING_SCALE_NONE: return size;
			case STRING_SCALE_X_AXIS: return this.inverseZoomX(size);
			case STRING_SCALE_Y_AXIS: return this.inverseZoomY(size);
		}
	}
	
	/**
	 * Get the offset which must be used to align the string based on the current alignment value set for the x and y axes
	 * @param str the string to find the alignment
	 * @return The offset add to the baseline render position to use for aligning the y coordinate
	 */
	public Size2D alignPixelString(String str){
		Size2D d = this.stringPixelSize(str);
		double dw = d.getWidth();
		double dh = d.getHeight();
		Size2D off = this.stringPixelOffset(str);
		double ow = off.getWidth();
		double oh = off.getHeight();

		double w = this.alignStringX(dw, ow);
		double h = this.alignStringY(dh, oh);
		
		return new Size2D(w, h);
	}

	/**
	 * Get the offset which must be used to align the string based on the current alignment value set for the x and y axes
	 * @param str the string to find the alignment
	 * @return The offset add to the baseline render position to use for aligning the y coordinate
	 */
	public Size2D alignCameraString(String str){
		Graphics2D g = this.getGraphics();
		Size2D d = GuiUtils.stringBounds(str, g);
		double dw = d.getWidth();
		double dh = d.getHeight();
		Size2D off = GuiUtils.stringBaselineOffset(str, g);
		double ow = off.getWidth();
		double oh = off.getHeight();
		
		double w = this.alignStringX(dw, ow);
		double h = this.alignStringY(dh, oh);
		
		return new Size2D(w, h);
	}
	
	/**
	 * Utility method for string alignment. Given a width of a string and an offset width for that string, 
	 * find an alignment value to add to an x coordinate to align that position based on this {@link Camera} 
	 * @param bw The width of the string
	 * @param ow The offset width of the string
	 * @return The amount to add
	 */
	public double alignStringX(double bw, double ow){
		return alignString(this.getStringXAlignment(), bw, ow);
	}
	/**
	 * Utility method for string alignment. Given a height of a string and an offset height for that string, 
	 * find an alignment value to add to an y coordinate to align that position based on this {@link Camera} 
	 * @param bh The height of the string
	 * @param oh The offset height of the string
	 * @return The amount to add
	 */
	public double alignStringY(double bh, double oh){
		return alignString(this.getStringYAlignment(), bh, oh);
	}
	/**
	 * Utility method for string alignment. Given a size of a string and an offset size for that string, 
	 * find an alignment value to add to a coordinate to align that position based on this {@link Camera} 
	 * @param align The alignment setting to use
	 * @param b The size of the string
	 * @param o The offset size of the string
	 * @return The amount to add
	 */
	public static double alignString(int align, double b, double o){
		switch(align){
			default:
			case STRING_ALIGN_DEFAULT: return 0;
			case STRING_ALIGN_CENTER: return o - b * 0.5;
			case STRING_ALIGN_MIN: return o;
			case STRING_ALIGN_MAX: return o - b;
		}
	}
	
	/** @return See {@link #camX} */
	public double getX(){
		return this.camX;
	}
	/** @param x See {@link #camX} */
	public void setX(double x){
		this.camX = Math.max(this.minX, Math.min(this.maxX, x));
	}
	/**
	 * Add the specified amount to the x position of the camera
	 * @param x The amount to add
	 */
	public void addX(double x){
		this.setX(this.getX() + x);
	}
	
	/** @return See {@link #camY} */
	public double getY(){
		return this.camY;
	}
	/** @param y See {@link #camY} */
	public void setY(double y){
		this.camY = Math.max(this.minY, Math.min(this.maxY, y));
	}
	/**
	 * Add the specified amount to the y position of the camera
	 * @param y The amount to add
	 */
	public void addY(double y){
		this.setY(this.getY() + y);
	}

	/** @return See {@link #camWidth} */
	public double getWidth(){
		return this.camWidth;
	}
	/** @param camWidth See {@link #camWidth} */
	public void setWidth(double camWidth){
		this.camWidth = camWidth;
	}
	
	/** @return See {@link #camHeight} */
	public double getHeight(){
		return this.camHeight;
	}
	/** @param camHeight See {@link #camHeight} */
	public void setHeight(double camHeight){
		this.camHeight = camHeight;
	}

	/** @return See {@link #xZoomFactor} */
	public double getXZoomFactor(){
		return this.xZoomFactor;
	}
	/** @param xZoomFactor See {@link #xZoomFactor} */
	public void setXZoomFactor(double xZoomFactor){
		this.xZoomFactor = xZoomFactor;
	}

	/** @return See {@link #yZoomFactor} */
	public double getYZoomFactor(){
		return this.yZoomFactor;
	}
	/** @param yZoomFactor See {@link #yZoomFactor} */
	public void setYZoomFactor(double yZoomFactor){
		this.yZoomFactor = yZoomFactor;
	}
	
	/** @return See {@link #zoomBase} */
	public double getZoomBase(){
		return this.zoomBase;
	}
	/** @param zoomBase See {@link #zoomBase} */
	public void setZoomBase(double zoomBase){
		this.zoomBase = zoomBase;
	}

	/** @return See {@link #minX} */
	public double getMinX(){
		return this.minX;
	}
	/** @param minX See {@link #minX} */
	public void setMinX(double minX){
		this.minX = minX;
	}

	/** @return See {@link #minY} */
	public double getMinY(){
		return this.minY;
	}
	/** @param minY See {@link #minY} */
	public void setMinY(double minY){
		this.minY = minY;
	}

	/** @return See {@link #maxX} */
	public double getMaxX(){
		return this.maxX;
	}
	/** @param maxX See {@link #maxX} */
	public void setMaxX(double maxX){
		this.maxX = maxX;
	}

	/** @return See {@link #maxY} */
	public double getMaxY(){
		return this.maxY;
	}
	/** @param maxY See {@link #maxY} */
	public void setMaxY(double maxY){
		this.maxY = maxY;
	}

	/** @return See {@link #graphics} */
	public Graphics2D getGraphics(){
		return this.graphics;
	}
	/** @param graphics See {@link #graphics} */
	public void setGraphics(Graphics2D graphics){
		this.graphics = graphics;
	}
	
	/** @return See {@link #drawOnlyInBounds} */
	public boolean isDrawOnlyInBounds(){
		return this.drawOnlyInBounds;
	}
	/**
	 * True if objects should only be drawn when they are in bounds of the camera, false otherwise
	 * @param drawOnlyInBounds see {@link #drawOnlyInBounds}
	 */
	public void setDrawOnlyInBounds(boolean drawOnlyInBounds){
		this.drawOnlyInBounds = drawOnlyInBounds;
	}

	/** @return See {@link #stringScaleMode} */
	public int getStringScaleMode(){
		return this.stringScaleMode;
	}
	/** @param stringScaleXAxis See {@link #stringScaleMode} */
	public void setStringScaleMode(int stringScaleMode){
		this.stringScaleMode = stringScaleMode;
	}
	
	/** @return See {@link #stringXAlignment} */
	public int getStringXAlignment(){
		return this.stringXAlignment;
	}
	/** @param stringAlignment {@link #stringXAlignment} */
	public void setStringXAlignment(int stringXAlignment){
		this.stringXAlignment = stringXAlignment;
	}
	
	/** @return See {@link #stringYAlignment} */
	public int getStringYAlignment(){
		return this.stringYAlignment;
	}
	/** @param stringAlignment {@link #stringYAlignment} */
	public void setStringYAlignment(int stringYAlignment){
		this.stringYAlignment = stringYAlignment;
	}
	
	/**
	 * Get the zoomed value of the given value based on the given zoom factor, with this camera's zoom base
	 * @param value the value to zoom
	 * @param factor the zoom factor
	 * @return the zoomed value
	 */
	public double zoomed(double value, double factor){
		return zoom(value, this.getZoomBase(), factor);
	}
	/**
	 * Get the zoomed value of the given value based on the given zoom factor, with this camera's zoom base
	 * where the zoom factor is inverted before it's multiplied by value
	 * @param value the value to zoom
	 * @param factor the zoom factor
	 * @return the zoomed value
	 */
	public double inverseZoomed(double value, double factor){
		return inverseZoom(value, this.getZoomBase(), factor);
	}
	/**
	 * Determine the value for zooming in on one axis at the specified location on a camera with the default zoom base
	 * @param zoomPos The position to zoom into
	 * @param camSize The size of the camera zooming in to
	 * @param camPos The zoom position on the camera with zooming
	 * @param zoomFactor The original amount zoomed in before a new zoom is applied
	 * @param direction The amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 */
	public double zoomedValue(double zoomPos, double camSize, double camPos, double zoomFactor, double direction){
		return zoomValue(zoomPos, camSize, camPos, zoomFactor, this.getZoomBase(), direction);
	}
	
	/**
	 * Get the zoomed value of the given value based on the given zoom factor
	 * @param value the value to zoom
	 * @param factor the zoom factor
	 * @param base The base value of the zoom exponential
	 * @return the zoomed value
	 */
	public static double zoom(double value, double base, double factor){
		return value * Math.pow(base, factor);
	}
	/**
	 * Get the zoomed value of the given value based on the given zoom factor with the default zoom base
	 * @param value the value to zoom
	 * @param base The base value of the zoom exponential
	 * @param factor the zoom factor
	 * @return the zoomed value
	 */
	public static double zoom(double value, double factor){
		return zoom(value, DEFAULT_ZOOM_BASE, factor);
	}
	
	/**
	 * Get the zoomed value of the given value based on the given zoom factor, 
	 * where the zoom factor is inverted before it's multiplied by value
	 * @param value the value to zoom
	 * @param base The base value of the zoom exponential
	 * @param factor the zoom factor
	 * @return the zoomed value
	 */
	public static double inverseZoom(double value, double base, double factor){
		return zoom(value, base, -factor);
	}
	/**
	 * Get the zoomed value of the given value based on the given zoom factor, with the default zoom base, 
	 * where the zoom factor is inverted before it's multiplied by value
	 * @param value the value to zoom
	 * @param factor the zoom factor
	 * @return the zoomed value
	 */
	public static double inverseZoom(double value, double factor){
		return inverseZoom(value, DEFAULT_ZOOM_BASE, factor);
	}
	
	/**
	 * Determine the value for zooming in on one axis at the specified location on a camera
	 * @param zoomPos The position to zoom into
	 * @param camSize The size of the camera zooming in to
	 * @param camPos The zoom position on the camera with zooming
	 * @param zoomFactor The original amount zoomed in before a new zoom is applied
	 * @param base The base value of the zoom exponential
	 * @param direction The amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 * @return The zoomed value
	 */
	public static double zoomValue(double zoomPos, double camSize, double camPos, double zoomFactor, double base, double direction){
		// Save the old value of the zoomed camera before the zoom
		// Inverse is used because width and height should change in opposite directions from the zoomFactors
		double oldSize = inverseZoom(camSize, base, zoomFactor);
		
		// Increase the zoom factor
		zoomFactor += direction;
		
		// Adjust the camera position based on where the zoom occurred
		
		// Find the new size after the zoom
		double newSize = inverseZoom(camSize, base, zoomFactor);
		
		// Find the difference in size in distance changed
		double dist = (oldSize - newSize);
		
		/*
		 * Return the camera position based on the distance moved in the zoom, 
		 *  and the ratio of the positions of the zoom position
		 */
		return camPos + dist * (zoomPos / camSize);
	}
	
	/**
	 * Determine the value for zooming in on one axis at the specified location on a camera with the default zoom base
	 * @param zoomPos The position to zoom into
	 * @param camSize The size of the camera zooming in to
	 * @param camPos The zoom position on the camera with zooming
	 * @param zoomFactor The original amount zoomed in before a new zoom is applied
	 * @param direction The amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 */
	public static double zoomValue(double zoomPos, double camSize, double camPos, double zoomFactor, double direction){
		return zoomValue(zoomPos, camSize, camPos, zoomFactor, DEFAULT_ZOOM_BASE, direction);
	}
	
	/**
	 * Convert a floating point value to the nearest integer pixel
	 * @param value The value to round
	 * @return The rounded value
	 */
	public static int drawValue(double value){
		return (int)Math.round(value);
	}

	/***/
	@Override
	public boolean load(Scanner read){
		Double[] load = Saveable.loadDoubles(read, 11);
		if(load == null) return false;
		
		Boolean loadBool = Saveable.loadBool(read);
		if(loadBool == null) return false;
		
		this.setX(load[0]);
		this.setY(load[1]);
		this.setWidth(load[2]);
		this.setHeight(load[3]);
		this.setXZoomFactor(load[4]);
		this.setYZoomFactor(load[5]);
		this.setZoomBase(load[6]);
		this.setMinX(load[7]);
		this.setMinY(load[8]);
		this.setMaxX(load[9]);
		this.setMaxY(load[10]);
		this.setDrawOnlyInBounds(loadBool);
		
		Integer[] loadInt = Saveable.loadInts(read, 3);
		if(loadInt == null) return false;
		
		this.setStringScaleMode(loadInt[0]);
		this.setStringXAlignment(loadInt[1]);
		this.setStringYAlignment(loadInt[2]);
		
		return true;
	}
	
	/***/
	@Override
	public boolean save(PrintWriter write){
		Saveable.saveToStrings(
				write, new Object[]{this.getX(), this.getY(), this.getWidth(), this.getHeight(),
				                    this.getXZoomFactor(), this.getYZoomFactor(), this.getZoomBase(), 
				                    this.getMinX(), this.getMinY(), this.getMaxX(), this.getMaxY(),
				                    this.isDrawOnlyInBounds(), 
				                    this.getStringScaleMode(), 
				                    this.getStringXAlignment(), this.getStringYAlignment()
				                   }
				);
		
		return Saveable.newLine(write);
	}
	
}
