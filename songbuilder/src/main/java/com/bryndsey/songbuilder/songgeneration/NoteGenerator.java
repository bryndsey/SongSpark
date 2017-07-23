package com.bryndsey.songbuilder.songgeneration;

import com.bryndsey.songbuilder.SongGenerationProperties;
import com.bryndsey.songbuilder.Utils;
import com.bryndsey.songbuilder.songstructure.MusicStructure;
import com.bryndsey.songbuilder.songstructure.Note;
import com.bryndsey.songbuilder.songstructure.Pattern;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDouble;
import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomIntUpTo;

public class NoteGenerator {

	private double[] basePitchProbs = {10, 1, 5, 2, 5, 1, 0.025};

	private final SongGenerationProperties songGenerationProperties;
	private final RhythmGenerator rhythmGenerator;

	@Inject
	public NoteGenerator(SongGenerationProperties songGenerationProperties,
						 RhythmGenerator rhythmGenerator) {
		this.songGenerationProperties = songGenerationProperties;
		this.rhythmGenerator = rhythmGenerator;
	}

	public ArrayList<Note> generateNotes() {
		// generate a rhythm based on 1/8th notes
		ArrayList<Integer> rhythm = rhythmGenerator.generateRhythm(2);
		return generateNotes(rhythm);

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
						distancePitchProbs[ndx] = Math.pow(MusicStructure.NUMPITCHES, songGenerationProperties.getNoteRepeatFactor());
					else
						distancePitchProbs[ndx] = Math.pow(MusicStructure.NUMPITCHES - Math.abs(pitchNdx - ndx), 4);

				}
				double[] pitchProbs = Utils.combineProbs(basePitchProbs, distancePitchProbs, 0.2);
				pitch = Utils.pickNdxByProb(pitchProbs) + 1;
			}

			//TODO: Use note length and start times
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
				pattern.notes.set(chord, generateNotes());
			}
		}
		// 40% change to regenerate just modify the last measure/chord
		else if (variationChance < 0.5) {
			pattern.notes.set(numMeasures - 1, generateNotes());
		}
		// 40% chance to modify a a random number of measure so that it has the same rhythm with different pitches
		else if (variationChance < 0.9) {
			int numMeasuresToChange = getRandomIntUpTo(numMeasures) + 1;
			// currently has a chance to repeatedly replace the same measure... I'm ok with this for now...
			for (int measureCount = 0; measureCount < numMeasuresToChange; measureCount++) {
				int iMeasure = getRandomIntUpTo(numMeasures);
				ArrayList<Integer> rhythm = rhythmGenerator.getRhythmFromNotes(pattern.notes.get(iMeasure));
				pattern.notes.set(iMeasure, generateNotes(rhythm));
			}
		}
		// 10% chance to just make the last note last the whole measure
		else {
			int pitch = Utils.pickNdxByProb(basePitchProbs) + 1;
			// have to multiply by 2 here since duration is in half beats
			Note lastNote = new Note(pitch, songGenerationProperties.getTimeSigNumerator() * 2);
			ArrayList<Note> notes = pattern.notes.get(numMeasures - 1);
			notes.clear();
			notes.add(lastNote);
		}
	}
}
