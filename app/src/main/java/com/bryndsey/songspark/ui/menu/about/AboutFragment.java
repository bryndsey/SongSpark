package com.bryndsey.songspark.ui.menu.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.ui.base.BaseFragment;
import com.metova.slim.annotation.Layout;

@Layout(R.layout.empty)
public class AboutFragment extends BaseFragment {

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.about, menu);
	}
}
