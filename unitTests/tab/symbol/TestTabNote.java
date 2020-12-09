package tab.symbol;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.Pitch;

public class TestTabNote{

	private TabNote note;
	private TabNote noteNoMod;
	private TabNote noteValues;
	
	private Pitch pitch;
	private TabPosition pos;
	private TabModifier mod;
	
	@BeforeEach
	public void setup(){
		pitch = new Pitch(2);
		pos = new TabPosition(1);
		mod = new TabModifier("(", ")");
		
		note = new TabNote(pitch, pos, mod);
		noteNoMod = new TabNote(pitch, pos);
		noteValues = new TabNote(3, 2);
	}
	
	@Test
	public void constructor(){
		assertEquals(pitch, note.getPitch(), "Checking pitch initialized in full constructor");
		assertEquals(pos, note.getPos(), "Checking position initialized in full constructor");
		assertEquals(mod, note.getModifier(), "Checking modifier initialized in full constructor");
		
		assertEquals(pitch, noteNoMod.getPitch(), "Checking pitch initialized in no modifier constructor");
		assertEquals(pos, noteNoMod.getPos(), "Checking position initialized in no modifier constructor");
		assertEquals(null, noteNoMod.getModifier(), "Checking modifier null in no modifier constructor");
		
		assertEquals(3, noteValues.getPitch().getNote(), "Checking pitch initialized in values constructor");
		assertEquals(2, noteValues.getPos().getValue(), "Checking position initialized in values constructor");
		assertEquals(null, noteValues.getModifier(), "Checking modifier null in values constructor");
	}
	
	@Test
	public void copy(){
		TabNote copy = note.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(note));
		assertTrue("Checking copy is not the same as the source object", copy != note);
	}
	
	@AfterEach
	public void end(){}
	
}
