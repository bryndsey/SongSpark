package com.bryndsey.songspark.ui.songproperties;

import com.bryndsey.songbuilder.SongInstruments;
import com.bryndsey.songbuilder.SongWriter;
import com.bryndsey.songbuilder.songstructure.MusicStructure;
import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.MidiSongFactory;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import easymvp.RxPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class SongPropertiesPresenter extends RxPresenter<SongPropertiesView> {

	private final MidiSongFactory midiSongFactory;
	private Song song;

	private static final List<Integer> TEMPO_LIST = Ints.asList(SongWriter.bpmValues);
	private static final List<MusicStructure.Pitch> PITCH_LIST = Arrays.asList(MusicStructure.PITCHES);
	private static final List<MusicStructure.ScaleType> SCALE_TYPE_LIST = Arrays.asList(MusicStructure.ScaleType.values());
	private static final List<MusicStructure.MidiInstrument> LEAD_INSTRUMENT_LIST = Arrays.asList(SongInstruments.melodyInstruments);
	private static final List<MusicStructure.MidiInstrument> RHYTHM_INSTRUMENT_LIST = Arrays.asList(SongInstruments.chordInstruments);
	private static final List<MusicStructure.MidiInstrument> BASS_INSTRUMENT_LIST = Arrays.asList(SongInstruments.BASS_INSTRUMENT);

	@Inject
	SongPropertiesPresenter(MidiSongFactory midiSongFactory) {
		this.midiSongFactory = midiSongFactory;
		Disposable subscription = midiSongFactory.latestSong()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(midiSong -> {
					song = midiSong.song;
					updateDisplay();
				});

		addSubscription(subscription);
	}

	@Override
	public void onViewAttached(SongPropertiesView view) {
		super.onViewAttached(view);

		getView().setTempoList(TEMPO_LIST);
		getView().setScaleRootList(PITCH_LIST);
		getView().setScaleTypeList(SCALE_TYPE_LIST);
		getView().setLeadInstrumentList(LEAD_INSTRUMENT_LIST);
		getView().setRhythmInstrumentList(RHYTHM_INSTRUMENT_LIST);
		getView().setBassInstrumentList(BASS_INSTRUMENT_LIST);

		updateDisplay();
	}

	private void updateDisplay() {
		if (isViewAttached() && song != null) {

			int currentTempoSelection = TEMPO_LIST.indexOf(song.tempo);
			getView().setTempoSelection(currentTempoSelection);

			int currentScalePitchSelection = PITCH_LIST.indexOf(song.key);
			getView().setScaleRootSelection(currentScalePitchSelection);

			int currentScaleTypeSelection = SCALE_TYPE_LIST.indexOf(song.scaleType);
			getView().setScaleTypeSelection(currentScaleTypeSelection);

			int currentLeadInstrument = LEAD_INSTRUMENT_LIST.indexOf(song.melodyInstrument);
			getView().setLeadInstrumentSelection(currentLeadInstrument);

			int currentRhythmInstrument = RHYTHM_INSTRUMENT_LIST.indexOf(song.chordInstrument);
			getView().setRhythmInstrumentSelection(currentRhythmInstrument);

			int currentBassInstrument = BASS_INSTRUMENT_LIST.indexOf(song.bassInstrument);
			getView().setBassInstrumentSelection(currentBassInstrument);
		}
	}

	void updateTempo(int tempoPosition) {
		Integer tempo = TEMPO_LIST.get(tempoPosition);
		if (song.tempo != tempo) {
			song.tempo = tempo;
			midiSongFactory.setTempo(tempo);
			midiSongFactory.makeMidiSongFrom(song);
		}
	}

	void updateScaleRoot(int scaleRootPosition) {
		MusicStructure.Pitch pitch = PITCH_LIST.get(scaleRootPosition);
		if (song.key != pitch) {
			song.key = pitch;
			midiSongFactory.setScaleRoot(pitch);
			midiSongFactory.makeMidiSongFrom(song);
		}
	}

	void updateScaleType(int scaleTypePosition) {
		MusicStructure.ScaleType scaleType = SCALE_TYPE_LIST.get(scaleTypePosition);
		if (song.scaleType != scaleType) {
			song.scaleType = scaleType;
			midiSongFactory.setScaleType(scaleType);
			midiSongFactory.makeMidiSongFrom(song);
		}
	}

	void updateLeadInstrument(int leadInstrumentPosition) {
		MusicStructure.MidiInstrument selectedInstrument = LEAD_INSTRUMENT_LIST.get(leadInstrumentPosition);
		if (song.melodyInstrument != selectedInstrument) {
			song.melodyInstrument = selectedInstrument;
			midiSongFactory.setLeadInstrument(selectedInstrument);
			midiSongFactory.makeMidiSongFrom(song);
		}
	}

	void updateRhythmInstrument(int rhythmInstrumentPosition) {
		MusicStructure.MidiInstrument selectedInstrument = RHYTHM_INSTRUMENT_LIST.get(rhythmInstrumentPosition);
		if (song.chordInstrument != selectedInstrument) {
			song.chordInstrument = selectedInstrument;
			midiSongFactory.setRhythmInstrument(selectedInstrument);
			midiSongFactory.makeMidiSongFrom(song);
		}
	}

	void updateBassInstrument(int bassInstrumentPosition) {
		MusicStructure.MidiInstrument selectedInstrument = BASS_INSTRUMENT_LIST.get(bassInstrumentPosition);
		if (song.bassInstrument != selectedInstrument) {
			song.bassInstrument = selectedInstrument;
			midiSongFactory.setBassInstrument(selectedInstrument);
			midiSongFactory.makeMidiSongFrom(song);
		}
	}

	void updateTempoRandomization(boolean isRandom) {
		midiSongFactory.setTempoRandomization(isRandom);
	}

	void updateScalePitchRandomization(boolean isRandom) {
		midiSongFactory.setScaleRootRandomization(isRandom);
	}

	void updateScaleTypeRandomization(boolean isRandom) {
		midiSongFactory.setScaleTypeRandomization(isRandom);
	}

	void updateLeadInstrumentRandomization(boolean isRandom) {
		midiSongFactory.setLeadInstrumentRandomization(isRandom);
	}

	void updateRhythmInstrumentRandomization(boolean isRandom) {
		midiSongFactory.setRhythmInstrumentRandomization(isRandom);
	}

	void updateBassInstrumentRandomization(boolean isRandom) {
		midiSongFactory.setBassInstrumentRandomization(isRandom);
	}
}
