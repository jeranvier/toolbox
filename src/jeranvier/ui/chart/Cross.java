package jeranvier.ui.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.JPanel;

public class Cross extends JPanel{

	private static final long serialVersionUID = 1L;
	protected static final Stroke STROKE = new BasicStroke(1);
	protected static final Stroke BOLD_STROKE = new BasicStroke(2);
	protected static final Color COLOR = Color.BLACK;
	protected static final int MARGIN = 6; //in px

	
	public Cross(){
		super();
		this.setBackground(Color.WHITE);
		this.setToolTipText("reset view");
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(COLOR);
		g2d.setStroke(STROKE);
		g2d.drawLine(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
		g2d.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
		g2d.setStroke(BOLD_STROKE);
		g2d.drawOval(MARGIN, MARGIN, this.getWidth()-2*MARGIN, this.getHeight()-2*MARGIN);
	}

}
