package com.bryndsey.songbuilder.dagger;

import com.bryndsey.songbuilder.ChordProgressionGenerator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SongWriterModule {

	@Provides
	@Singleton
	ChordProgressionGenerator provideChordProgressionGenerator() {
		return new ChordProgressionGenerator();
	}
}
