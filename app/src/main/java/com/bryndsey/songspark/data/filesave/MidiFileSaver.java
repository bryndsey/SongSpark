package com.bryndsey.songspark.data.filesave;

import android.content.Context;
import android.os.Environment;

import com.bryndsey.songspark.R;
import com.pdrogfer.mididroid.MidiFile;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MidiFileSaver {

	private static final String MIDI_FILE_EXTENSION = ".mid";
	private static String APP_DIRECTORY_NAME;

	private File tempFileDirectory;
	private File publicFileDirectory;

	@Inject
	MidiFileSaver(Context context) {
		APP_DIRECTORY_NAME = context.getResources().getString(R.string.app_name);
		tempFileDirectory = context.getCacheDir();

		setUpPublicFileDirectory();
	}

	private void setUpPublicFileDirectory() {
		publicFileDirectory = new File(tempFileDirectory, "share");
		if (!publicFileDirectory.exists()) {
			publicFileDirectory.mkdirs();
		}
	}

	private boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	private String getFileNameWithProperExtension(String fileName) {
		String fileNameWithExtension = fileName;
		if (!fileNameWithExtension.endsWith(MIDI_FILE_EXTENSION)) {
			fileNameWithExtension += MIDI_FILE_EXTENSION;
		}

		return fileNameWithExtension;
	}

	private void writeMidiFile(MidiFile midiFile, File saveFile) throws MidiFileSaveException {
		try {
			midiFile.writeToFile(saveFile);
		} catch (IOException e) {
			throw new MidiFileSaveException();
		}
	}

	public File savePublicMidiFile(MidiFile midiFile, String fileName) throws MidiFileSaveException {
		if (publicFileDirectory == null) {
			setUpPublicFileDirectory();
		}

		if (publicFileDirectory == null || !isExternalStorageWritable()) {
			throw new MidiFileSaveException("External storage not available.");
		}

		File saveFile = new File(publicFileDirectory, getFileNameWithProperExtension(fileName));
		writeMidiFile(midiFile, saveFile);
		return saveFile;
	}

	public File saveTemporaryMidiFile(MidiFile midiFile, String fileName) throws MidiFileSaveException {
		File saveFile = new File(tempFileDirectory, getFileNameWithProperExtension(fileName));
		writeMidiFile(midiFile, saveFile);
		return saveFile;
	}
}
