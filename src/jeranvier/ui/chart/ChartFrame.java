package jeranvier.ui.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jeranvier.math.timeseries.Timeseries;
import jeranvier.math.timeseries.TimeseriesCollection;

public class ChartFrame<X, Y> extends JFrame{
	private static final long serialVersionUID = -0L;
	private Chart<X,Y> chart;
	
	public ChartFrame(JPanel prettyChart){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(800, 500));
		this.getContentPane().add(prettyChart);
	}
	
	public static void main(String args[]){
		int NUMBER_OF_POINTS = 600000; //per timeseries
		TimeseriesCollection.Builder tscb = new TimeseriesCollection.Builder();
		Timeseries.Builder tsb = new Timeseries.Builder();
		for(double i = 0; i <NUMBER_OF_POINTS; i++){
			tsb.put((long) i, Math.sqrt(i));
		}
		tscb.putTimeseries("a", tsb.build());
		
		tsb = new Timeseries.Builder();
		for(double i = 0; i <NUMBER_OF_POINTS; i++){
			tsb.put((long) i, 10*Math.cos(i/10));
		}
		tscb.putTimeseries("b", tsb.build());
		Chart<Long, Double> chart = new TimeChart(tscb.build());
		PrettyChart<Long, Double> prettyChart = new PrettyChart<Long, Double>(chart);
		System.out.println("built");
		ChartFrame<Long, Double> cf = new ChartFrame<>(prettyChart);
		cf.setLocation(200, 200);
		cf.pack();
		cf.revalidate();
		cf.setVisible(true);
	}

}
