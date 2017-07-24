package com.bryndsey.songbuilder.songgeneration;


import com.bryndsey.songbuilder.Utils;
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

		for (Pattern pattern : progression.patterns) {
			ArrayList<ArrayList<Note>> chordNotes = new ArrayList<>();
			for (int chord = 0; chord < pattern.chords.size(); chord++) {
				chordNotes.add(chordPattern);
			}
			pattern.chordNotes = chordNotes;
		}
	}

	private ArrayList<Note> chooseChordNotes() {
		ArrayList<Note> value;
		double generationTypeSelection = getRandomDouble();
		if (generationTypeSelection < 0.6) {
			value = generateTriadChordNotes();
		} else if (generationTypeSelection < 0.95){
			value = generateRhythmArpeggioChordNotes();
		} else {
			value = generateArpeggioChordNotes();
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

	private ArrayList<Note> generateArpeggioChordNotes() {
		return arpeggioGenerator.generateArpeggio();
	}

	private ArrayList<Note> generateRhythmArpeggioChordNotes() {
		ArrayList<Note> noteList = new ArrayList<>();

		int numberOfSubbeats = 2;
		ArrayList<Integer> progressionRhythm = rhythmGenerator.generateRhythm(numberOfSubbeats);

		boolean needToAddChordRoot = true;
		float currentBeat = 0;
		for (Integer duration : progressionRhythm) {

			float length = (float) duration / (float) numberOfSubbeats;

			if (length < 0) {
				length *= -1;
			} else {
				int pitch;
				if (needToAddChordRoot) {
					pitch = 1;
					needToAddChordRoot = false;
				} else {
					int[] triadPitches = {1, 3, 5};
					double[] triadPitchProbs = {0.25, 0.35, 0.4};
					int pitchIndex = Utils.pickNdxByProb(triadPitchProbs);
					pitch = triadPitches[pitchIndex];
				}
				noteList.add(new Note(pitch, currentBeat, length));
			}

			currentBeat += length;
		}

		return noteList;
	}
}
