package com.bryndsey.songspark.ui.menu.exportmidi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.dagger.ComponentHolder;
import com.bryndsey.songspark.ui.base.BaseFragment;
import com.metova.slim.annotation.Layout;

import java.io.File;

import javax.inject.Inject;

import easymvp.annotation.FragmentView;
import easymvp.annotation.Presenter;

@Layout(R.layout.empty)
@FragmentView(presenter = ExportMidiPresenter.class)
public class ExportMidiFragment extends BaseFragment implements ExportMidiView {

	@Inject
	@Presenter
	ExportMidiPresenter presenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ComponentHolder.getApplicationComponent().inject(this);

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.export_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.export_midi) {
			presenter.exportToShareableFile();
			return true;
		}

		return false;
	}

	@Override
	public void shareFile(File file) {
		Uri uri = FileProvider.getUriForFile(getContext(), getString(R.string.file_provider_authority), file);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.setType("audio/midi");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		startActivity(Intent.createChooser(intent, getResources().getString(R.string.export_midi_file)));
	}

	@Override
	public void showFileSaveError() {
		showFileSaveErrorWithMessage("Error occurred. File not saved");
	}

	@Override
	public void showFileSaveErrorWithMessage(String message) {
		Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT)
				.show();
	}
}
