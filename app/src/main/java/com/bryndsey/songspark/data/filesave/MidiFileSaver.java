package com.bryndsey.songspark.data.filesave;

import android.content.Context;
import android.os.Environment;

import com.bryndsey.songspark.R;
import com.pdrogfer.mididroid.MidiFile;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public class MidiFileSaver {

	private static final String MIDI_FILE_EXTENSION = ".mid";
	private static String APP_DIRECTORY_NAME;

	private Context context;

	private File tempFileDirectory;
	private File publicFileDirectory;

	@Inject
	MidiFileSaver(Context context) {
		this.context = context;

		APP_DIRECTORY_NAME = context.getResources().getString(R.string.app_name);
		tempFileDirectory = context.getCacheDir();

		//TODO: Make sure external media is mounted
		publicFileDirectory = new File(Environment.getExternalStorageDirectory(), APP_DIRECTORY_NAME);
		if (!publicFileDirectory.exists()) {
			publicFileDirectory.mkdirs();
		}
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
