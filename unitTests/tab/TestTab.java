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

public class TestTab{

	private Tab tab;
	private ArrayList<TabString> strings;
	private TabString lowString;
	private TabString highString;
	
	@BeforeEach
	public void setup(){
		lowString = new TabString(new Pitch(Music.createNote(Music.C, 2)));
		highString = new TabString(new Pitch(Music.createNote(Music.C, 3)));
		strings = new ArrayList<TabString>();
		strings.add(lowString);
		strings.add(highString);
		tab = new Tab(strings);
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
	public void equals(){
		ArrayList<TabString> s = new ArrayList<TabString>();
		s.add(lowString);
		s.add(highString);
		Tab t = new Tab(s);
		assertFalse("Checking objects are not the same object", t == tab);
		assertTrue("Checking objects are equal", t.equals(tab));
		
		s.remove(lowString);
		assertFalse("Checking objects are not equal", t.equals(tab));
	}
	
	@AfterEach
	public void end(){}
	
}
