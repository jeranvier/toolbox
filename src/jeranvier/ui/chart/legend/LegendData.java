package jeranvier.ui.chart.legend;

import java.awt.Color;
import java.awt.Stroke;

import javax.swing.JLabel;

import jeranvier.ui.chart.Chart;

public class LegendData extends LegendItem{

	public final Color color;
	public final Stroke stroke;

	public LegendData(String name) {
		this(name, Chart.getColor(0), Chart.getStroke(0));
	}
	
	public LegendData(String name, Color color, Stroke stroke){
		super(name);
		this.color = color;
		this.stroke=stroke;
		this.add(new LineSample(color,stroke));
		this.add(new JLabel(this.name));
	}

}
