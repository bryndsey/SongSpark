package com.bryndsey.songbuilder;

import com.bryndsey.songbuilder.songstructure.MusicStructure;

public class ScaleRandomizer {

	protected static final double[] SCALETYPEPROBS = {25.0, 15.0, 1.0, 3.0, 2.0, 2.0, 2.0, 1.0/*, 1.0*/};

	public MusicStructure.ScaleType chooseScaleType() {
		int ndx = Utils.pickNdxByProb(SCALETYPEPROBS);
		return MusicStructure.ScaleType.values()[ndx];
	}
}
