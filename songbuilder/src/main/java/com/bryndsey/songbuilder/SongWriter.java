package com.bryndsey.songbuilder;

import com.bryndsey.songbuilder.songstructure.ChordProgression;
import com.bryndsey.songbuilder.songstructure.MusicStructure;
import com.bryndsey.songbuilder.songstructure.MusicStructure.Cadence;
import com.bryndsey.songbuilder.songstructure.MusicStructure.MidiInstrument;
import com.bryndsey.songbuilder.songstructure.MusicStructure.Pitch;
import com.bryndsey.songbuilder.songstructure.MusicStructure.ScaleType;
import com.bryndsey.songbuilder.songstructure.MusicStructure.SongPart;
import com.bryndsey.songbuilder.songstructure.Note;
import com.bryndsey.songbuilder.songstructure.Pattern;
import com.bryndsey.songbuilder.songstructure.Song;

import java.util.ArrayList;
import java.util.List;
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
	
	protected static final double[] CHORDPROBS = {8.0, 0.5, 1.0, 4.0, 5.0, 1.0, 0.1};
	protected static final int NUMCHORDS = CHORDPROBS.length;
	
	protected static final double[] SCALETYPEPROBS = {25.0, 15.0, 1.0, 3.0, 2.0, 2.0, 2.0, 1.0/*, 1.0*/};
	
	protected double[] basePartProbs = {0.6, 0.3, 0.1};
	
	protected double[] basePitchProbs = {10, 1, 5, 2, 5, 1, 0.025};
	
	public static final double[][] chordChances = {
		{3, 2, 4, 10, 8, 4, 0.25},
		{5, 1, 2, 5, 8, 2, 0.25},
		{5, 2, 1, 6, 6, 8, 0.25},
		{7, 2, 4, 2, 11, 4, 0.25},
		{10, 2, 3, 6, 2, 6, 0.25},
		{5, 4, 5, 5, 5, 1, 0.25},
		{10, 1, 1, 3, 4, 1, 0}
	};
	
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
	private boolean mUseRandomScale;
	private boolean mUseRandomChordInst;
	private boolean mUseRandomMelodyInst;
	
	double eighthNoteFactor;
	double chordRepeatChance;
	double noteRepeatFactor;
	
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
		mUseRandomScale = true;
		mUseRandomChordInst = true;
		mUseRandomMelodyInst = true;
		
		eighthNoteFactor = 1.0;
		chordRepeatChance = 0.75;
		noteRepeatFactor = 2.5;
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
	
	public boolean getUseRandomScale() { return mUseRandomScale; }
	public void setUseRandomScale(boolean useRandomScale) { mUseRandomScale = useRandomScale; }
	
	public boolean getUseRandomChordInst() { return mUseRandomChordInst; }
	public void setUseRandomChordInst(boolean useRandomChordInst) { mUseRandomChordInst = useRandomChordInst; }
	
	public boolean getUseRandomMelodyInst() { return mUseRandomMelodyInst; }
	public void setUseRandomMelodyInst(boolean useRandomMelodyInst) { mUseRandomMelodyInst = useRandomMelodyInst; }

	public Song writeNewSong()
	{
		Song masterpiece = new Song();
		
		eighthNoteFactor = (randGen.nextDouble() * 3.0) + 0.2;
		chordRepeatChance = randGen.nextDouble() * 0.75;
		noteRepeatFactor = 2 + (randGen.nextDouble() * 2.0);
		
		int numTimeSigNums = MusicStructure.TIMESIGNUMVALUES.length;
		int numTimeSigDenoms = MusicStructure.TIMESIGDENOMVALUES.length;
		mTimeSigNumer = masterpiece.timeSigNum = MusicStructure.TIMESIGNUMVALUES[randGen.nextInt(numTimeSigNums)];
		mTimeSigDenom = masterpiece.timeSigDenom = MusicStructure.TIMESIGDENOMVALUES[randGen.nextInt(numTimeSigDenoms)];
		
		if (mUseRandomTempo || mTempo < bpmMin || mTempo > bpmMax)
			masterpiece.tempo = bpmValues[randGen.nextInt(bpmValues.length)];
		else
			masterpiece.tempo = mTempo;
		
		if (mUseRandomScale || mCurrKey == null)
			masterpiece.key = MusicStructure.PITCHES[randGen.nextInt(MusicStructure.NUMPITCHES)];
		else
			masterpiece.key = mCurrKey;
		if (mUseRandomScale || mCurrScaleType == null)
			masterpiece.scaleType = chooseScaleType();
		else
			masterpiece.scaleType = mCurrScaleType;
		
		if (mUseRandomChordInst || mChordInstrument == null)
			masterpiece.chordInstrument = chordInstruments[randGen.nextInt(chordInstruments.length)];
		else
			masterpiece.chordInstrument = mChordInstrument;
		
		if (mUseRandomMelodyInst || mMelodyInstrument == null)
			masterpiece.melodyInstrument = melodyInstruments[randGen.nextInt(melodyInstruments.length)];
		else
			masterpiece.melodyInstrument = mMelodyInstrument;
		
		masterpiece.structure = generateStructure();
		masterpiece.verseProgression = generateVerseProgression();
		masterpiece.chorusProgression = generateChorusProgression();
		masterpiece.bridgeProgression = generateVerseProgression();
		
		// change factor here for more variation between rhythm and melody
		eighthNoteFactor = (randGen.nextDouble() * 3.0) + 0.2;
		// generate based on eighth notes - 1 for verse and 1 for chorus
		masterpiece.verseChordRhythm = generateRhythm(2);
		if (randGen.nextDouble() < 0.2)
			masterpiece.chorusChordRhythm = masterpiece.verseChordRhythm;
		else 
			masterpiece.chorusChordRhythm = generateRhythm(2);
		
		masterpiece.theme = generateTheme();
		
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
	
	public ArrayList<SongPart> generateStructure()
	{
		ArrayList<SongPart> structure = new ArrayList<SongPart>();
		double[] partProbs = basePartProbs;
		
		int iNumParts = randGen.nextInt(2) + 4;
		
		for (int iPart = 0; iPart < iNumParts; iPart++)
		{
			int songPartNdx = Utils.pickNdxByProb(partProbs);
			
			structure.add(SongPart.values()[songPartNdx]);
			partProbs = basePartProbs;
			partProbs[songPartNdx] = 0.1;
		}
		return structure;
	}
	
	/*
	 * Some old methods for generating Chord progressions.
	 * Commenting out for now.

	public ArrayList<Integer> generateChordProgression()
	{
		ArrayList<Integer> chordProg = new ArrayList<Integer>();
		// make it an even number of chords for now
		int numChords = 4*(randGen.nextInt(3) + 1);
		for (int chord = 0; chord < numChords; chord++)
		{
			int nextChord = Utils.pickNdxByProb(CHORDPROBS) + 1;
			chordProg.add(nextChord);
		}
		return chordProg;			
	}
	
	// slightly better(?) "algorithm" for generating chord progressions
	public ArrayList<Integer> generateBetterChordProgression()
	{
		ArrayList<Integer> chordProg = new ArrayList<Integer>();
		
		int currChord = Utils.pickNdxByProb(CHORDPROBS);
		chordProg.add(currChord + 1);
		
		// make it an even number of chords for now
		int numChords = 4*(randGen.nextInt(3) + 1);
		for (int chord = 1; chord < numChords; chord++)
		{
			currChord = Utils.pickNdxByProb(MusicStructure.chordChances[currChord]) ;
			chordProg.add(currChord + 1);
		}
		return chordProg;			
	}
	*/
	
	/*
	 * 
	 * 
	 * Some psuedo code for how I want chord progression generation to work
	 * 
	 * Determine base number of measures per chord (maybe choose between 1, 2, and 4?)
	 * Determine # of subprogressions/couplets/triplets and which kind
	 * Generate:
	 * 		pick chords based on base number of meas/chord
	 * 		do rest of generation on these mini progressions like usual
	 * combine mini progressions into one larger one
	 * 
	 */
	
	public ChordProgression generateVerseProgression()
	{
		ChordProgression chorus = generateChordProgression();
		double cadenceChance = randGen.nextDouble();
		if (cadenceChance < 0.75)
			applyCadence(chorus.patterns.get(chorus.patterns.size() - 1), Cadence.HALF);
		else if (cadenceChance < 0.8)
			applyCadence(chorus.patterns.get(chorus.patterns.size() - 1), Cadence.AUTHENTIC);
		else if (cadenceChance < 0.9)
			applyCadence(chorus.patterns.get(chorus.patterns.size() - 1), Cadence.INTERRUPTED);
		
			
		return chorus;
	}
	
	
	public ChordProgression generateChorusProgression()
	{
		ChordProgression chorus = generateChordProgression();
		Cadence type = null;
		double cadenceChance = randGen.nextDouble();
		if (cadenceChance < 0.35)
			type = Cadence.AUTHENTIC;
		else if (cadenceChance < 0.65)
			type = Cadence.PLAGAL;
		
		if (type != null)
			applyCadence(chorus.patterns.get(chorus.patterns.size() - 1), type);
		return chorus;
	}
	
	public ChordProgression generate2PatternProgression()
	{
		ChordProgression chordProg = new ChordProgression();
		
		int numChords = 4;
		
		Pattern partA, partB;
		
		partA = generatePattern(numChords);
		// always start with root chord
		//double[] startChords = {5.0, 0.0, 0.0, 3.0, 0.5, 1.0, 0.0};
		//partA.chords.set(0, Utils.pickNdxByProb(startChords) + 1);
		
		if (randGen.nextDouble() < 0.45)
		{
			partB = new Pattern(partA);
			if (randGen.nextDouble() < 0.7)
				applyMelodyVariation(partB);
		}
		else
			partB = generatePattern(numChords);
		
		chordProg.patterns.add(partA);	
		chordProg.patterns.add(partB);

		return chordProg;
	}
	
	public ChordProgression generate4PatternProgression()
	{
		ChordProgression partA, partB;
		
		partA = generate2PatternProgression();
		
		partB = generate2PatternProgression();
		
		Pattern partAFirst = partA.patterns.get(0);
		Pattern partASecond = partA.patterns.get(1);
		if (randGen.nextDouble() < 0.4)
		{
			Pattern nextPart =  new Pattern(partAFirst);
			if (randGen.nextDouble() < 0.7)
			{
				applyMelodyVariation(nextPart);
			}
			partB.patterns.set(0, nextPart);
		}
		
		if (randGen.nextDouble() < 0.7)
		{
			Pattern lastPart =  new Pattern(partASecond);
			if (randGen.nextDouble() < 0.75)
			{
				applyMelodyVariation(lastPart);
			}
			partB.patterns.set(1, lastPart);
		}
		
		double cadenceChance = randGen.nextDouble();
		if (cadenceChance < 0.35)
			applyCadence(partASecond, Cadence.HALF);
		else if (cadenceChance < 0.5)
			applyCadence(partASecond, Cadence.INTERRUPTED);
		
		ChordProgression chordProg = partA.plus(partB);
		
		return chordProg;
	}
	
	// slightly better(?) "algorithm" for generating chord progressions
	// TODO: Keep working on this so it is more robust and
	// generates more varied songs
	public ChordProgression generateChordProgression()
	{
		double typeProb = randGen.nextDouble();
		if (typeProb < 0.85)
			return generate4PatternProgression();
		else //(typeProb < 0.95)
			return generate2PatternProgression();
		
		
//		ChordProgression chordProg = new ChordProgression();
//		
//		int numChords = 4;
//		
//		Pattern partA, partB, partC, partD;
//		
//		partA = generatePattern(numChords);
//		// always start with root chord
//		double[] startChords = {5.0, 0.0, 0.0, 3.0, 0.5, 1.0, 0.0};
//		partA.chords.set(0, Utils.pickNdxByProb(startChords) + 1);
//		
//		if (randGen.nextDouble() < 0.65)
//		{
//			partB = new Pattern(partA);
//			if (randGen.nextDouble() < 0.6)
//				applyMelodyVariation(partB);
//		}
//		else
//			partB = generatePattern(numChords);
//		double cadenceChance = randGen.nextDouble();
//		if (cadenceChance < 0.35)
//			// end second part on half cadence
//			//partB.chords.set(numChords - 1, 4);
//			applyCadence(partB, Cadence.HALF);
//		else if (cadenceChance < 0.5)
//			applyCadence(partB, Cadence.INTERRUPTED);
//
//		if (randGen.nextDouble() < 0.6)
//		{
//			partC = new Pattern(partA);
//			if (randGen.nextDouble() < 0.25)
//				applyMelodyVariation(partC);
//		}
//		else
//			partC = generatePattern(numChords);
//
//		if (randGen.nextDouble() < 0.85)
//		{
//			partD = new Pattern(partB);
//			if (randGen.nextDouble() < 0.85)
//				// add a different melody at the end to make it more interesting
//				applyMelodyVariation(partD);
//				//partD.notes.set(partD.notes.size() - 1, generateNotes());
//		}
//		else
//			partD = generatePattern(numChords);
//		
//		// This part will get applied at the progression level, so don't set it here
//		// end last part on half cadence
//		//partD.chords.set(numChords - 1, 5);
//		//applyCadence(partD, Cadence.HALF);
//		
//		chordProg.patterns.add(partA);	
//		chordProg.patterns.add(partB);
//		chordProg.patterns.add(partC);
//		chordProg.patterns.add(partD);
//
//		return chordProg;			
	}
	
	public Pattern generatePattern(int numChords)
	{
		if (numChords < 0)
			return null;
		
		Pattern pattern = new Pattern();
		
		int currChord = Utils.pickNdxByProb(CHORDPROBS);
		pattern.chords.add(currChord + 1);
		pattern.melody.add(generateTheme());
		pattern.notes.add(generateNotes());
		
		for (int chord = 1; chord < numChords; chord++)
		{
			
			if (!(numChords % 2 == 0 && // have halfWay point of measure
					chord % (numChords / 2) != 0 && // not at halfway point, so fine to repeat
					randGen.nextDouble() < chordRepeatChance)) // use probability
				currChord = Utils.pickNdxByProb(chordChances[currChord]) ;
			pattern.chords.add(currChord + 1);
			pattern.melody.add(generateTheme());
			
			// chance to repeat a series of notes
			double repeatNoteChance = randGen.nextDouble();
			if (repeatNoteChance < 0.45)
			{
				ArrayList<Note> repeatNotes;
				
				// just repeat first notes, since that sort of sets the theme
				if (repeatNoteChance < 0.15)
				{
					// grab a previous set of notes, and use the rhythm to create a new set of notes
					repeatNotes = generateNotes(getRhythmFromNotes(pattern.notes.get(randGen.nextInt(chord))));
				}
				else if (repeatNoteChance < 0.2)
					repeatNotes = new ArrayList<Note>(pattern.notes.get(0));
				else
					repeatNotes = new ArrayList<Note>(pattern.notes.get(randGen.nextInt(chord)));
				
				pattern.notes.add(repeatNotes);
			}
			else
				pattern.notes.add(generateNotes());
		}
		return pattern;	
	}
	
	public ArrayList<Integer> generateRhythm(int numUnitsPerBeat)
	{
		if (numUnitsPerBeat <= 0)
			return null;
		
		int numSubbeats = mTimeSigNumer * numUnitsPerBeat;
		ArrayList<Integer> rhythm = new ArrayList<Integer>();
		
		int note = 0;
		while (note < numSubbeats)
		{
			// generate even numbered notes with weird distribution
			double[] probs = new double[numSubbeats - note];
			for (int prob = 0; prob < probs.length; prob++)
			{
				if (prob == 0)
					probs[prob] = probs.length/eighthNoteFactor;
				else if (/*prob == 0 ||*/ (prob + 1) % 2 == 0 )
					probs[prob] = (double)(probs.length - prob);
				else 
					probs[prob] = 0;
				
				if (note % 4 == 0)
				{
					probs[0] = probs[0]/3.0;
				}
			}
			int numBeats = Utils.pickNdxByProb(probs) + 1;
			if (numBeats <= 0)
				continue;
			
			// small chance to be negative (a rest)
			double restChance = randGen.nextDouble();
			if ((note == 0 && restChance < 0.02) // first note
					|| (note + numBeats >= numSubbeats && restChance < 0.08) //last note
					|| restChance < 0.005)
				numBeats *= -1;
			
			note += Math.abs(numBeats);
			
			rhythm.add(numBeats);
		}
		return rhythm;
	}
	
	public ArrayList<Integer> generateTheme()
	{
		ArrayList<Integer> theme = new ArrayList<Integer>();
		
		for (int note = 0; note < mTimeSigNumer; note++)
		{
			theme.add(Utils.pickNdxByProb(basePitchProbs) + 1);
		}
				
		return theme;
	}
	
	public ArrayList<Note> generateNotes()
	{
		// generate a rhythm based on 1/8th notes
		ArrayList<Integer> rhythm = generateRhythm(2);
		return generateNotes(rhythm);

	}
	
	public ArrayList<Note> generateNotes(ArrayList<Integer> rhythm)
	{
		if (rhythm == null)
			return null;
		
		ArrayList<Note> notes = new ArrayList<Note>();
		
		int pitch = -1;
		for (Integer item: rhythm)
		{
			// check if it is a rest
			if (item < 0)
				pitch = -1;
			else
			{
				int pitchNdx = pitch - 1;
				double[] distancePitchProbs = new double[MusicStructure.NUMPITCHES];
				for (int ndx = 0; ndx < distancePitchProbs.length; ndx++)
				{
					if (pitchNdx < 0)
						distancePitchProbs[ndx] = 1;
					else if (ndx == pitchNdx)
						distancePitchProbs[ndx] = Math.pow(MusicStructure.NUMPITCHES, noteRepeatFactor);
					else
						distancePitchProbs[ndx] = Math.pow(MusicStructure.NUMPITCHES - Math.abs(pitchNdx - ndx), 4);
					
				}
				double[] pitchProbs = Utils.combineProbs(basePitchProbs, distancePitchProbs, 0.2);
				pitch = Utils.pickNdxByProb(pitchProbs) + 1;
			}
			
			notes.add(new Note(pitch, item));
		}
		return notes;
	}
	
	// TODO: Old way of creating melody... this is now created with the pattern.
	// Get rid of it once I am sure that is what I want to do.
//	public ArrayList<ArrayList<Integer>> generateMelody(ArrayList<Integer> chords)
//	{
//		ArrayList<ArrayList<Integer>> melody = new ArrayList<ArrayList<Integer>>();
//		for (int chord = 0; chord < chords.size(); chord++)
//		{
//			melody.add(generateTheme());
//		}
//		return melody;
//	}
	
	public void applyCadence(Pattern pattern, Cadence type)
	{
		if (pattern == null || type == null)
			return;
		
		List<Integer> cadenceChords = type.getChords();
		int numCadenceChords = cadenceChords.size();
		int numPatternChords = pattern.chords.size();
		
		// realistically, this shouldn't happen, but try to handle it just in case
		if (numCadenceChords > numPatternChords)
			cadenceChords = cadenceChords.subList(numCadenceChords - numPatternChords, numCadenceChords);
		
		for (int chord = 0; chord < cadenceChords.size(); chord++)
		{
			pattern.chords.set(numPatternChords - numCadenceChords + chord, cadenceChords.get(chord));
		}
		
		if (type == Cadence.INTERRUPTED)
		{
			pattern.chords.set(numPatternChords - 1, Utils.pickNdxByProb(Cadence.INTERRUPTEDCHORDCHANCES) + 1);
		}
	}
	
	public void applyMelodyVariation(Pattern pattern)
	{
		if (pattern == null)
			return;
		
		int numMeasures = pattern.chords.size();
		double variationChance = randGen.nextDouble();
		// 10% chance to just make a whole new melody
		if (variationChance < 0.1)
		{
			for (int chord = 0; chord < numMeasures; chord++)
			{
				pattern.melody.set(chord, generateTheme());
				pattern.notes.set(chord, generateNotes());
			}
		}
		// 40% change to regenerate just modify the last measure/chord
		else if (variationChance < 0.5)
		{
			pattern.melody.set(numMeasures - 1, generateTheme());
			pattern.notes.set(numMeasures - 1, generateNotes());
		}
		// 40% chance to modify a a random number of measure so that it has the same rhythm with different pitches
		else if (variationChance < 0.9)
		{
			int numMeasuresToChange = randGen.nextInt(numMeasures) + 1;
			// currently has a chance to repeatedly replace the same measure... I'm ok with this for now...
			for (int measureCount = 0; measureCount < numMeasuresToChange; measureCount++)
			{
				int iMeasure = randGen.nextInt(numMeasures);
				ArrayList<Integer> rhythm = getRhythmFromNotes(pattern.notes.get(iMeasure));
				pattern.notes.set(iMeasure, generateNotes(rhythm));
			}
		}
		// 10% chance to just make the last note last the whole measure
		else
		{
			int pitch = Utils.pickNdxByProb(basePitchProbs) + 1;
			// have to multiply by 2 here since duration is in half beats
			Note lastNote = new Note(pitch, mTimeSigNumer * 2);
			ArrayList<Note> notes = pattern.notes.get(numMeasures - 1);
			notes.clear();
			notes.add(lastNote);	
		}
		
	}
	
	public ArrayList<Integer> getRhythmFromNotes(ArrayList<Note> notes)
	{
		if (notes == null)
			return null;
		
		ArrayList<Integer> rhythm = new ArrayList<Integer>();		
		for (Note note: notes)
		{
			rhythm.add(note.numBeats);
		}
		
		return rhythm;
	}
	
} //class SongWriter
