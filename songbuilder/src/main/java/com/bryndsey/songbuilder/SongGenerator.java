package com.bryndsey.songbuilder;

import com.bryndsey.songbuilder.songstructure.ChordProgression;
import com.bryndsey.songbuilder.songstructure.MusicStructure.Cadence;

import java.util.Random;

import javax.inject.Inject;

public class SongGenerator {

	private final Random randGen;

	private ChordProgressionGenerator chordProgressionGenerator;

	private CadenceTransformer cadenceTransformer;

	@Inject
	SongGenerator(ChordProgressionGenerator chordProgressionGenerator) {
		this.chordProgressionGenerator = chordProgressionGenerator;
		randGen = new Random();
		cadenceTransformer = new CadenceTransformer();
	}

	public ChordProgression generateVerseProgression() {
		ChordProgression verseProgression = chordProgressionGenerator.generateChordProgression();
		double cadenceChance = randGen.nextDouble();
		if (cadenceChance < 0.75)
			cadenceTransformer.applyCadence(verseProgression.patterns.get(verseProgression.patterns.size() - 1), Cadence.HALF);
		else if (cadenceChance < 0.8)
			cadenceTransformer.applyCadence(verseProgression.patterns.get(verseProgression.patterns.size() - 1), Cadence.AUTHENTIC);
		else if (cadenceChance < 0.9)
			cadenceTransformer.applyCadence(verseProgression.patterns.get(verseProgression.patterns.size() - 1), Cadence.INTERRUPTED);

		return verseProgression;
	}

	public ChordProgression generateChorusProgression() {
		ChordProgression chorus = chordProgressionGenerator.generateChordProgression();
		Cadence type = null;
		double cadenceChance = randGen.nextDouble();
		if (cadenceChance < 0.35)
			type = Cadence.AUTHENTIC;
		else if (cadenceChance < 0.65)
			type = Cadence.PLAGAL;

		if (type != null)
			cadenceTransformer.applyCadence(chorus.patterns.get(chorus.patterns.size() - 1), type);
		return chorus;
	}
}
