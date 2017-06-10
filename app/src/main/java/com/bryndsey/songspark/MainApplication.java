package com.bryndsey.songspark;

import android.app.Application;

import com.bryndsey.songspark.dagger.AndroidModule;
import com.bryndsey.songspark.dagger.ApplicationComponent;
import com.bryndsey.songspark.dagger.ComponentHolder;
import com.bryndsey.songspark.dagger.DaggerApplicationComponent;


public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
				.androidModule(new AndroidModule(this)).build();

		ComponentHolder.setApplicationComponent(applicationComponent);
	}
}
