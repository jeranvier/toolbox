package jeranvier.math.timeseries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Function;

import javax.naming.OperationNotSupportedException;

import jeranvier.io.CSVHandler;
import jeranvier.io.CSVStreamingHandler;
import jeranvier.io.CSVStreamingHandler.Record;
import jeranvier.math.dsp.filters.ButterworthFilter;
import jeranvier.math.linearAlgebra.Matrix;
import jeranvier.math.linearAlgebra.Vector;
import jeranvier.math.stats.SimpleStats;
import jeranvier.math.util.Complex;

public class Timeseries extends TreeMap<Long,Double> implements Serializable{

	private static final long serialVersionUID = -4556555657603027141L;

	public static enum SLIDING_WINDOW_TYPE{CENTERED, DELAYED, AHEAD};
	
	public Timeseries(Map<Long,Double> data){
		super(data);
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<Long, Double> entry : this.entrySet()){
			sb.append(entry.getKey()+"\t"+entry.getValue()+"\n");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	public Timeseries subTimeseries(Long start, Long end){
		 Builder tsb = new Timeseries.Builder();
		 for(Map.Entry<Long, Double> element : this.entrySet()){
			 if(element.getKey() >= start && element.getKey() < end){
				 tsb.put(element.getKey(), element.getValue());
			 }
		 }
		 return tsb.build();
	}
	
	public Timeseries translate(Long diff){
		 Builder tsb = new Timeseries.Builder();
		 for(Map.Entry<Long, Double> element : this.entrySet()){
			 tsb.put(element.getKey()+diff, element.getValue());
		 }
		 return tsb.build();
	}
	
	public double integrate(){
		double sum = 0.0;
		Entry<Long, Double> previousEntry = null;
		for(Entry<Long, Double> currentEntry: this.entrySet()){
			if(previousEntry != null){
				double y1 = previousEntry.getValue();
				double y2 = currentEntry.getValue();
				long x1 = previousEntry.getKey();
				long x2 = currentEntry.getKey();
				
				sum += (y1+y2)*(x2-x1)/2;
			}
			previousEntry = currentEntry;
		}
		return sum;
	}
	
	public Timeseries splice(Timeseries that){
		 Builder tsb = new Timeseries.Builder();
		 for(Map.Entry<Long, Double> element : this.entrySet()){
			 tsb.put(element.getKey(), element.getValue());
		 }
		 for(Map.Entry<Long, Double> element : that.entrySet()){
			 tsb.put(element.getKey(), element.getValue());
		 }
		 return tsb.build();
	}
	
	public Timeseries cumulativeSplice(Timeseries that){
		 Builder tsb = new Timeseries.Builder();
		 for(Map.Entry<Long, Double> element : this.entrySet()){
			 tsb.put(element.getKey(), element.getValue());
		 }
		 for(Map.Entry<Long, Double> element : that.entrySet()){
			 if(this.containsKey(element.getKey())){
				 tsb.put(element.getKey(), this.get(element.getKey())+element.getValue());
			 }else{				 
				 tsb.put(element.getKey(), element.getValue());
			 }
		 }
		 return tsb.build();
	}
	
	public Timeseries averageSplice(Timeseries that){
		 Builder tsb = new Timeseries.Builder();
		 for(Map.Entry<Long, Double> element : this.entrySet()){
			 tsb.put(element.getKey(), element.getValue());
		 }
		 for(Map.Entry<Long, Double> element : that.entrySet()){
			 if(this.containsKey(element.getKey())){
				 tsb.put(element.getKey(), (this.get(element.getKey())+element.getValue())/2);
			 }else{				 
				 tsb.put(element.getKey(), element.getValue());
			 }
		 }
		 return tsb.build();
	}
	
	public Timeseries substituteAll(double[] values){
		if(this.size() != values.length){
			throw new IllegalArgumentException("operation on timeseries of different sizes (" +this.size() +"=!"+values.length+")");
		}
		Builder tsb = new Timeseries.Builder();
		int i = 0;
		for(Map.Entry<Long, Double> element : this.entrySet()){
			tsb.put(element.getKey(), values[i]);
			i++;
		}
		return tsb.build();
	}
	
	public Timeseries substituteAll(Vector values){
		if(this.size() != values.size()){
			throw new IllegalArgumentException("operation on timeseries of different sizes (" +this.size() +"=!"+values.size()+")");
		}
		Builder tsb = new Timeseries.Builder();
		int i = 1;
		for(Map.Entry<Long, Double> element : this.entrySet()){
			tsb.put(element.getKey(), values.get(i).re());
			i++;
		}
		return tsb.build();
	}

	public Timeseries max(int maxValue) {
		 Builder tsb = new Timeseries.Builder();
		Iterator<Map.Entry<Long, Double>> ite = this.entrySet().iterator();
		while(ite.hasNext()){
			Map.Entry<Long, Double> element = ite.next();
			 if((Double)element.getValue() <= maxValue){
				 tsb.put(element.getKey(), element.getValue());
			 }
		 }
		 return tsb.build();
	}
	
	public Timeseries process(Function<Entry<Long,Double>, Double> function){
		Timeseries.Builder tsb = new Timeseries.Builder();
		for(Map.Entry<Long, Double> entry : this.entrySet()){
			tsb.put(entry.getKey(), function.apply(entry));	
		}
		return tsb.build();
	}

	
	//windowLength is in milliseconds
	public Timeseries slidingWindow(long windowLength, SLIDING_WINDOW_TYPE type, Function<SortedMap<Long, Double>, Double> function){
		Timeseries.Builder tsb = new Timeseries.Builder();
		
		long start;
		long end;
		long key;
		switch(type){
		case CENTERED:
			long half = windowLength/2;
			start = this.firstKey() + half;
			end = this.lastKey()-half;
			for(Map.Entry<Long, Double> entry : this.entrySet()){
				key = entry.getKey();
				if(key >= start && key <= end){
					tsb.put(key, function.apply(this.subMap(key-half, key+half)));
				}
			}
			break;
		case AHEAD:
			start = this.firstKey();
			end = this.lastKey()-windowLength;
			for(Map.Entry<Long, Double> entry : this.entrySet()){
				key = entry.getKey();
				if(key >= start && key <= end){
					tsb.put(key, function.apply(this.subMap(key, key+windowLength)));
				}
			}
			break;
		case DELAYED:
			start = this.firstKey()+windowLength;
			end = this.lastKey();
			for(Map.Entry<Long, Double> entry : this.entrySet()){
				key = entry.getKey();
				if(key >= start && key <= end){
					tsb.put(key, function.apply(this.subMap(key-windowLength, key)));
				}
			}
			break;
		}
			
		return tsb.build();
	}
	
	public Timeseries add(double value){
		 Builder tsb = new Timeseries.Builder();		
		for(Map.Entry<Long, Double> entry : this.entrySet()){
			tsb.put(entry.getKey(), entry.getValue() + value);
		}
		return tsb.build();
	}
	
	public Timeseries substract(double value){
		 Builder tsb = new Timeseries.Builder();		
			for(Map.Entry<Long, Double> entry : this.entrySet()){
				tsb.put(entry.getKey(), entry.getValue() - value);
			}
			return tsb.build();
	}
	
	public Timeseries divide(double value){
		 Builder tsb = new Timeseries.Builder();		
			for(Map.Entry<Long, Double> entry : this.entrySet()){
				tsb.put(entry.getKey(), entry.getValue() / value);
			}
			return tsb.build();
	}
	
	public Timeseries multiply(double value){
		Builder tsb = new Timeseries.Builder();		
		for(Map.Entry<Long, Double> entry : this.entrySet()){
			tsb.put(entry.getKey(), entry.getValue() * value);
		}
		return tsb.build();
	}
	
	public Timeseries rescale(){
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for(Map.Entry<Long, Double> entry : this.entrySet()){
			if(entry.getValue()>max){
				max = entry.getValue();
			}
			if(entry.getValue()<min){
				min = entry.getValue();
			}
		}
		
		double diff = max-min;
		Builder tsb = new Timeseries.Builder();		
		for(Map.Entry<Long, Double> entry : this.entrySet()){
			tsb.put(entry.getKey(), (entry.getValue()-min) /diff);
		}
		return tsb.build();
	}
	
	public Timeseries substract(Map<Long, Double> values){
		if(this.size() != values.size()){
			throw new IllegalArgumentException("operation on timeseries of different sizes");
		}
		
		Builder tsb = new Timeseries.Builder();
		
		for(Map.Entry<Long, Double> entry : this.entrySet()){
			tsb.put(entry.getKey(), entry.getValue() - values.get(entry.getKey()));
		}
		return tsb.build();
	}
	
	public Timeseries add(Map<Long, Double> values){
		if(this.size() != values.size()){
			throw new IllegalArgumentException("operation on timeseries of different sizes");
		}

		Builder tsb = new Timeseries.Builder();

		for(Map.Entry<Long, Double> entry : this.entrySet()){
			tsb.put(entry.getKey(), entry.getValue() + values.get(entry.getKey()));
		}
		return tsb.build();
	}
	
	public Timeseries resample(long start, long end, long step, Resampler<Long, Double> resampler){
		Builder tsb = new Timeseries.Builder();
		for(long i = start; i <= end; i+=step){
			tsb.put(i, get(i, resampler));
		}
		return tsb.build();
	}
	
	public double mean(){
	double mean = 0;
	int t = 1;
	for (double x : this.values()) {
		mean += (x - mean) / t;
		++t;
	}
	return mean;
	}
	
	public double get(long key, Resampler<Long, Double> resampler) {
		if(this.containsKey(key)){
			return get(key);
		}
		else{
			java.util.Map.Entry<Long, Double> floor = this.floorEntry(key);
			java.util.Map.Entry<Long, Double> ceiling = this.ceilingEntry(key);
			if(floor == null){
				return ceiling.getValue();
			}
			if(ceiling == null){
				return floor.getValue();
			}
			return resampler.interpolate(floor, key, ceiling);
		}
	}
	
	public long duration(){
		return this.lastKey()-this.firstKey();
	}

	public static final class Builder{
		SortedMap<Long, Double> data;
		public Builder(){
			this.data = new TreeMap<>();
		}
		
		public Builder(Map<Long, Double> data){
			this.data = new TreeMap<>(data);
		}

		public void put(Long key, double value) {
			data.put(key, value);
		}
		
		public boolean containsKey(Long key){
			return data.containsKey(key);
		}
		
		public void remove(Long key) {
			data.remove(key);
		}
		
		public static Timeseries zeros(long start, long stop, long step){
			Builder z = new Builder();
			for(long i=start;i<stop; i+=step){
				z.put(i, 0);
			}
			return z.build();
		}

		public Timeseries build() {
			return new Timeseries(data);
		}
	}

	public Timeseries linearResample(long start, long end, long step) {
		return this.resample(start, end, step, (f, k ,c)->{
					double ratio = ((double)(k-f.getKey()))/(c.getKey()-f.getKey());
					return f.getValue() + ratio*(c.getValue() - f.getValue());
				});
	}
	
	public Timeseries linearResample(long step) {
		return this.resample(this.firstKey(), this.lastKey(), step, (f, k ,c)->{
					double ratio = ((double)(k-f.getKey()))/(c.getKey()-f.getKey());
					return f.getValue() + ratio*(c.getValue() - f.getValue());
				});
	}
	
	public Timeseries nearestNeighborResample(long start, long end, long step) {
		return this.resample(start, end, step, (f, k ,c)->{
					if(Math.abs(f.getKey()-k) < Math.abs(c.getKey()-k)){
						return f.getValue();
					}
					else{
						return c.getValue();	
					}
				});
	}

	public double getNearest(long t){
			return this.get(t, (f, k ,c)->{
						if(Math.abs(f.getKey()-k) < Math.abs(c.getKey()-k)){
							return f.getValue();
						}
						else{
							return c.getValue();	
						}
					});
	}
	
	public Vector vector() {
		Vector.Builder vb = new Vector.Builder(this.size());
		int i=1;
		for(Double value : this.values()){
			vb.set(i, new Complex(value, 0.0));
			i++;
		}
		return vb.build();
	}
	
	public static Timeseries load(File file) throws IOException{
		Timeseries.Builder b = new Timeseries.Builder();
		CSVStreamingHandler csh = new CSVStreamingHandler(CSVHandler.TAB, CSVHandler.EMPTY, false, file.getAbsolutePath());
		
		while(csh.hasNext()){
			Record next = csh.next();
			b.put(next.getLong(0), next.getDouble(1));
		}
		return b.build();
	}
	
	public static Timeseries load(File file, boolean headerAvailable) throws IOException{
		Timeseries.Builder b = new Timeseries.Builder();
		CSVStreamingHandler csh = new CSVStreamingHandler(CSVHandler.TAB, CSVHandler.EMPTY, headerAvailable, file.getAbsolutePath());
		csh.getRecords();
		while(csh.hasNext()){
			Record next = csh.next();
			b.put(next.getLong(0), next.getDouble(1));
		}
		return b.build();
	}

	public Timeseries substituteFrom(long start, Vector values) {
		Builder tsb = new Timeseries.Builder();
		int i = 1;
		for(Map.Entry<Long, Double> element : this.entrySet()){
			if(element.getKey()<start){
				//don't alter the timeseries
				tsb.put(element.getKey(), element.getValue());
			}else{
				if(i <= values.size()){
					tsb.put(element.getKey(), values.get(i).re());
					i++;
				}else{
					//nothing else to substitute
					tsb.put(element.getKey(), element.getValue());					
				}
			}
		}
		if(i+1 <= values.size()){
			throw new IllegalArgumentException("operation with incompatible sizes. Some values could not be copied.");
		}
		return tsb.build();
	}

	public Entry<Long, Double> max() {
		Map.Entry<Long, Double> max = this.firstEntry();
		for(Map.Entry<Long, Double> entry : this.entrySet()){
			if(entry.getValue()> max.getValue()){
				max = entry;
			}
		}
		return max;
	}
	
	public Entry<Long, Double> min() {
		Map.Entry<Long, Double> min = this.firstEntry();
		for(Map.Entry<Long, Double> entry : this.entrySet()){
			if(entry.getValue()< min.getValue()){
				min = entry;
			}
		}
		return min;
	}

	public Timeseries filter(ButterworthFilter bw) {
		return this.substituteAll(bw.filter(this.vector()));
	}

	public Timeseries computeMaximums() {
		Timeseries.Builder tsb = new Timeseries.Builder();
		double previousValue = Double.NEGATIVE_INFINITY;
		long previousTime = 0l;
		double previousSlop=1.0;
		for(Entry<Long, Double> entry : this.entrySet()){
			double slop = Math.signum(entry.getValue()-previousValue);
			if(slop<0 && previousSlop>0){ // we found a max (the previous value)
				tsb.put(previousTime, previousValue);
			}
			
			if(slop !=0){
				previousSlop = slop;
				previousValue = entry.getValue();
				previousTime = entry.getKey();
			}
		}
		
		return tsb.build();
	}

	//stretch a timeseries of length l to a TS of length new L;
	public Timeseries stretch(long newL) {
		long start = this.firstKey();
		long l = this.lastKey()-start;
		double ratio = ((double)newL)/l;
		Timeseries.Builder tsb = new Timeseries.Builder();
		for(Entry<Long, Double> entry : this.entrySet()){
			long t = entry.getKey();
			long newT = (long) (start +(t-start)*ratio);
			tsb.put(newT, entry.getValue());
		}
		return tsb.build();
	}
	
}
