package appMain.gui.dropMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import appMain.gui.ZabGui;
import appMain.gui.comp.ZabDialog;
import appMain.gui.help.ControlsDialog;
import appUtils.ZabConstants;
import lang.Language;

public class HelpMenu extends ZabMenu{
	private static final long serialVersionUID = 1L;
	
	/** The {@link ZabMenuItem} which opens a {@link ControlsDialog} */
	private ZabMenuItem controlsItem;
	
	/** The listener used by {@link #controlsItem} to open {@link #controlsDialog} */
	private ControlsListener controlsListener;
	
	/** The {@link ZabDialog} which displays the controls to the user */
	private ControlsDialog controlsDialog;
	
	/**
	 * Create a new {@link HelpMenu} in the default state
	 * @param gui The {@link ZabGui} which uses this {@link HelpMenu}
	 */
	public HelpMenu(ZabGui gui){
		super(Language.get().help(), gui);
		
		this.controlsDialog = new ControlsDialog(gui);
		
		this.controlsListener = new ControlsListener();
		
		this.controlsItem = new ZabMenuItem(Language.get().controls());
		this.controlsItem.addActionListener(this.controlsListener);
		this.add(this.controlsItem);
	}
	
	/** @return See {@link #controlsItem} */
	public ZabMenuItem getControlsItem(){
		return this.controlsItem;
	}
	
	/** @return See {@link #controlsListener} */
	public ControlsListener getControlsListener(){
		return this.controlsListener;
	}
	
	/** @return See {@link #controlsDialog} */
	public ControlsDialog getControlsDialog(){
		return this.controlsDialog;
	}
	
	/**
	 * A class used by {@link HelpMenu#controlsItem} to open {@link HelpMenu#controlsDialog}
	 * @author zrona
	 */
	public class ControlsListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			if(ZabConstants.ENABLE_DIALOG){
				ControlsDialog dialog = getControlsDialog();
				dialog.setLocationRelativeTo(getGui());
				dialog.toFront();
				dialog.setVisible(true);
			}
		}
	}

}
