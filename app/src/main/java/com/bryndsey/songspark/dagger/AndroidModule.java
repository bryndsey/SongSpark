package com.bryndsey.songspark.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModule {
	private final Context context;

	public AndroidModule(Context context) {
		this.context = context;
	}

	@Provides
	@Singleton
	Context provideContext() {
		return context;
	}
}
