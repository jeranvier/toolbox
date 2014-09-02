package jeranvier.math.util;

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
	
	public static int max(int... candidates){
		Arrays.sort(candidates);
		return candidates[candidates.length-1];
	}
	
	public static short max(short... candidates){
		Arrays.sort(candidates);
		return candidates[candidates.length-1];
	}
	
	public static byte max(byte... candidates){
		Arrays.sort(candidates);
		return candidates[candidates.length-1];
	}
	
	public static double max(double... candidates){
		Arrays.sort(candidates);
		return candidates[candidates.length-1];
	}
	
	public static long max(long... candidates){
		Arrays.sort(candidates);
		return candidates[candidates.length-1];
	}
	
	public static float max(float... candidates){
		Arrays.sort(candidates);
		return candidates[candidates.length-1];
	}
}
