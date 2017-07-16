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

import io.reactivex.android.schedulers.AndroidSchedulers;

@Singleton
public class MidiPlayer implements MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

	private static final String TAG = MidiPlayer.class.getSimpleName();
	private static final String TEMP_MIDI_FILE_NAME = "play.mid";
	private static final float DUCKED_VOLUME = 0.35f;
	private static final float FULL_VOLUME = 1;

	private final MidiFileSaver midiFileSaver;

	private AudioManager audioManager;
	private MediaPlayer mediaPlayer;

	private File currentSongFile;

	private boolean isPrepared;

	private PlaybackStateListener playbackStateListener;

	@Inject
	MidiPlayer(MidiSongFactory midiSongFactory, MidiFileSaver midiFileSaver, Context context) {
		this.midiFileSaver = midiFileSaver;

		midiSongFactory.latestSong()
				// TODO: If I make this class more reactive, then I probably don't need this line
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(midiSong -> {
					saveSongForPlayback(midiSong);
					initializePlayer();
				});

		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
	}

	private void saveSongForPlayback(MidiSong song) {
		MidiFile midiFile = song.midiFile;
		currentSongFile = null;
		try {
			currentSongFile = midiFileSaver.saveTemporaryMidiFile(midiFile, TEMP_MIDI_FILE_NAME);
		} catch (MidiFileSaveException e) {
			setPlayerNotReady();
		}
	}

	private void initializePlayer() {
		resetPlayerState();

		if (currentSongFile != null) {
			try {
				mediaPlayer.setDataSource(currentSongFile.getPath());
			} catch (IOException e) {
				setPlayerNotReady();
				return;
			}

			preparePlayer();
		}
	}

	private void resetPlayerState() {
		if (mediaPlayer.isPlaying()) {
			stopMediaPlayer();
		}
		if (isPrepared) {
			isPrepared = false;
			mediaPlayer.reset();
		}
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
		initializePlayer();
	}

	private void stopMediaPlayer() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			finishPlayback();
		}
	}

	public void setPlaybackStateListener(PlaybackStateListener playbackStateListener) {
		this.playbackStateListener = playbackStateListener;
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		finishPlayback();
		initializePlayer();
	}

	private void finishPlayback() {
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
			stopAndPrepareAgain();
		} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
			mediaPlayer.pause();
		} else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
			mediaPlayer.setVolume(DUCKED_VOLUME, DUCKED_VOLUME);
		}
	}

	public float getPlayerProgress() {
		if (mediaPlayer == null || !isPrepared || mediaPlayer.getDuration() <= 0) {
			return 0;
		}

		return (float) mediaPlayer.getCurrentPosition() / (float) mediaPlayer.getDuration();
	}

	public interface PlaybackStateListener {
		void onPlaybackReady();

		void onPlaybackNotReady();

		void onPlaybackStarted();

		void onPlaybackComplete();
	}
}
