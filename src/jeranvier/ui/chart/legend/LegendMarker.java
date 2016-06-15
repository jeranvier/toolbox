package jeranvier.ui.chart.legend;

import java.awt.Color;
import java.awt.Stroke;

import javax.swing.JLabel;

import jeranvier.ui.chart.Chart;
import jeranvier.ui.chart.Marker;
import jeranvier.ui.chart.MarkersDrawer;

public class LegendMarker extends LegendItem{

	public final Color color;
	public final Marker marker;

	public LegendMarker(String name) {
		this(name, Chart.getColor(0), MarkersDrawer.getMarker(0));
	}
	
	public LegendMarker(String name, Color color, Marker marker){
		super(name);
		this.color = color;
		this.marker=marker;
		this.add(new MarkerSample(color,marker));
		this.add(new JLabel(this.name));
	}

}
