package jeranvier.ui.chart.axis;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.Format;

public class HorizontalAxis extends Axis{
	
	private static final long serialVersionUID = 1L;

	public HorizontalAxis(Format formater){
		super(formater);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		Point2D minX = new Point2D.Double();
		Point2D maxX = new Point2D.Double();
		
		if(space == null){
			return;
		}
		Rectangle2D fov = space.getFieldOfView();
		space.spaceToPixel(new Point2D.Double(fov.getMinX(), 0), minX);
		space.spaceToPixel(new Point2D.Double(fov.getMaxX(), 0), maxX);
		g2d.drawLine((int)minX.getX(), Axis.AXIS_SIZE-1, (int)maxX.getX(), Axis.AXIS_SIZE-1);
		
		double diff = fov.getMaxX() - fov.getMinX();
		double orderOfMagnitude = Math.pow(10, Math.floor(Math.log10(diff)));
		double tick = Math.floor(fov.getMinX()/orderOfMagnitude)*orderOfMagnitude;
		Point2D pixel = new Point2D.Double();
		
		while(tick <= fov.getMaxX()){
			space.spaceToPixel(new Point2D.Double(tick, 0), pixel);
			g2d.drawLine((int)pixel.getX(), Axis.AXIS_SIZE/2,(int)pixel.getX(), Axis.AXIS_SIZE-1);
			g2d.scale(1, -1);
			g2d.translate(0, -this.getHeight());
			if(orderOfMagnitude >1){
				g2d.drawString(formater.format(tick), (int) pixel.getX()+1, 12);
			}else{
				g2d.drawString(formater.format(tick), (int) pixel.getX()+1, 12);
			}
			g2d.scale(1, -1);
			g2d.translate(0, -this.getHeight());
			tick += orderOfMagnitude;
		}
	}
	
	

}
