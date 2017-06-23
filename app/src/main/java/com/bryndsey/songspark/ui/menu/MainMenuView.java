package com.bryndsey.songspark.ui.menu;

interface MainMenuView {

	void launchSaveFileSelector();

	void showFileSaveConfirmation(String fileName);

	void showFileSaveError();
}
