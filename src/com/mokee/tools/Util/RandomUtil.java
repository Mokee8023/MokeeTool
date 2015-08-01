package com.mokee.tools.Util;

import java.util.Random;

public class RandomUtil {
	public static int getRandom(int max, int min) {	
		Random random = new Random();
		return random.nextInt(max) % (max - min + 1) + min;
	}
}
