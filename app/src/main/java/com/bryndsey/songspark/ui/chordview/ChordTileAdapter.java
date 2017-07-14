package com.bryndsey.songspark.ui.chordview;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bryndsey.songspark.R;
import com.metova.slim.Slim;
import com.metova.slim.annotation.Layout;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.chord_tile)
public class ChordTileAdapter extends RecyclerView.Adapter<ChordTileViewHolder> {

	private List<ChordViewModel> chords;

	ChordTileAdapter() {
		chords = new ArrayList<>();
	}

	@Override
	public ChordTileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = Slim.createLayout(viewGroup.getContext(), this, viewGroup);
		return new ChordTileViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ChordTileViewHolder chordTileViewHolder, int i) {
		ChordViewModel chord = chords.get(i);
		chordTileViewHolder.chordName.setText(chord.chordName);
	}

	@Override
	public int getItemCount() {
		return chords.size();
	}

	void setChords(List<ChordViewModel> chords) {
		this.chords = chords;
		notifyDataSetChanged();
	}
}
