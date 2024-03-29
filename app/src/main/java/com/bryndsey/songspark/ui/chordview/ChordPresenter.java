package com.bryndsey.songspark.ui.chordview;

import com.bryndsey.songbuilder.songstructure.MusicStructure;
import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.player.MidiPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import easymvp.RxPresenter;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

class ChordPresenter extends RxPresenter<ChordView> {
	private static final int HIGHLIGHT_UPDATE_CHECK_INTERVAL = 100;

	private final MidiPlayer midiPlayer;

	private Song song;
	private List<ChordViewModel> chordViewModels;
	private int currentPlayingTile;

	@Inject
	ChordPresenter(MidiSongFactory midiSongFactory, final MidiPlayer midiPlayer) {
		this.midiPlayer = midiPlayer;
		Disposable subscription = midiSongFactory.latestSong()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(midiSong -> {
					song = midiSong.song;
					updateDisplay();
				});

		addSubscription(subscription);

		startHighlightUpdating();
	}

	private void startHighlightUpdating() {
		Observable.interval(HIGHLIGHT_UPDATE_CHECK_INTERVAL, TimeUnit.MILLISECONDS)
				.flatMap((Function<Long, ObservableSource<?>>) aLong ->
						Observable.just(midiPlayer.getPlayerProgress())
				)
				.map(progress -> {
					if (chordViewModels != null && chordViewModels.size() != 0) {
						Double chordProgress = chordViewModels.size() * (double) progress;
						return chordProgress.intValue();
					}
					return -1;
				})
				.filter(integer ->
						integer >= 0 && integer != currentPlayingTile
				)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(v -> {
					currentPlayingTile = v;
					if (isViewAttached()) {
						getView().highlightChord(currentPlayingTile);
					}
				});
	}

	@Override
	public void onViewAttached(ChordView view) {
		super.onViewAttached(view);

		updateDisplay();

		view.highlightChord(currentPlayingTile);
	}

	private void updateDisplay() {
		if (isViewAttached() && song != null) {
			chordViewModels = getViewModelsFromSong();
			getView().displayChords(chordViewModels);
		}
	}

	//TODO: Create a mapper class to handle this
	private List<ChordViewModel> getViewModelsFromSong() {
		List<Integer> rawChords = song.verseProgression.getChords();
		rawChords.addAll(song.chorusProgression.getChords());

		List<ChordViewModel> chords = new ArrayList<>(rawChords.size());

		for (Integer chordDegree : rawChords) {
			// Get absolute note of key (in semitones)
			int key = song.key.ordinal();
			// Get offset of chord degree from root of chord (in semitones)
			int offset = song.scaleType.getAbsIntervals()[chordDegree - 1];
			// Get absolute note value of chord root (in semitones)
			int chordPitchNdx = (key + offset) % MusicStructure.NUMPITCHES;
			MusicStructure.Pitch newPitch = MusicStructure.Pitch.values()[chordPitchNdx];

			MusicStructure.ChordType chordType = song.scaleType.getTriadChordType(chordDegree);

			ChordViewModel model = new ChordViewModel(newPitch + "" + chordType);
			chords.add(model);
		}

		return chords;
	}

	void seekToChord(int chordPosition) {
		double playerPosition = (double) chordPosition / (double) chordViewModels.size();
		midiPlayer.setPlayerProgress(playerPosition);
	}
}
