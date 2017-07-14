package com.bryndsey.songbuilder;

import com.bryndsey.songbuilder.songstructure.MusicStructure;
import com.bryndsey.songbuilder.songstructure.MusicStructure.MidiInstrument;
import com.bryndsey.songbuilder.songstructure.MusicStructure.Pitch;
import com.bryndsey.songbuilder.songstructure.MusicStructure.ScaleType;
import com.bryndsey.songbuilder.songstructure.Song;

import java.util.Random;

public class SongWriter {

	/*
	 * TODO: Should some of these go into the MusicStructure class?
	 */
	public static MidiInstrument[] chordInstruments = {
		MidiInstrument.ACCORDION,
		MidiInstrument.ACOUSTIC_GRAND_PIANO,
		MidiInstrument.ACOUSTIC_GUITAR_NYLON,
		MidiInstrument.ACOUSTIC_GUITAR_STEEL,
		MidiInstrument.BAGPIPE,
		MidiInstrument.BANJO,
		MidiInstrument.BARITONE_SAX,
		MidiInstrument.BRASS_SECTION,
		MidiInstrument.BRIGHT_ACOUSTIC_PIANO,
		MidiInstrument.CELLO,
		MidiInstrument.CHOIR_AAHS,
		MidiInstrument.CHURCH_ORGAN,
		MidiInstrument.DISTORTION_GUITAR,
		MidiInstrument.DRAWBAR_ORGAN,
		MidiInstrument.DULCIMER,
		MidiInstrument.ELECTRIC_GRAND_PIANO,
		MidiInstrument.ELECTRIC_GUITAR_CLEAN,
		MidiInstrument.ELECTRIC_GUITAR_JAZZ,
		MidiInstrument.ELECTRIC_GUITAR_MUTED,
		MidiInstrument.ELECTRIC_PIANO_1,
		MidiInstrument.ELECTRIC_PIANO_2,
		MidiInstrument.HARPSICHORD,
		MidiInstrument.HONKYTONK_PIANO,
		MidiInstrument.LEAD_1_SQUARE,
		MidiInstrument.LEAD_2_SAWTOOTH,
		MidiInstrument.LEAD_3_CALLIOPE,
		MidiInstrument.LEAD_4_CHIFF,
		MidiInstrument.LEAD_5_CHARANG,
		MidiInstrument.LEAD_6_VOICE,
		MidiInstrument.LEAD_7_FIFTHS,
		MidiInstrument.LEAD_8_BASS_AND_LEAD,
		MidiInstrument.OBOE,
		MidiInstrument.ORCHESTRA_HIT,
		MidiInstrument.OVERDRIVEN_GUITAR,
		MidiInstrument.PAD_1_NEW_AGE,
		MidiInstrument.PAD_2_WARM,
		MidiInstrument.PAD_3_POLYSYNTH,
		MidiInstrument.PAD_4_CHOIR,
		MidiInstrument.PAD_5_BOWED,
		MidiInstrument.PAD_6_METALLIC,
		MidiInstrument.PAD_7_HALO,
		MidiInstrument.PAD_8_SWEEP,
		MidiInstrument.PERCUSSIVE_ORGAN,
		MidiInstrument.PIZZICATO_STRINGS,
		MidiInstrument.REED_ORGAN,
		MidiInstrument.ROCK_ORGAN,
		MidiInstrument.STRING_ENSEMBLE_1,
		MidiInstrument.STRING_ENSEMBLE_2,
		MidiInstrument.SYNTH_BRASS_1,
		MidiInstrument.SYNTH_BRASS_2,
		MidiInstrument.SYNTH_CHOIR,
		MidiInstrument.SYNTH_STRINGS_1,
		MidiInstrument.SYNTH_STRINGS_2,
		MidiInstrument.TANGO_ACCORDION,
		MidiInstrument.TIMPANI,
		MidiInstrument.TREMOLO_STRINGS,
		MidiInstrument.TROMBONE,
		MidiInstrument.VOICE_OOHS,
		MidiInstrument.XYLOPHONE
	};

	public static MidiInstrument[] melodyInstruments = {
		MidiInstrument.ACCORDION,
		MidiInstrument.ACOUSTIC_GRAND_PIANO,
		MidiInstrument.ACOUSTIC_GUITAR_NYLON,
		MidiInstrument.ACOUSTIC_GUITAR_STEEL,
		MidiInstrument.ALTO_SAX,
		MidiInstrument.BAGPIPE,
		MidiInstrument.BANJO,
		MidiInstrument.BRASS_SECTION,
		MidiInstrument.BRIGHT_ACOUSTIC_PIANO,
		MidiInstrument.CELLO,
		MidiInstrument.CHOIR_AAHS,
		MidiInstrument.CHURCH_ORGAN,
		MidiInstrument.CLARINET,
		MidiInstrument.DISTORTION_GUITAR,
		MidiInstrument.DRAWBAR_ORGAN,
		MidiInstrument.DULCIMER,
		MidiInstrument.ELECTRIC_GRAND_PIANO,
		MidiInstrument.ELECTRIC_GUITAR_CLEAN,
		MidiInstrument.ELECTRIC_GUITAR_JAZZ,
		MidiInstrument.FIDDLE,
		MidiInstrument.FLUTE,
		MidiInstrument.HARMONICA,
		MidiInstrument.HARPSICHORD,
		MidiInstrument.HONKYTONK_PIANO,
		MidiInstrument.LEAD_1_SQUARE,
		MidiInstrument.LEAD_2_SAWTOOTH,
		MidiInstrument.LEAD_3_CALLIOPE,
		MidiInstrument.LEAD_4_CHIFF,
		MidiInstrument.LEAD_5_CHARANG,
		MidiInstrument.LEAD_6_VOICE,
		MidiInstrument.LEAD_7_FIFTHS,
		MidiInstrument.LEAD_8_BASS_AND_LEAD,
		MidiInstrument.MUSIC_BOX,
		MidiInstrument.MUTED_TRUMPET,
		MidiInstrument.OCARINA,
		MidiInstrument.OVERDRIVEN_GUITAR,
		MidiInstrument.PAD_1_NEW_AGE,
		MidiInstrument.PAD_2_WARM,
		MidiInstrument.PAD_3_POLYSYNTH,
		MidiInstrument.PAD_4_CHOIR,
		MidiInstrument.PAD_5_BOWED,
		MidiInstrument.PAD_6_METALLIC,
		MidiInstrument.PAD_7_HALO,
		MidiInstrument.PAD_8_SWEEP,
		MidiInstrument.PERCUSSIVE_ORGAN,
		MidiInstrument.REED_ORGAN,
		MidiInstrument.ROCK_ORGAN,
		MidiInstrument.SITAR,
		MidiInstrument.STRING_ENSEMBLE_1,
		MidiInstrument.STRING_ENSEMBLE_2,
		MidiInstrument.SYNTH_BRASS_1,
		MidiInstrument.SYNTH_BRASS_2,
		MidiInstrument.SYNTH_CHOIR,
		MidiInstrument.SYNTH_STRINGS_1,
		MidiInstrument.SYNTH_STRINGS_2,
		MidiInstrument.TANGO_ACCORDION,
		MidiInstrument.TREMOLO_STRINGS,
		MidiInstrument.TRUMPET,
		MidiInstrument.TUBULAR_BELLS,
		MidiInstrument.VIOLIN,
		MidiInstrument.VOICE_OOHS,
		MidiInstrument.WHISTLE,
		MidiInstrument.XYLOPHONE
	};

	protected static final double[] SCALETYPEPROBS = {25.0, 15.0, 1.0, 3.0, 2.0, 2.0, 2.0, 1.0/*, 1.0*/};

	public static final int bpmMin = 80;
	public static final int bpmMax = 200;
	public static final int[] bpmValues = Utils.createRangeArray(bpmMin, bpmMax);

	private Random randGen;

	private Pitch mCurrKey;
	private ScaleType mCurrScaleType;

	private int mTempo;
	private int mTimeSigNumer;
	private int mTimeSigDenom;

	private MidiInstrument mMelodyInstrument;
	private MidiInstrument mChordInstrument;

	private boolean mUseRandomTempo;
	private boolean mUseRandomScaleRoot;
	private boolean mUseRandomScaleType;
	private boolean mUseRandomChordInst;
	private boolean mUseRandomMelodyInst;

	private SongGenerator songGenerator;

	public SongWriter()
	{
		randGen = new Random();

		mCurrKey = null;
		mCurrScaleType = null;

		mTempo = 0;
		mTimeSigNumer = 0;
		mTimeSigDenom = 0;

		mMelodyInstrument = null;
		mChordInstrument = null;

		mUseRandomTempo = true;
		mUseRandomScaleRoot = true;
		mUseRandomScaleType = true;
		mUseRandomChordInst = true;
		mUseRandomMelodyInst = true;

		songGenerator = new SongGenerator();
	}

	// TODO: Should I do some validation in the setters? Some of these have a set of valid
	// values, and anything else is invalid...
	public Pitch getKey() {return mCurrKey; }
	public ScaleType getScaleType() { return mCurrScaleType; }

	public int getTempo() { return mTempo; }
	public int getTimeSigNumerator() {return mTimeSigNumer; }
	public int getTimeSigDenominator() {return mTimeSigDenom; }

	public MidiInstrument getMelodyInstrument() { return mMelodyInstrument; }
	public MidiInstrument getChordInstrument() { return mChordInstrument; }

	public void setKey(Pitch newKey) { mCurrKey = newKey; }
	public void setScaleType(ScaleType newScaleType) { mCurrScaleType = newScaleType; }

	public void setTempo(int newTempo) { mTempo = newTempo; }
	public void setTimeSigNumerator(int newNumer) { mTimeSigNumer = newNumer; }
	public void setTimeSigDenominator(int newDenom) { mTimeSigDenom = newDenom; }

	public void setMelodyInstrument(MidiInstrument instr) { mMelodyInstrument = instr; }
	public void setChordInstrument(MidiInstrument instr) { mChordInstrument = instr; }

	public boolean getUseRandomTempo() { return mUseRandomTempo; }
	public void setUseRandomTempo(boolean useRandomTempo) { mUseRandomTempo = useRandomTempo; }

	public boolean getUseRandomScaleRoot() { return mUseRandomScaleType; }
	public void setUseRandomScaleRoot(boolean useRandomScaleRoot) { mUseRandomScaleRoot = useRandomScaleRoot; }
	public boolean getUseRandomScaleType() { return mUseRandomScaleType; }
	public void setUseRandomScaleType(boolean useRandomScaleType) { mUseRandomScaleType = useRandomScaleType; }

	public boolean getUseRandomChordInst() { return mUseRandomChordInst; }
	public void setUseRandomChordInst(boolean useRandomChordInst) { mUseRandomChordInst = useRandomChordInst; }

	public boolean getUseRandomMelodyInst() { return mUseRandomMelodyInst; }
	public void setUseRandomMelodyInst(boolean useRandomMelodyInst) { mUseRandomMelodyInst = useRandomMelodyInst; }

	public Song writeNewSong()
	{
		Song masterpiece = new Song();

		songGenerator.shuffleProbabilities();

//		eighthNoteFactor = (randGen.nextDouble() * 3.0) + 0.2;

		int numTimeSigNums = MusicStructure.TIMESIGNUMVALUES.length;
		int numTimeSigDenoms = MusicStructure.TIMESIGDENOMVALUES.length;
		mTimeSigNumer = masterpiece.timeSigNum = MusicStructure.TIMESIGNUMVALUES[randGen.nextInt(numTimeSigNums)];
		mTimeSigDenom = masterpiece.timeSigDenom = MusicStructure.TIMESIGDENOMVALUES[randGen.nextInt(numTimeSigDenoms)];

		songGenerator.setTimeSignatureNumerator(mTimeSigNumer);

		if (mUseRandomTempo || mTempo < bpmMin || mTempo > bpmMax) {
			mTempo = bpmValues[randGen.nextInt(bpmValues.length)];
		}
		masterpiece.tempo = mTempo;

		if (mUseRandomScaleRoot || mCurrKey == null) {
			mCurrKey = MusicStructure.PITCHES[randGen.nextInt(MusicStructure.NUMPITCHES)];
		}
		masterpiece.key = mCurrKey;

		if (mUseRandomScaleType || mCurrScaleType == null) {
			mCurrScaleType = chooseScaleType();
		}
		masterpiece.scaleType = mCurrScaleType;

		if (mUseRandomChordInst || mChordInstrument == null) {
			mChordInstrument = chordInstruments[randGen.nextInt(chordInstruments.length)];
		}
		masterpiece.chordInstrument = mChordInstrument;

		if (mUseRandomMelodyInst || mMelodyInstrument == null) {
			mMelodyInstrument = melodyInstruments[randGen.nextInt(melodyInstruments.length)];
		}
		masterpiece.melodyInstrument = mMelodyInstrument;

		masterpiece.structure = songGenerator.generateStructure();
		masterpiece.verseProgression = songGenerator.generateVerseProgression();
		masterpiece.chorusProgression = songGenerator.generateChorusProgression();
		masterpiece.bridgeProgression = songGenerator.generateVerseProgression();

//		// change factor here for more variation between rhythm and melody
//		eighthNoteFactor = (randGen.nextDouble() * 3.0) + 0.2;
		// generate based on eighth notes - 1 for verse and 1 for chorus
		masterpiece.verseChordRhythm = songGenerator.generateRhythm(2);
		if (randGen.nextDouble() < 0.2)
			masterpiece.chorusChordRhythm = masterpiece.verseChordRhythm;
		else
			masterpiece.chorusChordRhythm = songGenerator.generateRhythm(2);

		masterpiece.theme = songGenerator.generateTheme();

		return masterpiece;
	} // writeNewSong

	/*
	 * Generation methods
	 */
	public ScaleType chooseScaleType()
	{
		int ndx = Utils.pickNdxByProb(SCALETYPEPROBS);
		return ScaleType.values()[ndx];
	}

} //class SongWriter
