package util.testUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

/**
 * A class holding a simple template for a test case, not for use in actual code.
 * @author zrona
 */
public class EmptyTest{

	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){}
	
	@Test
	public void test(){}
	
	@AfterEach
	public void end(){}
	
}
