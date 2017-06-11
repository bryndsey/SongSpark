package com.bryndsey.songspark.ui.main;

import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.model.MidiSong;
import com.bryndsey.songspark.data.player.MidiPlayer;

import javax.inject.Inject;

import easymvp.AbstractPresenter;

public class MainPresenter extends AbstractPresenter<MainView> implements MidiPlayer.PlaybackStateListener {

	private MidiSongFactory midiSongFactory;
	private MidiSong midiSong;
	private MidiPlayer midiPlayer;

	private boolean isPlaying;
	private boolean isPlaybackDisabled;

	private boolean isInitialAttach = true;

	@Inject
	MainPresenter(MidiSongFactory midiSongFactory, MidiPlayer midiPlayer) {
		this.midiSongFactory = midiSongFactory;
		this.midiPlayer = midiPlayer;

		this.midiPlayer.setPlaybackStateListener(this);
	}

	@Override
	public void onViewAttached(MainView view) {
		super.onViewAttached(view);

		if (isInitialAttach) {
			enterNotPlayingState();

			getView().disablePlayback();
			isPlaybackDisabled = true;

			generateNewSong();

			isInitialAttach = false;
		} else {
			syncPlayPauseButtonState();
		}

		updateSongDisplay();
	}

	private void enterNotPlayingState() {
		isPlaying = false;
		if (isViewAttached()) {
			getView().displayPausedState();
		}
	}

	private void syncPlayPauseButtonState() {
		if (isViewAttached()) {
			if (isPlaybackDisabled) {
				getView().disablePlayback();
			} else if (isPlaying) {
				getView().displayPlayingState();
			} else {
				getView().displayPausedState();
			}
		}
	}

	void generateNewSong() {
		makeSong();

		updateSongDisplay();
	}

	private void updateSongDisplay() {
		if (isViewAttached()) {
			Song song = midiSong.song;

			String displayString = "";
			displayString += "Time Signature: ";
			displayString += song.timeSigNum + "/" + song.timeSigDenom;
			displayString += "\nTempo: " + song.tempo + " BPM";
			displayString += "\nChord instrument: " + song.chordInstrument;
			displayString += "\nMelody instrument: " + song.melodyInstrument;
			displayString += "\nScale: " + song.key.toString() + " " + song.scaleType;
			displayString += "\nVerse: " + song.verseProgression.getChords();
			displayString += "\nChorus: " + song.chorusProgression.getChords();

			getView().displaySong(displayString);
		}
	}

	private void makeSong() {
		midiSong = midiSongFactory.newSong();
		midiPlayer.preparePlayer(midiSong.midiFile);
	}

	void playPauseSong() {
		if (isPlaying) {
			pause();
		} else {
			play();
		}
	}

	private void pause() {
		midiPlayer.pause();
		enterNotPlayingState();
	}

	private void play() {
		midiPlayer.startPlaying();
		isPlaying = true;
		if (isViewAttached()) {
			getView().displayPlayingState();
		}
	}

	@Override
	public void onPlaybackReady() {
		isPlaybackDisabled = false;
		if (isViewAttached()) {
			getView().displayPausedState();
		}
	}

	@Override
	public void onPlaybackNotReady() {
		isPlaybackDisabled = true;
		if (isViewAttached()) {
			getView().disablePlayback();
		}
	}

	@Override
	public void onPlaybackComplete() {
		enterNotPlayingState();
	}
}
