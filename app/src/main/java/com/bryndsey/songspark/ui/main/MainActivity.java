package com.bryndsey.songspark.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.dagger.ComponentHolder;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import easymvp.annotation.ActivityView;
import easymvp.annotation.Presenter;

@ActivityView(layout = R.layout.activity_main, presenter = MainPresenter.class)
public class MainActivity extends AppCompatActivity implements MainView {

	@BindView(R.id.new_song_button)
	ImageView newSongButton;

	@BindView(R.id.play_pause_button)
	ImageView playPauseButton;

	@Inject
	@Presenter
	MainPresenter presenter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ComponentHolder.getApplicationComponent().inject(this);
		super.onCreate(savedInstanceState);

		ButterKnife.bind(this);
	}

	@Override
	public void displayPlayingState() {
		playPauseButton.setImageResource(R.drawable.pause_icon);
		playPauseButton.setEnabled(true);
	}

	@Override
	public void displayPausedState() {
		playPauseButton.setImageResource(R.drawable.play_icon);
		playPauseButton.setEnabled(true);
	}

	@Override
	public void disablePlayback() {
		playPauseButton.setEnabled(false);
	}

	@OnClick(R.id.play_pause_button)
	void onPlayPause() {
		presenter.playPauseSong();
	}

	@OnClick(R.id.new_song_button)
	void onGenerateNewSong() {
		presenter.generateNewSong();
	}
}
