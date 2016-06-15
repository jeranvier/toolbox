package jeranvier.ui.chart.legend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JComponent;

public class LineSample extends JComponent{
	
	public final Color color;
	public final Stroke stroke;
	
	public LineSample(Color color, Stroke stroke){
		this.color = color;
		this.stroke = stroke;
		this.setSize(20, 20);
		this.setPreferredSize(new Dimension(20, 20));
		this.setMinimumSize(new Dimension(20, 20));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(color);
		g2d.setStroke(stroke);
		g2d.drawLine(0,10, 20, 10);
	}
	
	

}
