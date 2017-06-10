package com.bryndsey.songspark.dagger;

public class ComponentHolder {

	public static final ComponentHolder INSTANCE = new ComponentHolder();

	private ApplicationComponent component;

	private static ComponentHolder getInstance() {
		return INSTANCE;
	}

	public static ApplicationComponent getApplicationComponent() {
		return getInstance().component;
	}

	public static void setApplicationComponent(ApplicationComponent component) {
		getInstance().component = component;
	}
}
