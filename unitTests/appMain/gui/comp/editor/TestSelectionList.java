package appMain.gui.comp.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import music.Pitch;
import music.TimeSignature;
import tab.TabPosition;
import tab.TabString;
import util.testUtils.Assert;

public class TestSelectionList{

	private SelectionList list;
	private TabString[] str;
	private TabPosition[][] pos;
	private TimeSignature four4;
	private Selection[] selections;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		list = new SelectionList();
		selections = new Selection[8];
		str = new TabString[2];
		str[0] = new TabString(new Pitch(0));
		str[1] = new TabString(new Pitch(12));
		pos = new TabPosition[2][4];
		four4 = new TimeSignature(4, 4);
		for(int i = 0; i < 4; i++){
			str[0].placeQuantizedNote(four4, 0, i);
			pos[0][i] = str[0].get(i);
			
			str[1].placeQuantizedNote(four4, 0, i + .5);
			pos[1][i] = str[1].get(i);
			
			selections[i] = new Selection(pos[0][i], str[0], 0);
			selections[i + 4] = new Selection(pos[1][i], str[1], 1);
		}
	}
	
	@Test
	public void copy(){
		list = new SelectionList();
		SelectionList copy = list.copy();
		assertFalse(copy == list, "Checking copy is not the same object as the original, with an empty list");
		assertTrue(copy.equals(list), "Checking copy equals the original, with an empty list");

		list.add(selections[0]);
		list.add(selections[1]);
		list.add(selections[2]);
		copy = list.copy();
		assertFalse(copy == list, "Checking copy is not the same object as the original");
		assertTrue(copy.equals(list), "Checking copy equals the original");
		
	}
	
	@Test
	public void add(){
		assertFalse(list.add(null), "Checking add a null element fails");
		assertEquals(0, list.size(), "Checking list contains no elements");
		
		assertTrue(list.add(selections[0]), "Checking element added");
		assertFalse(list.add(selections[0]), "Checking duplicate element not added");
		
		// Adding elements in arbitrary order
		assertTrue(list.add(selections[3]), "Checking element added");
		assertTrue(list.add(selections[5]), "Checking element added");
		assertTrue(list.add(selections[2]), "Checking element added");
		assertTrue(list.add(selections[7]), "Checking element added");
		assertTrue(list.add(selections[1]), "Checking element added");
		assertTrue(list.add(selections[6]), "Checking element added");
		assertTrue(list.add(selections[4]), "Checking element added");
		
		// Checking elements are correctly sorted
		Assert.listSame(list, (Object[])selections);
	}
	
	@Test
	public void remove(){
		for(Selection s : selections) list.add(s);
		
		assertFalse(list.remove(null), "Checking null object fails to be removed");
		
		assertTrue(list.remove(selections[0]), "Checking element removed");
		assertTrue(list.remove(selections[3]), "Checking element removed");
		assertFalse(list.remove(selections[0]), "Checking element not in list fails to be removed");
		
		// Checking only correct elements remain
		Assert.listSame(list, selections[1], selections[2], selections[4], selections[5], selections[6], selections[7]);
	}
	
	@Test
	public void contains(){
		list.add(selections[1]);
		assertFalse(list.contains(null), "Checking list cannot contain null");
		assertFalse(list.contains(selections[0]), "Checking list doesn't contain a selection");
		assertTrue(list.contains(selections[1]), "Checking list does contain a selection");
	}
	
	@Test
	public void isSelected(){
		for(int i = 0; i < 4; i++) list.add(selections[i]);
		assertTrue(list.isSelected(selections[0]), "Checking element selected");
		assertTrue(list.isSelected(selections[1]), "Checking element selected");
		assertTrue(list.isSelected(selections[2]), "Checking element selected");
		assertTrue(list.isSelected(selections[3]), "Checking element selected");
		
		assertFalse(list.isSelected(selections[4]), "Checking element not selected");
		assertFalse(list.isSelected(selections[5]), "Checking element not selected");
		assertFalse(list.isSelected(selections[6]), "Checking element not selected");
		assertFalse(list.isSelected(selections[7]), "Checking element not selected");
		
		Selection s = selections[0];
		assertFalse(list.isSelected(s.getPos(), s.getString(), 2), "Checking element with invalid string index not selected");
	}
	
	@Test
	public void selectedPosition(){
		for(int i = 0; i < 4; i++) list.add(selections[i]);
		assertEquals(pos[0][1], list.selectedPosition(1), "Checking correct position found");
		assertEquals(null, list.selectedPosition(-1), "Checking null on too low index");
		assertEquals(null, list.selectedPosition(4), "Checking null on too high index");
	}
	
	@Test
	public void deselect(){
		list.add(selections[0]);
		assertTrue(list.isSelected(selections[0]), "Checking selection added");
		
		list.deselect(selections[0]);
		assertFalse(list.isSelected(selections[0]), "Checking selection removed");
	}
	
	@AfterEach
	public void end(){}

}
