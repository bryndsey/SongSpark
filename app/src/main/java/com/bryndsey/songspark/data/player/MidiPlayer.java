package com.bryndsey.songspark.data.player;

import android.content.Context;
import android.media.MediaPlayer;

import com.bryndsey.songspark.dagger.ComponentHolder;
import com.pdrogfer.mididroid.MidiFile;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public class MidiPlayer implements MediaPlayer.OnCompletionListener {

	private static final String TEMP_MIDI_FILE_NAME = "play.mid";

	private File tempMidiFile;

	private MediaPlayer mediaPlayer;

	private boolean isPrepared;

	private PlaybackCompleteListener playbackCompleteListener;

	@Inject
	public MidiPlayer(Context context) {
		ComponentHolder.getApplicationComponent().inject(this);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);

		tempMidiFile = new File(context.getCacheDir(), TEMP_MIDI_FILE_NAME);

		if (!tempMidiFile.exists()) {
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

			isPrepared = true;
		} catch (IOException e) {
			e.printStackTrace();
			isPrepared = false;
			throw new MidiPlayerPrepareException();
		}
	}

	private void resetPlayerState() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		if (isPrepared) {
			mediaPlayer.reset();
		}

		isPrepared = false;
	}

	public void startPlaying() {

		//TODO: Implement audio focus

		if (isPrepared) {
			mediaPlayer.start();
		}

		//TODO: Show something to the user if not prepared?
	}

	public void pause() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	public void stopPlaying() {
		mediaPlayer.stop();
	}

	public void setPlaybackCompleteListener(PlaybackCompleteListener playbackCompleteListener) {
		this.playbackCompleteListener = playbackCompleteListener;
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		if (playbackCompleteListener != null) {
			playbackCompleteListener.onPlaybackComplete();
		}
	}

	public interface PlaybackCompleteListener {
		void onPlaybackComplete();
	}
}
