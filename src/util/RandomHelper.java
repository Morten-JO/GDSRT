package util;

import java.util.Random;

public class RandomHelper {

	public static Random random = new Random();

	/**
	 * from is inclusive to is inclusive
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static int getRandomInt(int from, int to) {
		return random.nextInt(to + 1 - from) + from;
	}

	public static float getProperFloat(float val) {
		int big = (int) (val * 1000f);
		float decimaled = (big) / 1000f;
		return decimaled;
	}

}