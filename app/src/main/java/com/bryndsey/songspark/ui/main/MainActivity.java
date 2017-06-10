package com.bryndsey.songspark.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.dagger.ComponentHolder;

import javax.inject.Inject;

import easymvp.annotation.ActivityView;
import easymvp.annotation.Presenter;

@ActivityView(layout = R.layout.activity_main, presenter = MainPresenter.class)
public class MainActivity extends AppCompatActivity implements MainView {

	TextView infoView;

	Button newSongButton;

	Button playPauseButton;

	@Inject
	@Presenter
	MainPresenter presenter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ComponentHolder.getApplicationComponent().inject(this);
		super.onCreate(savedInstanceState);

		infoView = (TextView)findViewById(R.id.info);
		newSongButton = (Button)findViewById(R.id.new_song_button);
		playPauseButton = (Button)findViewById(R.id.play_pause_button);

		newSongButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.generateNewSong();
			}
		});
		playPauseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.playSong();
			}
		});
	}

	@Override
	public void displaySong(String song) {
		infoView.setText(song);
	}
}
