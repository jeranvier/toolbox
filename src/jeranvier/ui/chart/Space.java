package jeranvier.ui.chart;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;

public class Space {
	

	private AffineTransform dataTransform; //data to zoom and translated data
	private AffineTransform pixelTransform; //data to pixel
	private Dimension pixelDim;
	private Point2D dataMax;
	private Point2D dataMin;
	private Rectangle2D fieldOfView;
	public static final float ZOOM_FACTOR = 1.5f;
	
	public Space(Point2D min, Point2D max, Dimension dim){
		this.pixelDim = new Dimension (1, 1); //don't use the initial dimension of the window as it is 0x0 ( before being rendered)
		this.fieldOfView = new Rectangle2D.Double();
		this.dataMin = min;
		this.dataMax = max;
		pixelTransform = new AffineTransform();
		dataTransform = new AffineTransform();
		dataTransform.translate(-this.dataMin.getX(), -this.dataMin.getY());
		updateViewField();
	}
	
	public Space(Space s) {
		this(s.dataMin, s.dataMax, s.pixelDim);
	}
	
	public Point2D getDataMin(){
		return this.dataMin;
	}
	
	public Point2D getDataMax(){
		return this.dataMax;
	}

	private void updateViewField() {
		Point2D min = new Point2D.Double();
		Point2D max = new Point2D.Double();
		pixelToSpace(new Point2D.Double(0, 0), min);
		pixelToSpace(new Point2D.Double(this.pixelDim.getWidth(), this.pixelDim.getHeight()), max);
		fieldOfView = new Rectangle2D.Double(min.getX(), min.getY(), max.getX()-min.getX(), max.getY()- min.getY());
	}

	public void zoom(double zoom, Double pixel) throws NoninvertibleTransformException {
		if(zoom<0){
			zoom(ZOOM_FACTOR, ZOOM_FACTOR, pixel); //zoom in
		}else{
			zoom(1.0/ZOOM_FACTOR, 1.0/ZOOM_FACTOR, pixel); //zoom out
		}
		
	}
	
	public void zoom(double zoomX, double zoomY, Point2D pixel) throws NoninvertibleTransformException{
		Point2D dataPoint = new Point2D.Double();
		pixel = new Point2D.Double(pixel.getX(), this.pixelDim.getHeight()-pixel.getY());
		
		AffineTransform transform = new AffineTransform(pixelTransform);
		transform.concatenate(dataTransform);
		transform.createInverse().transform(pixel, dataPoint);

		
		double dx = dataPoint.getX() * (1/zoomX-1); //delta to compensate the zoom
		double dy = dataPoint.getY() * (1/zoomY-1); //delta to compensate the zoom
		
		
		dataTransform.scale(zoomX, zoomY);
		dataTransform.translate(dx, dy);
		
		updateViewField();
	}
	
	public void horizontalZoom(double zoom, Point2D pixel) throws NoninvertibleTransformException{
		if(zoom<0){
			zoom(ZOOM_FACTOR, 1, pixel); //zoom in	
		}else{
			zoom(1.0/ZOOM_FACTOR, 1, pixel); //zoom out
		}
	}
	
	public void verticalZoom(double zoom, Point2D pixel) throws NoninvertibleTransformException{
		if(zoom<0){
			zoom(1, ZOOM_FACTOR, pixel); //zoom in
		}else{
			zoom(1, 1.0/ZOOM_FACTOR, pixel); //zoom out
		}
	}

	public void updateSize(Dimension dim) {
		this.pixelDim = dim;
		pixelTransform.setToIdentity();
		pixelTransform.scale(
				((double)this.pixelDim.width)/(this.dataMax.getX() - this.dataMin.getX()),
				((double)this.pixelDim.height)/(this.dataMax.getY() - this.dataMin.getY()));
		updateViewField();
	}
	
	public void updateTranslation(Point2D delta) throws NoninvertibleTransformException{
		Point2D dataDelta = new Point2D.Double();
		pixelTransform.createInverse().transform(delta, dataDelta);
		dataDelta = new Point2D.Double(dataDelta.getX()/dataTransform.getScaleX(), dataDelta.getY()/dataTransform.getScaleY());
		dataTransform.translate(dataDelta.getX(), -dataDelta.getY());
		updateViewField();
	}
	
	public Point2D spaceToPixel(Point2D data, Point2D pixel){
		AffineTransform transform = new AffineTransform(pixelTransform);
		transform.concatenate(dataTransform);
		return transform.transform(data, pixel);
	}
	
	public Point2D pixelToSpace(Point2D pixel, Point2D data){
		AffineTransform transform = new AffineTransform(pixelTransform);
		transform.concatenate(dataTransform);
		AffineTransform invertedTransform;
		try {
			invertedTransform = transform.createInverse();
			invertedTransform.transform(pixel, data);
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public boolean contains(Point2D point){
		return this.fieldOfView.contains(point);
	}

	public Rectangle2D getFieldOfView() {
		return fieldOfView;
	}

}
