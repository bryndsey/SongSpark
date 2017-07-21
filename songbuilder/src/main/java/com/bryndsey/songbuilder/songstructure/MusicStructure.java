package com.bryndsey.songbuilder.songstructure;

import com.bryndsey.songbuilder.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicStructure {

	//TODO: Break this into individual classes all grouped under a MusicStructure package
	public enum ScaleType {
		MAJOR("Major", new int[]{2, 2, 1, 2, 2, 2, 1}),
		NATURALMINOR("Minor", new int[]{2, 1, 2, 2, 1, 2, 2}),
		HARMONICMINOR("Harmonic minor", new int[]{2, 1, 2, 2, 1, 3, 1}),
		MIXOLYDIAN("Mixolydian", new int[]{2, 2, 1, 2, 2, 1, 2}),
		DORIAN("Dorian", new int[]{2, 1, 2, 2, 2, 1, 2}),
		PHRYGIAN("Phrygian", new int[]{1, 2, 2, 2, 1, 2, 2}),
		LYDIAN("Lydian", new int[]{2, 2, 2, 1, 2, 2, 1}),
		LOCRIAN("Locrian", new int[]{1, 2, 2, 1, 2, 2, 2})/*,
		DOUBLEHARMONIC("Double Harmonic", new int[]{1, 3, 1, 2, 1, 3, 1})*/;

		private String friendlyName;
		private int[] relativeIntervals;
		private int[] absoluteIntervals;

		ScaleType(String friendlyName, int[] relativeIntervals) {
			this.friendlyName = friendlyName;
			this.relativeIntervals = relativeIntervals;
			this.absoluteIntervals = calculateAbsIntervals(relativeIntervals);
		}

		private int[] calculateAbsIntervals(int[] relIntervals) {
			int[] absIntervals = new int[relIntervals.length + 1];
			absIntervals[0] = 0;
			for (int ndx = 0; ndx < relIntervals.length; ndx++) {
				absIntervals[ndx + 1] = absIntervals[ndx] + relIntervals[ndx];
			}
			return absIntervals;
		}

		public int[] getAbsIntervals() {
			return absoluteIntervals;
		}

		public int[] getRelIntervals() {
			return relativeIntervals;
		}

		public ChordType getTriadChordType(int chordDegree) {
			// calculate interval between root of chord and third of chord
			int thirdInterval = getChordInterval(chordDegree, 3);
			//calculate interval between root of chord and fifth of chord
			int fifthInterval = getChordInterval(chordDegree, 5);

			if (thirdInterval == 4) {
				if (fifthInterval == 7)
					return ChordType.MAJOR;
				else if (fifthInterval == 8)
					return ChordType.AUGMENTED;
			} else if (thirdInterval == 3) {
				if (fifthInterval == 6)
					return ChordType.DIMINISHED;
				else if (fifthInterval == 7)
					return ChordType.MINOR;
			}

			return ChordType.UNKNOWN;
		}

		public int[] generateTriad(int root) {
			//int[] scaleIntervals = getScaleIntervals(scaleType);
			int[] triad = new int[3];
			/*
			triad[0] = scaleIntervals[root - 1]; 
			triad[1] = scaleIntervals[(root + 1) % 7];
			triad[2] = scaleIntervals[(root + 3) % 7];
			*/
			triad[0] = getInterval(1, root) + getChordInterval(root, 1);
			triad[1] = getInterval(1, root) + getChordInterval(root, 3);
			triad[2] = getInterval(1, root) + getChordInterval(root, 5);

			return triad;
		}

		/*
		 * Returns the interval in semitones from scale degree degree1 to 
		 *  scale degree degree2 in scale
		 */
		public int getInterval(int degree1, int degree2) {
			return Utils.sumSubArray(getRelIntervals(), degree1 - 1, degree2 - 1);
		}

		/*
		 * Returns the interval in semitones from scale degree chordRoot to 
		 *  scale degree degree2 in scale
		 */
		public int getChordInterval(int chordRoot, int chordDegree) {
			return getInterval(chordRoot, (chordRoot - 1 + chordDegree - 1) % 7 + 1);
		}

		@Override
		public String toString() {
			return friendlyName;
		}

		;
	}

	public enum TimeSignature {
		FOUR_FOUR(4, 4),
		THREE_FOUR(3, 4),
		TWO_FOUR(2, 4);

		private int numerator;
		private int denominator;

		TimeSignature(int numerator, int denominator) {
			this.numerator = numerator;
			this.denominator = denominator;
		}

		public int getNumerator() {
			return numerator;
		}

		public int getDenominator() {
			return denominator;
		}

		public String toString() {
			return numerator + "/" + denominator;
		}
	}

	// TODO: Replace all instances of code based around this to use enum instead
	public static final int[] TIMESIGNUMVALUES = {2, 3, 4};//, 6};
	public static final int[] TIMESIGDENOMVALUES = {4}; // just 4 for now... may expand later

	// Define the pitches present in the "Western" system
	// (forgive my ignorance on naming conventions)
	public enum Pitch {
		C("C"),
		D_FLAT("C♯/D♭"),
		D("D"),
		E_FLAT("D♯/E♭"),
		E("E"),
		F("F"),
		G_FLAT("F♯/G♭"),
		G("G"),
		A_FLAT("G♯/A♭"),
		A("A"),
		B_FLAT("A♯/B♭"),
		B("B");

		public int getBaseMidiPitch() {
			return this.ordinal() + 60;
		}

		// TODO: Implement MIDI key signature so importing into other software shows correct key signature
		// cof = Circle of Fifths: used to determine key signature
		protected int[] cofMajor = {0, -5, 2, -3, 4, -1, -6, 1, -4, 3, -2, 5};

		public int getMIDIKeyNumMajor() {
			return cofMajor[this.ordinal()];
		}

		public int getMIDIKeyNumMinor() {
			// TODO: I THINK THIS STILL NEEDS WORK!!!!
			return getRelativeMajor().getMIDIKeyNumMajor();
		}

		// TODO: I think these intervals are dependent on the scale...
		// maybe need to move this into the scale enum
		public Pitch getRelativeMinor() {
			return values()[(this.ordinal() + 9) % NUMPITCHES];
		}

		public Pitch getRelativeMajor() {
			return values()[(this.ordinal() + 3) % NUMPITCHES];
		}

		private String friendlyName;

		Pitch(String friendlyName) {
			this.friendlyName = friendlyName;
		}

		@Override
		public String toString() {
			return friendlyName;
		}
	}

	public enum ChordType {
		MAJOR(""),
		MINOR("m"),
		DIMINISHED("°"),
		AUGMENTED("+"),
		UNKNOWN("?");

		private String friendlyName;

		ChordType(String friendlyName) {
			this.friendlyName = friendlyName;
		}

		@Override
		public String toString() {
			return friendlyName;
		}

	}

	public static Pitch[] PITCHES = Pitch.values();
	public static final int NUMPITCHES = 12, OCTAVE = NUMPITCHES;

	public enum SongPart {VERSE, CHORUS, BRIDGE}

	;

	public enum Cadence {
		AUTHENTIC, HALF, PLAGAL, INTERRUPTED;

		public List<Integer> getChords() {
			switch (this) {
				case AUTHENTIC:
					return Arrays.asList(5, 1);
				case HALF:
					return Arrays.asList(5);
				case PLAGAL:
					return Arrays.asList(4, 1);
				case INTERRUPTED:
					return Arrays.asList(5, -1);
				default:
					return new ArrayList<Integer>();
			}
		}

		public static final double[] INTERRUPTEDCHORDCHANCES = {0.0, 3.0, 1.0, 2.0, 0.0, 4.0, 0.1};

	}

	public enum MidiInstrument {
		ACOUSTIC_GRAND_PIANO("Acoustic Grand Piano"),
		BRIGHT_ACOUSTIC_PIANO("Bright Acoustic Piano"),
		ELECTRIC_GRAND_PIANO("Electric Grand Piano"),
		HONKYTONK_PIANO("Honky-tonk Piano"),
		ELECTRIC_PIANO_1("Electric Piano 1"),
		ELECTRIC_PIANO_2("Electric Piano 2"),
		HARPSICHORD("Harpsichord"),
		CLAVINET("Clavinet"),
		CELESTA("Celesta"),
		GLOCKENSPIEL("Glockenspiel"),
		MUSIC_BOX("Music Box"),
		VIBRAPHONE("Vibraphone"),
		MARIMBA("Marimba"),
		XYLOPHONE("Xylophone"),
		TUBULAR_BELLS("Tubular Bells"),
		DULCIMER("Dulcimer"),
		DRAWBAR_ORGAN("Drawbar Organ"),
		PERCUSSIVE_ORGAN("Percussive Organ"),
		ROCK_ORGAN("Rock Organ"),
		CHURCH_ORGAN("Church Organ"),
		REED_ORGAN("Reed Organ"),
		ACCORDION("Accordion"),
		HARMONICA("Harmonica"),
		TANGO_ACCORDION("Tango Accordion"),
		ACOUSTIC_GUITAR_NYLON("Acoustic Guitar (nylon)"),
		ACOUSTIC_GUITAR_STEEL("Acoustic Guitar (steel)"),
		ELECTRIC_GUITAR_JAZZ("Electric Guitar (jazz)"),
		ELECTRIC_GUITAR_CLEAN("Electric Guitar (clean)"),
		ELECTRIC_GUITAR_MUTED("Electric Guitar (muted)"),
		OVERDRIVEN_GUITAR("Overdriven Guitar"),
		DISTORTION_GUITAR("Distortion Guitar"),
		GUITAR_HARMONICS("Guitar Harmonics"),
		ACOUSTIC_BASS("Acoustic Bass"),
		ELECTRIC_BASS_FINGER("Electric Bass (finger)"),
		ELECTRIC_BASS_PICK("Electric Bass (pick)"),
		FRETLESS_BASS("Fretless Bass"),
		SLAP_BASS_1("Slap Bass 1"),
		SLAP_BASS_2("Slap Bass 2"),
		SYNTH_BASS_1("Synth Bass 1"),
		SYNTH_BASS_2("Synth Bass 2"),
		VIOLIN("Violin"),
		VIOLA("Viola"),
		CELLO("Cello"),
		CONTRABASS("Contrabass"),
		TREMOLO_STRINGS("Tremolo Strings"),
		PIZZICATO_STRINGS("Pizzicato Strings"),
		ORCHESTRAL_HARP("Orchestral Harp"),
		TIMPANI("Timpani"),
		STRING_ENSEMBLE_1("String Ensemble 1"),
		STRING_ENSEMBLE_2("String Ensemble 2"),
		SYNTH_STRINGS_1("Synth Strings 1"),
		SYNTH_STRINGS_2("Synth Strings 2"),
		CHOIR_AAHS("Choir Aahs"),
		VOICE_OOHS("Voice Oohs"),
		SYNTH_CHOIR("Synth Choir"),
		ORCHESTRA_HIT("Orchestra Hit"),
		TRUMPET("Trumpet"),
		TROMBONE("Trombone"),
		TUBA("Tuba"),
		MUTED_TRUMPET("Muted Trumpet"),
		FRENCH_HORN("French Horn"),
		BRASS_SECTION("Brass Section"),
		SYNTH_BRASS_1("Synth Brass 1"),
		SYNTH_BRASS_2("Synth Brass 2"),
		SOPRANO_SAX("Soprano Sax"),
		ALTO_SAX("Alto Sax"),
		TENOR_SAX("Tenor Sax"),
		BARITONE_SAX("Baritone Sax"),
		OBOE("Oboe"),
		ENGLISH_HORN("English Horn"),
		BASSOON("Bassoon"),
		CLARINET("Clarinet"),
		PICCOLO("Piccolo"),
		FLUTE("Flute"),
		RECORDER("Recorder"),
		PAN_FLUTE("Pan Flute"),
		BLOWN_BOTTLE("Blown bottle"),
		SHAKUHACHI("Shakuhachi"),
		WHISTLE("Whistle"),
		OCARINA("Ocarina"),
		LEAD_1_SQUARE("Lead 1 (square)"),
		LEAD_2_SAWTOOTH("Lead 2 (sawtooth)"),
		LEAD_3_CALLIOPE("Lead 3 (calliope)"),
		LEAD_4_CHIFF("Lead 4 (chiff)"),
		LEAD_5_CHARANG("Lead 5 (charang)"),
		LEAD_6_VOICE("Lead 6 (voice)"),
		LEAD_7_FIFTHS("Lead 7 (fifths)"),
		LEAD_8_BASS_AND_LEAD("Lead 8 (bass + lead)"),
		PAD_1_NEW_AGE("Pad 1 (new age)"),
		PAD_2_WARM("Pad 2 (warm)"),
		PAD_3_POLYSYNTH("Pad 3 (polysynth)"),
		PAD_4_CHOIR("Pad 4 (choir)"),
		PAD_5_BOWED("Pad 5 (bowed)"),
		PAD_6_METALLIC("Pad 6 (metallic)"),
		PAD_7_HALO("Pad 7 (halo)"),
		PAD_8_SWEEP("Pad 8 (sweep)"),
		FX_1_RAIN("FX 1 (rain)"),
		FX_2_SOUNDTRACK("FX 2 (soundtrack)"),
		FX_3_CRYSTAL("FX 3 (crystal)"),
		FX_4_ATMOSPHERE("FX 4 (atmosphere)"),
		FX_5_BRIGHTNESS("FX 5 (brightness)"),
		FX_6_GOBLINS("FX 6 (goblins)"),
		FX_7_ECHOES("FX 7 (echoes)"),
		FX_8_SCIFI("FX 8 (sci-fi)"),
		SITAR("Sitar"),
		BANJO("Banjo"),
		SHAMISEN("Shamisen"),
		KOTO("Koto"),
		KALIMBA("Kalimba"),
		BAGPIPE("Bagpipe"),
		FIDDLE("Fiddle"),
		SHANAI("Shanai"),
		TINKLE_BELL("Tinkle Bell"),
		AGOGO("Agogo"),
		STEEL_DRUMS("Steel Drums"),
		WOODBLOCK("Woodblock"),
		TAIKO_DRUM("Taiko Drum"),
		MELODIC_TOM("Melodic Tom"),
		SYNTH_DRUM("Synth Drum"),
		REVERSE_CYMBAL("Reverse Cymbal"),
		GUITAR_FRET_NOISE("Guitar Fret Noise"),
		BREATH_NOISE("Breath Noise"),
		SEASHORE("Seashore"),
		BIRD_TWEET("Bird Tweet"),
		TELEPHONE_RING("Telephone Ring"),
		HELICOPTER("Helicopter"),
		APPLAUSE("Applause"),
		GUNSHOT("Gunshot");

		private String friendlyName;

		MidiInstrument(String friendlyName) {
			this.friendlyName = friendlyName;
		}

		public String toString() {
			return friendlyName;
		}

		public int programNumber() {
			return this.ordinal() + 1;
		}
	}

	////////////////////////////////////////////////////
	//This is a playground for testing at the moment:


	/*
	 * Translates degree, which is the interval from the root of the chord, 
	 * represented by chordRoot, to the interval relative to the scale tonic
	 */
	public int chordToScaleInterval(int chordRoot, int degree) {
		return (chordRoot + degree) % 7;
	}

	/*
	 * Translates degree, which is the interval from the tonic of the scale 
	 *  to the interval relative to the chord root, represented by chordRoot
	 */
	public int scaleToChordInterval(int chordRoot, int degree) {
		return (degree - chordRoot) % 7;
	}


}
