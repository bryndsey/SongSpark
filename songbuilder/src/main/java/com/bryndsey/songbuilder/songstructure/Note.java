package com.bryndsey.songbuilder.songstructure;

public class Note {

	public int pitch;
	public int numBeats;

	public float startBeatInQuarterNotes;
	public float lengthInQuarterNotes;
	public int velocity;
	
	public Note()
	{
		pitch = -1;
		numBeats = 1;
	}

	@Deprecated
	public Note(Note inst)
	{
		this.pitch = inst.pitch;
		this.numBeats = inst.numBeats;
	}

	@Deprecated
	public Note(int pitch, int numBeats)
	{
		this.pitch = pitch;
		this.numBeats = numBeats;
	}

	// TODO: Add velocity
	public Note(int pitch, float startBeatInQuarterNotes, float lengthInQuarterNotes) {
		this.pitch = pitch;
		this.startBeatInQuarterNotes = startBeatInQuarterNotes;
		this.lengthInQuarterNotes = lengthInQuarterNotes;
	}
	
	@Override
	public String toString()
	{
		return pitch + "x" + numBeats;
	}
}
