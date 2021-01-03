package appUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

public class TestZabAppSettings{

	private ByteArrayOutputStream bytes;
	private BufferedOutputStream bs;
	private PrintWriter write;
	
	private File unitFile;
	
	private Tab guitar;
	
	private static ArrayList<Setting<?>> settings;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		
		settings = ZabAppSettings.get().getAll();
	}
	
	@BeforeEach
	public void setup(){
		unitFile = new File(ZabAppSettings.makeFileName(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME));
		unitFile.getParentFile().mkdirs();
		
		guitar = InstrumentFactory.guitarStandard();
	}
	
	@Test
	public void get(){
		assertNotEquals(null, ZabAppSettings.get(), "Checking settings are initialized");
	}
	
	@Test
	public void makeFileName(){
		assertEquals("path/name.zab", ZabAppSettings.makeFileName("path", "name"), "Checking name generated with no slash");
		assertEquals("path/name.zab", ZabAppSettings.makeFileName("path/", "name"), "Checking name generated with slash");
		assertEquals("/name.zab", ZabAppSettings.makeFileName(null, "name"), "Checking name generated with null path");
		assertEquals("path/.zab", ZabAppSettings.makeFileName("path", null), "Checking name generated with null name");
		assertEquals("/.zab", ZabAppSettings.makeFileName(null, null), "Checking name generated with null path and name");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner(""
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
				+ "0\n"
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
				+ "0\n");

		assertFalse("Checking load fails with invalid scanner", ZabAppSettings.load(null, guitar, true));
		assertFalse("Checking load fails with null tab and not saving settings", ZabAppSettings.load(scan, null, false));
		
		ArrayList<Setting<?>> settingsCopy = new ArrayList<>();
		settingsCopy.addAll(settings);
		Tab tunedGuitar = InstrumentFactory.guitarEbStandard();
		assertTrue("Checking load successful with tab", ZabAppSettings.load(scan, tunedGuitar, true));
		assertEquals(guitar, tunedGuitar, "Checking correct tab loaded in");
		assertEquals(settingsCopy, settings, "Checking correct settings loaded in");
		
		assertTrue("Checking load successful with no tab", ZabAppSettings.load(scan, null, true));
		assertEquals(settingsCopy, settings, "Checking correct settings loaded in");

		settingsCopy = new ArrayList<>();
		settingsCopy.addAll(settings);
		tunedGuitar = InstrumentFactory.guitarEbStandard();
		assertTrue("Checking load successful with no settings", ZabAppSettings.load(scan, tunedGuitar, false));
		assertEquals(settingsCopy, settings, "Checking settings unchanged");
		assertEquals(guitar, tunedGuitar, "Checking correct tab loaded in");
		
		assertFalse("Checking load fails with nothing left to load", ZabAppSettings.load(scan, null, true));
		scan.close();
		
		scan = new Scanner(""
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
				+ "false 4 4 \n"
				+ "6\n");
		
		assertFalse("Checking load fails with invalid formatted tab save file", ZabAppSettings.load(scan, new Tab(), true));
		scan.close();
		
		scan = new Scanner(""
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
				+ "0\n"
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
				+ ">\n");
		
		guitar.getStrings().add(null);
		assertFalse("Checking load fails with invalid tab", ZabAppSettings.load(scan, guitar, true));
		guitar.getStrings().remove(6);

		settings.add(0, null);
		assertFalse("Checking load fails with invalid settings", ZabAppSettings.load(scan, null, true));
		settings.remove(0);
		
		scan.close();
		
		assertFalse("Checking load from file fails with no file existing", ZabAppSettings.load(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, null));
		
		ZabAppSettings.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, null, true);
		assertTrue("Checking load settings from file successful", ZabAppSettings.load(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, null, true));
		assertFalse("Checking load from file fails with invalid file", ZabAppSettings.load(UtilsTest.UNIT_PATH + "/path", UtilsTest.UNIT_NAME, null, true));

		assertTrue("Checking load from file successful with no tab", ZabAppSettings.load(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME));
	}
	
	@Test
	public void save(){
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		
		assertFalse("Checking save fails with null tab and not saving settings", ZabAppSettings.save(write, null, false));
		
		assertTrue("Checking save successful saving settings and a tab", ZabAppSettings.save(write, guitar));
		write.close();
		String text = UtilsTest.removeSlashR(bytes.toString());
		assertEquals(""
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
				+ "0\n", text, "Checking correct text saved with a tab");

		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertTrue("Checking save successful with null tab", ZabAppSettings.save(write, null));
		write.close();
		text = UtilsTest.removeSlashR(bytes.toString());
		assertEquals(""
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
				+ ">\n", text, "Checking correct text saved with no tab");
		
		assertFalse("Checking save fails with invalid writer", ZabAppSettings.save(null, new Tab()));
		
		guitar.getStrings().add(0, null);
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertFalse("Checking save fails with invalid tab", ZabAppSettings.save(write, guitar));
		write.close();
		
		guitar.getStrings().remove(0);
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertTrue("Checking save successful with tab only", ZabAppSettings.save(write, guitar, false));
		write.close();

		settings.add(0, null);
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertFalse("Checking save fails with invalid settings", ZabAppSettings.save(write, null));
		settings.remove(0);
		write.close();
		
		assertTrue("Checking save settings to file successful", ZabAppSettings.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME));
		assertFalse("Checking save to file fails with invalid file", ZabAppSettings.save(UtilsTest.UNIT_PATH + "/path", UtilsTest.UNIT_NAME));

		assertTrue("Checking save to file successful with no tab", ZabAppSettings.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME));
		
		assertTrue("Checking save to file successful with only tab", ZabAppSettings.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, guitar));
	}
	
	@AfterEach
	public void end(){
		UtilsTest.deleteUnitFolder();
	}
	
}
