package appMain.gui.util;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.Scanner;

import util.Saveable;

/**
 * A class that handles camera movement. This allows a single object to be used to keep track of all movements of a camera, 
 * and then change the location on a component's associated graphics object based on the camera position and zoom level. 
 * All methods that have the same name as those in a Graphics object do the same thing, but take double values as parameters.<br>
 * This is recycled code from an older project
 */
public class Camera implements Saveable{
	
	/**
	 * The x position of the camera, upper left hand corner
	 */
	private double camX;
	/**
	 * The y position of the camera, upper left hand corner
	 */
	private double camY;
	/**
	 * The width of the space that this camera takes up, for example the width of a GUI that uses this camera
	 */
	private double camWidth;
	/**
	 * The height of the space that this camera takes up, for example the height of a GUI that uses this camera
	 */
	private double camHeight;
	/**
	 * The amount that will be zoomed in on the x axis. Negative values zoom out, positive values zoom in.
	 */
	private double xZoomFactor;
	/**
	 * The amount that will be zoomed in on the y axis. Negative values zoom out, positive values zoom in.
	 */
	private double yZoomFactor;
	
	/**
	 * The anchor x position used for panning
	 */
	private double anchorX;
	/**
	 * The anchor y position used for panning
	 */
	private double anchorY;
	/**
	 * The x position the camera was in when the camera was anchored
	 */
	private double anchorCamX;
	/**
	 * They y position the camera was in when the camera was anchored
	 */
	private double anchorCamY;
	/**
	 * true if the camera currently has an anchor point set, false otherwise
	 */
	private boolean anchored;

	/**
	 * The minimum value that {@link #camX} can take
	 */
	private double minX;
	/**
	 * The minimum value that {@link #camY} can take
	 */
	private double minY;
	/**
	 * The maximum value that {@link #camX} can take
	 */
	private double maxX;
	/**
	 * The maximum value that {@link #camY} can take
	 */
	private double maxY;
	
	/**
	 * The graphics object that this camera should draw graphics to
	 */
	private Graphics2D g;
	
	/**
	 * True if objects should only be drawn when they are in bounds of the camera, false otherwise
	 */
	private boolean drawOnlyInBounds;
	
	/**
	 * Create a new empty camera
	 */
	public Camera(double camWidth, double camHeight){
		this.camWidth = camWidth;
		this.camHeight = camHeight;
		
		camX = 0;
		camY = 0;
		
		xZoomFactor = 0;
		yZoomFactor = 0;
		
		anchored = false;
		anchorX = 0;
		anchorY = 0;
		
		minX = Integer.MIN_VALUE;
		minY = Integer.MIN_VALUE;
		maxX = Integer.MAX_VALUE;
		maxY = Integer.MAX_VALUE;
		
		g = null;
		
		drawOnlyInBounds = true;
	}
	
	/**
	 * @return See {@link #anchored}
	 */
	public boolean isAchored(){
		return anchored;
	}
	/**
	 * @return See {@link #anchorX}
	 */
	public double getAnchorX(){
		return anchorX;
	}
	/**
	 * @return See {@link #anchorY}
	 */
	public double getAnchorY(){
		return anchorY;
	}
	
	/**
	 * Set the anchor position of this camera, used for panning. When anchored, 
	 * all calls to pan the camera will be made relative to this anchor point. <br>
	 * The values should be points where a screen would have been clicked, not based
	 * on camera position.<br>
	 * A new anchor position is only set when the camera is not anchored
	 * @param anchorX the x position for the anchor
	 * @param anchorY the y position for the anchor
	 */
	public void setAnchor(double anchorX, double anchorY){
		if(!anchored){
			this.anchorX = anchorX;
			this.anchorY = anchorY;
			
			this.anchorCamX = this.camX;
			this.anchorCamY = this.camY;
			
			anchored = true;
		}
	}
	
	/**
	 * If an anchor position is set, the anchor is released
	 */
	public void releaseAnchor(){
		anchored = false;
	}
	
	/**
	 * Based on the anchor position, pan the camera to the position given.<br>
	 * The position should again be given as where a screen was clicked and dragged, 
	 * not based on camera position.<br>
	 * This method does nothing if no anchor is been set
	 * @param panX the x position to pan to
	 * @param panY the y position to pan to
	 */
	public void pan(double panX, double panY){
		if(anchored){
			setX(this.anchorCamX + inverseZoom(anchorX - panX, xZoomFactor));
			setY(this.anchorCamY + inverseZoom(anchorY - panY, yZoomFactor));
		}
	}
	
	/**
	 * Set the given point as the center of the camera
	 * @param x the x position
	 * @param y the y position
	 */
	public void center(double x, double y){
		setX(x - inverseZoom(camWidth, xZoomFactor) * 0.5);
		setY(y - inverseZoom(camHeight, yZoomFactor) * 0.5);
	}
	
	/**
	 * Zoom in on both axes at the specified location on the camera
	 * @param x the x zoom position on the camera
	 * @param y the y zoom position on the camera
	 * @param direction the amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 */
	public void zoomIn(double x, double y, double direction){
		zoomInX(x, direction);
		zoomInY(y, direction);
	}

	/**
	 * Zoom in on only the x axis at the specified location on the camera
	 * @param x The x zoom position on the camera
	 * @param direction The amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 */
	public void zoomInX(double x, double direction){
		// Zoom the value
		setX(zoomValue(x, this.getWidth(), this.getX(), this.getXZoomFactor(), direction));

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
		setY(zoomValue(y, this.getHeight(), this.getY(), this.getYZoomFactor(), direction));

		// Update the zoom factor
		this.setYZoomFactor(this.getYZoomFactor() + direction);
	}
	
	/**
	 * Get the pixel coordinate of a value on the x axis as it should be used for a graphics call.<br>
	 * Generally should only be used by the inside of this class.
	 * @param value the value to find the coordinate of
	 * @return the coordinate
	 */
	public int drawX(double value){
		return (int)Math.round(zoom(value - camX, xZoomFactor));
	}
	
	/**
	 * Get the pixel coordinate of a value on the y axis as it should be used for a graphics call.<br>
	 * Generally should only be used by the inside of this class.
	 * @param value the value to find the coordinate of
	 * @return the coordinate
	 */
	public int drawY(double value){
		return (int)Math.round(zoom(value - camY, yZoomFactor));
	}

	/**
	 * Get the pixel width of a value that will be drawn to the x axis<br>
	 * @param value the value to get the pixel width of
	 * @return the pixel width
	 */
	public int drawW(double value){
		return (int)Math.round(zoom(value, xZoomFactor));
	}
	
	/**
	 * Get the pixel height of a value that will be drawn to the y axis<br>
	 * @param value the value to get the pixel height of
	 * @return the pixel height
	 */
	public int drawH(double value){
		return (int)Math.round(zoom(value, yZoomFactor));
	}

	/**
	 * Based on an x pixel coordinate, get the corresponding location based on where the camera is.
	 * @param value the pixel coordinate
	 * @return the x pixel coordinate of the camera
	 */
	public double mouseX(double value){
		return inverseZoom(value, xZoomFactor) + camX;
	}

	/**
	 * Based on an y pixel coordinate, get the corresponding location based on where the camera is.
	 * @param value the pixel coordinate
	 * @return the y pixel coordinate of the camera
	 */
	public double mouseY(double value){
		return inverseZoom(value, yZoomFactor) + camY;
	}
	
	/**
	 * See {@link Graphics2D#fillRect(int, int, int, int)}
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean fillRect(double x, double y, double width, double height){
		if(!inBounds(x, y, width, height)) return false;
		g.fillRect(drawX(x), drawY(y), drawW(width), drawH(height));
		return true;
	}

	/**
	 * See {@link Graphics2D#fillOval(int, int, int, int)}
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean fillOval(double x, double y, double width, double height){
		if(!inBounds(x, y, width, height)) return false;
		g.fillOval(drawX(x), drawY(y), drawW(width), drawH(height));
		return true;
	}

	/**
	 * See {@link Graphics2D#drawLine(int, int, int, int)}
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean drawLine(double x1, double y1, double x2, double y2){
		if(!inBounds(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2))) return false;
		g.drawLine(drawX(x1), drawY(y1), drawX(x2), drawY(y2));
		return true;
	}
	
	/**
	 * Draw a string which does not scale with the size of the zoom
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean drawString(String s, double x, double y){
		return drawString(s, x, y, -1);
	}
	/**
	 * Draw a string which does not scale with the size of the zoom
	 * @param s The string to draw
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param fontSize The font size to use, the graphics object will have this font size, used negative values to not change the font
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean drawString(String s, double x, double y, double fontSize){
		Font f = g.getFont();
		if(fontSize < 0){
			fontSize = f.getSize();
		}
		else{
			g.setFont(new Font(f.getName(), f.getStyle(), (int)fontSize));
		}
		if(!inBounds(x, y - fontSize, g.getFontMetrics().stringWidth(s), fontSize)) return false;
		g.drawString(s, drawX(x), drawY(y));
		return true;
	}
	/**
	 * Draw a string that changes it's size based on the current scale of the camera.<br>
	 * The size is based on the current font set by this objects graphics object
	 * @param s the string to draw
	 * @param x the x position
	 * @param y the y position
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean drawScaleString(String s, double x, double y){
		double fontSize = zoom(g.getFont().getSize(), yZoomFactor);
		return drawString(s, x, y, fontSize);
	}
	/**
	 * Draw an image at the specified location and size
	 * @param img The image to draw
	 * @param x The x coordinate to draw at
	 * @param y The y coordinate to draw at
	 * @param w The width at which to draw the image
	 * @param h The height at which to draw the image
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean drawImage(BufferedImage img, double x, double y, double w, double h){
		if(!inBounds(x, y, w, h)) return false;
		g.drawImage(img, drawX(x), drawY(y), drawW(w), drawH(h), null);
		return true;
	}
	/**
	 * Draw an image at the specified location, size is determined by the image width and height
	 * @param img The image to draw
	 * @param x The x coordinate to draw at
	 * @param y The y coordinate to draw at
	 * @return true if the object was drawn, false otherwise
	 */
	public boolean drawImage(BufferedImage img, double x, double y){
		return this.drawImage(img, x, y, img.getWidth(), img.getHeight());
	}
	
	/**
	 * Determine if the given rectangular bounds are within the size of the camera.<br>
	 * Essentially, determines if graphics the rectangular bounds will be rendered on the graphics object
	 * @param x The x coordinate of the bounds
	 * @param y The y coordinate of the bounds
	 * @param width The width of the bounds
	 * @param height The height of the bounds
	 * @return true if drawing only in bounds is disabled, or if the given bounds intersect the camera bounds, false otherwise
	 */
	public boolean inBounds(double x, double y, double width, double height){
		if(!isDrawOnlyInBounds()) return true;
		Rectangle2D.Double camBounds = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
		Rectangle2D.Double testBounds = new Rectangle2D.Double(
				zoom(x - camX, xZoomFactor), zoom(y - camY, yZoomFactor),
				zoom(width, xZoomFactor), zoom(height, yZoomFactor)
		);
		return camBounds.intersects(testBounds);
	}
	
	/**
	 * @return See {@link #camX}
	 */
	public double getX(){
		return camX;
	}
	/**
	 * @param x See {@link #camX}
	 */
	public void setX(double x){
		this.camX = Math.max(minX, Math.min(maxX, x));
	}

	/**
	 * @return See {@link #camY}
	 */
	public double getY(){
		return camY;
	}
	/**
	 * @param y See {@link #camY}
	 */
	public void setY(double y){
		this.camY = Math.max(minY, Math.min(maxY, y));
	}

	/**
	 * @return See {@link #camWidth}
	 */
	public double getWidth(){
		return camWidth;
	}
	/**
	 * @param camWidth See {@link #camWidth}
	 */
	public void setWidth(double camWidth){
		this.camWidth = camWidth;
	}
	
	/**
	 * @return See {@link #camHeight}
	 */
	public double getHeight(){
		return camHeight;
	}
	/**
	 * @param camHeight See {@link #camHeight}
	 */
	public void setHeight(double camHeight){
		this.camHeight = camHeight;
	}

	/**
	 * @return See {@link #xZoomFactor}
	 */
	public double getXZoomFactor(){
		return xZoomFactor;
	}
	/**
	 * @param xZoomFactor See {@link #xZoomFactor}
	 */
	public void setXZoomFactor(double xZoomFactor){
		this.xZoomFactor = xZoomFactor;
	}

	/**
	 * @return See {@link #yZoomFactor}
	 */
	public double getYZoomFactor(){
		return yZoomFactor;
	}
	/**
	 * @param yZoomFactor See {@link #yZoomFactor}
	 */
	public void setYZoomFactor(double yZoomFactor){
		this.yZoomFactor = yZoomFactor;
	}

	/**
	 * @return See {@link #minX}
	 */
	public double getMinX(){
		return minX;
	}
	/**
	 * @param minX See {@link #minX}
	 */
	public void setMinX(double minX){
		this.minX = minX;
	}

	/**
	 * @return See {@link #minY}
	 */
	public double getMinY(){
		return minY;
	}
	/**
	 * @param minY See {@link #minY}
	 */
	public void setMinY(double minY){
		this.minY = minY;
	}

	/**
	 * @return See {@link #maxX}
	 */
	public double getMaxX(){
		return maxX;
	}
	/**
	 * @param maxX See {@link #maxX}
	 */
	public void setMaxX(double maxX){
		this.maxX = maxX;
	}

	/**
	 * @return See {@link #maxY}
	 */
	public double getMaxY(){
		return maxY;
	}
	/**
	 * @param maxY See {@link #maxY}
	 */
	public void setMaxY(double maxY){
		this.maxY = maxY;
	}

	/**
	 * @return See {@link #g}
	 */
	public Graphics2D getG(){
		return g;
	}
	/**
	 * @param g See {@link #g}
	 */
	public void setG(Graphics2D g){
		this.g = g;
	}

	/**
	 * @return See {@link #drawOnlyInBounds}
	 */
	public boolean isDrawOnlyInBounds(){
		return drawOnlyInBounds;
	}
	/**
	 * True if objects should only be drawn when they are in bounds of the camera, false otherwise
	 * @param drawOnlyInBounds see {@link #drawOnlyInBounds}
	 */
	public void setDrawOnlyInBounds(boolean drawOnlyInBounds){
		this.drawOnlyInBounds = drawOnlyInBounds;
	}
	
	/**
	 * Get the zoomed value of the given value based on the given zoom factor
	 * @param value the value to zoom
	 * @param factor the zoom factor
	 * @return the zoomed value
	 */
	public static double zoom(double value, double factor){
		return value * Math.pow(2, factor);
	}
	
	/**
	 * Get the zoomed value of the given value based on the given zoom factor, 
	 * where the zoom factor is inverted before it's multiplied by value
	 * @param value the value to zoom
	 * @param factor the zoom factor
	 * @return the zoomed value
	 */
	public static double inverseZoom(double value, double factor){
		return zoom(value, -factor);
	}
	
	/**
	 * Determine the value for zooming in on one axis at the specified location on a camera
	 * @param zoomPos The position to zoom into
	 * @param camSize The size of the camera zooming in to
	 * @param camPos The zoom position on the camera with zooming
	 * @param zoomFactor The original amount zoomed in before a new zoom is applied
	 * @param direction The amount to zoom in. Use negative values to zoom out, positive values to zoom in
	 */
	public static double zoomValue(double zoomPos, double camSize, double camPos, double zoomFactor, double direction){
		//save the old value of the zoomed camera before the zoom
		//inverse is used because width and height should change in opposite directions from the zoomFactors
		double oldHeight = inverseZoom(camSize, zoomFactor);
		
		//increase the zoom factor
		zoomFactor += direction;
		
		//adjust the camera position based on where the zoom occurred
		
		//find the new width and height after the zoom
		double newHeight = inverseZoom(camSize, zoomFactor);
		
		//find the difference in height in distance changed
		double hDist = (oldHeight - newHeight);
		
		//move the y camera position based on the distance moved in the zoom,
		//and the ratio of the positions of the zoom position
		return camPos + hDist * (zoomPos / camSize);
	}

	/***/
	@Override
	public boolean load(Scanner read){
		Double[] load = Saveable.loadDoubles(read, 10);
		if(load == null) return false;
		
		Boolean loadBool = Saveable.loadBool(read);
		if(loadBool == null) return false;
		
		setX(load[0]);
		setY(load[1]);
		setWidth(load[2]);
		setHeight(load[3]);
		setXZoomFactor(load[4]);
		setYZoomFactor(load[5]);
		setMinX(load[6]);
		setMinY(load[7]);
		setMaxX(load[8]);
		setMaxY(load[9]);
		setDrawOnlyInBounds(loadBool);
			
		return true;
	}
	
	/***/
	@Override
	public boolean save(PrintWriter write){
		if(!Saveable.saveToStrings(
				write, new Object[]{this.getX(), this.getY(), this.getWidth(), this.getHeight(),
				                    this.getXZoomFactor(), this.getYZoomFactor(),
				                    this.getMinX(), this.getMinY(), this.getMaxX(), this.getMaxY(),
				                    this.isDrawOnlyInBounds()}))
			return false;
		
		return Saveable.newLine(write);
	}
	
}
