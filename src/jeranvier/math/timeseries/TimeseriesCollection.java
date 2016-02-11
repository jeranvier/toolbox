package jeranvier.math.timeseries;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class TimeseriesCollection implements Serializable{

	private static final long serialVersionUID = 1484279538252898170L;
	private final SortedMap<Long,Map<Integer, Double>> data; // <timestamp, <column id, value>>
	private final Map<String, Integer> labels;
		
	public TimeseriesCollection(SortedMap<Long,Map<Integer, Double>> data, Map<String, Integer> labels) {
		this.data = data;
		this.labels = labels;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("labels: "+this.labels+"\n");
		for(Entry<Long, Map<Integer, Double>> element: data.entrySet()){
			sb.append(element.getKey()+": "+element.getValue()+"\n");
		}
		return sb.toString();
	}
	
	public Timeseries getTimeSeries(String name){
		Timeseries.Builder tsb = new Timeseries.Builder();
		if(labels.containsKey(name)){
			Integer column = labels.get(name);
			for(Entry<Long, Map<Integer, Double>> element: data.entrySet()){
				tsb.put(element.getKey(), element.getValue().get(column));
			}
		}
		return tsb.build();
	}
	
	public TimeseriesCollection subTimeseries(Long start, Long end){
		 TimeseriesCollection.Builder tsc = new TimeseriesCollection.Builder();
		 tsc.labels = this.labels;
		 for(Map.Entry<Long, Map<Integer, Double>> element : this.data.entrySet()){
			 if(element.getKey() >= start && element.getKey() <= end){
				 tsc.putElement(element.getKey(), element.getValue());
			 }
		 }
		 return tsc.build();
	}
	
	public int size(){
		return this.data.size();
	}
	
	public Set<String> getLabels(){
		return labels.keySet();
	}
	
	public static final class Builder{
		private SortedMap<Long,Map<Integer, Double>> data; // <timestamp, <column id, value>>
		private Map<String, Integer> labels;
		
		public Builder(){
			data = new TreeMap<Long,Map<Integer, Double>>();
			labels = new HashMap<String, Integer>();
		}
		
		public void putElement(Long key, String name, Double value) {
			if(!data.containsKey(key)){
				data.put(key, new HashMap<Integer, Double>());
			}
			if(!labels.containsKey(name)){
				labels.put(name, labels.size());
			}
			
			data.get(key).put(labels.get(name), value);
		}
		
		public void putElement(Long key, Map<Integer, Double> values) {
				data.put(key, values);
		}
		
		public Builder putTimeseries(String name, Timeseries timeseries){		
		if(!labels.containsKey(name)){
			labels.put(name, labels.size());
		}
		
		for(Entry<Long, Double> element:timeseries.entrySet()){
			putElement(element.getKey(), name, element.getValue());
		}
		return this;
	}
		
		public TimeseriesCollection build(){
			return new TimeseriesCollection(Collections.unmodifiableSortedMap(data),  Collections.unmodifiableMap(labels));
		}
	}

}
