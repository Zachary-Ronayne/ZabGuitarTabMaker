package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestFileUtils{

	@BeforeEach
	public void setup(){}
	
	@Test
	public void makeFileName(){
		assertEquals("/", FileUtils.makeFileName(null, null, null), "Checking all null values");
		assertEquals("/a.txt", FileUtils.makeFileName(null, "a", "txt"), "Checking null path");
		assertEquals("yes/.txt", FileUtils.makeFileName("yes", null, "txt"), "Checking null name");
		assertEquals("yes/wee", FileUtils.makeFileName("yes", "wee", null), "Checking null extension");
		assertEquals("yes/wee.png", FileUtils.makeFileName("yes", "wee", "png"), "Checking nothing null");
		assertEquals("yes/wee.png" , FileUtils.makeFileName("yes/", "wee", "png"), "Checking with a slash in the path");
	}
	
	@AfterEach
	public void end(){}
	
}
