package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import util.testUtils.Assert;

public class TestGuiUtils{

	private BufferedImage img;
	private Graphics2D g;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		img = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
		g = img.createGraphics();
		g.setFont(new Font("Arial", Font.PLAIN, 20));
	}
	
	@Test
	public void getAllComponents(){
		JPanel root = new JPanel(); // Component containing all others
		root.setBackground(new Color(0));
		JPanel layer1 = new JPanel(); // Checking recursion layers
		layer1.setBackground(new Color(1));
		JPanel layer2 = new JPanel();
		layer2.setBackground(new Color(2));
		JPanel layer1Item1 = new JPanel(); // Checking items after recursion
		layer1Item1.setBackground(new Color(3));
		JPanel layer1Item2 = new JPanel();
		layer1Item2.setBackground(new Color(4));
		JPanel layer2Item = new JPanel();
		layer2Item.setBackground(new Color(5));
		JPanel duplicate = new JPanel(); // Case of an item appearing more than once should only be counted once
		duplicate.setBackground(new Color(6));
		@SuppressWarnings("serial")
		Component nonContainer = new Component(){}; // Case of an item which is not a container
		nonContainer.setBackground(new Color(7));
		
		root.add(layer1);
		root.add(layer2);
		layer1.add(layer1Item1);
		layer1.add(layer1Item2);
		layer1.add(nonContainer);
		layer1.add(duplicate);
		layer2.add(layer2Item);
		layer2.add(duplicate);
		
		ArrayList<Component> comps = GuiUtils.getAllComponents(root);
		Assert.containsSize(comps, root, layer1, layer2, layer1Item1, layer1Item2, nonContainer, duplicate, layer2Item);
		
		assertEquals(0, GuiUtils.getAllComponents(null).size(), "Checking empty list returned on null");
		
		comps = GuiUtils.getAllComponents(nonContainer);
		Assert.containsSize(comps, nonContainer);
	}
	
	@Test
	public void setFontSize(){
		JLabel lab = new JLabel();
		Font f = new Font("Times New Roman", Font.BOLD, 32);
		lab.setFont(f);
		assertTrue(GuiUtils.setFontSize(lab, 3), "Checking change is successful");
		assertEquals(3, lab.getFont().getSize(), "Checking size set");
		assertEquals("Times New Roman Bold", lab.getFont().getName(), "Checking name unchanged");
		assertEquals(Font.BOLD, lab.getFont().getStyle(), "Checking style unchanged");

		assertTrue(GuiUtils.setFontSize(lab, 0), "Checking change is successful");
		assertFalse(GuiUtils.setFontSize(lab, -1), "Checking change fails");
	}
	
	@Test
	public void stringBounds(){
		assertEquals(new Rectangle(), GuiUtils.stringBounds("", null, 0, 0), "Checking empty rectangle returned with null graphics");
		assertEquals(new Rectangle(31, 5, 30, 15), GuiUtils.stringBounds("lllOl", g, 30, 20), "Checking correct bounds");
		assertEquals(new Rectangle(36, 11, 30, 15), GuiUtils.stringBounds("lllOl", g, 35, 26), "Checking correct bounds");
		assertEquals(new Rectangle(36, 11, 9, 15), GuiUtils.stringBounds("0", g, 35, 26), "Checking correct bounds");
		assertEquals(new Rectangle(34, 11, 4, 19), GuiUtils.stringBounds("j", g, 35, 26), "Checking correct bounds");
		
		assertEquals(new Size2D(30, 15), GuiUtils.stringBounds("lllOl", g), "Checking correct with no coordinates");
		assertEquals(new Size2D(9, 15), GuiUtils.stringBounds("0", g), "Checking correct with no coordinates");
		assertEquals(new Size2D(4, 19), GuiUtils.stringBounds("j", g), "Checking correct with no coordinates");
		
		g.setFont(new Font("Times New Roman", Font.BOLD, 132));
		assertEquals(new Rectangle(38, -64, 803, 118), GuiUtils.stringBounds("tfdgrehtrerhw", g, 35, 26), "Checking correct bounds");
		assertEquals(new Rectangle(131, -316, 36, 118), GuiUtils.stringBounds("j", g, 135, -226), "Checking correct bounds");
	}

	@Test
	public void stringBaselineOffset(){
		assertEquals(new Size2D(-1, 15), GuiUtils.stringBaselineOffset("lllOl", g), "Checking correct offset");
		assertEquals(new Size2D(-1, 15), GuiUtils.stringBaselineOffset("0", g), "Checking correct offset");
		assertEquals(new Size2D(1, 15), GuiUtils.stringBaselineOffset("j", g), "Checking correct offset");
		
		g.setFont(new Font("Times New Roman", Font.BOLD, 132));
		assertEquals(new Size2D(-3, 90), GuiUtils.stringBaselineOffset("tfdgrehtrerhw", g), "Checking correct offset");
		assertEquals(new Size2D(4, 90), GuiUtils.stringBaselineOffset("j", g), "Checking correct offset");
	}
	
	@AfterEach
	public void end(){}
	
}
