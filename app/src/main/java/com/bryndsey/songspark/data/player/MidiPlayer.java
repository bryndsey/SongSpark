package com.bryndsey.songspark.data.player;

import android.media.MediaPlayer;

import com.bryndsey.songspark.data.MidiSongFactory;
import com.bryndsey.songspark.data.filesave.MidiFileSaveException;
import com.bryndsey.songspark.data.filesave.MidiFileSaver;
import com.bryndsey.songspark.data.model.MidiSong;
import com.pdrogfer.mididroid.MidiFile;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;

@Singleton
public class MidiPlayer implements MediaPlayer.OnCompletionListener {

	private static final String TEMP_MIDI_FILE_NAME = "play.mid";

	private final MidiFileSaver midiFileSaver;

	private MediaPlayer mediaPlayer;

	private boolean isPrepared;

	private PlaybackStateListener playbackStateListener;

	@Inject
	MidiPlayer(MidiSongFactory midiSongFactory, MidiFileSaver midiFileSaver) {
		this.midiFileSaver = midiFileSaver;

		midiSongFactory.latestSong()
				.subscribe(new Consumer<MidiSong>() {
					@Override
					public void accept(MidiSong midiSong) throws Exception {
						preparePlayer(midiSong.midiFile);
					}
				});

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
	}

	private void preparePlayer(MidiFile midiFile) {

		resetPlayerState();

		try {
			File tempMidiFile = midiFileSaver.saveTemporaryMidiFile(midiFile, TEMP_MIDI_FILE_NAME);

			mediaPlayer.setDataSource(tempMidiFile.getPath());
			mediaPlayer.prepare();

			isPrepared = true;

			if (playbackStateListener != null) {
				playbackStateListener.onPlaybackReady();
			}
		} catch (IOException | MidiFileSaveException e) {
			e.printStackTrace();
			isPrepared = false;

			if (playbackStateListener != null) {
				playbackStateListener.onPlaybackNotReady();
			}
		}
	}

	private void resetPlayerState() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			onCompletion(mediaPlayer);
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

	public void setPlaybackStateListener(PlaybackStateListener playbackStateListener) {
		this.playbackStateListener = playbackStateListener;
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		if (playbackStateListener != null) {
			playbackStateListener.onPlaybackComplete();
		}
	}

	public interface PlaybackStateListener {
		void onPlaybackReady();

		void onPlaybackNotReady();

		void onPlaybackComplete();
	}
}
