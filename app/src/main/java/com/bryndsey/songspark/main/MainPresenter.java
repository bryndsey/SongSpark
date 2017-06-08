package com.bryndsey.songspark.main;

import com.bryndsey.songbuilder.SongWriter;
import com.bryndsey.songbuilder.songstructure.Song;

import easymvp.AbstractPresenter;


public class MainPresenter extends AbstractPresenter<MainView> {

	private SongWriter songWriter;
	private Song song;

	MainPresenter() {
		songWriter = new SongWriter();
		song = songWriter.writeNewSong();
	}

	@Override
	public void onViewAttached(MainView view) {
		super.onViewAttached(view);
		updateDisplay();
	}

	private void updateDisplay() {
		if (isViewAttached()) {
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
