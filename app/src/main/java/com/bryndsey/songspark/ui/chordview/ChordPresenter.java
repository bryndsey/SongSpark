package com.bryndsey.songspark.ui.chordview;

import com.bryndsey.songbuilder.songstructure.MusicStructure;
import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.model.MidiSong;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import easymvp.RxPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

class ChordPresenter extends RxPresenter<ChordView> {
	private final MidiSongFactory midiSongFactory;

	private Song song;

	@Inject
	public ChordPresenter(MidiSongFactory midiSongFactory) {
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
	public void onViewAttached(ChordView view) {
		super.onViewAttached(view);

		updateDisplay();
	}


	private void updateDisplay() {
		if (isViewAttached() && song != null) {
			List<ChordViewModel> chords = getViewModelsFromSong();
			getView().displayChords(chords);
		}
	}

	//TODO: Create a mapper class to handle this
	private List<ChordViewModel> getViewModelsFromSong() {
		List<Integer> rawChords = song.verseProgression.getChords();
		rawChords.addAll(song.chorusProgression.getChords());

		List<ChordViewModel> chords = new ArrayList<>(rawChords.size());

		for ( Integer chordDegree : rawChords ) {
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
}
