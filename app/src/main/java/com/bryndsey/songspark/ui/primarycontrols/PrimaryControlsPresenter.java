package com.bryndsey.songspark.ui.primarycontrols;

import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.player.MidiPlayer;

import javax.inject.Inject;

import easymvp.AbstractPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PrimaryControlsPresenter extends AbstractPresenter<PrimaryControlsView> {

	private MidiSongFactory midiSongFactory;
	private MidiPlayer midiPlayer;

	private boolean isPlaying;
	private boolean isPlaybackDisabled;

	private boolean isInitialAttach = true;

	@Inject
	PrimaryControlsPresenter(MidiSongFactory midiSongFactory, MidiPlayer midiPlayer) {
		this.midiSongFactory = midiSongFactory;
		this.midiPlayer = midiPlayer;

		midiPlayer.getLatestPlaybackStateEvent()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(playbackStateEvent ->
				{
					switch (playbackStateEvent) {
						case NOTREADY:
							onPlaybackNotReady();
							break;
						case READY:
							onPlaybackReady();
							break;
						case STARTED:
							onPlaybackStarted();
							break;
						case COMPLETED:
							onPlaybackComplete();
							break;
					}
				});
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
			getView().transitionToPausedState();
		}
		isPlaying = false;
	}

	private void enterPlayingState() {
		if (isViewAttached() && !isPlaying) {
			getView().transitionToPlayingState();
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

	private void onPlaybackReady() {
		isPlaybackDisabled = false;
		if (isViewAttached()) {
			getView().enablePlayback();
		}
		enterNotPlayingState();
	}

	private void onPlaybackNotReady() {
		isPlaybackDisabled = true;
		if (isViewAttached()) {
			getView().disablePlayback();
		}
	}

	private void onPlaybackStarted() {
		enterPlayingState();
	}

	private void onPlaybackComplete() {
		enterNotPlayingState();
	}
}
