package com.bryndsey.songbuilder;


import java.util.Random;

public class RandomNumberGenerator {

	private static Random random = new Random();

	public static int getRandomInt() {
		return random.nextInt();
	}

	public static int getRandomIntUpTo(int max) {
		return random.nextInt(max);
	}

	public static int getRandomIntInRange(int min, int max) {
		int rangeSize = max - min;
		return random.nextInt(rangeSize) + min;
	}

	public static double getRandomDouble() {
		return random.nextDouble();
	}

	public static double getRandomDoubleUpTo(double max) {
		return random.nextDouble() * max;
	}

	public static double getRandomDoubleInRange(double min, double max) {
		double rangeSize = max - min;
		return getRandomDoubleUpTo(rangeSize) + min;
	}
}
