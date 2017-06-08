package com.bryndsey.songspark.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bryndsey.songspark.R;

import easymvp.annotation.ActivityView;
import easymvp.annotation.Presenter;

@ActivityView(layout = R.layout.activity_main, presenter = MainPresenter.class)
public class MainActivity extends AppCompatActivity implements MainView {

	TextView infoView;

	@Presenter
	MainPresenter presenter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		infoView = (TextView)findViewById(R.id.info);
	}

	@Override
	public void displaySong(String song) {
		infoView.setText(song);
	}
}
