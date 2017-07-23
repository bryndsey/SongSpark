package com.bryndsey.songbuilder.songgeneration;

import com.bryndsey.songbuilder.SongGenerationProperties;
import com.bryndsey.songbuilder.Utils;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDouble;

@Singleton
public class RhythmGenerator {

	private final SongGenerationProperties songGenerationProperties;

	@Inject
	RhythmGenerator(SongGenerationProperties songGenerationProperties) {
		this.songGenerationProperties = songGenerationProperties;
	}

	// TODO: Have this return a list of Notes that use start beats and lengths
	ArrayList<Integer> generateRhythm(int numUnitsPerBeat) {
		if (numUnitsPerBeat <= 0)
			return null;

		int numSubbeats = songGenerationProperties.getTimeSigNumerator() * numUnitsPerBeat;
		ArrayList<Integer> rhythm = new ArrayList<>();

		int note = 0;
		while (note < numSubbeats) {
			// generate even numbered notes with weird distribution
			double[] probs = new double[numSubbeats - note];
			for (int prob = 0; prob < probs.length; prob++) {
				if (prob == 0) {
					probs[prob] = probs.length / songGenerationProperties.getEighthNoteFactor();
				} else if (/*prob == 0 ||*/ (prob + 1) % 2 == 0) {
					probs[prob] = (double) (probs.length - prob);
				} else {
					probs[prob] = 0;
				}

				if (note % 4 == 0) {
					probs[0] = probs[0] / 3.0;
				}
			}
			int numBeats = Utils.pickNdxByProb(probs) + 1;
			if (numBeats <= 0) {
				continue;
			}

			// small chance to be negative (a rest)
			double restChance = getRandomDouble();
			if ((note == 0 && restChance < 0.02) // first note
					|| (note + numBeats >= numSubbeats && restChance < 0.08) //last note
					|| restChance < 0.005) {
				numBeats *= -1;
			}

			note += Math.abs(numBeats);

			rhythm.add(numBeats);
		}
		return rhythm;
	}
}
