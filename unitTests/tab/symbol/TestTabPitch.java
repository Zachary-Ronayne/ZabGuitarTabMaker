package tab.symbol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import music.NotePosition;
import music.Pitch;
import tab.TabString;

public class TestTabPitch{

	private Pitch pitch;
	private Pitch newPitch;
	private NotePosition pos;
	private TabModifier mod;
	private TabPitch note;
	
	private TabPitch noteNoMod;
	private TabString string;
	
	private class TestT extends TabPitch{
		public TestT(Pitch pitch, NotePosition pos, TabModifier mod){
			super(pitch, pos, mod);
		}
		@Override
		public TabSymbol copy(){return this;}
	}
	
	@BeforeEach
	public void setup(){
		pitch = new Pitch(4);
		newPitch = new Pitch(2);
		pos = new NotePosition(2);
		mod = new TabModifier("{", "}");
		note = new TestT(pitch, pos, mod);
		
		noteNoMod = new TabPitch(pitch, pos){
			@Override
			public TabSymbol copy(){return this;}
		};
		string = new TabString(new Pitch(4));
	}
	
	@Test
	public void constructor(){
		assertEquals(pos, note.getPos(), "Checking position initialized in full constructor");
		assertEquals(mod, note.getModifier(), "Checking modifier initialized in full constructor");
		
		assertEquals(pos, noteNoMod.getPos(), "Checking position initialized in no modifier constructor");
		assertEquals(new TabModifier(), noteNoMod.getModifier(), "Checking modifier initialized in no modifier constructor");
		
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new TestT(null, pos, mod);
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
		TabPitch n = new TestT(pitch, pos, mod);
		
		assertFalse("Checking objects are not the same object", n == note);
		assertTrue("Checking objects are equal", n.equals(note));
		
		n.setPitch(new Pitch(0));
		assertFalse("Checking objects are not equal", n.equals(note));
	}
	
	@AfterEach
	public void end(){}
	
}
