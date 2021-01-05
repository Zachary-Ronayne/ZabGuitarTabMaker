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

public class TestTabSymbol{
	
	
	private TabSymbol symbol;
	private TabModifier mod;
	private TabString string;
	
	private TabModifier newMod;
	
	private class TestSymbolObject extends TabSymbol{
		public TestSymbolObject(TabModifier modifier){
			super(modifier);
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
		@Override
		public String toString(){
			return "[Test, " + super.toString() + "]";
		}
	}
	
	@BeforeEach
	public void setup(){
		newMod = new TabModifier("{", "}");
		
		mod = new TabModifier("[", "]");
		string = new TabString(new Pitch(-4));
		symbol = new TestSymbolObject(mod);
	}
	
	@Test
	public void constructor(){
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new TestSymbolObject(null);
			}
		}, "Checking exception is thrown on null modifier");
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
	public void equals(){
		TabSymbol s = new TestSymbolObject(mod);
		assertFalse("Checking objects are not the same object", s == symbol);
		assertTrue("Checking objects are equal", s.equals(symbol));
		
		s.setModifier(new TabModifier("ab", "bc"));
		assertFalse("Checking objects are not equal", s.equals(symbol));

		s.setModifier(new TabModifier("[", "]"));
		assertTrue("Checking objects are equal", s.equals(symbol));
		s.setModifier(new TabModifier("a", "b"));
		assertFalse("Checking objects are not equal", s.equals(symbol));
	}

	@Test
	public void testToString(){
		assertEquals("[Test, [On C4 string, note: \"[A]\"], [TabModifier: \"[\" \"]\"]]", symbol.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
