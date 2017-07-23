package com.bryndsey.songbuilder.songstructure;

import java.util.ArrayList;

public class Pattern {

	// This class is for small subsections of a chord progression. They are often repeated
	// during a chord progression, so that is why they are broken up

	public ArrayList<Integer> chords;
	public ArrayList<ArrayList<Note>> notes;

	public Pattern() {
		chords = new ArrayList<>();
		notes = new ArrayList<>();
	}

	public Pattern(Pattern inst) {
		chords = new ArrayList<>(inst.chords);
		notes = new ArrayList<>(inst.notes);
	}

	public Pattern add(Pattern addend) {
		Pattern result = new Pattern(this);
		result.chords.addAll(addend.chords);
		result.notes.addAll(addend.notes);
		return result;
	}

}
