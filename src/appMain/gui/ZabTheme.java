package appMain.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;

/**
 * A class containing utility methods for setting the look and feel of GUI components in the Zab Application
 * @author zrona
 */
public final class ZabTheme{
	
	/**
	 * Set the look and feel of the given {@link Component} to that of the Zab Application. Only modifies the given {@link Component} 
	 * 	not any of the components within the given {@link Component}
	 * @param c The component to update
	 */
	public static void setToTheme(Component c){
		// Set the background to dark gray
		c.setBackground(new Color(40, 40, 40));
		
		// Set the foreground / text to light gray
		c.setForeground(new Color(180, 180, 180));
	}
	
	/**
	 * Set the look and feel of the given {@link AbstractButton} to that of the Zab Application. Only modifies the given {@link AbstractButton} 
	 * 	not any of the components within the given {@link AbstractButton}
	 * @param c The component to update
	 */
	public static void setToTheme(AbstractButton b){
		// Set general theme
		setToTheme((Component)b);
		
		// Set the hovering and clicking colors
		b.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e){
				b.setBackground(new Color(80, 80, 80));
			}
			@Override
			public void mouseExited(MouseEvent e){
				b.setBackground(new Color(40, 40, 40));
			}
		});
		b.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				b.setBackground(new Color(120, 120, 120));
			}
			@Override
			public void mouseReleased(MouseEvent e){
				b.setBackground(new Color(40, 40, 40));
			}
		});
	}
	
	/** Cannot instantiate a {@link ZabTheme} */
	private ZabTheme(){}
	
}
