package tab;

import java.util.ArrayList;

import music.Music;

public final class InstrumentFactory{
	
	/**
	 * Create an empty tab of a 6 string guitar in standard tuning, 
	 * i.e. 6 strings tuned low to high as EADGBe
	 * @return The tab
	 */
	public static Tab guitarStandard(){
		Tab guitar = new Tab();
		ArrayList<TabString> strings = guitar.getStrings();
		strings.add(new TabString(Music.E, 4));
		strings.add(new TabString(Music.B, 3));
		strings.add(new TabString(Music.G, 3));
		strings.add(new TabString(Music.D, 3));
		strings.add(new TabString(Music.A, 2));
		strings.add(new TabString(Music.E, 2));
		return guitar;
	}

	/**
	 * Create an empty tab of a 6 string guitar in standard tuning, but offset by a number of half steps,  
	 * 	i.e. -1 for Eb standard, -2 for D standard, etc.<br>
	 * Calling {@code guitarTuned(-1)} is the same as calling {@code guitarEbStandard()}
	 * @param change The amount to tune up or down in pitch
	 * @return The tab
	 */
	public static Tab guitarTuned(int change){
		Tab guitar = guitarStandard();
		ArrayList<TabString> strings = guitar.getStrings();
		for(TabString t : strings){
			t.setRootPitch(t.getRootPitch().tune(change));
		}
		return guitar;
	}
	
	/**
	 * Create an empty tab of a 6 string guitar in Eb standard tuning, 
	 * i.e. 6 strings tuned low to high as Eb Ab Db Gb Bb eb
	 * @return The tab
	 */
	public static Tab guitarEbStandard(){
		return guitarTuned(-1);
	}

	/**
	 * Create an empty tab of a 6 string guitar in D standard tuning, 
	 * i.e. 6 strings tuned low to high as DGCFAd
	 * @return The tab
	 */
	public static Tab guitarDStandard(){
		return guitarTuned(-2);
	}

	/**
	 * Create an empty tab of a 6 string guitar in drop D tuning, 
	 * i.e. 6 strings tuned low to high as DADGBe
	 * @return The tab
	 */
	public static Tab guitarDropD(){
		Tab guitar = guitarStandard();
		TabString str = guitar.getStrings().get(5);
		str.setRootPitch(str.getRootPitch().tune(-2));
		return guitar;
	}
	
	/**
	 * Create an empty tab of a 4 string bass guitar in standard tuning, 
	 * i.e. 4 strings tuned low to high as EADG
	 * @return The tab
	 */
	public static Tab bassStandard(){
		Tab bass = new Tab();
		ArrayList<TabString> strings = bass.getStrings();
		strings.add(new TabString(Music.G, 2));
		strings.add(new TabString(Music.D, 2));
		strings.add(new TabString(Music.A, 1));
		strings.add(new TabString(Music.E, 1));
		return bass;
	}
	
	/**
	 * Create an empty tab of a 4 string ukulele in standard tuning, 
	 * i.e. 4 strings tuned low to high as gCEA, 
	 * Note the high G
	 * @return The tab
	 */
	public static Tab ukuleleStandard(){
		Tab guitar = new Tab();
		ArrayList<TabString> strings = guitar.getStrings();
		strings.add(new TabString(Music.A, 4));
		strings.add(new TabString(Music.E, 4));
		strings.add(new TabString(Music.C, 4));
		strings.add(new TabString(Music.G, 4));
		return guitar;
	}
	
	/** Cannot instantiate {@link InstrumentFactory} */
	private InstrumentFactory(){}
	
}
