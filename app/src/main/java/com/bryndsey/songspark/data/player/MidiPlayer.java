package com.bryndsey.songspark.data.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

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
public class MidiPlayer implements MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

	private static final String TAG = MidiPlayer.class.getSimpleName();
	private static final String TEMP_MIDI_FILE_NAME = "play.mid";
	private static final float DUCKED_VOLUME = 0.35f;
	private static final float FULL_VOLUME = 1;

	private final MidiFileSaver midiFileSaver;

	private AudioManager audioManager;
	private MediaPlayer mediaPlayer;

	private boolean isPrepared;

	private PlaybackStateListener playbackStateListener;

	@Inject
	MidiPlayer(MidiSongFactory midiSongFactory, MidiFileSaver midiFileSaver, Context context) {
		this.midiFileSaver = midiFileSaver;

		midiSongFactory.latestSong()
				.subscribe(new Consumer<MidiSong>() {
					@Override
					public void accept(MidiSong midiSong) throws Exception {
						initializePlayer(midiSong.midiFile);
					}
				});

		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
	}

	private void initializePlayer(MidiFile midiFile) {
		resetPlayerState();

		File tempMidiFile = null;
		try {
			tempMidiFile = midiFileSaver.saveTemporaryMidiFile(midiFile, TEMP_MIDI_FILE_NAME);
		} catch (MidiFileSaveException e) {
			setPlayerNotReady();
			return;
		}

		try {
			mediaPlayer.setDataSource(tempMidiFile.getPath());
		} catch (IOException e) {
			setPlayerNotReady();
			return;
		}

		preparePlayer();
	}

	private void resetPlayerState() {
		if (mediaPlayer.isPlaying()) {
			stopMediaPlayer();
		}
		if (isPrepared) {
			mediaPlayer.reset();
		}

		isPrepared = false;
	}

	private void setPlayerNotReady() {
		Log.d(TAG, "Player failed to enter ready state");
		isPrepared = false;

		if (playbackStateListener != null) {
			playbackStateListener.onPlaybackNotReady();
		}
	}

	private void preparePlayer() {
		try {
			mediaPlayer.prepare();
		} catch (IOException e) {
			setPlayerNotReady();
			return;
		}

		isPrepared = true;

		if (playbackStateListener != null) {
			playbackStateListener.onPlaybackReady();
		}
	}

	public void startPlaying() {
		if (isPrepared) {
			int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
			if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
				startMediaPlayer();
			} else {
				notifyPlaybackComplete();
			}
		}
	}

	private void startMediaPlayer() {
		mediaPlayer.setVolume(FULL_VOLUME, FULL_VOLUME);
		mediaPlayer.start();

		if (playbackStateListener != null) {
			playbackStateListener.onPlaybackStarted();
		}
	}

	public void pause() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			audioManager.abandonAudioFocus(this);
		}
	}

	public void stop() {
		stopAndPrepareAgain();
	}

	private void stopAndPrepareAgain() {
		stopMediaPlayer();
		preparePlayer();
	}

	private void stopMediaPlayer() {
		mediaPlayer.stop();
		notifyPlaybackComplete();
		audioManager.abandonAudioFocus(this);
	}

	public void setPlaybackStateListener(PlaybackStateListener playbackStateListener) {
		this.playbackStateListener = playbackStateListener;
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		audioManager.abandonAudioFocus(this);

		notifyPlaybackComplete();
	}

	private void notifyPlaybackComplete() {
		if (playbackStateListener != null) {
			playbackStateListener.onPlaybackComplete();
		}
	}

	@Override
	public void onAudioFocusChange(int focusChange) {
		if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
			startMediaPlayer();
		} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
			stopMediaPlayer();
		} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
			mediaPlayer.pause();
		} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
			mediaPlayer.setVolume(DUCKED_VOLUME, DUCKED_VOLUME);
		}
	}

	public interface PlaybackStateListener {
		void onPlaybackReady();

		void onPlaybackNotReady();

		void onPlaybackStarted();

		void onPlaybackComplete();
	}
}
