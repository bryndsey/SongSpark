package com.bryndsey.songbuilder.songgeneration;


import com.bryndsey.songbuilder.songstructure.ChordProgression;
import com.bryndsey.songbuilder.songstructure.Note;
import com.bryndsey.songbuilder.songstructure.Pattern;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDouble;

@Singleton
public class ChordNoteGenerator {

	private final RhythmGenerator rhythmGenerator;
	private final ArpeggioGenerator arpeggioGenerator;

	@Inject
	ChordNoteGenerator(RhythmGenerator rhythmGenerator,
					   ArpeggioGenerator arpeggioGenerator) {
		this.rhythmGenerator = rhythmGenerator;
		this.arpeggioGenerator = arpeggioGenerator;
	}

	void generateChordNotes(ChordProgression progression) {

		ArrayList<Note> chordPattern = chooseChordNotes();

		ArrayList<Note> bassPattern = arpeggioGenerator.generateRhythmArpeggio();

		for (Pattern pattern : progression.patterns) {
			ArrayList<ArrayList<Note>> chordNotes = new ArrayList<>();

			ArrayList<ArrayList<Note>> bassNotes = new ArrayList<>();

			for (int chord = 0; chord < pattern.chords.size(); chord++) {
				chordNotes.add(chordPattern);

				bassNotes.add(bassPattern);
			}
			pattern.chordNotes = chordNotes;

			pattern.bassNotes = bassNotes;
		}
	}

	private ArrayList<Note> chooseChordNotes() {
		ArrayList<Note> value;
		double generationTypeSelection = getRandomDouble();
		if (generationTypeSelection < 0.65) {
			value = generateTriadChordNotes();
		} else {
			value = arpeggioGenerator.generateArpeggio();
		}

		return value;
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
