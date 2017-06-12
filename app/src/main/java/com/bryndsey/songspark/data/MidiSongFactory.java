package com.bryndsey.songspark.data;

import com.bryndsey.songbuilder.MidiGenerator;
import com.bryndsey.songbuilder.SongWriter;
import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.model.MidiSong;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.pdrogfer.mididroid.MidiFile;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class MidiSongFactory {

	private SongWriter songWriter;
	private MidiGenerator midiGenerator;

	private BehaviorRelay<MidiSong> midiSongRelay = BehaviorRelay.create();

	@Inject
	public MidiSongFactory() {
		songWriter = new SongWriter();
		midiGenerator = new MidiGenerator();
	}

	public MidiSong newSong() {
		//TODO: Maybe do this on another thread?
		Song song = songWriter.writeNewSong();

		MidiFile songFile = midiGenerator.generateChordMidi(song);

		MidiSong midiSong = new MidiSong(song, songFile);

		midiSongRelay.accept(midiSong);

		return midiSong;
	}

	public Observable<MidiSong> latestSong() {
		return midiSongRelay;
	}
}
