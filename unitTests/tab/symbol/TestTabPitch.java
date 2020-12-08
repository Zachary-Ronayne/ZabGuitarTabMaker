package tab.symbol;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.Pitch;
import tab.TabString;

public class TestTabPitch{

	private Pitch pitch;
	private Pitch newPitch;
	private TabPosition pos;
	private TabModifier mod;
	private TabPitch note;
	
	private TabPitch noteNoMod;
	private TabString string;
	
	@BeforeEach
	public void setup(){
		pitch = new Pitch(4);
		newPitch = new Pitch(2);
		pos = new TabPosition(2);
		mod = new TabModifier("{", "}");
		note = new TabPitch(pitch, pos, mod){};
		
		noteNoMod = new TabPitch(pitch, pos){};
		string = new TabString(new Pitch(4));
	}
	
	@Test
	public void constructor(){
		assertEquals(pos, note.getPos(), "Checking position initialized in full constructor");
		assertEquals(mod, note.getModifier(), "Checking modifier initialized in full constructor");
		
		assertEquals(pos, noteNoMod.getPos(), "Checking position initialized in no modifier constructor");
		assertEquals(null, noteNoMod.getModifier(), "Checking modifier null in no modifier constructor");
	}
	
	@Test
	public void getPitch(){
		assertEquals(pitch, note.getPitch(), "Checking pitch initialized");
	}
	
	@Test
	public void setPitch(){
		note.setPitch(newPitch);
		assertEquals(newPitch, note.getPitch(), "Checking pitch initialized");
		
		note.setPitch(5);
		assertEquals(5, note.getPitch().getNote(), "Checking pitch initialized using integer");
	}
	
	@Test
	public void getSymbol(){
		assertEquals("0", note.getSymbol(string), "Checking correct symbol is found");
		
		note.setPitch(6);
		assertEquals("2", note.getSymbol(string), "Checking correct symbol is found");
		
		note.setPitch(2);
		assertEquals("-2", note.getSymbol(string), "Checking correct symbol is found");
	}
	
	@AfterEach
	public void end(){}
	
}
