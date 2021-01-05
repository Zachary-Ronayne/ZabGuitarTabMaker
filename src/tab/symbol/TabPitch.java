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
		this.setPitch(pitch);
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
	 * Set the pitch of this {@link TabPitch}
	 * @param pitch See {@link #pitch}
	 */
	public void setPitch(Pitch pitch){
		if(pitch == null) return;
		this.pitch = pitch;
	}

	/**
	 * Set the pitch of this {@link TabPitch} based on an integer
	 * @param pitch The integer for see {@link #pitch}
	 */
	public void setPitch(int pitch){
		this.setPitch(new Pitch(pitch));
	}
	
	/***/
	public String getSymbol(TabString string){
		return String.valueOf(string.getTabNumber(this.getPitch()));
	}

	/***/
	@Override
	public boolean equals(Object obj){
		if(!ObjectUtils.isType(obj, this.getClass())) return false;
		TabPitch p = (TabPitch)obj;
		return	super.equals(obj) &&
				this.getPitch().equals(p.getPitch());
	}
	
}
