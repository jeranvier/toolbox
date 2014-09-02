package jeranvier.math.stats;

import java.lang.reflect.Method;
import java.util.Arrays;

import jeranvier.math.stats.exceptions.EmptyArrayException;

/* Class computing various mectrics on an array of data
 */

public class Analyzer <T extends Number> {
	
	private T[] data;
	private double mean;
	private double variance;
	private double standardDeviation;
	private T min;
	private T max;
	private Histogram histogram;
	
	public Analyzer(T[] data) throws EmptyArrayException{
		if(data.length <1){
			throw new EmptyArrayException();
		}
		
		this.data = data;
		this.mean = Double.NaN;
		this.variance = Double.NaN;
		this.standardDeviation = Double.NaN;
		this.min = null;
		this.max = null;
		this.histogram = null;
	}
	
	public double getMean() {
		return Double.isNaN(mean) ? computeMean(data) : mean;
	}

	public double getVariance() {
		return Double.isNaN(variance) ? computeVariance(data, getMean()) : variance;
	}

	public double getStandardDeviation() {
		return Double.isNaN(standardDeviation) ? computeStandardDeviation(getVariance()) : standardDeviation;
	}
	
	public T getMin() {
		return this.min == null? computeMin(data) : min;
	}
	
	public T getMax() {
		return this.max == null? computeMax(data) : max;
	}
	
	public Histogram getHistogram(int numberOfBins){
		return this.histogram == null? computeHistogram(data, numberOfBins) : histogram;
	}
	
	public T computeMin(T[] data) {
		T[] dataSorted = data.clone();
		Arrays.sort(dataSorted);
		return dataSorted[0];
	}
	
	public T computeMax(T[] data) {
		T[] dataSorted = data.clone();
		Arrays.sort(dataSorted);
		return dataSorted[dataSorted.length-1];
	}


	private double computeMean(T[] data) {
		mean = 0.0;
		
		for(Number n : data){
			mean += n.doubleValue();
		}
		
		return mean/data.length;
	}
	
	private double computeVariance(T[] data, double mean){
		variance = 0.0;
				
		for(Number n : data){
			variance += Math.pow(n.doubleValue()-mean, 2);
		}
		
		return variance/data.length;
	}
	
	private double computeStandardDeviation(double variance){
		this.standardDeviation = Math.sqrt(variance);
		return standardDeviation;
	}
	
	public Histogram computeHistogram(T[] data, int numberOfBins){
		this.histogram = new Histogram(numberOfBins);
		this.histogram.populate(data);
		return this.histogram;
	}
	
	public String displayData(){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Number n : data){
			sb.append(n);
			sb.append(", ");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append("]");
		return sb.toString();
	}

	public static void main(String[] args) throws EmptyArrayException{
		Integer[] data = new Integer[]{5,2,7,9,4,4,4,5};
		Analyzer<Integer> analyzer = new Analyzer<Integer>(data);
		System.out.println("Data: "+analyzer.displayData());
		System.out.println("Mean: "+analyzer.getMean());
		System.out.println("Variance: "+analyzer.getVariance());
		System.out.println("Standard deviation: "+analyzer.getStandardDeviation());
		System.out.println("Min: "+analyzer.getMin());
		System.out.println("Max: "+analyzer.getMax());
	}

}
