package com.bryndsey.songbuilder;

import com.bryndsey.songbuilder.songstructure.MusicStructure;
import com.bryndsey.songbuilder.songstructure.Note;
import com.bryndsey.songbuilder.songstructure.Pattern;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.bryndsey.songbuilder.ChordProgressionGenerator.getRhythmFromNotes;
import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDouble;
import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDoubleInRange;
import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDoubleUpTo;
import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomIntUpTo;

@Singleton
public class PatternGenerator {

	private static final double[] CHORDPROBS = {8.0, 0.5, 1.0, 4.0, 5.0, 1.0, 0.1};

	private double[] basePitchProbs = {10, 1, 5, 2, 5, 1, 0.025};

	private static final double[][] chordChances = {
			{3, 2, 4, 10, 8, 4, 0.25},
			{5, 1, 2, 5, 8, 2, 0.25},
			{5, 2, 1, 6, 6, 8, 0.25},
			{7, 2, 4, 2, 11, 4, 0.25},
			{10, 2, 3, 6, 2, 6, 0.25},
			{5, 4, 5, 5, 5, 1, 0.25},
			{10, 1, 1, 3, 4, 1, 0}
	};

	private double eighthNoteFactor;
	private double chordRepeatChance;
	private double noteRepeatFactor;
	private int mTimeSigNumer;

	@Inject
	public PatternGenerator() {
	}

	public void setTimeSignatureNumerator(int timeSigNumerator) {
		mTimeSigNumer = timeSigNumerator;
	}

	public void shuffleProbabilities() {
		eighthNoteFactor = getRandomDoubleInRange(0.2, 3.2);//randGen.nextDouble() * 3.0) + 0.2;
		chordRepeatChance = getRandomDoubleUpTo(0.75);//randGen.nextDouble() * 0.75;
		noteRepeatFactor = getRandomDoubleInRange(2, 4);//2 + (randGen.nextDouble() * 2.0);
	}

	public Pattern generatePattern(int numChords) {
		if (numChords < 0)
			return null;

		Pattern pattern = new Pattern();

		int currChord = Utils.pickNdxByProb(CHORDPROBS);
		pattern.chords.add(currChord + 1);
		pattern.melody.add(generateTheme());
		pattern.notes.add(generateNotes());

		for (int chord = 1; chord < numChords; chord++) {

			if (!(numChords % 2 == 0 && // have halfWay point of measure
					chord % (numChords / 2) != 0 && // not at halfway point, so fine to repeat
					getRandomDouble() < chordRepeatChance)) // use probability
				currChord = Utils.pickNdxByProb(chordChances[currChord]);
			pattern.chords.add(currChord + 1);
			pattern.melody.add(generateTheme());

			// chance to repeat a series of notes
			double repeatNoteChance = getRandomDouble();
			if (repeatNoteChance < 0.45) {
				ArrayList<Note> repeatNotes;

				// just repeat first notes, since that sort of sets the theme
				if (repeatNoteChance < 0.15) {
					// grab a previous set of notes, and use the rhythm to create a new set of notes
					repeatNotes = generateNotes(getRhythmFromNotes(pattern.notes.get(getRandomIntUpTo(chord))));
				} else if (repeatNoteChance < 0.2)
					repeatNotes = new ArrayList<Note>(pattern.notes.get(0));
				else
					repeatNotes = new ArrayList<Note>(pattern.notes.get(getRandomIntUpTo(chord)));

				pattern.notes.add(repeatNotes);
			} else
				pattern.notes.add(generateNotes());
		}
		return pattern;
	}

//	public ArrayList<Integer> generateTheme() {
//		ArrayList<Integer> theme = new ArrayList<Integer>();
//
//		for (int note = 0; note < mTimeSigNumer; note++) {
//			theme.add(Utils.pickNdxByProb(basePitchProbs) + 1);
//		}
//
//		return theme;
//	}

	public ArrayList<Note> generateNotes() {
		// generate a rhythm based on 1/8th notes
		ArrayList<Integer> rhythm = generateRhythm(2);
		return generateNotes(rhythm);

	}

	public ArrayList<Integer> generateRhythm(int numUnitsPerBeat) {
		if (numUnitsPerBeat <= 0)
			return null;

		int numSubbeats = mTimeSigNumer * numUnitsPerBeat;
		ArrayList<Integer> rhythm = new ArrayList<Integer>();

		int note = 0;
		while (note < numSubbeats) {
			// generate even numbered notes with weird distribution
			double[] probs = new double[numSubbeats - note];
			for (int prob = 0; prob < probs.length; prob++) {
				if (prob == 0)
					probs[prob] = probs.length / eighthNoteFactor;
				else if (/*prob == 0 ||*/ (prob + 1) % 2 == 0)
					probs[prob] = (double) (probs.length - prob);
				else
					probs[prob] = 0;

				if (note % 4 == 0) {
					probs[0] = probs[0] / 3.0;
				}
			}
			int numBeats = Utils.pickNdxByProb(probs) + 1;
			if (numBeats <= 0)
				continue;

			// small chance to be negative (a rest)
			double restChance = getRandomDouble();
			if ((note == 0 && restChance < 0.02) // first note
					|| (note + numBeats >= numSubbeats && restChance < 0.08) //last note
					|| restChance < 0.005)
				numBeats *= -1;

			note += Math.abs(numBeats);

			rhythm.add(numBeats);
		}
		return rhythm;
	}

	public ArrayList<Note> generateNotes(ArrayList<Integer> rhythm) {
		if (rhythm == null)
			return null;

		ArrayList<Note> notes = new ArrayList<Note>();

		int pitch = -1;
		for (Integer item : rhythm) {
			// check if it is a rest
			if (item < 0)
				pitch = -1;
			else {
				int pitchNdx = pitch - 1;
				double[] distancePitchProbs = new double[MusicStructure.NUMPITCHES];
				for (int ndx = 0; ndx < distancePitchProbs.length; ndx++) {
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

	public void applyMelodyVariation(Pattern pattern) {
		if (pattern == null)
			return;

		int numMeasures = pattern.chords.size();
		double variationChance = getRandomDouble();
		// 10% chance to just make a whole new melody
		if (variationChance < 0.1) {
			for (int chord = 0; chord < numMeasures; chord++) {
				pattern.melody.set(chord, generateTheme());
				pattern.notes.set(chord, generateNotes());
			}
		}
		// 40% change to regenerate just modify the last measure/chord
		else if (variationChance < 0.5) {
			pattern.melody.set(numMeasures - 1, generateTheme());
			pattern.notes.set(numMeasures - 1, generateNotes());
		}
		// 40% chance to modify a a random number of measure so that it has the same rhythm with different pitches
		else if (variationChance < 0.9) {
			int numMeasuresToChange = getRandomIntUpTo(numMeasures) + 1;
			// currently has a chance to repeatedly replace the same measure... I'm ok with this for now...
			for (int measureCount = 0; measureCount < numMeasuresToChange; measureCount++) {
				int iMeasure = getRandomIntUpTo(numMeasures);
				ArrayList<Integer> rhythm = getRhythmFromNotes(pattern.notes.get(iMeasure));
				pattern.notes.set(iMeasure, generateNotes(rhythm));
			}
		}
		// 10% chance to just make the last note last the whole measure
		else {
			int pitch = Utils.pickNdxByProb(basePitchProbs) + 1;
			// have to multiply by 2 here since duration is in half beats
			Note lastNote = new Note(pitch, mTimeSigNumer * 2);
			ArrayList<Note> notes = pattern.notes.get(numMeasures - 1);
			notes.clear();
			notes.add(lastNote);
		}

	}
}
