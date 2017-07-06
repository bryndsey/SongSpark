package com.bryndsey.songspark.ui.menu.about;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bryndsey.songspark.R;
import com.bryndsey.songspark.ui.base.BaseFragment;
import com.metova.slim.annotation.Layout;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

@Layout(R.layout.empty)
public class AboutFragment extends BaseFragment {

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.about, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.about) {
			new MaterialDialog.Builder(getContext())
					.title(R.string.about)
					.customView(R.layout.about_page, true)
					.positiveText("OK")
					.neutralText("View Licenses")
					.onNeutral(new MaterialDialog.SingleButtonCallback() {
						@Override
						public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
							final Notices notices = new Notices();
							notices.addNotice(new Notice("Apache License 2.0", "http://www.apache.org/licenses/", "", new ApacheSoftwareLicense20()));
							notices.addNotice(new Notice("MIT License", "https://opensource.org/licenses/MIT", "", new MITLicense()));

							new LicensesDialog.Builder(getContext())
									.setTitle("Open Source Licenses")
									.setNotices(notices)
									.setShowFullLicenseText(true)
									.build()
									.show();
						}
					})
					.show();

			return true;
		}

		return false;
	}
}
