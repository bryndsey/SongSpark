package com.bryndsey.songspark.data.model;

import com.bryndsey.songbuilder.songstructure.Song;
import com.pdrogfer.mididroid.MidiFile;

public class MidiSong {

	public final Song song;
	public final MidiFile midiFile;

	public MidiSong(Song song, MidiFile midiFile) {
		this.song = song;
		this.midiFile = midiFile;
	}
}
