package appMain.gui.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import util.testUtils.Assert;

public class TestZabLayoutHandler{
	
	private JPanel panel;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		panel = new JPanel();
	}
	
	@Test
	public void createVerticalLayout(){
		ZabLayoutHandler.createVerticalLayout(panel);
		LayoutManager lay = panel.getLayout();
		Assert.isInstance(BoxLayout.class, lay, "Checking layout is correct type, should be BoxLayout, was " + lay.getClass());
		BoxLayout b = (BoxLayout)lay;
		assertEquals(BoxLayout.Y_AXIS, b.getAxis(), "Checking correct axis");
	}
	
	@Test
	public void createHorizontalLayout(){
		ZabLayoutHandler.createHorizontalLayout(panel);
		LayoutManager lay = panel.getLayout();
		Assert.isInstance(BoxLayout.class, lay, "Checking layout is correct type, should be BoxLayout, was " + lay.getClass());
		BoxLayout b = (BoxLayout)lay;
		assertEquals(BoxLayout.X_AXIS, b.getAxis(), "Checking correct axis");
	}
	
	@Test
	public void createGridlLayout(){
		ZabLayoutHandler.createGridLayout(panel, 2, 3);
		LayoutManager lay = panel.getLayout();
		Assert.isInstance(GridLayout.class, lay, "Checking layout is correct type, should be GridLayout, was " + lay.getClass());
		GridLayout g = (GridLayout)lay;
		assertEquals(2, g.getRows(), "Checking correct rows");
		assertEquals(3, g.getColumns(), "Checking correct columns");
	}
	
	@AfterEach
	public void end(){}

}
