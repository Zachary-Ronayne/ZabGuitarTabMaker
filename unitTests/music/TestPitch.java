package music;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPitch{
	
	private Pitch fSharp3;
	private Pitch c4;
	private Pitch c5;
	
	@BeforeEach
	public void setup(){
		fSharp3 = new Pitch(-6);
		c4 = new Pitch(0);
		c5 = new Pitch(12);
	}
	
	@Test
	public void getNote(){
		assertEquals(-6, fSharp3.getNote(), "Checking correct note is initialized");
		assertEquals(0, c4.getNote(), "Checking correct note is initialized");
		assertEquals(12, c5.getNote(), "Checking correct note is initialized");
	}
	
	@Test
	public void setNote(){
		c5.setNote(5);
		assertEquals(5, c5.getNote(), "Checking correct note is set");
	}
	
	@Test
	public void getPitchName(){
		assertEquals(Music.F_SHARP + "3", fSharp3.getPitchName(), "Checking correct note name is found");
		assertEquals(Music.G_FLAT + "3", fSharp3.getPitchName(true), "Checking correct note name is found as flat");
		assertEquals(Music.F_SHARP + "3", fSharp3.getPitchName(false), "Checking correct note name is found as sharp");
	}
	
	@AfterEach
	public void end(){}
	
}
