package com.bryndsey.songspark.ui.menu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.dagger.ComponentHolder;
import com.bryndsey.songspark.ui.base.BaseFragment;
import com.metova.slim.annotation.Layout;

import javax.inject.Inject;

import easymvp.annotation.FragmentView;
import easymvp.annotation.Presenter;
import eltos.simpledialogfragment.input.SimpleInputDialog;

@Layout(R.layout.empty)
@FragmentView(presenter = MainMenuPresenter.class)
public class MainMenuFragment extends BaseFragment implements MainMenuView, SimpleInputDialog.OnDialogResultListener {

	private static final String SAVE_FILE_DIALOG_TAG = "saveFileDialog";

	@Inject
	@Presenter
	MainMenuPresenter presenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ComponentHolder.getApplicationComponent().inject(this);

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.export_midi) {
			presenter.exportMidiSong();
		}

		return true;
	}

	@Override
	public void launchSaveFileSelector() {
			showSaveFileDialog();
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
