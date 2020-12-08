package tab.symbol;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.Pitch;
import tab.TabString;

public class TestTabSymbol{
	
	
	private TabSymbol symbol;
	private TabPosition pos;
	private TabModifier mod;
	private TabString string;
	
	private TabPosition newPos;
	private TabModifier newMod;
	
	@BeforeEach
	public void setup(){
		newPos = new TabPosition(5);
		newMod = new TabModifier("{", "}");
		
		pos = new TabPosition(3);
		mod = new TabModifier("[", "]");
		string = new TabString(new Pitch(-4));
		symbol = new TabSymbol(pos, mod){
			@Override
			public String getSymbol(TabString string){return "A";}
		};
	}
	
	@Test
	public void getPos(){
		assertEquals(pos, symbol.getPos(), "Checking position initialized");
	}
	
	@Test
	public void setPos(){
		symbol.setPos(newPos);
		assertEquals(newPos, symbol.getPos(), "Checking position set");
	}
	
	@Test
	public void getModifier(){
		assertEquals(mod, symbol.getModifier(), "Checking modifier initialized");
	}
	
	@Test
	public void setModifier(){
		symbol.setModifier(newMod);
		assertEquals(newMod, symbol.getModifier(), "Checking modifier set");
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
		
		c.setPos(new TabPosition(4));
		assertTrue("Pos 4 should compare greater than 0 for pos 3", c.compareTo(symbol) > 0);

		c.setPos(new TabPosition(3));
		assertTrue("Pos 3 should compare equal to 0 for pos 3", c.compareTo(symbol) == 0);
	}
	
	@AfterEach
	public void end(){}
	
}
