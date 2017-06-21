package com.bryndsey.songspark.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bryndsey.songspark.R;
import com.bryndsey.songspark.ui.menu.MainMenuFragment;
import com.metova.slim.Slim;
import com.metova.slim.annotation.Layout;

@Layout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

	private static final String MENU_FRAGMENT_TAG = "menu_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View layout = Slim.createLayout(this, this);
		if(layout != null) {
			this.setContentView(layout);
		}

		if (getSupportFragmentManager().findFragmentByTag(MENU_FRAGMENT_TAG) == null) {
			getSupportFragmentManager().beginTransaction()
					.add(new MainMenuFragment(), MENU_FRAGMENT_TAG)
					.commit();
		}
	}
}
