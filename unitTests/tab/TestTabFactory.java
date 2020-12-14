package tab;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import music.Music;
import music.Rhythm;
import tab.symbol.TabModifier;
import tab.symbol.TabNote;
import tab.symbol.TabNoteRhythm;

public class TestTabFactory{
	
	private String note;
	private int octave;
	private double pos;
	private TabModifier mod;
	private Rhythm rhythm;
	
	private TabString string;
	private int fret;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		note = Music.C;
		octave = 4;
		pos = 1;
		mod = new TabModifier("(", ")");
		rhythm = new Rhythm(3, 4);
		
		string = new TabString(Music.createPitch(note, octave));
		fret = 4;
	}
	
	@Test
	public void modifiedPitch(){
		TabNote n = TabFactory.modifiedPitch(note, octave, pos, mod);
		assertEquals(Music.createNote(note, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, n.getPos().getValue(), "Checking note has correct position");
		assertTrue("Checking note has correct modifier", n.getModifier() == mod);
	}
	
	@Test
	public void modifiedRhythm(){
		TabNoteRhythm n = TabFactory.modifiedRhythm(note, octave, rhythm, pos, mod);
		assertEquals(Music.createNote(note, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertTrue("Checking note has correct rhythm", n.getRhythm() == rhythm);
		assertEquals(pos, n.getPos().getValue(), "Checking note has correct position");
		assertTrue("Checking note has correct modifier", n.getModifier().equals(mod));
	}
	
	@Test
	public void modifiedFret(){
		TabNote n = TabFactory.modifiedFret(string, fret, pos, mod);
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, n.getPos().getValue(), "Checking note has correct position");
		assertTrue("Checking note has correct modifier", n.getModifier() == mod);
		
		n = TabFactory.modifiedFret(string, fret, pos);
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, n.getPos().getValue(), "Checking note has correct position");
		assertTrue("Checking note has empty modifier", n.getModifier().equals(new TabModifier()));
	}
	
	@Test
	public void modifiedFretRhythm(){
		TabNoteRhythm n = TabFactory.modifiedFretRhythm(string, fret, rhythm, pos, mod);
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertTrue("Checking note has correct rhythm", n.getRhythm() == rhythm);
		assertEquals(pos, n.getPos().getValue(), "Checking note has correct position");
		assertTrue("Checking note has correct modifier", n.getModifier().equals(mod));
	}
	
	@Test
	public void hammerOn(){
		TabNote n = TabFactory.hammerOn(string, fret, pos);
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, n.getPos().getValue(), "Checking note has correct position");
		assertTrue("Checking note has correct modifier", n.getModifier().equals(new TabModifier("h", "")));
	}
	
	@Test
	public void pullOff(){
		TabNote n = TabFactory.pullOff(string, fret, pos);
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, n.getPos().getValue(), "Checking note has correct position");
		assertTrue("Checking note has correct modifier", n.getModifier().equals(new TabModifier("p", "")));
	}
	
	@Test
	public void harmonic(){
		TabNote n = TabFactory.harmonic(string, fret, pos);
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, n.getPos().getValue(), "Checking note has correct position");
		assertTrue("Checking note has correct modifier", n.getModifier().equals(new TabModifier("<", ">")));
	}
	
	@AfterEach
	public void end(){}
	
}