package appUtils.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import settings.Setting;
import util.testUtils.Assert;

public class TestZabSettings{

	private ZabSettings settings;

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		settings = new ZabSettings();
	}
	
	/**
	 * Utility method for asserting that a setting is set up correctly
	 * @param s The setting to check
	 * @param value The expected value of the setting for the current value and default
	 */
	public static void assertSettingInitialized(Setting<?> s, Object value){
		assertEquals(value, s.get(), "Checking value set");
		assertEquals(value, s.getDefault(), "Checking default value set");
		Assert.contains(ZabAppSettings.get().getAll(), s);
	}
	
	@Test
	public void constructor(){
		// Checking all symbol settings are contained
		Assert.contains(settings.getAll(), settings.symbol().getAll().toArray());
		// Checking all text settings are contained
		Assert.contains(settings.getAll(), settings.text().getAll().toArray());
		// Checking all paint settings are contained
		Assert.contains(settings.getAll(), settings.paint().getAll().toArray());
		// Checking all control settings are contained
		Assert.contains(settings.getAll(), settings.control().getAll().toArray());
		// Checking all tab settings are contained
		Assert.contains(settings.getAll(), settings.tab().getAll().toArray());
	}
	
	@Test
	public void symbol(){
		assertNotEquals(null, settings.symbol(), "Checking symbol settings initialized");
	}
	
	@Test
	public void text(){
		assertNotEquals(null, settings.text(), "Checking text settings initialized");
	}
	
	@Test
	public void paint(){
		assertNotEquals(null, settings.paint(), "Checking paint settings initialized");
	}
	
	@Test
	public void control(){
		assertNotEquals(null, settings.control(), "Checking control settings initialized");
	}
	
	@Test
	public void tab(){
		assertNotEquals(null, settings.tab(), "Checking tab settings initialized");
	}

	@AfterEach
	public void end(){}

}
