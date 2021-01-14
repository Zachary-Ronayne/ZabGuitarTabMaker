package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		TabPosition p = TabFactory.modifiedPitch(note, octave, pos, mod);
		TabNote n = (TabNote)p.getSymbol();
		assertEquals(Music.createNote(note, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, p.getPosition().getValue(), "Checking note has correct position");
		assertEquals(mod, n.getModifier(), "Checking note has correct modifier");
	}
	
	@Test
	public void modifiedRhythm(){
		TabPosition p = TabFactory.modifiedRhythm(note, octave, rhythm, pos, mod);
		TabNoteRhythm n = (TabNoteRhythm)p.getSymbol();
		assertEquals(Music.createNote(note, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertTrue(n.getRhythm() == rhythm, "Checking note has correct rhythm");
		assertEquals(pos, p.getPosition().getValue(), "Checking note has correct position");
		assertEquals(mod, n.getModifier(), "Checking note has correct modifier");
	}
	
	@Test
	public void modifiedFret(){
		TabPosition p = TabFactory.modifiedFret(string, fret, pos, mod);
		TabNote n = (TabNote)p.getSymbol();
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, p.getPosition().getValue(), "Checking note has correct position");
		assertTrue(n.getModifier() == mod, "Checking note has correct modifier");
		
		p = TabFactory.modifiedFret(string, fret, pos);
		n = (TabNote)p.getSymbol();
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, p.getPosition().getValue(), "Checking note has correct position");
		assertEquals(new TabModifier(), n.getModifier(), "Checking note has empty modifier");
	}
	
	@Test
	public void modifiedFretRhythm(){
		TabPosition p = TabFactory.modifiedFretRhythm(string, fret, rhythm, pos, mod);
		TabNoteRhythm n = (TabNoteRhythm)p.getSymbol();
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertTrue(n.getRhythm() == rhythm, "Checking note has correct rhythm");
		assertEquals(pos, p.getPosition().getValue(), "Checking note has correct position");
		assertEquals(mod, n.getModifier(), "Checking note has correct modifier");
	}
	
	@Test
	public void hammerOn(){
		TabPosition p = TabFactory.hammerOn(string, fret, pos);
		TabNote n = (TabNote)p.getSymbol();
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, p.getPosition().getValue(), "Checking note has correct position");
		assertEquals(new TabModifier("h", ""), n.getModifier(), "Checking note has correct modifier");
	}
	
	@Test
	public void pullOff(){
		TabPosition p = TabFactory.pullOff(string, fret, pos);
		TabNote n = (TabNote)p.getSymbol();
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, p.getPosition().getValue(), "Checking note has correct position");
		assertEquals(new TabModifier("", "p"), n.getModifier(), "Checking note has correct modifier");
	}
	
	@Test
	public void harmonic(){
		TabPosition p = TabFactory.harmonic(string, fret, pos);
		TabNote n = (TabNote)p.getSymbol();
		assertEquals(Music.createNote(Music.E, octave), n.getPitch().getNote(), "Checking note has correct pitch");
		assertEquals(pos, p.getPosition().getValue(), "Checking note has correct position");
		assertEquals(new TabModifier("<", ">"), n.getModifier(), "Checking note has correct modifier");
	}
	
	@AfterEach
	public void end(){}
	
}