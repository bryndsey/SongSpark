package com.bryndsey.songspark.data;

import com.bryndsey.songbuilder.MidiGenerator;
import com.bryndsey.songbuilder.SongWriter;
import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.model.MidiSong;
import com.pdrogfer.mididroid.MidiFile;

import javax.inject.Inject;

public class MidiSongFactory {

	private SongWriter songWriter;
	private MidiGenerator midiGenerator;

	@Inject
	public MidiSongFactory() {
		songWriter = new SongWriter();
		midiGenerator = new MidiGenerator();
	}

	public MidiSong newSong() {
		//TODO: Maybe do this on another thread?
		Song song = songWriter.writeNewSong();

		MidiFile songFile = midiGenerator.generateChordMidi(song);

		return new MidiSong(song, songFile);
	}
}
