package com.bryndsey.songbuilder;

import com.bryndsey.songbuilder.songstructure.MusicStructure;
import com.bryndsey.songbuilder.songstructure.MusicStructure.MidiInstrument;
import com.bryndsey.songbuilder.songstructure.MusicStructure.Pitch;
import com.bryndsey.songbuilder.songstructure.MusicStructure.ScaleType;
import com.bryndsey.songbuilder.songstructure.Song;

import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDouble;
import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomIntUpTo;

public class SongWriter {

	public static final int bpmMin = 80;
	public static final int bpmMax = 200;
	public static final int[] bpmValues = Utils.createRangeArray(bpmMin, bpmMax);

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
	private ScaleRandomizer scaleRandomizer;
	private StructureGenerator structureGenerator;

	public SongWriter() {
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

		// TODO: Inject these
		songGenerator = new SongGenerator();
		scaleRandomizer = new ScaleRandomizer();
		structureGenerator = new StructureGenerator();
	}

	// TODO: Should I do some validation in the setters? Some of these have a set of valid
	// values, and anything else is invalid...
	public Pitch getKey() {
		return mCurrKey;
	}

	public ScaleType getScaleType() {
		return mCurrScaleType;
	}

	public int getTempo() {
		return mTempo;
	}

	public int getTimeSigNumerator() {
		return mTimeSigNumer;
	}

	public int getTimeSigDenominator() {
		return mTimeSigDenom;
	}

	public MidiInstrument getMelodyInstrument() {
		return mMelodyInstrument;
	}

	public MidiInstrument getChordInstrument() {
		return mChordInstrument;
	}

	public void setKey(Pitch newKey) {
		mCurrKey = newKey;
	}

	public void setScaleType(ScaleType newScaleType) {
		mCurrScaleType = newScaleType;
	}

	public void setTempo(int newTempo) {
		mTempo = newTempo;
	}

	public void setTimeSigNumerator(int newNumer) {
		mTimeSigNumer = newNumer;
	}

	public void setTimeSigDenominator(int newDenom) {
		mTimeSigDenom = newDenom;
	}

	public void setMelodyInstrument(MidiInstrument instr) {
		mMelodyInstrument = instr;
	}

	public void setChordInstrument(MidiInstrument instr) {
		mChordInstrument = instr;
	}

	public boolean getUseRandomTempo() {
		return mUseRandomTempo;
	}

	public void setUseRandomTempo(boolean useRandomTempo) {
		mUseRandomTempo = useRandomTempo;
	}

	public boolean getUseRandomScaleRoot() {
		return mUseRandomScaleType;
	}

	public void setUseRandomScaleRoot(boolean useRandomScaleRoot) {
		mUseRandomScaleRoot = useRandomScaleRoot;
	}

	public boolean getUseRandomScaleType() {
		return mUseRandomScaleType;
	}

	public void setUseRandomScaleType(boolean useRandomScaleType) {
		mUseRandomScaleType = useRandomScaleType;
	}

	public boolean getUseRandomChordInst() {
		return mUseRandomChordInst;
	}

	public void setUseRandomChordInst(boolean useRandomChordInst) {
		mUseRandomChordInst = useRandomChordInst;
	}

	public boolean getUseRandomMelodyInst() {
		return mUseRandomMelodyInst;
	}

	public void setUseRandomMelodyInst(boolean useRandomMelodyInst) {
		mUseRandomMelodyInst = useRandomMelodyInst;
	}

	public Song writeNewSong() {
		Song masterpiece = new Song();

		songGenerator.shuffleProbabilities();

//		eighthNoteFactor = (randGen.nextDouble() * 3.0) + 0.2;

		int numTimeSigNums = MusicStructure.TIMESIGNUMVALUES.length;
		int numTimeSigDenoms = MusicStructure.TIMESIGDENOMVALUES.length;
		mTimeSigNumer = masterpiece.timeSigNum = MusicStructure.TIMESIGNUMVALUES[getRandomIntUpTo(numTimeSigNums)];
		mTimeSigDenom = masterpiece.timeSigDenom = MusicStructure.TIMESIGDENOMVALUES[getRandomIntUpTo(numTimeSigDenoms)];

		songGenerator.setTimeSignatureNumerator(mTimeSigNumer);

		if (mUseRandomTempo || mTempo < bpmMin || mTempo > bpmMax) {
			mTempo = bpmValues[getRandomIntUpTo(bpmValues.length)];
		}
		masterpiece.tempo = mTempo;

		// TODO: Move scale root chooser into scaleRandomizer
		if (mUseRandomScaleRoot || mCurrKey == null) {
			mCurrKey = MusicStructure.PITCHES[getRandomIntUpTo(MusicStructure.NUMPITCHES)];
		}
		masterpiece.key = mCurrKey;

		if (mUseRandomScaleType || mCurrScaleType == null) {
			mCurrScaleType = scaleRandomizer.chooseScaleType();
		}
		masterpiece.scaleType = mCurrScaleType;

		if (mUseRandomChordInst || mChordInstrument == null) {
			mChordInstrument = SongInstruments.chordInstruments[getRandomIntUpTo(SongInstruments.chordInstruments.length)];
		}
		masterpiece.chordInstrument = mChordInstrument;

		if (mUseRandomMelodyInst || mMelodyInstrument == null) {
			mMelodyInstrument = SongInstruments.melodyInstruments[getRandomIntUpTo(SongInstruments.melodyInstruments.length)];
		}
		masterpiece.melodyInstrument = mMelodyInstrument;

		masterpiece.structure = structureGenerator.generateStructure();
		masterpiece.verseProgression = songGenerator.generateVerseProgression();
		masterpiece.chorusProgression = songGenerator.generateChorusProgression();
		masterpiece.bridgeProgression = songGenerator.generateVerseProgression();

//		// change factor here for more variation between rhythm and melody
//		eighthNoteFactor = (randGen.nextDouble() * 3.0) + 0.2;
		// generate based on eighth notes - 1 for verse and 1 for chorus
		masterpiece.verseChordRhythm = songGenerator.generateRhythm(2);
		if (getRandomDouble() < 0.2)
			masterpiece.chorusChordRhythm = masterpiece.verseChordRhythm;
		else
			masterpiece.chorusChordRhythm = songGenerator.generateRhythm(2);

		masterpiece.theme = songGenerator.generateTheme();

		return masterpiece;
	} // writeNewSong

} //class SongWriter
