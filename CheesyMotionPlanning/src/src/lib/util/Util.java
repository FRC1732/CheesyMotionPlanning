package src.lib.util;

/**
 * Contains basic functions that are used often.
 */
public class Util {
	/** Prevent this class from being instantiated. */
	private Util() {
	}

	public static boolean epsilonEquals(double a, double b, double epsilon) {
		return (a - epsilon <= b) && (a + epsilon >= b);
	}
}
