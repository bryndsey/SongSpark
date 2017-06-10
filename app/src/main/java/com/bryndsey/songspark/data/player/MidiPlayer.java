package com.bryndsey.songspark.data.player;

import android.content.Context;
import android.media.MediaPlayer;

import com.bryndsey.songspark.dagger.ComponentHolder;
import com.pdrogfer.mididroid.MidiFile;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public class MidiPlayer {

	private static final String TEMP_MIDI_FILE_PATH = "/temp/play.mid";

	private Context context;

	private final String filePath;

	private MediaPlayer mediaPlayer;

	@Inject
	public MidiPlayer(Context context) {
		ComponentHolder.getApplicationComponent().inject(this);

		this.context = context;

		mediaPlayer = new MediaPlayer();

		filePath = context.getFilesDir() + TEMP_MIDI_FILE_PATH;
	}

	public void preparePlayer(MidiFile midiFile) throws MidiPlayerPrepareException {
		File playableMidiFile = new File(filePath);
		try {
			midiFile.writeToFile(playableMidiFile);

			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();
		} catch (IOException e) {
			throw new MidiPlayerPrepareException();
		}
	}

	public void startPlaying() {
		mediaPlayer.start();
	}

	public void stopPlaying() {
		mediaPlayer.stop();
	}
}
