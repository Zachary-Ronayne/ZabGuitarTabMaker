package tab.symbol;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.NotePosition;
import music.Rhythm;
import util.Saveable;
import util.testUtils.UtilsTest;

public class TestTabDeadNote{
	
	private TabDeadNote note;
	private NotePosition pos;
	
	@BeforeEach
	public void setup(){
		pos = new NotePosition(4);
		note = new TabDeadNote(pos);
	}
	
	@Test
	public void constructor(){
		assertEquals(pos, note.getPosition(), "Checking position initialized");
		assertEquals(new TabModifier(), note.getModifier(), "Checking modifier initialized");
	}

	@Test
	public void copy(){
		TabDeadNote copy = note.copy();
		assertTrue("Checking copy is equal to the source object", copy.equals(note));
		assertTrue("Checking copy is not the same as the source object", copy != note);
	}
	
	@Test
	public void convertToRhythm(){
		assertEquals(note, note.convertToRhythm(new Rhythm(1, 1)), "Checking converted version is the same");
	}

	@Test
	public void removeRhythm(){
		assertEquals(note, note.removeRhythm(), "Checking removed version is the same");
	}
	
	@Test
	public void usesRhythm(){
		assertFalse("Checking note doesn't use rhythm", note.usesRhythm());
	}
	
	@Test
	public void getSymbol(){
		assertEquals("X", note.getSymbol(null), "Checking symbol is obtained");
	}
	
	@Test
	public void getSaveObjects(){
		Saveable[] objs = note.getSaveObjects();
		assertEquals(note.getPosition(), objs[0], "Checking position object in correct list position");
		assertEquals(note.getModifier(), objs[1], "Checking modifier object in correct list position");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("1.2 \n<\n>\n\ng");
		assertTrue("Checking load successful", note.load(scan));
		assertEquals(1.2, note.getPos(), "Checking position loaded correctly");
		assertEquals("<", note.getModifier().getBefore(), "Checking modifier before loaded correctly");
		assertEquals(">", note.getModifier().getAfter(), "Checking modifier after loaded correctly");

		assertFalse("Checking load fails without enough data", note.load(scan));
	}
	
	@Test
	public void save(){
		assertEquals("4.0 \n\n\n", UtilsTest.testSave(note), "Checking note saved correctly");
		
		note.getModifier().setBefore("{");
		note.getModifier().setAfter("}");
		note.setPos(2.3);
		assertEquals("2.3 \n{\n}\n", UtilsTest.testSave(note), "Checking note saved correctly");
		
		note.getModifier().setBefore("<");
		note.getModifier().setAfter(">");
		note.setPos(1.2);
		assertEquals("1.2 \n<\n>\n", UtilsTest.testSave(note), "Checking note saved correctly");
		
		note.getModifier().setBefore("{ {");
		note.getModifier().setAfter("} }");
		note.setPos(2.3);
		assertEquals("2.3 \n{ {\n} }\n", UtilsTest.testSave(note), "Checking note saved correctly");
	}
	
	@AfterEach
	public void end(){}
	
}
