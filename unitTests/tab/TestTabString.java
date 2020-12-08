package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.Pitch;
import tab.symbol.TabNote;

public class TestTabString{
	
	private TabString string;
	private Pitch pitch;
	private Pitch newPitch;
	private Pitch negPitch;
	
	private TabNote[] notes;
	
	@BeforeEach
	public void setup(){
		newPitch = new Pitch(0);
		pitch = new Pitch(4);
		string = new TabString(pitch);
		
		notes = new TabNote[]{
				new TabNote(4, 0),
				new TabNote(7, 1),
				new TabNote(9, 2),
				new TabNote(4, 3),
				new TabNote(7, 4),
				new TabNote(10, 5),
				new TabNote(9, 6),
		};
		negPitch = new Pitch(-3);
	}
	
	@Test
	public void getRootPitch(){
		assertEquals(pitch, string.getRootPitch(), "Checking root pitch initialized");
	}
	
	@Test
	public void setRootPitch(){
		string.setRootPitch(newPitch);
		assertEquals(newPitch, string.getRootPitch(), "Checking root pitch initialized");
	}
	
	@Test
	public void add(){
		// Adding notes in arbitrary order
		string.add(notes[4]);
		string.add(notes[6]);
		string.add(notes[2]);
		string.add(notes[0]);
		string.add(notes[5]);
		string.add(notes[1]);
		string.add(notes[3]);
		
		assertEquals(notes[0], string.get(0), "Checking notes are added in sorted order");
		assertEquals(notes[1], string.get(1), "Checking notes are added in sorted order");
		assertEquals(notes[2], string.get(2), "Checking notes are added in sorted order");
		assertEquals(notes[3], string.get(3), "Checking notes are added in sorted order");
		assertEquals(notes[4], string.get(4), "Checking notes are added in sorted order");
		assertEquals(notes[5], string.get(5), "Checking notes are added in sorted order");
		assertEquals(notes[6], string.get(6), "Checking notes are added in sorted order");
	}
	
	@Test
	public void getTabNumber(){
		assertEquals(0, string.getTabNumber(notes[0]), "Checking tab number is correctly found");
		assertEquals(3, string.getTabNumber(notes[1]), "Checking tab number is correctly found");
		assertEquals(5, string.getTabNumber(notes[2]), "Checking tab number is correctly found");
		assertEquals(0, string.getTabNumber(notes[3]), "Checking tab number is correctly found");
		assertEquals(3, string.getTabNumber(notes[4]), "Checking tab number is correctly found");
		assertEquals(6, string.getTabNumber(notes[5]), "Checking tab number is correctly found");
		assertEquals(5, string.getTabNumber(notes[6]), "Checking tab number is correctly found");
		
		assertEquals(-7, string.getTabNumber(negPitch), "Checking negative tab number is correctly found");
	}
	
	@AfterEach
	public void end(){}
	
}
