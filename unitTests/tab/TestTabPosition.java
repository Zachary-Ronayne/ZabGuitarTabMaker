package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import appUtils.ZabAppSettings;
import music.NotePosition;
import music.Pitch;
import music.TimeSignature;
import tab.symbol.TabModifier;
import tab.symbol.TabNote;
import tab.symbol.TabSymbol;
import util.Saveable;
import util.testUtils.UtilsTest;

public class TestTabPosition{

	private TabPosition note;
	private TabSymbol symbol;
	private NotePosition pos;

	private TimeSignature four4;
	private TimeSignature five8;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		symbol = new TabNote(2);
		pos = new NotePosition(3);
		note = new TabPosition(symbol, pos);
		
		four4 = new TimeSignature(4, 4);
		five8 = new TimeSignature(5, 8);
	}
	
	@Test
	public void constructor(){
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new TabPosition(null, 0);
			}
		}, "Checking for error on null symbol");
		
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new TabPosition(symbol, null);
			}
		}, "Checking for error on null position");
	}
	
	@Test
	public void copy(){
		TabPosition copy = note.copy();
		assertTrue(copy.equals(note), "Checking the copies are equal");
		assertFalse(copy == note, "Checking the copies are not the same object");
	}
	
	@Test
	public void getSymbol(){
		assertEquals(symbol, note.getSymbol(), "Checking symbol initialized");
	}
	
	@Test
	public void copySymbol(){
		TabPosition newPos = note.copySymbol(new TabNote(1));
		assertFalse(note == newPos, "Checking new tab position is not the original object");
		assertEquals(new TabNote(1), newPos.getSymbol(), "Checking correct symbol set");
		assertEquals(symbol, note.getSymbol(), "Checking old symbol unchanged");
		assertEquals(pos, newPos.getPosition(), "Checking new position set");
		assertEquals(pos, note.getPosition(), "Checking old position unchanged");
	}
	
	@Test
	public void getPosition(){
		assertEquals(pos, note.getPosition(), "Checking position initialized");
	}
	
	@Test
	public void getPos(){
		assertEquals(3, note.getPos(), "Checking position value obtained");
	}
	
	@Test
	public void copyPosition(){
		TabPosition newPos = note.copyPosition(2.3);
		assertFalse(note == newPos, "Checking new tab position is not the original object");
		assertEquals(new NotePosition(2.3), newPos.getPosition(), "Checking new position set");
		assertEquals(pos, note.getPosition(), "Checking old position unchanged");
		
		newPos = note.copyPosition(new NotePosition(4.3));
		assertFalse(note == newPos, "Checking new tab position is not the original object");
		assertEquals(new NotePosition(4.3), newPos.getPosition(), "Checking new position set");
		assertEquals(pos, note.getPosition(), "Checking old position unchanged");
	}
	
	@Test
	public void quantize(){
		note = note.copyPosition(4.01);
		note = note.quantize(four4, 4);
		assertEquals(4, note.getPos(), "Checking note is quantized");
	}
	
	@Test
	public void retime(){
		note = note.copyPosition(2.25);
		note = note.retime(five8, four4);
		assertEquals(3.6, note.getPos(), "Checking note is retimed to new measure");
	}
	
	@Test
	public void retimeMeasure(){
		note = note.copyPosition(2.25);
		note = note.retimeMeasure(five8, four4);
		assertEquals(2.4, note.getPos(), "Checking note is retimed to the same measure");
	}
	
	@Test
	public void compareTo(){
		TabPosition p = new TabPosition(new TabNote(0), 0);
		assertTrue(p.compareTo(note) < 0, "Pos 0 should compare less than 0 for pos 3");
		
		p = p.copyPosition(4);
		assertTrue(p.compareTo(note) > 0, "Pos 4 should compare greater than 0 for pos 3");

		p = p.copyPosition(3);
		assertTrue(p.compareTo(note) == 0, "Pos 3 should compare equal to 0 for pos 3");
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void equals(){
		TabPosition same = new TabPosition(new TabNote(2), 3);
		assertTrue(note.equals(same), "Checking notes are equal");
		
		same = same.copyPosition(4);
		assertFalse(note.equals(same), "Checking notes are not equal with different positions");

		same = same.copyPosition(3);
		assertTrue(note.equals(same), "Checking notes are equal");
		
		same = new TabPosition(new TabNote(new Pitch(2), new TabModifier(same.getSymbol().getModifier().getBefore(), "aa")), 3);
		assertFalse(note.equals(same), "Checking notes are not equal with different modifiers");
		
		same = new TabPosition(new TabNote(new Pitch(2), new TabModifier("aa", same.getSymbol().getModifier().getAfter())), 3);
		assertFalse(note.equals(symbol), "Checking notes are not equal with different object types");
	}
	
	@Test
	public void getSaveObjects(){
		Saveable[] save = note.getSaveObjects();
		assertEquals(2, save.length, "Checking save objects has 2 elements");
		assertEquals(symbol, save[0], "Checking save objects has the symbol");
		assertEquals(pos, save[1], "Checking save objects has the position");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner(""
				+ "TabNote\n"
				+ "2 \n"
				+ "\n"
				+ "\n"
				+ "3.0 \n"
				+ "Tab Note\n"
				+ "TabNote \n"
				+ "2 \n");
		
		TabPosition newNote = new TabPosition(new TabNote(10), 0);
		assertTrue(newNote.load(scan), "Checking load successful");
		assertEquals(newNote, note, "Checking loaded note values correct");
		assertFalse(newNote.load(scan), "Checking load fails with invalid symbol type");
		assertFalse(newNote.load(scan), "Checking load fails with not enough data");
		
		scan.close();
		
		assertFalse(newNote.load(null), "Checking load fails with invalid reader");
	}
	
	@Test
	public void save(){
		assertFalse(note.save(null), "Checking save fails with invalid writer");
		
		assertEquals(""
				+ "TabNote\n"
				+ "2 \n"
				+ "\n"
				+ "\n"
				+ "3.0 \n"
				+ "",
				UtilsTest.testSave(note), "Checking save successful");
	}

	@Test
	public void testToString(){
		assertEquals(""
				+ "[TabPosition, "
					+ "[TabNote, "
						+ "[On C4 string, note: \"2\"], "
						+ "[TabModifier: \"\" \"\"], "
						+ "[Pitch: D4]"
					+ "], "
					+ "[NotePosition, position: 3.0]"
				+ "]", note.toString(), "Checking correct string");
	}
	
	@AfterEach
	public void end(){}
	
}
