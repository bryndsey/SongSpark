package com.bryndsey.songspark.ui.main;

import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.model.MidiSong;
import com.bryndsey.songspark.data.player.MidiPlayer;
import com.bryndsey.songspark.data.player.MidiPlayerPrepareException;

import javax.inject.Inject;

import easymvp.AbstractPresenter;


public class MainPresenter extends AbstractPresenter<MainView> {

	private MidiSongFactory midiSongFactory;
	private MidiSong midiSong;
	private MidiPlayer midiPlayer;

	@Inject
	MainPresenter(MidiSongFactory midiSongFactory, MidiPlayer midiPlayer) {
		this.midiSongFactory = midiSongFactory;
		this.midiPlayer = midiPlayer;

		makeSong();
	}

	@Override
	public void onViewAttached(MainView view) {
		super.onViewAttached(view);
		updateDisplay();
	}

	void generateNewSong() {
		makeSong();

		updateDisplay();
	}

	private void updateDisplay() {
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
			displayString += "\nBridge: " + song.bridgeProgression.getChords();

			getView().displaySong(displayString);
		}
	}

	private void makeSong() {
		midiSong = midiSongFactory.newSong();
		try {
			midiPlayer.preparePlayer(midiSong.midiFile);
		} catch (MidiPlayerPrepareException midiPlayerPrepareException) {
			midiPlayerPrepareException.printStackTrace();
		}
	}

	void playSong() {
		midiPlayer.startPlaying();
	}
}
