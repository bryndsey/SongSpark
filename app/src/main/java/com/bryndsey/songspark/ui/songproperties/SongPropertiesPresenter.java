package com.bryndsey.songspark.ui.songproperties;

import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.model.MidiSong;

import javax.inject.Inject;

import easymvp.RxPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class SongPropertiesPresenter extends RxPresenter<SongPropertiesView> {

	private Song song;

	@Inject
	public SongPropertiesPresenter(MidiSongFactory midiSongFactory) {
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

		updateDisplay();
	}

	private void updateDisplay() {
		if (isViewAttached() && song != null) {
			getView().setTimeSignature(song.timeSigNum + "/" + song.timeSigDenom);
			getView().setTempo(song.tempo);
			getView().setScale(song.key.toString() + song.scaleType.toString());
			getView().setLeadInstrument(song.melodyInstrument.toString());
			getView().setRhythmInstrument(song.chordInstrument.toString());
		}
	}
}
