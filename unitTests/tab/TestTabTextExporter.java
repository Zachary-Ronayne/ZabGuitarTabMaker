package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import appUtils.ZabSettings;
import tab.TabTextExporter.IndexAndPos;
import tab.symbol.TabNote;
import util.testUtils.Assert;
import util.testUtils.UtilsTest;

public class TestTabTextExporter{

	private Tab guitar;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		guitar = InstrumentFactory.guitarTuned(1);
		TabString highE = guitar.getStrings().get(0);
		TabString b = guitar.getStrings().get(1);
		TabString g = guitar.getStrings().get(2);
		TabString d = guitar.getStrings().get(3);
		TabString a = guitar.getStrings().get(4);
		TabString lowE = guitar.getStrings().get(5);
		highE.add(TabFactory.modifiedFret(highE, 0, 0));
		highE.add(TabFactory.modifiedFret(highE, 3, 1));
		highE.add(TabFactory.modifiedFret(highE, 5, 2));
		highE.add(TabFactory.modifiedFret(highE, 0, 2.5));
		highE.add(TabFactory.modifiedFret(highE, 3, 3));
		highE.add(TabFactory.modifiedFret(highE, 6, 3.1));
		highE.add(TabFactory.modifiedFret(highE, 5, 3.2));

		b.add(TabFactory.modifiedFret(b, 0, 3.2));
		
		highE.add(TabFactory.modifiedFret(highE, 0, 4));
		b.add(TabFactory.modifiedFret(b, 0, 4));
		g.add(TabFactory.modifiedFret(g, 0, 4));
		d.add(TabFactory.modifiedFret(d, 2, 4));
		a.add(TabFactory.modifiedFret(a, 2, 4));
		lowE.add(TabFactory.modifiedFret(lowE, 0, 4));

		a.add(TabFactory.modifiedFret(a, 2, 5));
		lowE.add(TabFactory.modifiedFret(lowE, 0, 5));
		
		a.add(TabFactory.modifiedFret(a, 14, 6));
		lowE.add(TabFactory.modifiedFret(lowE, 12, 6));
		
		a.add(TabFactory.pullOff(a, 8, 7));
		lowE.add(TabFactory.pullOff(lowE, 10, 7));
	}
	
	@Test
	public void export(){
		assertEquals(null, TabTextExporter.export(null), "Checking null returned on null tab");
		
		assertEquals(""
				+ "F |-0--3--5--0--3--6--5--0-------------|\n"
				+ "C |-------------------0--0-------------|\n"
				+ "G#|----------------------0-------------|\n"
				+ "D#|----------------------2-------------|\n"
				+ "A#|----------------------2--2--14--p8--|\n"
				+ "F |----------------------0--0--12--p10-|\n",
				TabTextExporter.export(guitar),
				"Checking exporting guitar with default settings");
		
		ZabSettings settings = ZabAppSettings.get();
		settings.getTabTextNoteNameOctave().set(true);
		settings.getTabTextNoteNameFormat().set(TabTextExporter.NOTE_FORMAT_ALL_FLAT);
		settings.getTabTextNoteNameAlignEnd().set(true);
		settings.getTabTextAlignSymbolsEnd().set(true);
		settings.getTabTextEnd().set("|]");
		settings.getTabTextPreString().set("[");;
		
		assertEquals(""
				+ "[ F4|-0--3--5--0--3--6--5--0-------------|]\n"
				+ "[ C4|-------------------0--0-------------|]\n"
				+ "[Ab3|----------------------0-------------|]\n"
				+ "[Eb3|----------------------2-------------|]\n"
				+ "[Bb2|----------------------2--2--14---p8-|]\n"
				+ "[ F2|----------------------0--0--12--p10-|]\n",
				TabTextExporter.export(guitar),
				"Checking exporting guitar with modified settings");
	}
	
	@Test
	public void exportToFile(){
		assertTrue(TabTextExporter.exportToFile(guitar, UtilsTest.UNIT_PATH, "test"), "Checking file export successful");

		assertFalse(TabTextExporter.exportToFile(null, UtilsTest.UNIT_PATH, "test"), "Checking file export fails with null tab");
		
		assertFalse(TabTextExporter.exportToFile(guitar, null), "Checking file export fails with null file");
		
		assertTrue(TabTextExporter.exportToFile(guitar, UtilsTest.UNIT_PATH + "/testFolder", "test"),
				"Checking file export successful when making directory");
		
		assertTrue(TabTextExporter.exportToFile(guitar, UtilsTest.UNIT_PATH + "/testFolder/subFolder", "test"),
				"Checking file export successful when making sub directory");
		
	}
	
	@Test
	public void constructorIndexAndSymbol(){
		TabPosition note = new TabPosition(new TabNote(1), 2);
		IndexAndPos i = new IndexAndPos(note, 3);
		assertEquals(note, i.pos, "Checking symbol initialized");
		assertEquals(3, i.index, "Checking index initialized");
	}
	
	@Test
	public void compareToIndexAndSymbol(){
		IndexAndPos i1 = new IndexAndPos(new TabPosition(new TabNote(0), 0), 0);
		IndexAndPos i2 = new IndexAndPos(new TabPosition(new TabNote(0), 0.2), 1);
		IndexAndPos i3 = new IndexAndPos(new TabPosition(new TabNote(0), 0.2), 2);
		Assert.lessThan(i1.compareTo(i2), 0);
		Assert.greaterThan(i2.compareTo(i1), 0);
		assertEquals(0, i2.compareTo(i3), "Checking value equal to 0");
		assertEquals(0, i3.compareTo(i2), "Checking value equal to 0");
	}
	
	@AfterEach
	public void end(){
		UtilsTest.deleteUnitFolder();
	}
	
}
