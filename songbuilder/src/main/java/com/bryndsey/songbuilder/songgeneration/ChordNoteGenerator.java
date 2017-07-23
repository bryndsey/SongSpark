package com.bryndsey.songbuilder.songgeneration;


import com.bryndsey.songbuilder.songstructure.ChordProgression;
import com.bryndsey.songbuilder.songstructure.Note;
import com.bryndsey.songbuilder.songstructure.Pattern;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChordNoteGenerator {

	private final RhythmGenerator rhythmGenerator;

	@Inject
	ChordNoteGenerator(RhythmGenerator rhythmGenerator) {
		this.rhythmGenerator = rhythmGenerator;
	}

	// TODO: Clean this up
	void generateChordNotes(ChordProgression progression) {
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
