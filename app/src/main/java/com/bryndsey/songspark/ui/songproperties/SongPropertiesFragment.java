package com.bryndsey.songspark.ui.songproperties;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

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

	@BindView(R.id.tempo)
	SongPropertyWidget tempoChooser;

	@BindView(R.id.scale_root)
	SongPropertyWidget scaleRootChooser;

	@BindView(R.id.scale_type)
	SongPropertyWidget scaleTypeChooser;

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

		scaleRootChooser.setSongPropertySelectedAction(new Consumer<Integer>() {
			@Override
			public void accept(Integer integer) throws Exception {
				presenter.updateScaleRoot(integer);
			}
		});

		scaleTypeChooser.setSongPropertySelectedAction(new Consumer<Integer>() {
			@Override
			public void accept(Integer integer) throws Exception {
				presenter.updateScaleType(integer);
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
	public void setTempoList(List tempoList) {
		tempoChooser.setPropertyItems(tempoList);
	}

	@Override
	public void setTempoSelection(int positionInList) {
		tempoChooser.setPropertySelection(positionInList);
	}

	@Override
	public void setScaleRootList(List scaleRootList) {
		scaleRootChooser.setPropertyItems(scaleRootList);
	}

	@Override
	public void setScaleRootSelection(int positionInList) {
		scaleRootChooser.setPropertySelection(positionInList);
	}

	@Override
	public void setScaleTypeList(List scaleTypeList) {
		scaleTypeChooser.setPropertyItems(scaleTypeList);
	}

	@Override
	public void setScaleTypeSelection(int positionInList) {
		scaleTypeChooser.setPropertySelection(positionInList);
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

//	@OnClick(R.id.scale)
//	void onScaleClick() {
////		SimpleFormDialog.build()
////				.title("Select a key")
////				.fields(
////						Spinner.plain("SPINNER1")
////								.items("A", "B", "C", "D")
////								.preset(0)
////								.required(),
////						Spinner.plain("SPINNER2")
////								.items("Major", "Minor", "Mixolydian")
////								.preset(0)
////								.required()
////				)
////				.show(this, "DIALOG_TEST");
//
//		MaterialDialog scaleDialog = new MaterialDialog.Builder(getContext())
//				.title("Scale")
//				.customView(R.layout.dialog_scale_chooser, true)
//				.positiveText("OK")
////				.onPositive(new MaterialDialog.SingleButtonCallback() {
////					@Override
////					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
////						SongPropertyWidget rootSpinner = (SongPropertyWidget)dialog.getCustomView().findViewById(R.id.scale_root);
////						Log.d("BRYAN", "Root spinner value: " + rootSpinner.);
////					}
////				})
//				.build();
//
//		SongPropertyWidget rootWidget = (SongPropertyWidget)scaleDialog.getCustomView().findViewById(R.id.scale_root);
//		rootWidget.setPropertyItems(Arrays.asList(MusicStructure.PITCHES));
//
//		SongPropertyWidget typeWidget = (SongPropertyWidget)scaleDialog.getCustomView().findViewById(R.id.scale_type);
//		typeWidget.setPropertyItems(Arrays.asList(MusicStructure.ScaleType.values()));
////		Spinner rootSpinner = (Spinner)scaleDialog.getCustomView().findViewById(R.id.scale_root);
////		ArrayAdapter adapter1 = new ArrayAdapter(getContext(), R.layout.song_property_spinner_item, MusicStructure.PITCHES);
////		adapter1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
////		rootSpinner.setAdapter(adapter1);
////
////		Spinner typeSpinner = (Spinner)scaleDialog.getCustomView().findViewById(R.id.scale_type);
////		ArrayAdapter adapter2 = new ArrayAdapter(getContext(), R.layout.song_property_spinner_item, MusicStructure.ScaleType.values());
////		adapter2.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
////		typeSpinner.setAdapter(adapter2);
//
//		scaleDialog.show();
//	}
}
