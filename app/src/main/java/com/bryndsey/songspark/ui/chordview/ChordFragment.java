package com.bryndsey.songspark.ui.chordview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.dagger.ComponentHolder;
import com.bryndsey.songspark.ui.base.BaseFragment;
import com.metova.slim.annotation.Layout;

import javax.inject.Inject;

import butterknife.BindView;
import easymvp.annotation.FragmentView;
import easymvp.annotation.Presenter;

@Layout(R.layout.chord_view)
@FragmentView(presenter = ChordPresenter.class)
public class ChordFragment extends BaseFragment implements ChordView {

	@BindView(R.id.display)
	TextView display;

	@Inject
	@Presenter
	ChordPresenter presenter;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		ComponentHolder.getApplicationComponent().inject(this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void displayChords(String text) {
		display.setText(text);
	}

}
