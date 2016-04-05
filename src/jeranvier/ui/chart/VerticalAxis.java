package jeranvier.ui.chart;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.Format;

public class VerticalAxis extends Axis{

	private static final long serialVersionUID = 1L;


	public VerticalAxis(Format formater){
		super(formater);
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		Point2D minY = new Point2D.Double();
		Point2D maxY = new Point2D.Double();
		
		Rectangle2D fov = space.getFieldOfView();
		space.spaceToPixel(new Point2D.Double(0, fov.getMinY()), minY);
		space.spaceToPixel(new Point2D.Double(0, fov.getMaxY()), maxY);
		g2d.drawLine(Axis.AXIS_SIZE-1, (int)minY.getY(), Axis.AXIS_SIZE-1, (int)maxY.getY());
		
		double diff = fov.getMaxY() - fov.getMinY();
		double orderOfMagnitude = Math.pow(10, Math.floor(Math.log10(diff)));
		double tick = Math.floor(fov.getMinY()/orderOfMagnitude)*orderOfMagnitude;
		Point2D pixel = new Point2D.Double();
		
		int height = this.getHeight();
		
		while(tick <= fov.getMaxY()){
			space.spaceToPixel(new Point2D.Double(0, tick), pixel);
			g2d.drawLine(Axis.AXIS_SIZE/2, (int)pixel.getY(), Axis.AXIS_SIZE-1, (int)pixel.getY());
			g2d.scale(1, -1);
			g2d.translate(0, -height);
			if(orderOfMagnitude >1){
				g2d.drawString(""+(int)tick, 0, (int)(height-pixel.getY()-5));
			}else{
				g2d.drawString(""+tick, 0, (int)(height-pixel.getY()-5));

			}
			g2d.scale(1, -1);
			g2d.translate(0, -height);
			tick += orderOfMagnitude;
		}
	}

}
