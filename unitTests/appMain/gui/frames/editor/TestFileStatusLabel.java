package appMain.gui.frames.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.frames.editor.FileStatusLabel.FileStatusTimerTask;
import appUtils.ZabAppSettings;

public class TestFileStatusLabel{
	
	private FileStatusLabel label;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		label = new FileStatusLabel();
	}

	@Test
	public void constructor(){
		assertEquals(" ", label.getText(), "Checking text is just a space initially");
		assertEquals(15, label.getFont().getSize(), "Checking font size set");
	}
	
	@Test
	public void getFileStatusTimer(){
		assertEquals(null, label.getFileStatusTimer(), "Checking timer is null initially");
	}
	
	@Test
	public void fileStatusTask(){
		assertEquals(null, label.getFileStatusTask(), "Checking task is null initially");
	}
	
	@Test
	public void updateFileStatus(){
		label.updateFileStatus("text");
		assertEquals("text", label.getText(), "Checking text set");
		assertNotEquals(null, label.getFileStatusTask(), "Checking task not null");
		assertNotEquals(null, label.getFileStatusTimer(), "Checking timer not null");
		
		label.updateFileStatus(null);
		assertEquals(" ", label.getText(), "Checking text set after using null");
		assertEquals(null, label.getFileStatusTask(), "Checking task set to null");
		assertEquals(null, label.getFileStatusTimer(), "Checking timer set to null");
		
		label.updateFileStatus("new words");
		assertEquals("new words", label.getText(), "Checking text updated");
		
		label.updateFileStatus(true, "a", "b");
		assertEquals("a", label.getText(), "Checking success text updated");
		
		label.updateFileStatus(false, "a", "b");
		assertEquals("b", label.getText(), "Checking fail text updated");
	}
	
	@Test
	public void updateSaveStatus(){
		label.updateSaveStatus(true);
		assertEquals("Save successful", label.getText(), "Checking success text");
	
		label.updateSaveStatus(false);
		assertEquals("Save failed", label.getText(), "Checking fail text");
	}
	
	@Test
	public void updateLoadStatus(){
		label.updateLoadStatus(true);
		assertEquals("Load successful", label.getText(), "Checking success text");
		
		label.updateLoadStatus(false);
		assertEquals("Load failed", label.getText(), "Checking fail text");
	}

	@Test
	public void updateExportStatus(){
		label.updateExportStatus(true);
		assertEquals("Export successful", label.getText(), "Checking success text");
		
		label.updateExportStatus(false);
		assertEquals("Export failed", label.getText(), "Checking fail text");
	}
	
	@Test
	public void runFileStatusTimerTask(){
		label.updateFileStatus("test");
		FileStatusTimerTask task = label.getFileStatusTask();
		task.run();
		assertEquals(" ", label.getText(), "Checking text cleared");
		task.cancel();
	}
	
	@AfterEach
	public void end(){}
}
