package com.bryndsey.songspark.ui.songproperties;

import java.util.List;

interface SongPropertiesView {

	void setTimeSignature(String timeSignature);

	void setTempo(String tempo);

	void setScale(String scale);

	void setLeadInstrument(String leadInstrument);

	void setRhythmInstrumentList(List instrumentList);

	void setInstrumentSelection(int positionInList);
}
