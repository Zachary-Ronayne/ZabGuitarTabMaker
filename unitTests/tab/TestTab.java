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
	public void retime(){
		Tab copy;
		TabString copyH;
		TabString copyL;
		TimeSignature newTime;
		
		highString.add(TabFactory.modifiedFret(lowString, 0, 0, null));
		highString.add(TabFactory.modifiedFret(lowString, 0, 0.75, null));
		highString.add(TabFactory.modifiedFret(lowString, 0, 1, null));
		highString.add(TabFactory.modifiedFret(lowString, 0, 1.25, null));
		lowString.add(TabFactory.modifiedFret(lowString, 0, 2, null));
		lowString.add(TabFactory.modifiedFret(lowString, 0, 2.9, null));
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(5, 4);
		copy.retime(newTime, false, false);
		assertEquals(newTime, copy.getTimeSignature(), "Checking retime set the time signature");
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(0, copyH.get(0).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(0.6, copyH.get(1).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(0.8, copyH.get(2).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(1, copyH.get(3).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(1.6, copyL.get(0).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		assertEquals(2.32, copyL.get(1).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 5/4");
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(5, 4);
		copy.retime(newTime, false, true);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(0, copyH.get(0).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(0.6, copyH.get(1).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(1, copyH.get(2).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(1.2, copyH.get(3).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(2, copyL.get(0).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		assertEquals(2.72, copyL.get(1).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 5/4");
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(3, 4);
		copy.retime(newTime, false, false);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(0, copyH.get(0).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(1, copyH.get(1).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(1.33333333333333, copyH.get(2).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(1.66666666666667, copyH.get(3).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(2.66666666666667, copyL.get(0).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		assertEquals(3.86666666666667, copyL.get(1).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra false, converting 4/4 to 3/4");
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(3, 4);
		copy.retime(newTime, false, true);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(1, copyL.size(), "Checking one symbol was deleted with rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(0, copyH.get(0).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(1, copyH.get(1).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(1, copyH.get(2).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(1.33333333333333, copyH.get(3).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 3/4");
		assertEquals(2, copyL.get(0).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale false, deleteExtra true, converting 4/4 to 3/4");
		
		copy = tab.copy();
		copyH = copy.getStrings().get(0);
		copyL = copy.getStrings().get(1);
		newTime = new TimeSignature(5, 4);
		copy.retime(newTime, true, true);
		assertEquals(4, copyH.size(), "Checking no symbols were deleted with rescale true, converting 4/4 to 5/4");
		assertEquals(2, copyL.size(), "Checking no symbols were deleted with rescale true, converting 4/4 to 5/4");
		assertEquals(0, copyH.get(0).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
		assertEquals(0.75, copyH.get(1).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
		assertEquals(1, copyH.get(2).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
		assertEquals(1.25, copyH.get(3).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
		assertEquals(2, copyL.get(0).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
		assertEquals(2.9, copyL.get(1).getPos().getValue(), UtilsTest.DELTA,
				"Checking correct retimed note rescale true, converting 4/4 to 5/4");
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
