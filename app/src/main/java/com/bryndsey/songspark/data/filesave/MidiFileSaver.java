package com.bryndsey.songspark.data.filesave;

import android.content.Context;

import com.bryndsey.songspark.R;
import com.pdrogfer.mididroid.MidiFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MidiFileSaver {

	private static final String MIDI_FILE_EXTENSION = ".mid";
	private static String APP_NAME;

	private File tempFileDirectory;
	private File shareableFileDirectory;

	@Inject
	MidiFileSaver(Context context) {
		APP_NAME = context.getString(R.string.app_name);

		tempFileDirectory = context.getCacheDir();

		// TODO: Delete the share subdirectory to clean up cache

		shareableFileDirectory = new File(tempFileDirectory, context.getString(R.string.share_directory));
		if (!shareableFileDirectory.exists()) {
			shareableFileDirectory.mkdirs();
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

	public File saveShareableMidiFile(MidiFile midiFile) throws MidiFileSaveException {
		return saveShareableMidiFile(midiFile, generateTimeStampFilename());
	}

	public File saveShareableMidiFile(MidiFile midiFile, String fileName) throws MidiFileSaveException {
		File saveFile = new File(shareableFileDirectory, getFileNameWithProperExtension(fileName));
		writeMidiFile(midiFile, saveFile);
		return saveFile;
	}

	public File saveTemporaryMidiFile(MidiFile midiFile, String fileName) throws MidiFileSaveException {
		File saveFile = new File(tempFileDirectory, getFileNameWithProperExtension(fileName));
		writeMidiFile(midiFile, saveFile);
		return saveFile;
	}

	private String generateTimeStampFilename() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss");
		String timestamp = simpleDateFormat.format(new Date());
		return APP_NAME + "_" + timestamp;
	}
}
