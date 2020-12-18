package tab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import music.NotePosition;
import music.TimeSignature;
import tab.symbol.TabDeadNote;
import tab.symbol.TabNote;

public class TestTabLabel{
	
	private TabLabel fullLabel;
	private TabLabel label;
	
	private TabDeadNote symbol;
	
	private TimeSignature sig;
	
	@BeforeEach
	public void setup(){
		symbol = new TabDeadNote(new NotePosition(1));
		
		fullLabel = new TabLabel("a label", symbol, 5, -2);
		label = new TabLabel("label b", symbol);
		
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
	public void getBaseSymbol(){
		assertEquals(symbol, fullLabel.getBaseSymbol(), "Checking symbol initialized");
		assertEquals(symbol, label.getBaseSymbol(), "Checking symbol initialized");
	}
	
	@Test
	public void setBaseSymbol(){
		label.setBaseSymbol(new TabNote(1, 2));
		assertEquals(new TabNote(1, 2), label.getBaseSymbol(), "Checking symbol set");
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
		
		symbol.setPos(4);
		assertEquals(2, fullLabel.getBeginningPos(), "Checking beginning position after changing note position");
	}
	
	@Test
	public void getEndingPos(){
		assertEquals(1, label.getEndingPos(), "Checking end position with offset and length zero");
		assertEquals(4, fullLabel.getEndingPos(), "Checking end position with nonzero offset and length");
		
		symbol.setPos(3);
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
	
	@AfterEach
	public void end(){}
	
}
