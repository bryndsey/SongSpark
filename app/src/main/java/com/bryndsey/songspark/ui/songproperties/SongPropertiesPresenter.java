package com.bryndsey.songspark.ui.songproperties;

import android.util.Log;

import com.bryndsey.songbuilder.SongWriter;
import com.bryndsey.songbuilder.songstructure.MusicStructure;
import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.model.MidiSong;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import easymvp.RxPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class SongPropertiesPresenter extends RxPresenter<SongPropertiesView> {

	private final MidiSongFactory midiSongFactory;
	private Song song;

	private static final List<MusicStructure.MidiInstrument> RHYTHM_INSTRUMENT_LIST = Arrays.asList(SongWriter.chordInstruments);

	@Inject
	SongPropertiesPresenter(MidiSongFactory midiSongFactory) {
		this.midiSongFactory = midiSongFactory;
		Disposable subscription = midiSongFactory.latestSong()
				.subscribeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<MidiSong>() {
					@Override
					public void accept(MidiSong midiSong) throws Exception {
						song = midiSong.song;
						updateDisplay();
					}
				});

		addSubscription(subscription);
	}

	@Override
	public void onViewAttached(SongPropertiesView view) {
		super.onViewAttached(view);

		getView().setRhythmInstrumentList(RHYTHM_INSTRUMENT_LIST);

		updateDisplay();
	}

	private void updateDisplay() {
		if (isViewAttached() && song != null) {
			getView().setTimeSignature(song.timeSigNum + "/" + song.timeSigDenom);
			getView().setTempo(song.tempo + " bpm");
			getView().setScale(song.key.toString() + " " + song.scaleType.toString());
			getView().setLeadInstrument(song.melodyInstrument.toString());

			int currentRhythmInstrument = RHYTHM_INSTRUMENT_LIST.indexOf(song.chordInstrument);
			getView().setInstrumentSelection(currentRhythmInstrument);
		}
	}

	void updateRhythmInstrument(int rhythmInstrumentPosition) {
		MusicStructure.MidiInstrument selectedInstrument = RHYTHM_INSTRUMENT_LIST.get(rhythmInstrumentPosition);
		if (song.chordInstrument != selectedInstrument) {
			song.chordInstrument = selectedInstrument;
			midiSongFactory.makeMidiSongFrom(song);
		}
	}
}
