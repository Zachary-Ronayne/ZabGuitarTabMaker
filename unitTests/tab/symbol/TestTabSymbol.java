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

import music.NotePosition;
import music.Pitch;
import music.Rhythm;
import music.TimeSignature;
import tab.TabString;

public class TestTabSymbol{
	
	
	private TabSymbol symbol;
	private NotePosition pos;
	private TabModifier mod;
	private TabString string;
	
	private NotePosition newPos;
	private TabModifier newMod;
	
	private TimeSignature four4;
	private TimeSignature five8;
	
	private class TestT extends TabSymbol{
		public TestT(NotePosition pos, TabModifier modifier){
			super(pos, modifier);
		}
		@Override
		public String getSymbol(TabString string){return "A";}
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
		newPos = new NotePosition(5);
		newMod = new TabModifier("{", "}");
		
		pos = new NotePosition(3);
		mod = new TabModifier("[", "]");
		string = new TabString(new Pitch(-4));
		symbol = new TestT(pos, mod);
		
		four4 = new TimeSignature(4, 4);
		five8 = new TimeSignature(5, 8);
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
	public void getPosition(){
		assertEquals(pos, symbol.getPosition(), "Checking position initialized");
	}
	
	@Test
	public void getPos(){
		assertEquals(3, symbol.getPos(), "Checking position initialized");
	}
	
	@Test
	public void setPosition(){
		symbol.setPosition(newPos);
		assertEquals(newPos, symbol.getPosition(), "Checking position set");
		
		symbol.setPosition(null);
		assertEquals(newPos, symbol.getPosition(), "Checking position not changed to null");
	}
	
	@Test
	public void setPos(){
		symbol.setPos(8);
		assertEquals(8, symbol.getPos(), "Checking position set");
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
	public void quantize(){
		symbol.setPos(4.01);
		symbol.quantize(four4, 4);
		assertEquals(4, symbol.getPos(), "Checking symbol is quantized");
	}
	
	@Test
	public void retime(){
		symbol.setPos(2.25);
		symbol.retime(five8, four4);
		assertEquals(3.6, symbol.getPos(), "Checking symbol is retimed to new measure");
	}
	
	@Test
	public void retimeMeasure(){ 
		symbol.setPos(2.25);
		symbol.retimeMeasure(five8, four4);
		assertEquals(2.4, symbol.getPos(), "Checking symbol is retimed to the same measure");
	}
	
	@Test
	public void compareTo(){
		TabSymbol c = new TabNote(0, 0);
		assertTrue("Pos 0 should compare less than 0 for pos 3", c.compareTo(symbol) < 0);
		
		c.setPosition(new NotePosition(4));
		assertTrue("Pos 4 should compare greater than 0 for pos 3", c.compareTo(symbol) > 0);

		c.setPosition(new NotePosition(3));
		assertTrue("Pos 3 should compare equal to 0 for pos 3", c.compareTo(symbol) == 0);
	}

	@Test
	public void equals(){
		TabSymbol s = new TestT(pos, mod);
		assertFalse("Checking objects are not the same object", s == symbol);
		assertTrue("Checking objects are equal", s.equals(symbol));
		
		s.setPosition(new NotePosition(4));
		assertFalse("Checking objects are not equal", s.equals(symbol));

		s.setPosition(pos);
		assertTrue("Checking objects are equal", s.equals(symbol));
		s.setModifier(new TabModifier("a", "b"));
		assertFalse("Checking objects are not equal", s.equals(symbol));
	}
	
	@AfterEach
	public void end(){}
	
}
