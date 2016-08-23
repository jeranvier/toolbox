package jeranvier.ui.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import jeranvier.math.timeseries.Timeseries;
import jeranvier.math.timeseries.TimeseriesCollection;

public class ChartFrame<X extends Number, Y extends Number> extends JFrame{
	private static final long serialVersionUID = -0L;
	
	public ChartFrame(PrettyChart<X, Y> prettyChart){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setPreferredSize(screenSize);
		KeyboardController keyboardController = new KeyboardController(prettyChart.getChart());
		this.addKeyListener(keyboardController);
		this.getContentPane().add(prettyChart);
	}
	
	public static void main(String args[]){
		int NUMBER_OF_POINTS = 6000; //per timeseries
		TimeseriesCollection.Builder tscb = new TimeseriesCollection.Builder();
		Timeseries.Builder tsb = new Timeseries.Builder();
		for(double i = -NUMBER_OF_POINTS/2; i <NUMBER_OF_POINTS/2; i++){
			double v = Math.exp(-i*i/(2*300*300))/(300*Math.sqrt(2*Math.PI));
			tsb.put((long) i, v);
		}
		tscb.putTimeseries("normal 1", tsb.build());
		
		tsb = new Timeseries.Builder();
		for(double i = -NUMBER_OF_POINTS/2; i <NUMBER_OF_POINTS/2; i++){
			double v = Math.exp(-i*i/(2*200*200))/(200*Math.sqrt(2*Math.PI));
			tsb.put((long) i, v);
		}
		tscb.putTimeseries("normal 2", tsb.build());
		
		tsb = new Timeseries.Builder();
		for(double i = -NUMBER_OF_POINTS/2; i <NUMBER_OF_POINTS/2; i++){
			double v = Math.cos(i/300)/1000;
			tsb.put((long) i, v);
		}
		tscb.putTimeseries("cos(x)", tsb.build());
		
		tsb = new Timeseries.Builder();
		for(double i = -NUMBER_OF_POINTS/20; i <NUMBER_OF_POINTS/20; i++){
			double v = Math.tanh(i/300)/1000;
			tsb.put((long) i, v);
		}
		Timeseries marker = tsb.build();
		

		Chart<Long, Double> chart = new TimeChart(tscb.build());
		chart.addMarker("tanh", marker);
		PrettyChart<Long, Double> prettyChart = new PrettyChart<Long, Double>(chart);
		ChartFrame<Long, Double> cf = new ChartFrame<>(prettyChart);
		cf.setLocation(200, 200);
		cf.pack();
		cf.revalidate();
		cf.setVisible(true);
	}

	public void display() {
		setLocation(200, 200);
		pack();
		revalidate();
		repaint();
		setVisible(true);		
	}

}
