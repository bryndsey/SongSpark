package com.bryndsey.songspark.data.player;

import android.content.Context;
import android.media.MediaPlayer;

import com.bryndsey.songspark.dagger.ComponentHolder;
import com.pdrogfer.mididroid.MidiFile;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public class MidiPlayer {

	private static final String TEMP_MIDI_FILE_NAME = "play.mid";

	private File tempMidiFile;

	private MediaPlayer mediaPlayer;

	private boolean isReadyToPlay;

	@Inject
	public MidiPlayer(Context context) {
		ComponentHolder.getApplicationComponent().inject(this);

		mediaPlayer = new MediaPlayer();

		tempMidiFile = new File(context.getCacheDir(), TEMP_MIDI_FILE_NAME);

		if(!tempMidiFile.exists())
		{
			try {
				tempMidiFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void preparePlayer(MidiFile midiFile) throws MidiPlayerPrepareException {

		resetPlayerState();

		try {
			midiFile.writeToFile(tempMidiFile);

			mediaPlayer.setDataSource(tempMidiFile.getPath());
			mediaPlayer.prepare();

			isReadyToPlay = true;
		} catch (IOException e) {
			e.printStackTrace();
			isReadyToPlay = false;
			throw new MidiPlayerPrepareException();
		}
	}

	private void resetPlayerState() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		if (isReadyToPlay) {
			mediaPlayer.reset();
		}

		isReadyToPlay = false;
	}

	public void startPlaying() {
		if (isReadyToPlay) {
			mediaPlayer.start();
		}
	}

	public void stopPlaying() {
		mediaPlayer.stop();
	}
}
