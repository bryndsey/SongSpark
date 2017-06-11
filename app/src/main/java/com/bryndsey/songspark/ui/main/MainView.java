package com.bryndsey.songspark.ui.main;

interface MainView {
	void displaySong(String song);

	void displayPlayingState();

	void displayPausedState();

	void disablePlayback();
}
