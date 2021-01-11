package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestGuiUtils{

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){}
	
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
		assertEquals(8, comps.size(), "Checking correct number of components obtained");
		assertTrue(comps.contains(root), "Checking component in list");
		assertTrue(comps.contains(layer1), "Checking component in list");
		assertTrue(comps.contains(layer2), "Checking component in list");
		assertTrue(comps.contains(layer1Item1), "Checking component in list");
		assertTrue(comps.contains(layer1Item2), "Checking component in list");
		assertTrue(comps.contains(duplicate), "Checking component in list");
		assertTrue(comps.contains(nonContainer), "Checking component in list");
		assertTrue(comps.contains(layer2Item), "Checking component in list");
		
		assertEquals(0, GuiUtils.getAllComponents(null).size(), "Checking empty list returned on null");
		
		comps = GuiUtils.getAllComponents(nonContainer);
		assertEquals(1, comps.size(), "Checking one component in list");
		assertTrue(comps.contains(nonContainer), "Checking component in list");
	}
	
	@AfterEach
	public void end(){}
	
}