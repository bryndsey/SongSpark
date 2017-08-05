package com.bryndsey.songbuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDoubleInRange;
import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDoubleUpTo;

@Singleton
public class SongGenerationProperties {

	private double eighthNoteFactor;
	private double chordRepeatChance;
	private double noteRepeatFactor;
	private int timeSigNumerator;

	@Inject
	public SongGenerationProperties() {
	}

	// TODO: Rearrange packages and make this package private
	public void setTimeSignatureNumerator(int timeSigNumerator) {
		this.timeSigNumerator = timeSigNumerator;
	}

	public void shuffleProbabilities() {
		eighthNoteFactor = getRandomDoubleInRange(0.2, 3.2);
		chordRepeatChance = getRandomDoubleUpTo(0.75);
		noteRepeatFactor = getRandomDoubleInRange(1, 4);
	}

	public double getEighthNoteFactor() {
		return eighthNoteFactor;
	}

	public double getChordRepeatChance() {
		return chordRepeatChance;
	}

	public double getNoteRepeatFactor() {
		return noteRepeatFactor;
	}

	public int getTimeSigNumerator() {
		return timeSigNumerator;
	}
}
