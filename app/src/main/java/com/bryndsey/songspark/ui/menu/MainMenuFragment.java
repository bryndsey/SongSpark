package com.bryndsey.songspark.ui.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.ui.base.BaseFragment;

import javax.inject.Inject;

import easymvp.annotation.FragmentView;
import easymvp.annotation.Presenter;

@FragmentView(presenter = MainMenuPresenter.class)
public class MainMenuFragment extends BaseFragment {

	@Inject
	@Presenter
	MainMenuPresenter presenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.main_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.export_midi) {
			presenter.exportMidiSong();
		}

		return true;
	}
}
