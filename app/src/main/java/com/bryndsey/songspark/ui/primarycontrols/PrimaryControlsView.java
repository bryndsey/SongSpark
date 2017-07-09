package com.bryndsey.songspark.ui.primarycontrols;

interface PrimaryControlsView {
	void displayPlayingState();

	void displayPausedState();

	void transitionToPlayingState();

	void transitionToPausedState();

	void disablePlayback();

	void enablePlayback();
}
