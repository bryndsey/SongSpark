package com.bryndsey.songspark.dagger;

import com.bryndsey.songspark.data.player.MidiPlayer;
import com.bryndsey.songspark.ui.chordview.ChordFragment;
import com.bryndsey.songspark.ui.menu.exportmidi.ExportMidiFragment;
import com.bryndsey.songspark.ui.primarycontrols.PrimaryControlsFragment;
import com.bryndsey.songspark.ui.songproperties.SongPropertiesFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
		AndroidModule.class
})
public interface ApplicationComponent {

	void inject(ChordFragment chordFragment);

	void inject(PrimaryControlsFragment primaryControlsFragment);

	void inject(SongPropertiesFragment songPropertiesFragment);

	void inject(ExportMidiFragment mainMenuFragment);

	void inject(MidiPlayer midiPlayer);

}
