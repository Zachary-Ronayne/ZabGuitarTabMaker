package appMain.gui.frames.editor;

import java.util.Timer;
import java.util.TimerTask;

import appMain.gui.comp.ZabLabel;
import lang.AbstractLanguage;
import lang.Language;

/**
 * A label used to display the status of saving, loading, and exporting files, 
 * and keep track of a timer to clear the status after a time
 * @author zrona
 */
public class FileStatusLabel extends ZabLabel{
	private static final long serialVersionUID = 1L;

	/** The amount of time, in milliseconds, which the file status will display */
	public static final int DISP_TIME = 2000;

	
	/**
	 * The {@link Timer} currently being used to track the file status tasks. 
	 * Can be null if no timer task is set
	 */
	private Timer fileStatusTimer;
	/**
	 * The {@link TimerTask} currently tracking when the file status will be updated. 
	 * Can be null if no timer task is set
	 */
	private FileStatusTimerTask fileStatusTask;
	
	/**
	 * Create a new {@link FileStatusLabel} in a default state
	 */
	public FileStatusLabel(){
		this.setFontSize(15);
		this.updateFileStatus(null);
		this.fileStatusTimer = null;
		this.fileStatusTask = null;
	}
	
	/** @return See {@link #fileStatusTimer} */
	public Timer getFileStatusTimer(){
		return this.fileStatusTimer;
	}
	
	/** @return See {@link #fileStatusTask} */
	public FileStatusTimerTask getFileStatusTask(){
		return this.fileStatusTask;
	}
	
	/**
	 * Set the string to display for the status of {@link #fileStatusLab}
	 * @param status The new status, null to clear the status
	 */
	public void updateFileStatus(String status){
		// Cancel whatever the last status was
		if(this.fileStatusTask != null) this.fileStatusTask.cancel();
		if(this.fileStatusTimer != null) this.fileStatusTimer.cancel();
		
		// If there status should be cleared, then also set the timer related objects to null
		if(status == null){
			this.setText(" ");
			this.fileStatusTask = null;
			this.fileStatusTimer = null;
		}
		// Otherwise, begin the new timer
		else{
			this.setText(status);
			this.fileStatusTimer = new Timer();
			this.fileStatusTask = new FileStatusTimerTask();
			this.fileStatusTimer.schedule(this.fileStatusTask, DISP_TIME);
		}
		
		// Repaint so that the display is updated
		this.repaint();
	}

	/**
	 * Update the text of {@link #fileStatusLab} to one of the given text values, depending on success
	 * @param success true if this update is for a successful message, false for a failure message
	 * @param ifSuccess The message to display on success
	 * @param ifFail The message to display on fail
	 */
	public void updateFileStatus(boolean success, String ifSuccess, String ifFail){
		if(success) this.updateFileStatus(ifSuccess);
		else this.updateFileStatus(ifFail);
	}
	
	/**
	 * Update the text of this label to the text for saving
	 * @param success true if the save was successful, false otherwise
	 */
	public void updateSaveStatus(boolean success){
		AbstractLanguage lang = Language.get();
		this.updateFileStatus(success, lang.saveSuccess(), lang.saveFail());
	}
	
	/**
	 * Update the text of this label to the text for loading
	 * @param success true if the load was successful, false otherwise
	 */
	public void updateLoadStatus(boolean success){
		AbstractLanguage lang = Language.get();
		this.updateFileStatus(success, lang.loadSuccess(), lang.loadFail());
	}
	
	/**
	 * Update the text of this label to the text for exporting
	 * @param success true if the load was successful, false otherwise
	 */
	public void updateExportStatus(boolean success){
		AbstractLanguage lang = Language.get();
		this.updateFileStatus(success, lang.exportSuccess(), lang.exportFail());
	}
	
	/**
	 * A class used to keep track of when the time to clear the displayed file status.<br>
	 * This task simply clears the file status when it executes.
	 * @author zrona
	 */
	public class FileStatusTimerTask extends TimerTask{
		@Override
		public void run(){
			updateFileStatus(null);
		}
	}
	
}
