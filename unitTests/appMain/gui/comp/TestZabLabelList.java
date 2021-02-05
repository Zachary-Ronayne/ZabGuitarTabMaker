package appMain.gui.comp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import javax.swing.border.LineBorder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestZabLabelList{

	private ZabLabelList listEmpty;
	private ZabLabelList listArr;
	private ZabLabelList list;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		listEmpty = new ZabLabelList();
		listArr = new ZabLabelList(new String[]{"a", "sd", "fgh"});
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("q");
		arr.add("we");
		list = new ZabLabelList(arr);
	}
	
	@Test
	public void getLabels(){
		assertTrue(listEmpty.getLabels().isEmpty(), "Checking list has no labels");
		
		assertEquals(3, listArr.getLabels().size(), "Checking array has correct number of elements");
		assertEquals("a", listArr.getLabels().get(0).getText(), "Checking array has correct text order");
		assertEquals("sd", listArr.getLabels().get(1).getText(), "Checking array has correct text order");
		assertEquals("fgh", listArr.getLabels().get(2).getText(), "Checking array has correct text order");
		
		assertEquals(2, list.getLabels().size(), "Checking array has correct number of elements");
		assertEquals("q", list.getLabels().get(0).getText(), "Checking array has correct text order");
		assertEquals("we", list.getLabels().get(1).getText(), "Checking array has correct text order");
	}
	
	@Test
	public void getTitle(){
		assertEquals("", list.getTitle().getText(), "Checking title initially empty");
		assertEquals(20, list.getTitle().getFont().getSize(), "Checking initial font size");
	}
	
	@Test
	public void setTitle(){
		list.setTitle("word");
		assertEquals("word", list.getTitle().getText(), "Checking title text set");
	}
	
	@Test
	public void setFontSize(){
		assertEquals(15, list.getLabels().get(0).getFont().getSize(), "Checking correct base font size");
		assertEquals(15, list.getLabels().get(1).getFont().getSize(), "Checking correct base font size");
		
		list.setFontSize(12);
		assertEquals(12, list.getLabels().get(0).getFont().getSize(), "Checking correct set font size");
		assertEquals(12, list.getLabels().get(1).getFont().getSize(), "Checking correct set font size");
	}
	
	@Test
	public void setBorderSize(){
		assertEquals(10, ((LineBorder)list.getBorder()).getThickness(), "Checking correct initial border size");
		
		list.setBorderSize(3);
		assertEquals(3, ((LineBorder)list.getBorder()).getThickness(), "Checking correct set border size");
	}
	
	@AfterEach
	public void end(){}
	
}
