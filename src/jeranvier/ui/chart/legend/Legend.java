package jeranvier.ui.chart.legend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import jeranvier.ui.chart.Chart;
import jeranvier.ui.chart.ChartDataListener;
import jeranvier.ui.chart.MarkersDrawer;

public class Legend<X,Y> extends JPanel implements ChartDataListener<X,Y>{
	
	Map<String, LegendItem> markerItems;
	Map<String, LegendItem> dataItems;
	
	public Legend(){
		markerItems = new HashMap<>();
		dataItems = new HashMap<>();
		this.setBackground(Color.WHITE);
	}

	@Override
	public void notify(Map<String, Map<X,Y>> markers, Map<String, Map<X,Y>> data) {
		this.removeAll();
		int i = 0;
		for(String dataString : data.keySet()){
			dataItems.put(dataString, new LegendData(dataString, Chart.getColor(i), Chart.getStroke(i)));
			this.add(dataItems.get(dataString));
			i++;
		}
		
		for(String markerString : markers.keySet()){
			markerItems.put(markerString, new LegendMarker(markerString, Chart.getColor(i), MarkersDrawer.getMarker(i)));
			this.add(markerItems.get(markerString));
			i++;
		}
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
