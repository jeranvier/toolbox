package jeranvier.ui.chart;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;

import jeranvier.math.timeseries.Timeseries;
import jeranvier.math.timeseries.TimeseriesCollection;

public class TimeChart extends Chart<Long, Double>{

	private static final long serialVersionUID = 1L;

	public TimeChart(TimeseriesCollection tsc) {
		super(tsc.getLabelsData(), tsc.getTimeSeries());
	}
	
	public TimeChart(String label, Timeseries ts) {
		super(label, ts);
		
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
	protected void drawSeries(Graphics2D g2d) {
		int i = 0;
		for(SortedMap<Long, Double> series:this.data.values()){
			g2d.setColor(getColor(i));
			Point2D previousPixel = new Point2D.Double(0, 0);
			boolean previousVisible = true;
			Point2D data = new Point2D.Double();
			Point2D pixel = new Point2D.Double();
			boolean firstPoint = true;
			Set<Entry<Long, Double>> submap = series.subMap((long)(currentSpace.getFieldOfView().getMinX()), (long)(currentSpace.getFieldOfView().getMaxX())).entrySet();
			for(Entry<Long, Double> datapoint : submap){
				data.setLocation(datapoint.getKey().doubleValue(), datapoint.getValue().doubleValue());
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
					displayHighlightedPoint(g2d, datapoint.getKey(), datapoint.getValue());
				}
				
				if(Math.abs(x-previousX) > MIN_DISTANCE_BETWEEN_POINTS || Math.abs(y-previousY) > MIN_DISTANCE_BETWEEN_POINTS){
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
	
	@Override
	protected void drawMarkers(Graphics2D g2d) {
		int i = this.data.size();
		for(SortedMap<Long, Double> marker : this.markers.values()){
			g2d.setColor(getColor(i));
			Point2D data = new Point2D.Double();
			Point2D pixel = new Point2D.Double();
			for(Entry<Long, Double> datapoint : marker.subMap((long)(currentSpace.getFieldOfView().getMinX()), (long)(currentSpace.getFieldOfView().getMaxX())).entrySet()){
				data.setLocation(datapoint.getKey().doubleValue(), datapoint.getValue().doubleValue());
				currentSpace.spaceToPixel(data, pixel);
				MarkersDrawer.getMarker(i).draw(g2d, pixel, MARKER_SIZE);
			}
			i++;
		}
	}
		
}
