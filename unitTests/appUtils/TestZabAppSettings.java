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
	private File unitFolder;
	
	private Tab nullTab;
	private Tab guitar;
	
	private static ArrayList<Setting<?>> settings;
	
	private static final String UNIT_PATH = "./TestZabAppSettingsUnitTest";
	private static final String UNIT_NAME = "test";
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
		
		settings = ZabAppSettings.get().getAll();
	}
	
	@BeforeEach
	public void setup(){
		unitFolder = new File(UNIT_PATH);
		unitFile = new File(ZabAppSettings.makeFileName(UNIT_PATH, UNIT_NAME));
		unitFile.getParentFile().mkdirs();
		
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		
		nullTab = null;
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
				+ ">\n");

		ArrayList<Setting<?>> settingsCopy = new ArrayList<>();
		settingsCopy.addAll(settings);
		Tab tunedGuitar = InstrumentFactory.guitarEbStandard();
		assertTrue("Checking load successful with tab", ZabAppSettings.load(scan, tunedGuitar));
		assertEquals(guitar, tunedGuitar, "Checking correct tab loaded in");
		assertEquals(settingsCopy, settings, "Checking correct settings loaded in");

		assertTrue("Checking load successful with no tab", ZabAppSettings.load(scan, nullTab));
		assertEquals(settingsCopy, settings, "Checking correct settings loaded in");
		
		assertFalse("Checking load fails with nothing left to load", ZabAppSettings.load(scan, nullTab));
		assertFalse("Checking load fails with invalid scanner", ZabAppSettings.load(null, nullTab));
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
		assertFalse("Checking load fails with invalid tab", ZabAppSettings.load(scan, guitar));
		guitar.getStrings().remove(6);

		settings.add(0, null);
		assertFalse("Checking load fails with invalid settings", ZabAppSettings.load(scan, nullTab));
		settings.remove(0);
		
		scan.close();
		
		assertFalse("Checking load from file fails with no file existing", ZabAppSettings.load(UNIT_PATH, UNIT_NAME, nullTab));
		
		ZabAppSettings.save(UNIT_PATH, UNIT_NAME, nullTab);
		assertTrue("Checking load from file successful", ZabAppSettings.load(UNIT_PATH, UNIT_NAME, nullTab));
		assertFalse("Checking save to file fails with invalid file", ZabAppSettings.load(UNIT_PATH + "/path", UNIT_NAME, nullTab));

		assertTrue("Checking load from file successful with no tab", ZabAppSettings.load(UNIT_PATH, UNIT_NAME));
	}
	
	@Test
	public void save(){
		assertTrue("Checking save successful with a tab", ZabAppSettings.save(write, guitar));
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
		assertTrue("Checking save successful with null tab", ZabAppSettings.save(write, nullTab));
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
		
		assertFalse("Checking save fails with invalid writer", ZabAppSettings.save(null, nullTab));
		
		guitar.getStrings().add(null);
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertFalse("Checking save fails with invalid tab", ZabAppSettings.save(write, guitar));
		write.close();

		settings.add(0, null);
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertFalse("Checking save fails with invalid settings", ZabAppSettings.save(write, nullTab));
		settings.remove(0);
		write.close();
		
		assertTrue("Checking save to file successful", ZabAppSettings.save(UNIT_PATH, UNIT_NAME, nullTab));
		assertFalse("Checking save to file fails with invalid file", ZabAppSettings.save(UNIT_PATH + "/path", UNIT_NAME, nullTab));

		assertTrue("Checking save to file successful with no tab", ZabAppSettings.save(UNIT_PATH, UNIT_NAME));
	}
	
	@AfterEach
	public void end(){
		if(unitFile.exists()) unitFile.delete();
		if(unitFolder.exists()) unitFolder.delete();
	}
	
}
