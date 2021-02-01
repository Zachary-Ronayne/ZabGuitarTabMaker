package appMain.gui.comp.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class TestTabPaintController extends AbstractTestTabPainter{
	
	private Selector sel;
	
	/**
	 * A utility class for testing the abstract {@link TabPaintController}
	 * @author zrona
	 */
	private static class Selector extends TabPaintController{
		public Selector(TabPainter painter){ super(painter); }
	}
	
	@BeforeAll
	public static void init(){
		AbstractTestTabPainter.init();
	}
	
	@BeforeEach
	public void setup(){
		super.setup(true);
		sel = new Selector(paint);
	}
	
	@Test
	public void constructor(){
		assertThrows(IllegalArgumentException.class, new Executable(){
			@Override
			public void execute() throws Throwable{
				new Selector(null);
			}
		}, "Checking a null parameter to a TabPaintSelector throws an error");
	}
	
	@Test
	public void getPainter(){
		assertEquals(paint, sel.getPainter(), "Checking painter initialized");
	}
	
	@AfterEach
	public void end(){}
	
}
