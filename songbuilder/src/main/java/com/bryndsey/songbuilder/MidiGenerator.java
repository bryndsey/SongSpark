package com.bryndsey.songbuilder;

import com.bryndsey.songbuilder.songstructure.ChordProgression;
import com.bryndsey.songbuilder.songstructure.MusicStructure.ScaleType;
import com.bryndsey.songbuilder.songstructure.Note;
import com.bryndsey.songbuilder.songstructure.Pattern;
import com.bryndsey.songbuilder.songstructure.Song;
import com.pdrogfer.mididroid.MidiFile;
import com.pdrogfer.mididroid.MidiTrack;
import com.pdrogfer.mididroid.event.ProgramChange;
import com.pdrogfer.mididroid.event.meta.KeySignature;
import com.pdrogfer.mididroid.event.meta.Tempo;
import com.pdrogfer.mididroid.event.meta.TimeSignature;

import java.util.ArrayList;

import static com.bryndsey.songbuilder.songstructure.MusicStructure.getNumberOfPitchesInOctave;

public class MidiGenerator {

	private static final int TICKS_IN_QUARTER_NOTE = MidiFile.DEFAULT_RESOLUTION;

	private static final int chordChannel = 0;
	private static final int melodyChannel = 1;
	private static final int bassChannel = 2;

	private static final int CHORD_VOLUME = 70;
	private static final int MELODY_VOLUME = 105;
	private static final int BASS_VOLUME = 85;

//	private static final int drumChannel = 9;

	private Song song;

	public MidiGenerator() {
		song = null;
	}

	public MidiFile generateChordMidi(Song newSong) {

		if (newSong == null) {
			return null;
		}

		song = newSong;

		// 1. Create some MidiTracks
		MidiTrack tempoTrack = new MidiTrack();
		MidiTrack melodyTrack = new MidiTrack();
		MidiTrack chordTrack = new MidiTrack();

		MidiTrack bassTrack = new MidiTrack();

		// 2. Add events to the tracks
		// 2a. Track 0 is typically the tempo map
		TimeSignature ts = new TimeSignature();
		ts.setTimeSignature(song.timeSigNum, song.timeSigDenom, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

		Tempo t = new Tempo();
		t.setBpm(song.tempo);

		tempoTrack.insertEvent(ts);
		tempoTrack.insertEvent(t);

		// prepare the key signature
		int iMajor, iKey;
		boolean bMajor = song.scaleType == ScaleType.MAJOR;
		if (bMajor) {
			iMajor = KeySignature.SCALE_MAJOR;
			iKey = song.key.getMIDIKeyNumMajor();
		} else {
			iMajor = KeySignature.SCALE_MINOR;
			iKey = song.key.getMIDIKeyNumMinor();
		}
		KeySignature k = new KeySignature(0, 0, iKey, iMajor);
		tempoTrack.insertEvent(k);

		// Add instruments
		ProgramChange chordInstrumentSelect = new ProgramChange(0, chordChannel, song.chordInstrument.ordinal());//.programNumber());
		chordTrack.insertEvent(chordInstrumentSelect);

		ProgramChange melodyInstrumentSelect = new ProgramChange(0, melodyChannel, song.melodyInstrument.ordinal());//programNumber());
		melodyTrack.insertEvent(melodyInstrumentSelect);

		ProgramChange bassInstrumentSelect = new ProgramChange(0, bassChannel, song.bassInstrument.ordinal());//programNumber());
		bassTrack.insertEvent(bassInstrumentSelect);

		int chordTick = renderChords(0, chordTrack, song.verseProgression);
		int melodyTick = renderMelody(0, melodyTrack, song.verseProgression);

		int bassTick = renderBassNotes(0, bassTrack, song.verseProgression);

		int nextTick = Math.max(chordTick, melodyTick);

		renderChords(nextTick, chordTrack, song.chorusProgression);
		renderMelody(nextTick, melodyTrack, song.chorusProgression);

		renderBassNotes(nextTick, bassTrack, song.chorusProgression);

		// It's best not to manually insert EndOfTrack events; MidiTrack will
		// call closeTrack() on itself before writing itself to a file

		// 3. Create a MidiFile with the tracks we created
		ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
		tracks.add(tempoTrack);
		tracks.add(melodyTrack);
		tracks.add(chordTrack);

		tracks.add(bassTrack);

		return new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);
	}

	// TODO: Refactor this and melody method to reuse logic
	private int renderBassNotes(int tick, MidiTrack track, ChordProgression progression) {
		int basePitch = song.key.getBaseMidiPitch();

		for (Pattern pattern : progression.patterns) {
			for (int chord = 0; chord < pattern.chords.size(); chord++) {
				int root = pattern.chords.get(chord);

				int chordTick = tick;

				for (Note note : pattern.bassNotes.get(chord)) {
					int pitch = basePitch + getScalePitchFromPitchRelativeToChord(song.scaleType, root, note.pitch);
					int startTick = (int)(note.startBeatInQuarterNotes * TICKS_IN_QUARTER_NOTE) + chordTick;
					int length = (int)(note.lengthInQuarterNotes * TICKS_IN_QUARTER_NOTE);

					track.insertNote(bassChannel, pitch - 2 * getNumberOfPitchesInOctave(), BASS_VOLUME, startTick, length);
				}

				// FIXME: This currently assumes 4 as the denominator
				tick = chordTick + (TICKS_IN_QUARTER_NOTE * song.timeSigNum);
			}
		}

		return tick;
	}
	// TODO: Refactor this and melody method to reuse logic
	private int renderChords(int tick, MidiTrack track, ChordProgression progression) {
		int basePitch = song.key.getBaseMidiPitch();

		for (Pattern pattern : progression.patterns) {
			for (int chord = 0; chord < pattern.chords.size(); chord++) {
				int root = pattern.chords.get(chord);

				int chordTick = tick;

				for (Note note : pattern.chordNotes.get(chord)) {
					int pitch = basePitch + getScalePitchFromPitchRelativeToChord(song.scaleType, root, note.pitch);
					int startTick = (int)(note.startBeatInQuarterNotes * TICKS_IN_QUARTER_NOTE) + chordTick;
					int length = (int)(note.lengthInQuarterNotes * TICKS_IN_QUARTER_NOTE);

					track.insertNote(chordChannel, pitch - getNumberOfPitchesInOctave(), CHORD_VOLUME, startTick, length);
				}

				// FIXME: This currently assumes 4 as the denominator
				tick = chordTick + (TICKS_IN_QUARTER_NOTE * song.timeSigNum);
			}
		}

		return tick;
	}

	private int renderMelody(int tick, MidiTrack track, ChordProgression progression) {
		int basePitch = song.key.getBaseMidiPitch();

		for (Pattern pattern : progression.patterns) {
			for (int chord = 0; chord < pattern.chords.size(); chord++) {
				int root = pattern.chords.get(chord);

				int chordTick = tick;

				for (Note note : pattern.notes.get(chord)) {
					int pitch = basePitch + getScalePitchFromPitchRelativeToChord(song.scaleType, root, note.pitch);
					int startTick = (int)(note.startBeatInQuarterNotes * TICKS_IN_QUARTER_NOTE) + chordTick;
					int length = (int)(note.lengthInQuarterNotes * TICKS_IN_QUARTER_NOTE);

					track.insertNote(melodyChannel, pitch, MELODY_VOLUME, startTick, length);
				}

				// FIXME: This currently assumes 4 as the denominator
				tick = chordTick + (TICKS_IN_QUARTER_NOTE * song.timeSigNum);
			}
		}

		return tick;
	}

	private int getScalePitchFromPitchRelativeToChord(ScaleType scaleType, int chordNumber, int pitchRelativeToChord) {
		return scaleType.getInterval(1, chordNumber) + scaleType.getChordInterval(chordNumber, pitchRelativeToChord);
	}
}
