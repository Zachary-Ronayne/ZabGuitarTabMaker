package tab.symbol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
		TabPitch n = new TestPitchObject(pitch, mod);
		
		assertFalse("Checking objects are not the same object", n == note);
		assertTrue("Checking objects are equal", n.equals(note));
		
		n.setPitch(new Pitch(0));
		assertFalse("Checking objects are not equal", n.equals(note));
	}
	
	@AfterEach
	public void end(){}
	
}
