package com.bryndsey.songspark.ui.main;

import com.bryndsey.songbuilder.songstructure.Song;
import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.model.MidiSong;

import easymvp.AbstractPresenter;


public class MainPresenter extends AbstractPresenter<MainView> {

	private MidiSongFactory midiSongFactory;
	private MidiSong midiSong;

	MainPresenter() {
		midiSongFactory = new MidiSongFactory();
		midiSong = midiSongFactory.newSong();
	}

	@Override
	public void onViewAttached(MainView view) {
		super.onViewAttached(view);
		updateDisplay();
	}

	void generateNewSong() {
		midiSong = midiSongFactory.newSong();

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
}
