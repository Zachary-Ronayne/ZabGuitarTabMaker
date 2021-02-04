package appUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import settings.Setting;
import tab.InstrumentFactory;
import tab.Tab;
import util.testUtils.UtilsTest;

public class TestZabFileSaver{

	private ByteArrayOutputStream bytes;
	private BufferedOutputStream bs;
	private PrintWriter write;
	
	private Tab guitar;
	
	private static ArrayList<Setting<?>> settings;
	
	/** A string containing the saved version of the default settings */
	private static final String DEFAULT_SETTINGS = ""
			+ "h\n"
			+ "h\n"
			+ "p\n"
			+ "p\n"
			+ "/\n"
			+ "/\n"
			+ "\\\n"
			+ "\\\n"
			+ "<\n"
			+ "<\n"
			+ ">\n"
			+ ">\n"
			+ "X\n"
			+ "X\n"
			+ "b\n"
			+ "b\n"
			+ "(\n"
			+ "(\n"
			+ ")\n"
			+ ")\n"
			+ "ph\n"
			+ "ph\n"
			+ "~\n"
			+ "~\n"
			+ "t\n"
			+ "t\n"
			+ "\n"
			+ "\n"
			+ "|\n"
			+ "|\n"
			+ " \n"
			+ " \n"
			+ "false false \n"
			+ "false false \n"
			+ "1 1 null null \n"
			+ "-\n"
			+ "-\n"
			+ "\n"
			+ "\n"
			+ "-\n"
			+ "-\n"
			+ "false false \n"
			+ "|\n"
			+ "|\n"
			+ "8 8 null null \n"
			+ "100.0 100.0 null null \n"
			+ "-100.0 -100.0 null null \n"
			+ "200.0 200.0 null null \n"
			+ "8 8 null null \n"
			+ "30.0 30.0 null null \n"
			+ "15.0 15.0 null null \n"
			+ "40.0 40.0 null null \n"
			+ "40.0 40.0 null null \n"
			+ "2 2 null null \n"
			+ "1 1 null null \n"
			+ "1 1 null null \n"
			+ "2 2 null null \n"
			+ "3 3 null null \n"
			+ "1 1 null null \n"
			+ "2.0 2.0 null null \n"
			+ "14.0 14.0 null null \n"
			+ "false false \n"
			+ "false false \n"
			+ "0.1 0.1 null null \n"
			+ "true true \n"
			+ "40.0 40.0 null null \n"
			+ "false false \n"
			+ "false false \n"
			+ "8.0 8.0 null null \n"
			+ "1 4 \n"
			+ "1 4 \n";
	
	/** A string containing the saved version of a standard guitar */
	private static final String STANDARD_GUITAR = ""
			+ "false 4 4 \n"
			+ "6\n"
			+ "4 \n"
			+ "0\n"
			+ "-1 \n"
			+ "0\n"
			+ "-5 \n"
			+ "0\n"
			+ "-10 \n"
			+ "0\n"
			+ "-15 \n"
			+ "0\n"
			+ "-20 \n"
			+ "0\n";
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		
		settings = ZabAppSettings.get().getAll();
	}

	@BeforeEach
	public void setup(){
		guitar = InstrumentFactory.guitarStandard();
		UtilsTest.createUnitFolder();
	}
	
	@Test
	public void makeFileName(){
		assertEquals("path/name.zab", ZabFileSaver.makeFileName("path", "name"), "Checking name generated with no slash");
		assertEquals("path/name.zab", ZabFileSaver.makeFileName("path/", "name"), "Checking name generated with slash");
		assertEquals("/name.zab", ZabFileSaver.makeFileName(null, "name"), "Checking name generated with null path");
		assertEquals("path/.zab", ZabFileSaver.makeFileName("path", null), "Checking name generated with null name");
		assertEquals("/.zab", ZabFileSaver.makeFileName(null, null), "Checking name generated with null path and name");
	}
	
	@Test
	public void load(){
		init();
		
		Scanner scan = new Scanner(STANDARD_GUITAR);

		assertFalse(ZabFileSaver.load(new Scanner(""), guitar, true), "Checking load fails with invalid scanner");
		assertFalse(ZabFileSaver.load(scan, null, false), "Checking load fails with null tab and not saving settings");
		
		scan.close();
		scan = new Scanner(""
				+ DEFAULT_SETTINGS
				+ STANDARD_GUITAR
				+ DEFAULT_SETTINGS);
		
		ArrayList<Setting<?>> settingsCopy = new ArrayList<>();
		settingsCopy.addAll(settings);
		Tab tunedGuitar = InstrumentFactory.guitarEbStandard();
		assertTrue(ZabFileSaver.load(scan, tunedGuitar, true), "Checking load successful with tab");
		assertEquals(guitar, tunedGuitar, "Checking correct tab loaded in");
		assertEquals(settingsCopy, settings, "Checking correct settings loaded in");
		
		assertTrue(ZabFileSaver.load(scan, null, true), "Checking load successful with no tab");
		assertEquals(settingsCopy, settings, "Checking correct settings loaded in");

		scan.close();
		scan = new Scanner(STANDARD_GUITAR);
		settingsCopy = new ArrayList<>();
		settingsCopy.addAll(settings);
		tunedGuitar = InstrumentFactory.guitarEbStandard();
		assertTrue(ZabFileSaver.load(scan, tunedGuitar, false), "Checking load successful with no settings");
		assertEquals(settingsCopy, settings, "Checking settings unchanged");
		assertEquals(guitar, tunedGuitar, "Checking correct tab loaded in");
		
		assertFalse(ZabFileSaver.load(scan, null, true), "Checking load fails with nothing left to load");
		
		scan.close();
		scan = new Scanner(""
				+ STANDARD_GUITAR
				+ "false 4 4 \n"
				+ "6\n");
		
		assertFalse(ZabFileSaver.load(scan, new Tab(), true), "Checking load fails with invalid formatted tab save file");
		
		scan.close();
		scan = new Scanner(""
				+ "false 5 4 4 \n"
				+ "6\n"
				+ "4 \n"
				+ "0\n"
				+ "-1 \n"
				+ "0\n"
				+ "-5 \n"
				+ "0\n"
				+ "-10 \n"
				+ "0\n"
				+ "-15 \n"
				+ "0\n"
				+ "-20 \n");
		
		guitar.getStrings().add(null);
		assertFalse(ZabFileSaver.load(scan, guitar, false), "Checking load fails with invalid tab");
		guitar.getStrings().remove(6);

		settings.add(0, null);
		assertFalse(ZabFileSaver.load(scan, null, true), "Checking load fails with invalid settings");
		settings.remove(0);
		
		scan.close();
		
		assertFalse(ZabFileSaver.load(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, null), "Checking load from file fails with no file existing");
		
		ZabFileSaver.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, null, true);
		assertTrue(ZabFileSaver.load(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, null, true), "Checking load settings from file successful");
		assertFalse(ZabFileSaver.load(UtilsTest.UNIT_PATH + "/path", UtilsTest.UNIT_NAME, null, true), "Checking load from file fails with invalid file");

		assertTrue(ZabFileSaver.load(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME), "Checking load from file successful with no tab");
		
		assertFalse(ZabFileSaver.load((File)null, guitar, false), "Checking load fails with null file");
		assertFalse(ZabFileSaver.load(new File(UtilsTest.UNIT_PATH), guitar, false), "Checking load fails with file not found");
	}
	
	@Test
	public void save(){
		ZabAppSettings.init();
		
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertFalse(ZabFileSaver.save(write, null, false), "Checking save fails with null tab and not saving settings");
		write.close();
		
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertTrue(ZabFileSaver.save(write, guitar), "Checking save successful saving settings and a tab");
		write.close();
		String text = UtilsTest.removeSlashR(bytes.toString());
		
		assertEquals(""
				+ DEFAULT_SETTINGS
				+ STANDARD_GUITAR, text, "Checking correct text saved with a tab");

		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertTrue(ZabFileSaver.save(write, null), "Checking save successful with null tab");
		write.close();
		text = UtilsTest.removeSlashR(bytes.toString());
		assertEquals(DEFAULT_SETTINGS, text, "Checking correct text saved with no tab");
		
		assertFalse(ZabFileSaver.save(null, new Tab()), "Checking save fails with invalid writer");
		
		guitar.getStrings().add(0, null);
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertFalse(ZabFileSaver.save(write, guitar), "Checking save fails with invalid tab");
		write.close();
		
		guitar.getStrings().remove(0);
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertTrue(ZabFileSaver.save(write, guitar, false), "Checking save successful with tab only");
		write.close();
		
		assertTrue(ZabFileSaver.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME), "Checking save settings to file successful");
		assertFalse(ZabFileSaver.save(UtilsTest.UNIT_PATH + "/path", UtilsTest.UNIT_NAME), "Checking save to file fails with invalid file");

		assertTrue(ZabFileSaver.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME), "Checking save to file successful with no tab");
		
		assertTrue(ZabFileSaver.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, guitar), "Checking save to file successful with only tab");
		
		assertFalse(ZabFileSaver.save((File)null, guitar, false), "Checking save to file fails with null file");
	}
	
	@AfterEach
	public void end(){
		UtilsTest.deleteUnitFolder();
	}
	
}
