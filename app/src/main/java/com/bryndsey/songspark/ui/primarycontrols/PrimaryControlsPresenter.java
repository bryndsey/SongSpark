package com.bryndsey.songspark.ui.primarycontrols;

import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.player.MidiPlayer;

import javax.inject.Inject;

import easymvp.AbstractPresenter;

public class PrimaryControlsPresenter extends AbstractPresenter<PrimaryControlsView> implements MidiPlayer.PlaybackStateListener {

	private MidiSongFactory midiSongFactory;
	private MidiPlayer midiPlayer;

	private boolean isPlaying;
	private boolean isPlaybackDisabled;

	private boolean isInitialAttach = true;

	@Inject
	PrimaryControlsPresenter(MidiSongFactory midiSongFactory, MidiPlayer midiPlayer) {
		this.midiSongFactory = midiSongFactory;
		this.midiPlayer = midiPlayer;

		this.midiPlayer.setPlaybackStateListener(this);
	}

	@Override
	public void onViewAttached(PrimaryControlsView view) {
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
	}

	private void enterNotPlayingState() {
		if (isViewAttached() && isPlaying) {
			getView().displayPausedState();
		}
		isPlaying = false;
	}

	private void enterPlayingState() {
		if (isViewAttached() && !isPlaying) {
			getView().displayPlayingState();
		}
		isPlaying = true;
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
		midiSongFactory.newSong();
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
		enterPlayingState();
	}

	@Override
	public void onPlaybackReady() {
		isPlaybackDisabled = false;
		if (isViewAttached()) {
			getView().enablePlayback();
		}
		enterNotPlayingState();
	}

	@Override
	public void onPlaybackNotReady() {
		isPlaybackDisabled = true;
		if (isViewAttached()) {
			getView().disablePlayback();
		}
	}

	@Override
	public void onPlaybackStarted() {
		enterPlayingState();
	}

	@Override
	public void onPlaybackComplete() {
		enterNotPlayingState();
	}
}
