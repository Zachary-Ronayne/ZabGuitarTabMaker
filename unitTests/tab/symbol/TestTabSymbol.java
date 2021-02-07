package tab.symbol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import appUtils.ZabAppSettings;
import music.Pitch;
import music.Rhythm;
import tab.ModifierFactory;
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
		public TabSymbol movingToNewString(TabString oldStr, TabString newStr){return this.copy();}
		@Override
		public boolean load(Scanner reader){return false;}
		@Override
		public boolean save(PrintWriter writer){return false;}
		@Override
		public String toString(){
			return "[Test, " + super.toString() + "]";
		}
	}

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
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
	public void copyNewModifier(){
		symbol = symbol.copyNewModifier(newMod);
		assertEquals(newMod, symbol.getModifier(), "Checking modifier set");

		assertEquals(null, symbol.copyNewModifier(null), "Checking giving modifier null returns null");
	}
	
	@Test
	public void copyAddModifier(){
		symbol = new TestSymbolObject(new TabModifier("a", ""));
		symbol = symbol.copyAddModifier(new TabModifier("b", "d"));
		assertEquals(new TabModifier("a", "d"), symbol.getModifier(), "Checking modifier added correctly");
		
		symbol = new TestSymbolObject(new TabModifier("", ""));
		symbol = symbol.copyAddModifier(new TabModifier("b", "d"));
		assertEquals(new TabModifier("b", "d"), symbol.getModifier(), "Checking modifier added correctly");
		
		assertEquals(null, symbol.copyAddModifier(null), "Checking null modifier returns null");
	}
	
	@Test
	public void createPitchNote(){
		symbol = new TestSymbolObject(ModifierFactory.bend());
		TabPitch newSymbol = symbol.createPitchNote(new Pitch(2));
		assertFalse(symbol == newSymbol, "Checking the new symbol is not the same object");
		assertEquals(2, newSymbol.getPitch().getNote(), "Checking correct pitch set");
		assertEquals(ModifierFactory.bend(), newSymbol.getModifier(), "Checking modifier is the same");
	}
	
	@Test
	public void getModifiedSymbol(){
		assertEquals("[A]", symbol.getModifiedSymbol(string), "Checking modified symbol obtained");
		
		symbol = new TestSymbolObject(newMod);
		assertEquals("{A}", symbol.getModifiedSymbol(string), "Checking modified symbol obtained after changing fields");
	}

	@Test
	public void equals(){
		assertTrue(symbol.equals(symbol), "Checking symbol equals itself");
		assertFalse(symbol.equals(null), "Checking symbol does not equal null");
		
		TabSymbol s = new TestSymbolObject(mod);
		assertFalse(s == symbol, "Checking objects are not the same object");
		assertTrue(s.equals(symbol), "Checking objects are equal");
		
		s = new TestSymbolObject(new TabModifier("ab", "bc"));
		assertFalse(s.equals(symbol), "Checking objects are not equal");

		s = new TestSymbolObject(new TabModifier("[", "]"));
		assertTrue(s.equals(symbol), "Checking objects are equal");
		s = new TestSymbolObject(new TabModifier("a", "b"));
		assertFalse(s.equals(symbol), "Checking objects are not equal");
	}

	@Test
	public void testToString(){
		assertEquals("[Test, [On C4 string, note: \"[A]\"], [TabModifier: \"[\" \"]\"]]", symbol.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
