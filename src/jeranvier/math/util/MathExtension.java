package jeranvier.math.util;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jeranvier.math.timeseries.Timeseries;

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
	
	public static Entry<Long, Double> max(Timeseries ts){
		Entry<Long, Double> max = new AbstractMap.SimpleEntry<Long, Double>(-1l, Double.NEGATIVE_INFINITY);
		for(Entry<Long, Double> entry : ts.entrySet()){
			if(entry.getValue() > max.getValue()){
				max = new AbstractMap.SimpleEntry<Long, Double>(entry);
			}
		}
		return max;
	}
	
	public static Entry<Long, Double> min(Timeseries ts){
		Entry<Long, Double> min = new AbstractMap.SimpleEntry<Long, Double>(-1l, Double.POSITIVE_INFINITY);
		for(Entry<Long, Double> entry : ts.entrySet()){
			if(entry.getValue() < min.getValue()){
				min = new AbstractMap.SimpleEntry<Long, Double>(entry);
			}
		}
		return min;
	}
	
}
