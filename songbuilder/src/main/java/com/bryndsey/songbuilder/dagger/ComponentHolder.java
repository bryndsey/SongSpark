package com.bryndsey.songbuilder.dagger;


public class ComponentHolder {
	public static final ComponentHolder INSTANCE = new ComponentHolder();

	private SongWriterComponent component;

	private static ComponentHolder getInstance() {
		return INSTANCE;
	}

	public static SongWriterComponent getSongWriterComponent() {
		return getInstance().component;
	}

	public static void setSongWriterComponent(SongWriterComponent component) {
		getInstance().component = component;
	}
}
