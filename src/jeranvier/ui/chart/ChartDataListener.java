package jeranvier.ui.chart;

import java.util.Map;

public interface ChartDataListener<X,Y> {
	
	public void notify(Map<String, Map<X,Y>> markers, Map<String, Map<X,Y>> data);

}
