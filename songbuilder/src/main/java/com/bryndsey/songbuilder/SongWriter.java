package com.bryndsey.songbuilder;


import com.bryndsey.songbuilder.songstructure.ChordProgression;
import com.bryndsey.songbuilder.songstructure.MusicStructure;
import com.bryndsey.songbuilder.songstructure.MusicStructure.Cadence;
import com.bryndsey.songbuilder.songstructure.MusicStructure.Pitch;
import com.bryndsey.songbuilder.songstructure.MusicStructure.ScaleType;
import com.bryndsey.songbuilder.songstructure.MusicStructure.SongPart;
import com.bryndsey.songbuilder.songstructure.Note;
import com.bryndsey.songbuilder.songstructure.Pattern;
import com.bryndsey.songbuilder.songstructure.Song;
import com.pdrogfer.mididroid.event.ProgramChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SongWriter {

	/*
	 * TODO: Should some of these go into the MusicStructure class?
	 */
	public static ProgramChange.MidiProgram[] chordInstruments = {
		ProgramChange.MidiProgram.STRING_ENSEMBLE_1,
		ProgramChange.MidiProgram.ACOUSTIC_GUITAR_STEEL,
		ProgramChange.MidiProgram.ACOUSTIC_GRAND_PIANO,
		ProgramChange.MidiProgram.CELLO,
		ProgramChange.MidiProgram.CHURCH_ORGAN,
		ProgramChange.MidiProgram.DISTORTION_GUITAR,
		ProgramChange.MidiProgram.BRASS_SECTION,
		ProgramChange.MidiProgram.OVERDRIVEN_GUITAR,
		ProgramChange.MidiProgram.TROMBONE,
		ProgramChange.MidiProgram.OBOE,
		ProgramChange.MidiProgram.BARITONE_SAX,
		ProgramChange.MidiProgram.ACCORDION,
		ProgramChange.MidiProgram.ELECTRIC_GUITAR_JAZZ,
		ProgramChange.MidiProgram.BRIGHT_ACOUSTIC_PIANO
	};
	
	public static ProgramChange.MidiProgram[] melodyInstruments = {
		ProgramChange.MidiProgram.STRING_ENSEMBLE_1,
		ProgramChange.MidiProgram.ACOUSTIC_GUITAR_STEEL,
		ProgramChange.MidiProgram.ACOUSTIC_GRAND_PIANO,
		ProgramChange.MidiProgram.VIOLIN,
		ProgramChange.MidiProgram.CHURCH_ORGAN,
		ProgramChange.MidiProgram.DISTORTION_GUITAR,
		ProgramChange.MidiProgram.BRASS_SECTION,
		ProgramChange.MidiProgram.OVERDRIVEN_GUITAR,
		ProgramChange.MidiProgram.TRUMPET,
		ProgramChange.MidiProgram.CLARINET,
		ProgramChange.MidiProgram.ALTO_SAX,
		ProgramChange.MidiProgram.ACCORDION,
		ProgramChange.MidiProgram.FIDDLE,
		ProgramChange.MidiProgram.HARMONICA,
		ProgramChange.MidiProgram.DULCIMER,
		ProgramChange.MidiProgram.MUTED_TRUMPET,
		ProgramChange.MidiProgram.FLUTE,
		ProgramChange.MidiProgram.ELECTRIC_GUITAR_JAZZ,
		ProgramChange.MidiProgram.BRIGHT_ACOUSTIC_PIANO
	};
	
	protected static final double[] CHORDPROBS = {8.0, 1.5, 1.5, 4.0, 5.0, 1.5, 0.1};
	protected static final int NUMCHORDS = CHORDPROBS.length;
	
	protected static final double[] SCALETYPEPROBS = {15.0, 7.0, 0.5, 3.0, 2.0};
	
	protected double[] basePartProbs = {0.6, 0.3, 0.1};
	
	protected double[] basePitchProbs = {10, 1, 5, 2, 5, 1, 0.025};
	
	Random randGen;
	
	Pitch mCurrKey;
	int mTimeSigNumer;
	int mTimeSigDenom;
	
	double eighthNoteFactor;
	double chordRepeatChance;
	
	ProgramChange.MidiProgram mMelodyInstrument;
	ProgramChange.MidiProgram mChordInstrument;
	
	public SongWriter() 
	{
		randGen = new Random();
		
		mCurrKey = null;
		mTimeSigNumer = 0;
		mTimeSigDenom = 0;
		
		mMelodyInstrument = null;
		mChordInstrument = null;
		
		eighthNoteFactor = 1.0;
		chordRepeatChance = 0.75;
	}
	
	public Pitch getKey() {return mCurrKey; }
	public int getTimeSigNumerator() {return mTimeSigNumer; }
	public int getTimeSigDenominator() {return mTimeSigDenom; }
	
	public void setKey(Pitch newKey) { mCurrKey = newKey; }
	public void setTimeSigNumerator(int newNumer) { mTimeSigNumer = newNumer; }
	public void setTimeSigDenominator(int newDenom) { mTimeSigDenom = newDenom; }
	
	public void setMelodyInstrument(ProgramChange.MidiProgram instr) { mMelodyInstrument = instr; }
	public void setChordInstrument(ProgramChange.MidiProgram instr) { mChordInstrument = instr; }
	
	
	public Song writeNewSong()
	{
		Song masterpiece = new Song();
		
		eighthNoteFactor = (randGen.nextDouble() * 3.0) + 0.2;
		chordRepeatChance = randGen.nextDouble();
		
		int numTimeSigNums = MusicStructure.TIMESIGNUMVALUES.length;
		int numTimeSigDenoms = MusicStructure.TIMESIGDENOMVALUES.length;
		mTimeSigNumer = masterpiece.timeSigNum = MusicStructure.TIMESIGNUMVALUES[randGen.nextInt(numTimeSigNums)];
		mTimeSigDenom = masterpiece.timeSigDenom = MusicStructure.TIMESIGDENOMVALUES[randGen.nextInt(numTimeSigDenoms)];
		
		masterpiece.tempo = randGen.nextInt(120) + 80;
		
		masterpiece.scaleType = chooseScaleType();
		masterpiece.key = MusicStructure.PITCHES[randGen.nextInt(MusicStructure.NUMPITCHES)];
		
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
		
		if (mChordInstrument == null)
			masterpiece.chordInstrument = chordInstruments[randGen.nextInt(chordInstruments.length)];
		else
			masterpiece.chordInstrument = mChordInstrument;
		
		if (mMelodyInstrument == null)
			masterpiece.melodyInstrument = melodyInstruments[randGen.nextInt(melodyInstruments.length)];
		else
			masterpiece.melodyInstrument = mMelodyInstrument;
		
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
		
		if (randGen.nextDouble() < 0.65)
		{
			partB = new Pattern(partA);
			if (randGen.nextDouble() < 0.6)
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
		if (randGen.nextDouble() < 0.45)
		{
			Pattern nextPart =  new Pattern(partAFirst);
			if (randGen.nextDouble() < 0.75)
			{
				applyMelodyVariation(nextPart);
			}
			partB.patterns.set(0, nextPart);
		}
		
		if (randGen.nextDouble() < 0.75)
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
		else if (typeProb < 0.95)
			return generate2PatternProgression();
		
		
		ChordProgression chordProg = new ChordProgression();
		
		int numChords = 4;
		
		Pattern partA, partB, partC, partD;
		
		partA = generatePattern(numChords);
		// always start with root chord
		double[] startChords = {5.0, 0.0, 0.0, 3.0, 0.5, 1.0, 0.0};
		partA.chords.set(0, Utils.pickNdxByProb(startChords) + 1);
		
		if (randGen.nextDouble() < 0.65)
		{
			partB = new Pattern(partA);
			if (randGen.nextDouble() < 0.6)
				applyMelodyVariation(partB);
		}
		else
			partB = generatePattern(numChords);
		double cadenceChance = randGen.nextDouble();
		if (cadenceChance < 0.35)
			// end second part on half cadence
			//partB.chords.set(numChords - 1, 4);
			applyCadence(partB, Cadence.HALF);
		else if (cadenceChance < 0.5)
			applyCadence(partB, Cadence.INTERRUPTED);

		if (randGen.nextDouble() < 0.6)
		{
			partC = new Pattern(partA);
			if (randGen.nextDouble() < 0.25)
				applyMelodyVariation(partC);
		}
		else
			partC = generatePattern(numChords);

		if (randGen.nextDouble() < 0.85)
		{
			partD = new Pattern(partB);
			if (randGen.nextDouble() < 0.85)
				// add a different melody at the end to make it more interesting
				applyMelodyVariation(partD);
				//partD.notes.set(partD.notes.size() - 1, generateNotes());
		}
		else
			partD = generatePattern(numChords);
		
		// This part will get applied at the progression level, so don't set it here
		// end last part on half cadence
		//partD.chords.set(numChords - 1, 5);
		//applyCadence(partD, Cadence.HALF);
		
		chordProg.patterns.add(partA);	
		chordProg.patterns.add(partB);
		chordProg.patterns.add(partC);
		chordProg.patterns.add(partD);

		return chordProg;			
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
				currChord = Utils.pickNdxByProb(MusicStructure.chordChances[currChord]) ;
			pattern.chords.add(currChord + 1);
			pattern.melody.add(generateTheme());
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
						distancePitchProbs[ndx] = Math.pow(MusicStructure.NUMPITCHES, 1.2);
					else
						distancePitchProbs[ndx] = Math.pow(MusicStructure.NUMPITCHES - Math.abs(pitchNdx - ndx), 3);
					
				}
				double[] pitchProbs = Utils.combineProbs(basePitchProbs, distancePitchProbs);
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
