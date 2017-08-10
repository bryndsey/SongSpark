package com.bryndsey.songbuilder.songstructure;

import java.util.ArrayList;

public class ChordProgression {
	
	public ArrayList<Pattern> patterns;
	
	public ChordProgression()
	{
		patterns = new ArrayList<>();
	}
	
	public ChordProgression(ChordProgression inst)
	{
		patterns = new ArrayList<>(inst.patterns);
	}
	
	public ArrayList<Integer> getChords()
	{
		ArrayList<Integer> chords = new ArrayList<>();
		for (Pattern pattern: patterns)
		{
			chords.addAll(pattern.chords);
		}
		return chords;
	}
	
	public ArrayList<ArrayList<Note>> getNotes()
	{
		ArrayList<ArrayList<Note>> notes = new ArrayList<>();
		for (Pattern pattern: patterns)
		{
			notes.addAll(pattern.notes);
		}
		return notes;
	}
	
	public ChordProgression add(ChordProgression addend)
	{
		ChordProgression result = new ChordProgression(this);
		result.patterns.addAll(addend.patterns);
		return result;
	}

}
