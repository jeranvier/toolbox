package jeranvier.math.stats;

import java.util.Map;
import java.util.TreeMap;

/** Represent a probabilistic distribution of items of type T
 * @param <T>
 * 
 */

public class Distribution<T extends Comparable<T>> extends TreeMap<T, Double>{
	private static final long serialVersionUID = 8978630696165523413L;
	private int samplesCounter;
	
	public Distribution(T[] samples) {
		samplesCounter = 0;
		for(T sample : samples){
			addSample(sample);
		}
	}

	public void addSample(T sample) {
		this.put(sample, containsKey(sample) ? get(sample)+1 : 1);
		samplesCounter++;
	}
	
	public double getProbability(T instance){
		return get(instance)/samplesCounter;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for( Map.Entry<T, Double> entry : entrySet()){
			sb.append(entry.getKey() + ": " + getProbability(entry.getKey()) +"\n");
		}
		return sb.toString();
	}

	public static void main(String[] args){
		Integer[] integers = new Integer[]{1, 2, 3, 1 ,2, 1, 1, 1, 2, 3, 4, 1};
		Distribution<Integer> distrib = new Distribution<>(integers);
		System.out.println(distrib.toString());
	}

}
