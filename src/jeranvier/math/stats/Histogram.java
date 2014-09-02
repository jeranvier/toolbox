package jeranvier.math.stats;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Histogram {
	
	private int numberOfBins;
	private Map<Double,Integer> bins;
	private double step;
	private int total;

	public Histogram(int numberOfBins){
		this.numberOfBins = numberOfBins;
	}
	
	private void determinateBuckets(int numberOfBins, Number min, Number max){
		this.step = Math.ceil((max.doubleValue()-min.doubleValue())/numberOfBins);
		bins = new TreeMap<Double, Integer>();
		for(int i = 0; i <numberOfBins; i++){
			bins.put(min.doubleValue()+i*step, 0);
		}
	}
	
	public void populate(Number[] data){
		this.total = 0;
		
		Arrays.sort(data);
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
		
	}
	
	public String toString(){
		StringBuilder  sb = new StringBuilder();
		for(Entry<Double, Integer> bin : bins.entrySet()){
			sb.append(bin.getKey());
			sb.append("\t");
			sb.append(((double)bin.getValue())/total);
			sb.append("\n");
		}
		return sb.toString();
	}

}
