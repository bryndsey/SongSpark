package com.bryndsey.songspark.ui.songproperties;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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
import io.reactivex.functions.Consumer;

@Layout(R.layout.song_properties)
@FragmentView(presenter = SongPropertiesPresenter.class)
public class SongPropertiesFragment extends BaseFragment implements SongPropertiesView {

	@BindView(R.id.time_signature)
	TextView timeSignatureView;

	@BindView(R.id.tempo_old)
	TextView tempoView;

	@BindView(R.id.tempo)
	SongPropertyWidget tempoChooser;

	@BindView(R.id.scale)
	TextView scaleView;

	@BindView(R.id.lead_instrument)
	SongPropertyWidget leadInstrumentChooser;

	@BindView(R.id.rhythm_instrument)
	SongPropertyWidget rhythmInstrumentChooser;

	@Inject
	@Presenter
	SongPropertiesPresenter presenter;

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		tempoChooser.setSongPropertySelectedAction(new Consumer<Integer>() {
			@Override
			public void accept(Integer integer) throws Exception {
				presenter.updateTempo(integer);
			}
		});

		leadInstrumentChooser.setSongPropertySelectedAction(new Consumer<Integer>() {
			@Override
			public void accept(Integer integer) throws Exception {
				presenter.updateLeadInstrument(integer);
			}
		});

		rhythmInstrumentChooser.setSongPropertySelectedAction(new Consumer<Integer>() {
			@Override
			public void accept(Integer integer) throws Exception {
				presenter.updateRhythmInstrument(integer);
			}
		});
	}

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
	public void setTempoList(List tempoList) {
		tempoChooser.setPropertyItems(tempoList);
	}

	@Override
	public void setTempoSelection(int positionInList) {
		tempoChooser.setPropertySelection(positionInList);
	}

	@Override
	public void setScale(String scale) {
		scaleView.setText(scale);
	}

	@Override
	public void setLeadInstrumentList(List instrumentList) {
		leadInstrumentChooser.setPropertyItems(instrumentList);
	}

	@Override
	public void setLeadInstrumentSelection(int positionInList) {
		leadInstrumentChooser.setPropertySelection(positionInList);
	}

	@Override
	public void setRhythmInstrumentList(List instrumentList) {
		rhythmInstrumentChooser.setPropertyItems(instrumentList);
	}

	@Override
	public void setRhythmInstrumentSelection(int positionInList) {
		rhythmInstrumentChooser.setPropertySelection(positionInList);
	}
}
