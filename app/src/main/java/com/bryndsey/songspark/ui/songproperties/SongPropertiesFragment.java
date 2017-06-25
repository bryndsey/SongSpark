package com.bryndsey.songspark.ui.songproperties;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.dagger.ComponentHolder;
import com.bryndsey.songspark.ui.base.BaseFragment;
import com.bryndsey.songspark.ui.songproperties.widget.SongPropertyWidget;
import com.metova.slim.annotation.Layout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import easymvp.annotation.FragmentView;
import easymvp.annotation.Presenter;

@Layout(R.layout.song_properties)
@FragmentView(presenter = SongPropertiesPresenter.class)
public class SongPropertiesFragment extends BaseFragment implements SongPropertiesView {

	@BindView(R.id.time_signature)
	TextView timeSignatureView;

	@BindView(R.id.tempo)
	TextView tempoView;

	@BindView(R.id.scale)
	TextView scaleView;

	@BindView(R.id.lead_instrument)
	TextView leadInstrumentView;

	@BindView(R.id.rhythm_instrument)
	SongPropertyWidget rhythmInstrumentChooser;

	@Inject
	@Presenter
	SongPropertiesPresenter presenter;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		ComponentHolder.getApplicationComponent().inject(this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void setTimeSignature(String timeSignature) {
		timeSignatureView.setText(timeSignature);
	}

	@Override
	public void setTempo(String tempo) {
		tempoView.setText(tempo);
	}

	@Override
	public void setScale(String scale) {
		scaleView.setText(scale);
	}

	@Override
	public void setLeadInstrument(String leadInstrument) {
		leadInstrumentView.setText(leadInstrument);
	}

	@Override
	public void setRhythmInstrumentList(List instrumentList) {
		rhythmInstrumentChooser.setPropertyItems(instrumentList);
	}

	@Override
	public void setInstrumentSelection(int positionInList) {
		rhythmInstrumentChooser.setPropertySelection(positionInList);
	}
}
