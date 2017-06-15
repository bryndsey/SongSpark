package com.bryndsey.songspark.ui.primarycontrols;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.dagger.ComponentHolder;
import com.bryndsey.songspark.ui.base.BaseFragment;
import com.metova.slim.annotation.Layout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import easymvp.annotation.FragmentView;
import easymvp.annotation.Presenter;

@Layout(R.layout.primary_controls)
@FragmentView(presenter = PrimaryControlsPresenter.class)
public class PrimaryControlsFragment extends BaseFragment implements PrimaryControlsView {

	@BindView(R.id.new_song_button)
	ImageView newSongButton;

	@BindView(R.id.play_pause_button)
	ImageView playPauseButton;

	@Inject
	@Presenter
	PrimaryControlsPresenter presenter;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		ComponentHolder.getApplicationComponent().inject(this);
		super.onActivityCreated(savedInstanceState);
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