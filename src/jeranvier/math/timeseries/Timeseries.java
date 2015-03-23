package jeranvier.math.timeseries;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;

import jeranvier.math.util.MathExtension;

public class Timeseries extends TreeMap<Long,Double>{
	private static final long serialVersionUID = 1L;
	public static enum AVERAGE_TYPE{CENTERED, DELAYED, AHEAD};
	
	public Timeseries(){
		super();
	}
	
	public Timeseries(Timeseries toCopy){
		super(toCopy);
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<Long, Double> entry : this.entrySet()){
			sb.append(entry.getKey()+" : "+entry.getValue()+"\n");
		}
		return sb.toString();
	}
	
	public Timeseries subTimeseries(Long start, Long end){
		 Timeseries ts = new  Timeseries();
		 for(Map.Entry<Long, Double> element : this.entrySet()){
			 if(element.getKey() > start && element.getKey() < end){
				 ts.put(element.getKey(), element.getValue());
			 }
		 }
		 return ts;
	}

	public Timeseries max(int maxValue) {
		Timeseries ts = new  Timeseries();
		Iterator<Map.Entry<Long, Double>> ite = this.entrySet().iterator();
		while(ite.hasNext()){
			Map.Entry<Long, Double> element = ite.next();
			 if((Double)element.getValue() <= maxValue){
				 ts.put(element.getKey(), element.getValue());
			 }
		 }
		 return ts;
	}
	
	public Timeseries movingAverage(int radius, AVERAGE_TYPE type){
		int windowLength = 2 * radius + 1;
		Timeseries ts = new  Timeseries();
		Queue<Double> window = new ArrayBlockingQueue<Double>(windowLength);
		Queue<Long> times = new ArrayBlockingQueue<Long>(radius +1);
		for(Map.Entry<Long, Double> entry : this.entrySet()){
			
			times.add(entry.getKey());
				
			window.add(entry.getValue());
			
			if(window.size() == windowLength){
				ts.put(times.poll(), MathExtension.average((Collection<? extends Number>) window));
				window.poll();
			}
			
			if(times.size() == radius + 1){
				times.poll();
			}
		}
		
		return ts;
	}
	
	public Timeseries add(double value){
		Timeseries result = new Timeseries(this);
		
		for(Map.Entry<Long, Double> entry : result.entrySet()){
			entry.setValue(entry.getValue() + value);
		}
		return result;
	}
	
	public Timeseries substract(double value){
		Timeseries result = new Timeseries(this);
		
		for(Map.Entry<Long, Double> entry : result.entrySet()){
			entry.setValue(entry.getValue() - value);
		}
		return result;
	}
	
	public Timeseries divide(double value){
		Timeseries result = new Timeseries(this);
		
		for(Map.Entry<Long, Double> entry : result.entrySet()){
			entry.setValue(entry.getValue() / value);
		}
		return result;
	}
	
	public Timeseries multiply(double value){
		Timeseries result = new Timeseries(this);
		
		for(Map.Entry<Long, Double> entry : result.entrySet()){
			entry.setValue(entry.getValue() * value);
		}
		return result;
	}
	
	public Timeseries substract(Map<Long, Double> values){
		if(this.size() != values.size()){
			throw new IllegalArgumentException("operation on timeseries of different sizes");
		}
		
		Timeseries result = new Timeseries(this);
		
		for(Map.Entry<Long, Double> entry : result.entrySet()){
			entry.setValue(entry.getValue() - values.get(entry.getKey()));
		}
		return result;
	}
	
	public Timeseries add(Map<Long, Double> values){
		if(this.size() != values.size()){
			throw new IllegalArgumentException("operation on timeseries of different sizes");
		}
		Timeseries result = new Timeseries(this);
		
		for(Map.Entry<Long, Double> entry : result.entrySet()){
			entry.setValue(entry.getValue() + values.get(entry.getKey()));
		}
		return result;
	}
	
}
