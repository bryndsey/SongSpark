package com.bryndsey.songspark.ui.chordview;

import com.bryndsey.songspark.data.MidiSongFactory;

import javax.inject.Inject;

import easymvp.AbstractPresenter;

class ChordPresenter extends AbstractPresenter<ChordView> {

	private final MidiSongFactory midiSongFactory;

	@Inject
	public ChordPresenter(MidiSongFactory midiSongFactory) {
		this.midiSongFactory = midiSongFactory;
	}

}
