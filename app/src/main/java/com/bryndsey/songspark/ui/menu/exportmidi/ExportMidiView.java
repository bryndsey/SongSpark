package com.bryndsey.songspark.ui.menu.exportmidi;

import java.io.File;

interface ExportMidiView {

	void shareFile(File file);

	void showFileSaveError();

	void showFileSaveErrorWithMessage(String message);
}
