package com.bryndsey.songbuilder.dagger;

import com.bryndsey.songbuilder.SongWriter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		SongWriterModule.class,
		}
)
public interface SongWriterComponent {

	void inject(SongWriter songWriter);
}
