package com.bryndsey.songspark.ui.songproperties;

public interface SongPropertiesView {

	void setTimeSignature(String timeSignature);

	void setTempo(int tempo);

	void setScale(String scale);

	void setLeadInstrument(String leadInstrument);

	void setRhythmInstrument(String rhythmInstrument);
}
