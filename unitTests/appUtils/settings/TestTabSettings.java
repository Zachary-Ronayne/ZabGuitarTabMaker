package appUtils.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestTabSettings{

	private static TabSettings settings;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		settings = new TabSettings();
	}
	
	@BeforeEach
	public void setup(){}

	@Test
	public void quantizeDivisor(){
		TestZabSettings.assertSettingInitialized(settings.getQuantizeDivisor(), TabSettings.QUANTIZE_DIVISOR);
		assertEquals(TabSettings.QUANTIZE_DIVISOR, settings.quantizeDivisor(), "Checking value getter correct");
	}
	@Test
	public void rhythmConversionEndValue(){
		TestZabSettings.assertSettingInitialized(settings.getRhythmConversionEndValue(), TabSettings.RHYTHM_CONVERSION_END_VALUE);
		assertEquals(TabSettings.RHYTHM_CONVERSION_END_VALUE, settings.rhythmConversionEndValue(), "Checking value getter correct");
	}
	
	@AfterEach
	public void end(){}
}
