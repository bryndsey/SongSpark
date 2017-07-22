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

	private int highlightPosition;

	private ChordTileClickListener tileClickListener;

	ChordTileAdapter(ChordTileClickListener tileClickListener) {
		this.tileClickListener = tileClickListener;
		chords = new ArrayList<>();
		highlightPosition = 0;
	}

	@Override
	public ChordTileViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = Slim.createLayout(viewGroup.getContext(), this, viewGroup);
		return new ChordTileViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ChordTileViewHolder chordTileViewHolder, int position) {
		ChordViewModel chord = chords.get(position);
		chordTileViewHolder.chordName.setText(chord.chordName);

		chordTileViewHolder.itemView.setSelected(shouldBeHighlighted(position));

		chordTileViewHolder.itemView.setOnClickListener(view ->
			tileClickListener.onChordTileClicked(position)
		);
	}

	private boolean shouldBeHighlighted(int position) {
		return position == highlightPosition;
	}

	@Override
	public int getItemCount() {
		return chords.size();
	}

	void setChords(List<ChordViewModel> chords) {
		this.chords = chords;
		notifyDataSetChanged();
	}

	void highlightTile(int tilePosition) {
		int oldHighlightPosition = highlightPosition;
		highlightPosition = tilePosition;

		notifyItemChanged(oldHighlightPosition);
		notifyItemChanged(highlightPosition);
	}

	interface ChordTileClickListener {
		void onChordTileClicked(int position);
	}
}
