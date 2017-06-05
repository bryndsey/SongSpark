package com.bryndsey.songbuilder;

import com.bryndsey.songbuilder.songstructure.ChordProgression;
import com.bryndsey.songbuilder.songstructure.MusicStructure;
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
	
	protected static final int qtrNote = 480; // Still need to figure out why this value works... is it the resolution below?
	protected static final int eigthNote = qtrNote / 2;
	
	protected static final int chordChannel = 0;
	protected static final int melodyChannel = 1;
	
	protected Song song;
	
	public MidiGenerator()
	{
		song = null;
	}
	
	public MidiFile generateChordMidi(Song newSong) {

		if (newSong == null)
		{
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
		if (bMajor)
		{
			iMajor = KeySignature.SCALE_MAJOR;
			iKey = song.key.getMIDIKeyNumMajor();
		}
		else
		{
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
		
		//ChordProgression longerProgression = song.verseProgression.plus(song.chorusProgression);
		//addChordProgressionV2(melodyTrack, chordTrack, longerProgression);//song.verseProgression);
//		addChordProgression(melodyTrack, chordTrack, song.chorusProgression);
//		addChordProgression(melodyTrack, chordTrack, song.bridgeProgression);
		int chordTick = addChordProgressionV3(0, chordTrack, song.verseProgression, song.verseChordRhythm);
		int melodyTick = addMelody(0, melodyTrack, song.verseProgression);
		
		int nextTick = Math.max(chordTick, melodyTick);
		
		addChordProgressionV3(nextTick, chordTrack, song.chorusProgression, song.chorusChordRhythm);
		addMelody(nextTick, melodyTrack, song.chorusProgression);
		
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
	
	// TODO: Maybe clean up parameters list... break out into separate functions for chords and melody
	public void addChordProgression(MidiTrack melodyTrack, MidiTrack chordTrack, ChordProgression progression)
	{
		int channel = 0;
		int basePitch = song.key.getBaseMidiPitch();
		int velocity = 100;
		
		int chordTick = 0;
		int melodyTick = 0;
		
		ArrayList<Integer> themeNotes = song.theme;
		ArrayList<Integer> chords = progression.getChords();
		
		for (int ndx = 0; ndx < chords.size(); ndx++)
		{
			int root = chords.get(ndx);
			int[] triad = MusicStructure.generateTriad(root, song.scaleType);
			
			for (int interval = 0; interval < triad.length; interval++)
			{
				chordTrack.insertNote(channel, basePitch + triad[interval] - 12, velocity, chordTick /*ndx * qtrNote * song.timeSigNum*/, qtrNote * song.timeSigNum);
			}
			chordTick += qtrNote * song.timeSigNum;
			/*for (int melodyNote = 0; melodyNote < themeNotes.size(); melodyNote++)
			{
				int timeStart = (ndx * qtrNote * timeSigNum) + (qtrNote * melodyNote);
				int pitch = basePitch + root + themeNotes.get(melodyNote) + 12;
				noteTrack.insertNote(channel + 1, pitch, velocity + 20, timeStart, qtrNote);
			}*/
			
			themeNotes = progression.getMelody().get(ndx); //song.melody.get(ndx);
			for (int melodyNote = 0; melodyNote < themeNotes.size(); melodyNote++)
			{
				//int timeStart = (ndx * qtrNote * song.timeSigNum) + (qtrNote * melodyNote);
				int pitch = basePitch + MusicStructure.getScaleIntervals(song.scaleType)[(root + themeNotes.get(melodyNote)) % 7];// + 12;
				melodyTrack.insertNote(channel + 1, pitch, velocity + 20, melodyTick/*timeStart*/, qtrNote);
				melodyTick += qtrNote;
			}

		}
	}
	
	// TODO: Maybe clean up parameters list... break out into separate functions for chords and melody
	public void addChordProgressionV2(MidiTrack melodyTrack, MidiTrack chordTrack, ChordProgression progression)
	{
		int channel = 0;
		int basePitch = song.key.getBaseMidiPitch();
		int velocity = 85;
		
		int chordTick = 0;
		int melodyTick = 0;
		
		//ArrayList<ArrayList<Note>> melodyNotes = progression.getNotes();
		ArrayList<Integer> chords = progression.getChords();
		
		for (int ndx = 0; ndx < chords.size(); ndx++)
		{
			int root = chords.get(ndx);
			int[] triad = MusicStructure.generateTriad(root, song.scaleType);
			
			for (int interval = 0; interval < triad.length; interval++)
			{
				chordTrack.insertNote(channel, basePitch + triad[interval] - 12, velocity, chordTick /*ndx * qtrNote * song.timeSigNum*/, qtrNote * song.timeSigNum);
			}
			// TODO: JUST DOING THIS FOR RIGHT NOW TO MAYBE MAKE SONGS SONGS SOUND A LITTLE RICHER..
			// REALLY SHOULD IMPOROVE CHORD GENERATION TO HELP
			chordTrack.insertNote(channel, basePitch + triad[0] - 24, velocity, chordTick, qtrNote * song.timeSigNum);
			chordTick += qtrNote * song.timeSigNum;
			
			ArrayList<Note> melodyNotes = progression.getNotes().get(ndx); //song.melody.get(ndx);
			for (Note note: melodyNotes)
			{
				int noteVelocity = velocity + 30;
				int numHalfBeats = note.numBeats;
				if (note.numBeats < 0 || note.pitch < 0)
				{
					noteVelocity = 0;
					numHalfBeats *= -1;
				}
				// numBeats is actually in halfBeats
				int duration = numHalfBeats * qtrNote / 2;
				int pitch = basePitch + MusicStructure.getScaleIntervals(song.scaleType)[(root + note.pitch) % 7];// + 12;
				melodyTrack.insertNote(channel + 1, pitch, noteVelocity, melodyTick, duration);
				melodyTick += duration;
			}

		}		
	}
	
	// TODO: Maybe clean up parameters list... break out into separate functions for chords and melody
	public int addChordProgressionV3(int tick, MidiTrack track, ChordProgression progression, ArrayList<Integer> rhythm)
	{
		int basePitch = song.key.getBaseMidiPitch();
		int velocity = 80;
		
		//ArrayList<ArrayList<Note>> melodyNotes = progression.getNotes();
		ArrayList<Integer> chords = progression.getChords();
		
		for (int ndx = 0; ndx < chords.size(); ndx++)
		{
			int root = chords.get(ndx);
			int[] triad = MusicStructure.generateTriad(root, song.scaleType);
			
			int chordTick = tick;
			for (Integer duration: rhythm)
			{
				int noteVelocity = velocity;
				if (duration < 0)
				{
					noteVelocity = 0;
					duration *= -1;
					
				}
				int length = eigthNote * duration;
				for (int interval = 0; interval < triad.length; interval++)
				{
					track.insertNote(chordChannel, basePitch + triad[interval] - 12, noteVelocity, chordTick, length);
				}
				// TODO: JUST DOING THIS FOR RIGHT NOW TO MAYBE MAKE SONGS SONGS SOUND A LITTLE RICHER, AND ESTABLISH CHORD BETTER
				// REALLY SHOULD IMPOROVE CHORD GENERATION TO HELP
				track.insertNote(chordChannel, basePitch + triad[0] - 24, velocity, chordTick, length);
				
				chordTick += length;
			}
			
			tick = chordTick;
			
			
/*			ArrayList<Note> melodyNotes = progression.getNotes().get(ndx); //song.melody.get(ndx);
			for (Note note: melodyNotes)
			{
				int noteVelocity = velocity + 30;
				int numHalfBeats = note.numBeats;
				if (note.numBeats < 0 || note.pitch < 0)
				{
					noteVelocity = 0;
					numHalfBeats *= -1;
				}
				// numBeats is actually in halfBeats
				int duration = numHalfBeats * qtrNote / 2;
				int pitch = basePitch + MusicStructure.getScaleIntervals(song.scaleType)[(root + note.pitch) % 7];// + 12;
				melodyTrack.insertNote(channel + 1, pitch, noteVelocity, melodyTick, duration);
				melodyTick += duration;
			}*/

		}	
		
		return tick;
	}
	
	public int addMelody(int tick, MidiTrack track, ChordProgression progression)
	{
		int basePitch = song.key.getBaseMidiPitch();
		int velocity = 105;
		
		//ArrayList<ArrayList<Note>> melodyNotes = progression.getNotes();
		ArrayList<Integer> chords = progression.getChords();
		
		for (int ndx = 0; ndx < chords.size(); ndx++)
		{
			int root = chords.get(ndx);
			
			ArrayList<Note> melodyNotes = progression.getNotes().get(ndx); //song.melody.get(ndx);
			for (Note note: melodyNotes)
			{
				int noteVelocity = velocity;
				int numHalfBeats = note.numBeats;
				if (note.numBeats < 0 || note.pitch < 0)
				{
					noteVelocity = 0;
					numHalfBeats *= -1;
				}
				// numBeats is actually in halfBeats
				int duration = numHalfBeats * eigthNote;
				int pitch;
				if (note.pitch < 0)
					pitch = 0;
				else
					pitch = basePitch + MusicStructure.getInterval(song.scaleType, 1, root) 
						+ MusicStructure.getChordInterval(song.scaleType, root, note.pitch);

				track.insertNote(melodyChannel, pitch, noteVelocity, tick, duration);
				tick += duration;
			}

		}	
		return tick;
	}
	
	/*	public MidiFile generateTempMidi(Song song) {
	
	int eigthNote = 240; // Still need to figure out why this value works... is it the resolution below?
	
	// 1. Create some MidiTracks
	MidiTrack tempoTrack = new MidiTrack();
	MidiTrack noteTrack = new MidiTrack();
	
	// 2. Add events to the tracks
	// 2a. Track 0 is typically the tempo map
	TimeSignature ts = new TimeSignature();
	ts.setTimeSignature(song.timeSigNum, song.timeSigDenom, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
	
	Tempo t = new Tempo();
	t.setBpm(120);
	
	tempoTrack.insertEvent(ts);
	tempoTrack.insertEvent(t);
	
	int channel = 0;
	int pitch = 60;
	int currBeat = 0;
	ArrayList<Integer> rhythm1 = song.rhythm1;
	for (int ndx = 0; ndx < rhythm1.size(); ndx++)
	{
		int velocity = 100;
		int numEigthNotes = rhythm1.get(ndx);
		// handle rests
		if (numEigthNotes < 0)
		{
			numEigthNotes *= -1;
			velocity = 0;
		}
		
		noteTrack.insertNote(channel, pitch, velocity, currBeat * eigthNote, eigthNote * numEigthNotes);
		currBeat += numEigthNotes;
	}
	
	ArrayList<Integer> chorusChordRhythm = song.rhythm2;
	for (int ndx = 0; ndx < rhythm1.size(); ndx++)
	{
		int velocity = 100;
		int numEigthNotes = chorusChordRhythm.get(ndx);
		// handle rests
		if (numEigthNotes < 0)
		{
			numEigthNotes *= -1;
			velocity = 0;
		}
		
		noteTrack.insertNote(channel, pitch, velocity, currBeat * eigthNote, eigthNote * numEigthNotes);
		currBeat += numEigthNotes;
	}
	// It's best not to manually insert EndOfTrack events; MidiTrack will
	// call closeTrack() on itself before writing itself to a file
	
	// 3. Create a MidiFile with the tracks we created
	ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
	tracks.add(tempoTrack);
	tracks.add(noteTrack);
	
	MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);
	
	return midi;
}*/
	
/*	public MidiFile example()
	{
		
		// 1. Create some MidiTracks
		MidiTrack tempoTrack = new MidiTrack();
		MidiTrack noteTrack = new MidiTrack();
		MidiTrack bassTrack = new MidiTrack();
		
		// 2. Add events to the tracks
		// 2a. Track 0 is typically the tempo map
		TimeSignature ts = new TimeSignature();
		ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);
		
		Tempo t = new Tempo();
		t.setBpm(60);
		
		tempoTrack.insertEvent(ts);
		tempoTrack.insertEvent(t);
		
		// 2b. Track 1 will have some notes in it
		for(int i = 0; i < 20; i++) {
			
			int channel = 0, pitch = 41 + i, velocity = 100;
			NoteOn on = new NoteOn(i*480, channel, pitch, velocity);
			NoteOff off = new NoteOff(i*480 + 120, channel, pitch, 0);
			
			noteTrack.insertEvent(on);
			noteTrack.insertEvent(off);
			
			// There is also a utility function for notes that you should use instead of the above.
			noteTrack.insertNote(channel, pitch + 2, velocity, i*480, 120);
			
			bassTrack.insertNote(channel, pitch - 10, velocity, i*480 + 125, 115);
		}
		
		// It's best not to manually insert EndOfTrack events; MidiTrack will
		// call closeTrack() on itself before writing itself to a file
		
		// 3. Create a MidiFile with the tracks we created
		ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
		tracks.add(tempoTrack);
		tracks.add(noteTrack);
		tracks.add(bassTrack);
		
		MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);
		
		return midi;
		
	}*/
	
	
}
