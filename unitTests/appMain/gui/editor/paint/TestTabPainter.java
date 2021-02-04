package appMain.gui.editor.paint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.util.Camera;
import appUtils.ZabAppSettings;
import appUtils.settings.ZabSettings;
import music.Music;
import music.Pitch;
import tab.InstrumentFactory;
import tab.Tab;
import tab.TabFactory;
import tab.TabPosition;
import tab.TabString;
import tab.symbol.TabModifier;
import tab.symbol.TabPitch;
import tab.symbol.TabSymbol;
import util.testUtils.Assert;
import util.testUtils.UtilsTest;

public class TestTabPainter extends AbstractTestTabPainter{

	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
	}
	
	@Test
	public void getGui(){
		assertEquals(gui, paint.getGui(), "Checking gui initialized");
	}
	
	@Test
	public void getPaintWidth(){
		assertEquals(400, paint.getPaintWidth(), "Checking width initialized");
	}
	
	@Test
	public void setPaintWidth(){
		paint.setPaintWidth(450);
		assertEquals(450, paint.getPaintWidth(), "Checking width set");
		assertEquals(340, paint.getPaintHeight(), "Checking height unchanged");
	}
	
	@Test
	public void getPaintHeight(){
		assertEquals(340, paint.getPaintHeight(), "Checking height initialized");
	}
	
	@Test
	public void setPaintHeight(){
		paint.setPaintHeight(420);
		assertEquals(400, paint.getPaintWidth(), "Checking width unchanged");
		assertEquals(420, paint.getPaintHeight(), "Checking height set");
	}
	
	@Test
	public void setPaintSize(){
		paint.setPaintSize(320, 520);
		assertEquals(320, paint.getPaintWidth(), "Checking width set");
		assertEquals(520, paint.getPaintHeight(), "Checking height set");

		Camera cam = paint.getCamera();
		assertEquals(320, cam.getWidth(), "Checking camera width set");
		assertEquals(520, cam.getHeight(), "Checking camera height set");
		
		Dimension d = paint.getPreferredSize();
		assertEquals(320, d.getWidth(), "Checking preferred width set");
		assertEquals(520, d.getHeight(), "Checking preferred height set");
	}
	
	@Test
	public void getSelected(){
		assertNotEquals(null, paint.getSelected(), "Checking selected initialized");
		assertTrue(paint.getSelected().isEmpty(), "Checking selected initially empty");
	}
	
	@Test
	public void isSelected(){
		paint.select(0, 0);
		assertTrue(paint.isSelected(str0.get(0), 0), "Checking note is selected");
		assertFalse(paint.isSelected(str1.get(0), 1), "Checking note is not selected");
		assertFalse(paint.isSelected(str0.get(0), 1), "Checking note on different string is not selected");
		
		paint.setTab(null);
		assertFalse(paint.isSelected(str0.get(0), 0), "Checking nothing selected with null tab");
		assertFalse(paint.isSelected(str0.get(0), str0), "Checking nothing selected with null tab");
		
		paint.setTab(tab);
		assertFalse(paint.isSelected(str0.get(0), new TabString(new Pitch(0))), "Checking nothing selected with unrecognized TabString");
		assertTrue(paint.isSelected(str0.get(0), str0), "Checking note is selected");
	}
	
	@Test
	public void getLastSelected(){
		assertEquals(null, paint.getLastSelected(), "Checking no initial last selection");
	}
	
	@Test
	public void setLastSelected(){
		paint.select(0, 0);
		paint.select(0, 1);
		paint.setLastSelected(paint.createSelection(3.5, 0));
		assertEquals(paint.getSelected().get(0), paint.getLastSelected(), "Checking last selection set");
	}
	
	@Test
	public void getHoveredPosition(){
		assertEquals(null, paint.getHoveredPosition(), "Checking hovered position initially null");
	}
	
	@Test
	public void setHoveredPosition(){
		paint.setHoveredPosition(strs.get(0).get(0));
		assertEquals(strs.get(0).get(0), paint.getHoveredPosition(), "Checking hovered position set");
	}
	
	@Test
	public void updateHoveredPosition(){
		TabPosition p = strs.get(0).get(0);
		assertTrue(paint.updateHoveredPosition(1029.0, 350.0), "Checking hovered position updated");
		assertEquals(p, paint.getHoveredPosition(), "Checking hovered position set with the position on the center of the TabPosition");
		
		assertFalse(paint.updateHoveredPosition(1029.0, 350.0), "Checking update returns false when setting to the same TabPosition");
		assertEquals(p, paint.getHoveredPosition(), "Checking hovered position set with the position on the center of the TabPosition");
		
		assertTrue(paint.updateHoveredPosition(-1000, 0), "Checking update the hovered position changed even though it sets to null");
		assertEquals(null, paint.getHoveredPosition(), "Checking hovered position set to null");
	}
	
	@Test
	public void getSelectionBox(){
		assertNotEquals(null, paint.getSelectionBox(), "Checking selection box initialized");
	}
	
	@Test
	public void getDragger(){
		assertNotEquals(null, paint.getDragger(), "Checking selection dragger initialized");
	}
	
	@Test
	public void selected(){
		paint.select(0, 0);
		paint.select(0, 1);
		assertEquals(strs.get(0).get(0).getSymbol(), paint.selected(0), "Checking correct symbol found");
		assertEquals(strs.get(1).get(0).getSymbol(), paint.selected(1), "Checking correct symbol found");
		assertEquals(null, paint.selected(-1), "Checking null returned on invalid index");
		assertEquals(null, paint.selected(2), "Checking null returned on invalid index");
	}

	@Test
	public void select(){
		paint.clearSelection();
		assertTrue(paint.select(0, 0), "Checking selection succeeds");
		assertFalse(paint.select(0, 0), "Checking selection fails with position already selected");
		assertTrue(list.isSelected(str0.get(0), str0, 0), "Checking position selected");
		assertFalse(list.isSelected(str1.get(0), str1, 1), "Checking position not selected");
		
		paint.clearSelection();
		assertTrue(paint.select(0, strs.get(1)), "Checking selection succeeds");
		assertTrue(list.isSelected(str1.get(0), str1, 1), "Checking position selected");
		assertFalse(list.isSelected(str2.get(0), str2, 2), "Checking position not selected");
		
		paint.clearSelection();
		assertTrue(paint.select(strs.get(2).get(0), 2), "Checking selection succeeds");
		assertTrue(list.isSelected(str2.get(0), str2, 2), "Checking position selected");
		assertFalse(list.isSelected(str3.get(0), str3, 3), "Checking position not selected");
		
		paint.clearSelection();
		assertTrue(paint.select(strs.get(3).get(0), strs.get(3)), "Checking selection succeeds");
		assertTrue(list.isSelected(str3.get(0), str3, 3), "Checking position selected");
		assertFalse(list.isSelected(str4.get(0), str4, 4), "Checking position not selected");
		
		paint.clearSelection();
		assertFalse(paint.select(strs.get(3).get(0), strs.get(2)), "Checking selection fails with note not on string");
		assertTrue(paint.getSelected().isEmpty(), "Checking no selection");
		
		paint.clearSelection();
		assertFalse(paint.select(null), "Checking a null selection is not selected");
		assertTrue(paint.getSelected().isEmpty(), "Checking no selection");
		
		paint.clearSelection();
		assertFalse(paint.select(strs.get(0).get(0), new TabString(new Pitch(0))), "Checking selecting a note on a string that doesn't exist in the tab, fails");
		assertFalse(paint.select(new Selection(strs.get(0).get(0), strs.get(1), 1)), "Checking selecting a note on a string that doesn't contain that note, fails");
		
		TabString str = new TabString(new Pitch(0));
		TabPosition p = TabFactory.modifiedFret(str, 0, 0);
		assertFalse(paint.select(new Selection(p, str, 0)), "Checking selecting a note on a string which is not in the tab, fails");

		paint.setTab(null);
		assertFalse(paint.select(new Selection(strs.get(0).get(0), strs.get(0), 0)), "Checking selection fails with null tab");
		assertFalse(paint.select(strs.get(3).get(0), 3), "Checking selection fails with null tab");
		assertFalse(paint.select(0, 3), "Checking selection fails with null tab");
		assertFalse(paint.select(strs.get(3).get(0), strs.get(3)), "Checking selection fails with null tab");
	}
	
	@Test
	public void createSelection(){
		assertEquals(new Selection(strs.get(0).get(0), strs.get(0), 0), 
				paint.createSelection(3.5, 0),
				"Checking creating the correct selection");
		
		assertEquals(new Selection(strs.get(5).get(0), strs.get(5), 5), 
				paint.createSelection(0, 5),
				"Checking creating the correct selection");
		
		assertEquals(new Selection(strs.get(5).get(1), strs.get(5), 5), 
				paint.createSelection(1, 5),
				"Checking creating the correct selection");
		
		assertEquals(null, paint.createSelection(-1000, 5),
				"Checking null returned when TabPosition not found");
		
		paint.setTab(null);
		assertEquals(null, paint.createSelection(0, 0), "Checking null selection returned on null tab");
	}
	
	@Test
	public void stringSelection(){
		assertEquals(new Selection(strs.get(0).get(0), strs.get(0), 0), 
				paint.stringSelection(0, 0),
				"Checking creating the correct selection");
		
		assertEquals(new Selection(strs.get(1).get(0), strs.get(1), 1), 
				paint.stringSelection(0, 1),
				"Checking creating the correct selection");
		
		assertEquals(new Selection(strs.get(5).get(0), strs.get(5), 5), 
				paint.stringSelection(0, 5),
				"Checking creating the correct selection");
		
		assertEquals(new Selection(strs.get(5).get(1), strs.get(5), 5), 
				paint.stringSelection(1, 5),
				"Checking creating the correct selection");
		
		paint.setTab(null);
		assertEquals(null, paint.stringSelection(0, 0), "Checking returns null with no tab");
	}
	
	@Test
	public void deselect(){
		TabPosition p = str0.get(0);
		
		paint.select(0, 0);
		assertTrue(paint.getSelected().isSelected(p, str0, 0), "Checking position selected");
		
		assertTrue(paint.deselect(paint.tabPosToX(p.getPos()), paint.tabPosToY(p.getPos(), 0)), "Checking a position is deselected");
		assertFalse(paint.getSelected().isSelected(str0.get(0), str0, 0), "Checking position deselected");
		
		paint.select(0, 0);
		assertFalse(paint.deselect(507.0, 497.0), "Checking coordinates not on a note do not deselect");
	}
	
	@Test
	public void selectOne(){
		TabString s = strs.get(0);
		TabPosition p = s.get(0);
		paint.appendSelectedTabNum('0');
		assertFalse(paint.selectOne(p, strs.get(1)), "Checking note fails to select with note on invalid string");
		assertTrue(paint.selectOne(p, s), "Checking note selected");
		assertFalse(paint.selectOne(p, s), "Checking returns false with note already selected");
		assertEquals(p, list.selectedPosition(0), "Checking note was selected");
		assertEquals(1, paint.getSelected().size(), "Checking only one note was selected");
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking selected new tab num set to null");
		
		assertFalse(paint.selectOne(p, s), "Checking note fails to select when it is already selected");
	}
	
	@Test
	public void selectNote(){
		assertTrue(paint.getSelected().isEmpty(), "Checking nothing is selected");
		
		assertTrue(paint.selectNote(1031.0, 347.0), "Checking note selected");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected");
		assertEquals(tab.getStrings().get(0).get(0), list.selectedPosition(0), "Checking string 0 note selected");
		assertFalse(paint.selectNote(1031.0, 347.0), "Checking trying to select an already selected note fails");
		
		paint.clearSelection();
		assertTrue(paint.selectNote(969.0, 472.0), "Checking note selected");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected");
		assertEquals(tab.getStrings().get(1).get(0), list.selectedPosition(0), "Checking string 1 note selected");
		
		assertFalse(paint.selectNote(455.0, 962.0), "Checking note not selected below the line");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected, no change from not finding a note");
		assertEquals(tab.getStrings().get(1).get(0), list.selectedPosition(0), "Checking string 1 note still selected");
		
		assertFalse(paint.selectNote(1032.0, 15.0), "Checking note not selected selecting above the string");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected, no change from not finding a note");
		assertEquals(tab.getStrings().get(1).get(0), list.selectedPosition(0), "Checking string 1 note still selected");
		
		assertFalse(paint.selectNote(565.0, 696.0), "Checking note not selected selecting between 2 notes");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected, no change from not finding a note");
		assertEquals(tab.getStrings().get(1).get(0), list.selectedPosition(0), "Checking string 1 note still selected");
		
		assertFalse(paint.selectNote(1455.0, 570.0), "Checking note not selected with invalid x position");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected, no change from not finding a note");
		assertEquals(tab.getStrings().get(1).get(0), list.selectedPosition(0), "Checking string 1 note still selected");
		
		paint.clearSelection();
		assertTrue(paint.getSelected().isEmpty(), "Checking none selected");
		assertTrue(paint.selectNote(458.0, 934.0), "Checking note selected");
		assertEquals(1, paint.getSelected().size(), "Checking one note selected");
		assertEquals(tab.getStrings().get(5).get(1), list.selectedPosition(0), "Checking string 5 note 1 selected");
		assertFalse(paint.selectNote(458.0, 934.0), "Checking selecting an already selected note fails");

		tab.getStrings().get(0).clear();
		paint.clearSelection();
		assertFalse(paint.selectNote(686.0, 346.0), "Checking no note selected with no note on the string");
		
		paint.setTab(null);
		assertFalse(paint.selectNote(1031.0, 347.0), "checking selection fails with null tab");
	}
	
	@Test
	public void selectLine(){
		TabPosition p0 = str5.get(0);
		double pX0 = paint.tabPosToX(p0.getPos());
		double pY0 = paint.tabPosToY(p0.getPos(), 5);
		TabPosition p1 = str5.get(1);
		double pX1 = paint.tabPosToX(p1.getPos());
		double pY1 = paint.tabPosToY(p1.getPos(), 5);
		TabPosition p2 = str5.get(2);
		TabPosition p3 = str5.get(3);
		double pX3 = paint.tabPosToX(p3.getPos());
		double pY3 = paint.tabPosToY(p3.getPos(), 5);
		
		TabPosition p = str4.get(0);
		double pX = paint.tabPosToX(p.getPos());
		double pY = paint.tabPosToY(p.getPos(), 4);
		
		assertFalse(paint.selectLine(pX1 - 1000, pY1, false), "Checking selecting a line, when failing to find a note to select, returns false");
		assertTrue(paint.selectLine(pX1, pY1, false), "Checking selecting a line with no previous selected note, but finding a note returns true");
		assertTrue(paint.isSelected(p1, 5), "Checking the note was selected even though the method returned false");
		assertFalse(paint.selectLine(pX1, pY1, false), "Checking selecting a line, when selecting the same note, returns false");
		
		paint.clearSelection();
		paint.selectLine(pX1, pY1, false);
		assertTrue(paint.selectLine(pX0, pY0, false), "Checking selecting a second TabPosition before the fist selected position returns true");
		assertTrue(paint.isSelected(p0, 5), "Checking notes selected");
		assertTrue(paint.isSelected(p1, 5), "Checking notes selected");
		assertEquals(p1, paint.getLastSelected().getPos(), "Checking last selected set to the original");
		
		assertTrue(paint.selectLine(pX3, pY3, false), "Checking selecting a new TabPosition to the right on the same line selects all notes");
		assertFalse(paint.isSelected(p0, 5), "Checking note not selected");
		assertTrue(paint.isSelected(p1, 5), "Checking notes selected");
		assertTrue(paint.isSelected(p2, 5), "Checking notes selected");
		assertTrue(paint.isSelected(p3, 5), "Checking notes selected");
		assertEquals(p1, paint.getLastSelected().getPos(), "Checking last selected set to the original");
		
		assertFalse(paint.selectLine(pX, pY, false), "Checking selecting from a different string as the original fails");
		
		paint.clearSelection();
		paint.selectLine(pX1, pY1, false);
		paint.selectLine(pX0, pY0, false);
		assertTrue(paint.selectLine(pX3, pY3, true), "Checking all notes get selected after adding to a selection");
		assertTrue(paint.isSelected(p0, 5), "Checking notes selected");
		assertTrue(paint.isSelected(p1, 5), "Checking notes selected");
		assertTrue(paint.isSelected(p2, 5), "Checking notes selected");
		assertTrue(paint.isSelected(p3, 5), "Checking notes selected");
	}

	@Test
	public void selectAllNotes(){
		paint.selectAllNotes();
		ArrayList<Selection> sel = paint.getSelected();
		assertEquals(9, sel.size(), "Checking correct number of notes selected");
		
		paint.clearSelection();
		paint.setTab(null);
		paint.selectAllNotes();
		assertEquals(0, sel.size(), "Checking no notes selected with null tab");
	}
	
	@Test
	public void clearSelection(){
		TabString s = tab.getStrings().get(0);
		TabPosition p = s.get(0);
		paint.selectOne(p, s);
		paint.clearSelection();
		assertTrue(paint.getSelected().isEmpty(), "Checking selected empty");
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking selected new tab num set to null");
		assertEquals(null, paint.getLastSelected(), "Checking last selected set to null");
	}
	
	@Test
	public void removeSelection(){
		paint.removeSelection(paint.createSelection(3.5, 0));
		assertEquals(0, str0.size(), "Checking the TabPosition on the TabString was removed");
		assertFalse(paint.getUndoStack().isSaved(), "Checking stack is not saved");
		
		paint.select(0, 1);
		assertTrue(paint.getSelected().isSelected(paint.createSelection(3.25, 1)), "Checking the TabPosition is selected");
		
		Selection s = paint.createSelection(3.25, 1);
		paint.removeSelection(s);
		assertFalse(paint.getSelected().isSelected(s), "Checking the TabPosition is no longer selected");
		assertEquals(0, str0.size(), "Checking the TabPosition on the TabString was removed");
	}
	
	@Test
	public void removeSelections(){
		assertEquals(0, paint.removeSelections(null).size(), "Checking null parameter returns an empty list");
		
		paint.select(0, 0);
		paint.select(0, 1);
		paint.select(0, 2);
		Selection s0 = new Selection(str0.get(0), str0, 0);
		Selection s1 = new Selection(str1.get(0), str1, 1);
		Selection s2 = new Selection(str2.get(0), str2, 2);
		SelectionList list = new SelectionList();
		
		assertEquals(0, paint.removeSelections(list).size(), "Checking empty list returns an empty list");
		
		list.add(s0);
		list.add(s2);
		SelectionList removed = paint.removeSelections(list);
		assertEquals(2, removed.size(), "Checking correct number of selections removed");
		// Checking list contains correct selections which were removed
		Assert.contains(removed, s0);
		Assert.contains(removed, s2);
		assertTrue(paint.getSelected().isSelected(s1), "Checking selected note is still selected");
		assertEquals(0, str0.size(), "Checking TabPosition removed");
		assertEquals(0, str2.size(), "Checking TabPosition removed");
		assertEquals(s1.getPos(), str1.get(0), "Checking TabPosition not removed");
	}
	
	@Test
	public void removeSelectedNotes(){
		paint.selectAllNotes();
		assertFalse(paint.getSelected().isEmpty(), "Checking notes are selected");
		
		paint.removeSelectedNotes();
		assertTrue(paint.getSelected().isEmpty(), "Checking no notes are selected");
		assertTrue(tab.isEmpty(), "Checking the tab has no notes are selected");
		
		TabString str = tab.getStrings().get(0);
		tab.placeQuantizedNote(0, 0, 0);
		tab.placeQuantizedNote(0, 0, 1);
		paint.selectOne(str.get(0), str);
		assertEquals(1, paint.getSelected().size(), "Checking one note selected");
		assertEquals(2, str.size(), "Checking string has 2 notes");
		
		paint.removeSelectedNotes();
		assertTrue(paint.getSelected().isEmpty(), "Checking no notes selected");
		assertEquals(1, str.size(), "Checking string has 1 note");
	}

	@Test
	public void findMovePosition(){
		// The note on the D string at measure 2
		double x = 686.0;
		double y = 689.0;
		Selection s = paint.findPosition(x, y);
		paint.select(s);
		
		// Cases of invalid movement
		assertEquals(null, paint.findMovePosition(null, 0, 0, -10, false), "Checking null selection returns null");
		assertEquals(null, paint.findMovePosition(s, s.getPosition(), 0, -4, false), "Checking invalid new string index returns null");
		assertEquals(null, paint.findMovePosition(s, s.getPosition(), 2, -3, false), "Checking negative invalid new tab position returns null");
		assertEquals(null, paint.findMovePosition(s, s.getPosition(), -1, 1, false), "Checking moving to an occupied tab position returns null");

		// Checking moving to a valid place not keeping the pitch
		Selection move = paint.findMovePosition(s, 0, -2, 1, false);
		assertEquals(0, move.getPosition(), "Checking position value");
		assertEquals(4, move.getStringIndex(), "Checking string index");
		assertEquals(Music.createPitch(Music.A, 2), ((TabPitch)move.getPos().getSymbol()).getPitch(), "Checking pitch of note changed after moving strings");

		// Checking moving to a valid place and keeping the pitch
		move = paint.findMovePosition(s, 0, -2, 0, true);
		assertEquals(0, move.getPosition(), "Checking position value");
		assertEquals(3, move.getStringIndex(), "Checking string index");
		assertEquals(Music.createPitch(Music.D, 3), ((TabPitch)move.getPos().getSymbol()).getPitch(), "Checking pitch of note unchanged not moving between strings");

		// Checking moving to a valid place on the same string
		move = paint.findMovePosition(s, 0, -2, -1, true);
		assertEquals(0, move.getPosition(), "Checking position value");
		assertEquals(2, move.getStringIndex(), "Checking string index");
		assertEquals(Music.createPitch(Music.D, 3), ((TabPitch)move.getPos().getSymbol()).getPitch(), "Checking pitch of note unchanged after moving strings");
		
		move = paint.findMovePosition(s, 0, 0, 0, true);
		assertEquals(null, move, "Checking moving to its current location, i.e. it doesn't move, returns null");
		assertEquals(Music.createPitch(Music.D, 3), ((TabPitch)s.getPos().getSymbol()).getPitch(), "Checking pitch of note unchanged");
		
		// Checking moving to a valid new string, but keeping the same position value
		move = paint.findMovePosition(s, 0, 0, -3, false);
		assertEquals(2, move.getPosition(), "Checking position value");
		assertEquals(0, move.getStringIndex(), "Checking string index");
		assertEquals(Music.createPitch(Music.E, 4), ((TabPitch)move.getPos().getSymbol()).getPitch(), "Checking pitch of note changed moving between strings");
		
		// Case of staying on the same string, and moving into the negative via base position
		move = paint.findMovePosition(s, 0, -1, 0, false);
		assertEquals(1, move.getPosition(), "Checking position value");
		assertEquals(3, move.getStringIndex(), "Checking string index");
		
		// Case of staying on the same string, and moving into the negative via base position, but the base position moves it out of the measure
		paint.clearSelection();
		paint.select(0, 4);
		s = paint.stringSelection(0, 4);
		move = paint.findMovePosition(s, 0, -2, 0, false);
		assertEquals(null, move, "Checking move is invalid");
		
		// Case of staying on the same string, but base position moves it out of the measure into positive coordinates
		paint.clearSelection();
		paint.select(0, 2);
		s = paint.stringSelection(0, 2);
		move = paint.findMovePosition(s, 3, 2, 0, false);
		assertEquals(null, move, "Checking move is invalid");
		
		// Same as before, but one line down
		paint.clearSelection();
		tab.placeQuantizedNote(2, 0, 7.0);
		paint.select(1, 2);
		s = paint.stringSelection(1, 2);
		move = paint.findMovePosition(s, 7, 2, 0, false);
		assertEquals(null, move, "Checking move is invalid");
		str2.remove(1);
		
		// Case of staying on the same string, but base position moves it just out of the measure into positive coordinates
		paint.clearSelection();
		paint.select(0, 2);
		s = paint.stringSelection(0, 2);
		move = paint.findMovePosition(s, 3, 1, 0, false);
		assertEquals(null, move, "Checking move is invalid");
		
		// Case of staying on the same string, but base position moves it just inside the measure
		move = paint.findMovePosition(s, 3, 0.99, 0, false);
		assertEquals(3.99, move.getPosition(), "Checking position correct");
		
		// Case of staying on the same string, but base position moves left just inside a measure
		tab.placeQuantizedNote(4, 0, 5.0);
		paint.clearSelection();
		paint.select(1, 4);
		s = paint.stringSelection(1, 4);
		move = paint.findMovePosition(s, 5, -1.0, 0, false);
		assertEquals(4, move.getPosition(), "Checking position correct");
		
		// Same as before, but a line down
		str4.get(1).setPos(9.0);
		paint.clearSelection();
		paint.select(1, 4);
		s = paint.stringSelection(1, 4);
		move = paint.findMovePosition(s, 9, -1.0, 0, false);
		assertEquals(8, move.getPosition(), "Checking position correct");
		str4.get(1).setPos(5.0);
		
		// Case of staying on the same string, but base position moves left out of a measure staying in positive coordinates
		move = paint.findMovePosition(s, 5, -1.01, 0, false);
		assertEquals(null, move, "Checking move invalid");
		
		// Case of staying on the same string, but moving base to the edge of the left side, making the move invalid
		move = paint.findMovePosition(s, 8, 5, 0, false);
		assertEquals(null, move, "Checking move fails");
		
		// Case of staying on the same string, but moving to a different line of tab
		move = paint.findMovePosition(s, 8, 7, 0, false);
		assertEquals(12, move.getPosition(), "Checking position correct");
		
		// Same as previous, but a different base value makes it valid
		move = paint.findMovePosition(s, 13, 7, 0, false);
		assertEquals(12, move.getPosition(), "Checking position correct");
		
		// Same as previous, but one line down
		str4.get(1).setPos(9.0);
		move = paint.findMovePosition(s, 17, 7, 0, false);
		assertEquals(16, move.getPosition(), "Checking position correct");
		
		// Case of position moving to the left of a line where it would end on the previous line, it should be invalid 
		tab.clearNotes();
		AbstractTestTabPainter.initNotes(tab);
		AbstractTestTabPainter.initAditionalNotes(tab);
		s = paint.stringSelection(4, 5);
		move = paint.findMovePosition(s, 0.5, -0.5, 0, false);
		assertEquals(null, move, "Checking move invalid");
		
		// Case of position moving to the right of a line where it would end on the next line, it should be invalid 
		s = paint.stringSelection(0, 0);
		move = paint.findMovePosition(s, 5, 1, 0, false);
		assertEquals(null, move, "Checking move invalid");
		
		// Final null case
		paint.setTab(null);
		assertEquals(null, paint.findMovePosition(s, s.getPosition(), -2, 1, false), "Checking null tab returns null");
	}
	
	@Test
	public void findPosition(){
		assertEquals(str0.get(0), paint.findPosition(1026.0, 351.0).getPos(), "Checking correct note found");
		assertEquals(str1.get(0), paint.findPosition(976.0, 468.0).getPos(), "Checking correct note found");
		assertEquals(str2.get(0), paint.findPosition(912.0, 580.0).getPos(), "Checking correct note found");
		assertEquals(str3.get(0), paint.findPosition(688.0, 689.0).getPos(), "Checking correct note found");
		assertEquals(str4.get(0), paint.findPosition(461.0, 812.0).getPos(), "Checking correct note found");
		assertEquals(str5.get(0), paint.findPosition(228.0, 918.0).getPos(), "Checking correct note found");
		assertEquals(str5.get(1), paint.findPosition(456.0, 915.0).getPos(), "Checking correct note found");
		assertEquals(str5.get(2), paint.findPosition(689.0, 925.0).getPos(), "Checking correct note found");
		assertEquals(str5.get(3), paint.findPosition(922.0, 926.0).getPos(), "Checking correct note found");
		
		assertEquals(null, paint.findPosition(51.0, 638.0), "Checking null returned on invalid x position");
		assertEquals(null, paint.findPosition(675.0, 14.0), "Checking null returned on invalid y position");

		assertEquals(null, paint.findPosition(603.0, 496.0), "Checking null returned with coordinates on the tab, but not on a note");
		
		str5.clear();
		assertEquals(null, paint.findPosition(228.0, 918.0), "Checking null returned when a string has no TabPositions");
		
		paint.setTab(null);
		assertEquals(null, paint.findPosition(1026.0, 351.0), "Checking null returned with null tab");

	}
	
	@Test
	public void symbolBounds(){
		setup(false);
		ZabSettings settings = ZabAppSettings.get();
		Rectangle2D.Double empty = new Rectangle2D.Double();
		assertEquals(empty, paint.symbolBounds(str0.get(0), 0), "Checking an empty rectangle returned with no valid font metrics");
		
		setup();
		paint.resetCamera();
		Assert.rectangleAproxEqual(new Rectangle2D.Double(542.5, 540.5, 15.0, 19.0), paint.symbolBounds(str0.get(0), 0), "Checking correct bounds");
		
		cam.addX(10);
		cam.addY(5);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(542.5, 540.5, 15.0, 19.0), paint.symbolBounds(str0.get(0), 0), "Checking changing camera doesn't change bounds");
		
		paint.resetCamera();
		settings.paint().getSymbolScaleMode().set(Camera.STRING_SCALE_X_AXIS);
		cam.zoomIn(0, 0, 1);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(544, 541.75, 12.0, 16.5), paint.symbolBounds(str0.get(0), 0), "Checking changing camera zoom");
		
		paint.resetCamera();
		settings.paint().getSymbolScaleMode().set(Camera.STRING_SCALE_X_AXIS);
		cam.zoomInY(0, 1);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(543.5, 541.5, 13.0, 17.0), paint.symbolBounds(str0.get(0), 0), 
				"Checking symbol width unchanged, but y is changed with zooming on the y axis and scaling on the x axis");
		AbstractTestTabPainter.init();
		
		cam.zoomInX(0, 1);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(544, 541.75, 12.0, 16.5), paint.symbolBounds(str0.get(0), 0), 
				"Checking symbol size changed to zoomed after also zooming on the x axis");
		AbstractTestTabPainter.init();
		
		paint.resetCamera();
		settings.paint().getSymbolScaleMode().set(Camera.STRING_SCALE_Y_AXIS);
		cam.zoomInX(0, 1);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(542.5, 540.5, 15.0, 19.0), paint.symbolBounds(str0.get(0), 0), 
				"Checking symbol size unchanged with zooming on the x axis and scaling on the y axis");
		cam.zoomInY(0, 1);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(544, 541.75, 12.0, 16.5), paint.symbolBounds(str0.get(0), 0), 
				"Checking symbol size unchanged with zooming on the y axis and scaling on the y axis");
		
		paint.resetCamera();
		settings.paint().getSymbolScaleMode().set(Camera.STRING_SCALE_NONE);
		cam.zoomIn(0, 0, 1);
		Assert.rectangleAproxEqual(new Rectangle2D.Double(544, 541.75, 12, 16.5), paint.symbolBounds(str0.get(0), 0), 
				"Checking both symbol dimensions changed when zooming with no specific scaling");
		
		paint.setTab(null);
		assertEquals(empty, paint.symbolBounds(str0.get(0), 0), "Checking an empty rectangle returned with null tab");
	}
	
	@Test
	public void reset(){
		paint.setTab(null);
		paint.reset();
		assertFalse(tab.isEmpty(), "Checking no notes removed with null tab in painter");
		assertEquals(0, paint.getLineTabCount(), "Checking line count with null tab");
		
		paint.setTab(tab);
		paint.reset();
		assertTrue(tab.isEmpty(), "Checking all notes removed");
		assertEquals(2, paint.getLineTabCount(), "Checking line count");
		assertFalse(paint.getUndoStack().isSaved(), "Checking stack is not saved");
	}
	
	@Test
	public void getLineTabCount(){
		assertEquals(2, paint.getLineTabCount(), "Checking line count initialized");
	}
	
	@Test
	public void updateLineTabCount(){
		paint.setTab(null);
		paint.updateLineTabCount();
		assertEquals(0, paint.getLineTabCount(), "Checking line count with null tab");
		
		tab.clearNotes();
		paint.setTab(tab);
		paint.updateLineTabCount();
		assertEquals(2, paint.getLineTabCount(), "Checking line count with tab and no notes");
		
		tab.placeQuantizedNote(0, 0, 5);
		paint.updateLineTabCount();
		assertEquals(3, paint.getLineTabCount(), "Checking placing note causes a new line");
		
		tab.placeQuantizedNote(0, 0, 1);
		paint.updateLineTabCount();
		assertEquals(3, paint.getLineTabCount(), "Checking placing note at the beginning doesn't make a new line");
		
		tab.placeQuantizedNote(1, 0, 6);
		paint.updateLineTabCount();
		assertEquals(3, paint.getLineTabCount(), "Checking placing note on the same line doesn't make a new line");
		
		tab.placeQuantizedNote(1, 0, 13);
		paint.updateLineTabCount();
		assertEquals(5, paint.getLineTabCount(), "Checking placing note multiple lines down line adds multiple lines");
		
		tab.placeQuantizedNote(2, 0, 10);
		paint.updateLineTabCount();
		assertEquals(5, paint.getLineTabCount(), "Checking placing note on same line, on a later string");
		
		tab.getStrings().clear();
		paint.updateLineTabCount();
		assertEquals(0, paint.getLineTabCount(), "Checking line count with no strings");
	}
	
	@Test
	public void getSelectedNewTabNum(){
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking tab num initialized to null");
	}
	
	@Test
	public void appendSelectedTabNum(){
		paint.appendSelectedTabNum('1');
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking tab num null with no selection");

		TabString s = tab.getStrings().get(0);
		TabPosition p = s.get(0);
		paint.selectOne(p, s);
		paint.appendSelectedTabNum('j');
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking tab num null with non number");
		
		paint.appendSelectedTabNum('-');
		assertEquals(null, paint.getSelectedNewTabNum(), "Checking tab num null with minus sign");
		
		paint.appendSelectedTabNum('1');
		assertEquals(1, paint.getSelectedNewTabNum(), "Checking tab num with number");
		assertFalse(paint.getUndoStack().isSaved(), "Checking stack is not saved");
		
		paint.appendSelectedTabNum('2');
		assertEquals(12, paint.getSelectedNewTabNum(), "Checking tab num with 2 numbers");
		
		paint.appendSelectedTabNum('3');
		assertEquals(3, paint.getSelectedNewTabNum(), "Checking tab for a third number reset to given number");
		
		paint.appendSelectedTabNum('-');
		assertEquals(-3, paint.getSelectedNewTabNum(), "Checking tab sign change with a minus sign");
		
		paint.appendSelectedTabNum('-');
		assertEquals(3, paint.getSelectedNewTabNum(), "Checking tab sign change back with a minus sign");
		
		paint.appendSelectedTabNum('s');
		assertEquals(3, paint.getSelectedNewTabNum(), "Checking tab num not changed adding a non number or minus sign");
		
		paint.appendSelectedTabNum('-');
		assertEquals(-3, paint.getSelectedNewTabNum(), "Checking appending a minus sign sets it");
		paint.appendSelectedTabNum('5');
		assertEquals(-35, paint.getSelectedNewTabNum(), "Checking appending a negative number adds it");

		paint.appendSelectedTabNum('7');
		assertEquals(-7, paint.getSelectedNewTabNum(), "Checking appending a negative number resets it");
	}
	
	@Test
	public void placeModifier(){
		paint.clearSelection();
		paint.select(0, 0);
		
		TabSymbol sym = str0.get(0).getSymbol();
		paint.placeModifier(new TabModifier("a", ""), 0);
		assertEquals(new TabModifier("a", ""), sym.getModifier(), "Checking replacing a modifier");
		assertFalse(paint.getUndoStack().isSaved(), "Checking stack is not saved");
		
		paint.placeModifier(new TabModifier("", "b"), 0);
		assertEquals(new TabModifier("", "b"), sym.getModifier(), "Checking replacing modifier");
		
		paint.placeModifier(new TabModifier("d", "f"), 0);
		assertEquals(new TabModifier("d", "f"), sym.getModifier(), "Checking replacing modifier");
		
		paint.placeModifier(new TabModifier("e", "r"), 0);
		assertEquals(new TabModifier("e", "r"), sym.getModifier(), "Checking replacing modifier");
		
		paint.placeModifier(new TabModifier("e", "r"), 2);
		assertEquals(new TabModifier("", ""), sym.getModifier(), "Checking erasing modifier");
		
		paint.placeModifier(new TabModifier("", "r"), 1);
		assertEquals(new TabModifier("", "r"), sym.getModifier(), "Checking adding modifier");
		
		paint.placeModifier(new TabModifier("d", "f"), 1);
		assertEquals(new TabModifier("d", "r"), sym.getModifier(), "Checking adding modifier");
		
		paint.placeModifier(new TabModifier("w", "e"), 1);
		assertEquals(new TabModifier("d", "r"), sym.getModifier(), "Checking adding modifier");

		paint.clearSelection();
		paint.select(0, 5);
		paint.select(1, 5);
		paint.select(2, 5);
		paint.placeModifier(new TabModifier("v", "b"), 0);
		assertEquals(new TabModifier("v", "b"), str5.get(0).getSymbol().getModifier(), "Checking adding modifiers to many notes");
		assertEquals(new TabModifier("v", "b"), str5.get(1).getSymbol().getModifier(), "Checking adding modifiers to many notes");
		assertEquals(new TabModifier("v", "b"), str5.get(2).getSymbol().getModifier(), "Checking adding modifiers to many notes");
		assertEquals(new TabModifier("", ""), str5.get(3).getSymbol().getModifier(), "Checking adding modifier not added");
	}
	
	@Test
	public void placeNote(){
		assertTrue(paint.placeNote(460.0, 340.0, 0), "Checking note was placed");
		assertEquals(2, tab.getStrings().get(0).size(), "Checking a new note added");
		assertEquals(1, tab.getStrings().get(0).get(0).getPos(), "Checking note has correct position");
		assertFalse(paint.getUndoStack().isSaved(), "Checking stack is not saved");
		
		assertFalse(paint.placeNote(460.0, 340.0, 0), "Checking note in the same position not placed");

		assertFalse(paint.placeNote(44.0, 628.0, 0), "Checking no note added with too low of x");
		assertFalse(paint.placeNote(629.0, 26.0, 0), "Checking a no new note added with invalid y");
		
		paint.setTab(null);
		assertFalse(paint.placeNote(687.0, 339.0, 0), "Checking note fails to place with null tab");
	}
	
	@Test
	public void placeAndSelect(){
		Selection s0 = paint.createSelection(3.5, 0);
		Selection s1 = paint.createSelection(3.25, 1);
		paint.removeSelection(s0);
		assertFalse(paint.isSelected(s0.getPos(), 0), "Checking position not selected");
		assertEquals(null, paint.createSelection(3.5, 0), "Checking note removed");
		
		paint.placeAndSelect(s0);
		assertTrue(paint.isSelected(s0.getPos(), 0), "Checking position selected");
		assertEquals(s0, paint.createSelection(3.5, 0), "Checking note added again");
		
		SelectionList list = new SelectionList();
		paint.removeSelection(s0);
		paint.removeSelection(s1);
		assertFalse(paint.isSelected(s0.getPos(), 0), "Checking position not selected");
		assertEquals(null, paint.createSelection(3.5, 0), "Checking note removed");
		assertFalse(paint.isSelected(s1.getPos(), 0), "Checking position not selected");
		assertEquals(null, paint.createSelection(3.25, 1), "Checking note removed");
		list.add(s0);
		list.add(s1);
		paint.placeAndSelect(list);
		assertTrue(paint.isSelected(s0.getPos(), 0), "Checking position selected");
		assertEquals(s0, paint.createSelection(3.5, 0), "Checking note added again");
		assertTrue(paint.isSelected(s0.getPos(), 0), "Checking position selected");
		assertEquals(s1, paint.createSelection(3.25, 1), "Checking note added again");
		
		// Running case of null selection list
		paint.placeAndSelect((SelectionList)null);
	}

	@Test
	public void getCamera(){
		assertEquals(400, cam.getWidth(), "Checking camera width initialized");
		assertEquals(340, cam.getHeight(), "Checking camera height initialized");
	}
	
	@Test
	public void resetCamera(){
		cam.setX(2);
		cam.setY(2);
		cam.setXZoomFactor(2);
		cam.setYZoomFactor(2);
		cam.setDrawOnlyInBounds(true);
		paint.resetCamera();
		assertEquals(200, cam.getX(), "Checking camera x reset");
		assertEquals(1630, cam.getY(), "Checking camera y reset");
		assertEquals(0, cam.getXZoomFactor(), "Checking camera x zoom factor reset");
		assertEquals(0, cam.getYZoomFactor(), "Checking camera y zoom factor reset");
		assertEquals(true, cam.isDrawOnlyInBounds(), "Checking camera drawing in bounds reset");
	}
	
	@Test
	public void getTab(){
		assertEquals(tab, paint.getTab(), "Checking tab initialized");
	}
	
	@Test
	public void setTab(){
		Tab t = InstrumentFactory.bassStandard();
		paint.setTab(t);
		assertEquals(t, paint.getTab(), "Checking tab set");
	}
	
	@Test
	public void getUndoStack(){
		assertNotEquals(null, paint.getUndoStack(), "Checking stack is initialized");
		assertFalse(paint.getUndoStack().isSaved(), "Checking stack is not saved");
	}
	
	@Test
	public void getMouseInput(){
		assertNotEquals(null, paint.getMouseInput(), "Checking mouse input initialized");
		// Checking mouse input is in the painter
		Assert.contains(paint.getMouseListeners(), paint.getMouseInput());
		Assert.contains(paint.getMouseMotionListeners(), paint.getMouseInput());
		Assert.contains(paint.getMouseWheelListeners(), paint.getMouseInput());
	}
	
	@Test
	public void getKeyInput(){
		assertNotEquals(null, paint.getKeyInput(), "Checking key input initialized");
		// Checking key input is in the painter
		Assert.contains(paint.getKeyListeners(), paint.getKeyInput());
	}
	
	@Test
	public void removeXOffset(){
		assertEquals(0, paint.removeXOffset(200), UtilsTest.DELTA, "Checking correct amount removed");
		assertEquals(-25, paint.removeXOffset(175), UtilsTest.DELTA, "Checking correct amount removed");
	}
	
	@Test
	public void removeYOffset(){
		assertEquals(50, paint.removeYOffset(200), UtilsTest.DELTA, "Checking correct amount removed");
		assertEquals(25, paint.removeYOffset(175), UtilsTest.DELTA, "Checking correct amount removed");
	}
	
	@Test
	public void tabWidth(){
		assertEquals(400, paint.tabWidth(), UtilsTest.DELTA, "Checking correct width");
	}
	
	@Test
	public void tabHeight(){
		assertEquals(250, paint.tabHeight(), "Checking correct height 6 strings");
		
		tab.getStrings().remove(0);
		assertEquals(200, paint.tabHeight(), "Checking correct height 5 strings");
		
		tab.getStrings().clear();
		assertEquals(0, paint.tabHeight(), "Checking correct height zero strings");
	}

	@Test
	public void tabLineStart(){
		assertEquals(-850, paint.tabLineStart(-1), "Checking start of line before the first line");
		assertEquals(400, paint.tabLineStart(0), "Checking start of first line");
		assertEquals(1650, paint.tabLineStart(1), "Checking start of second line");
		assertEquals(2900, paint.tabLineStart(2), "Checking start of third line");
	}
	
	@Test
	public void tabLineEnd(){
		assertEquals(-600, paint.tabLineEnd(-1), "Checking end of line before the first line");
		assertEquals(650, paint.tabLineEnd(0), "Checking end of first line");
		assertEquals(1900, paint.tabLineEnd(1), "Checking end of second line");
		assertEquals(3150, paint.tabLineEnd(2), "Checking end of third line");
	}
	
	@Test
	public void tabLineStartBuffer(){
		assertEquals(-860, paint.tabLineStartBuffer(-1), "Checking start of line before the first line");
		assertEquals(390, paint.tabLineStartBuffer(0), "Checking start of first line");
		assertEquals(1640, paint.tabLineStartBuffer(1), "Checking start of second line");
		assertEquals(2890, paint.tabLineStartBuffer(2), "Checking start of third line");
	}
	
	@Test
	public void tabLineEndBuffer(){
		assertEquals(-590, paint.tabLineEndBuffer(-1), "Checking end of line before the first line");
		assertEquals(660, paint.tabLineEndBuffer(0), "Checking end of first line");
		assertEquals(1910, paint.tabLineEndBuffer(1), "Checking end of second line");
		assertEquals(3160, paint.tabLineEndBuffer(2), "Checking end of third line");
	}
	
	@Test
	public void tabLineSideBuffer(){
		assertEquals(25, paint.tabLineSideBuffer(), "Checking side buffer correct");
	}
	
	@Test
	public void camXInTabLine(){
		assertTrue(paint.camXInTabLine(200), "Checking point in the middle of the line");
		assertTrue(paint.camXInTabLine(175.001), "Checking point just inside the line on the left");
		assertTrue(paint.camXInTabLine(599.999), "Checking point just inside the line on the right");
		assertFalse(paint.camXInTabLine(174.999), "Checking point just outside the line on the left");
		assertFalse(paint.camXInTabLine(600.001), "Checking point just outside the line on the right");
		assertFalse(paint.camXInTabLine(100.0), "Checking point far outside the line on the left");
		assertFalse(paint.camXInTabLine(700.0), "Checking point far outside the line on the right");
	}
	
	@Test
	public void camXInTabLineNoBuffer(){
		assertTrue(paint.camXInTabLineNoBuffer(200), "Checking point in the middle of the line");
		assertTrue(paint.camXInTabLineNoBuffer(200.001), "Checking point just inside the line on the left");
		assertTrue(paint.camXInTabLineNoBuffer(599.999), "Checking point just inside the line on the right");
		assertFalse(paint.camXInTabLineNoBuffer(199.999), "Checking point just outside the line on the left");
		assertFalse(paint.camXInTabLineNoBuffer(600.001), "Checking point just outside the line on the right");
		assertFalse(paint.camXInTabLineNoBuffer(100.0), "Checking point far outside the line on the left");
		assertFalse(paint.camXInTabLineNoBuffer(700.0), "Checking point far outside the line on the right");
	}
	
	@Test
	public void tabPosInTabLine(){
		assertTrue(paint.tabPosInTabLine(2, 0), "Checking no change is still in a line");
		assertTrue(paint.tabPosInTabLine(4, 0), "Checking no change is still in a line");
		assertTrue(paint.tabPosInTabLine(7, 0), "Checking no change is still in a line");
		assertTrue(paint.tabPosInTabLine(7, 0.1), "Checking small change near right side is still in a line");
		assertTrue(paint.tabPosInTabLine(4.2, -0.1), "Checking small change near left side is still in a line");
		
		assertFalse(paint.tabPosInTabLine(3.9, 0.2), "Checking small change out of a line is not in a line");
		assertFalse(paint.tabPosInTabLine(4.1, -0.36), "Checking small change out of a line is not in a line");
		
		assertFalse(paint.tabPosInTabLine(7.75, -4), "Checking change of full a line backward");
		assertFalse(paint.tabPosInTabLine(3.9, 4), "Checking change of full a line forward");
		assertFalse(paint.tabPosInTabLine(2.3, 4), "Checking change of full a line forward");
		assertFalse(paint.tabPosInTabLine(2.3, 4.8), "Checking change of full a line forward");
		
	}
	
	@Test
	public void numStrings(){
		assertEquals(6, paint.numStrings(), "Checking number of strings with tab");
		
		tab.getStrings().remove(0);
		assertEquals(5, paint.numStrings(), "Checking number of strings with a removed string");
		
		paint.setTab(null);
		assertEquals(0, paint.numStrings(), "Checking zero strings with null tab");
	}
	
	@Test
	public void validStringIndex(){
		assertTrue(paint.validStringIndex(0), "Checking a valid index for the first string");
		assertTrue(paint.validStringIndex(5), "Checking a valid index for the last string");
		assertFalse(paint.validStringIndex(-1), "Checking a too low index");
		assertFalse(paint.validStringIndex(6), "Checking a too high index");
	}
	
	@Test
	public void lineNumberValue(){
		assertEquals(0.5, paint.lineNumberValue(775), UtilsTest.DELTA, "Checking halfway between the first and second lines");
		assertEquals(0, paint.lineNumberValue(150), UtilsTest.DELTA, "Checking at the first line");
		assertEquals(-0.5, paint.lineNumberValue(-475), UtilsTest.DELTA, "Checking halfway between the first and line before the first");
		assertEquals(0.9479411292300564, paint.lineNumberValue(1334.9264115375704), UtilsTest.DELTA, "Checking between the first and second lines");
		assertEquals(2.180207084061017, paint.lineNumberValue(2875.258855076271), UtilsTest.DELTA, "Checking between the second and third lines");
	}
	
	@Test
	public void lineNumber(){
		assertEquals(0, paint.lineNumber(775), "Checking halfway between the first and second lines");
		assertEquals(0, paint.lineNumber(150), "Checking at the first line");
		assertEquals(-1, paint.lineNumber(-475), "Checking halfway between the first and line before the first");
		assertEquals(0, paint.lineNumber(1334.9264115375704), "Checking between the first and second lines");
		assertEquals(2, paint.lineNumber(2875.258855076271), "Checking between the second and third lines");
	}
	
	@Test
	public void lineNumberFromPos(){
		assertEquals(0, paint.lineNumberFromPos(0), "Checking at the first line");
		assertEquals(-1, paint.lineNumberFromPos(-1), "Checking a negative line");
		assertEquals(1, paint.lineNumberFromPos(4), "Checking the start of the second line");
		assertEquals(1, paint.lineNumberFromPos(7), "Checking the middle of the of the second line");
	}
	
	@Test
	public void lineLength(){
		assertEquals(0.0, paint.lineLength(633.5551752921597), UtilsTest.DELTA, "Checking the first line");
		assertEquals(400.0, paint.lineLength(1452.390100669888), UtilsTest.DELTA, "Checking the first line");
		assertEquals(800.0, paint.lineLength(3008.6051682725856), UtilsTest.DELTA, "Checking the third line");
		assertEquals(-400.0, paint.lineLength(41.93622397212516), UtilsTest.DELTA, "Checking before the first line");
	}
	
	@Test
	public void lineNumberLength(){
		assertEquals(0, paint.lineNumberLength(0), "Checking line zero begins at zero");
		assertEquals(400, paint.lineNumberLength(1), "Checking line 1 begins at the start of one line");
		assertEquals(800, paint.lineNumberLength(2), "Checking line 2 begins at the start of two lines");
	}
	
	@Test
	public void lineNumberMeasures(){
		assertEquals(0, paint.lineNumberMeasures(0), "Checking line zero begins at the zeroth measure");
		assertEquals(4, paint.lineNumberMeasures(1), "Checking line 1 begins at the 4th measure");
		assertEquals(8, paint.lineNumberMeasures(2), "Checking line 2 begins at 8th measure");
	}
	
	@Test
	public void lineHeight(){
		assertEquals(1250, paint.lineHeight(), "Checking line height with 6 strings");
		
		tab.getStrings().remove(0);
		assertEquals(1200, paint.lineHeight(), "Checking line height with 5 strings");
		
		paint.setTab(null);
		assertEquals(1000, paint.lineHeight(), "Checking line height with null tab");
	}
	
	@Test
	public void paintWidthToMeasures(){
		assertEquals(4, paint.paintWidthToMeasures(400), UtilsTest.DELTA, "Checking integer width");
		assertEquals(1.3, paint.paintWidthToMeasures(130), UtilsTest.DELTA, "Checking decimal width");
		assertEquals(0, paint.paintWidthToMeasures(0), UtilsTest.DELTA, "Checking zero width");
		assertEquals(-2.008, paint.paintWidthToMeasures(-200.8), UtilsTest.DELTA, "Checking negative width");
	}
	
	@Test
	public void measuresToPaintWidth(){
		assertEquals(400, paint.measuresToPaintWidth(4), UtilsTest.DELTA, "Checking integer width");
		assertEquals(130, paint.measuresToPaintWidth(1.3), UtilsTest.DELTA, "Checking decimal width");
		assertEquals(0, paint.measuresToPaintWidth(0), UtilsTest.DELTA, "Checking zero width");
		assertEquals(-200.8, paint.measuresToPaintWidth(-2.008), UtilsTest.DELTA, "Checking negative width");
	}
	
	@Test
	public void quanitzedTabPos(){
		assertEquals(0, paint.quanitzedTabPos(234.0, 804.0), "Checking position at the beginning of a line");
		assertEquals(2, paint.quanitzedTabPos(688.0, 459.0), "Checking position in the middle of a line");
		assertEquals(2.5, paint.quanitzedTabPos(794.0, 574.0), "Checking position in the middle of a measure");
		
		assertEquals(-1, paint.quanitzedTabPos(44.0, 636.0), "Checking invalid position left of a line");
		assertEquals(-1, paint.quanitzedTabPos(1436.0, 602.0), "Checking invalid position right of a line");
		assertEquals(2.25, paint.quanitzedTabPos(730.0, 219.0), "Checking position above a line");
		assertEquals(0.75, paint.quanitzedTabPos(391.0, 949.0), "Checking position below a line");
		
		paint.setTab(null);
		assertEquals(-1, paint.quanitzedTabPos(234.0, 804.0), "Checking invalid position with no tab");
	}
	
	@Test
	public void xToTabPos(){
		assertEquals(0.43305567662212413, paint.xToTabPos(329.0, 399.0), UtilsTest.DELTA, "Checking middle of first measure");
		assertEquals(3.2884615242334108, paint.xToTabPos(985.0, 535.0), UtilsTest.DELTA, "Checking middle of fourth measure");
		assertEquals(2.0522797243529145, paint.xToTabPos(701.0, 949.0), UtilsTest.DELTA, "Checking below line still finds a tab position");
		assertEquals(1.9913411849221858, paint.xToTabPos(687.0, 30.0), UtilsTest.DELTA, "Checking above line still finds a tab position");
		assertEquals(-1.0, paint.xToTabPos(73.0, 517.0), UtilsTest.DELTA, "Checking left of line finds invalid position");
		assertEquals(-1.0, paint.xToTabPos(1433.0, 591.0), UtilsTest.DELTA, "Checking right of line finds invalid position");
	}
	
	@Test
	public void tabPosToX(){
		assertEquals(329.0, paint.tabPosToX(0.43305567662212413), UtilsTest.DELTA, "Checking middle of first measure");
		assertEquals(985.0, paint.tabPosToX(3.2884615242334108), UtilsTest.DELTA, "Checking middle of fourth measure");
		assertEquals(701.0, paint.tabPosToX(2.0522797243529145), UtilsTest.DELTA, "Checking middle of third measure");
		assertEquals(687.0, paint.tabPosToX(1.9913411849221858), UtilsTest.DELTA, "Checking middle of second measure");
	}
	
	@Test
	public void camXToTabPos(){
		assertEquals(1.4385415772291474, paint.camXToTabPos(343.85415772291475, 624.8120453304), UtilsTest.DELTA, "Checking position in the middle of a measure");
		assertEquals(2.5310825341657837, paint.camXToTabPos(453.10825341657835, 717.5256803214372), UtilsTest.DELTA, "Checking position in the middle of a measure");
		assertEquals(0.0, paint.camXToTabPos(188.4608821745566, 737.548343277248), UtilsTest.DELTA, "Checking position in the at the left edge of a measure");
		assertEquals(3.75, paint.camXToTabPos(589.3494165724218, 675.3039780015752), UtilsTest.DELTA, "Checking position in the at the right edge of a measure");
		assertEquals(1.1991401723227135, paint.camXToTabPos(319.91401723227136, 515.5579496367365), UtilsTest.DELTA, "Checking position above a line finds the correct measure");
		assertEquals(3.271050512967489, paint.camXToTabPos(527.1050512967489, 818.9448209454357), UtilsTest.DELTA, "Checking position below a line finds the correct measure");
		assertEquals(-1.0, paint.camXToTabPos(120.12266295581085, 646.5758094128031), UtilsTest.DELTA, "Checking position left of a line is invalid");
		assertEquals(-1.0, paint.camXToTabPos(669.0047931140172, 683.5742083528884), UtilsTest.DELTA, "Checking position right of a line is invalid");
	}
	
	@Test
	public void tabPosToCamX(){
		assertEquals(343.85415772291475, paint.tabPosToCamX(1.4385415772291474), UtilsTest.DELTA, "Checking position in the middle of a measure");
		assertEquals(453.10825341657835, paint.tabPosToCamX(2.5310825341657837), UtilsTest.DELTA, "Checking position in the middle of a measure");
		assertEquals(200.0, paint.tabPosToCamX(0.0), UtilsTest.DELTA, "Checking position in the at the left edge of a measure");
		assertEquals(575.0, paint.tabPosToCamX(3.75), UtilsTest.DELTA, "Checking position in the at the right edge of a measure");
	}
	
	@Test
	public void yToTabPos(){
		assertEquals(3.6203842810505424, paint.yToTabPos(760.0), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(0.32099764615823234, paint.yToTabPos(381.0), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(0.0, paint.yToTabPos(333.0), UtilsTest.DELTA, "Checking string in the top edge of the tab line");
		assertEquals(5.0, paint.yToTabPos(929.0), UtilsTest.DELTA, "Checking string in the bottom edge of the tab line");
		assertEquals(-1.0, paint.yToTabPos(251.0), UtilsTest.DELTA, "Checking position above tab is invalid");
		assertEquals(-1.0, paint.yToTabPos(958.0), UtilsTest.DELTA, "Checking position below tab is invalid");
		assertEquals(2.523490571297425, paint.yToTabPos(634.0), UtilsTest.DELTA, "Checking position to the right of a tab finds a string");
		assertEquals(1.3830693333795034, paint.yToTabPos(503.0), UtilsTest.DELTA, "Checking position to the left of a tab finds a string");
	}
	
	@Test
	public void tabPosToY(){
		assertEquals(760.0, paint.tabPosToY(2, 3.6203842810505424), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(381.0, paint.tabPosToY(2, 0.32099764615823234), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(344.12705319001174, paint.tabPosToY(2, 0.0), UtilsTest.DELTA, "Checking string in the top edge of the tab line");
		assertEquals(918.4762306885292, paint.tabPosToY(2, 5.0), UtilsTest.DELTA, "Checking string in the bottom edge of the tab line");
	}
	
	@Test
	public void camYToTabPos(){
		assertEquals(1.91410517699014, paint.camYToTabPos(645.705258849507), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(3.898960461305303, paint.camYToTabPos(744.9480230652651), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(0.0, paint.camYToTabPos(543.4155676622124), UtilsTest.DELTA, "Checking string in the top edge of the tab line");
		assertEquals(5.0, paint.camYToTabPos(803.7101860877535), UtilsTest.DELTA, "Checking string in the bottom edge of the tab line");
		assertEquals(-1.0, paint.camYToTabPos(472.90097203522635), UtilsTest.DELTA, "Checking position above tab is invalid");
		assertEquals(-1.0, paint.camYToTabPos(816.3331692555473), UtilsTest.DELTA, "Checking position below tab is invalid");
		assertEquals(-1.0, paint.camYToTabPos(-369.3566979537738), UtilsTest.DELTA, "Checking negative line number returns -1");
	}
	
	@Test
	public void tabPosToCamY(){
		assertEquals(645.705258849507, paint.tabPosToCamY(2, 1.91410517699014), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(744.9480230652651, paint.tabPosToCamY(2, 3.898960461305303), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(550.0, paint.tabPosToCamY(2, 0.0), UtilsTest.DELTA, "Checking string in the top edge of the tab line");
		assertEquals(800.0, paint.tabPosToCamY(2, 5.0), UtilsTest.DELTA, "Checking string in the bottom edge of the tab line");
	}
	
	@Test
	public void pixelYToStringNum(){
		assertEquals(4, paint.pixelYToStringNum(760.0), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(0, paint.pixelYToStringNum(381.0), UtilsTest.DELTA, "Checking string in the middle of the tab line");
		assertEquals(0.0, paint.pixelYToStringNum(333.0), UtilsTest.DELTA, "Checking string in the top edge of the tab line");
		assertEquals(5.0, paint.pixelYToStringNum(929.0), UtilsTest.DELTA, "Checking string in the bottom edge of the tab line");
		assertEquals(-1.0, paint.pixelYToStringNum(251.0), UtilsTest.DELTA, "Checking position above tab is invalid");
		assertEquals(-1.0, paint.pixelYToStringNum(958.0), UtilsTest.DELTA, "Checking position below tab is invalid");
		assertEquals(3, paint.pixelYToStringNum(634.0), UtilsTest.DELTA, "Checking position to the right of a tab finds a string");
		assertEquals(1, paint.pixelYToStringNum(503.0), UtilsTest.DELTA, "Checking position to the left of a tab finds a string");
	
	}
	
	@Test
	public void getLastSymbolFont(){
		assertNotEquals(null, paint.getLastSymbolFont(), "Checking font metrics set with the paint method called");
		
		setup(false);
		assertEquals(null, paint.getLastSymbolFont(), "Checking font metrics not set with the paint method not called");
	}
	
	@Test
	public void paint(){
		paint.paint(g);
		assertEquals(g, paint.getCamera().getGraphics(), "Checking graphics object set and used");
		
		// Calling paint with a drag point set
		paint.getDragger().setDragPoint(new Point2D.Double(0, 0));
		paint.paint(g);
	}
	
	@Test
	public void drawTab(){
		assertTrue(paint.drawTab(), "Checking painting occurred");
		assertEquals(g, paint.getCamera().getGraphics(), "Checking graphics object set and used");

		TabString s = tab.getStrings().get(0);
		paint.selectOne(s.get(0), s);
		assertTrue(paint.drawTab(), "Checking painting occurred with drawing a selection");
		
		paint.setTab(null);
		assertFalse(paint.drawTab(), "Checking painting failed with null tab");
	}
	
	@Test
	public void drawSymbols(){
		assertFalse(paint.drawSymbols(null, 0, 0), "Checking draw fails with null tab");
		assertTrue(paint.drawSymbols(tab, 0, 0), "Checking draw succeeds with normal tab");
	}
	
	@Test
	public void drawSymbolHighlight(){
		Rectangle2D bounds = new Rectangle2D.Double(1, 2, 10, 20);
		
		paint.select(0, 0);
		paint.select(0, 1);
		paint.setTab(null);
		assertFalse(paint.drawSymbolHighlight(list, str0.get(0), 0, bounds), "Checking paint fails with null tab");
		paint.setTab(tab);
		assertTrue(paint.drawSymbolHighlight((SelectionList)null, str0.get(0), 0, bounds), "Checking paint succeeds with null list");
		
		assertFalse(paint.drawSymbolHighlight(list, str2.get(0), 0, bounds), "Checking paint fails with position not in list");
		assertTrue(paint.drawSymbolHighlight(list, str0.get(0), 0, bounds), "Checking paint succeeds");
		
		assertTrue(paint.drawSymbolHighlight(str0.get(0), str0.get(0), bounds), "Checking paint succeeds");
		assertFalse(paint.drawSymbolHighlight(str1.get(0), str0.get(0), bounds), "Checking paint fails with differing check TabPosition");
	}
	
	@AfterEach
	public void end(){}

}
