package Func;


public class MiscFunc {

	public static boolean isInteger(String str) {
		for (char c : str.toCharArray())
			if (c < '0' || c > '9')
				return false;
			return true;
	}

	public static int getRandom(int min, int max) {
		return (int)(Math.random() * ((max - min) + 1)) + min;
	}
}
