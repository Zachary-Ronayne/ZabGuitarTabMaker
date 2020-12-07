package tab.symbol;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTabDeadNote{
	
	private TabDeadNote note;
	private TabPosition pos;
	
	@BeforeEach
	public void setup(){
		pos = new TabPosition(4);
		note = new TabDeadNote(pos);
	}
	
	@Test
	public void constructor(){
		assertEquals(pos, note.getPos(), "Chekcing position initialized");
		assertEquals(null, note.getModifier(), "Chekcing modifier initialized");
	}
	
	@Test
	public void getSymbol(){
		assertEquals("X", note.getSymbol(null), "Checking symbol is obtained");
	}
	
	@AfterEach
	public void end(){}
	
}
