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
import music.Rhythm;
import music.TimeSignature;
import tab.symbol.TabModifier;
import tab.symbol.TabNote;
import tab.symbol.TabNoteRhythm;
import tab.symbol.TabSymbol;
import util.testUtils.UtilsTest;

public class TestTab{

	private Tab tab;
	private Tab tabDefault;
	private Tab tabSpecific;
	private Tab tabFull;
	private ArrayList<TabString> strings;
	private TabString highString;
	private TabString lowString;
	
	private Tab tabForRhythms;
	private TabString highRhythms;
	private TabString lowRhythms;
	
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
		tabFull = new Tab(strings, new TimeSignature(4, 4), true);
		
		tabForRhythms = new Tab();
		highRhythms = new TabString(Music.createPitch(Music.E, 3));
		lowRhythms = new TabString(Music.createPitch(Music.E, 2));
		tabForRhythms.getStrings().add(highRhythms);
		tabForRhythms.getStrings().add(lowRhythms);
		highRhythms.add(TabFactory.modifiedFret(highRhythms, 0, 0));
		highRhythms.add(TabFactory.modifiedFret(highRhythms, 0, 1));
		highRhythms.add(TabFactory.modifiedFret(highRhythms, 0, 1.5));
		lowRhythms.add(TabFactory.modifiedFret(lowRhythms, 0, 2));
		lowRhythms.add(TabFactory.modifiedFret(lowRhythms, 0, 2.25));
		lowRhythms.add(TabFactory.modifiedFret(lowRhythms, 0, 2.375));
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
	public void usesRhythm(){
		assertFalse("Checking rhythm initialized", tab.usesRhythm());
		assertTrue("Checking initialized", tabFull.usesRhythm());
	}
	
	@Test
	public void setUsesRhythm(){
		lowString.add(TabFactory.modifiedFret(lowString, 0, 0));
		tab.setUsesRhythm(true);
		assertTrue("Checking rhythm set", tab.usesRhythm());
		assertTrue("Checking note uses rhythm", lowString.get(0).usesRhythm());
		
		tab.setUsesRhythm(false);
		assertFalse("Checking rhythm set", tab.usesRhythm());
		assertFalse("Checking note doesn't use rhythm", lowString.get(0).usesRhythm());
	}
	
	@Test
	public void retime(){
		Tab copy;
		TabString copyH;
		TabString copyL;
		TimeSignature newTime;
		
		highString.add(TabFactory.modifiedFret(lowString, 0, 0));
		highString.add(TabFactory.modifiedFret(lowString, 0, 0.75));
		highString.add(TabFactory.modifiedFret(lowString, 0, 1));
		highString.add(TabFactory.modifiedFret(lowString, 0, 1.25));
		lowString.add(TabFactory.modifiedFret(lowString, 0, 2));
		lowString.add(TabFactory.modifiedFret(lowString, 0, 2.9));
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(5, 4);
		copy.retime(newTime, false, false);
		assertEquals(newTime, copy.getTimeSignature(), "Checking retime set the time signature");
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(0, copyH.get(0).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(0.6, copyH.get(1).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(0.8, copyH.get(2).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(1, copyH.get(3).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(1.6, copyL.get(0).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(2.32, copyL.get(1).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(5, 4);
		copy.retime(newTime, false, true);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(0, copyH.get(0).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(0.6, copyH.get(1).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(1, copyH.get(2).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(1.2, copyH.get(3).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(2, copyL.get(0).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(2.72, copyL.get(1).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(3, 4);
		copy.retime(newTime, false, false);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(0, copyH.get(0).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(1, copyH.get(1).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(1.33333333333333, copyH.get(2).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(1.66666666666667, copyH.get(3).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(2.66666666666667, copyL.get(0).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(3.86666666666667, copyL.get(1).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(3, 4);
		copy.retime(newTime, false, true);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(1, copyL.size(), "Checking one symbol was deleted with rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(0, copyH.get(0).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(1, copyH.get(1).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(1, copyH.get(2).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(1.33333333333333, copyH.get(3).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(2, copyL.get(0).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 3/4");
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(5, 4);
		copy.retime(newTime, true, true);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale true, converting 4/4 to 5/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale true, converting 4/4 to 5/4");
		assertEquals(0, copyH.get(0).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
		assertEquals(0.75, copyH.get(1).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
		assertEquals(1, copyH.get(2).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
		assertEquals(1.25, copyH.get(3).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
		assertEquals(2, copyL.get(0).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
		assertEquals(2.9, copyL.get(1).getPosition().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
	}
	
	@Test
	public void quantize(){
		lowString.add(new TabNote(0, 1.1));
		highString.add(new TabNote(0, 2.1));
		tab.quantize(1);
		assertEquals(1, lowString.get(0).getPosition().getValue(), UtilsTest.DELTA, "Checking string is quantized");
		assertEquals(2, highString.get(0).getPosition().getValue(), UtilsTest.DELTA, "Checking string is quantized");
	}
	
	@Test
	public void getRootNote(){
		assertEquals(-12, tab.getRootNote(0), "Checking root note found");
		assertEquals(-24, tab.getRootNote(1), "Checking root note found");
	}
	
	@Test
	public void removeRhythms(){
		tabFull.getStrings().get(0).add(TabFactory.modifiedFretRhythm(highString, 0, new Rhythm(1, 1), 0, new TabModifier()));
		tabFull.getStrings().get(1).add(TabFactory.modifiedFretRhythm(lowString, 0, new Rhythm(1, 1), 0, new TabModifier()));
		assertTrue("Checking using rhythm is set to true initially", tabFull.usesRhythm());
		assertTrue("Checking rhythm is being used", highString.get(0).usesRhythm());
		assertTrue("Checking rhythm is being used", lowString.get(0).usesRhythm());
		
		tabFull.removeRhythms();
		assertFalse("Checking using rhythm is set to false", tabFull.usesRhythm());
		assertFalse("Checking rhythm is not being used", highString.get(0).usesRhythm());
		assertFalse("Checking rhythm is not being used", lowString.get(0).usesRhythm());
	}
	
	@Test
	public void addRhythm(){
		assertFalse("Checking using rhythm is set to false initially", tabForRhythms.usesRhythm());
		assertFalse("Checking note doesn't use rhythm", highRhythms.get(0).usesRhythm());
		assertFalse("Checking note doesn't use rhythm", highRhythms.get(1).usesRhythm());
		assertFalse("Checking note doesn't use rhythm", highRhythms.get(2).usesRhythm());
		assertFalse("Checking note doesn't use rhythm", lowRhythms.get(0).usesRhythm());
		assertFalse("Checking note doesn't use rhythm", lowRhythms.get(1).usesRhythm());
		assertFalse("Checking note doesn't use rhythm", lowRhythms.get(2).usesRhythm());
		
		Rhythm one8 = new Rhythm(1, 8);
		tabForRhythms.addRhythm(one8);
		assertTrue("Checking using rhythm is set to true", tabForRhythms.usesRhythm());
		rhythmEqualTest(1, 8, highRhythms.get(0));
		
		tabForRhythms.removeRhythms();
		tabForRhythms.addRhythm(null);
		assertTrue("Checking using rhythm is set to true", tabForRhythms.usesRhythm());
		rhythmEqualTest(1, 1, highRhythms.get(0));
		rhythmEqualTest(1, 2, highRhythms.get(1));
		
		highRhythms.clear();
		lowRhythms.clear();
		tabForRhythms.addRhythm(null);
		assertEquals(0, highRhythms.size(), "Checking nothing happens when adding rhythms to empty strings");
		assertEquals(0, lowRhythms.size(), "Checking nothing happens when adding rhythms to empty strings");
	}
	
	@Test
	public void guessRhythms(){
		tabForRhythms.removeRhythms();
		tabForRhythms.guessRhythms();
		assertTrue("Checking using rhythm is set to true", tabForRhythms.usesRhythm());
		rhythmEqualTest(1, 1, highRhythms.get(0));
		rhythmEqualTest(1, 2, highRhythms.get(1));
		rhythmEqualTest(1, 4, highRhythms.get(2));
		rhythmEqualTest(1, 4, lowRhythms.get(0));
		rhythmEqualTest(1, 8, lowRhythms.get(1));
		rhythmEqualTest(1, 4, lowRhythms.get(2));
	}

	@Test
	public void setRhythmAll(){
		tabForRhythms.removeRhythms();
		tabForRhythms.setRhythmAll(new Rhythm(1, 8));
		assertTrue("Checking using rhythm is set to true", tabForRhythms.usesRhythm());
		rhythmEqualTest(1, 8, highRhythms.get(0));
		rhythmEqualTest(1, 8, highRhythms.get(1));
		rhythmEqualTest(1, 8, highRhythms.get(2));
		rhythmEqualTest(1, 8, lowRhythms.get(0));
		rhythmEqualTest(1, 8, lowRhythms.get(1));
		rhythmEqualTest(1, 8, lowRhythms.get(2));
	}
	
	/***
	 * Helper method for checking rhythmic equality and showing test information
	 * @param duration
	 * @param unit
	 * @param note
	 */
	private void rhythmEqualTest(int duration, int unit, TabSymbol note){
		Rhythm r = ((TabNoteRhythm)note).getRhythm();
		assertTrue("Checking note has correct rhythm."
				+ "Expected duration: " + duration + " and unit: " + unit + ", "
				+ "but got duration: " + r.getDuration() + " and unit: " + r.getUnit(),
				r.getDuration() == duration && r.getUnit() == unit);
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
		assertFalse("Checking objects are not equal after changing strings", t.equals(tab));
		s.add(lowString);
		assertTrue("Checking objects equal", t.equals(tab));
		
		t.setTimeSignature(new TimeSignature(1, 8));
		assertFalse("Checking objects not equal with different time signatures", t.equals(tab));
		t.setTimeSignature(new TimeSignature(4, 4));
		assertTrue("Checking objects equal", t.equals(tab));
		
		t.setUsesRhythm(true);
		assertFalse("Checking objects not equal with different rhythm usage", t.equals(tab));
	}
	
	@AfterEach
	public void end(){}
	
}
