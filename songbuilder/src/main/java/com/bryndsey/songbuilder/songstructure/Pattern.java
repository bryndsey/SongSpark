package com.bryndsey.songbuilder.songstructure;

import java.util.ArrayList;

public class Pattern {

	// This class is for small subsections of a chord progression. They are often repeated
	// during a chord progression, so that is why they are broken up

	public ArrayList<Integer> chords;
	public ArrayList<ArrayList<Note>> notes;

	public Pattern() {
		chords = new ArrayList<Integer>();
		notes = new ArrayList<ArrayList<Note>>();
	}

	public Pattern(Pattern inst) {
		chords = new ArrayList<Integer>(inst.chords);
		notes = new ArrayList<ArrayList<Note>>(inst.notes);
	}

	public ArrayList<Integer> getChords() {
		return chords;
	}

	public ArrayList<ArrayList<Note>> getNotes() {
		return notes;
	}

	public Pattern plus(Pattern addend) {
		Pattern result = new Pattern(this);
		result.chords.addAll(addend.chords);
		result.notes.addAll(addend.notes);
		return result;
	}

}
