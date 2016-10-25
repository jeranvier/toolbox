package jeranvier.math.stats;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class Histogram {
	
	private int numberOfBins;
	private Map<Double,Integer> bins;
	private double step;
	private int total;
	private boolean normalized;

	public Histogram(int numberOfBins){
		this.numberOfBins = numberOfBins;
		this.normalized = true;
	}
	
	private void determinateBuckets(int numberOfBins, Number min, Number max){
		this.step = (max.doubleValue()-min.doubleValue())/numberOfBins;
		bins = new TreeMap<Double, Integer>();
		for(int i = 0; i <numberOfBins; i++){
			bins.put(min.doubleValue()+i*step, 0);
		}
	}
	
	public Histogram populate(Number[] data){
		this.total = 0;
		
		Arrays.sort(data);
		System.out.println(data[0]+" ; "+data[data.length-1]);
		determinateBuckets(this.numberOfBins, data[0], data[data.length-1]);
		
		for(Number element : data){
			Double selectedBin = null;
			for(Double binStart : bins.keySet()){
				if(element.doubleValue() < binStart){
					break;
				}else{
					selectedBin = binStart;
				}
			}
			bins.put(selectedBin, bins.get(selectedBin) + 1);
			total++;
		}
		return this;
	}
	
	public Histogram populate(Collection< ? extends Number> data){
		Number[] array = new Number[data.size()];
		int i = 0;
		for(Number number : data){
			array[i] = number;
			i++;
		}
		return populate(array);
	}
	
	public String toString(){
		StringBuilder  sb = new StringBuilder();
		for(Entry<Double, Integer> bin : bins.entrySet()){
			sb.append(bin.getKey());
			sb.append("\t");
			sb.append(((double)bin.getValue())/(normalized?total:1));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public void setNormalized(boolean normalized){
		this.normalized = normalized;
	}

	public SortedMap<Double, Double> toDoubleMap() {
		SortedMap<Double, Double> map = new TreeMap<>();
		for(Entry<Double, Integer> bin : this.bins.entrySet()){
			map.put(bin.getKey(), (double)bin.getValue());
		}
		return map;
	}

}
