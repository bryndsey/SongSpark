package com.bryndsey.songspark.ui.menu.exportmidi;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
import eltos.simpledialogfragment.input.SimpleInputDialog;

@Layout(R.layout.empty)
@FragmentView(presenter = ExportMidiPresenter.class)
public class ExportMidiFragment extends BaseFragment implements ExportMidiView, SimpleInputDialog.OnDialogResultListener {

	private static final String SAVE_FILE_DIALOG_TAG = "saveFileDialog";

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
		Snackbar.make(getView(), "Error occurred. File not saved", Snackbar.LENGTH_SHORT)
		.show();
	}

	private void showSaveFileDialog() {
		SimpleInputDialog.build()
				.allowEmpty(false)
				.title("Save as:")
				.hint("File Name")
				.show(this, SAVE_FILE_DIALOG_TAG);
	}

	@Override
	public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
		if (dialogTag.equals(SAVE_FILE_DIALOG_TAG) && which == BUTTON_POSITIVE) {
			String fileName = extras.getString(SimpleInputDialog.TEXT);
			presenter.exportToFile(fileName);
			return true;
		}

		return false;
	}
}
