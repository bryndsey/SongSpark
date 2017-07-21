package com.bryndsey.songbuilder;


import com.bryndsey.songbuilder.songstructure.MusicStructure.Cadence;
import com.bryndsey.songbuilder.songstructure.Pattern;

import java.util.List;

public class CadenceTransformer {
	public void applyCadence(Pattern pattern, Cadence type) {
		if (pattern == null || type == null)
			return;

		List<Integer> cadenceChords = type.getChords();
		int numCadenceChords = cadenceChords.size();
		int numPatternChords = pattern.chords.size();

		// realistically, this shouldn't happen, but try to handle it just in case
		if (numCadenceChords > numPatternChords)
			cadenceChords = cadenceChords.subList(numCadenceChords - numPatternChords, numCadenceChords);

		for (int chord = 0; chord < cadenceChords.size(); chord++) {
			pattern.chords.set(numPatternChords - numCadenceChords + chord, cadenceChords.get(chord));
		}

		if (type == Cadence.INTERRUPTED) {
			pattern.chords.set(numPatternChords - 1, Utils.pickNdxByProb(Cadence.INTERRUPTEDCHORDCHANCES) + 1);
		}
	}
}
