package jeranvier.ui.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import jeranvier.math.timeseries.Timeseries;
import jeranvier.math.timeseries.TimeseriesCollection;

public class TimeChart extends Chart<Long, Double>{

	private static final long serialVersionUID = 1L;
	private static final Color BACKGROUND = Color.white;
	private int MIN_DISTANCE_BETWEEN_POINTS = 3; //in px
	private SimpleDateFormat dateParser;
	private Entry<Long, Double> highlightedDataPoint;

	public TimeChart(String label, Timeseries model){
		this.data.put(label,  model);
		dateParser = new SimpleDateFormat("HH:mm:ss.SSS");
		computeBoundaries();
		resetView();
	}
	
	

	public TimeChart(TimeseriesCollection model){
		for(String label : model.getLabels()){
			this.data.put(label,  model.getTimeSeries(label));
		}
		computeBoundaries();
		resetView();
	}

	private void resetView() {
		currentSpace = totalSpace;
	}

	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
		        RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
		g2d.setStroke(Chart.stroke);
		
		int i = 0;
		for(TreeMap<Long, Double> series : this.data.values()){
			g2d.setColor(Chart.colors[i%Chart.colors.length]);
			Point2D previousPixel = new Point2D.Double(0, 0);
			boolean previousVisible = true;
			Point2D data = new Point2D.Double();
			Point2D pixel = new Point2D.Double();
			boolean firstPoint = true;
			for(Entry<Long, Double> datapoint : series.entrySet()){
				data.setLocation(datapoint.getKey(), datapoint.getValue());
				currentSpace.spaceToPixel(data, pixel);
				int x = (int) pixel.getX();
				int y = (int) pixel.getY();
				int previousX = (int) previousPixel.getX();
				int previousY = (int) previousPixel.getY();

				if(!currentSpace.contains(data)){ //If the point is not visible, we may not have to process it
					if(!previousVisible){ //we don't even have to render the line between this point and the previous one	
						previousVisible = false;
						previousPixel = new Point2D.Double(pixel.getX(), pixel.getY()); //we keep it to display a previous point if needed
						continue; //we move to he next point to render
					}else{ //it is not visible but we render it to draw the line out of the field of view
						previousVisible = true;
					}
				}
				
				if(!previousVisible){
					firstPoint = false;
				}
				
				if(datapoint.equals(highlightedDataPoint)){
					g2d.fillOval(x-4, y-4, 8, 8);
					displayHighlightedPoint(g2d, datapoint);
				}
				
				if(Math.abs(x-previousX) > MIN_DISTANCE_BETWEEN_POINTS || Math.abs(y-previousY) > MIN_DISTANCE_BETWEEN_POINTS){
//					g2d.fillOval(x-2, y-2, 4, 4);
					if(firstPoint){
						firstPoint = false;
					}else{
						g2d.drawLine(previousX, previousY, x, y);
					}
					previousPixel = new Point2D.Double(pixel.getX(), pixel.getY());
					previousVisible = true;
				}
			}	
			i++;
		}
	}
	
	private void displayHighlightedPoint(Graphics2D g2d, Entry<Long, Double> datapoint) {
		g2d.scale(1, -1);
		g2d.translate(0, -this.getHeight());
		g2d.drawString("x: "+datapoint.getKey(), this.getWidth()-200, 20);
		g2d.drawString("y: "+datapoint.getValue(), this.getWidth()-200, 50);
		g2d.scale(1, -1);
		g2d.translate(0, -this.getHeight());
	}



	private void computeBoundaries() {
		Double minX = Double.POSITIVE_INFINITY;
		Double maxX = Double.NEGATIVE_INFINITY;
		Double minY = Double.POSITIVE_INFINITY;
		Double maxY = Double.NEGATIVE_INFINITY;
		for(TreeMap<Long, Double> series : this.data.values()){
			minX = Math.min(minX, series.firstKey());
			maxX = Math.max(maxX, series.lastKey());
			for(Double value : series.values()){
				minY = Math.min(minY, value);
				maxY = Math.max(maxY, value);
			}
		}
		
		//compute margins
		double height = maxY - minY;
		double width  = maxX - minX;
		double marginHeight = height * Chart.DATA_SPACE_MARGINS;
		double marginWidth = width * Chart.DATA_SPACE_MARGINS;
		this.totalSpace = new Space(new Point2D.Double(minX-marginWidth, minY- marginHeight), new Point2D.Double(maxX + marginWidth, maxY + marginHeight), this.getSize());
}



	@Override
	public Format verticalAxisFormater() {
		return new NumberFormat();
	}



	@Override
	public Format horizontalAxisFormater() {
		return new SimpleDateFormat("HH:mm:ss.SSS");
	}
	
	@Override
	public void highlightClosestDataPoint(int x, int y) {
		Point2D.Double pixelPoint = new Point2D.Double(x, this.getHeight()-y);
		Point2D.Double dataPoint = new Point2D.Double();
		currentSpace.pixelToSpace(pixelPoint, dataPoint);
		
		Map<Entry<Long, Double>, Double> distances = new HashMap<>();
		
		for(TreeMap<Long, Double> series : data.values()){
			Entry<Long, Double> entry = series.floorEntry((long) dataPoint.getX());
			if(entry!= null){
				double distance = Math.abs(entry.getValue() - dataPoint.getY());
				distances.put(entry,  distance);
			}
		}
		
		double minDistance = Double.POSITIVE_INFINITY;
		Entry<Long, Double> winner = null;
		for(Entry<Entry<Long, Double>, Double> distanceEntry : distances.entrySet()){
			if(distanceEntry.getValue() < minDistance){
				minDistance = distanceEntry.getValue();
				winner = distanceEntry.getKey();
			}
		}
		
		if(winner != null){
			highlightedDataPoint = winner;
			Chart.clipboardHandler.setClipboardContent(winner.getKey()+"\t"+winner.getValue());
		}else{
			highlightedDataPoint = null;
		}
		this.repaint();
	}
}
