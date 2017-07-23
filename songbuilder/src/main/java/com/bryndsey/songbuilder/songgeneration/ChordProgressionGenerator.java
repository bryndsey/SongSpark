package com.bryndsey.songbuilder.songgeneration;

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
	private final RhythmGenerator rhythmGenerator;
	private final NoteGenerator noteGenerator;

	private CadenceTransformer cadenceTransformer;

	private PatternGenerator patternGenerator;

	@Inject
	public ChordProgressionGenerator(PatternGenerator patternGenerator,
									 RhythmGenerator rhythmGenerator,
									 NoteGenerator noteGenerator) {
		this.patternGenerator = patternGenerator;
		this.rhythmGenerator = rhythmGenerator;
		this.noteGenerator = noteGenerator;

		randGen = new Random();
		cadenceTransformer = new CadenceTransformer();
	}

	// slightly better(?) "algorithm" for generating chord progressions
	// TODO: Keep working on this so it is more robust and
	// generates more varied songs
	public ChordProgression generateChordProgression() {
		ChordProgression progression;

		double typeProb = randGen.nextDouble();
		if (typeProb < 0.85) {
			progression = generate4PatternProgression();
		} else {
			progression = generate2PatternProgression();
		}

		generateChordNotes(progression);

		return progression;
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
				noteGenerator.applyMelodyVariation(nextPart);
			}
			partB.patterns.set(0, nextPart);
		}

		if (randGen.nextDouble() < 0.7) {
			Pattern lastPart = new Pattern(partASecond);
			if (randGen.nextDouble() < 0.75) {
				noteGenerator.applyMelodyVariation(lastPart);
			}
			partB.patterns.set(1, lastPart);
		}

		double cadenceChance = randGen.nextDouble();
		if (cadenceChance < 0.35) {
			cadenceTransformer.applyCadence(partASecond, Cadence.HALF);
		} else if (cadenceChance < 0.5) {
			cadenceTransformer.applyCadence(partASecond, Cadence.INTERRUPTED);
		}

		ChordProgression chordProg = partA.add(partB);

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
			if (randGen.nextDouble() < 0.7) {
				noteGenerator.applyMelodyVariation(partB);
			}
		} else {
			partB = patternGenerator.generatePattern(numChords);
		}

		chordProg.patterns.add(partA);
		chordProg.patterns.add(partB);

		return chordProg;
	}

	// TODO: Clean this up
	private void generateChordNotes(ChordProgression progression) {
		int numberOfSubbeats = 2;
		ArrayList<Integer> progressionRhythm = rhythmGenerator.generateRhythm(numberOfSubbeats);

		for (Pattern pattern : progression.patterns) {
			ArrayList<ArrayList<Note>> chordNotes = new ArrayList<>();
			for (int chord = 0; chord < pattern.chords.size(); chord++) {
				float currentBeat = 0;
				ArrayList<Note> notes = new ArrayList<>();
				for (Integer duration : progressionRhythm) {

					float length = (float) duration / (float) numberOfSubbeats;

					if (length < 0) {
						length *= -1;
					} else {
						notes.add(new Note(3, currentBeat, length));
						notes.add(new Note(5, currentBeat, length));
					}

					notes.add(new Note(1, currentBeat, length));


					currentBeat += length;
				}
				chordNotes.add(notes);
			}
			pattern.chordNotes = chordNotes;
		}
	}
}
