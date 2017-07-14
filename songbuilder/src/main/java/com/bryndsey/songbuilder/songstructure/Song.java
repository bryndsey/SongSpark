package com.bryndsey.songbuilder.songstructure;

import com.bryndsey.songbuilder.songstructure.MusicStructure.Pitch;
import com.bryndsey.songbuilder.songstructure.MusicStructure.ScaleType;
import com.bryndsey.songbuilder.songstructure.MusicStructure.SongPart;
import com.bryndsey.songbuilder.songstructure.MusicStructure.MidiInstrument;

import java.util.ArrayList;

public class Song
{
	public int timeSigNum;
	public int timeSigDenom;
	public int tempo;
	
	public ScaleType scaleType;
	public Pitch key;
	
	public MidiInstrument chordInstrument;
	public MidiInstrument melodyInstrument;
	
	public ArrayList<SongPart> structure;
	
	public ChordProgression verseProgression;
	public ChordProgression chorusProgression;
	public ChordProgression bridgeProgression;
	
	public ArrayList<Integer> verseChordRhythm;
	public ArrayList<Integer> chorusChordRhythm;
	
	public ArrayList<Integer> theme;
	
	//public ArrayList<ArrayList<Integer>> melody;
	
	public Song(){		
		timeSigNum = 0;
		timeSigDenom = 0;
		
		tempo = 120;
		
		scaleType = ScaleType.MAJOR;
		key = Pitch.C;
		
		chordInstrument = MidiInstrument.ACOUSTIC_GRAND_PIANO;
		melodyInstrument = MidiInstrument.ACOUSTIC_GRAND_PIANO;
		
		structure = null;
		
		verseProgression = null;
		chorusProgression = null;
		bridgeProgression = null;
		
		verseChordRhythm = null;
		chorusChordRhythm = null;
		
		theme = null;
	}
		
} //class Song
