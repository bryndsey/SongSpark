package com.bryndsey.songspark.ui.menu.exportmidi;

import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.filesave.MidiFileSaveException;
import com.bryndsey.songspark.data.filesave.MidiFileSaver;
import com.pdrogfer.mididroid.MidiFile;

import java.io.File;

import javax.inject.Inject;

import easymvp.RxPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

class ExportMidiPresenter extends RxPresenter<ExportMidiView> {

	private final MidiFileSaver midiFileSaver;
	private MidiFile midiFile;

	@Inject
	ExportMidiPresenter(MidiSongFactory midiSongFactory, MidiFileSaver midiFileSaver) {
		Disposable subscription = midiSongFactory.latestSong()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(midiSong -> midiFile = midiSong.midiFile);

		addSubscription(subscription);

		this.midiFileSaver = midiFileSaver;
	}

	void exportToShareableFile() {
		try {
			File savedFile = midiFileSaver.saveShareableMidiFile(midiFile);
			if (isViewAttached()) {
				getView().shareFile(savedFile);
			}
		} catch (MidiFileSaveException e) {
			if (isViewAttached()) {
				if (e.getMessage() != null && !e.getMessage().equals("")) {
					getView().showFileSaveErrorWithMessage(e.getMessage());
				} else {
					getView().showFileSaveError();
				}
			}
		}
	}
}
