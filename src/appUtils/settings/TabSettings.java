package appUtils.settings;

import music.Rhythm;
import settings.SettingDouble;
import settings.SettingRhythm;
import settings.Settings;

/**
 * A {@link Settings} object which contains all of the information about tabs 
 * 	in general without direct respect to any input or output interface
 * @author zrona
 */
public class TabSettings extends Settings{

	/** Default for {@link #quantizeDivisor} */
	public static final double QUANTIZE_DIVISOR = 16.0;
	/** @return Default for {@link #rhythmConversionEndValue} */
	public static Rhythm RHYTHM_CONVERSION_END_VALUE = new Rhythm(1, 4);

	/**
	 * The divisor used for quantizing notes for creation, placement, and selection, based on note duration. 
	 * If this value is 8, it uses eighth notes, if it is 4, it uses quarter notes, if it is 1.23 it uses notes 1/1.23 the length of a whole note
	 */
	private SettingDouble quantizeDivisor;
	/**
	 * The value for the {@link Rhythm} used for the last note when a rhythmicless tab is converted to use rhythm, and the last note cannot be guessed
	 * because it has no note after it
	 */
	private SettingRhythm rhythmConversionEndValue;

	/**
	 * Create a new set of {@link TabSettings} with all default values loaded
	 */
	public TabSettings(){
		super();
		this.quantizeDivisor = this.addDouble(QUANTIZE_DIVISOR);
		this.rhythmConversionEndValue = this.add(new SettingRhythm(RHYTHM_CONVERSION_END_VALUE));
	}

	/** @return See {@link #quantizeDivisor} */
	public SettingDouble getQuantizeDivisor(){ return this.quantizeDivisor; }
	/** @return See {@link #rhythmConversionEndValue} */
	public SettingRhythm getRhythmConversionEndValue(){ return this.rhythmConversionEndValue; }

	/** @return See {@link #quantizeDivisor} */
	public Double quantizeDivisor(){ return this.getQuantizeDivisor().get(); }
	/** @return See {@link #rhythmConversionEndValue} */
	public Rhythm rhythmConversionEndValue(){ return this.getRhythmConversionEndValue().get(); }
}
