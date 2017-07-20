package com.bryndsey.songbuilder;

import com.bryndsey.songbuilder.songstructure.ChordProgression;
import com.bryndsey.songbuilder.songstructure.MusicStructure.ScaleType;
import com.bryndsey.songbuilder.songstructure.Note;
import com.bryndsey.songbuilder.songstructure.Song;
import com.pdrogfer.mididroid.MidiFile;
import com.pdrogfer.mididroid.MidiTrack;
import com.pdrogfer.mididroid.event.ProgramChange;
import com.pdrogfer.mididroid.event.meta.KeySignature;
import com.pdrogfer.mididroid.event.meta.Tempo;
import com.pdrogfer.mididroid.event.meta.TimeSignature;

import java.util.ArrayList;

public class MidiGenerator {

	private static final int qtrNote = 480; // Still need to figure out why this value works... is it the resolution below?
	private static final int eigthNote = qtrNote / 2;

	private static final int chordChannel = 0;
	private static final int melodyChannel = 1;

	private static final int CHORD_VOLUME = 70;
	private static final int MELODY_VOLUME = 105;

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
		
		int chordTick = renderChords(0, chordTrack, song.verseProgression, song.verseChordRhythm);
		int melodyTick = renderMelody(0, melodyTrack, song.verseProgression);

		int nextTick = Math.max(chordTick, melodyTick);

		renderChords(nextTick, chordTrack, song.chorusProgression, song.chorusChordRhythm);
		renderMelody(nextTick, melodyTrack, song.chorusProgression);

		// It's best not to manually insert EndOfTrack events; MidiTrack will
		// call closeTrack() on itself before writing itself to a file

		// 3. Create a MidiFile with the tracks we created
		ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
		tracks.add(tempoTrack);
		tracks.add(melodyTrack);
		tracks.add(chordTrack);

		MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

		return midi;
	}

	public int renderChords(int tick, MidiTrack track, ChordProgression progression, ArrayList<Integer> rhythm) {
		int basePitch = song.key.getBaseMidiPitch();

		//ArrayList<ArrayList<Note>> melodyNotes = progression.getNotes();
		ArrayList<Integer> chords = progression.getChords();

		for (int ndx = 0; ndx < chords.size(); ndx++) {
			int root = chords.get(ndx);
			int[] triad = song.scaleType.generateTriad(root);

			int chordTick = tick;
			for (Integer duration : rhythm) {
				int noteVelocity = CHORD_VOLUME;
				if (duration < 0) {
					noteVelocity = 0;
					duration *= -1;

				}
				int length = eigthNote * duration;
				for (int interval = 0; interval < triad.length; interval++) {
					track.insertNote(chordChannel, basePitch + triad[interval] - 12, noteVelocity, chordTick, length);
				}
				// TODO: JUST DOING THIS FOR RIGHT NOW TO MAYBE MAKE SONGS SONGS SOUND A LITTLE RICHER, AND ESTABLISH CHORD BETTER
				// REALLY SHOULD IMPOROVE CHORD GENERATION TO HELP
				track.insertNote(chordChannel, basePitch + triad[0] - 24, CHORD_VOLUME + 25, chordTick, length);

				chordTick += length;
			}

			tick = chordTick;
		}

		return tick;
	}

	public int renderMelody(int tick, MidiTrack track, ChordProgression progression) {
		int basePitch = song.key.getBaseMidiPitch();

		//ArrayList<ArrayList<Note>> melodyNotes = progression.getNotes();
		ArrayList<Integer> chords = progression.getChords();

		for (int ndx = 0; ndx < chords.size(); ndx++) {
			int root = chords.get(ndx);

			ArrayList<Note> melodyNotes = progression.getNotes().get(ndx); //song.melody.get(ndx);
			for (Note note : melodyNotes) {
				int noteVelocity = MELODY_VOLUME;
				int numHalfBeats = note.numBeats;
				if (note.numBeats < 0 || note.pitch < 0) {
					noteVelocity = 0;
					numHalfBeats *= -1;
				}
				// numBeats is actually in halfBeats
				int duration = numHalfBeats * eigthNote;
				int pitch;
				if (note.pitch < 0)
					pitch = 0;
				else
					pitch = basePitch + song.scaleType.getInterval(1, root)
							+ song.scaleType.getChordInterval(root, note.pitch);

				track.insertNote(melodyChannel, pitch, noteVelocity, tick, duration);
				tick += duration;
			}

		}
		return tick;
	}
}
