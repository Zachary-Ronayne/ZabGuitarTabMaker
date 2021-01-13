package tab.symbol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import music.Pitch;
import music.Rhythm;
import tab.TabString;

public class TestTabPitch{

	private Pitch pitch;
	private Pitch newPitch;
	private TabModifier mod;
	private TabPitch note;
	
	private TabPitch noteNoMod;
	private TabString string;
	
	private class TestPitchObject extends TabPitch{
		public TestPitchObject(Pitch pitch, TabModifier mod){
			super(pitch, mod);
		}
		public TestPitchObject(Pitch pitch){
			super(pitch);
		}
		@Override
		public TabSymbol copy(){return this;}
		@Override
		public TabSymbol convertToRhythm(Rhythm r){return this;}
		@Override
		public TabSymbol removeRhythm(){return this;}
		@Override
		public boolean usesRhythm(){return false;}
		@Override
		public boolean load(Scanner reader){return false;}
		@Override
		public boolean save(PrintWriter writer){return false;}
		@Override
		public String toString(){
			return "[Test, " + super.toString() + "]";
		}
	}
	
	@BeforeEach
	public void setup(){
		pitch = new Pitch(4);
		newPitch = new Pitch(2);
		mod = new TabModifier("{", "}");
		note = new TestPitchObject(pitch, mod);
		
		noteNoMod = new TestPitchObject(pitch);
		string = new TabString(new Pitch(4));
	}
	
	@Test
	public void constructor(){
		assertEquals(mod, note.getModifier(), "Checking modifier initialized in full constructor");
		
		assertEquals(new TabModifier(), noteNoMod.getModifier(), "Checking modifier initialized in no modifier constructor");
		
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new TestPitchObject(null, mod);
			}
		}, "Checking exception is thrown on null pitch");
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
		
		note.setPitch(null);
		assertEquals(5, note.getPitch().getNote(), "Checking pitch not changed with null parameter");
	}
	
	@Test
	public void getSymbol(){
		assertEquals("0", note.getSymbol(string), "Checking correct symbol is found");
		
		note.setPitch(6);
		assertEquals("2", note.getSymbol(string), "Checking correct symbol is found");
		
		note.setPitch(2);
		assertEquals("-2", note.getSymbol(string), "Checking correct symbol is found");
	}
	
	@Test
	public void equals(){
		assertTrue(note.equals(note), "Checking note equals itself");
		assertFalse(note.equals(null), "Checking note does not equal null");
		
		TabPitch n = new TestPitchObject(pitch, mod);
		
		assertFalse(n == note, "Checking objects are not the same object");
		assertTrue(n.equals(note), "Checking objects are equal");
		
		n.setPitch(new Pitch(0));
		assertFalse(n.equals(note), "Checking objects are not equal");
		
		n = new TestPitchObject(newPitch, mod);
		assertFalse(note.equals(n), "Checking note not equal with different modifiers");
	}

	@Test
	public void testToString(){
		assertEquals(""
				+ "[Test, [On C4 string, note: \"{4}\"], "
				+ "[TabModifier: \"{\" \"}\"], "
				+ "[Pitch: E4]]", note.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
