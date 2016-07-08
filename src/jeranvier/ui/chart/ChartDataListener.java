package jeranvier.ui.chart;

import java.util.Map;
import java.util.SortedMap;

public interface ChartDataListener<X,Y> {
	
	public void notify(Map<String, SortedMap<X,Y>> markers, Map<String, SortedMap<X,Y>> data);

}
