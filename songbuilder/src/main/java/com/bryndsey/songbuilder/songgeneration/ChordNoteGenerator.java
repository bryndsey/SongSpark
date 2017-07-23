package com.bryndsey.songbuilder.songgeneration;


import com.bryndsey.songbuilder.SongGenerationProperties;
import com.bryndsey.songbuilder.Utils;
import com.bryndsey.songbuilder.songstructure.ChordProgression;
import com.bryndsey.songbuilder.songstructure.Note;
import com.bryndsey.songbuilder.songstructure.Pattern;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDouble;

@Singleton
public class ChordNoteGenerator {

	private final RhythmGenerator rhythmGenerator;
	private final SongGenerationProperties songGenerationProperties;

	@Inject
	ChordNoteGenerator(RhythmGenerator rhythmGenerator,
					   SongGenerationProperties songGenerationProperties) {
		this.rhythmGenerator = rhythmGenerator;
		this.songGenerationProperties = songGenerationProperties;
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
		ArrayList<Note> noteList = new ArrayList<>();

		int numberOfBeats = songGenerationProperties.getTimeSigNumerator();

		List<Integer> pitchList = new ArrayList<>(numberOfBeats);

		if (numberOfBeats == 2) {
			pitchList.add(1);
			pitchList.add(5);
		} else if (numberOfBeats == 3) {
			pitchList.add(1);
			pitchList.add(3);
			pitchList.add(5);
		} else if (numberOfBeats == 4) {
			pitchList.add(1);
			pitchList.add(3);
			pitchList.add(5);
			pitchList.add(3);
		}

		int numberOfArpeggiosPerChord;
		if (getRandomDouble() < 0.8) {
			numberOfArpeggiosPerChord = 2;
		} else {
			numberOfArpeggiosPerChord = 1;
		}

		for (int beat = 0; beat < pitchList.size() * numberOfArpeggiosPerChord; beat++) {
			int pitch = pitchList.get(beat % pitchList.size());
			float startBeat = (float) beat / (float) numberOfArpeggiosPerChord;
			float length = 1f / numberOfArpeggiosPerChord;


			Note note = new Note(pitch, startBeat, length);
			noteList.add(note);
		}

		return noteList;
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
