package com.bryndsey.songbuilder.songstructure;

public class Note {

	public int pitch;

	public float startBeatInQuarterNotes;
	public float lengthInQuarterNotes;
	public int velocity;

	// TODO: Add velocity
	public Note(int pitch, float startBeatInQuarterNotes, float lengthInQuarterNotes) {
		this.pitch = pitch;
		this.startBeatInQuarterNotes = startBeatInQuarterNotes;
		this.lengthInQuarterNotes = lengthInQuarterNotes;
	}
}
