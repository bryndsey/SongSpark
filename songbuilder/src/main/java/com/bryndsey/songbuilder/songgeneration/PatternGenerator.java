package com.bryndsey.songbuilder.songgeneration;

import com.bryndsey.songbuilder.SongGenerationProperties;
import com.bryndsey.songbuilder.Utils;
import com.bryndsey.songbuilder.songstructure.Note;
import com.bryndsey.songbuilder.songstructure.Pattern;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDouble;
import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomIntUpTo;

@Singleton
public class PatternGenerator {

	private static final double[] INITIAL_CHORD_PROBABILITIES = {8.0, 0.5, 1.0, 4.0, 5.0, 1.0, 0.1};

	private final SongGenerationProperties songGenerationProperties;
	private final NoteGenerator noteGenerator;
	private final RhythmGenerator rhythmGenerator;

	private static final double[][] CHORD_TRANSITION_PROBABILITY_MATRIX = {
			{3, 2, 4, 10, 8, 4, 0.25},
			{5, 1, 2, 5, 8, 2, 0.25},
			{5, 2, 1, 6, 6, 8, 0.25},
			{7, 2, 4, 2, 11, 4, 0.25},
			{10, 2, 3, 6, 2, 6, 0.25},
			{5, 4, 5, 5, 5, 1, 0.25},
			{10, 1, 1, 3, 4, 1, 0}
	};

	@Inject
	public PatternGenerator(SongGenerationProperties songGenerationProperties,
							NoteGenerator noteGenerator,
							RhythmGenerator rhythmGenerator) {
		this.songGenerationProperties = songGenerationProperties;
		this.noteGenerator = noteGenerator;
		this.rhythmGenerator = rhythmGenerator;
	}

	public Pattern generatePattern(int numChords) {
		if (numChords < 0)
			return null;

		Pattern pattern = new Pattern();
		
		pattern.notes.add(noteGenerator.generateNotes());

		pattern.chords = generateChordProgression(numChords);

		for (int chord = 1; chord < numChords; chord++) {

			// chance to repeat a series of notes
			double repeatNoteChance = getRandomDouble();
			if (repeatNoteChance < 0.45) {
				ArrayList<Note> repeatNotes;

				// just repeat first notes, since that sort of sets the theme
				if (repeatNoteChance < 0.15) {
					// grab a previous set of notes, and use the rhythm to create a new set of notes
					repeatNotes = noteGenerator.applyNoteVariation(pattern.notes.get(getRandomIntUpTo(chord)));
//					repeatNotes = noteGenerator.generateNotes(rhythmGenerator.getRhythmFromNotes(pattern.notes.get(getRandomIntUpTo(chord))));
				} else if (repeatNoteChance < 0.2)
					repeatNotes = new ArrayList<Note>(pattern.notes.get(0));
				else
					repeatNotes = new ArrayList<Note>(pattern.notes.get(getRandomIntUpTo(chord)));

				pattern.notes.add(repeatNotes);
			} else
				pattern.notes.add(noteGenerator.generateNotes());
		}
		return pattern;
	}

	// TODO: This is weird to be here when I have a ProgressionGenerator class
	// May need to rename some stuff
	private ArrayList<Integer> generateChordProgression(int numChords) {
		ArrayList<Integer> chords = new ArrayList<>(numChords);

		int currChord = Utils.pickNdxByProb(INITIAL_CHORD_PROBABILITIES);
		// TODO: Figure out better way to deal with this + 1
		chords.add(currChord + 1);

		for (int chord = 1; chord < numChords; chord++) {
			if (shouldChangeChord(currChord, numChords)) {
				currChord = Utils.pickNdxByProb(CHORD_TRANSITION_PROBABILITY_MATRIX[currChord]);
			}
			chords.add(currChord + 1);
		}

		return chords;
	}

	private boolean shouldChangeChord(int currentChord, int numberOfChords) {
		return !(numberOfChords % 2 == 0 && // have halfWay point of measure
				currentChord % (numberOfChords / 2) != 0 && // not at halfway point, so fine to repeat
				getRandomDouble() < songGenerationProperties.getChordRepeatChance()); // use probability
	}
}
