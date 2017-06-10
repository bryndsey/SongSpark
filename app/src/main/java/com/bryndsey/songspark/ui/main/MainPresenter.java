package com.bryndsey.songspark.ui.main;

import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.model.MidiSong;
import com.bryndsey.songspark.data.player.MidiPlayer;
import com.bryndsey.songspark.data.player.MidiPlayerPrepareException;

import javax.inject.Inject;

import easymvp.AbstractPresenter;

public class MainPresenter extends AbstractPresenter<MainView> implements MidiPlayer.PlaybackCompleteListener {

	private MidiSongFactory midiSongFactory;
	private MidiSong midiSong;
	private MidiPlayer midiPlayer;

	// TODO: Not sure I like this. I am basically tracking the same state in 2 classes now...
	private boolean isSongReady;
	private boolean isPlaying;

	@Inject
	MainPresenter(MidiSongFactory midiSongFactory, MidiPlayer midiPlayer) {
		this.midiSongFactory = midiSongFactory;
		this.midiPlayer = midiPlayer;

		this.midiPlayer.setPlaybackCompleteListener(this);

		makeSong();
	}

	@Override
	public void onViewAttached(MainView view) {
		super.onViewAttached(view);
		updateSongDisplay();

		enterNotPlayingState();
	}

	private void enterNotPlayingState() {
		isPlaying = false;
		if (isViewAttached()) {
			getView().displayPlayState();
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
		try {
			midiPlayer.preparePlayer(midiSong.midiFile);

			isSongReady = true;
		} catch (MidiPlayerPrepareException midiPlayerPrepareException) {
			midiPlayerPrepareException.printStackTrace();
			isSongReady = false;
		}
	}

	void playPauseSong() {
		if (isPlaying) {
			pause();
		} else if (isSongReady) {
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
			getView().displayPauseState();
		}
	}

	@Override
	public void onPlaybackComplete() {
		enterNotPlayingState();
	}
}
