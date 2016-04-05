package jeranvier.ui.chart;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class MarkersDrawer{
	
	private static final Marker[] markers = new Marker[]{
		new Box(),
		new Square(),
		new Circle(),
		new Disk(),
		new Cross()
	};
	
	public static Marker getMarker(int i){
		return markers[i%markers.length];
	}


	private static final class Box implements Marker{
		public void draw(Graphics2D g, Point2D center, int radius) {
			g.drawRect((int)center.getX()-radius, (int)center.getY()-radius, 2*radius, 2*radius);
		}
	}
	
	private static final class Square implements Marker{
		public void draw(Graphics2D g, Point2D center, int radius) {
			g.fillRect((int)center.getX()-radius, (int)center.getY()-radius, 2*radius, 2*radius);
		}
	}
	
	private static final class Circle implements Marker{
		public void draw(Graphics2D g, Point2D center, int radius) {
			g.drawOval((int)center.getX()-radius, (int)center.getY()-radius, 2*radius, 2*radius);
		}
	}
	
	private static final class Disk implements Marker{
		public void draw(Graphics2D g, Point2D center, int radius) {
			g.fillOval((int)center.getX()-radius, (int)center.getY()-radius, 2*radius, 2*radius);
		}
	}
	
	private static final class Cross implements Marker{
		public void draw(Graphics2D g, Point2D center, int radius) {
			g.drawLine((int)center.getX()-radius, (int)center.getY(),(int)center.getX()+radius, (int)center.getY());			
			g.drawLine((int)center.getX(), (int)center.getY()-radius,(int)center.getX(), (int)center.getY()+radius);
		}
	}
}
