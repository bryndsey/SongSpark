package com.bryndsey.songbuilder.songgeneration;

import com.bryndsey.songbuilder.Utils;
import com.bryndsey.songbuilder.songstructure.MusicStructure.SongPart;

import java.util.ArrayList;

import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomIntInRange;

public class StructureGenerator {

	private static double[] BASE_PARTS_PROBABILITIES = {0.6, 0.3, 0.1};

	public ArrayList<SongPart> generateStructure() {
		ArrayList<SongPart> structure = new ArrayList<SongPart>();
		double[] partProbs = BASE_PARTS_PROBABILITIES;

		int iNumParts = getRandomIntInRange(4, 6);// randGen.nextInt(2) + 4;

		for (int iPart = 0; iPart < iNumParts; iPart++) {
			int songPartNdx = Utils.pickNdxByProb(partProbs);

			structure.add(SongPart.values()[songPartNdx]);
			partProbs = BASE_PARTS_PROBABILITIES;
			partProbs[songPartNdx] = 0.1;
		}
		return structure;
	}
}
