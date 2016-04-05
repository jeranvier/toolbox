package jeranvier.ui.chart;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public interface Marker {
	
	public void draw(Graphics2D g, Point2D center, int size);

}
