package jeranvier.math.stats;

import java.util.Collection;
import java.util.Map;

public class SimpleStats {
	
	public static double mean(Collection<? extends Number> collection){
		double mean = 0.0;
		for(Number element : collection){
			mean = mean + element.doubleValue();
		}
		return mean/collection.size();
	}
	
	public static double mean(Map<?, ? extends Number> map){
		return mean(map.values());
	}

}
