package com.bryndsey.songspark.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.dagger.ComponentHolder;

import javax.inject.Inject;

import easymvp.annotation.ActivityView;
import easymvp.annotation.Presenter;

@ActivityView(layout = R.layout.activity_main, presenter = MainPresenter.class)
public class MainActivity extends AppCompatActivity implements MainView {

	TextView infoView;

	ImageView newSongButton;

	ImageView playPauseButton;

	@Inject
	@Presenter
	MainPresenter presenter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ComponentHolder.getApplicationComponent().inject(this);
		super.onCreate(savedInstanceState);

		infoView = (TextView) findViewById(R.id.info);
		newSongButton = (ImageView) findViewById(R.id.new_song_button);
		playPauseButton = (ImageView) findViewById(R.id.play_pause_button);

		newSongButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.generateNewSong();
			}
		});
		playPauseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.playPauseSong();
			}
		});
	}

	@Override
	public void displaySong(String song) {
		infoView.setText(song);
	}

	@Override
	public void displayPlayingState() {
		playPauseButton.setImageResource(R.drawable.pause);
		playPauseButton.setEnabled(true);
	}

	@Override
	public void displayPausedState() {
		playPauseButton.setImageResource(R.drawable.play_arrow);
		playPauseButton.setEnabled(true);
	}

	@Override
	public void disablePlayback() {
		playPauseButton.setEnabled(false);
	}
}
