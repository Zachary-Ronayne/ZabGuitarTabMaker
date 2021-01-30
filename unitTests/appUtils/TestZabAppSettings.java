package appUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appMain.gui.ZabGui;
import appMain.gui.ZabTheme;
import appMain.gui.ZabTheme.DarkTheme;
import appMain.gui.ZabTheme.LightTheme;
import settings.Setting;
import tab.InstrumentFactory;
import tab.Tab;
import util.testUtils.Assert;
import util.testUtils.UtilsTest;

public class TestZabAppSettings{

	private ByteArrayOutputStream bytes;
	private BufferedOutputStream bs;
	private PrintWriter write;
	
	private File unitFile;
	
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
			+ "-\n"
			+ "-\n"
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
		unitFile = new File(ZabAppSettings.makeFileName(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME));
		unitFile.getParentFile().mkdirs();
		
		guitar = InstrumentFactory.guitarStandard();
	}
	
	@Test
	public void reset(){
		ZabAppSettings.reset();
		assertEquals(null, ZabAppSettings.get(), "Checking settings set to null");
		assertEquals(null, ZabAppSettings.theme(), "Checking theme set to null");
		ZabAppSettings.init();
	}
	
	@Test
	public void get(){
		assertNotEquals(null, ZabAppSettings.get(), "Checking settings are initialized");
	}
	
	@Test
	public void theme(){
		assertNotEquals(null, ZabAppSettings.theme(), "Checking theme initialized");
	}
	
	@Test
	public void setTheme(){
		ZabGui gui = new ZabGui();
		gui.setVisible(false);
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), gui, false);
		ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), null, true);
		
		gui.dispose();
	}
	
	@Test
	public void getThemeFile(){
		assertEquals(new File(ZabAppSettings.THEME_SAVE_LOCATION), ZabAppSettings.getThemeFile(), "Checking file correct");
	}
	
	@Test
	public void loadDefaultTheme(){
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		ZabAppSettings.loadDefaultTheme();
		Assert.isInstance(DarkTheme.class, ZabAppSettings.theme(), "Checking default theme set");
	}
	
	@Test
	public void loadTheme() throws FileNotFoundException{
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		ZabAppSettings.getThemeFile().delete();
		assertFalse(ZabAppSettings.loadTheme(), "Checking theme failed to load from file");
		Assert.isInstance(DarkTheme.class, ZabAppSettings.theme(), "Checking default theme set");
		
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, true);
		assertTrue(ZabAppSettings.loadTheme(), "Checking theme loaded");
		Assert.isInstance(LightTheme.class, ZabAppSettings.theme(), "Checking light theme set");

		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, true);
		File f = ZabAppSettings.getThemeFile();
		PrintWriter write = new PrintWriter(f);
		write.print("a");
		write.close();
		assertFalse(ZabAppSettings.loadTheme(), "Checking theme failed to load from file with invalid theme name");
		Assert.isInstance(DarkTheme.class, ZabAppSettings.theme(), "Checking default theme set");

		ZabAppSettings.setTheme(new LightTheme(), null, false);
		ZabAppSettings.getThemeFile().delete();
		assertFalse(ZabAppSettings.loadTheme(), "Checking theme failed to load from file with missing file");
		Assert.isInstance(DarkTheme.class, ZabAppSettings.theme(), "Checking default theme set");
	}
	
	@Test
	public void saveTheme(){
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		assertTrue(ZabAppSettings.saveTheme(), "Checking save successful");
		ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), null, false);
		assertTrue(ZabAppSettings.loadTheme(), "Checking load after save successful");
		Assert.isInstance(LightTheme.class, ZabAppSettings.theme(), "Checking light theme loaded");
		
		ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), null, false);
		assertTrue(ZabAppSettings.saveTheme(), "Checking save successful");
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		assertTrue(ZabAppSettings.loadTheme(), "Checking load after save successful");
		Assert.isInstance(DarkTheme.class, ZabAppSettings.theme(), "Checking dark theme loaded");
		
		ZabAppSettings.reset();
		assertFalse(ZabAppSettings.saveTheme(), "Checking reset theme fails to save");
		ZabAppSettings.init();
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
		init();
		
		Scanner scan = new Scanner(STANDARD_GUITAR);

		assertFalse(ZabAppSettings.load(new Scanner(""), guitar, true), "Checking load fails with invalid scanner");
		assertFalse(ZabAppSettings.load(scan, null, false), "Checking load fails with null tab and not saving settings");
		
		scan.close();
		scan = new Scanner(""
				+ DEFAULT_SETTINGS
				+ STANDARD_GUITAR
				+ DEFAULT_SETTINGS);
		
		ArrayList<Setting<?>> settingsCopy = new ArrayList<>();
		settingsCopy.addAll(settings);
		Tab tunedGuitar = InstrumentFactory.guitarEbStandard();
		assertTrue(ZabAppSettings.load(scan, tunedGuitar, true), "Checking load successful with tab");
		assertEquals(guitar, tunedGuitar, "Checking correct tab loaded in");
		assertEquals(settingsCopy, settings, "Checking correct settings loaded in");
		
		assertTrue(ZabAppSettings.load(scan, null, true), "Checking load successful with no tab");
		assertEquals(settingsCopy, settings, "Checking correct settings loaded in");

		scan.close();
		scan = new Scanner(STANDARD_GUITAR);
		settingsCopy = new ArrayList<>();
		settingsCopy.addAll(settings);
		tunedGuitar = InstrumentFactory.guitarEbStandard();
		assertTrue(ZabAppSettings.load(scan, tunedGuitar, false), "Checking load successful with no settings");
		assertEquals(settingsCopy, settings, "Checking settings unchanged");
		assertEquals(guitar, tunedGuitar, "Checking correct tab loaded in");
		
		assertFalse(ZabAppSettings.load(scan, null, true), "Checking load fails with nothing left to load");
		
		scan.close();
		scan = new Scanner(""
				+ STANDARD_GUITAR
				+ "false 4 4 \n"
				+ "6\n");
		
		assertFalse(ZabAppSettings.load(scan, new Tab(), true), "Checking load fails with invalid formatted tab save file");
		
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
		assertFalse(ZabAppSettings.load(scan, guitar, false), "Checking load fails with invalid tab");
		guitar.getStrings().remove(6);

		settings.add(0, null);
		assertFalse(ZabAppSettings.load(scan, null, true), "Checking load fails with invalid settings");
		settings.remove(0);
		
		scan.close();
		
		assertFalse(ZabAppSettings.load(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, null), "Checking load from file fails with no file existing");
		
		ZabAppSettings.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, null, true);
		assertTrue(ZabAppSettings.load(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, null, true), "Checking load settings from file successful");
		assertFalse(ZabAppSettings.load(UtilsTest.UNIT_PATH + "/path", UtilsTest.UNIT_NAME, null, true), "Checking load from file fails with invalid file");

		assertTrue(ZabAppSettings.load(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME), "Checking load from file successful with no tab");
		
		assertFalse(ZabAppSettings.load((File)null, guitar, false), "Checking load fails with null file");
		assertFalse(ZabAppSettings.load(new File(UtilsTest.UNIT_PATH), guitar, false), "Checking load fails with file not found");
	}
	
	@Test
	public void save(){
		ZabAppSettings.init();
		
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertFalse(ZabAppSettings.save(write, null, false), "Checking save fails with null tab and not saving settings");
		write.close();
		
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertTrue(ZabAppSettings.save(write, guitar), "Checking save successful saving settings and a tab");
		write.close();
		String text = UtilsTest.removeSlashR(bytes.toString());
		
		assertEquals(""
				+ DEFAULT_SETTINGS
				+ STANDARD_GUITAR, text, "Checking correct text saved with a tab");

		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertTrue(ZabAppSettings.save(write, null), "Checking save successful with null tab");
		write.close();
		text = UtilsTest.removeSlashR(bytes.toString());
		assertEquals(DEFAULT_SETTINGS, text, "Checking correct text saved with no tab");
		
		assertFalse(ZabAppSettings.save(null, new Tab()), "Checking save fails with invalid writer");
		
		guitar.getStrings().add(0, null);
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertFalse(ZabAppSettings.save(write, guitar), "Checking save fails with invalid tab");
		write.close();
		
		guitar.getStrings().remove(0);
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertTrue(ZabAppSettings.save(write, guitar, false), "Checking save successful with tab only");
		write.close();
		
		assertTrue(ZabAppSettings.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME), "Checking save settings to file successful");
		assertFalse(ZabAppSettings.save(UtilsTest.UNIT_PATH + "/path", UtilsTest.UNIT_NAME), "Checking save to file fails with invalid file");

		assertTrue(ZabAppSettings.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME), "Checking save to file successful with no tab");
		
		assertTrue(ZabAppSettings.save(UtilsTest.UNIT_PATH, UtilsTest.UNIT_NAME, guitar), "Checking save to file successful with only tab");
	}
	
	@AfterEach
	public void end(){
		UtilsTest.deleteUnitFolder();
	}
	
}
