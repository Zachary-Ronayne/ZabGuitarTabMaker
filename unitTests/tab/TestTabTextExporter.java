package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import appUtils.settings.TabTextSettings;
import tab.TabTextExporter.IndexAndPos;
import tab.symbol.TabNote;
import util.testUtils.Assert;
import util.testUtils.UtilsTest;

public class TestTabTextExporter{

	private Tab guitar;
	
	private static TabTextSettings text;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		text = ZabAppSettings.get().text();
	}
	
	@BeforeEach
	public void setup(){
		init();
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
		highE.add(TabFactory.modifiedFret(highE, 6, 3.125));
		highE.add(TabFactory.modifiedFret(highE, 5, 3.25));

		b.add(TabFactory.modifiedFret(b, 0, 3.25));
		
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
		
		guitar.placeQuantizedNote(0, 0, 8);
		guitar.placeQuantizedNote(0, 1, 8.5);
		guitar.placeQuantizedNote(0, 2, 9);
		guitar.placeQuantizedNote(0, 3, 9.25);
		guitar.placeQuantizedNote(0, 4, 10);
		guitar.placeQuantizedNote(0, 5, 13);
	}
	
	@Test
	public void export(){
		assertEquals(null, TabTextExporter.export(null), "Checking null returned on null tab");
		
		assertEquals(""
				+ "F |-0-------|-3-------|-5----0---|-3-6-5-----|-0-------|---------|----------|-----------|\n"
				+ "C |---------|---------|----------|-----0-----|-0-------|---------|----------|-----------|\n"
				+ "G#|---------|---------|----------|-----------|-0-------|---------|----------|-----------|\n"
				+ "D#|---------|---------|----------|-----------|-2-------|---------|----------|-----------|\n"
				+ "A#|---------|---------|----------|-----------|-2-------|-2-------|-14-------|-8p--------|\n"
				+ "F |---------|---------|----------|-----------|-0-------|-0-------|-12-------|-10p-------|\n"
				+ "\n"
				+ "F |-0----1---|-2--3-----|-4-------|--------|--------|-5-------|--------|--------|\n"
				+ "C |----------|----------|---------|--------|--------|---------|--------|--------|\n"
				+ "G#|----------|----------|---------|--------|--------|---------|--------|--------|\n"
				+ "D#|----------|----------|---------|--------|--------|---------|--------|--------|\n"
				+ "A#|----------|----------|---------|--------|--------|---------|--------|--------|\n"
				+ "F |----------|----------|---------|--------|--------|---------|--------|--------|\n",
				TabTextExporter.export(guitar),
				"Checking exporting guitar with default settings");
		
		text.getNoteNameOctave().set(true);
		text.getNoteNameFormat().set(TabTextExporter.NOTE_FORMAT_ALL_FLAT);
		text.getNoteNameAlignEnd().set(true);
		text.getAlignSymbolsEnd().set(true);
		text.getEnd().set("|]");
		text.getPreString().set("[");
		text.getMeasuresPerLine().set(3);
		text.getUseSpacing().set(false);
		
		assertEquals(""
				+ "[ F4|-0-3-5-0|]\n"
				+ "[ C4|--------|]\n"
				+ "[Ab3|--------|]\n"
				+ "[Eb3|--------|]\n"
				+ "[Bb2|--------|]\n"
				+ "[ F2|--------|]\n"
				+ "\n"
				+ "[ F4|-3-6-5-0--|]\n"
				+ "[ C4|-----0-0--|]\n"
				+ "[Ab3|-------0--|]\n"
				+ "[Eb3|-------2--|]\n"
				+ "[Bb2|-------2-2|]\n"
				+ "[ F2|-------0-0|]\n"
				+ "\n"
				+ "[ F4|--------0-1|]\n"
				+ "[ C4|-----------|]\n"
				+ "[Ab3|-----------|]\n"
				+ "[Eb3|-----------|]\n"
				+ "[Bb2|-14--8p----|]\n"
				+ "[ F2|-12-10p----|]\n"
				+ "\n"
				+ "[ F4|-2-3-4|]\n"
				+ "[ C4|------|]\n"
				+ "[Ab3|------|]\n"
				+ "[Eb3|------|]\n"
				+ "[Bb2|------|]\n"
				+ "[ F2|------|]\n"
				+ "\n"
				+ "[ F4|-5|]\n"
				+ "[ C4|--|]\n"
				+ "[Ab3|--|]\n"
				+ "[Eb3|--|]\n"
				+ "[Bb2|--|]\n"
				+ "[ F2|--|]\n",
				TabTextExporter.export(guitar),
				"Checking exporting guitar with modified settings");
	}
	
	@Test
	public void exportLine(){
		assertEquals(null, TabTextExporter.exportLine(null, 0, 1, false), "Checking null returned on null tab");
		
		assertEquals(""
				+ "F |-0-------|\n"
				+ "C |---------|\n"
				+ "G#|---------|\n"
				+ "D#|---------|\n"
				+ "A#|---------|\n"
				+ "F |---------|\n",
				TabTextExporter.exportLine(guitar, 0, 1, false),
				"Checking exporting only the first measure with default settings");
		assertEquals(null, TabTextExporter.exportLine(null, 0, 1, false), "Checking null returned on null tab");
		
		text.getUseSpacing().set(false);
		assertEquals(""
				+ "F |-0|\n"
				+ "C |--|\n"
				+ "G#|--|\n"
				+ "D#|--|\n"
				+ "A#|--|\n"
				+ "F |--|\n",
				TabTextExporter.exportLine(guitar, 0, 1, false),
				"Checking exporting only the first measure with no spacing");
		
		text.getUseSpacing().set(true);
		assertEquals(""
				+ "F |----|-3-------|-5---|\n"
				+ "C |----|---------|-----|\n"
				+ "G#|----|---------|-----|\n"
				+ "D#|----|---------|-----|\n"
				+ "A#|----|---------|-----|\n"
				+ "F |----|---------|-----|\n",
				TabTextExporter.exportLine(guitar, 0.5, 2.5, false),
				"Checking exporting some in the middle of measures with spacing");
		
		assertEquals(""
				+ "F |-0---|-3-6-5|\n"
				+ "C |-----|-----0|\n"
				+ "G#|-----|------|\n"
				+ "D#|-----|------|\n"
				+ "A#|-----|------|\n"
				+ "F |-----|------|\n",
				TabTextExporter.exportLine(guitar, 2.5, 3.25, true),
				"Checking exporting regions between measures with a specified hard ending point, with default settings");
		
		assertEquals(""
				+ "F |-0---|-3-6|\n"
				+ "C |-----|----|\n"
				+ "G#|-----|----|\n"
				+ "D#|-----|----|\n"
				+ "A#|-----|----|\n"
				+ "F |-----|----|\n",
				TabTextExporter.exportLine(guitar, 2.5, 3.25, false),
				"Checking exporting regions between measures with no hard ending point, with default settings");
	}
	
	@Test
	public void appendSymbolsNoSpace(){
		String[] strs = appendBase();
		TabTextExporter.appendSymbolsNoSpace(strs, 1, 3.25, guitar.getStrings(), true);
		assertEquals(""
				+ "[0]-3-5-0-3-6-5\n"
				+ "[1]-----------0\n"
				+ "[2]------------\n"
				+ "[3]------------\n"
				+ "[4]------------\n"
				+ "[5]------------",
				String.join("\n", strs), "Checking correct text added with hard end");

		strs = appendBase();
		TabTextExporter.appendSymbolsNoSpace(strs, 1, 3.25, guitar.getStrings(), false);
		assertEquals(""
				+ "[0]-3-5-0-3-6\n"
				+ "[1]----------\n"
				+ "[2]----------\n"
				+ "[3]----------\n"
				+ "[4]----------\n"
				+ "[5]----------",
				String.join("\n", strs), "Checking correct text added with no hard end");
		
		strs = appendBase();
		TabTextExporter.appendSymbolsNoSpace(strs, 7, 10.1, guitar.getStrings(), false);
		assertEquals(""
				+ "[0]-----0-1-2-3-4\n"
				+ "[1]--------------\n"
				+ "[2]--------------\n"
				+ "[3]--------------\n"
				+ "[4]-8p-----------\n"
				+ "[5]-10p----------",
				String.join("\n", strs), "Checking correct text added with modifiers");
	}
	
	@Test
	public void appendSymbolsWithSpace(){
		String[] strs = appendBase();
		TabTextExporter.appendSymbolsWithSpace(strs, 1, 3.25, guitar.getStrings(), true);
		assertEquals(""
				+ "[0]-3-------|-5----0---|-3-6-5\n"
				+ "[1]---------|----------|-----0\n"
				+ "[2]---------|----------|------\n"
				+ "[3]---------|----------|------\n"
				+ "[4]---------|----------|------\n"
				+ "[5]---------|----------|------",
				String.join("\n", strs), "Checking correct text added with hard end");

		strs = appendBase();
		TabTextExporter.appendSymbolsWithSpace(strs, 1, 3.25, guitar.getStrings(), false);
		assertEquals(""
				+ "[0]-3-------|-5----0---|-3-6\n"
				+ "[1]---------|----------|----\n"
				+ "[2]---------|----------|----\n"
				+ "[3]---------|----------|----\n"
				+ "[4]---------|----------|----\n"
				+ "[5]---------|----------|----",
				String.join("\n", strs), "Checking correct text added with no hard end");

		strs = appendBase();
		TabTextExporter.appendSymbolsWithSpace(strs, 7, 10.1, guitar.getStrings(), false);
		assertEquals(""
				+ "[0]-----------|-0----1---|-2--3-----|-4\n"
				+ "[1]-----------|----------|----------|--\n"
				+ "[2]-----------|----------|----------|--\n"
				+ "[3]-----------|----------|----------|--\n"
				+ "[4]-8p--------|----------|----------|--\n"
				+ "[5]-10p-------|----------|----------|--",
				String.join("\n", strs), "Checking correct text added with modifiers");
		
		strs = appendBase();
		TabTextExporter.appendSymbolsWithSpace(strs, 7, 10, guitar.getStrings(), false);
		assertEquals(""
				+ "[0]-----------|-0----1---|-2--3-----\n"
				+ "[1]-----------|----------|----------\n"
				+ "[2]-----------|----------|----------\n"
				+ "[3]-----------|----------|----------\n"
				+ "[4]-8p--------|----------|----------\n"
				+ "[5]-10p-------|----------|----------",
				String.join("\n", strs), "Checking correct text ending on an exact measure");
		
		text.getMeasureSeparator().set("");
		strs = appendBase();
		TabTextExporter.appendSymbolsWithSpace(strs, 7, 10.1, guitar.getStrings(), false);
		assertEquals(""
				+ "[0]------------0----1----2--3------4\n"
				+ "[1]---------------------------------\n"
				+ "[2]---------------------------------\n"
				+ "[3]---------------------------------\n"
				+ "[4]-8p------------------------------\n"
				+ "[5]-10p-----------------------------",
				String.join("\n", strs), "Checking correct text with no measure separator");
	}
	
	/**
	 * Helper method for resetting a string array for testing appendSymbolsNoSpace and appendSymbolsWithSpace  
	 * @return
	 */
	private String[] appendBase(){
		return new String[]{"[0]", "[1]", "[2]", "[3]", "[4]", "[5]"};
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
