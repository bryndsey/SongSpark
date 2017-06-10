package com.bryndsey.songspark.dagger;

import com.bryndsey.songspark.data.player.MidiPlayer;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		AndroidModule.class
})
public interface ApplicationComponent {
	
	void inject(MidiPlayer midiPlayer);
}
