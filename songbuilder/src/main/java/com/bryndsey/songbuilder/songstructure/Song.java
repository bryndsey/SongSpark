package com.bryndsey.songbuilder.songstructure;

import com.bryndsey.songbuilder.songstructure.MusicStructure.Pitch;
import com.bryndsey.songbuilder.songstructure.MusicStructure.ScaleType;
import com.bryndsey.songbuilder.songstructure.MusicStructure.SongPart;
import com.pdrogfer.mididroid.event.ProgramChange;

import java.util.ArrayList;

public class Song
{
	public int timeSigNum;
	public int timeSigDenom;
	public int tempo;
	
	public ScaleType scaleType;
	public Pitch key;
	
	public ProgramChange.MidiProgram chordInstrument;
	public ProgramChange.MidiProgram melodyInstrument;
	
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
		
		scaleType = ScaleType.MAJOR;
		key = Pitch.C;
		
		chordInstrument = ProgramChange.MidiProgram.ACOUSTIC_GRAND_PIANO;
		melodyInstrument = ProgramChange.MidiProgram.ACOUSTIC_GRAND_PIANO;
		
		structure = null;
		
		verseProgression = null;
		chorusProgression = null;
		bridgeProgression = null;
		
		verseChordRhythm = null;
		chorusChordRhythm = null;
		
		theme = null;
	}
		
} //class Song
