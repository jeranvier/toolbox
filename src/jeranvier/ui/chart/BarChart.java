package jeranvier.ui.chart;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.text.Format;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.TreeMap;


public class BarChart extends Chart<Double, Double>{

	private static final long serialVersionUID = 1L;

	public BarChart(String label, SortedMap<Double, Double> data) {
		super(label, data);
		
	}
	
	public BarChart(String label, Double[][] data) {
		super(label, BarChart.convertData(data));
	}

	private static SortedMap<Double, Double> convertData(Double[][] data) {
		SortedMap<Double,Double> map = new TreeMap<>();
		for(Double[] datum : data){
			map.put(datum[0], datum[1]);
		}
		return map;
	}

	@Override
	public Format verticalAxisFormater() {
		return new NumberFormat();
	}

	@Override
	public Format horizontalAxisFormater() {
		return new NumberFormat();
	}
	
	@Override
	protected void drawSeries(Graphics2D g2d) {
		int i = 0;
		for(Entry<String, SortedMap<Double, Double>> series:this.data.entrySet()){
			g2d.setColor(getColor(i));
			Point2D data = new Point2D.Double();
			Point2D pixel = new Point2D.Double();
			data.setLocation(0.0, 0.0);
			currentSpace.spaceToPixel(data, pixel);
			int zeroX = (int) pixel.getX();
			int zeroY = (int) pixel.getY();
			Set<Entry<Double, Double>> submap = series.getValue().subMap(currentSpace.getFieldOfView().getMinX(), currentSpace.getFieldOfView().getMaxX()).entrySet();

			double smallestInterval = Double.POSITIVE_INFINITY;
			Entry<Double, Double> previousPoint = null;
			for(Entry<Double, Double> datapoint : submap){
				if(previousPoint!= null){
					double interval = datapoint.getKey() - previousPoint.getKey();
					if(smallestInterval > interval){
						smallestInterval = interval;
					}
					
				}
				previousPoint = datapoint;
			}
			
			data.setLocation(smallestInterval, 0.0);
			currentSpace.spaceToPixel(data, pixel);
			int pixelWidth = (int)((pixel.getX()-zeroX)*0.9);
			
			for(Entry<Double, Double> datapoint : submap){
				data.setLocation(datapoint.getKey().doubleValue(), datapoint.getValue().doubleValue());
				currentSpace.spaceToPixel(data, pixel);
				int x = (int) pixel.getX();
				int y = (int) pixel.getY();
				if(datapoint.getValue()>=0){
					g2d.fillRect(x-pixelWidth/2, zeroY, pixelWidth, y-zeroY);
				}
				else{
					g2d.fillRect(x-pixelWidth/2, y, pixelWidth, zeroY-y);
				}
				g2d.fillOval(x-4, zeroY-4, 8, 8);
				
			}
			i++;
		}
		
	}
	
	@Override
	protected void computeBoundaries() {
		Double minX = Double.POSITIVE_INFINITY;
		Double maxX = Double.NEGATIVE_INFINITY;
		Double minY = 0.0;
		Double maxY = Double.NEGATIVE_INFINITY;
		
		for(SortedMap<Double, Double> series : this.data.values()){
			for(Entry<Double,Double> entry : series.entrySet()){
				minX = Math.min(minX, entry.getKey().doubleValue());
				maxX = Math.max(maxX, entry.getKey().doubleValue());
				minY = Math.min(minY, entry.getValue().doubleValue());
				maxY = Math.max(maxY, entry.getValue().doubleValue());
			}
		}
		
		//compute margins
		double height = maxY - minY;
		double width  = maxX - minX;
		double marginHeight = height * Chart.DATA_SPACE_MARGINS;
		double marginWidth = width * Chart.DATA_SPACE_MARGINS;
		this.totalSpace = new Space(new Point2D.Double(minX-marginWidth, minY- marginHeight), new Point2D.Double(maxX + marginWidth, maxY + marginHeight), this.getSize());
}
	
	public static void main(String[] args){
		int size = 20;
		Double[][] data = new Double[size][];
		for(int i = 0; i<size; i++){
			data[i] = new Double[]{(double) i,Math.random()*100*(Math.random()>0.8?-1.0:1.0)}; 
		}
		BarChart bc = new BarChart("data", data);
		new ChartFrame<>(new PrettyChart<>(bc)).display();;
	}
		
}

