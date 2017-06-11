package com.bryndsey.songspark.dagger;

import com.bryndsey.songspark.data.player.MidiPlayer;
import com.bryndsey.songspark.ui.chordview.ChordFragment;
import com.bryndsey.songspark.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		AndroidModule.class
})
public interface ApplicationComponent {

	void inject(MainActivity mainActivity);

	void inject(ChordFragment chordFragment);

	void inject(MidiPlayer midiPlayer);
}
