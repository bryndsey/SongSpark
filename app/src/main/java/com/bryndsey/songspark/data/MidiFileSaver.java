package com.bryndsey.songspark.data;

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
	public MidiFileSaver(Context context) {
		this.context = context;

		APP_DIRECTORY_NAME = context.getResources().getString(R.string.app_name);
		tempFileDirectory = context.getCacheDir();

		//TODO: Make sure external media is mounted and that we have permissions
		publicFileDirectory = new File(Environment.getExternalStorageDirectory(), APP_DIRECTORY_NAME);
		if (!publicFileDirectory.exists()) {
			publicFileDirectory.mkdirs();
		}
	}

	private String getFileNameWithProperExternsion(String fileName) {
		String fileNameWithExtension = fileName;
		if (!fileNameWithExtension.endsWith(MIDI_FILE_EXTENSION)) {
			fileNameWithExtension += MIDI_FILE_EXTENSION;
		}

		return fileNameWithExtension;
	}

	private void writeMidiFile(MidiFile midiFile, File saveFile) {
		try {
			midiFile.writeToFile(saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void savePublicMidiFile(MidiFile midiFile, String fileName) {
		File saveFile = new File(publicFileDirectory, getFileNameWithProperExternsion(fileName));
		writeMidiFile(midiFile, saveFile);
	}

	public void saveTemporaryMidiFile(MidiFile midiFile, String fileName) {
		File saveFile = new File(tempFileDirectory, getFileNameWithProperExternsion(fileName));
		writeMidiFile(midiFile, saveFile);
	}
}
