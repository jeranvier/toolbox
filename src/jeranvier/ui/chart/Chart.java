package jeranvier.ui.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.text.Format;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Chart <X extends Number, Y extends Number> extends JPanel{
	//CONSTANTS
	protected static final double DATA_SPACE_MARGINS = 0.05; //in percentage
	public static final int MARKER_SIZE = 4; //in px
	protected static final int MIN_DISTANCE_BETWEEN_POINTS = 1; //in px
	protected static final Stroke STROKE = new BasicStroke(2);
	protected static final Color[] colors = new Color[]{
		Color.decode("#7B1FA2"),
		Color.decode("#FFAB00"),
		Color.decode("#880E4F"),
		Color.decode("#4A148C"),
		Color.decode("#1B5E20"),
		Color.decode("#006064")};

	
	//DATA
	protected Map<String, SortedMap<X, Y>> markers;
	protected Map<String, SortedMap<X, Y>> data;
	protected Entry<X, Y> highlightedDataPoint;
	
	//SPACES
	protected Space currentSpace;
	protected Space totalSpace;
	
	//AXIS
	protected Format verticalAxisFormater;
	protected Format horizontalAxisFormater;

	//SYSMTE INTERACTION
	private List<ChartListener> chartListeners;
	private List<ChartDataListener<X,Y>> chartDataListeners;
	protected static final ClipboardHandler clipboardHandler = new ClipboardHandler();
	
	public Chart(){
		this.chartListeners = new LinkedList<>();
		this.chartDataListeners = new LinkedList<>();
		MouseController mouseController = new MouseController(this);
		this.addMouseMotionListener(mouseController);
		this.addMouseWheelListener(mouseController);
		this.addMouseListener(mouseController);
		this.verticalAxisFormater = new NumberFormat();
		this.horizontalAxisFormater = new NumberFormat();
		this.markers = new HashMap<>();
	}
	
	
	public Chart(Map<String, Integer> labels, Map<Integer,SortedMap<X, Y>> data){
		this();
		this.data = new TreeMap<>();
		for(Entry<String,Integer> label: labels.entrySet()){
			addData(label.getKey(), data.get(label.getValue()));
		} 
	}

	public Chart(String label, SortedMap<X, Y> ts) {
		this();
		this.data = new TreeMap<>();
		this.addData(label, ts);
	}
	
	public void addData(String label, SortedMap<X, Y> data){
		this.data.put(label, data);
		computeBoundaries();
		this.notifyChartDataListeners();
		resetView();
	}
	
	public void addMarker(String label, SortedMap<X, Y> marker){
		this.markers.put(label, marker);
		this.notifyChartDataListeners();
	}


	public void resetView() {
		currentSpace = new Space(totalSpace);
		this.repaint();
	}

	public void zoom(double delta, int x, int y) {
		try {
			currentSpace.zoom(delta, new Point2D.Double(x, y));
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		this.repaint();
	}
	
	public void horizontalZoom(double delta, int x, int y) {
		try {
			currentSpace.horizontalZoom(delta, new Point2D.Double(x, y));
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		this.repaint();
	}
	
	public void verticalZoom(double delta, int x, int y) {
		try {
			currentSpace.verticalZoom(delta, new Point2D.Double(x, y));
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		this.repaint();
	}
	
	public void addChartListener(ChartListener listener){
		this.chartListeners.add(listener);
	}
	
	public void addChartDataListener(ChartDataListener<X,Y> listener){
		this.chartDataListeners.add(listener);
	}
	
	public void notifyChartListeners(){
		for(ChartListener cl : chartListeners){
			cl.notify(currentSpace);
		}
	}
	
	public void notifyChartDataListeners(){
		for(ChartDataListener<X,Y> cdl : chartDataListeners){
			cdl.notify(this.markers, this.data);
		}
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		currentSpace.updateSize(this.getSize());
		notifyChartListeners();
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -this.getHeight());
		g2d.transform(tx);	
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
		g2d.setStroke(Chart.STROKE);
		
		drawSeries(g2d);	
		drawMarkers(g2d);	
	}
	
	protected void drawMarkers(Graphics2D g2d) {
		int i = this.data.size();
		for(Map<X, Y> marker : this.markers.values()){
			g2d.setColor(getColor(i));
			Point2D data = new Point2D.Double();
			Point2D pixel = new Point2D.Double();
			for(Entry<X, Y> datapoint : marker.entrySet()){
				data.setLocation(datapoint.getKey().doubleValue(), datapoint.getValue().doubleValue());
				currentSpace.spaceToPixel(data, pixel);
				MarkersDrawer.getMarker(i).draw(g2d, pixel, MARKER_SIZE);
			}
			i++;
		}
	}

	protected void drawSeries(Graphics2D g2d) {
		
		int i = 0;
		for(SortedMap<X, Y> series:data.values()){
			g2d.setColor(getColor(i));
			Point2D previousPixel = new Point2D.Double(0, 0);
			boolean previousVisible = true;
			Point2D data = new Point2D.Double();
			Point2D pixel = new Point2D.Double();
			boolean firstPoint = true;
			for(Entry<X, Y> datapoint : series.entrySet()){
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


	protected void displayHighlightedPoint(Graphics2D g2d, X x, Y y) {
		g2d.scale(1, -1);
		g2d.translate(0, -this.getHeight());
		g2d.drawString("x: "+this.horizontalAxisFormater().format(x), this.getWidth()-200, 20);
		g2d.drawString("y: "+this.verticalAxisFormater().format(y), this.getWidth()-200, 50);
		g2d.scale(1, -1);
		g2d.translate(0, -this.getHeight());
	}

	public void drag(int dx, int dy){
		try {
			currentSpace.updateTranslation(new Point2D.Double(dx, dy));
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		this.repaint();
	}

	public Format verticalAxisFormater(){
		return verticalAxisFormater;
	};

	public Format horizontalAxisFormater(){
		return horizontalAxisFormater;
	}

	public void highlightClosestDataPoint(int x, int y) {
		Point2D.Double pixelPoint = new Point2D.Double(x, this.getHeight()-y);
		Point2D.Double dataPoint = new Point2D.Double();
		currentSpace.pixelToSpace(pixelPoint, dataPoint);
		
		Map<Entry<X, Y>, Double> distances = new HashMap<>();
		
		for(Map<X, Y> series : data.values()){
			Entry<X, Y> entry = findFloor(series, dataPoint.getX());
			if(entry!= null){
				double distance = Math.abs(entry.getValue().doubleValue() - dataPoint.getY());
				distances.put(entry,  distance);
			}
		}
		
		double minDistance = Double.POSITIVE_INFINITY;
		Entry<X, Y> winner = null;
		for(Entry<Entry<X, Y>, Double> distanceEntry : distances.entrySet()){
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
	
	public static Color getColor(int i) {
		return Chart.colors[i%Chart.colors.length];
	}
	
	public static Stroke getStroke(int i) {
		return Chart.STROKE;
	}
	
	private void computeBoundaries() {
		Double minX = Double.POSITIVE_INFINITY;
		Double maxX = Double.NEGATIVE_INFINITY;
		Double minY = Double.POSITIVE_INFINITY;
		Double maxY = Double.NEGATIVE_INFINITY;
		for(Map<X, Y> series : this.data.values()){
			for(Entry<X,Y> entry : series.entrySet()){
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
	
	private Entry<X, Y> findFloor(Map<X, Y> series, double x) {
		Entry<X, Y> latestEntry = null;
		for(Entry<X, Y> entry: series.entrySet()){
			if(entry.getKey().doubleValue()<x){
				latestEntry = entry;
			}else{
				break;
			}
		}
		return latestEntry;
	}

	public static final class ClipboardHandler implements ClipboardOwner {

		@Override
		public void lostOwnership(Clipboard clipboard, Transferable contents) {
		}
		
		public void setClipboardContent(String string){
		    StringSelection stringSelection = new StringSelection(string);
		    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		    clipboard.setContents(stringSelection, this);
		  }
		
	}
}
