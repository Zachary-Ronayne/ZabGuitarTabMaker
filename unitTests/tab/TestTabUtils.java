package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.NotePosition;
import music.Rhythm;
import tab.symbol.TabDeadNote;
import tab.symbol.TabNote;
import tab.symbol.TabNoteRhythm;

public class TestTabUtils{

	@BeforeEach
	public void setup(){}
	
	@Test
	public void stringToSymbol(){
		assertEquals(new TabDeadNote(new NotePosition(0)).getClass().getName(),
				TabUtils.stringToSymbol("TabDeadNote").getClass().getName(),
				"Checking correct object type is returned for TabDeadNote");
		
		assertEquals(new TabNote(0, 0).getClass().getName(),
				TabUtils.stringToSymbol("TabNote").getClass().getName(),
				"Checking correct object type is returned for TabNote");
		
		assertEquals(new TabNoteRhythm(0, new Rhythm(1, 1), 0).getClass().getName(),
				TabUtils.stringToSymbol("TabNoteRhythm").getClass().getName(),
				"Checking correct object type is returned for TabNoteRhythm");
		
		assertEquals(null, TabUtils.stringToSymbol("fake class"), "Checking null returned for invalid type");
	}
	
	@AfterEach
	public void end(){}
	
}