package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import appUtils.ZabAppSettings;

public class TestTabTextExporter{

	private Tab guitar;
	
	@BeforeAll
	public static void init(){
		ZabAppSettings.init();
	}
	
	@BeforeEach
	public void setup(){
		guitar = InstrumentFactory.guitarTuned(1);
		TabString highE = guitar.getStrings().get(0);
		TabString b = guitar.getStrings().get(1);
		TabString g = guitar.getStrings().get(2);
		TabString d = guitar.getStrings().get(3);
		TabString a = guitar.getStrings().get(4);
		TabString lowE = guitar.getStrings().get(5);
		highE.add(TabFactory.modifiedFret(highE, 0, 0));
		highE.add(TabFactory.modifiedFret(highE, 3, 1));
		highE.add(TabFactory.modifiedFret(highE, 5, 2));
		highE.add(TabFactory.modifiedFret(highE, 0, 2.5));
		highE.add(TabFactory.modifiedFret(highE, 3, 3));
		highE.add(TabFactory.modifiedFret(highE, 6, 3.1));
		highE.add(TabFactory.modifiedFret(highE, 5, 3.2));

		b.add(TabFactory.modifiedFret(b, 0, 3.2));
		
		highE.add(TabFactory.modifiedFret(highE, 0, 4));
		b.add(TabFactory.modifiedFret(b, 0, 4));
		g.add(TabFactory.modifiedFret(g, 0, 4));
		d.add(TabFactory.modifiedFret(d, 2, 4));
		a.add(TabFactory.modifiedFret(a, 2, 4));
		lowE.add(TabFactory.modifiedFret(lowE, 0, 4));

		a.add(TabFactory.modifiedFret(a, 2, 5));
		lowE.add(TabFactory.modifiedFret(lowE, 0, 5));
		
		a.add(TabFactory.modifiedFret(a, 14, 6));
		lowE.add(TabFactory.modifiedFret(lowE, 12, 6));
		
		a.add(TabFactory.pullOff(a, 8, 7));
		lowE.add(TabFactory.pullOff(lowE, 10, 7));
	}
	
	@Test
	public void export(){
		assertEquals(null, TabTextExporter.export(null), "Checking null returned on null tab");
		
		assertEquals(""
				+ "F |-0--3--5--0--3--6--5--0-------------|\n"
				+ "C |-------------------0--0-------------|\n"
				+ "G#|----------------------0-------------|\n"
				+ "D#|----------------------2-------------|\n"
				+ "A#|----------------------2--2--14--p8--|\n"
				+ "F |----------------------0--0--12--p10-|\n",
				TabTextExporter.export(guitar),
				"Checking exporting guitar with default settings");
	}
	
	@AfterEach
	public void end(){}
	
}
