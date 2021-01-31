package appUtils.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestTabPaintSettings{

	private static TabPaintSettings settings;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		settings = new TabPaintSettings();
	}
	
	@BeforeEach
	public void setup(){}
	@Test
	public void baseX(){
		TestZabSettings.assertSettingInitialized(settings.getBaseX(), TabPaintSettings.BASE_X);
		assertEquals(TabPaintSettings.BASE_X, settings.baseX(), "Checking value getter correct");
	}
	@Test
	public void baseY(){
		TestZabSettings.assertSettingInitialized(settings.getBaseY(), TabPaintSettings.BASE_Y);
		assertEquals(TabPaintSettings.BASE_Y, settings.baseY(), "Checking value getter correct");
	}
	@Test
	public void measureWidth(){
		TestZabSettings.assertSettingInitialized(settings.getMeasureWidth(), TabPaintSettings.MEASURE_WIDTH);
		assertEquals(TabPaintSettings.MEASURE_WIDTH, settings.measureWidth(), "Checking value getter correct");
	}
	@Test
	public void lineMeasures(){
		TestZabSettings.assertSettingInitialized(settings.getLineMeasures(), TabPaintSettings.LINE_MEASURES);
		assertEquals(TabPaintSettings.LINE_MEASURES, settings.lineMeasures(), "Checking value getter correct");
	}
	@Test
	public void stringSpace(){
		TestZabSettings.assertSettingInitialized(settings.getStringSpace(), TabPaintSettings.STRING_SPACE);
		assertEquals(TabPaintSettings.STRING_SPACE, settings.stringSpace(), "Checking value getter correct");
	}
	@Test
	public void selectionBuffer(){
		TestZabSettings.assertSettingInitialized(settings.getSelectionBuffer(), TabPaintSettings.SELECTION_BUFFER);
		assertEquals(TabPaintSettings.SELECTION_BUFFER, settings.selectionBuffer(), "Checking value getter correct");
	}
	@Test
	public void aboveSpace(){
		TestZabSettings.assertSettingInitialized(settings.getAboveSpace(), TabPaintSettings.ABOVE_SPACE);
		assertEquals(TabPaintSettings.ABOVE_SPACE, settings.aboveSpace(), "Checking value getter correct");
	}
	@Test
	public void belowSpace(){
		TestZabSettings.assertSettingInitialized(settings.getBelowSpace(), TabPaintSettings.BELOW_SPACE);
		assertEquals(TabPaintSettings.BELOW_SPACE, settings.belowSpace(), "Checking value getter correct");
	}
	@Test
	public void symbolScaleMode(){
		TestZabSettings.assertSettingInitialized(settings.getSymbolScaleMode(), TabPaintSettings.SYMBOL_SCALE_MODE);
		assertEquals(TabPaintSettings.SYMBOL_SCALE_MODE, settings.symbolScaleMode(), "Checking value getter correct");
	}
	@Test
	public void symbolXAlign(){
		TestZabSettings.assertSettingInitialized(settings.getSymbolXAlign(), TabPaintSettings.SYMBOL_X_ALIGN);
		assertEquals(TabPaintSettings.SYMBOL_X_ALIGN, settings.symbolXAlign(), "Checking value getter correct");
	}
	@Test
	public void symbolYAlign(){
		TestZabSettings.assertSettingInitialized(settings.getSymbolYAlign(), TabPaintSettings.SYMBOL_Y_ALIGN);
		assertEquals(TabPaintSettings.SYMBOL_Y_ALIGN, settings.symbolYAlign(), "Checking value getter correct");
	}
	@Test
	public void stringLabelScaleMode(){
		TestZabSettings.assertSettingInitialized(settings.getStringLabelScaleMode(), TabPaintSettings.STRING_LABEL_SCALE_MODE);
		assertEquals(TabPaintSettings.STRING_LABEL_SCALE_MODE, settings.stringLabelScaleMode(), "Checking value getter correct");
	}
	@Test
	public void stringLabelXAlign(){
		TestZabSettings.assertSettingInitialized(settings.getStringLabelXAlign(), TabPaintSettings.STRING_LABEL_X_ALIGN);
		assertEquals(TabPaintSettings.STRING_LABEL_X_ALIGN, settings.stringLabelXAlign(), "Checking value getter correct");
	}
	@Test
	public void stringLabelYAlign(){
		TestZabSettings.assertSettingInitialized(settings.getStringLabelYAlign(), TabPaintSettings.STRING_LABEL_Y_ALIGN);
		assertEquals(TabPaintSettings.STRING_LABEL_Y_ALIGN, settings.stringLabelYAlign(), "Checking value getter correct");
	}
	@Test
	public void symbolBorderSize(){
		TestZabSettings.assertSettingInitialized(settings.getSymbolBorderSize(), TabPaintSettings.SYMBOL_BORDER_SIZE);
		assertEquals(TabPaintSettings.SYMBOL_BORDER_SIZE, settings.symbolBorderSize(), "Checking value getter correct");
	}
	@Test
	public void stringLabelSpace(){
		TestZabSettings.assertSettingInitialized(settings.getStringLabelSpace(), TabPaintSettings.STRING_LABEL_SPACE);
		assertEquals(TabPaintSettings.STRING_LABEL_SPACE, settings.stringLabelSpace(), "Checking value getter correct");
	}
	
	@AfterEach
	public void end(){}
}
