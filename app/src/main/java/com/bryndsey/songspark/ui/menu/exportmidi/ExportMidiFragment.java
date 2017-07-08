package com.bryndsey.songspark.ui.menu.exportmidi;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bryndsey.songspark.R;
import com.bryndsey.songspark.dagger.ComponentHolder;
import com.bryndsey.songspark.ui.base.BaseFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.metova.slim.annotation.Layout;

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
			presenter.exportMidiSong();
			return true;
		}

		return false;
	}

	@Override
	public void launchSaveFileSelector() {
		Dexter.withActivity(getActivity())
				.withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.withListener(new BasePermissionListener() {
					@Override
					public void onPermissionGranted(PermissionGrantedResponse response) {
						showSaveFileDialog();
					}
				})
				.onSameThread()
				.check();
	}

	@Override
	public void showFileSaveConfirmation(String fileName) {
		Snackbar.make(getView(), "File \"" + fileName + "\" saved.", Snackbar.LENGTH_SHORT)
				.show();
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

	private void showSaveFileDialog() {

		new MaterialDialog.Builder(getContext())
				.title("Save as:")
				.input("File Name",
						"",
						false,
						new MaterialDialog.InputCallback() {
							@Override
							public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
								presenter.exportToFile(input.toString());
							}
						})
				.show();
	}
}
