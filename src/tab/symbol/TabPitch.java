package tab.symbol;

import music.Pitch;
import tab.TabString;
import util.ObjectUtils;

/**
 * An object representing a symbol in a tab which contains pitch information
 * @author zrona
 */
public abstract class TabPitch extends TabSymbol{

	/** The {@link Pitch} of this {@link TabPitch}, i.e. the musical note. Cannot be null. */
	private Pitch pitch;
	
	/**
	 * Create a new {@link TabPitch} using the given values
	 * @param pitch Initial value for {@link #pitch}
	 * @param modifier Initial value for {@link TabSymbol#modifier}
	 */
	public TabPitch(Pitch pitch, TabModifier modifier){
		super(modifier);
		if(pitch == null) throw new IllegalArgumentException("Pitch cannot be null");
		this.pitch = pitch;
	}

	/**
	 * Create a new {@link TabPitch} using the given values and no modifier
	 * @param pitch Initial value for {@link #pitch}
	 */
	public TabPitch(Pitch pitch){
		this(pitch, new TabModifier());
	}
	
	/**
	 * Get the pitch of this {@link TabPitch}
	 * @return See {@link #pitch}
	 */
	public Pitch getPitch(){
		return pitch;
	}
	
	/**
	 * Get the text representing this pitch as a note, i.e. E4, C#2, etc.
	 * @param useFlats true to represent all applicable notes as flat, i.e. Db4, false to represent them as sharps, i.e. C#4
	 * @return The text
	 */
	public String getPitchName(boolean useFlats){
		return this.getPitch().getPitchName(useFlats);
	}
	
	/***/
	public String getSymbol(TabString string){
		return String.valueOf(string.getTabNumber(this.getPitch()));
	}
	
	/**
	 * Sets the pitch of this {@link TabPitch} so that it will have the same tab number when placed on the new string.
	 * Will cause a {@link ClassCastException} if the {@link #copy()} method of an extension of this class does not return a {@link TabPitch}
	 */
	@Override
	public TabPitch movingToNewString(TabString oldStr, TabString newStr){
		if(oldStr == null || newStr == null) return null;
		int oldNum = oldStr.getTabNumber(this.getPitch());
		TabPitch newNote = (TabPitch)this.copy();
		newNote.pitch = newStr.createPitch(oldNum);
		return newNote;
	}
	
	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, TabPitch.class)) return false;
		TabPitch p = (TabPitch)obj;
		return	super.equals(obj) &&
				this.getPitch().equals(p.getPitch());
	}
	
	/***/
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder();
		b.append(super.toString());
		b.append(", ");
		b.append(this.getPitch());
		return b.toString();
	}
	
}
