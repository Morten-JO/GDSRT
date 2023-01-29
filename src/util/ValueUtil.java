package util;

public class ValueUtil {

	public static boolean isAroundEqual(float valueOne, float valueTwo, float shared) {
		if(Math.abs(valueOne - valueTwo) < shared) {
			return true;
		}
		return false;
	}

}
