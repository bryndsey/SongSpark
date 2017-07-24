package com.bryndsey.songbuilder.songgeneration;

import com.bryndsey.songbuilder.SongGenerationProperties;
import com.bryndsey.songbuilder.songstructure.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDouble;
import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomIntUpTo;

@Singleton
public class ArpeggioGenerator {

	private static final Integer[][] THREE_PART_ARPEGGIO_PITCHES = {
			{1, 3, 5},
			{1, 5, 3},
			{1, 5, 1},
			{5, 3, 1}
	};

	private static final Integer[][] FOUR_PART_ARPEGGIO_PITCHES = {
			{1, 3, 5, 3},
			{1, 5, 3, 5},
			{1, 5, 3, 1},
			{1, 3, 1, 5},
			{5, 3, 1, 3}
	};

	private final SongGenerationProperties songGenerationProperties;

	@Inject
	ArpeggioGenerator(SongGenerationProperties songGenerationProperties) {
		this.songGenerationProperties = songGenerationProperties;
	}

	ArrayList<Note> generateArpeggio() {
		int numberOfArpeggiosPerChord;
		if (getRandomDouble() < 0.6) {
			numberOfArpeggiosPerChord = 2;
		} else {
			numberOfArpeggiosPerChord = 1;
		}

		return generateArpeggio(numberOfArpeggiosPerChord);
	}

	private ArrayList<Note> generateArpeggio(int numberOfArpeggiosPerChord) {

		ArrayList<Note> noteList = new ArrayList<>();

		int numberOfBeats = songGenerationProperties.getTimeSigNumerator();

		List<Integer> pitchList = new ArrayList<>(numberOfBeats);

		for (int repetitions = 0; repetitions < numberOfArpeggiosPerChord; repetitions++) {
			if (numberOfBeats == 2) {
				pitchList.add(1);
				pitchList.add(5);
			} else if (numberOfBeats == 3) {
				pitchList.addAll(get3BeatArpeggioPitches());
			} else if (numberOfBeats == 4) {
				pitchList.addAll(get4BeatArpeggioPitches());
			}
		}

		for (int beat = 0; beat < pitchList.size(); beat++) {
			int pitch = pitchList.get(beat);
			float startBeat = (float) beat / (float) numberOfArpeggiosPerChord;
			float length = 1f / numberOfArpeggiosPerChord;


			Note note = new Note(pitch, startBeat, length);
			noteList.add(note);
		}

		return noteList;
	}

	private List<Integer> get3BeatArpeggioPitches() {
		Integer[] arpeggioPitches = THREE_PART_ARPEGGIO_PITCHES[getRandomIntUpTo(THREE_PART_ARPEGGIO_PITCHES.length)];

		return Arrays.asList(arpeggioPitches);
	}

	private List<Integer> get4BeatArpeggioPitches() {
		Integer[] arpeggioPitches = FOUR_PART_ARPEGGIO_PITCHES[getRandomIntUpTo(FOUR_PART_ARPEGGIO_PITCHES.length)];

		return Arrays.asList(arpeggioPitches);
	}
}
