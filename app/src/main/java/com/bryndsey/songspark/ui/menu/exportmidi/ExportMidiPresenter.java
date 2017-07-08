package com.bryndsey.songspark.ui.menu.exportmidi;

import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.filesave.MidiFileSaveException;
import com.bryndsey.songspark.data.filesave.MidiFileSaver;
import com.bryndsey.songspark.data.model.MidiSong;
import com.pdrogfer.mididroid.MidiFile;

import java.io.File;

import javax.inject.Inject;

import easymvp.RxPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

class ExportMidiPresenter extends RxPresenter<ExportMidiView> {

	private final MidiFileSaver midiFileSaver;
	private MidiFile midiFile;

	@Inject
	ExportMidiPresenter(MidiSongFactory midiSongFactory, MidiFileSaver midiFileSaver) {
		Disposable subscription = midiSongFactory.latestSong()
				.subscribeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<MidiSong>() {
					@Override
					public void accept(MidiSong midiSong) throws Exception {
						midiFile = midiSong.midiFile;
					}
				});

		addSubscription(subscription);

		this.midiFileSaver = midiFileSaver;
	}

	void exportMidiSong() {
		if (midiFile != null && isViewAttached()) {
			getView().launchSaveFileSelector();
		}
	}

	void exportToFile(String fileName) {
		try {
			File savedFile = midiFileSaver.savePublicMidiFile(midiFile, fileName);
			if (isViewAttached()) {
				getView().showFileSaveConfirmation(savedFile.getName());
			}
		} catch (MidiFileSaveException e) {
			if (isViewAttached()) {
				getView().showFileSaveError();
			}
		}
	}
}