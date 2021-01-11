package appUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
		assertTrue(ZabAppSettings.theme() instanceof ZabTheme.DarkTheme, "Checking default theme set");
	}
	
	@Test
	public void loadTheme(){
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		ZabAppSettings.getThemeFile().delete();
		assertFalse(ZabAppSettings.loadTheme(), "Checking theme failed to load from file");
		assertTrue(ZabAppSettings.theme() instanceof ZabTheme.DarkTheme, "Checking default theme set");
		
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, true);
		assertTrue(ZabAppSettings.loadTheme(), "Checking theme loaded");
		assertTrue(ZabAppSettings.theme() instanceof ZabTheme.LightTheme, "Checking light theme set");

		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, true);
		File f = ZabAppSettings.getThemeFile();
		try{
			PrintWriter write = new PrintWriter(f);
			write.print("a");
			write.close();
		}catch(FileNotFoundException e){
			System.err.println("Error in testing loading themes in TestZabAppSettings");
			e.printStackTrace();
		}
		assertFalse(ZabAppSettings.loadTheme(), "Checking theme failed to load from file with invalid theme name");
		assertTrue(ZabAppSettings.theme() instanceof ZabTheme.DarkTheme, "Checking default theme set");
	}
	
	@Test
	public void saveTheme(){
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		assertTrue(ZabAppSettings.saveTheme(), "Checking save successful");
		ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), null, false);
		assertTrue(ZabAppSettings.loadTheme(), "Checking load after save successful");
		assertTrue(ZabAppSettings.theme() instanceof ZabTheme.LightTheme, "Checking light theme loaded");
		
		ZabAppSettings.setTheme(new ZabTheme.DarkTheme(), null, false);
		assertTrue(ZabAppSettings.saveTheme(), "Checking save successful");
		ZabAppSettings.setTheme(new ZabTheme.LightTheme(), null, false);
		assertTrue(ZabAppSettings.loadTheme(), "Checking load after save successful");
		assertTrue(ZabAppSettings.theme() instanceof ZabTheme.DarkTheme, "Checking dark theme loaded");
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
		
		Scanner scan = new Scanner(""
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

		assertFalse("Checking load fails with invalid scanner", ZabAppSettings.load(new Scanner(""), guitar, true));
		assertFalse("Checking load fails with null tab and not saving settings", ZabAppSettings.load(scan, null, false));
		
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
				+ "0.1 0.1 null null \n"
				+ "true true \n"
				+ "2.0 2.0 null null \n"
				+ "8.0 8.0 null null \n"
				+ "1 4 \n"
				+ "1 4 \n"
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
				+ "0.1 0.1 null null \n"
				+ "true true \n"
				+ "2.0 2.0 null null \n"
				+ "8.0 8.0 null null \n"
				+ "1 4 \n"
				+ "1 4 \n");
		
		ArrayList<Setting<?>> settingsCopy = new ArrayList<>();
		settingsCopy.addAll(settings);
		Tab tunedGuitar = InstrumentFactory.guitarEbStandard();
		assertTrue("Checking load successful with tab", ZabAppSettings.load(scan, tunedGuitar, true));
		assertEquals(guitar, tunedGuitar, "Checking correct tab loaded in");
		assertEquals(settingsCopy, settings, "Checking correct settings loaded in");
		
		assertTrue("Checking load successful with no tab", ZabAppSettings.load(scan, null, true));
		assertEquals(settingsCopy, settings, "Checking correct settings loaded in");

		scan.close();
		scan = new Scanner(""
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
		assertFalse("Checking load fails with invalid tab", ZabAppSettings.load(scan, guitar, false));
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
		ZabAppSettings.init();
		
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
		assertFalse("Checking save fails with null tab and not saving settings", ZabAppSettings.save(write, null, false));
		write.close();
		
		bytes = new ByteArrayOutputStream();
		bs = new BufferedOutputStream(bytes);
		write = new PrintWriter(bs);
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
				+ "0.1 0.1 null null \n"
				+ "true true \n"
				+ "2.0 2.0 null null \n"
				+ "8.0 8.0 null null \n"
				+ "1 4 \n"
				+ "1 4 \n"
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
				+ ">\n"
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
				+ "0.1 0.1 null null \n"
				+ "true true \n"
				+ "2.0 2.0 null null \n"
				+ "8.0 8.0 null null \n"
				+ "1 4 \n"
				+ "1 4 \n", text, "Checking correct text saved with no tab");
		
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
