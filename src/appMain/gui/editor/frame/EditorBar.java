package appMain.gui.editor.frame;

import javax.swing.JLabel;

import appMain.gui.comp.ZabPanel;
import appMain.gui.layout.ZabLayoutHandler;

/**
 * A {@link ZabPanel} which holds all of the items which the user can click for quick access to editing tools
 * @author zrona
 */
public class EditorBar extends ZabPanel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The {@link JLabel} holding the status of the last file operation, i.e. failed save, successful save, etc.
	 * Can be blank if no recent status should be displayed
	 */
	private FileStatusLabel fileStatusLab;
	
	/**
	 * Create an {@link EditorBar} in its default state
	 */
	public EditorBar(){
		super();
		ZabLayoutHandler.createHorizontalLayout(this);
		
		// Object holding state of file status
		fileStatusLab = new FileStatusLabel();
		this.add(fileStatusLab);
	}
	
	/** @return See {@link #fileStatusLab} */
	public FileStatusLabel getFileStatusLab(){
		return this.fileStatusLab;
	}
	
}
