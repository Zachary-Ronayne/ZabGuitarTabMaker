package appMain.gui.help;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabDialog;
import lang.Language;

public class ControlsDialog extends ZabDialog{
	private static final long serialVersionUID = 1L;
	
	/** The {@link KeyAdapter} used to close this window when escape is pressed */
	private DialogEscaper escaper;
	
	/**
	 * Create a new {@link ControlsDialog} in a default state which uses the given {@link ZabGui}]
	 * @param gui See {@link #getGui()}
	 */
	public ControlsDialog(ZabGui gui){
		super(gui, new ControlsFrame(gui));
		this.setTitle(Language.get().controls());
		
		this.escaper = new DialogEscaper();
		this.addKeyListener(this.escaper);
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(false);
		this.pack();
	}
	
	/** @return See {@link #escaper} */
	public DialogEscaper getEscaper(){
		return this.escaper;
	}
	
	/**
	 * A class that makes this {@link ControlsDialog} close when the escape key is pressed
	 * @author zrona
	 */
	public class DialogEscaper extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) setVisible(false);
		}
	}

}
