package jeranvier.ui.chart;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseController implements MouseListener, MouseMotionListener, MouseWheelListener {

	private Chart<?, ?> chart;
	private int refX;
	private int refY;

	public MouseController(Chart<?, ?> chart) {
		this.chart = chart;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	chart.drag(e.getX()-refX, e.getY()-refY);
	refX = e.getX();
	refY = e.getY();
	}
	
	

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.isShiftDown()) {
			chart.horizontalZoom(e.getWheelRotation(), e.getX(), e.getY());
		}else if(e.isMetaDown()){
			chart.verticalZoom(e.getWheelRotation(), e.getX(), e.getY());
		}else{
        	chart.zoom(e.getWheelRotation(), e.getX(), e.getY());
        }
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		refX = e.getX();
		refY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	@Override
	public void mouseExited(MouseEvent e) {		
	}

}