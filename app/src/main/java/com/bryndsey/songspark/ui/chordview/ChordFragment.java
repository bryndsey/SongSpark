package com.bryndsey.songspark.ui.chordview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.dagger.ComponentHolder;
import com.bryndsey.songspark.ui.base.BaseFragment;
import com.metova.slim.annotation.Layout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import easymvp.annotation.FragmentView;
import easymvp.annotation.Presenter;

@Layout(R.layout.chord_view)
@FragmentView(presenter = ChordPresenter.class)
public class ChordFragment extends BaseFragment implements ChordView {

	@BindView(R.id.display)
	RecyclerView display;

	@Inject
	@Presenter
	ChordPresenter presenter;

	private ChordTileAdapter chordTileAdapter;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		ComponentHolder.getApplicationComponent().inject(this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
		display.setLayoutManager(layoutManager);
		chordTileAdapter = new ChordTileAdapter();
		display.setAdapter(chordTileAdapter);
	}

	@Override
	public void displayChords(List<ChordViewModel> chords) {
		chordTileAdapter.setChords(chords);

	}

}
