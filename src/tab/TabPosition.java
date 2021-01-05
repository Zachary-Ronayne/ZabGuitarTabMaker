package tab;

import java.io.PrintWriter;
import java.util.Scanner;

import music.NotePosition;
import music.TimeSignature;
import tab.symbol.TabSymbol;
import util.Copyable;
import util.ObjectUtils;
import util.Saveable;


/**
 * A class used to hold {@link TabSymbol} objects in a {@link TabString}, along with a position
 * @author zrona
 */
public class TabPosition implements Comparable<TabPosition>, Copyable<TabPosition>, Saveable{
	
	/** The {@link TabSymbol} held by this holder. Cannot be null */ 
	private TabSymbol symbol;
	
	/** The {@link NotePosition} tracking the position of this {@link TabPosition} objects {@link #symbol}. Cannot be null*/
	private NotePosition position;
	
	/**
	 * Create a {@link SymbolHolder} with the given {@link TabSymbol}
	 * @param symbol See {@link #symbol}
	 * @param position See {@link #position}
	 */
	public TabPosition(TabSymbol symbol, NotePosition position){
		if(symbol == null) throw new IllegalArgumentException("Pos cannot be null");
		if(position == null) throw new IllegalArgumentException("Pos cannot be null");
		
		this.setSymbol(symbol);
		this.setPosition(position);
	}
	
	/**
	 * Create a {@link SymbolHolder} with the given {@link TabSymbol}
	 * @param symbol see {@link #symbol}
	 * @param position The numerical value for {@link #position}
	 */
	public TabPosition(TabSymbol symbol, double position){
		this(symbol, new NotePosition(position));
	}
	
	@Override
	public TabPosition copy(){
		return new TabPosition(symbol.copy(), getPosition().copy());
	}
	
	/**
	 * @return See {@link #symbol}
	 */
	public TabSymbol getSymbol(){
		return this.symbol;
	}
	
	/**
	 * @param symbol See {@link #symbol}
	 */
	public void setSymbol(TabSymbol symbol){
		if(symbol == null) return;
		this.symbol = symbol;
	}
	
	/**
	 * Get the {@link #position} of this {@link TabSymbol}
	 * @return See {@link #position}
	 */
	public NotePosition getPosition(){
		return this.position;
	}
	
	/**
	 * Get the value of the {@link #position} of this {@link TabSymbol}
	 * @return The position
	 */
	public double getPos(){
		return this.getPosition().getValue();
	}
	
	/**
	 * Set the {@link #position} of this {@link TabSymbol}
	 * @param pos See {@link #position}
	 */
	public void setPosition(NotePosition position){
		if(position == null) return;
		this.position = position;
	}

	/**
	 * Set the value for the {@link #position} of this {@link TabSymbol} creating a new {@link NotePosition} object
	 * @param pos See {@link #position}
	 */
	public void setPos(double pos){
		this.setPosition(new NotePosition(pos));
	}
	
	/**
	 * Quantize this {@link TabSymbol} position to the nearest place in a measure
	 * @param sig The time signature to base the quantization off of
	 * @param divisor The amount to divide up the units of a whole note.<br>
	 * 	i.e. use 4 to quantize to quarter notes, use 6 to quantize to dotted quarter notes, etc
	 */
	public void quantize(TimeSignature sig, int divisor){
		this.getPosition().quantize(sig, divisor);
	}
	
	/**
	 * Convert this {@link TabSymbol} objects position so that it is the same number of whole notes, but in the new time signature
	 * @param newTime The {@link TimeSignature} to convert to
	 * @param oldTime The {@link TimeSignature} which this position was in
	 */
	public void retime(TimeSignature newTime, TimeSignature oldTime){
		this.getPosition().retime(newTime, oldTime);
	}
	
	/**
	 * Convert this {@link NoteSymbol} object position so that it stays in the same measure and same relative position in the measure.<br>
	 * @param newTime The {@link TimeSignature} to convert to
	 * @param oldTime The {@link TimeSignature} which the position was in
	 * @return true if the position is in the same measure, false otherwise
	 */
	public boolean retimeMeasure(TimeSignature newTime, TimeSignature oldTime){
		return this.getPosition().retimeMeasure(newTime, oldTime);
	}
	
	/***/
	@Override
	public int compareTo(TabPosition p){
		return this.getPosition().compareTo(p.getPosition());
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		TabPosition t = ((TabPosition)obj);
		return	t.getSymbol().equals(this.getSymbol()) && 
				t.getPosition().equals(this.getPosition());
	}
	
	/**
	 * Get the objects used to save this {@link TabPosition}
	 * @return The objects, meaning the symbol and then position
	 */
	public Saveable[] getSaveObjects(){
		return new Saveable[]{this.getSymbol(), this.getPosition()};
	}
	
	/***/
	@Override
	public boolean load(Scanner reader){
		// Load in the name of the symbol
		String[] loadStr = Saveable.loadStrings(reader, 1);
		if(loadStr == null) return false;
		String type = loadStr[0];
		
		TabSymbol s = TabUtils.stringToSymbol(type);
		if(s == null) return false;
		this.setSymbol(s);
		
		// Load the symbol and position
		return Saveable.loadMultiple(reader, this.getSaveObjects());
	}

	/***/
	@Override
	public boolean save(PrintWriter writer){
		// Save the type of note
		if(!Saveable.saveToString(writer, this.getSymbol().getClass().getSimpleName(), true)) return false;

		// Load the symbol and position
		return Saveable.saveMultiple(writer, getSaveObjects());
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder("[TabPosition, ");
		b.append(this.getSymbol());
		b.append(", ");
		b.append(this.getPosition());
		b.append("]");
		return b.toString();
	}

}
