package tab.symbol;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTabPosition{
	
	private TabPosition pos;
	
	@BeforeEach
	public void setup(){
		pos = new TabPosition(3);
	}
	
	@Test
	public void getPosition(){
		assertEquals(3, pos.getValue(), "Checking value is initialized");
	}
	
	@Test
	public void setPosition(){
		pos.setPosition(2);
		assertEquals(2, pos.getValue(), "Checking value is set");
	}
	
	@AfterEach
	public void end(){}
	
}
