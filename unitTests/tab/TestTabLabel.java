package tab;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.NotePosition;
import music.TimeSignature;
import util.testUtils.UtilsTest;

public class TestTabLabel{
	
	private TabLabel fullLabel;
	private TabLabel label;
	
	private NotePosition pos;
	
	private TimeSignature sig;
	
	@BeforeEach
	public void setup(){
		pos = new NotePosition(1);
		
		fullLabel = new TabLabel("a label", pos, 5, -2);
		label = new TabLabel("label b", pos);
		
		sig = new TimeSignature(5, 8);
	}
	
	@Test
	public void getText(){
		assertEquals("a label", fullLabel.getText(), "Checking text initialized");
		assertEquals("label b", label.getText(), "Checking text initialized");
	}
	
	@Test
	public void setText(){
		label.setText("c label");
		assertEquals("c label", label.getText(), "Checking text set");
	}
	
	@Test
	public void getPosition(){
		assertEquals(pos, fullLabel.getPosition(), "Checking symbol initialized");
		assertEquals(pos, label.getPosition(), "Checking symbol initialized");
	}
	
	@Test
	public void setPosition(){
		label.setPosition(new NotePosition(2));
		assertEquals(2, label.getPosition().getValue(), "Checking symbol set");
	}
	
	@Test
	public void getLength(){
		assertEquals(5, fullLabel.getLength(), "Checking length initialized");
		assertEquals(0, label.getLength(), "Checking length initialized");
	}
	
	@Test
	public void setLength(){
		label.setLength(2);
		assertEquals(2, label.getLength(), "Checking length set");
	}
	
	@Test
	public void getOffset(){
		assertEquals(-2, fullLabel.getOffset(), "Checking offset initialized");
		assertEquals(0, label.getOffset(), "Checking offset initialized");
	}
	
	@Test
	public void setOffset(){
		label.setOffset(3);
		assertEquals(3, label.getOffset(), "Checking offset set");
	}
	
	@Test
	public void getMeasures(){
		assertEquals(8, fullLabel.getMeasures(sig), "Checking correct number of measures");
		assertEquals(0, label.getMeasures(sig), "Checking correct number of measures");
	}
	
	@Test
	public void setMeasures(){
		label.setMeasures(4, sig);
		assertEquals(4, label.getMeasures(sig), "Checking correct number of measures set");
		assertEquals(2.5, label.getLength(), "Checking correct number of whole notes set");
	}
	
	@Test
	public void getBeginningPos(){
		assertEquals(1, label.getBeginningPos(), "Checking beginning position with offset zero");
		assertEquals(-1, fullLabel.getBeginningPos(), "Checking beginning position with non zero offset");
		
		pos.setValue(4);
		assertEquals(2, fullLabel.getBeginningPos(), "Checking beginning position after changing note position");
	}
	
	@Test
	public void getEndingPos(){
		assertEquals(1, label.getEndingPos(), "Checking end position with offset and length zero");
		assertEquals(4, fullLabel.getEndingPos(), "Checking end position with nonzero offset and length");
		
		pos.setValue(3);
		assertEquals(6, fullLabel.getEndingPos(), "Checking end position after changing note position");
		
		fullLabel.setLength(3);
		assertEquals(4, fullLabel.getEndingPos(), "Checking end position after changing length");
	}
	
	@Test
	public void modifyLength(){
		fullLabel.modifyLength(2);
		assertEquals(7, fullLabel.getLength(), "Checking total length correct expanding positive number");
		assertEquals(-1, fullLabel.getBeginningPos(), "Checking beginning position unchanged");
		assertEquals(6, fullLabel.getEndingPos(), "Checking end position updated");
		
		fullLabel.modifyLength(-4);
		assertEquals(11, fullLabel.getLength(), "Checking total length correct expanding negative number");
		assertEquals(-5, fullLabel.getBeginningPos(), "Checking beginning position updated");
		assertEquals(6, fullLabel.getEndingPos(), "Checking end position unchanged");
	}
	
	@Test
	public void expandBackwards(){
		fullLabel.expandBackwards(5);
		assertEquals(10, fullLabel.getLength(), "Checking total length correct expanding backwards");
		assertEquals(-6, fullLabel.getBeginningPos(), "Checking beginning position updated");
		assertEquals(4, fullLabel.getEndingPos(), "Checking end position unchanged");
		
		fullLabel.expandBackwards(-5);
		assertEquals(10, fullLabel.getLength(), "Checking total length unchanged with negative parameter");
		assertEquals(-6, fullLabel.getBeginningPos(), "Checking beginning position unchanged");
		assertEquals(4, fullLabel.getEndingPos(), "Checking end position unchanged");
	}
	
	@Test
	public void expandForwards(){
		fullLabel.expandForwards(5);
		assertEquals(10, fullLabel.getLength(), "Checking total length correct expanding forward");
		assertEquals(-1, fullLabel.getBeginningPos(), "Checking beginning position unchanged");
		assertEquals(9, fullLabel.getEndingPos(), "Checking end position updated");

		fullLabel.expandBackwards(-5);
		assertEquals(10, fullLabel.getLength(), "Checking total length unchanged with negative parameter");
		assertEquals(-1, fullLabel.getBeginningPos(), "Checking beginning position unchanged");
		assertEquals(9, fullLabel.getEndingPos(), "Checking end position unchanged");
	}
	
	@Test
	public void shift(){
		fullLabel.shift(4);
		assertEquals(3, fullLabel.getBeginningPos(), "Checking label shifted forwards beginning updated");
		assertEquals(8, fullLabel.getEndingPos(), "Checking label shifted forwards ending updated");
		assertEquals(5, fullLabel.getLength(), "Checking length unchanged on shift");
		
		fullLabel.shift(-2);
		assertEquals(1, fullLabel.getBeginningPos(), "Checking label shifted backwards beginning updated");
		assertEquals(6, fullLabel.getEndingPos(), "Checking label shifted backwards ending updated");
		assertEquals(5, fullLabel.getLength(), "Checking length unchanged on shift");
	}
	
	@Test
	public void load(){
		Scanner scan = new Scanner("z label\n3.2\n-2.1\n2.3 \nh");
		assertTrue("Checking load successful", label.load(scan));
		assertEquals("z label", label.getText(), "Checking text loaded");
		assertEquals(3.2, label.getLength(), "Checking length loaded");
		assertEquals(-2.1, label.getOffset(), "Checking offset loaded");
		assertEquals(2.3, label.getPosition().getValue(), "Checking position loaded");
		assertFalse("Checking load fails without enough data", label.load(scan));
		
		scan.close();
		scan = new Scanner("a\na\na\na\na");
		assertFalse("Checking load fails with invalid numbers", label.load(scan));
		scan.close();
	}
	
	@Test
	public void save(){
		assertEquals("a label\n5.0\n-2.0\n1.0 \n", UtilsTest.testSave(fullLabel), "Checking save successful");
		assertEquals("label b\n0.0\n0.0\n1.0 \n", UtilsTest.testSave(label), "Checking save successful");
	}
	
	@AfterEach
	public void end(){}
	
}
