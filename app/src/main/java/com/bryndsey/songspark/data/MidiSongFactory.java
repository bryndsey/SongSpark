package com.bryndsey.songspark.data;

import com.bryndsey.songbuilder.MidiGenerator;
import com.bryndsey.songbuilder.SongWriter;
import com.bryndsey.songbuilder.songstructure.MusicStructure;
import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.model.MidiSong;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.pdrogfer.mididroid.MidiFile;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

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

	public void newSong() {

		Observable.fromCallable(() -> songWriter.writeNewSong())
		.subscribeOn(Schedulers.computation())
		.subscribe(song -> makeMidiSongFrom(song));
	}

	public void makeMidiSongFrom(Song song) {
		MidiFile songFile = midiGenerator.generateChordMidi(song);

		MidiSong midiSong = new MidiSong(song, songFile);

		midiSongRelay.accept(midiSong);
	}

	public Observable<MidiSong> latestSong() {
		return midiSongRelay;
	}

	public void setTempo(int tempo) {
		songWriter.setTempo(tempo);
	}

	public void setTempoRandomization(boolean isRandom) {
		songWriter.setUseRandomTempo(isRandom);
	}

	public void setScaleRoot(MusicStructure.Pitch scaleRoot) {
		songWriter.setKey(scaleRoot);
	}

	public void setScaleRootRandomization(boolean isRandom) {
		songWriter.setUseRandomScaleRoot(isRandom);
	}

	public void setScaleType(MusicStructure.ScaleType scaleType) {
		songWriter.setScaleType(scaleType);
	}

	public void setScaleTypeRandomization(boolean isRandom) {
		songWriter.setUseRandomScaleType(isRandom);
	}

	public void setLeadInstrument(MusicStructure.MidiInstrument leadInstrument) {
		songWriter.setMelodyInstrument(leadInstrument);
	}

	public void setLeadInstrumentRandomization(boolean isRandom) {
		songWriter.setUseRandomMelodyInst(isRandom);
	}

	public void setRhythmInstrument(MusicStructure.MidiInstrument rhythmInstrument) {
		songWriter.setChordInstrument(rhythmInstrument);
	}

	public void setRhythmInstrumentRandomization(boolean isRandom) {
		songWriter.setUseRandomChordInst(isRandom);
	}
}
