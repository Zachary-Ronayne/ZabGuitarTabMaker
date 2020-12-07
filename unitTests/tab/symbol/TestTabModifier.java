package tab.symbol;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTabModifier{

	private TabModifier mod;
	
	@BeforeEach
	public void setup(){
		mod = new TabModifier("(", ")");
	}
	
	@Test
	public void getBefore(){
		assertEquals("(", mod.getBefore(), "Checking before iniitalized");
	}
	
	@Test
	public void setBefore(){
		mod.setBefore("<");
		assertEquals("<", mod.getBefore(), "Checking before set");
	}
	
	@Test
	public void getAfter(){
		assertEquals(")", mod.getAfter(), "Checking after iniitalized");
	}
	
	@Test
	public void setAfter(){
		mod.setAfter(">");
		assertEquals(">", mod.getAfter(), "Checking after set");
	}
	
	@Test
	public void modifySymbol(){
		assertEquals("()", mod.modifySymbol(""), "Checking modifying an empty string");
		assertEquals("(C3)", mod.modifySymbol("C3"), "Checking modifying a note");
		assertEquals("(3)", mod.modifySymbol("3"), "Checking modifying a number");
	}
	
	@AfterEach
	public void end(){}
	
}
