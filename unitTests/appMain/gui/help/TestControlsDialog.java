package appMain.gui.help;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.help.ControlsDialog.DialogEscaper;
import appUtils.ZabAppSettings;
import util.testUtils.Assert;

public class TestControlsDialog{

	private static ZabGui gui;
	private ControlsDialog dialog;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		gui = new ZabGui();
		gui.setVisible(false);
	}
	
	@BeforeEach
	public void setup(){
		dialog = new ControlsDialog(gui);
	}
	
	@Test
	public void constructor(){
		assertFalse(dialog.isResizable(), "Checking dialog is not resizable");
	}
	
	@Test
	public void getEscaper(){
		assertNotEquals(null, dialog.getEscaper(), "Checking escaper initialized");
		// Checking escaper in the key listeners
		Assert.contains(dialog.getKeyListeners(), dialog.getEscaper());
	}
	
	@Test
	public void keyPressedDialogEscaper(){
		DialogEscaper escape = dialog.getEscaper();
		// Running case of closing
		escape.keyPressed(new KeyEvent(dialog, 0, 0, 0, KeyEvent.VK_ESCAPE, ' '));
		// running case of not closing
		escape.keyPressed(new KeyEvent(dialog, 0, 0, 0, KeyEvent.VK_A, 'a'));
	}
	
	@AfterEach
	public void end(){
		gui.dispose();
	}
	
}
