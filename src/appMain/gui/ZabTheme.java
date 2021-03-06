package appMain.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.border.Border;

import appMain.gui.editor.paint.TabPainter;
import appUtils.ZabAppSettings;
import tab.TabString;

/**
 * An interface containing containing utility methods for setting the colors of components in the Zab Application.<br>
 * Implementations of this interface must define every color in a theme
 * @author zrona
 */
public interface ZabTheme{
	
	/**
	 * Set the color scheme of the given {@link Component} to that of the Zab Application. Only modifies the given {@link Component} 
	 * 	not any of the components within the given {@link Component}
	 * @param c The component to update, if c is null, this method does nothing.
	 */
	public static void setToTheme(Component c){
		// Ensure the component exists
		if(c == null) return;
		
		// Get the theme
		ZabTheme t = ZabAppSettings.theme();

		// Set up objects for general use
		Border normalBorder = BorderFactory.createLineBorder(t.borderColor(), 1, false);
		Border emptyBorder = BorderFactory.createEmptyBorder();
		
		// Set fields for all Components
		c.setBackground(t.background());
		c.setForeground(t.foreground());
		
		// Case of a JComponent
		if(c instanceof JComponent){
			JComponent j = (JComponent)c;
			
			// No border in general for components
			j.setBorder(emptyBorder);
		}
		
		// Case of a JMenuBar
		if(c instanceof JMenuBar){
			JComponent j = (JComponent)c;
			
			j.setBackground(t.menuBarBackground());
		}
		
		// Set fields in the case of an JMenu
		if(c instanceof JMenu){
			JMenu j = (JMenu)c;
			
			// Change the pop up menu border
			JPopupMenu p = j.getPopupMenu();
			p.setBorder(normalBorder);
		}
		
		// Set fields in the case of an AbstractButton
		if(c instanceof AbstractButton){
			AbstractButton b = (AbstractButton)c;

			// Add listeners for changing color
			b.setOpaque(true);
			// Ensure the button does not already have a ButtonHoverSensor
			MouseListener[] m = b.getMouseListeners();
			for(int i = 0; i < m.length; i++){
				if(m[i] instanceof ButtonHoverSensor) b.removeMouseListener(m[i]);
			}
			// Add a new listener
			ButtonHoverSensor sensor = new ButtonHoverSensor(b);
			b.addMouseListener(sensor);
			b.addActionListener(sensor);
		}
		
		// Set fields in the case of an JButton
		if(c instanceof JButton){
			JButton b = (JButton)c;

			b.setBorder(normalBorder);
		}
	}
	
	/**
	 * Set the values in the UIManager to that of the given theme
	 * @param t The theme to use
	 */
	public static void setUIMangerTheme(ZabTheme t){
		// General
		UIManager.put("Panel.background", t.background());
		UIManager.put("Label.foreground", t.foreground());
		
		// Buttons and menus
		UIManager.put("MenuItem.selectionBackground", t.menuBarMouseHover());
		UIManager.put("MenuItem.selectionForeground", t.menuBarMouseHoverText());
		UIManager.put("Button.select", t.buttonClick());
		UIManager.put("Button.highlight", t.buttonHover());
		UIManager.put("Button.light", t.buttonHover());
		UIManager.put("Button.foreground", t.foreground());
		UIManager.put("Button.background", t.background());
		UIManager.put("Menu.selectionBackground", t.menuMouseClick());
		UIManager.put("Menu.selectionForeground", t.menuMouseClickText());
		
		// Option pane
		UIManager.put("OptionPane.background", t.background());
		UIManager.put("OptionPane.foreground", t.foreground());
		UIManager.put("OptionPane.messageForeground", t.foreground());
		UIManager.put("OptionPane.border", BorderFactory.createLineBorder(t.background(), 15, false));
		UIManager.put("OptionPane.buttonAreaBorder", BorderFactory.createLineBorder(t.background(), 2, false));
		UIManager.put("Button.border", BorderFactory.createLineBorder(t.foreground(), 2, true));
		UIManager.put("OptionPane.buttonFont", TabPainter.SYMBOL_FONT);
		UIManager.put("OptionPane.font", TabPainter.SYMBOL_FONT);
		UIManager.put("OptionPane.messageFont", TabPainter.SYMBOL_FONT);
		UIManager.put("OptionPane.questionIcon", new ZabQuestionIcon());
		
//		printDefaultUI();
	}
	
	/**
	 * A method here for the sole reason of making it easier to read the keys and values of the UIManager
	 */
	public static void printDefaultUI(){
		String s = UIManager.getDefaults().toString();
		String[] arr = s.split(" ");
		for(String ar : arr){
			System.out.println(ar);
		}
	}
	
	/**
	 * Get the theme used by default in the Zab Application
	 * @return The theme
	 */
	public static ZabTheme getDefaultTheme(){
		return new DarkTheme();
	}
	
	/**
	 * The Default theme for the Zab Application with dark backgrounds and light text
	 * @author zrona
	 */
	public static class DarkTheme implements ZabTheme{
		@Override
		public Color background(){ return new Color(30, 30, 30); }
		@Override
		public Color foreground(){ return new Color(180, 180, 180); }
		@Override
		public Color buttonClick(){ return new Color(70, 70, 70); }
		@Override
		public Color buttonHover(){ return new Color(80, 80, 80); }
		@Override
		public Color menuMouseClick(){ return new Color(70, 70, 70); }
		@Override
		public Color menuMouseClickText(){ return new Color(150, 150, 150); }
		@Override
		public Color menuBarBackground(){ return new Color(50, 50, 50); }
		@Override
		public Color menuBarMouseHover(){ return new Color(80, 80, 80); }
		@Override
		public Color menuBarMouseHoverText(){ return new Color(150, 150, 150); }
		@Override
		public Color borderColor(){ return new Color(100, 100, 100); }
		@Override
		public Color tabString(){ return new Color(60, 60, 60); }
		@Override
		public Color tabSymbolText(){ return new Color(200, 200, 200); }
		@Override
		public Color tabSymbolDragText(){ return new Color(200, 200, 255, 150); }
		@Override
		public Color tabSymbolHighlight(){ return new Color(170, 170, 255, 100); }
		@Override
		public Color tabSymbolBoxHighlight(){ return new Color(0, 255, 0, 127); }
		@Override
		public Color tabSymbolHoverHighlight(){ return new Color(200, 200, 255, 63); }
	}
	
	/**
	 * A theme for the Zab Application with light backgrounds and dark text
	 * @author zrona
	 */
	public static class LightTheme implements ZabTheme{
		@Override
		public Color background(){ return new Color(240, 240, 240); }
		@Override
		public Color foreground(){ return new Color(0, 0, 0); }
		@Override
		public Color buttonClick(){ return new Color(200, 200, 255); }
		@Override
		public Color buttonHover(){ return new Color(220, 220, 255); }
		@Override
		public Color menuMouseClick(){ return new Color(200, 200, 255); }
		@Override
		public Color menuMouseClickText(){ return new Color(0, 0, 20); }
		@Override
		public Color menuBarBackground(){ return new Color(200, 200, 200); }
		@Override
		public Color menuBarMouseHover(){ return new Color(220, 220, 255); }
		@Override
		public Color menuBarMouseHoverText(){ return new Color(0, 0, 20); }
		@Override
		public Color borderColor(){ return new Color(0, 0, 0); }
		@Override
		public Color tabString(){ return new Color(180, 180, 180); }
		@Override
		public Color tabSymbolText(){ return new Color(20, 20, 20); }
		@Override
		public Color tabSymbolDragText(){ return new Color(20, 20, 100, 150); }
		@Override
		public Color tabSymbolHighlight(){ return new Color(70, 70, 255, 100); }
		@Override
		public Color tabSymbolBoxHighlight(){ return new Color(30, 225, 30, 127); }
		@Override
		public Color tabSymbolHoverHighlight(){ return new Color(150, 150, 225, 63); }
	
	}
	
	/** @return The main background color */
	public abstract Color background();
	
	/** @return The main foreground color, i.e. text color */
	public abstract Color foreground();
	
	/** @return The background color when the mouse clicks a button */
	public abstract Color buttonClick();
	
	/** @return The background color when the mouse hovers over a button */
	public abstract Color buttonHover();
	
	/** @return The color when a menu in a menu bar is clicked */
	public abstract Color menuMouseClick();
	
	/** @return The color of the text when a menu in a menu bar is clicked */
	public abstract Color menuMouseClickText();
	
	/** @return The background color for menuBars */
	public abstract Color menuBarBackground();
	
	/** @return The color of menuBar backgrounds when the mouse hovers over it */
	public abstract Color menuBarMouseHover();
	
	/** @return The color of menuBar text when the mouse hovers over it */
	public abstract Color menuBarMouseHoverText();
	
	/** @return The main border color, i.e. the line that goes around buttons and so on */
	public abstract Color borderColor();
	
	/** @return The color of the lines used to draw {@link TabString} */
	public abstract Color tabString();
	
	/** @return The color of symbols drawn on a tab */
	public abstract Color tabSymbolText();
	
	/** @return The color of symbols being dragged around */
	public abstract Color tabSymbolDragText();
	
	/** @return The color of the highlight of selected symbols drawn on a tab */
	public abstract Color tabSymbolHighlight();
	
	/** @return The color of the highlight of symbols drawn on a tab when a selection box is being drawn over them */
	public abstract Color tabSymbolBoxHighlight();
	
	/** @return The color of the highlight of symbols drawn on a tab when the mouse hovers over them */
	public abstract Color tabSymbolHoverHighlight();
	
	/**
	 * A {@link MouseAdapter} given to all buttons which, via {@link ZabTheme#setToTheme(Component)}, 
	 * are set to use  a {@link ZabTheme}
	 * @author zrona
	 */
	public static class ButtonHoverSensor extends MouseAdapter implements ActionListener{
		/** The {@link AbstractButton} which uses this sensor */
		private AbstractButton button;
		private ZabTheme theme;
		
		public ButtonHoverSensor(AbstractButton button){
			this.button = button;
			this.theme = ZabAppSettings.theme();
		}
		
		@Override
		public void actionPerformed(ActionEvent e){
			this.button.setBackground(this.theme.background());
		}
		@Override
		public void mouseEntered(MouseEvent e){
			this.button.setBackground(this.theme.buttonHover());
		}
		@Override
		public void mouseExited(MouseEvent e){
			this.button.setBackground(this.theme.background());
		}
	}
	
	/**
	 * A utility class for creating a custom icon for displaying a question icon JOptionPane. 
	 * This is currently an empty icon which displays nothing
	 * @author zrona
	 */
	public static class ZabQuestionIcon implements Icon{
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y){}
		@Override
		public int getIconWidth(){return 0;}
		@Override
		public int getIconHeight(){return 0;}
	}
	
}
