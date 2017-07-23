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

		ArrayList<Note> chordPattern = generateTriadChordNotes();

		for (Pattern pattern : progression.patterns) {
			ArrayList<ArrayList<Note>> chordNotes = new ArrayList<>();
			for (int chord = 0; chord < pattern.chords.size(); chord++) {
				chordNotes.add(chordPattern);
			}
			pattern.chordNotes = chordNotes;
		}
	}

	private ArrayList<Note> generateTriadChordNotes() {
		ArrayList<Note> noteList = new ArrayList<>();

		int numberOfSubbeats = 2;
		ArrayList<Integer> progressionRhythm = rhythmGenerator.generateRhythm(numberOfSubbeats);

		float currentBeat = 0;
		for (Integer duration : progressionRhythm) {

			float length = (float) duration / (float) numberOfSubbeats;

			if (length < 0) {
				length *= -1;
			} else {
				noteList.add(new Note(3, currentBeat, length));
				noteList.add(new Note(5, currentBeat, length));
			}

			noteList.add(new Note(1, currentBeat, length));

			currentBeat += length;
		}

		return noteList;
	}
}
