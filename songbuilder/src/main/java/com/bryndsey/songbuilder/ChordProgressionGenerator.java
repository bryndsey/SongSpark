package com.bryndsey.songbuilder;

import com.bryndsey.songbuilder.songstructure.ChordProgression;
import com.bryndsey.songbuilder.songstructure.MusicStructure.Cadence;
import com.bryndsey.songbuilder.songstructure.Note;
import com.bryndsey.songbuilder.songstructure.Pattern;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChordProgressionGenerator {
	private final Random randGen;

	private CadenceTransformer cadenceTransformer;

	private PatternGenerator patternGenerator;

	@Inject
	public ChordProgressionGenerator(PatternGenerator patternGenerator) {
		randGen = new Random();
		cadenceTransformer = new CadenceTransformer();
		this.patternGenerator = patternGenerator;
	}

	// slightly better(?) "algorithm" for generating chord progressions
	// TODO: Keep working on this so it is more robust and
	// generates more varied songs
	public ChordProgression generateChordProgression() {
		double typeProb = randGen.nextDouble();
		if (typeProb < 0.85)
			return generate4PatternProgression();
		else //(typeProb < 0.95)
			return generate2PatternProgression();
	}

	public ChordProgression generate4PatternProgression() {
		ChordProgression partA, partB;

		partA = generate2PatternProgression();

		partB = generate2PatternProgression();

		Pattern partAFirst = partA.patterns.get(0);
		Pattern partASecond = partA.patterns.get(1);
		if (randGen.nextDouble() < 0.4) {
			Pattern nextPart = new Pattern(partAFirst);
			if (randGen.nextDouble() < 0.7) {
				patternGenerator.applyMelodyVariation(nextPart);
			}
			partB.patterns.set(0, nextPart);
		}

		if (randGen.nextDouble() < 0.7) {
			Pattern lastPart = new Pattern(partASecond);
			if (randGen.nextDouble() < 0.75) {
				patternGenerator.applyMelodyVariation(lastPart);
			}
			partB.patterns.set(1, lastPart);
		}

		double cadenceChance = randGen.nextDouble();
		if (cadenceChance < 0.35)
			cadenceTransformer.applyCadence(partASecond, Cadence.HALF);
		else if (cadenceChance < 0.5)
			cadenceTransformer.applyCadence(partASecond, Cadence.INTERRUPTED);

		ChordProgression chordProg = partA.plus(partB);

		return chordProg;
	}


	public ChordProgression generate2PatternProgression() {
		ChordProgression chordProg = new ChordProgression();

		int numChords = 4;

		Pattern partA, partB;

		partA = patternGenerator.generatePattern(numChords);
		// always start with root chord
		//double[] startChords = {5.0, 0.0, 0.0, 3.0, 0.5, 1.0, 0.0};
		//partA.chords.set(0, Utils.pickNdxByProb(startChords) + 1);

		if (randGen.nextDouble() < 0.45) {
			partB = new Pattern(partA);
			if (randGen.nextDouble() < 0.7)
				patternGenerator.applyMelodyVariation(partB);
		} else
			partB = patternGenerator.generatePattern(numChords);

		chordProg.patterns.add(partA);
		chordProg.patterns.add(partB);

		return chordProg;
	}

	// TODO: Maybe move this to a new injected class so it isn't static
	public static ArrayList<Integer> getRhythmFromNotes(ArrayList<Note> notes) {
		if (notes == null)
			return null;

		ArrayList<Integer> rhythm = new ArrayList<Integer>();
		for (Note note : notes) {
			rhythm.add(note.numBeats);
		}

		return rhythm;
	}
}
