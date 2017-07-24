package com.bryndsey.songbuilder.songgeneration;

import com.bryndsey.songbuilder.SongGenerationProperties;
import com.bryndsey.songbuilder.songstructure.Note;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.bryndsey.songbuilder.RandomNumberGenerator.getRandomDouble;

@Singleton
public class ArpeggioGenerator {

	private final SongGenerationProperties songGenerationProperties;

	@Inject
	ArpeggioGenerator(SongGenerationProperties songGenerationProperties) {
		this.songGenerationProperties = songGenerationProperties;
	}

	ArrayList<Note> generateArpeggio() {
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
}
