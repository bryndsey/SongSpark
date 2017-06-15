package com.bryndsey.songspark.ui.chordview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bryndsey.songspark.R;

class ChordTileViewHolder extends RecyclerView.ViewHolder {

	TextView chordName;

	ChordTileViewHolder(View itemView) {
		super(itemView);
		chordName = (TextView) itemView.findViewById(R.id.chord_name);
	}
}
