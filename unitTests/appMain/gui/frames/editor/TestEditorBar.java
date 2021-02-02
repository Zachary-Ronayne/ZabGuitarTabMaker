package appMain.gui.frames.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import javax.swing.BoxLayout;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.comp.ZabLabel;
import appUtils.ZabAppSettings;
import util.testUtils.Assert;

public class TestEditorBar{

	private EditorBar bar;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		bar = new EditorBar();
	}
	
	@Test
	public void constructor(){
		assertEquals(BoxLayout.X_AXIS, ((BoxLayout)bar.getLayout()).getAxis(), "Checking bar has correct layout");
	}
	
	@Test
	public void getFileStatusLab(){
		ZabLabel lab = bar.getFileStatusLab();
		assertNotEquals(null, lab, "Checking label initialized");
		
		// Checking label is in the bar
		Assert.contains(bar.getComponents(), lab);
	}
	
	
	@AfterEach
	public void end(){}
	
}
