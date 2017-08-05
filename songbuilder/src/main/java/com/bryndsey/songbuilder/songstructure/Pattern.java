package com.bryndsey.songbuilder.songstructure;

import java.util.ArrayList;

public class Pattern {

	// This class is for small subsections of a chord progression. They are often repeated
	// during a chord progression, so that is why they are broken up

	public ArrayList<Integer> chords;
	public ArrayList<ArrayList<Note>> chordNotes;

	public ArrayList<ArrayList<Note>> notes;

	public ArrayList<ArrayList<Note>> bassNotes;

	public Pattern() {
		chords = new ArrayList<>();
		chordNotes = new ArrayList<>();
		notes = new ArrayList<>();

		bassNotes = new ArrayList<>();
	}

	public Pattern(Pattern inst) {
		chords = new ArrayList<>(inst.chords);
		chordNotes = new ArrayList<>(inst.chordNotes);
		notes = new ArrayList<>(inst.notes);

		bassNotes = new ArrayList<>(inst.bassNotes);
	}
}
