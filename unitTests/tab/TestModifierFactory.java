package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;
import tab.symbol.TabModifier;

public class TestModifierFactory{

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){}
	
	@Test
	public void hammerOn(){
		assertEquals(new TabModifier("h", ""), ModifierFactory.hammerOn(), "Checking correct modifier");
	}
	
	@Test
	public void pullOff(){
		assertEquals(new TabModifier("", "p"), ModifierFactory.pullOff(), "Checking correct modifier");
	}
	
	@Test
	public void hammerOnPullOff(){
		assertEquals(new TabModifier("h", "p"), ModifierFactory.hammerOnPullOff(), "Checking correct modifier");
	}
	
	@Test
	public void slideUp(){
		assertEquals(new TabModifier("/", ""), ModifierFactory.slideUp(), "Checking correct modifier");
	}
	
	@Test
	public void slideDown(){
		assertEquals(new TabModifier("\\", ""), ModifierFactory.slideDown(), "Checking correct modifier");
	}
	
	@Test
	public void harmonic(){
		assertEquals(new TabModifier("<", ">"), ModifierFactory.harmonic(), "Checking correct modifier");
	}
	
	@Test
	public void bend(){
		assertEquals(new TabModifier("", "b"), ModifierFactory.bend(), "Checking correct modifier");
	}
	
	@Test
	public void ghostNote(){
		assertEquals(new TabModifier("(", ")"), ModifierFactory.ghostNote(), "Checking correct modifier");
	}
	
	@Test
	public void pinchHarmonic(){
		assertEquals(new TabModifier("", "ph"), ModifierFactory.pinchHarmonic(), "Checking correct modifier");
	}
	
	@Test
	public void vibrato(){
		assertEquals(new TabModifier("", "~"), ModifierFactory.vibrato(), "Checking correct modifier");
	}
	
	@Test
	public void tab(){
		assertEquals(new TabModifier("", "t"), ModifierFactory.tap(), "Checking correct modifier");
	}
	
	@AfterEach
	public void end(){}
	
}
