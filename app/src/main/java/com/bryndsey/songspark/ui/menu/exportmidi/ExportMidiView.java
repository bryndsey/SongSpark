package com.bryndsey.songspark.ui.menu.exportmidi;

interface ExportMidiView {

	void launchSaveFileSelector();

	void showFileSaveConfirmation(String fileName);

	void showFileSaveError();
}
