package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import util.testUtils.UtilsTest;

public class TestFileUtils{

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){}
	
	@Test
	public void makeFileName(){
		assertEquals("/", FileUtils.makeFileName(null, null, null), "Checking all null values");
		assertEquals("/a.txt", FileUtils.makeFileName(null, "a", "txt"), "Checking null path");
		assertEquals("yes/.txt", FileUtils.makeFileName("yes", null, "txt"), "Checking null name");
		assertEquals("yes/wee", FileUtils.makeFileName("yes", "wee", null), "Checking null extension");
		assertEquals("yes/wee", FileUtils.makeFileName("yes", "wee"), "Checking no extension");
		assertEquals("yes/wee.png", FileUtils.makeFileName("yes", "wee", "png"), "Checking nothing null");
		assertEquals("yes/wee.png" , FileUtils.makeFileName("yes/", "wee", "png"), "Checking with a slash in the path");
	}
	
	@Test
	public void extendTo(){
		File file;
		String path = UtilsTest.UNIT_PATH + "\\extendTest";
		
		file = FileUtils.extendTo(new File(path), "txt");
		assertEquals(path + ".txt", file.getPath(), "Checking path correct");
		
		file = FileUtils.extendTo(new File(path), ".png");
		assertEquals(path + ".png", file.getPath(), "Checking path correct");
		
		file = FileUtils.extendTo(new File(path + ".fakeFile"), "fakeFile");
		assertEquals(path + ".fakeFile", file.getPath(), "Checking path correct");
		
		assertEquals(null, FileUtils.extendTo(null, "txt"), "Checking null returned on null file");
		assertEquals(null, FileUtils.extendTo(file, null), "Checking null returned on null extension");
	}
	
	@Test
	public void extendToZab(){
		String path = UtilsTest.UNIT_PATH + "\\extendTest";
		File file = new File(path);
		file = FileUtils.extendToZab(file);
		assertEquals(path + ".zab", file.getPath(), "Checking path correct");
		
		assertEquals(null, FileUtils.extendToZab(null), "Checking null returned on null file");
	}
	
	@AfterEach
	public void end(){
		UtilsTest.deleteUnitFolder();
	}
	
}
