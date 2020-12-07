package tab.symbol;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTabSymbol{
	
	
	private TabSymbol symbol;
	private TabPosition pos;
	private TabModifier mod;
	
	private TabPosition newPos;
	private TabModifier newMod;
	
	@BeforeEach
	public void setup(){
		newPos = new TabPosition(5);
		newMod = new TabModifier("{", "}");
		pos = new TabPosition(3);
		mod = new TabModifier("[", "]");
		symbol = new TabNote(pos, mod);
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
		assertEquals("[]", symbol.getModifiedSymbol(null), "Checking modified symbol obtained");
		
		symbol.setModifier(newMod);
		assertEquals("{}", symbol.getModifiedSymbol(null), "Checking modified symbol obtained after changing fields");
	}
	
	@AfterEach
	public void end(){}
	
}
