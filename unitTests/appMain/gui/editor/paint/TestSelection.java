package appMain.gui.editor.paint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import appUtils.ZabAppSettings;
import music.Pitch;
import music.TimeSignature;
import tab.TabPosition;
import tab.TabString;
import tab.symbol.TabModifier;
import util.testUtils.Assert;

public class TestSelection{
	
	private Selection sel;
	
	private TabString str;
	private TabString newStr;
	private TabPosition pos;
	private TabPosition newPos;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		str = new TabString(new Pitch(0));
		str.placeQuantizedNote(new TimeSignature(4, 4), 0, 2);
		str.placeQuantizedNote(new TimeSignature(4, 4), 0, 3);
		pos = str.get(0);
		newPos = str.get(1);
		
		newStr = new TabString(new Pitch(6));
		newStr.placeQuantizedNote(new TimeSignature(4, 4), 0, 1);
		
		sel = new Selection(pos, str, 1);
	}
	
	@Test
	public void constructor(){
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new Selection(null, str, 0);
			}
		}, "Checking null TabPosition throws error");
		
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new Selection(pos, null, 0);
			}
		}, "Checking null TabString throws error");
	}
	
	@Test
	public void copy(){
		Selection copy = sel.copy();
		assertFalse(copy == sel, "Checking copy is not the same object as the original");
		assertTrue(copy.equals(sel), "Checking copy is equal to original");
	}
	
	@Test
	public void getStringPos(){
		Selection find = new Selection(pos, str, 0);
		assertEquals(pos, find.getStringPos(), "Checking position is found");
		assertTrue(pos == find.getStringPos(), "Checking position is the same object");
		
		str.clear();
		assertEquals(null, find.getStringPos(), "Checking position is not found");
		
		str.add(pos.copy());
		assertEquals(pos, find.getStringPos(), "Checking position is found after a copy is added back to string");
		
		str.clear();
		pos = pos.copySymbol(pos.getSymbol().copyNewModifier((new TabModifier("a", "g"))));
		str.add(pos.copy());
		assertEquals(null, find.getStringPos(), "Checking position is not found after changing the symbol on the string");
	}
	
	@Test
	public void getPos(){
		assertEquals(pos, sel.getPos(), "Checking position initialized");
		assertFalse(pos == sel.getPos(), "Checking position is not the same object as what is stored");
	}
	@Test
	public void getPosition(){
		assertEquals(2, sel.getPosition(), "Checking position initialized");
	}
	
	@Test
	public void getString(){
		assertEquals(str, sel.getString(), "Checking string initialized");
	}
	@Test
	public void getStringIndex(){
		assertEquals(1, sel.getStringIndex(), "Checking string index initialized");
	}
	
	@Test
	public void compareTo(){
		Assert.lessThan(sel.compareTo(null), 0, "Checking comparing to null is negative");
		
		assertEquals(0, sel.compareTo(new Selection(pos, str, 1)), "Checking comparing equivalent objects");
		
		Assert.lessThan(sel.compareTo(new Selection(pos, str, 2)), 0, "Checking comparing to higher string index");
		Assert.greaterThan(sel.compareTo(new Selection(pos, str, 0)), 0, "Checking comparing to lower string index");
		
		Assert.lessThan(sel.compareTo(new Selection(newPos, str, 1)), 0, "Checking comparing to equal string index and higher position");
		newPos = newPos.copyPosition(0);
		Assert.greaterThan(sel.compareTo(new Selection(newPos, str, 1)), 0, "Checking comparing to equal string index and lower position");
	}
	
	@Test
	public void equals(){
		assertFalse(sel.equals(null), "Checking object not equal to null");
		assertTrue(sel.equals(sel), "Checking object equal to itself");
		
		Selection copy = new Selection(pos.copy(), str.copy(), 1);
		assertFalse(copy == sel, "Checking the copy is not the same object");
		assertTrue(sel.equals(copy), "Checking object equal to version which is not the same object");
		
		Selection s = new Selection(newPos, str, 1);
		assertFalse(sel.equals(s), "Checking object not equal with different positions");
		
		s = new Selection(pos, newStr, 1);
		assertFalse(sel.equals(s), "Checking object not equal with different strings");
		
		s = new Selection(pos, str, 2);
		assertFalse(sel.equals(s), "Checking object not equal with different string indexes");
	}

	@Test
	public void testToString(){
		assertEquals(""
				+ "[Selection: "
					+ "[TabPosition, "
						+ "[TabNote, "
							+ "[On C4 string, note: \"0\"], "
							+ "[TabModifier: \"\" \"\"], "
							+ "[Pitch: C4]"
						+ "], "
						+ "[NotePosition, position: 2.0]"
					+ "], "
					+ "[TabString, rootPitch: [Pitch: C4], "
						+ "[Notes: "
							+ "[TabPosition, [TabNote, [On C4 string, note: \"0\"], [TabModifier: \"\" \"\"], [Pitch: C4]], [NotePosition, position: 2.0]], "
							+ "[TabPosition, [TabNote, [On C4 string, note: \"0\"], [TabModifier: \"\" \"\"], [Pitch: C4]], [NotePosition, position: 3.0]]"
						+ "]"
					+ "], String index: 1"
				+ "]", sel.toString(), "Checking string correct");
	}
	
	@AfterEach
	public void end(){}
	
}
