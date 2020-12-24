package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import appUtils.ZabAppSettings;

public class TestStringUtils{
	
	private String[] combineMain;
	private String[] combineAdd;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		combineMain = new String[]{
			"asd", "qwe", "123", "zxc"
		};
		combineAdd = new String[]{
			"1", "asdf", "car", ""
		};
	}
	
	@Test
	public void combineStringsWithFiller(){
		StringUtils.combineStringsWithFiller(combineMain, combineAdd, "", "", ' ', true);
		assertEquals("asd   1", combineMain[0], "Checking string list filler before");
		assertEquals("qweasdf", combineMain[1], "Checking string list filler before");
		assertEquals("123 car", combineMain[2], "Checking string list filler before");
		assertEquals("zxc    ", combineMain[3], "Checking string list filler before");
		
		setup();
		StringUtils.combineStringsWithFiller(combineMain, combineAdd, "", "", ' ', false);
		assertEquals("asd1   ", combineMain[0], "Checking string list filler after");
		assertEquals("qweasdf", combineMain[1], "Checking string list filler after");
		assertEquals("123car ", combineMain[2], "Checking string list filler after");
		assertEquals("zxc    ", combineMain[3], "Checking string list filler after");
		
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				StringUtils.combineStringsWithFiller(combineMain, new String[2], "", "", ' ', false);
			}
		}, "Checking error thrown with unequal list sizes");
	}
	
	@Test
	public void combineStringWithFiller(){
		assertEquals("nullnullnullnull",
				StringUtils.combineStringWithFiller(null, null, 0, null, null, ' ', false),
				"Checking with null values");
		
		assertEquals("big word(z)",
				StringUtils.combineStringWithFiller("big word", "z", 1, "(", ")", ' ', false),
				"Checking combining, no filler");
		
		assertEquals("big wordz)",
				StringUtils.combineStringWithFiller("big word", "z", 1, "", ")", ' ', false),
				"Checking combining, no before");
		
		assertEquals("big word(z",
				StringUtils.combineStringWithFiller("big word", "z", 1, "(", "", ' ', false),
				"Checking combining, no after");
		
		assertEquals("big word( z)",
				StringUtils.combineStringWithFiller("big word", "z", 2, "(", ")", ' ', true),
				"Checking combining, filler before");
		
		assertEquals("big word(   z)",
				StringUtils.combineStringWithFiller("big word", "z", 4, "(", ")", ' ', true),
				"Checking combining, filler before");
		
		assertEquals("bigger word(zzz)",
				StringUtils.combineStringWithFiller("bigger word", "z", 3, "(", ")", 'z', false),
				"Checking combining, filler after");
		
		assertEquals("bigger word(ssss)",
				StringUtils.combineStringWithFiller("bigger word", "ssss", 3, "(", ")", 'z', false),
				"Checking add longer than fill length");
	}
	
	@AfterEach
	public void end(){}
	
}
