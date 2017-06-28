package com.bryndsey.songspark.ui.songproperties;

import java.util.List;

interface SongPropertiesView {

	void setTempoList(List tempoList);

	void setTempoSelection(int positionInList);

	void setScaleRootList(List scaleRootList);

	void setScaleRootSelection(int positionInList);

	void setScaleTypeList(List scaleTypeList);

	void setScaleTypeSelection(int positionInList);

	void setLeadInstrumentList(List instrumentList);

	void setLeadInstrumentSelection(int positionInList);

	void setRhythmInstrumentList(List instrumentList);

	void setRhythmInstrumentSelection(int positionInList);
}
