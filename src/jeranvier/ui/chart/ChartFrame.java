package jeranvier.ui.chart;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jeranvier.math.timeseries.Timeseries;
import jeranvier.math.timeseries.TimeseriesCollection;

public class ChartFrame<X extends Number, Y extends Number> extends JFrame{
	private static final long serialVersionUID = -0L;
	
	public ChartFrame(JPanel prettyChart){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(800, 500));
		this.getContentPane().add(prettyChart);
	}
	
	public static void main(String args[]){
		int NUMBER_OF_POINTS = 60000; //per timeseries
		TimeseriesCollection.Builder tscb = new TimeseriesCollection.Builder();
		Timeseries.Builder tsb = new Timeseries.Builder();
		for(double i = -NUMBER_OF_POINTS/2; i <NUMBER_OF_POINTS/2; i++){
			double v = Math.exp(-i*i/(2*3000*3000))/(3000*Math.sqrt(2*Math.PI));
			tsb.put((long) i, v);
		}
		tscb.putTimeseries("a", tsb.build());
		
		tsb = new Timeseries.Builder();
		for(double i = -NUMBER_OF_POINTS/2; i <NUMBER_OF_POINTS/2; i++){
			double v = Math.exp(-i*i/(2*2000*2000))/(2000*Math.sqrt(2*Math.PI));
			tsb.put((long) i, v);
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

	public void display() {
		setLocation(200, 200);
		pack();
		revalidate();
		repaint();
		setVisible(true);		
	}

}
