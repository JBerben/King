package src.main.org.updater.util;

public class Maths {

	// Returns ratio. Makes code look cleaner when using this function :)
	public static float getRatio(int a, int b) {
		return ((float) Math.min(a, b) / (float) Math.max(a, b));
	}

	// Returns ratio. Makes code look cleaner when using this function :)
	public static float getRatio(float a, float b) {
		return ((float) Math.min(a, b) / (float) Math.max(a, b));
	}

	// Returns ratio. Makes code look cleaner when using this function :)
	public static float getRatio(long a, long b) {
		return ((float) Math.min(a, b) / (float) Math.max(a, b));
	}

	public static boolean isNan(float x) {
		return x != x;
	}

	public static boolean isNan(int x) {
		return x != x;
	}

}
