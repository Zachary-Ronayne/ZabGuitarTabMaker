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

public class TestTabSymbol{
	
	
	private TabSymbol symbol;
	private NotePosition pos;
	private TabModifier mod;
	private TabString string;
	
	private NotePosition newPos;
	private TabModifier newMod;
	
	private class TestT extends TabSymbol{
		public TestT(NotePosition pos, TabModifier modifier){
			super(pos, modifier);
		}
		@Override
		public String getSymbol(TabString string){return "A";}
		@Override
		public TabSymbol copy(){return this;}
	}
	
	@BeforeEach
	public void setup(){
		newPos = new NotePosition(5);
		newMod = new TabModifier("{", "}");
		
		pos = new NotePosition(3);
		mod = new TabModifier("[", "]");
		string = new TabString(new Pitch(-4));
		symbol = new TestT(pos, mod);
	}
	
	@Test
	public void constructor(){
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new TestT(null, mod);
			}
		}, "Checking exception is thrown on null position");
		
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new TestT(pos, null);
			}
		}, "Checking exception is thrown on null modifier");
	}
	
	@Test
	public void getPos(){
		assertEquals(pos, symbol.getPos(), "Checking position initialized");
	}
	
	@Test
	public void setPos(){
		symbol.setPos(newPos);
		assertEquals(newPos, symbol.getPos(), "Checking position set");
		
		symbol.setPos(null);
		assertEquals(newPos, symbol.getPos(), "Checking position not changed to null");
	}
	
	@Test
	public void getModifier(){
		assertEquals(mod, symbol.getModifier(), "Checking modifier initialized");
	}
	
	@Test
	public void setModifier(){
		symbol.setModifier(newMod);
		assertEquals(newMod, symbol.getModifier(), "Checking modifier set");
		
		symbol.setModifier(null);
		assertEquals(newMod, symbol.getModifier(), "Checking modifier not chaNged to null");
	}
	
	@Test
	public void getModifiedSymbol(){
		assertEquals("[A]", symbol.getModifiedSymbol(string), "Checking modified symbol obtained");
		
		symbol.setModifier(newMod);
		assertEquals("{A}", symbol.getModifiedSymbol(string), "Checking modified symbol obtained after changing fields");
	}
	
	@Test
	public void compareTo(){
		TabSymbol c = new TabNote(0, 0);
		assertTrue("Pos 0 should compare less than 0 for pos 3", c.compareTo(symbol) < 0);
		
		c.setPos(new NotePosition(4));
		assertTrue("Pos 4 should compare greater than 0 for pos 3", c.compareTo(symbol) > 0);

		c.setPos(new NotePosition(3));
		assertTrue("Pos 3 should compare equal to 0 for pos 3", c.compareTo(symbol) == 0);
	}

	@Test
	public void equals(){
		TabSymbol s = new TestT(pos, mod);
		assertFalse("Checking objects are not the same object", s == symbol);
		assertTrue("Checking objects are equal", s.equals(symbol));
		
		s.setPos(new NotePosition(4));
		assertFalse("Checking objects are not equal", s.equals(symbol));

		s.setPos(pos);
		assertTrue("Checking objects are equal", s.equals(symbol));
		s.setModifier(new TabModifier("a", "b"));
		assertFalse("Checking objects are not equal", s.equals(symbol));
	}
	
	@AfterEach
	public void end(){}
	
}
