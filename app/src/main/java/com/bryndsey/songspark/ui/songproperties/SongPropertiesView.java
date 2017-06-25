package com.bryndsey.songspark.ui.songproperties;

import java.util.List;

interface SongPropertiesView {

	void setTimeSignature(String timeSignature);

	void setTempo(String tempo);

	void setTempoList(List tempoList);

	void setTempoSelection(int positionInList);

	void setScale(String scale);

	void setLeadInstrumentList(List instrumentList);

	void setLeadInstrumentSelection(int positionInList);

	void setRhythmInstrumentList(List instrumentList);

	void setRhythmInstrumentSelection(int positionInList);
}
