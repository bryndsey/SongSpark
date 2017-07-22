package com.bryndsey.songbuilder.songgeneration;

import com.bryndsey.songbuilder.songstructure.ChordProgression;
import com.bryndsey.songbuilder.songstructure.MusicStructure.Cadence;

import javax.inject.Inject;

import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDouble;

public class SongGenerator {

	private ChordProgressionGenerator chordProgressionGenerator;

	private CadenceTransformer cadenceTransformer;

	@Inject
	SongGenerator(ChordProgressionGenerator chordProgressionGenerator) {
		this.chordProgressionGenerator = chordProgressionGenerator;
		cadenceTransformer = new CadenceTransformer();
	}

	public ChordProgression generateVerseProgression() {
		ChordProgression verseProgression = chordProgressionGenerator.generateChordProgression();
		double cadenceChance = getRandomDouble();
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
		double cadenceChance = getRandomDouble();
		if (cadenceChance < 0.35)
			type = Cadence.AUTHENTIC;
		else if (cadenceChance < 0.65)
			type = Cadence.PLAGAL;

		if (type != null)
			cadenceTransformer.applyCadence(chorus.patterns.get(chorus.patterns.size() - 1), type);
		return chorus;
	}
}
