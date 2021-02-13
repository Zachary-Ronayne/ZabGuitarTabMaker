package appUtils.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestTabControlSettings{

	private static TabControlSettings settings;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		settings = new TabControlSettings();
	}
	
	@BeforeEach
	public void setup(){}

	@Test
	public void moveOverwrite(){
		TestZabSettings.assertSettingInitialized(settings.getMoveOverwrite(), TabControlSettings.MOVE_OVERWRITE);
		assertEquals(TabControlSettings.MOVE_OVERWRITE, settings.moveOverwrite(), "Checking value getter correct");
	}
	@Test
	public void moveDeleteInvalid(){
		TestZabSettings.assertSettingInitialized(settings.getMoveDeleteInvalid(), TabControlSettings.MOVE_DELETE_INVALID);
		assertEquals(TabControlSettings.MOVE_DELETE_INVALID, settings.moveDeleteInvalid(), "Checking value getter correct");
	}
	@Test
	public void moveCancelInvalid(){
		TestZabSettings.assertSettingInitialized(settings.getMoveCancelInvalid(), TabControlSettings.MOVE_CANCEL_INVALID);
		assertEquals(TabControlSettings.MOVE_CANCEL_INVALID, settings.cancelInvalid(), "Checking value getter correct");
	}
	@Test
	public void  pasteOverwrite(){
		TestZabSettings.assertSettingInitialized(settings.getPasteOverwrite(), TabControlSettings.PASTE_OVERWRITE);
		assertEquals(TabControlSettings.PASTE_OVERWRITE, settings. pasteOverwrite(), "Checking value getter correct");
	}
	@Test
	public void pasteCancelInvalid(){
		TestZabSettings.assertSettingInitialized(settings.getPasteCancelInvalid(), TabControlSettings.PASTE_CANCEL_INVALID);
		assertEquals(TabControlSettings.PASTE_CANCEL_INVALID, settings.pasteCancelInvalid(), "Checking value getter correct");
	}
	@Test
	public void zoomFactor(){
		TestZabSettings.assertSettingInitialized(settings.getZoomFactor(), TabControlSettings.ZOOM_FACTOR);
		assertEquals(TabControlSettings.ZOOM_FACTOR, settings.zoomFactor(), "Checking value getter correct");
	}
	@Test
	public void zoomInverted(){
		TestZabSettings.assertSettingInitialized(settings.getZoomInverted(), TabControlSettings.ZOOM_INVERTED);
		assertEquals(TabControlSettings.ZOOM_INVERTED, settings.zoomInverted(), "Checking value getter correct");
	}
	@Test
	public void scrollFactor(){
		TestZabSettings.assertSettingInitialized(settings.getScrollFactor(), TabControlSettings.SCROLL_FACTOR);
		assertEquals(TabControlSettings.SCROLL_FACTOR, settings.scrollFactor(), "Checking value getter correct");
	}
	@Test
	public void scrollXInverted(){
		TestZabSettings.assertSettingInitialized(settings.getScrollXInverted(), TabControlSettings.SCROLL_X_INVERTED);
		assertEquals(TabControlSettings.SCROLL_X_INVERTED, settings.scrollXInverted(), "Checking value getter correct");
	}
	@Test
	public void scrollYInverted(){
		TestZabSettings.assertSettingInitialized(settings.getScrollYInverted(), TabControlSettings.SCROLL_Y_INVERTED);
		assertEquals(TabControlSettings.SCROLL_Y_INVERTED, settings.scrollYInverted(), "Checking value getter correct");
	}
	
	@AfterEach
	public void end(){}
}
