package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import tab.symbol.TabDeadNote;
import tab.symbol.TabNote;
import tab.symbol.TabNoteRhythm;
import util.testUtils.Assert;

public class TestTabUtils{

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){}
	
	@Test
	public void stringToSymbol(){
		Assert.isInstance(TabDeadNote.class, TabUtils.stringToSymbol("TabDeadNote"), "Checking correct object type is returned for TabDeadNote");
		Assert.isInstance(TabNote.class, TabUtils.stringToSymbol("TabNote"), "Checking correct object type is returned for TabNote");
		Assert.isInstance(TabNoteRhythm.class, TabUtils.stringToSymbol("TabNoteRhythm"), "Checking correct object type is returned for TabNoteRhythm");
		
		assertEquals(null, TabUtils.stringToSymbol("fake class"), "Checking null returned for invalid type");
	}
	
	@AfterEach
	public void end(){}
	
}
