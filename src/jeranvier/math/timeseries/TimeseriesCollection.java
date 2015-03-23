package jeranvier.math.timeseries;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class TimeseriesCollection{
	
	private SortedMap<Long,Map<Integer, Double>> data; // <timestamp, <column id, value>>
	private Map<String, Integer> labels;
	private Integer width;
	
	public TimeseriesCollection(){
		width = 0;
		data = new TreeMap<Long,Map<Integer, Double>>();
		labels = new HashMap<String, Integer>();
	}
	
	public TimeseriesCollection(String name, Timeseries timeseries){
			width = 0;
			data = new TreeMap<Long,Map<Integer, Double>>();
			labels = new HashMap<String, Integer>();
		
		width++;
		if(!labels.containsKey(name)){
			labels.put(name, width);
		}
		
		for(Entry<Long, Double> element:timeseries.entrySet()){
			putElement(element.getKey(), name, element.getValue());
		}
		
	}

	public void putElement(Long key, String name, Double value) {
		if(!data.containsKey(key)){
			data.put(key, new HashMap<Integer, Double>());
		}
		if(!labels.containsKey(name)){
			width++;
			labels.put(name, width);
		}
		
		data.get(key).put(labels.get(name), value);
	}
	
	public void putElement(Long key, Map<Integer, Double> values) {
			data.put(key, values);
	}
		
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Entry<Long, Map<Integer, Double>> element: data.entrySet()){
			sb.append(element.getKey()+": "+element.getValue()+"\n");
		}
		return sb.toString();
	}
	
	public Timeseries getTimeSeries(String name){
		Timeseries ts = new Timeseries();
		if(labels.containsKey(name)){
			Integer column = labels.get(name);
			for(Entry<Long, Map<Integer, Double>> element: data.entrySet()){
				ts.put(element.getKey(), element.getValue().get(column));
			}
		}
		return ts;
	}
	
	public TimeseriesCollection subTimeseries(Long start, Long end){
		 TimeseriesCollection tsc = new  TimeseriesCollection();
		 tsc.labels = this.labels;
		 for(Map.Entry<Long, Map<Integer, Double>> element : this.data.entrySet()){
			 if(element.getKey() >= start && element.getKey() <= end){
				 tsc.putElement(element.getKey(), element.getValue());
			 }
		 }
		 return tsc;
	}
	
	public int size(){
		return this.data.size();
	}

}
