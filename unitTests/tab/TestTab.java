package tab;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.Music;
import music.Pitch;
import music.TimeSignature;
import tab.symbol.TabNote;
import util.testUtils.UtilsTest;

public class TestTab{

	private Tab tab;
	private Tab tabDefault;
	private Tab tabSpecific;
	private ArrayList<TabString> strings;
	private TabString highString;
	private TabString lowString;
	
	@BeforeEach
	public void setup(){
		highString = new TabString(new Pitch(Music.createNote(Music.C, 3)));
		lowString = new TabString(new Pitch(Music.createNote(Music.C, 2)));
		strings = new ArrayList<TabString>();
		strings.add(highString);
		strings.add(lowString);
		tab = new Tab(strings);
		
		tabDefault = new Tab();
		tabSpecific = new Tab(strings, new TimeSignature(3, 2));
	}
	
	@Test
	public void copy(){
		Tab copy = tab.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(tab));
		assertTrue("Checking copy is not the same as the source object", copy != tab);
	}
	
	@Test
	public void getStrings(){
		assertEquals(strings, tab.getStrings(), "Checking strings initialized");
	}
	
	@Test
	public void setStrings(){
		ArrayList<TabString> newStrings = new ArrayList<TabString>();
		tab.setStrings(newStrings);
		assertEquals(newStrings, tab.getStrings(), "Checking strings set");
	}
	
	@Test
	public void getTimeSignature(){
		assertEquals(new TimeSignature(4, 4), tab.getTimeSignature(), "Checking time signature initialized");
		assertEquals(new TimeSignature(4, 4), tabDefault.getTimeSignature(), "Checking time signature initialized");
		assertEquals(new TimeSignature(3, 2), tabSpecific.getTimeSignature(), "Checking time signature initialized");
	}
	
	@Test
	public void setTimeSignature(){
		TimeSignature t = new TimeSignature(5, 4);
		tab.setTimeSignature(t);
		assertEquals(t, tab.getTimeSignature(), "Checking time signature set");
	}
	
	@Test
	public void quantize(){
		lowString.add(new TabNote(0, 1.1));
		highString.add(new TabNote(0, 2.1));
		tab.quantize(1);
		assertEquals(1, lowString.get(0).getPos().getValue(), UtilsTest.DELTA, "Checking string is quantized");
		assertEquals(2, highString.get(0).getPos().getValue(), UtilsTest.DELTA, "Checking string is quantized");
	}
	
	@Test
	public void getRootNote(){
		assertEquals(-12, tab.getRootNote(0), "Checking root note found");
		assertEquals(-24, tab.getRootNote(1), "Checking root note found");
	}
	
	@Test
	public void equals(){
		ArrayList<TabString> s = new ArrayList<TabString>();
		s.add(highString);
		s.add(lowString);
		Tab t = new Tab(s);
		assertFalse("Checking objects are not the same object", t == tab);
		assertTrue("Checking objects are equal", t.equals(tab));
		
		s.remove(lowString);
		assertFalse("Checking objects are not equal", t.equals(tab));
	}
	
	@AfterEach
	public void end(){}
	
}
