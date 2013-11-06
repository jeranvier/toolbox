package jeranvier.util;

import java.util.Arrays;

public class MathExtension {
	
	public static int min(int... candidates){
		Arrays.sort(candidates);
		return candidates[0];
	}
	
	public static short min(short... candidates){
		Arrays.sort(candidates);
		return candidates[0];
	}
	
	public static byte min(byte... candidates){
		Arrays.sort(candidates);
		return candidates[0];
	}
	
	public static double min(double... candidates){
		Arrays.sort(candidates);
		return candidates[0];
	}
	
	public static long min(long... candidates){
		Arrays.sort(candidates);
		return candidates[0];
	}
	
	public static float min(float... candidates){
		Arrays.sort(candidates);
		return candidates[0];
	}
}
