package jeranvier.ui.chart.axis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.text.Format;

import javax.swing.JPanel;

import jeranvier.ui.chart.ChartListener;
import jeranvier.ui.chart.Space;

public abstract class Axis extends JPanel implements ChartListener{
	
	private static final long serialVersionUID = 1L;
	protected Space space;
	protected Format formater;
	protected static final int NUMBER_OF_TICKS = 5;
	protected static final Color BACKGROUND = Color.white;

	public static final int AXIS_SIZE = 30; //in px

	public Axis(Format formater){
		this.setPreferredSize(new Dimension(20, 20));
		this.setFont(new Font("Arial",Font.PLAIN, 10));
		this.formater = formater;
	}	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -this.getHeight());
		g2d.transform(tx);
		g2d.setColor(BACKGROUND);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2d.setColor(Color.BLACK);
	}
	
	@Override
	public void notify(Space space) {
		this.space = space;
		this.repaint();
	}

}
