package jeranvier.ui.chart.legend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import javax.swing.JComponent;

import jeranvier.ui.chart.Chart;
import jeranvier.ui.chart.Marker;

public class MarkerSample extends JComponent{
	
	public final Color color;
	public final Marker marker;
	
	public MarkerSample(Color color, Marker marker){
		this.color = color;
		this.marker = marker;
		this.setSize(20, 20);
		this.setPreferredSize(new Dimension(20, 20));
		this.setMinimumSize(new Dimension(20, 20));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(color);
		marker.draw(g2d, new Point2D.Float(10, 10), Chart.MARKER_SIZE);
	}
	
	

}
